package com.java.collegeManager.model;

import java.time.LocalDate;

public class StaffShow {
    private String uniqueID;
    private String name;
    private String gender;
    private int age;
    private LocalDate entryDate;
    private String operation;

    public StaffShow(String uniqueID, String name, String gender, int age, LocalDate entryDate, String operation) {
        this.uniqueID = uniqueID;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.entryDate = entryDate;
        this.operation = operation;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
