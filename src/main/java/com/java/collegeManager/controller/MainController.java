package com.java.collegeManager.controller;

import com.java.collegeManager.model.*;
import com.java.collegeManager.service.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class MainController {
    private final StaffService teacherService=new TeacherService();
    private final StaffService administrationService=new AdministrationService();
    private final StaffService administrationAndTeacherService=new AdministrationAndTeacherService();
    private final StaffService researcherService=new ResearcherService();
    private final MainControllerService mainControllerService=new MainControllerService();

    @FXML
    private Label Name;
    @FXML
    private ImageView headPicture;
    @FXML
    private VBox systemAnnouncement;///增加文本，并且设置好样式
    @FXML
    private Label announcement;///系统文本
    @FXML
    private Label teacher;
    @FXML
    private Label researcher;
    @FXML private Label administration;
    @FXML
    private Label sumPeople;
    @FXML
    private TableView<StaffShow> staffTableView;
    @FXML
    private TableColumn<StaffShow,String> name;
    @FXML
    private TableColumn<StaffShow,String> uniqueID;
    @FXML
    private TableColumn<StaffShow,String> gender;
    @FXML
    private TableColumn<StaffShow,String> operation;
    @FXML
    private TableColumn<StaffShow,Integer> age;
    @FXML
    private TableColumn<StaffShow, LocalDate> entryDate;
    @FXML
    public void initialize(){
        long number1=teacherService.getTotalNumber()
                ,number2=administrationService.getTotalNumber()
                ,number3=researcherService.getTotalNumber()
                ,number4=administrationAndTeacherService.getTotalNumber();
        researcher.setText(String.valueOf(number3));
        administration.setText(String.valueOf(number2+number4));
        teacher.setText(String.valueOf(number1+number4));
        sumPeople.setText(String.valueOf(number1+number2+number3+number4));
        
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        uniqueID.setCellValueFactory(new PropertyValueFactory<>("uniqueID"));
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        operation.setCellValueFactory(new PropertyValueFactory<>("operation"));
        age.setCellValueFactory(new PropertyValueFactory<>("age"));
        entryDate.setCellValueFactory(new PropertyValueFactory<>("entryDate"));

        // 初始化TableView的数据源（ObservableList）
        staffTableView.setItems(FXCollections.observableArrayList());
    }

    public String getName(){
        return Name.getText();
    }

    public Image getHeadPicture(){
        return headPicture.getImage();
    }

    @FXML
    private void handleOpenTeacher() throws Exception {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/view/StaffManager.fxml"));
        Stage staffStage=new Stage();
        staffStage.setScene(new Scene(fxmlLoader.load()));
        StaffManagerController staffManagerController=fxmlLoader.getController();
        staffStage.setTitle("老师管理");
        staffStage.initModality(Modality.APPLICATION_MODAL);
        staffManagerController.setStaffTypeClass(Teacher.class);
        staffStage.show();
    }

    @FXML
    private void handleOpenResearch() throws Exception {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/view/StaffManager.fxml"));
        Stage staffStage=new Stage();
        staffStage.setScene(new Scene(fxmlLoader.load()));
        StaffManagerController staffManagerController=fxmlLoader.getController();
        staffStage.setTitle("研究员管理");
        staffStage.initModality(Modality.APPLICATION_MODAL);
        staffManagerController.setStaffTypeClass(Researcher.class);
        staffStage.show();
    }

    @FXML
    private void handleOpenAdministration() throws Exception {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/view/StaffManager.fxml"));
        Stage staffStage=new Stage();
        staffStage.setScene(new Scene(fxmlLoader.load()));
        StaffManagerController staffManagerController=fxmlLoader.getController();
        staffStage.setTitle("行政人员管理");
        staffManagerController.setStaffTypeClass(Administration.class);
        staffStage.initModality(Modality.APPLICATION_MODAL);
        staffStage.show();
    }


    @FXML
    private void handleSystemSetting() throws Exception{
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/view/SystemSetting.fxml"));
        Stage settingStage=new Stage();
        settingStage.setScene(new Scene(fxmlLoader.load()));
        settingStage.setTitle("系统设置");
        settingStage.setResizable(false);
        SystemSettingController systemSettingController=fxmlLoader.getController();
        systemSettingController.setMainController(this);
        settingStage.initModality(Modality.APPLICATION_MODAL);

        settingStage.show();
    }

    public void setName(String name){
        Name.setText(name);
    }

    public void setHeadPicture(Image image){
        headPicture.setImage(image);
    }

    @FXML
    private void handleOpenAdministrationTeacher() throws Exception {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/view/StaffManager.fxml"));
        Stage staffStage=new Stage();
        staffStage.setScene(new Scene(fxmlLoader.load()));
        StaffManagerController staffManagerController=fxmlLoader.getController();
        staffStage.setTitle("行政老师管理");
        staffManagerController.setStaffTypeClass(AdministrationAndTeacher.class);
        staffStage.initModality(Modality.APPLICATION_MODAL);
        staffStage.show();
    }

    /// 下面两个是直接通过ID或姓名删除的，然后删除记录可以保存在主页面
    /// 懒得搞拓展性了，直接写死！拓展性的演示已经在表格搜索那里演示了！
    @FXML
    private void handleAddStaff() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader((getClass().getResource("/view/addStaffWindow.fxml")));
        Stage addStaffStage=new Stage();
        addStaffStage.setScene(new Scene(fxmlLoader.load()));
        addStaffStage.setTitle("添加员工");
        addStaffStage.setResizable(false);
        AddStaffController addStaffController=fxmlLoader.getController();
        addStaffController.setMainController(this);
        addStaffStage.initModality(Modality.APPLICATION_MODAL);
        addStaffStage.showAndWait();
    }

    @FXML
    private void handleDeleteStaff() {
        /// 略微重构优化了一下，把业务逻辑弄到了Service里，现在看起来简洁多了！
        /// 之前只写了四个员工的Service，而删除员工用的是ID，需要四个都用，
        /// 就导致逻辑暴露在了Controller，我再建一个MainControllerService，然后把这个验证逻辑放进去
        /// 现在只需要MainController调用Service的方法和传参就行了
        TextInputDialog textInputDialog=new TextInputDialog();
        textInputDialog.setTitle("删除员工");
        textInputDialog.setHeaderText("请输入员工ID或姓名");
        textInputDialog.setContentText("ID/姓名: ");
        Optional<String> result=textInputDialog.showAndWait();
        mainControllerService.handleDeleteStaff(result);
    }

    @FXML
    private void handleRefresh(){
        long number1=teacherService.getTotalNumber()
                ,number2=administrationService.getTotalNumber()
                ,number3=researcherService.getTotalNumber()
                ,number4=administrationAndTeacherService.getTotalNumber();
        researcher.setText(String.valueOf(number3));
        administration.setText(String.valueOf(number2+number4));
        teacher.setText(String.valueOf(number1+number4));
        sumPeople.setText(String.valueOf(number1+number2+number3+number4));
    }

    public void addStaffShow(String name,String uniqueID,String gender,String operation,int age,LocalDate entryDate){
        StaffShow newStaff = new StaffShow(name, uniqueID, gender, age, entryDate, operation);
        staffTableView.getItems().add(newStaff);
    }

    public void deleteStaffShow(String name,String uniqueID,String gender,String operation,int age,LocalDate entryDate){
        StaffShow newStaff = new StaffShow(name, uniqueID, gender, age, entryDate, operation);
        staffTableView.getItems().add(newStaff);
    }
}
