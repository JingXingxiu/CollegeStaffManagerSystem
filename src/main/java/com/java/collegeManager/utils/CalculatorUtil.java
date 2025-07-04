package com.java.collegeManager.utils;

import java.time.LocalDate;
import java.time.Period;

public class CalculatorUtil {
    public static int calculateAge(LocalDate birthday){
        LocalDate currentDate=LocalDate.now();
        Period period=Period.between(birthday, currentDate);
        return period.getYears();
    }

    public static int calculateYearToToday(int year){
        LocalDate currentDate=LocalDate.now();
        return currentDate.getYear()-year;
    }
}
