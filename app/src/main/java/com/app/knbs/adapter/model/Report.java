package com.app.knbs.adapter.model;

/**
 * Mobile application created by Rodney on 18/04/2016.
 */
public class Report {
    private String sector_id,sector_name,report,publisher,source,table,api,favourite,county,title;
    private boolean selected;

    public Report() {
    }

    public Report(String source, String report, String publisher,String table, String favourite) {
        this.report = report;
        this.publisher = publisher;
        this.table = table;
        this.source = source;
        this.favourite = favourite;
    }

    public String getSectorID() { return sector_id; }

    public void setSectorID(String sector_id) {
        this.sector_id = sector_id;
    }

    public String getSector() {
        return sector_name;
    }

    public void setSector(String sector_name) {
        this.sector_name = sector_name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) { this.source = source; }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getTable() { return table; }

    public void setTable(String table) { this.table = table; }

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

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
