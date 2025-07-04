package com.java.collegeManager.model;

import java.time.LocalDate;

public class Researcher extends Staff{
    private String laboratory;
    private String position;

    public Researcher(String uniqueID, String name, String gender, LocalDate birthday, LocalDate entryDate, String laboratory, String position) {
        super(uniqueID, name, gender, birthday,entryDate);
        this.laboratory = laboratory;
        this.position = position;
    }

    public Researcher(String uniqueID, String name, String gender, LocalDate entryDate, String laboratory, String position, int age) {
        super(uniqueID, name, gender, entryDate, age);
        this.laboratory = laboratory;
        this.position = position;
    }

    public String getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(String laboratory) {
        this.laboratory = laboratory;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
