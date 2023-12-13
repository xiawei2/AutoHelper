package com.mouserecorder.player;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.mouserecorder.player.api.Player;


import javax.swing.*;

/**
 * User: eguller
 * Date: 3/13/14
 * Time: 8:28 AM
 */
public class StopHotKeyListener implements NativeKeyListener {
    public static final String STOP_HOTKEY = "ctrl F2";
    Player player = null;

    public StopHotKeyListener(Player player) {
        this.player = player;
    }
    public StopHotKeyListener(){

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        // 检查 Ctrl 键和 Alt 键是否按下
        if (e.getKeyCode() == NativeKeyEvent.VC_HOME) {
            ctrlPressed = true;
        } else if (e.getKeyCode() == NativeKeyEvent.VC_ALT) {
            altPressed = true;
        }

        // 检查是否同时按下了 Ctrl + Alt + N
        if (ctrlPressed && altPressed && e.getKeyCode() == NativeKeyEvent.VC_N) {
            System.out.println("Combined Hotkey Pressed: Ctrl + Alt + N");
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        // 检查 Ctrl 键和 Alt 键是否释放
        if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
            ctrlPressed = false;
        } else if (e.getKeyCode() == NativeKeyEvent.VC_ALT) {
            altPressed = false;
        }
    }
}
