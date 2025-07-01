package com.java.collegeManager.service;

import com.java.collegeManager.DAO.TeacherDAO;
import com.java.collegeManager.DAO.implement.mysql.TeacherMySQL;
import com.java.collegeManager.model.Staff;
import com.java.collegeManager.model.Teacher;
import java.util.List;

public class TeacherService implements StaffService{
    private final TeacherDAO teacherMySQL=new TeacherMySQL();

    public TeacherService() {
    }

    public void updateTeacher(String uniqueID,String propertyName,String information){
        teacherMySQL.updateTeacher(uniqueID,propertyName,information);
    }

    public List<Teacher> getAllStaff(){
        return teacherMySQL.getAllTeacher();
    }/// 填充TableView时，由反射工具类调用

    @Override
    public boolean findStaffByID(String ID) {
        return teacherMySQL.findStaffByUniqueID(ID);
    }

    @Override
    public boolean findStaffExist(String information) {
        if(teacherMySQL.findStaffByName(information)|| teacherMySQL.findStaffByUniqueID(information)) return true;
        else return false;
    }

    @Override
    public List<Teacher> findStaff(String information) {
        if(teacherMySQL.findStaffByName(information)) return teacherMySQL.getTeacherByName(information);
        else if(teacherMySQL.findStaffByUniqueID(information)) return teacherMySQL.getTeacherByUniqueID(information);
        else throw new IllegalArgumentException("找不到对应员工");/// 其实这里非常适合用自定义异常类！
    }

    @Override
    public void addStaff(Staff staff) {
        Teacher teacher=(Teacher) staff;
        teacherMySQL.addTeacher(teacher);
    }

    @Override
    public long getMenNumber() {
        return teacherMySQL.getAllMale();
    }

    @Override
    public long getWomenNumber() {
        return teacherMySQL.getAllFemale();
    }

    @Override
    public void deleteStaff(String information) {
        /// 已经检查过是否存在了！
        if(teacherMySQL.findStaffByName(information)) teacherMySQL.deleteStaffByName(information);
        else if(teacherMySQL.findStaffByUniqueID(information)) teacherMySQL.deleteStaffByID(information);
        else throw new IllegalArgumentException("找不到对应员工");/// 其实这里非常适合用自定义异常类！
    }

    @Override
    public long getSameNameNumber(String name) {
        return teacherMySQL.getNumberByName(name);
    }

    public void deleteStaff(String uniqueID, boolean must){
        teacherMySQL.deleteStaffByID(uniqueID);
    }

    @Override
    public long getTotalNumber() {
        return teacherMySQL.getTotalNumber();
    }

}
