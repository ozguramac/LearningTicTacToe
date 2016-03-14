package com.oz.game.tictactoe.core;

import com.oz.game.tictactoe.core.io.GameInput;
import com.oz.game.tictactoe.core.io.GameOutput;
import com.oz.game.tictactoe.core.persist.PersistFacade;

/**
 * Created by developer on 8/15/15.
 */
public class GameConfig {
    private PlayerType playerOne = PlayerType.COMPUTER, playerTwo = PlayerType.COMPUTER;
    private boolean bPrintState = false;
    private GameInput in;
    private GameOutput out;
    private PersistFacade persistFacade;
    private double difficulty = 0.5d;
    private Integer trainingGoal;

    public GameConfig persistFacade(final PersistFacade persistFacade) {
        this.persistFacade = persistFacade;
        return this;
    }

    public PersistFacade getPersistFacade() {
        return persistFacade;
    }

    public boolean isPrintState() {
        return bPrintState;
    }

    public GameConfig printState(final boolean bPrintState) {
        this.bPrintState = bPrintState;
        return this;
    }

    public GameConfig input(final GameInput in) {
        this.in = in;
        return this;
    }

    public GameInput getInput() {
        return in;
    }

    public GameConfig output(final GameOutput out) {
        this.out = out;
        return this;
    }

    public GameOutput getOutput() {
        return out;
    }

    public PlayerType getPlayerOne() {
        return playerOne;
    }

    public GameConfig playerOne(final PlayerType playerOne) {
        this.playerOne = playerOne;
        return this;
    }

    public PlayerType getPlayerTwo() {
        return playerTwo;
    }

    public GameConfig playerTwo(final PlayerType playerTwo) {
        this.playerTwo = playerTwo;
        return this;
    }

    public double getDifficulty() { return difficulty; }

    public GameConfig difficulty(final double difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public Integer getTrainingGoal() {
        return trainingGoal;
    }

    public GameConfig trainingGoal(final int trainingGoal) {
        this.trainingGoal = trainingGoal;
        return this;
    }

    public static enum PlayerType {
         COMPUTER
        ,HUMAN
    }
}
