package com.mouserecorder.player.api;

import com.mouserecorder.player.event.LoopStartedEvent;

/**
 * User: eguller
 * Date: 3/11/14
 * Time: 7:44 AM
 */
public interface LoopEventListener {
    public void loopStarted(LoopStartedEvent loopStartedEvent);
}
