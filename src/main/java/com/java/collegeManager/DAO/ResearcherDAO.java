package com.java.collegeManager.DAO;
import com.java.collegeManager.model.Researcher;
import java.util.List;
public interface ResearcherDAO extends StaffDAO{
    /// 增
    public void addResearcher(Researcher researcher);
    /// 删
    /// 改
    /// 查
    public List<Researcher> getAllResearcher();
    List<Researcher> getResearcherByUniqueID(String ID);
    List<Researcher> getResearcherByName(String name);
    void updateResearcher(String uniqueID, String propertyName, String information);
}
