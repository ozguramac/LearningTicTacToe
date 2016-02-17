package com.oz.game.tictactoe.core;

import com.oz.game.tictactoe.core.io.GameOutput;

import java.text.NumberFormat;
import java.util.logging.Logger;

/**
 * Created by developer on 7/11/15.
 */
public class GameSession {
    private static final Logger log = Logger.getLogger(GameSession.class.getName());

    private final PersistController persistController;
    private final GameHistory history;
    private final GameState state;
    private final Referee referee;

    private final Player player0;
    private final Player playerX;

    private final GameOutput out;

    private final boolean debugState;

    public GameSession(final GameConfig config) {
        referee = new Referee();

        persistController = new PersistController(config.getPersistFacade());

        history = new GameHistory(persistController);
        history.setGreedyMoveThreshold(config.getDifficulty().getThreshold());

        state = new GameState(history);

        playerX = createPlayer(config, GameState.GamePiece.X);
        player0 = createPlayer(config, GameState.GamePiece.O);

        debugState = config.isPrintState();

        out = config.getOutput();
    }

    private static Player createPlayer(GameConfig config, GameState.GamePiece gp) {
        final GameConfig.PlayerType pt;
        switch (gp) {
            case O: pt = config.getPlayerTwo(); break;
            default: pt = config.getPlayerOne(); break;
        }
        switch (pt) {
            case HUMAN: return new HumanPlayer(gp, config.getInput());
            default: return new ComputerPlayer(gp, config.getOutput());
        }
    }

    public boolean isGameOver() {
        return referee.isGameOver(state);
    }

    public void play() {
        if (isGameOver()) {
            return;
        }

        switch (whoseTurn()) {
            case O:
                player0.play(state);
                break;
            case X:
                playerX.play(state);
                break;
            default:
                break;
        }

        if (debugState) {
            state.print(System.out);
        }

        if (isGameOver()) {
            final GameState.GamePiece winner = referee.getWinner(state);
            state.getHistory().persist(winner);

            if (out != null) {
                out.onGameOver(winner.toChar());
            }

            if (debugState) {
                System.out.println("------------------------");
                System.out.println("The winner is " + winner.toChar());
                System.out.println("------------------------");
            }
        }
    }

    private GameState.GamePiece whoseTurn() {
        return referee.whoseTurn(state);
    }

    public void logStats() {
        final NumberFormat percFmt = NumberFormat.getPercentInstance();
        percFmt.setMinimumFractionDigits(4);
        log.info(String.format("Total Count => %d " +
                                " Best move find rate => %s  Exploratory move rate => %s " +
                                " Unplayed find rate => %s  Random move rate => %s "
                        ,GameHistory.getNumOfTotalEntries()
                        ,percFmt.format(GameHistory.getPercOfBestMoveFinds())
                        ,percFmt.format(GameHistory.getPercOfExploratoryMoves())
                        ,percFmt.format(GameHistory.getPercOfUnplayedFinds())
                        ,percFmt.format(GameHistory.getPercOfRandomMoves())
                )
        );
    }
}
