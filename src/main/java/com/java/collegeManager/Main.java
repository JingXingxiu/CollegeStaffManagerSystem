package com.java.collegeManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loaderLoad = new FXMLLoader(getClass().getResource("/view/MainStage.fxml"));
        Scene scene = new Scene(loaderLoad.load());
        primaryStage.setTitle("高校员工管理系统");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
/// 大概有四个窗口
/// 1 主页面 2 各个类员工的表格和增加，删除，编辑，搜索功能 3 增加 功能的窗口 4 系统设置 窗口（头像，昵称等等），其实没啥用
/// 其中1可以去3，还可以 去 TextInputDialog 的删除窗口，然后成功或失败直接弹窗，无需写一个完整的窗口，功能直接写在Controller里
/// 登录注册不写了，写过了
/// 如果考虑登录注册的话，utils里面又可以加东西了，比如加密解密...
/// 不过数据库密码加密本来就需要，到时候可以写一下

/// 升级计划：把添加员工的年龄改成选择出生年月日，然后数据库内部存出生年月日，每次统计显示的时候利用出生年月日和 now Date进行计算，得出年龄