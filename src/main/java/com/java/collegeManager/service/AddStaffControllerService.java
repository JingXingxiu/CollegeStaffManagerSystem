package com.java.collegeManager.service;

public class AddStaffControllerService {
    private final StaffService teacherService = new TeacherService();
    private final StaffService administrationService = new AdministrationService();
    private final StaffService administrationAndTeacherService = new AdministrationAndTeacherService();
    private final StaffService researcherService = new ResearcherService();

    public boolean authUniqueID(String uniqueID){
        if(teacherService.findStaffByID(uniqueID)||administrationService.findStaffByID(uniqueID)||
        researcherService.findStaffByID(uniqueID)|| administrationAndTeacherService.findStaffByID(uniqueID)) return false;
        return true;
    }
}
