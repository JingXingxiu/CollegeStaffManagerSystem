package com.java.collegeManager.service;

import com.java.collegeManager.DAO.AdministrationDAO;
import com.java.collegeManager.DAO.implement.mysql.AdministrationMySQL;
import com.java.collegeManager.model.Administration;
import com.java.collegeManager.model.Staff;

import java.time.LocalDate;
import java.util.List;

public class AdministrationService implements StaffService{
    private final AdministrationDAO administrationMySQL=new AdministrationMySQL();

    public void updateAdministration(String uniqueID,String propertyName,String information){
        administrationMySQL.updateAdministration(uniqueID,propertyName,information);
    }

    @Override
    public long getSameNameNumber(String name) {
        return administrationMySQL.getNumberByName(name);
    }

    @Override
    public boolean findStaffByID(String ID) {
        return administrationMySQL.findStaffByUniqueID(ID);
    }

    public AdministrationService() {
    }

    public List<Administration> getAllStaff(){
        return administrationMySQL.getAllAdministration();
    }/// 填充TableView时，由反射工具类调用

    @Override
    public List<Administration> findStaff(String information) {
        if(administrationMySQL.findStaffByName(information)) return administrationMySQL.getAdministrationByName(information);
        else if(administrationMySQL.findStaffByUniqueID(information)) return administrationMySQL.getAdministrationByUniqueID(information);
        else throw new IllegalArgumentException("找不到对应员工");/// 其实这里非常适合用自定义异常类！
    }

    @Override
    public boolean findStaffExist(String information) {
        if(administrationMySQL.findStaffByName(information)||administrationMySQL.findStaffByUniqueID(information)) return true;
        return false;
    }

    @Override
    public void addStaff(Staff staff) {
        Administration administration=(Administration) staff;
        administrationMySQL.addAdministration(administration);
    }

    @Override
    public void deleteStaff(String information) {
        if(administrationMySQL.findStaffByName(information)) administrationMySQL.deleteStaffByName(information);
        else if(administrationMySQL.findStaffByUniqueID(information)) administrationMySQL.deleteStaffByID(information);
        else throw new IllegalArgumentException("找不到对应员工");/// 其实这里非常适合用自定义异常类！
    }

    public void deleteStaff(String uniqueID,boolean must){
        administrationMySQL.deleteStaffByID(uniqueID);
    }

    @Override
    public long getTotalNumber() {
        return administrationMySQL.getTotalNumber();
    }


    @Override
    public long getMenNumber() {
        return administrationMySQL.getAllMale();
    }

    @Override
    public long getWomenNumber() {
        return administrationMySQL.getAllFemale();
    }

}
