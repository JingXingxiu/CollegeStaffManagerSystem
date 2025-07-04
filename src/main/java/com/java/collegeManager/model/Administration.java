package com.java.collegeManager.model;

import java.time.LocalDate;

public class Administration extends Staff{
    private String title;    ///职称
    private String political;    ///政治面貌

    public Administration(String uniqueID, String name, String gender, LocalDate entryDate, int age, String title, String political) {
        super(uniqueID, name, gender, entryDate, age);
        this.title = title;
        this.political = political;
    }

    public Administration(String uniqueID, String name, String gender, LocalDate birthday, LocalDate entryDate, String title, String political) {
        super(uniqueID, name, gender, birthday, entryDate);
        this.title = title;
        this.political = political;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPolitical() {
        return political;
    }

    public void setPolitical(String political) {
        this.political = political;
    }
}