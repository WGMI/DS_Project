package com.app.knbs.adapter.model;

import java.io.Serializable;

/**
 * Developed by Rodney on 22/11/2016.
 */

public class Sector_Data implements Serializable {
    private String county,gender,set_a,set_b,set_c,set_d,total,year;

    public Sector_Data(){
    }

    public Sector_Data(String county, String gender, String total, String year){
        this.county = county;
        this.gender = gender;
        this.year = year;
        this.total = total;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSet_A() {
        return set_a;
    }

    public void setSet_A(String set_a) {
        this.set_a = set_a;
    }

    public String getSet_B() { return set_b;   }

    public void setSet_C(String set_c) {
        this.set_c = set_c;
    }

    public String getSet_C() {
        return set_c;
    }

    public void setSet_D(String set_d) {
        this.set_d = set_d;
    }

    public String getSet_D() { return set_d;  }

    public void setSet_B(String set_b) {
        this.set_b = set_b;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}

