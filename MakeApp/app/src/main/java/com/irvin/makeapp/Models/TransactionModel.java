package com.irvin.makeapp.Models;

public class TransactionModel {

    private String customerName;
    private String totalAmount;
    private String totalAmountPaid;
    private String photoUrl;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    private String customerId;


    public TransactionModel(){

    }

    public TransactionModel(String customerName, String totalAmount, String totalAmountPaid, String photoUrl, String customerId) {
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.totalAmountPaid = totalAmountPaid;
        this.photoUrl = photoUrl;
        this.customerId = customerId;
    }


    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
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


}
