package com.op.dnf.dnf;

import com.op4j.IOpInterface;
import com.op4j.Op;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;



public class HelloApplication extends Application {
    public static Stage stage;
    @Override
    public void start(Stage stage) throws IOException {

        HelloController.stage = stage;
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loginer.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("未央助手"+1.0);
        stage.setScene(scene);
        stage.show();
//        alert.close();
    }

    @Override
    public void stop() throws Exception {
        stage.close();
    }
}