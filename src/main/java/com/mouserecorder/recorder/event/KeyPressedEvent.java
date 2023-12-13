package com.mouserecorder.recorder.event;

import com.mouserecorder.wrappers.KeyWrapper;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: eguller
 * Date: 12/4/13
 * Time: 5:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class KeyPressedEvent extends KeyEvent{
    public KeyPressedEvent(int key) {
        super(key);
    }

    @Override
    public void execute(Robot robot) {
        try {
            robot.keyPress(key);
        }catch (IllegalArgumentException e){
            System.out.println(key);
            System.out.println(KeyWrapper.codeToKey(key));
//            e.printStackTrace();
        }
    }
}
