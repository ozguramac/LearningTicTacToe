package com.oz.game.tictactoe;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.oz.game.tictactoe.core.persist.PersistFacade;
import com.oz.game.tictactoe.impl.PersistFacadeImpl;

import junit.framework.Assert;

import java.lang.Exception;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public final void test() throws Exception {
        //TODO...
        System.out.println("So far so good!");
    }
}