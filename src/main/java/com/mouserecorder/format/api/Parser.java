package com.mouserecorder.format.api;

import com.mouserecorder.recorder.event.Event;

/**
 * User: eguller
 * Date: 3/4/14
 * Time: 6:26 AM
 */
public interface Parser {
    public  <T extends Event> T parse();
}
