package com.java.collegeManager.model;

import java.time.LocalDate;

public class Administration extends Staff{
    private String title;    ///职称
    private String political;   ///政治面貌

    public Administration(String uniqueID, String name, String gender, int age, LocalDate entryDate, String title, String political) {
        super(uniqueID, name, gender, age,entryDate);
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