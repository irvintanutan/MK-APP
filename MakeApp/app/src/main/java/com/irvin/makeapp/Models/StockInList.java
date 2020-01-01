package com.irvin.makeapp.Models;

public class StockInList {

    private String dateCreated;
    private String id;
    private String details;



    public StockInList(String dateCreated, String id, String details) {
        this.dateCreated = dateCreated;
        this.id = id;
        this.details = details;
    }

    public StockInList(){}


    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
