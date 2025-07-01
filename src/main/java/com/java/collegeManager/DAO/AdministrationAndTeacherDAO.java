package com.java.collegeManager.DAO;

import com.java.collegeManager.model.AdministrationAndTeacher;
import java.util.List;
public interface AdministrationAndTeacherDAO extends StaffDAO{
    /// 增
    public void addAdministrationAndTeacher(AdministrationAndTeacher administrationAndTeacher);
    /// 删
    /// 改
    List<AdministrationAndTeacher> getAdministrationAndTeacherByUniqueID(String ID);
    List<AdministrationAndTeacher> getAdministrationAndTeacherByName(String name);
    /// 查
    public List<AdministrationAndTeacher> getAllAdministrationAndTeacher();
    public void updateAdministrationAndTeacher(String uniqueID, String propertyName, String information);
}
