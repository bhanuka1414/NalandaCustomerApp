package com.bp.nalandacustomerapp.services.models;

public class CartModel {
    //(id INTEGER PRIMARY KEY AUTOINCREMENT,item_name TEXT,item_id TEXT,qty TEXT,price TEXT,img_url TEXT)
    private int id;
    private String name;
    private String itemId;
    private String qty;
    private String price;
    private String img;

    public CartModel() {

    }

    public CartModel(int id, String name, String itemId, String qty, String price, String img) {
        this.id = id;
        this.name = name;
        this.itemId = itemId;
        this.qty = qty;
        this.price = price;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
