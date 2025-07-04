package com.java.collegeManager.model;

import java.time.LocalDate;

public class AdministrationAndTeacher extends Staff{
    private String political;
    private String faculty;
    private String major;
    private String title;

    public AdministrationAndTeacher(String uniqueID, String name, String gender, LocalDate entryDate, int age, String political, String faculty, String major, String title) {
        super(uniqueID, name, gender, entryDate, age);
        this.political = political;
        this.faculty = faculty;
        this.major = major;
        this.title = title;
    }

    public AdministrationAndTeacher(String uniqueID, String name, String gender, LocalDate birthday, LocalDate entryDate, String political, String faculty, String major, String title) {
        super(uniqueID, name, gender, birthday, entryDate);
        this.political = political;
        this.faculty = faculty;
        this.major = major;
        this.title = title;
    }

    public String getPolitical() {
        return political;
    }

    public void setPolitical(String political) {
        this.political = political;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
