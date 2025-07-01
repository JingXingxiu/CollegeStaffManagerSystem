package com.java.collegeManager.controller;

import com.java.collegeManager.model.Administration;
import com.java.collegeManager.model.AdministrationAndTeacher;
import com.java.collegeManager.model.Researcher;
import com.java.collegeManager.model.Teacher;
import com.java.collegeManager.service.*;
import com.java.collegeManager.utils.ShowAlert;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class AddStaffController {
    // 服务层
    private final StaffService teacherService = new TeacherService();
    private final StaffService administrationService = new AdministrationService();
    private final StaffService administrationAndTeacherService = new AdministrationAndTeacherService();
    private final StaffService researcherService = new ResearcherService();
    private String flag=null;
    private final AddStaffControllerService addStaffControllerService=new AddStaffControllerService();
    private MainController mainController=null;

    public void setMainController(MainController mainController){
        this.mainController=mainController;
    }

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private TextField uniqueID;

    @FXML
    private TextField name;

    @FXML
    private TextField gender;

    @FXML
    private Spinner<Integer> age;

    @FXML
    private TextField titleOrPosition;

    @FXML
    private TextField major;

    @FXML
    private TextField facultyOrLaboratory;

    @FXML
    private TextField political;

    @FXML
    public void initialize(){
        List<String> items=List.of("教师","研究员","行政人员","行政老师");
        choiceBox.getItems().setAll(items);
        choiceBox.setValue("教师");
        flag="教师";
        political.setVisible(false);
        age.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,130,25));
        choiceBox.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue.equals("教师")) handleChangeTeacher();
            else if(newValue.equals("研究员")) handleChangeResearcher();
            else if(newValue.equals("行政人员")) handleChangeAdministration();
            else handleChangeAdministrationAndTeacher();
            flag=newValue;
        });
    }

    private void handleChangeTeacher(){
        handleReset();
        titleOrPosition.setPromptText("职称");
        political.setVisible(false);
        facultyOrLaboratory.setVisible(true);
        facultyOrLaboratory.setPromptText("学院");
        major.setVisible(true);
    }

    private void handleChangeResearcher(){
        handleReset();
        titleOrPosition.setPromptText("职务");
        major.setVisible(false);
        facultyOrLaboratory.setVisible(true);
        facultyOrLaboratory.setPromptText("实验室");
        political.setVisible(false);
    }

    private void handleChangeAdministration(){
        handleReset();
        political.setVisible(true);
        major.setVisible(false);
        titleOrPosition.setPromptText("职称");
        facultyOrLaboratory.setVisible(false);
    }

    private void handleChangeAdministrationAndTeacher(){
        handleReset();
        titleOrPosition.setPromptText("职称");
        political.setVisible(true);
        political.setVisible(true);
        facultyOrLaboratory.setVisible(true);
        facultyOrLaboratory.setPromptText("学院");
        major.setVisible(true);
    }

    @FXML
    private void handleAddStaff(){
        /// 硬编码，俗称屎山代码！
        /// 提前熟练防御性编程！
        if(flag==null) throw new IllegalArgumentException("有大问题！！");
        if(!addStaffControllerService.authUniqueID(uniqueID.getText())){
            ShowAlert.show("警告","员工ID重复","请输入新ID", Alert.AlertType.WARNING);
            handleReset();
            return;///其实应该用异常抛出！
        }
// 检查基础字段是否为空
        if (name.getText() == null || name.getText().trim().isEmpty()) {
            ShowAlert.show("警告", "姓名为空", "请输入姓名", Alert.AlertType.WARNING);
            return;
        }
        if (uniqueID.getText() == null || uniqueID.getText().trim().isEmpty()) {
            ShowAlert.show("警告", "ID为空", "请输入员工ID", Alert.AlertType.WARNING);
            return;
        }
        if (gender.getText() == null || gender.getText().trim().isEmpty()) {
            ShowAlert.show("警告", "性别为空", "请选择性别", Alert.AlertType.WARNING);
            return;
        }

// 检查年龄是否有效
        if (age.getValue() == null || age.getValue() < 18) {
            ShowAlert.show("警告", "年龄无效", "年龄必须大于18岁", Alert.AlertType.WARNING);
            return;
        }

// 检查ID是否重复
        if (!addStaffControllerService.authUniqueID(uniqueID.getText())) {
            ShowAlert.show("警告", "员工ID重复", "请输入新ID", Alert.AlertType.WARNING);
            handleReset();
            return;
        }

// 类型特定字段检查
        switch (flag) {
            case "教师" -> {
                if (facultyOrLaboratory.getText().trim().isEmpty()) {
                    ShowAlert.show("警告", "学院/实验室为空", "请输入学院/实验室", Alert.AlertType.WARNING);
                    return;
                }
                if (major.getText().trim().isEmpty()) {
                    ShowAlert.show("警告", "专业为空", "请输入专业", Alert.AlertType.WARNING);
                    return;
                }
                if (titleOrPosition.getText().trim().isEmpty()) {
                    ShowAlert.show("警告", "职称为空", "请输入职称", Alert.AlertType.WARNING);
                    return;
                }
                teacherService.addStaff(new Teacher(uniqueID.getText(),
                    name.getText(), gender.getText(), age.getValue(), LocalDate.now(),
                    facultyOrLaboratory.getText(), major.getText(), titleOrPosition.getText()));
            }
            case "研究员" -> {
                if (facultyOrLaboratory.getText().trim().isEmpty()) {
                    ShowAlert.show("警告", "学院/实验室为空", "请输入学院/实验室", Alert.AlertType.WARNING);
                    return;
                }
                if (titleOrPosition.getText().trim().isEmpty()) {
                    ShowAlert.show("警告", "职称为空", "请输入职称", Alert.AlertType.WARNING);
                    return;
                }
                researcherService.addStaff(new Researcher(uniqueID.getText(),
                    name.getText(), gender.getText(), age.getValue(), LocalDate.now(),
                    facultyOrLaboratory.getText(), titleOrPosition.getText()));
            }
            case "行政人员" -> {
                if (titleOrPosition.getText().trim().isEmpty()) {
                    ShowAlert.show("警告", "职务为空", "请输入职务", Alert.AlertType.WARNING);
                    return;
                }
                if (political.getText().trim().isEmpty()) {
                    ShowAlert.show("警告", "政治面貌为空", "请输入政治面貌", Alert.AlertType.WARNING);
                    return;
                }
                administrationService.addStaff(new Administration(uniqueID.getText(),
                    name.getText(), gender.getText(), age.getValue(), LocalDate.now(),
                    titleOrPosition.getText(), political.getText()));
            }
            case "行政老师" -> {
                if (political.getText().trim().isEmpty()) {
                    ShowAlert.show("警告", "政治面貌为空", "请输入政治面貌", Alert.AlertType.WARNING);
                    return;
                }
                if (facultyOrLaboratory.getText().trim().isEmpty()) {
                    ShowAlert.show("警告", "学院/实验室为空", "请输入学院/实验室", Alert.AlertType.WARNING);
                    return;
                }
                if (major.getText().trim().isEmpty()) {
                    ShowAlert.show("警告", "专业为空", "请输入专业", Alert.AlertType.WARNING);
                    return;
                }
                if (titleOrPosition.getText().trim().isEmpty()) {
                    ShowAlert.show("警告", "职务/职称为空", "请输入职务/职称", Alert.AlertType.WARNING);
                    return;
                }
                administrationAndTeacherService.addStaff(new AdministrationAndTeacher(uniqueID.getText(), name.getText(),
                        gender.getText(), age.getValue(), LocalDate.now(), political.getText(),
                        facultyOrLaboratory.getText(), major.getText(), titleOrPosition.getText()));
            }
            default -> {
                System.out.println("                    _ooOoo_");
                System.out.println("                   o8888888o");
                System.out.println("                   88\" . \"88");
                System.out.println("                   (| -_- |)");
                System.out.println("                    O\\ = /O");
                System.out.println("                ____/`---'\\____");
                System.out.println("              .   ' \\\\| |// `.");
                System.out.println("               / \\\\||| : |||// \\");
                System.out.println("             / _||||| -:- |||||-  \\");
                System.out.println("               | | \\\\ - /// | |");
                System.out.println("             | \\_| ''\\---/'' | |");
                System.out.println("              \\ .-\\__ `-` ___/-. /");
                System.out.println("           ___`. .' /--.--\\ `. . __");
                System.out.println("        .\"\" '< `.___\\_<|>_/___.' >'\"\".");
                System.out.println("       | | : `- \\`.;`\\ _ /`;.`/ - ` : | |");
                System.out.println("         \\ \\ `-. \\_ __\\ /__ _/ .-` / /");
                System.out.println(" ======`-.____`-.___\\_____/___.-`____.-'======");
                System.out.println("                    `=---='");
                System.out.println("");
                System.out.println(" .............................................");
                System.out.println("          佛祖保佑             永无BUG");
            }/// 哈哈哈，屎山雕花
        }
        mainController.addStaffShow(name.getText(), uniqueID.getText(), gender.getText(), "增加", age.getValue(), LocalDate.now());
        ShowAlert.show("添加成功","员工已经成功添加","可在主页面刷新查看", Alert.AlertType.CONFIRMATION);
        handleReset();
    }

    /**
     *                    _ooOoo_
     *                   o8888888o
     *                   88" . "88
     *                   (| -_- |)
     *                    O\ = /O
     *                ____/`---'\____
     *              .   ' \\| |// `.
     *               / \\||| : |||// \
     *             / _||||| -:- |||||- \
     *               | | \\\ - /// | |
     *             | \_| ''\---/'' | |
     *              \ .-\__ `-` ___/-. /
     *           ___`. .' /--.--\ `. . __
     *        ."" '< `.___\_<|>_/___.' >'"".
     *       | | : `- \`.;`\ _ /`;.`/ - ` : | |
     *         \ \ `-. \_ __\ /__ _/ .-` / /
     * ======`-.____`-.___\_____/___.-`____.-'======
     *                    `=---='
     * <p>
     * .............................................
     *          佛祖保佑             永无BUG
     */

    @FXML
    private void handleReset(){
        name.clear();
        age.getEditor().clear();
        uniqueID.clear();
        titleOrPosition.clear();
        political.clear();
        facultyOrLaboratory.clear();
        major.clear();
        gender.clear();
    }
}