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
                log.info(String.format("Played %d games so far", i+1));
                gameSession.logStats();
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

        new TrainingSession(new GameConfig()
                .persistFacade(new PersistFacadeBase() {
                    @Override
                    protected LearningTicTacToeApi getApi() {
                        return api;
                    }
                })
                .difficuilty(GameConfig.Difficulty.EASY) //Maximize exploration
        ).startTraining(10000);
    }
}
