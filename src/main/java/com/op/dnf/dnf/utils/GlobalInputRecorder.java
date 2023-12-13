package com.op.dnf.dnf.utils;

import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseMotionListener;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;

public class GlobalInputRecorder implements NativeKeyListener, NativeMouseListener, NativeMouseMotionListener {
    private List<Object> events = new ArrayList<>();

    public static void main(String[] args) {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }

        GlobalInputRecorder recorder = new GlobalInputRecorder();

        GlobalScreen.addNativeKeyListener(recorder);
        GlobalScreen.addNativeMouseListener(recorder);
        GlobalScreen.addNativeMouseMotionListener(recorder);

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {

        if (e.getKeyCode()==NativeKeyEvent.VC_F2)
        {
            replay();
        }
        events.add(e);
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
        events.add(e);
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        events.add(e);
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        events.add(e);
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent e) {
        events.add(e);
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent e) {
        events.add(e);
    }

    public void replay() {
        try {
            Robot robot = new Robot();
            for (Object event : events) {

                if (event instanceof NativeKeyEvent) {
                    NativeKeyEvent keyEvent = (NativeKeyEvent) event;
                    System.out.println(NativeKeyEvent.getKeyText(keyEvent.getKeyCode()));
//                    robot.keyPress(keyEvent.getKeyCode());
//                    robot.keyRelease(keyEvent.getKeyCode());
                } else if (event instanceof NativeMouseEvent) {
                    NativeMouseEvent mouseEvent = (NativeMouseEvent) event;
//                    System.out.println(mouseEvent.toString());
                    int button = mouseEvent.getButton();
                    int x = mouseEvent.getX();
                    int y = mouseEvent.getY();
                    if (button == NativeMouseEvent.BUTTON1) {
                        robot.mouseMove(x, y);
                        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    } else if (button == NativeMouseEvent.BUTTON2) {
                        robot.mouseMove(x, y);
                        robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
                    } else if (button == NativeMouseEvent.BUTTON3) {
                        robot.mouseMove(x, y);
                        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                    }else {
                        robot.mouseMove(x, y);
                    }
                }
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
