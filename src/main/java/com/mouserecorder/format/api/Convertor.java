package com.mouserecorder.format.api;

import com.mouserecorder.recorder.event.Event;

/**
 * User: eguller
 * Date: 2/23/14
 * Time: 2:42 AM
 */
public interface Convertor<T extends Event> {
   public String event2String(T event);
   public Event string2Event(String str);
}
