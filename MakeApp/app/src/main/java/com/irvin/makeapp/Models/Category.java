package com.irvin.makeapp.Models;

public class Category {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    private String name;
    private boolean isClicked;

    public Category(String name, boolean isClicked) {
        this.name = name;
        this.isClicked = isClicked;
    }

    public Category (){}
}
