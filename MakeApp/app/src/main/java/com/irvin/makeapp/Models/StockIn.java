package com.irvin.makeapp.Models;

public class StockIn {

    private String productName;
    private String productCode;
    private String quantity;
    private String price;

    public StockIn(String productName, String productCode, String quantity , String price) {
        this.productName = productName;
        this.productCode = productCode;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
