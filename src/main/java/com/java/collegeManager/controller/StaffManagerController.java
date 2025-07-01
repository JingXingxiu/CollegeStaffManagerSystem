package com.java.collegeManager.controller;

import com.java.collegeManager.model.Staff;
import com.java.collegeManager.service.*;
import com.java.collegeManager.utils.ShowAlert;
import com.java.collegeManager.utils.TableViewUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/// 沃日，这个要大幅重构了！！！
/// 重构前：fxml：92，controller：100，需要硬编码，且拓展性差
/// 重构后：fxml:42，controller:52 只需要实现接口就可以拓展其他员工

public class StaffManagerController<Type extends Staff> {
    /// 其他
    private final StaffService teacherService=new TeacherService();
    private final StaffService administrationService=new AdministrationService();
    private final StaffService researcherService=new ResearcherService();
    private final StaffService administrationAndTeacherService=new AdministrationAndTeacherService();
    private final StaffManagerControllerService staffManagerControllerService =new StaffManagerControllerService();    /// 专用Service
    private Class<Type> staffTypeClass;

    @FXML
    private Label men;

    @FXML
    private Label women;

    @FXML
    private Label peopleSum;

    @FXML
    private TextField search;

    @FXML
    private TableView<Type> staffTableView;

    public void setStaffTypeClass(Class<Type> staffTypeClass) throws Exception {
        this.staffTypeClass=staffTypeClass;///将实例设置为需要用到的
        init();
    }

    private void init() throws Exception {
        /// 使用工具类进行填充
        staffTableView.setEditable(true);
        TableViewUtil.fillTableView(staffTypeClass,staffTableView);
        staffManagerControllerService.setClass(staffTypeClass);
        men.setText("男："+staffManagerControllerService.getMenNumber());
        women.setText("女："+staffManagerControllerService.getWomenNumber());
        peopleSum.setText("总人数："+staffManagerControllerService.getPeopleSumNumber());
    }

    @FXML
    public void initialize(){/// 空方法，用init代替，防止自动调用
    }

    @FXML
    private void handleSearch() throws Exception {
        String information=search.getText();
        if(information.equals("")||information==null) ShowAlert.show("警告","请输入具体的名字或ID","空搜索",Alert.AlertType.WARNING);
        else TableViewUtil.searchStaff(staffTypeClass,staffTableView,information);
    }

    @FXML
    private void handleDeleteStaff(){
        /// 选中行删除
        /// 如果选中行为空，弹窗警告
        var selectedItems=staffTableView.getSelectionModel().getSelectedItems();
        /// 其实这里都可以用异常类来写，但是时间不够充裕，算了！
        if(selectedItems.isEmpty()) ShowAlert.show("删除失败","请选择要删除的员工","无选中项", Alert.AlertType.WARNING);
        else{
            ShowAlert.show("删除成功","员工记录已被删除","主页面可撤销操作",Alert.AlertType.CONFIRMATION);
            for(Type staff:selectedItems) staffManagerControllerService.deleteStaff(staff.getUniqueID());
            staffTableView.getItems().removeAll(selectedItems);
        }
    }

}
