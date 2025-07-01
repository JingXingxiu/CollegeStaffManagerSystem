package com.java.collegeManager.DAO.implement.mysql;

import com.java.collegeManager.DAO.AdministrationDAO;
import com.java.collegeManager.model.Administration;
import com.java.collegeManager.utils.DBConnectionUtil;
import com.java.collegeManager.utils.ShowAlert;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/// 数据库的删除用逻辑删除
/// 其中，注意每一个查询都要避免查询到逻辑删除的
/// 对于处理逻辑删除的和存在的可能出现相同id的情况，解决方法如下：
public class AdministrationMySQL implements AdministrationDAO {

    // 数据库连接信息
    private static final String URL = DBConnectionUtil.getDatabaseUrl();
    private static final String USER = DBConnectionUtil.getDatabaseUser();
    private static final String PASSWORD = DBConnectionUtil.getDatabasePassword();

    // 行政人员表结构设计：
    /*
    CREATE TABLE administration (
        unique_id VARCHAR(50) PRIMARY KEY,  -- 员工唯一ID
        name VARCHAR(100) NOT NULL,         -- 姓名
        gender VARCHAR(10) NOT NULL,        -- 性别
        age INT NOT NULL,                   -- 年龄
        entry_date DATE NOT NULL,           -- 入职日期
        title VARCHAR(100) NOT NULL,        -- 行政职务
        political VARCHAR(50),              -- 政治面貌
        is_deleted TINYINT DEFAULT 0        -- 逻辑删除标志 0=存在, 1=删除
    );
    */

    @Override
    public void updateAdministration(String uniqueID, String propertyName, String information) {
        if(propertyName.equals("uniqueID")&&findStaffByUniqueID(information)){
            ShowAlert.show("警告","编辑ID错误,保存失败","出现重复ID，数据库将不做更新", Alert.AlertType.WARNING);
            throw new RuntimeException("修改失败!ID已经存在!");
        }
        // 根据 uniqueID 和propertyName查询指定位置，并用information更新数据
        String sql = "UPDATE administration SET " + propertyName + " = ? WHERE unique_id = ? AND is_deleted = 0";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, information);
            pstmt.setString(2, uniqueID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Administration> getAllAdministration() {
        // 返回所有的行政人员 new Administration (String uniqueID, String name, String gender,
        //             int age, LocalDate entryDate, String title, String political);
        List<Administration> adminList = new ArrayList<>();
        String sql = "SELECT * FROM administration WHERE is_deleted = 0";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                adminList.add(createAdministrationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminList;
    }

    @Override
    public List<Administration> getAdministrationByUniqueID(String ID) {
        // 根据ID获取单个老师，以列表形式返回
        // 记得跳过逻辑上以及被删除的老师
        List<Administration> adminList = new ArrayList<>();
        String sql = "SELECT * FROM administration WHERE unique_id = ? AND is_deleted = 0";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                adminList.add(createAdministrationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminList;
    }

    @Override
    public List<Administration> getAdministrationByName(String name) {
        // 可能有多个老师，以列表形式返回
        // 记得跳过逻辑上以及被删除的老师
        List<Administration> adminList = new ArrayList<>();
        String sql = "SELECT * FROM administration WHERE name = ? AND is_deleted = 0";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                adminList.add(createAdministrationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminList;
    }

    @Override
    public long getAllFemale() {
        return getCountByGender("女");
    }

    @Override
    public long getAllMale() {
        return getCountByGender("男");
    }

    @Override
    public void addAdministration(Administration administration) {
        String sql = "INSERT INTO administration (unique_id, name, gender, age, entry_date, title, political) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setPreparedStatementFromAdmin(pstmt, administration);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteStaffByID(String uniqueID) {
        // 直接根据唯一ID 来 逻辑 删除 1改0
        String sql = "UPDATE administration SET is_deleted = 1 WHERE unique_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, uniqueID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteStaffByName(String name) {
        // 根据姓名来删除，姓名一定唯一，在Service已经检验过了
        String sql = "UPDATE administration SET is_deleted = 1 WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getNumberByName(String name) {
        // 获取重名的人的个数
        return getCount("SELECT COUNT(*) FROM administration WHERE name = ? AND is_deleted = 0", name);
    }

    @Override
    public long getTotalNumber() {
        return getCount("SELECT COUNT(*) FROM administration WHERE is_deleted = 0");
    }

    @Override
    public boolean findStaffByUniqueID(String uniqueID) {
        return getCount("SELECT COUNT(*) FROM administration WHERE unique_id = ? AND is_deleted = 0", uniqueID) > 0;
    }

    @Override
    public boolean findStaffByName(String name) {
        return getNumberByName(name) > 0;
    }

    // ================ 辅助方法 ================
    private Administration createAdministrationFromResultSet(ResultSet rs) throws SQLException {
        return new Administration(
                rs.getString("unique_id"),
                rs.getString("name"),
                rs.getString("gender"),
                rs.getInt("age"),
                rs.getDate("entry_date").toLocalDate(),
                rs.getString("title"),
                rs.getString("political")
        );
    }

    private void setPreparedStatementFromAdmin(PreparedStatement pstmt, Administration admin) throws SQLException {
        pstmt.setString(1, admin.getUniqueID());
        pstmt.setString(2, admin.getName());
        pstmt.setString(3, admin.getGender());
        pstmt.setInt(4, admin.getAge());
        pstmt.setDate(5, Date.valueOf(admin.getEntryDate()));
        pstmt.setString(6, admin.getTitle());
        pstmt.setString(7, admin.getPolitical());
    }

    private long getCountByGender(String gender) {
        return getCount("SELECT COUNT(*) FROM administration WHERE gender = ? AND is_deleted = 0", gender);
    }

    private long getCount(String sql, Object... params) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                // 根据参数实际类型设置值
                if (params[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) params[i]);
                } else if (params[i] instanceof Long) {
                    pstmt.setLong(i + 1, (Long) params[i]);
                } else {
                    pstmt.setString(i + 1, params[i].toString());
                }
            }

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}