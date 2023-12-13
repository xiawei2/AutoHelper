package com.op.dnf.dnf;

import com.op.dnf.dnf.utils.Login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.ButtonBarSkin;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author xiawei
 * @version 1.0
 * @date 2023/11/28 20:05
 */
public class HelloController {
    public static Stage stage ;
    public TextField username;
    public PasswordField password;
    public CheckBox remember;
    public Label loginLog;

    public void exit(ActionEvent actionEvent) {

    }

    public void login(ActionEvent actionEvent) {
        loginHandle(Login.login(username.getText(), password.getText()));

    }

    public void show(){
        stage.show();
    }
    public void hide(){
        stage.hide();
    }

    public void loginHandle(String[] result) {

        if ("OK".equals(result[0])) {
            if ("7888888888888".equals(result[1])) {

                loginLog.setText("登录成功！");

            } else if (Long.valueOf(result[1]) >= 15) {
                loginLog.setText("登录成功！");

            } else {
                loginLog.setText("注册未达15天，等待…………！");
            }

        } else if ("pno".equals(result[0])) {
            loginLog.setText("密码或用户名错误，请检查密码后再次登录！");
        } else if ("mmcwlsxz".equals(result[0])) {
            loginLog.setText("帐号密码错误过多,限制登陆，请50分钟之后再登陆!");
        } else if ("mno1".equals(result[0])) {

            loginLog.setText("宅币不够啦！快点击下方链接进入社区赚取宅币吧！");
        } else if ("uno".equals(result[0])) {
            loginLog.setText("账号不存在！检查账号或者点击下方链接进入社区注册账号！");
        } else {
            loginLog.setText("未知错误，请联系管理员！");
        }
    }

    public void openWeb(ActionEvent actionEvent) {
        Hyperlink hp = (Hyperlink) actionEvent.getSource();
        try {
            Desktop.getDesktop().browse(new URI(hp.getText()));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
