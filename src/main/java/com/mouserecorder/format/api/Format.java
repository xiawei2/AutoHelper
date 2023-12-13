package com.mouserecorder.format.api;

import com.mouserecorder.recorder.Record;
import com.mouserecorder.recorder.event.Event;
import com.mouserecorder.recorder.*;

import java.io.File;

/**
 * User: eguller
 * Date: 2/23/14
 * Time: 2:36 AM
 */
public interface Format {
    public void save(File file, Record record);
    public Record load(File file);
    public Convertor getConvertor(Event event);
    public Convertor getConvertor(String str);
}
