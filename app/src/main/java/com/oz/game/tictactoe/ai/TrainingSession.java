package com.oz.game.tictactoe.ai;

import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.oz.game.tictactoe.backend.learningTicTacToeApi.LearningTicTacToeApi;
import com.oz.game.tictactoe.core.GameConfig;
import com.oz.game.tictactoe.core.GameSession;
import com.oz.game.tictactoe.impl.PersistFacadeBase;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by developer on 8/15/15.
 */
public class TrainingSession {
    private static final Logger log = Logger.getLogger(TrainingSession.class.getName());

    private final GameConfig gameConfig;

    private TrainingSession(final GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    private void startTraining(final int numOfGames) {
        if (numOfGames == 1) {
            gameConfig.printState(true);
        }

        for (int i = 0; i < numOfGames; i++) {
            final GameSession gameSession = new GameSession(gameConfig);

            do {
                gameSession.play();
            } while (false == gameSession.isGameOver());

            if ( numOfGames < 10 || (i % 100) == 0 ) {
                log.info(String.format("Played %d game(s) so far", i+1));

                gameSession.logStats();

                if (gameSession.isTrained()) {
                    log.info(String.format("Achieved training goal of %d"
                            , gameConfig.getTrainingGoal()));
                    break; //Stop when training goal is reached, if any set
                }
            }
        }
    }

    public static void main(String[] args) {
        final LearningTicTacToeApi api = new LearningTicTacToeApi.Builder(
                 new NetHttpTransport()
                ,new JacksonFactory()
                ,null)
                .setRootUrl("http://localhost:8080/_ah/api")
                .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                    @Override
                    public void initialize(AbstractGoogleClientRequest<?> gcr) throws IOException {
                        gcr.setDisableGZipContent(true);
                    }
                })
                .build();

        //Maximize exploration during initial learning. Maximize greed after learning.
        new TrainingSession(new GameConfig()
                .persistFacade(new PersistFacadeBase() {
                    @Override
                    protected LearningTicTacToeApi getApi() {
                        return api;
                    }
                })
                .difficulty(GameConfig.Difficulty.BREEZE)
                .trainingGoal(16000)
        ).startTraining(10000);
    }
}
