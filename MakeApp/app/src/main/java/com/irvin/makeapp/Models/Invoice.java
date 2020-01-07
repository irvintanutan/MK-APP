package com.irvin.makeapp.Models;

public class Invoice {


    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvoiceDetail() {
        return invoiceDetail;
    }

    public void setInvoiceDetail(String invoiceDetail) {
        this.invoiceDetail = invoiceDetail;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Invoice(String invoiceId, String customerId, String customerName, String totalAmount, String disCount, String status, String invoiceDetail,
                   String dateCreated , String dueDate) {

        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.discount = disCount;
        this.status = status;
        this.invoiceDetail = invoiceDetail;
        this.dateCreated = dateCreated;
        this.dueDate = dueDate;

    }

    public Invoice(){

    }

    String invoiceId ;
    String customerId ;
    String customerName;
    String totalAmount;
    String discount;
    String status;
    String invoiceDetail;
    String dateCreated;
    String dueDate;

}
