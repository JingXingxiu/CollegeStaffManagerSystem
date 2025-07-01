package com.java.collegeManager.DAO;
/// 数据库的删除用逻辑删除
/// 其中，注意每一个查询都要避免查询到逻辑删除的
/// 对于处理逻辑删除的和存在的可能出现相同id的情况，解决方法如下：
public interface StaffDAO {
    ///统一以第一个为索引！！
    /// 增
    /// 删
    public void deleteStaffByID(String uniqueID);
    public void deleteStaffByName(String name);
    /// 改
    /// 查（具体）
    public long getTotalNumber();
    public long getAllFemale();
    public long getAllMale();
    /// 查（存在）
    public boolean findStaffByUniqueID(String uniqueID);
    public boolean findStaffByName(String name);
    public long getNumberByName(String name); /// 获取重名的人的个数
}
