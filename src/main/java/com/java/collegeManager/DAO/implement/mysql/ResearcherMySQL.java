package com.java.collegeManager.DAO.implement.mysql;

import com.java.collegeManager.DAO.ResearcherDAO;
import com.java.collegeManager.model.Researcher;
import com.java.collegeManager.utils.DBConnectionUtil;
import com.java.collegeManager.utils.ShowAlert;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/// 数据库的删除用逻辑删除 0删除 1存在
/// 其中，注意每一个查询都要避免查询到逻辑删除的
/// 对于处理逻辑删除的和存在的可能出现相同id的情况，解决方法如下：
public class ResearcherMySQL implements ResearcherDAO {

    // 数据库连接信息
    private static final String URL = DBConnectionUtil.getDatabaseUrl();
    private static final String USER = DBConnectionUtil.getDatabaseUser();
    private static final String PASSWORD = DBConnectionUtil.getDatabasePassword();

    // 研究人员表结构设计：
    /*
    CREATE TABLE researchers (
        unique_id VARCHAR(50) PRIMARY KEY,  -- 员工唯一ID
        name VARCHAR(100) NOT NULL,         -- 姓名
        gender VARCHAR(10) NOT NULL,        -- 性别
        age INT NOT NULL,                   -- 年龄
        entry_date DATE NOT NULL,           -- 入职日期
        laboratory VARCHAR(100) NOT NULL,   -- 实验室
        position VARCHAR(100),              -- 职位
        is_deleted TINYINT DEFAULT 0        -- 逻辑删除标志 0=存在, 1=删除
    );
    */

    @Override
    public void updateResearcher(String uniqueID, String propertyName, String information) {
        if(propertyName.equals("uniqueID")&&findStaffByUniqueID(information)){
            ShowAlert.show("警告","编辑ID错误,保存失败","出现重复ID，数据库将不做更新", Alert.AlertType.WARNING);
            throw new RuntimeException("修改失败!ID已经存在!");
        }
        // 根据 uniqueID 和propertyName查询指定位置，并用information更新数据
        String sql = "UPDATE researchers SET " + propertyName + " = ? WHERE unique_id = ? AND is_deleted = 0";

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
    public List<Researcher> getAllResearcher() {
        // 返回所有的研究员 new Researcher (String uniqueID, String name, String gender, int age,
        //     LocalDate entryDate, String laboratory, String position);
        List<Researcher> researchers = new ArrayList<>();
        String sql = "SELECT * FROM researchers WHERE is_deleted = 0";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                researchers.add(createResearcherFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return researchers;
    }

    @Override
    public List<Researcher> getResearcherByUniqueID(String ID) {
        // 获取单个老师，以列表形式返回
        // 记得跳过逻辑上以及被删除的老师
        List<Researcher> researchers = new ArrayList<>();
        String sql = "SELECT * FROM researchers WHERE unique_id = ? AND is_deleted = 0";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                researchers.add(createResearcherFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return researchers;
    }

    @Override
    public List<Researcher> getResearcherByName(String name) {
        // 可能有多个老师，以列表形式返回
        // 记得跳过逻辑上以及被删除的老师
        List<Researcher> researchers = new ArrayList<>();
        String sql = "SELECT * FROM researchers WHERE name = ? AND is_deleted = 0";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                researchers.add(createResearcherFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return researchers;
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
    public void addResearcher(Researcher researcher) {
        // Researcher (String uniqueID, String name, String gender, int age,
        //     LocalDate entryDate, String laboratory, String position);
        // 有getter方法
        String sql = "INSERT INTO researchers (unique_id, name, gender, age, entry_date, laboratory, position) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setPreparedStatementFromResearcher(pstmt, researcher);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteStaffByID(String uniqueID) {
        // 直接根据唯一ID 来 逻辑 删除 1改0
        String sql = "UPDATE researchers SET is_deleted = 1 WHERE unique_id = ?";

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
        // 直接根据唯一name 来 逻辑 删除 1改0（已经在Service检查过是否存在重复的name了！）
        String sql = "UPDATE researchers SET is_deleted = 1 WHERE name = ?";

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
        return getCount("SELECT COUNT(*) FROM researchers WHERE name = ? AND is_deleted = 0", name);
    }

    @Override
    public long getTotalNumber() {
        return getCount("SELECT COUNT(*) FROM researchers WHERE is_deleted = 0");
    }

    @Override
    public boolean findStaffByUniqueID(String uniqueID) {
        return getCount("SELECT COUNT(*) FROM researchers WHERE unique_id = ? AND is_deleted = 0", uniqueID) > 0;
    }

    @Override
    public boolean findStaffByName(String name) {
        return getNumberByName(name) > 0;
    }

    // ================ 辅助方法 ================
    private Researcher createResearcherFromResultSet(ResultSet rs) throws SQLException {
        return new Researcher(
                rs.getString("unique_id"),
                rs.getString("name"),
                rs.getString("gender"),
                rs.getInt("age"),
                rs.getDate("entry_date").toLocalDate(),
                rs.getString("laboratory"),
                rs.getString("position")
        );
    }

    private void setPreparedStatementFromResearcher(PreparedStatement pstmt, Researcher researcher) throws SQLException {
        pstmt.setString(1, researcher.getUniqueID());
        pstmt.setString(2, researcher.getName());
        pstmt.setString(3, researcher.getGender());
        pstmt.setInt(4, researcher.getAge());
        pstmt.setDate(5, Date.valueOf(researcher.getEntryDate()));
        pstmt.setString(6, researcher.getLaboratory());
        pstmt.setString(7, researcher.getPosition());
    }

    private long getCountByGender(String gender) {
        return getCount("SELECT COUNT(*) FROM researchers WHERE gender = ? AND is_deleted = 0", gender);
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