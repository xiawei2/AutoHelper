package com.mouserecorder.recorder;

import  com.mouserecorder.recorder.event.Event;
import com.mouserecorder.recorder.Record;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.dispatcher.SwingDispatchService;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.*;
import com.mouserecorder.config.Config;
import com.mouserecorder.recorder.api.Recorder;
import com.mouserecorder.recorder.event.*;
import com.mouserecorder.wrappers.MouseWrapper;
import com.mouserecorder.wrappers.NativeWrapper;


/**
 * User: eguller
 * Date: 11/24/13
 * Time: 4:08 PM
 * <p/>
 * Record mouse and keyboard actions.
 */
public class BaseRecorder implements NativeMouseMotionListener, NativeKeyListener, NativeMouseListener, NativeMouseWheelListener, Recorder {


    Record record;
    //private static List<Event> inputEventList = new LinkedList<Event>();
    Config config;

    public BaseRecorder(Config config) {
        this.config = config;
        record = new Record(config);
    }

    public void record() {
        registerNativeHook();
        record = new Record(config);
    }

    public Record stop() {
        deRegisterNativeHook();
        return record;
    }

    @Override
    public Record getRecord() {
        return record;
    }

    @Override
    public void setRecord(Record record) {
        this.record = record;
    }

    /**
     * Register native hook and registerNativeHook capturing
     */
    public void registerNativeHook() {
        GlobalScreen.setEventDispatcher(new SwingDispatchService());// Force JNativeHook to use the Swing thread

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
        }

        GlobalScreen.addNativeKeyListener(this);
        GlobalScreen.addNativeMouseListener(this);
        GlobalScreen.addNativeMouseMotionListener(this);
        GlobalScreen.addNativeMouseWheelListener(this);

    }


    /**
     * De-register native hook and deRegisterNativeHook capturing
     */
    public void deRegisterNativeHook() {
        GlobalScreen.removeNativeKeyListener(this);
        GlobalScreen.removeNativeMouseListener(this);
        GlobalScreen.removeNativeMouseMotionListener(this);
        GlobalScreen.removeNativeMouseWheelListener(this);
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem unregistering the native hook.");
            System.err.println(ex.getMessage());
        }

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        System.out.println("key code:" + java.awt.event.KeyEvent.getKeyText(NativeWrapper.mapNativeKey(nativeKeyEvent.getKeyCode())));
        record.post(new KeyPressedEvent(NativeWrapper.mapNativeKey(nativeKeyEvent.getKeyCode())));
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        record.post(new KeyReleasedEvent(NativeWrapper.mapNativeKey(nativeKeyEvent.getKeyCode())));
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
        record.post(new MouseMoveEvent(nativeMouseEvent.getX(), nativeMouseEvent.getY()));
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent nativeMouseEvent) {
        record.post(new MouseMoveEvent(nativeMouseEvent.getX(), nativeMouseEvent.getY()));
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent nativeMouseEvent) {
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeMouseEvent) {
        int maskCode = MouseWrapper.native2Code(nativeMouseEvent.getButton());
        record.post(new MousePressedEvent(maskCode));
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {
        int maskCode = MouseWrapper.native2Code(nativeMouseEvent.getButton());
        record.post(new MouseReleasedEvent(maskCode));
    }

    @Override
    public void nativeMouseWheelMoved(NativeMouseWheelEvent nativeMouseWheelEvent) {
        record.post(new MouseWheelEvent(nativeMouseWheelEvent.getWheelRotation()));
    }
}
