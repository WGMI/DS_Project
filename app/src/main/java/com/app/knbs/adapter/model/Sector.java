package com.app.knbs.adapter.model;

import java.io.Serializable;

/**
 * Developed by Rodney on 22/11/2016.
 */

public class Sector implements Serializable {
    private String sector_id,sector_name,report,coverage,source,table,api,favourite;


    public Sector() {
    }

    public String getSectorID() { return sector_id; }

    public void setSectorID(String sector_id) {
        this.sector_id = sector_id;
    }

    public String getSector() { return sector_name; }

    public void setSector(String sector_name) {
        this.sector_name = sector_name;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

}

