package com.java.collegeManager.service;

import com.java.collegeManager.utils.ReflectionUtil;

public class StaffManagerControllerService <T>{
    private final TeacherService teacherService=new TeacherService();
    private final AdministrationService administrationService=new AdministrationService();
    private final ResearcherService researcherService=new ResearcherService();
    private final AdministrationAndTeacherService administrationAndTeacherService=new AdministrationAndTeacherService();
    private String name;
    /// 被反射调用
    public void updateTeacher(String uniqueID,String propertyName,String information){
        teacherService.updateTeacher(uniqueID,propertyName,information);
    }

    public void updateResearcher(String uniqueID,String propertyName,String information){
        researcherService.updateResearcher(uniqueID,propertyName,information);
    }

    public void updateAdministration(String uniqueID,String propertyName,String information){
        administrationService.updateAdministration(uniqueID,propertyName,information);
    }

    public void updateAdministrationAndTeacher(String uniqueID,String propertyName,String information){
        administrationAndTeacherService.updateAdministrationAndTeacher(uniqueID,propertyName,information);
    }

    public void deleteStaff(String id){
        if(teacherService.findStaffExist(id)) teacherService.deleteStaff(id,true);
        else if(researcherService.findStaffExist(id)) researcherService.deleteStaff(id,true);
        else if(administrationService.findStaffExist(id)) administrationService.deleteStaff(id,true);
        else if(administrationAndTeacherService.findStaffExist(id)) administrationAndTeacherService.deleteStaff(id,true);
    }

    public void setClass(Class<T> handleClass){
        name= ReflectionUtil.getClassName(handleClass);
    }

    public String getMenNumber(){
        if(name.equals("Teacher")) return String.valueOf(teacherService.getMenNumber());
        else if(name.equals("Researcher")) return String.valueOf(researcherService.getMenNumber());
        else if(name.equals("Administration")) return String.valueOf(administrationService.getMenNumber());
        else if(name.equals("AdministrationAndTeacher")) return String.valueOf(administrationAndTeacherService.getMenNumber());
        else throw new RuntimeException("出问题了！");
    }

    public String getWomenNumber(){
        if(name.equals("Teacher")) return String.valueOf(teacherService.getWomenNumber());
        else if(name.equals("Researcher")) return String.valueOf(researcherService.getWomenNumber());
        else if(name.equals("Administration")) return String.valueOf(administrationService.getWomenNumber());
        else if(name.equals("AdministrationAndTeacher")) return String.valueOf(administrationAndTeacherService.getWomenNumber());
        else throw new RuntimeException("出问题了！");
    }

    public String getPeopleSumNumber(){
        if(name.equals("Teacher")) return String.valueOf(teacherService.getMenNumber());
        else if(name.equals("Researcher")) return String.valueOf(researcherService.getMenNumber());
        else if(name.equals("Administration")) return String.valueOf(administrationService.getMenNumber());
        else if(name.equals("AdministrationAndTeacher")) return String.valueOf(administrationAndTeacherService.getMenNumber());
        else throw new RuntimeException("出问题了！");
    }

}
