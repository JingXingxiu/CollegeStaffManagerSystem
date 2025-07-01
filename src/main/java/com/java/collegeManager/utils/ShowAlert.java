package com.java.collegeManager.utils;

import javafx.scene.control.Alert;

public class ShowAlert {
    private ShowAlert(){}

    public static void show(String title, String headerText, String contentText, Alert.AlertType alertType){
        Alert alert=new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.show();
    }
}
