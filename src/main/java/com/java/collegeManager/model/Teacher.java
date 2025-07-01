package com.java.collegeManager.model;

import java.time.LocalDate;

public class Teacher extends Staff{
    private String faculty;     ///学院
    private String major;       ///专业
    private String title;       ///职称

    public String getFaculty() {
        return faculty;
    }

    public String getMajor() {
        return major;
    }

    public String getTitle() {
        return title;
    }

    public Teacher(String uniqueID, String name, String gender, int age, LocalDate entryDate, String faculty, String major, String title) {
        super(uniqueID, name, gender, age,entryDate);
        this.faculty = faculty;
        this.major = major;
        this.title = title;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
