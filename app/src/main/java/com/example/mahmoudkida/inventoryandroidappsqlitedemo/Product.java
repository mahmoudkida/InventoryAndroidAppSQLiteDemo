package com.example.mahmoudkida.inventoryandroidappsqlitedemo;

/**
 * Created by Mahmoud Kida on 6/6/2018.
 */

public class Product {

//    _ID
//    COLUMN_PRODUCT_NAME
//    COLUMN_PRODUCT_PRICE
//    COLUMN_PRODUCT_QUANTITY
//    COLUMN_PRODUCT_CATEGORY
//    COLUMN_PRODUCT_SUPPLIER_ID

    private int ID;
    private String name;
    private int price;
    private int quantity;
    private int category;
    private int supplierId;
    private String supplierName;


    public Product(int id, String name, int price, int quantity, int category, int supplierId) {
        ID = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.supplierId = supplierId;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
