package com.java.collegeManager.service;

import com.java.collegeManager.DAO.AdministrationAndTeacherDAO;
import com.java.collegeManager.DAO.implement.mysql.AdministrationAndTeacherMySQL;
import com.java.collegeManager.model.AdministrationAndTeacher;
import com.java.collegeManager.model.Staff;

import java.time.LocalDate;
import java.util.List;

public class AdministrationAndTeacherService implements StaffService{
    private final AdministrationAndTeacherDAO administrationAndTeacherMySQL=new AdministrationAndTeacherMySQL();

    public AdministrationAndTeacherService() {
    }

    public void updateAdministrationAndTeacher(String uniqueID,String propertyName,String information){
        administrationAndTeacherMySQL.updateAdministrationAndTeacher(uniqueID,propertyName,information);
    }

    public List<AdministrationAndTeacher> getAllStaff(){
        return administrationAndTeacherMySQL.getAllAdministrationAndTeacher();
    }/// 填充TableView时，由反射工具类调用

    @Override
    public List<AdministrationAndTeacher> findStaff(String information) {/// 搜索时调用
        if(administrationAndTeacherMySQL.findStaffByName(information)) return administrationAndTeacherMySQL.getAdministrationAndTeacherByName(information);
        else if(administrationAndTeacherMySQL.findStaffByUniqueID(information)) return administrationAndTeacherMySQL.getAdministrationAndTeacherByUniqueID(information);
        else throw new IllegalArgumentException("没找到员工");
    }

    @Override
    public boolean findStaffExist(String information) {
        if(administrationAndTeacherMySQL.findStaffByName(information)||administrationAndTeacherMySQL.findStaffByUniqueID(information)) return true;
        return false;
    }

    @Override
    public boolean findStaffByID(String ID) {
        return administrationAndTeacherMySQL.findStaffByUniqueID(ID);
    }

    @Override
    public void addStaff(Staff staff) {
        AdministrationAndTeacher administrationAndTeacher=(AdministrationAndTeacher) staff;
        administrationAndTeacherMySQL.addAdministrationAndTeacher(administrationAndTeacher);
    }

    @Override
    public void deleteStaff(String information) {
        if(administrationAndTeacherMySQL.findStaffByName(information)) administrationAndTeacherMySQL.deleteStaffByName(information);
        else if(administrationAndTeacherMySQL.findStaffByUniqueID(information)) administrationAndTeacherMySQL.deleteStaffByID(information);
        else throw new IllegalArgumentException("废了！");
    }

    public void deleteStaff(String uniqueID,boolean must){
        administrationAndTeacherMySQL.deleteStaffByID(uniqueID);
    }

    @Override
    public long getSameNameNumber(String name) {
        return administrationAndTeacherMySQL.getNumberByName(name);
    }

    @Override
    public long getMenNumber() {
        return administrationAndTeacherMySQL.getAllMale();
    }

    @Override
    public long getWomenNumber() {
        return administrationAndTeacherMySQL.getAllFemale();
    }

    @Override
    public long getTotalNumber() {
        return administrationAndTeacherMySQL.getTotalNumber();
    }

}
