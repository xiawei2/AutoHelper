package com.mouserecorder.player.api;

import com.mouserecorder.player.event.LoopStartedEvent;

/**
 * User: eguller
 * Date: 3/11/14
 * Time: 7:52 AM
 */
public interface LoopEventSource {
    public void addLoopEventListener(LoopEventListener loopEventListener);

    public void notifyLoopStarted(LoopStartedEvent loopStartedEvent);
}
