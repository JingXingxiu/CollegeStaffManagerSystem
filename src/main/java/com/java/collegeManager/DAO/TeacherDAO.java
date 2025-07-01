package com.java.collegeManager.DAO;

import com.java.collegeManager.model.Staff;
import com.java.collegeManager.model.Teacher;

import java.util.List;

public interface TeacherDAO extends StaffDAO{
    /// 增
    public void addTeacher(Teacher teacher);

    /// 删

    /// 改

    /// 查
    public List<Teacher> getAllTeacher();
    public List<Teacher> getTeacherByUniqueID(String ID);
    public List<Teacher> getTeacherByName(String name);

    public void updateTeacher(String uniqueID, String propertyName, String information);
}