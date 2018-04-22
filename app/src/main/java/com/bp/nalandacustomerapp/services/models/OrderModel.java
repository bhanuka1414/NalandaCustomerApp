package com.bp.nalandacustomerapp.services.models;

public class OrderModel {
    private String ono, date, confirm, c_remove, finish_driver, amount;

    public OrderModel() {
    }

    public OrderModel(String ono, String date, String confirm, String c_remove, String finish_driver, String amount) {
        this.ono = ono;
        this.date = date;
        this.confirm = confirm;
        this.c_remove = c_remove;
        this.finish_driver = finish_driver;
        this.amount = amount;
    }

    public String getOno() {
        return ono;
    }

    public void setOno(String ono) {
        this.ono = ono;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getC_remove() {
        return c_remove;
    }

    public void setC_remove(String c_remove) {
        this.c_remove = c_remove;
    }

    public String getFinish_driver() {
        return finish_driver;
    }

    public void setFinish_driver(String finish_driver) {
        this.finish_driver = finish_driver;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
