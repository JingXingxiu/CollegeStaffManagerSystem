package com.java.collegeManager.DAO.implement.mysql;

import com.java.collegeManager.DAO.TeacherDAO;
import com.java.collegeManager.model.Teacher;
import com.java.collegeManager.utils.DBConnectionUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TeacherMySQL implements TeacherDAO {
    // 数据库连接信息
    private static final String URL = DBConnectionUtil.getDatabaseUrl();
    private static final String USER = DBConnectionUtil.getDatabaseUser();
    private static final String PASSWORD = DBConnectionUtil.getDatabasePassword();

    // 教师表结构设计：
    /*
    CREATE TABLE teachers (
        unique_id VARCHAR(50) PRIMARY KEY,  -- 员工唯一ID
        name VARCHAR(100) NOT NULL,        -- 姓名
        gender VARCHAR(10) NOT NULL,        -- 性别
        age INT NOT NULL,                   -- 年龄
        entry_date DATE NOT NULL,           -- 入职日期
        faculty VARCHAR(100) NOT NULL,      -- 所属院系
        major VARCHAR(100),                 -- 专业（教师特有）
        title VARCHAR(50),                 -- 职称（教师特有）
        is_deleted TINYINT DEFAULT 0        -- 逻辑删除标志 0=存在, 1=删除
    );
    */

    @Override
    public List<Teacher> getAllTeacher() {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teachers WHERE is_deleted = 0";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                teachers.add(createTeacherFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teachers;
    }

    @Override
    public List<Teacher> getTeacherByUniqueID(String ID) {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teachers WHERE unique_id = ? AND is_deleted = 0";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                teachers.add(createTeacherFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teachers;
    }

    @Override
    public List<Teacher> getTeacherByName(String name) {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teachers WHERE name = ? AND is_deleted = 0";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                teachers.add(createTeacherFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teachers;
    }

    @Override
    public void updateTeacher(String uniqueID, String propertyName, String information) {
        if(propertyName.equals("uniqueID")&&findStaffByUniqueID(information)){
            ShowAlert.show("警告","编辑ID错误,保存失败","出现重复ID，数据库将不做更新", Alert.AlertType.WARNING);
            throw new RuntimeException("修改失败!ID已经存在!");
        }
        String sql = "UPDATE teachers SET " + propertyName + " = ? WHERE unique_id = ? AND is_deleted = 0";

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
    public long getAllFemale() {
        return getCountByGender("女");
    }

    @Override
    public long getAllMale() {
        return getCountByGender("男");
    }

    @Override
    public void addTeacher(Teacher teacher) {
        String sql = "INSERT INTO teachers (unique_id, name, gender, age, entry_date, faculty, major, title) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setPreparedStatementFromTeacher(pstmt, teacher);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteStaffByID(String uniqueID) {
        String sql = "UPDATE teachers SET is_deleted = 1 WHERE unique_id = ?";
        executeUpdateById(sql, uniqueID);
    }

    @Override
    public void deleteStaffByName(String name) {
        String sql = "UPDATE teachers SET is_deleted = 1 WHERE name = ?";

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
        return getCount("SELECT COUNT(*) FROM teachers WHERE is_deleted = 0");
    }

    @Override
    public long getNumberByName(String name) {
        return getCount("SELECT COUNT(*) FROM teachers WHERE name = ? AND is_deleted = 0", name);
    }

    @Override
    public boolean findStaffByUniqueID(String uniqueID) {
        return getCount("SELECT COUNT(*) FROM teachers WHERE unique_id = ? AND is_deleted = 0",uniqueID) > 0;
    }

    @Override
    public boolean findStaffByName(String name) {
        return getNumberByName(name) > 0;
    }

    // ================ 辅助方法 ================
    private Teacher createTeacherFromResultSet(ResultSet rs) throws SQLException {
        return new Teacher(
                rs.getString("unique_id"),
                rs.getString("name"),
                rs.getString("gender"),
                rs.getInt("age"),
                rs.getDate("entry_date").toLocalDate(),
                rs.getString("faculty"),
                rs.getString("major"),
                rs.getString("title")
        );
    }

    private void setPreparedStatementFromTeacher(PreparedStatement pstmt, Teacher teacher) throws SQLException {
        pstmt.setString(1, teacher.getUniqueID());
        pstmt.setString(2, teacher.getName());
        pstmt.setString(3, teacher.getGender());
        pstmt.setInt(4, teacher.getAge());
        pstmt.setDate(5, Date.valueOf(teacher.getEntryDate()));
        pstmt.setString(6, teacher.getFaculty());
        pstmt.setString(7, teacher.getMajor());
        pstmt.setString(8, teacher.getTitle());
    }

    private long getCountByGender(String gender) {
        return getCount("SELECT COUNT(*) FROM teachers WHERE gender = ? AND is_deleted = 0", gender);
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

    private void executeUpdateById(String sql, String id) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}