package com.mouserecorder.ui.state;

import com.mouserecorder.player.StopHotKeyListener;
import com.mouserecorder.ui.MainWindow;

/**
 * User: eguller
 * Date: 12/3/13
 * Time: 12:11 AM
 */


public enum ButtonStates implements Apply {

    PRERECORD {
        public void apply(MainWindow mainWindow) {
            mainWindow.getPlayButton().setEnabled(false);
            mainWindow.getRecordButton().setEnabled(true);
            mainWindow.getStopButton().setEnabled(false);
            mainWindow.getStatusBar().setText("点击红色按钮开始录制.");
        }
    },
    RECORDING {
        public void apply(MainWindow mainWindow) {
            mainWindow.getStopButton().setEnabled(true);
            mainWindow.getRecordButton().setEnabled(false);
            mainWindow.getPlayButton().setEnabled(false);
            mainWindow.getStatusBar().setText("点击绿色按钮停止录制.");
        }
    }

    , POSTRECORD {
        public void apply(MainWindow mainWindow) {
            mainWindow.getStopButton().setEnabled(false);
            mainWindow.getRecordButton().setEnabled(true);
            mainWindow.getPlayButton().setEnabled(true);
            mainWindow.getStatusBar().setText(String.format("点击蓝色按钮开始播放. %s 来停止.", StopHotKeyListener.STOP_HOTKEY.toUpperCase()));
        }
    },
    POSTPLAY{
        public void apply(MainWindow mainWindow) {
            mainWindow.getStopButton().setEnabled(false);
            mainWindow.getPlayButton().setEnabled(true);
            mainWindow.getRecordButton().setEnabled(true);
            mainWindow.getStatusBar().setText("点击蓝色按钮开始播放 / 红色按钮开始录制");
        }
    } ,
    PLAYING {
        public void apply(MainWindow mainWindow) {
            mainWindow.getPlayButton().setEnabled(false);
            mainWindow.getRecordButton().setEnabled(false);
            mainWindow.getStopButton().setEnabled(false);
            mainWindow.getStatusBar().setText("播放你的鼠标/键盘操作");
        }
    };


    public void apply(MainWindow mainWindow){}

}
