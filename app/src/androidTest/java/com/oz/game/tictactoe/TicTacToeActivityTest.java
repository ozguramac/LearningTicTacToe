package com.oz.game.tictactoe;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;

import com.oz.game.tictactoe.ui.TicTacToeActivity;

/**
 * Created by developer on 12/29/15.
 */
public class TicTacToeActivityTest extends ActivityInstrumentationTestCase2<TicTacToeActivity>
{
    public TicTacToeActivityTest() {
        super(TicTacToeActivity.class);
    }

    public void testSanity() {
        assertNotNull(getActivity());
    }

    public void testPlay() {
        final Button cell11 = (Button) getActivity().findViewById(R.id.cell11);
        assertEquals(cell11.getText().length(), 0);

        getActivity().setPlayProgressListener(new TicTacToeActivity.PlayProgressListener() {
            @Override
            public void onPlayed() {
                assertEquals(cell11.getText(), "X");
            }
        });
        TouchUtils.clickView(this, cell11);
    }

    //TODO: More UI flows...
}
