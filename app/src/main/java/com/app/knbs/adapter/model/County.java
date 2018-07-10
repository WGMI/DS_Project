package com.app.knbs.adapter.model;

import java.io.Serializable;

/**
 * Developed by Rodney on 22/11/2016.
 */

public class County {
    public String county,sector;

    public County() {
    }

    public County(String county, String sector){
        this.county = county;
        this.sector = sector;
    }

    public String getCounty() {
        return this.county;
    }

    public String getSector() {
        return this.sector;
    }

    public void setCounty(String county) {
        this.county = county;
    }

}

