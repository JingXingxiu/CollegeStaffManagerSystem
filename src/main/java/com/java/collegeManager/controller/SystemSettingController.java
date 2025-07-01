package com.java.collegeManager.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

public class SystemSettingController {
    private MainController mainController=null;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private Label name;

    @FXML
    private ImageView headPicture;

    @FXML
    private TextField newName;

    @FXML
    private void handleChangeName(){
        String NewName=newName.getText();
        if(NewName.isEmpty()){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText("名字不能为空");
            alert.setContentText("请重试！");
            alert.showAndWait();
        }else{
            name.setText("昵称"+NewName);
            mainController.setName(NewName);
        }
    }

    @FXML
    private void handleChangeHeadPicture(Event event){
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("选择头像图片");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("图片文件", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            // 处理选择的文件
            String imagePath = selectedFile.toURI().toString();
            Image image = new Image(imagePath);
            // 假设你有一个 ImageView 控件显示头像
            headPicture.setImage(image);
            mainController.setHeadPicture(image);
        }
    }

    @FXML
    private void handleResetAll(){
        name.setText("昵称：37");
        mainController.setName(name.getText());
        headPicture.setImage(new Image(getClass().getResource("img/头像37.JPG").toExternalForm()));
        mainController.setHeadPicture(new Image(getClass().getResource("img/头像37.JPG").toExternalForm()));
    }
}
