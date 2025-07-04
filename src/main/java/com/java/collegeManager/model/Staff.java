package com.java.collegeManager.model;

import java.time.LocalDate;

public abstract class Staff {
    private String uniqueID;
    private String name;
    private String gender;
    private LocalDate birthday;
    private LocalDate entryDate;
    private int age;

    public Staff(String uniqueID, String name, String gender, LocalDate entryDate, int age) {
        this.uniqueID = uniqueID;
        this.name = name;
        this.gender = gender;
        this.entryDate = entryDate;
        this.age = age;
    }

    public Staff(String uniqueID, String name, String gender, LocalDate birthday, LocalDate entryDate) {
        this.uniqueID = uniqueID;
        this.name = name;
        this.gender = gender;
        this.birthday=birthday;
        this.entryDate=entryDate;
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

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
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

    public LocalDate getBirthday() {
        return birthday;
    }
}
