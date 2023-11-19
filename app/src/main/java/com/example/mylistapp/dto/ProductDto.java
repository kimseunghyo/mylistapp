package com.example.mylistapp.dto;

public class ProductDto {
    private int id;
    private String name;
    private String image;
    private int price;
    private String priceUnit;
    private String link;
    private String etc;
    private int listId;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getPrice() {
        return price;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public String getLink() {
        return link;
    }

    public String getEtc() {
        return etc;
    }

    public int getListId() {
        return listId;
    }
}