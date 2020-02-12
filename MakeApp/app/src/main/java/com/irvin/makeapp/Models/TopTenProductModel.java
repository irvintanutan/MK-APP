package com.irvin.makeapp.Models;

public class TopTenProductModel {

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    private String productCode;
    private String productName;
    private double total;

    public TopTenProductModel(String productCode, String productName, double total) {
        this.productCode = productCode;
        this.productName = productName;
        this.total = total;
    }


}
