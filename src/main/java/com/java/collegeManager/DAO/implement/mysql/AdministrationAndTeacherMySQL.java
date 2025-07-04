package com.java.collegeManager.DAO.implement.mysql;

import com.java.collegeManager.DAO.AdministrationAndTeacherDAO;
import com.java.collegeManager.model.AdministrationAndTeacher;
import com.java.collegeManager.utils.CalculatorUtil;
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
public class AdministrationAndTeacherMySQL implements AdministrationAndTeacherDAO {

    // 数据库连接信息
    private static final String URL = DBConnectionUtil.getDatabaseUrl();
    private static final String USER = DBConnectionUtil.getDatabaseUser();
    private static final String PASSWORD = DBConnectionUtil.getDatabasePassword();

    // 行政老师表结构设计：
    /*
    CREATE TABLE administration_teacher (
        unique_id VARCHAR(50) PRIMARY KEY,  -- 员工唯一ID
        name VARCHAR(100) NOT NULL,         -- 姓名
        gender VARCHAR(10) NOT NULL,        -- 性别
        birthday DATE NOT NULL,                   -- 出生日期
        entry_date DATE NOT NULL,           -- 入职日期
        political VARCHAR(50) NOT NULL,     -- 政治面貌
        faculty VARCHAR(100) NOT NULL,      -- 所属院系
        major VARCHAR(100),                 -- 专业
        title VARCHAR(50) NOT NULL,         -- 职称
        is_deleted TINYINT DEFAULT 0        -- 逻辑删除标志 0=存在, 1=删除
    );
    */

    @Override
    public void updateAdministrationAndTeacher(String uniqueID, String propertyName, String information) {
        if(propertyName.equals("uniqueID")&&findStaffByUniqueID(information)){
            ShowAlert.show("警告","编辑ID错误,保存失败","出现重复ID，数据库将不做更新", Alert.AlertType.WARNING);
            throw new RuntimeException("修改失败!ID已经存在!");
        }
        // 根据 uniqueID 和 propertyName查询指定位置，并用information更新数据
        // 注意：实际项目中应对propertyName进行安全检查以防止SQL注入
        String sql = "UPDATE administration_teacher SET " + propertyName + " = ? WHERE unique_id = ? AND is_deleted = 0";

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
    public List<AdministrationAndTeacher> getAllAdministrationAndTeacher() {
        // 返回所有的行政老师 new AdministrationAndTeacher(String uniqueID, String name, String gender,
        //     int age, LocalDate entryDate, String political, String faculty, String major, String title);
        List<AdministrationAndTeacher> adminTeachers = new ArrayList<>();
        String sql = "SELECT * FROM administration_teacher WHERE is_deleted = 0";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                adminTeachers.add(createAdminTeacherFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminTeachers;
    }

    @Override
    public List<AdministrationAndTeacher> getAdministrationAndTeacherByUniqueID(String ID) {
        // 获取单个行政老师，以列表形式返回
        // 记得跳过逻辑上以及被删除的老师
        List<AdministrationAndTeacher> adminTeachers = new ArrayList<>();
        String sql = "SELECT * FROM administration_teacher WHERE unique_id = ? AND is_deleted = 0";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                adminTeachers.add(createAdminTeacherFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminTeachers;
    }

    @Override
    public List<AdministrationAndTeacher> getAdministrationAndTeacherByName(String name) {
        // 可能有多个行政老师，以列表形式返回
        // 记得跳过逻辑上以及被删除的老师
        List<AdministrationAndTeacher> adminTeachers = new ArrayList<>();
        String sql = "SELECT * FROM administration_teacher WHERE name = ? AND is_deleted = 0";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                adminTeachers.add(createAdminTeacherFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminTeachers;
    }

    @Override
    public long getAllFemale() {
        // 用于统计gender的男女数量，查询整个行政老师表
        return getCountByGender("女");
    }

    @Override
    public long getAllMale() {
        // 用于统计gender的男女数量，查询整个行政老师表
        return getCountByGender("男");
    }

    @Override
    public void addAdministrationAndTeacher(AdministrationAndTeacher administrationAndTeacher) {
        // 将实例各个字段存入数据库，有对应的getter函数
        // AdministrationAndTeacher(String uniqueID, String name, String gender,
        //     int age, LocalDate entryDate, String political, String faculty, String major, String title);
        String sql = "INSERT INTO administration_teacher (unique_id, name, gender, birthday, entry_date, political, faculty, major, title) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setPreparedStatementFromAdminTeacher(pstmt, administrationAndTeacher);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteStaffByID(String uniqueID) {
        // 以 ID 逻辑 删除员工
        String sql = "UPDATE administration_teacher SET is_deleted = 1 WHERE unique_id = ?";

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
        // 以 name 来删除（假设service层已确保名称唯一）
        String sql = "UPDATE administration_teacher SET is_deleted = 1 WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getTotalNumber() {
        // 总人数（排除已删除）
        return getCount("SELECT COUNT(*) FROM administration_teacher WHERE is_deleted = 0");
    }

    @Override
    public long getNumberByName(String name) {
        // 获取重名的人的个数
        return getCount("SELECT COUNT(*) FROM administration_teacher WHERE name = ? AND is_deleted = 0", name);
    }

    @Override
    public boolean findStaffByUniqueID(String uniqueID) {
        return getCount("SELECT COUNT(*) FROM administration_teacher WHERE unique_id = ? AND is_deleted = 0", uniqueID) > 0;
    }

    @Override
    public boolean findStaffByName(String name) {
        return getNumberByName(name) > 0;
    }

    // ================ 辅助方法 ================
    private AdministrationAndTeacher createAdminTeacherFromResultSet(ResultSet rs) throws SQLException {
        LocalDate birthday=rs.getDate("birthday").toLocalDate();
        int age= CalculatorUtil.calculateAge(birthday);
        return new AdministrationAndTeacher(
                rs.getString("unique_id"),
                rs.getString("name"),
                rs.getString("gender"),
                rs.getDate("entry_date").toLocalDate(),
                age,
                rs.getString("political"),
                rs.getString("faculty"),
                rs.getString("major"),
                rs.getString("title")
        );
    }

    private void setPreparedStatementFromAdminTeacher(PreparedStatement pstmt,
                                                      AdministrationAndTeacher adminTeacher) throws SQLException {
        pstmt.setString(1, adminTeacher.getUniqueID());
        pstmt.setString(2, adminTeacher.getName());
        pstmt.setString(3, adminTeacher.getGender());
        pstmt.setDate(4, Date.valueOf(adminTeacher.getBirthday()));
        pstmt.setDate(5, Date.valueOf(adminTeacher.getEntryDate()));
        pstmt.setString(6, adminTeacher.getPolitical());
        pstmt.setString(7, adminTeacher.getFaculty());
        pstmt.setString(8, adminTeacher.getMajor());
        pstmt.setString(9, adminTeacher.getTitle());
    }

    private long getCountByGender(String gender) {
        return getCount("SELECT COUNT(*) FROM administration_teacher WHERE gender = ? AND is_deleted = 0", gender);
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