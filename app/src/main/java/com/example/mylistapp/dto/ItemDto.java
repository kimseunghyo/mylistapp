package com.example.mylistapp.dto;

public class ItemDto {
    private int id;
    private String item;
    private boolean checked;

    public void setId(int id) {
        this.id = id;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public String getItem() {
        return item;
    }

    public  boolean getChecked() {
        return checked;
    }
}
