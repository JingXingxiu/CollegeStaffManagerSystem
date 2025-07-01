package com.java.collegeManager.model;

import java.time.LocalDate;

public abstract class Staff {
    private String uniqueID;
    private String name;
    private String gender;
    private int age;
    private LocalDate entryDate;
    public Staff(String uniqueID, String name, String gender, int age,LocalDate entryDate) {
        this.uniqueID = uniqueID;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.entryDate=entryDate;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }
}
