package com.mouserecorder.player.api;
import com.mouserecorder.recorder.Record;

import java.util.Observer;

/**
 * User: eguller
 * Date: 2/21/14
 * Time: 11:43 PM
 */
public interface Player extends LoopEventSource {
    public void play(Record record);
    public void addObserver(Observer observer);

    public void stop();

}
