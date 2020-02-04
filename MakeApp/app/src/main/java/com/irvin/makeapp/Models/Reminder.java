package com.irvin.makeapp.Models;

public class Reminder {

    public Reminder(){

    }

    public Reminder(String KEY_TITLE, String KEY_CUSTOMER_ID, String KEY_BODY, String KEY_DATE_TIME, String KEY_ROWID) {
        this.KEY_TITLE = KEY_TITLE;
        this.KEY_CUSTOMER_ID = KEY_CUSTOMER_ID;
        this.KEY_BODY = KEY_BODY;
        this.KEY_DATE_TIME = KEY_DATE_TIME;
        this.KEY_ROWID = KEY_ROWID;
    }

    public String getKEY_TITLE() {
        return KEY_TITLE;
    }

    public void setKEY_TITLE(String KEY_TITLE) {
        this.KEY_TITLE = KEY_TITLE;
    }

    public String getKEY_CUSTOMER_ID() {
        return KEY_CUSTOMER_ID;
    }

    public void setKEY_CUSTOMER_ID(String KEY_CUSTOMER_ID) {
        this.KEY_CUSTOMER_ID = KEY_CUSTOMER_ID;
    }

    public String getKEY_BODY() {
        return KEY_BODY;
    }

    public void setKEY_BODY(String KEY_BODY) {
        this.KEY_BODY = KEY_BODY;
    }

    public String getKEY_DATE_TIME() {
        return KEY_DATE_TIME;
    }

    public void setKEY_DATE_TIME(String KEY_DATE_TIME) {
        this.KEY_DATE_TIME = KEY_DATE_TIME;
    }

    public String getKEY_ROWID() {
        return KEY_ROWID;
    }

    public void setKEY_ROWID(String KEY_ROWID) {
        this.KEY_ROWID = KEY_ROWID;
    }

    private String KEY_TITLE = "title";
    private String KEY_CUSTOMER_ID = "customer";
    private String KEY_BODY = "body";
    private String KEY_DATE_TIME = "reminder_date_time";
    private String KEY_ROWID = "_id";
}
