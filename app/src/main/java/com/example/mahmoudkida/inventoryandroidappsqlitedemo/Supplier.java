package com.example.mahmoudkida.inventoryandroidappsqlitedemo;

/**
 * Created by Mahmoud Kida on 6/6/2018.
 */

public class Supplier {
    private int ID;
    private String name;
    private int phone;

    public Supplier(int id, String name, int phone) {
        ID = id;
        this.name = name;
        this.phone = phone;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }
}
