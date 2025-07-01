package com.java.collegeManager.service;

import com.java.collegeManager.DAO.ResearcherDAO;
import com.java.collegeManager.DAO.implement.mysql.ResearcherMySQL;
import com.java.collegeManager.model.Researcher;
import com.java.collegeManager.model.Staff;

import java.net.IDN;
import java.time.LocalDate;
import java.util.List;

public class ResearcherService implements StaffService{
    private final ResearcherDAO researcherMySQL=new ResearcherMySQL();

    public ResearcherService() {
    }

    public void updateResearcher(String uniqueID,String propertyName,String information){
        researcherMySQL.updateResearcher(uniqueID,propertyName,information);
    }

    @Override
    public boolean findStaffByID(String ID) {
        return researcherMySQL.findStaffByUniqueID(ID);
    }

    public List<Researcher> getAllStaff(){
        return researcherMySQL.getAllResearcher();
    }/// 填充TableView时，由反射工具类调用


    @Override
    public boolean findStaffExist(String information) {
        if(researcherMySQL.findStaffByName(information)||researcherMySQL.findStaffByUniqueID(information)) return true;
        return false;
    }

    @Override
    public void addStaff(Staff staff) {
        Researcher researcher=(Researcher) staff;
        researcherMySQL.addResearcher(researcher);
    }

    @Override
    public void deleteStaff(String information) {
        if(researcherMySQL.findStaffByName(information)) researcherMySQL.deleteStaffByName(information);
        else if(researcherMySQL.findStaffByUniqueID(information)) researcherMySQL.deleteStaffByID(information);
        else throw new IllegalArgumentException("找不到对应员工");/// 其实这里非常适合用自定义异常类！
    }

    @Override
    public long getSameNameNumber(String name) {
        return researcherMySQL.getNumberByName(name);
    }

    public void deleteStaff(String uniqueID, boolean must){
        researcherMySQL.deleteStaffByID(uniqueID);
    }

    @Override
    public long getTotalNumber() {
        return researcherMySQL.getTotalNumber();
    }

    @Override
    public List<Researcher> findStaff(String information) {
        if(researcherMySQL.findStaffByName(information)) return researcherMySQL.getResearcherByName(information);
        else if(researcherMySQL.findStaffByUniqueID(information)) return researcherMySQL.getResearcherByUniqueID(information);
        else throw new IllegalArgumentException("找不到对应员工");/// 其实这里非常适合用自定义异常类！
    }

    @Override
    public long getMenNumber() {
        return researcherMySQL.getAllMale();
    }

    @Override
    public long getWomenNumber() {
        return researcherMySQL.getAllFemale();
    }

}