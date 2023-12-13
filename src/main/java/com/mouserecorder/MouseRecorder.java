package com.mouserecorder;

import com.mouserecorder.config.Config;
import com.mouserecorder.config.PersistentConfig;
import com.mouserecorder.player.PlayerImpl;
import com.mouserecorder.player.api.Player;
import com.mouserecorder.recorder.BaseRecorder;
import com.mouserecorder.recorder.MacRecorder;
import com.mouserecorder.recorder.api.Recorder;
import com.mouserecorder.ui.MainWindow;
import com.mouserecorder.ui.util.MessageBox;
import com.mouserecorder.util.OS;

import javax.swing.*;
import java.awt.*;

/**
 * Initializes frame
 */
public class MouseRecorder {

    public static void main(String[] args) {
        setLookAndFeel();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = null;
                try {
                    Config config = createConfig();
                    frame = new MainWindow(createRecorder(config), createPlayer(config), config);
                } catch (AWTException e) {
                    MessageBox.showError("Player", "Creating player failed.");
                    System.exit(-1);
                }

            }
        });
    }

    private static Recorder createRecorder(Config config) {
        if (OS.isMacOSX()) {
            return new MacRecorder(config);
        } else {
            return new BaseRecorder(config);
        }
    }

    private static Player createPlayer(Config config) throws AWTException {
        return new PlayerImpl(config);
    }

    private static Config createConfig() {
        return new PersistentConfig();
    }

    public static void setLookAndFeel() {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }
    }
}
