package com.irvin.makeapp.Models;

public class Payment {

    public Payment(String paymentId, String amount, String invoiceId, String dateCreated) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.invoiceId = invoiceId;
        this.dateCreated = dateCreated;
    }

    public Payment(){

    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    String paymentId;
    String amount;
    String invoiceId;
    String dateCreated;

}
