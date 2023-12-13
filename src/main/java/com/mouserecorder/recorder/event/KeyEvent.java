package com.mouserecorder.recorder.event;

/**
 * Created with IntelliJ IDEA.
 * User: eguller
 * Date: 12/4/13
 * Time: 5:48 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class KeyEvent implements Event{
    protected int key;
    public KeyEvent(int key){
//        java.awt.event.KeyEvent.Vk_
        super();
        this.key = key;
    }

    public int getKey(){
        return key;
    }
}
