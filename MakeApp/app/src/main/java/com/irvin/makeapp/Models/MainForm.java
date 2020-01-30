package com.irvin.makeapp.Models;

public class MainForm {

    private String path;
    private String customerId;
    private String customerName;
    private String totalAmount;
    private String totalAmountPaid;
    private String totalBalance;


    public MainForm(){

    }

    public MainForm(String path, String customerName, String totalAmount, String totalAmountPaid, String totalBalance , String customerId) {
        this.path = path;
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.totalAmountPaid = totalAmountPaid;
        this.totalBalance = totalBalance;
        this.customerId = customerId;
    }


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid(String totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }
}
