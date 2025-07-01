package com.java.collegeManager.service;

import com.java.collegeManager.model.Staff;

import java.util.List;

/// 改动与优势：
/// 之前是写的方法像数据库，一下子ID，一下子Name。
/// 现在改成接受字符串Information，然后内部调用两个数据库方法查找
/// 这样保证了Controller和View上的更任意输入（不需要在那里处理细节）
/// 具体的实现放在Service，屏蔽细节，提供服务！
/// 完美的设计！！
public interface StaffService<T extends Staff> {
    public boolean findStaffExist(String information);
    public void addStaff(Staff staff);
    public void deleteStaff(String information);
    public long getTotalNumber();
    public boolean findStaffByID(String ID);
    public List<T> findStaff(String information);
    public long getMenNumber();
    public long getWomenNumber();
    public long getSameNameNumber(String name);
    public List<T> getAllStaff();
}


//public String getStaffNameByID(String uniqueID);
//public String getStaffIDByName(String name);
//public String getStaffGender(String information);
//public int getStaffAge(String information);
//public LocalDate getStaffEntryDate(String information);