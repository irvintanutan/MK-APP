package com.irvin.makeapp.Constant;

/**
 * Created by irvin on 1/6/2020.
 */
public enum TranStatus {
    PENDING(0, "PENDING"),
    PAID(1, "PAID");



    private final int code;
    private final String description;

    TranStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
