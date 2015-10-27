package com.oz.game.tictactoe.ai;

import com.oz.game.tictactoe.core.GameConfig;
import com.oz.game.tictactoe.core.GameSession;
import com.oz.game.tictactoe.impl.PersistFacadeImpl;

/**
 * Created by developer on 8/15/15.
 */
public class TrainingSession {
    private final GameConfig gameConfig;

    private TrainingSession(final GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    private void startTraining(final int numOfGames) {
        for (int i = 0; i < numOfGames; i++) {
            final GameSession gameSession = new GameSession(gameConfig);

            do {
                gameSession.play();
            } while (false == gameSession.isGameOver());

            if ( numOfGames < 10 || (i % (numOfGames/10)) == 0 ) {
                gameSession.logStats();
            }
        }
    }

    public static void main(String[] args) {
        new TrainingSession(new GameConfig()
                .persistFacade(new PersistFacadeImpl())
                .playerOne(GameConfig.PlayerType.COMPUTER)
                .playerTwo(GameConfig.PlayerType.COMPUTER)
                .printState(true)
        ).startTraining(1);
    }
}
