package com.irvin.makeapp.Models;

public class GroupSalesModel {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConsultants() {
        return consultants;
    }

    public void setConsultants(String consultants) {
        this.consultants = consultants;
    }

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

    private String id;
    private String name;
    private String consultants;
    private String dateCreated;


    public GroupSalesModel(String id, String name, String consultants, String dateCreated){
        this.id = id;
        this.name = name;
        this.consultants = consultants;
        this.dateCreated = dateCreated;
    }

    public GroupSalesModel(){

    }


}
