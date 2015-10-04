package com.oz.game.tictactoe.ai;

import com.oz.game.tictactoe.core.GameConfig;
import com.oz.game.tictactoe.core.GameSession;

/**
 * Created by developer on 8/15/15.
 */
public class TrainingSession {
    private final GameConfig gameConfig;

    private TrainingSession(final GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    private void startTraining(final int numOfGames) {
        try {
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
        finally {
            GameSession.cleanUp();
        }
    }

    public static void main(String[] args) {
        new TrainingSession(new GameConfig()
                .dbName("test")
                .playerOne(GameConfig.PlayerType.COMPUTER)
                .playerTwo(GameConfig.PlayerType.COMPUTER)
                .printState(true)
        ).startTraining(1);
    }
}
