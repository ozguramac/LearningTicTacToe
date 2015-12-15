package com.oz.game.tictactoe.ui;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.oz.game.tictactoe.R;
import com.oz.game.tictactoe.backend.learningTicTacToeApi.LearningTicTacToeApi;
import com.oz.game.tictactoe.core.GameConfig;
import com.oz.game.tictactoe.core.GameSession;
import com.oz.game.tictactoe.core.persist.PersistFacade;
import com.oz.game.tictactoe.impl.PersistFacadeBase;
import com.oz.game.tictactoe.impl.PersistFacadeImpl;
import com.oz.game.tictactoe.core.io.GameMove;
import com.oz.game.tictactoe.core.io.GameInput;
import com.oz.game.tictactoe.core.io.GameOutput;
import com.oz.game.tictactoe.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class TicTacToeActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        final View contentView = findViewById(R.id.fullscreen_content);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private GameSession gameSession = null;
    private GameMove lastPlayed = null;
    private String outcomeText = null; //TODO: horrible!
    private Object lock = new Object(); //TODO: ugly!!!

    private final LearningTicTacToeApi.Builder apiBuilder = new LearningTicTacToeApi.Builder(
            AndroidHttp.newCompatibleTransport()
            ,new AndroidJsonFactory()
            ,null)
            .setRootUrl("http://10.0.2.2:8080/_ah/api")
            .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                @Override
                public void initialize(AbstractGoogleClientRequest<?> gcr) throws IOException {
                    gcr.setDisableGZipContent(true);
                }
            });
    private LearningTicTacToeApi api = null;

    private final PersistFacade gameBackEnd = new PersistFacadeBase() {
        @Override
        protected LearningTicTacToeApi getApi() {
            return api;
        }
    };

    private final GameConfig gameConfig = new GameConfig()
            .persistFacade(gameBackEnd)
            .input(new GameInput() {
                @Override
                public GameMove get() {
                    synchronized (lock) {
                        //TODO: Refactor: Odd logic to set to null here!
                        final GameMove gameMove = lastPlayed;
                        lastPlayed = null;
                        return gameMove;
                    }
                }
            })
            .output(new GameOutput() {
                @Override
                public void onMove(final GameMove gameMove) {
                    synchronized (lock) {
                        lastPlayed = gameMove;
                    }
                }

                @Override
                public void onGameOver(char winner) {
                    synchronized (lock) {
                        outcomeText = "The winner is " + winner;
                    }
                }
            });

    public void resetGame(final View v) {
        resetSession();

        //TODO: Refactor cell id handling
        final int[] buttonIds = {
                R.id.cell00
                , R.id.cell01
                , R.id.cell02
                , R.id.cell10
                , R.id.cell11
                , R.id.cell12
                , R.id.cell20
                , R.id.cell21
                , R.id.cell22
        };

        synchronized (lock) {
            for (int id : buttonIds) {
                final Button button = (Button) findViewById(id);
                button.setText("");
            }

            final TextView outcome = (TextView) findViewById(R.id.victory);
            outcome.setText("");
        }
    }

    private void resetSession() {
        if (gameSession != null) {
            //TODO: Discard previous ??
        }

        if (null == api) {//lazy load
            api = apiBuilder.build();
        }

        //TODO: Give option to user
        gameConfig.playerOne(GameConfig.PlayerType.HUMAN)
                .playerTwo(GameConfig.PlayerType.COMPUTER);

        gameSession = new GameSession(gameConfig);
    }

    public void playCell(final View v) {
        final Button button = (Button) v;
        final CharSequence buttonText = button.getText();
        if (buttonText != null && buttonText.length() > 0) {
            return;
        }

        synchronized (lock) {
            if (null == lastPlayed) {
                final char piece = 'X'; //TODO: get from session
                button.setText(String.valueOf(piece));
                //TODO: Need clean up!
                switch (v.getId()) {
                    case R.id.cell00:
                        lastPlayed = new GameMove(piece, 0, 0);
                        break;
                    case R.id.cell01:
                        lastPlayed = new GameMove(piece, 0, 1);
                        break;
                    case R.id.cell02:
                        lastPlayed = new GameMove(piece, 0, 2);
                        break;
                    case R.id.cell10:
                        lastPlayed = new GameMove(piece, 1, 0);
                        break;
                    case R.id.cell11:
                        lastPlayed = new GameMove(piece, 1, 1);
                        break;
                    case R.id.cell12:
                        lastPlayed = new GameMove(piece, 1, 2);
                        break;
                    case R.id.cell20:
                        lastPlayed = new GameMove(piece, 2, 0);
                        break;
                    case R.id.cell21:
                        lastPlayed = new GameMove(piece, 2, 1);
                        break;
                    case R.id.cell22:
                        lastPlayed = new GameMove(piece, 2, 2);
                        break;
                    default:
                        lastPlayed = null;
                        break;
                }
            }
        }

        if (null == gameSession) {
            resetSession(); //TODO: Do this more elegantly
        }

        //TODO: Refactor and automate computer play call
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                gameSession.play(); //human play
                gameSession.play(); //computer play
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (lastPlayed != null) {
                    synchronized (lock) {
                        //TODO: Refactor: Odd logic to set to null here!
                        final GameMove gameMove = lastPlayed;
                        lastPlayed = null;

                        //TODO: Need clean up!
                        final int id;
                        if (gameMove.is(0, 0))
                            id = R.id.cell00;
                        else if (gameMove.is(0, 1))
                            id = R.id.cell01;
                        else if (gameMove.is(0, 2))
                            id = R.id.cell02;
                        else if (gameMove.is(1, 0))
                            id = R.id.cell10;
                        else if (gameMove.is(1, 1))
                            id = R.id.cell11;
                        else if (gameMove.is(1, 2))
                            id = R.id.cell12;
                        else if (gameMove.is(2, 0))
                            id = R.id.cell20;
                        else if (gameMove.is(2, 1))
                            id = R.id.cell21;
                        else if (gameMove.is(2, 2))
                            id = R.id.cell22;
                        else
                            return;

                        final Button button = (Button) findViewById(id);
                        if (button.getText().equals("")) {
                            button.setText(String.valueOf(gameMove.getPiece()));
                        } else {
                            throw new RuntimeException("TODO: What the hell??");
                        }
                    }
                }

                if (outcomeText != null) {
                    final TextView outcome = (TextView) findViewById(R.id.victory);
                    synchronized (lock) {
                        outcome.setText(outcomeText);
                        outcomeText = null; //TODO: There must be a better way!
                    }
                }

            }
        }.execute();
    }
}
