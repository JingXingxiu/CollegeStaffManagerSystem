package com.java.collegeManager.DAO;

import com.java.collegeManager.model.Administration;
import java.util.List;
public interface AdministrationDAO extends StaffDAO{
    /// 增
    public void addAdministration(Administration administration);
    /// 删
    /// 改
    /// 查
    public List<Administration> getAllAdministration();
    public List<Administration> getAdministrationByUniqueID(String ID);
    public List<Administration> getAdministrationByName(String name);
    public void updateAdministration(String uniqueID, String propertyName, String information);
}
