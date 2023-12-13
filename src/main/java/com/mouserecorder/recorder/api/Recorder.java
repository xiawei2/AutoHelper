package com.mouserecorder.recorder.api;
import com.mouserecorder.recorder.Record;

/**
 * User: eguller
 * Date: 2/21/14
 * Time: 11:41 PM
 */
public interface Recorder {
    public void record();
    public Record stop();
    public Record getRecord();

    void setRecord(Record record);
}
