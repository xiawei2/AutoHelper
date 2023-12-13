package com.mouserecorder.ui.action;

import com.mouserecorder.format.api.Format;
import com.mouserecorder.ui.MainWindow;
import com.mouserecorder.ui.state.ButtonStates;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import  com.mouserecorder.recorder.event.Event;
import com.mouserecorder.recorder.Record;

/**
 * User: eguller
 * Date: 3/6/14
 * Time: 7:42 AM
 */
public class OpenFileAction implements ActionListener {
    MainWindow mainWindow = null;
    Format format;

    public OpenFileAction(MainWindow mainWindow, Format format) {
        this.mainWindow = mainWindow;
        this.format = format;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JFileChooser jFileChooser = new JFileChooser();
        int retVal = jFileChooser.showOpenDialog(mainWindow);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            Record record = format.load(file);
            mainWindow.setRecord(record);
            ButtonStates.POSTPLAY.apply(mainWindow);
        }
    }
}
