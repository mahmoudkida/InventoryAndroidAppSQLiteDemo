package com.example.mahmoudkida.inventoryandroidappsqlitedemo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryContract.ProductEntry;
import com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryContract.SupplierEntry;

/**
 * Created by Mahmoud Kida on 6/5/2018.
 */
public class InventoryDBHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = InventoryDBHelper.class.getSimpleName();
    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "inventory.db";
    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link InventoryDBHelper}.
     *
     * @param context of the app
     */
    public InventoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the products table
        String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME + " ("
                + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL, "
                + ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER DEFAULT 0, "
                + ProductEntry.COLUMN_PRODUCT_CATEGORY + " INTEGER, "
                + ProductEntry.COLUMN_PRODUCT_SUPPLIER_ID + " INTEGER);";
        // Execute the SQL statement
        try {
            db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "Problem creating products table");
        }
        // Create a String that contains the SQL statement to create the products table
        String SQL_CREATE_SUPPLIERS_TABLE = "CREATE TABLE " + SupplierEntry.TABLE_NAME + " ("
                + SupplierEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SupplierEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + SupplierEntry.COLUMN_SUPPLIER_PHONE + " INTEGER);";
        try {
            // Execute the SQL statement
            db.execSQL(SQL_CREATE_SUPPLIERS_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "Problem creating supplier table");
        }
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
