package com.java.collegeManager.service;

import com.java.collegeManager.utils.ShowAlert;
import javafx.scene.control.Alert;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MainControllerService {
    private final StaffService teacherService=new TeacherService();
    private final StaffService administrationService=new AdministrationService();
    private final StaffService administrationAndTeacherService=new AdministrationAndTeacherService();
    private final StaffService researcherService=new ResearcherService();

    public void handleDeleteStaff(Optional<String> result){
        result.ifPresentOrElse(information->{
            long count=0,flag=-1;
            if(teacherService.findStaffExist(information)) {
                count++;
                flag=1;
            }
            if(researcherService.findStaffExist(information)) {
                count++;
                flag=2;
            }
            if(administrationService.findStaffExist(information)) {
                count++;
                flag=3;
            }
            if(administrationAndTeacherService.findStaffExist(information)) {
                count++;
                flag=4;
            }
            if(count==0) ShowAlert.show("员工不存在","找不到指定员工","请检查ID或姓名是否错误", Alert.AlertType.WARNING);
            else if(count>1) ShowAlert.show("删除失败","有重复名称员工存在","请用员工ID尝试删除", Alert.AlertType.WARNING);
            else {
                /// 其实这里可以写异常类来避免多个if检查逻辑
                /// 小于等于1说明 ：如果为0，则information是ID 如果为1 说明没有重名且information是姓名 如果大于1 则说明有重名
                if(flag==1&&teacherService.getSameNameNumber(information)<=1) teacherService.deleteStaff(information);
                else if(flag==2&&researcherService.getSameNameNumber(information)<=1) researcherService.deleteStaff(information);
                else if(flag==3&&administrationService.getSameNameNumber(information)<=1) administrationService.deleteStaff(information);
                else if(flag==4&&administrationAndTeacherService.getSameNameNumber(information)<=1) administrationAndTeacherService.deleteStaff(information);
                else ShowAlert.show("删除失败","有重复名称员工存在","请用员工ID尝试删除", Alert.AlertType.WARNING);
                ShowAlert.show("删除成功", "已经成功删除员工", "刷新界面可以看到变化", Alert.AlertType.CONFIRMATION);
            }
        },()->{
            ShowAlert.show("警告","空信息","请输入ID或姓名", Alert.AlertType.WARNING);
        });
    }

}
