package com.op.dnf.dnf.utils;


import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.SwingKeyAdapter;
import com.sun.glass.ui.Application;
import com.sun.glass.ui.GlassRobot;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Example extends javafx.application.Application {

    private final List<EventData> keyEvents = new ArrayList<>();
    private final EventListener listener = new EventListener();
    private final Robot robot = new Robot();
    private final TextArea textArea = new TextArea();
    private boolean replaying = false;

    public Example() throws AWTException {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox container = new VBox();
        Button button = new Button("Replay");
        container.getChildren().addAll(textArea, button);
        button.setOnAction(event -> replayEvents());
        Scene scene = new Scene(container);
        textArea.requestFocus();
        primaryStage.setScene(scene);
        primaryStage.show();
        startRecording();
    }

    private void startRecording() {
        try {
            GlobalScreen.setEventDispatcher(new JavaFxDispatchService());
            GlobalScreen.registerNativeHook();
            java.util.logging.Logger log = java.util.logging.Logger
                .getLogger(GlobalScreen.class.getPackage().getName());
            log.setLevel(Level.OFF);
            GlobalScreen.addNativeKeyListener(listener);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        stopRecording();
    }

    private void stopRecording() {
        try {
            GlobalScreen.removeNativeKeyListener(listener);
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
    }

    private class EventListener extends SwingKeyAdapter {

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            addKeyEvent(true, keyEvent);
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
            addKeyEvent(false, keyEvent);
        }
    }

    private void replayEvents() {
        replaying = true;
        textArea.requestFocus();
        synchronized (keyEvents) {
            playEnter();
            for(EventData eventData: keyEvents) {
                if (eventData.isKeyPress()) {
                    robot.keyPress(eventData.getKeyEvent().getKeyCode());
                } else {
                    robot.keyRelease(eventData.getKeyEvent().getKeyCode());
                }
            }
            keyEvents.clear();
            playEnter();
        }
        replaying = false;
    }

    private void playEnter() {
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    private void addKeyEvent(boolean keyPress, KeyEvent keyEvent) {
        if (!replaying) {
            synchronized (keyEvents) {
                keyEvents.add(new EventData(keyPress, keyEvent));
            }
        }
    }

    private class EventData {
        private final boolean keyPress;
        private final KeyEvent keyEvent;

        public EventData(boolean keyPress, KeyEvent keyEvent) {
            this.keyPress = keyPress;
            this.keyEvent = keyEvent;
        }

        public boolean isKeyPress() {
            return keyPress;
        }

        public KeyEvent getKeyEvent() {
            return keyEvent;
        }
    }
}
