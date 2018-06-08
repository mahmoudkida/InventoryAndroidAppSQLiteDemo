package com.example.mahmoudkida.inventoryandroidappsqlitedemo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import  com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryContract.ProductEntry;
import  com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryContract.SupplierEntry;

import com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryDBHelper;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private InventoryDBHelper inventoryDbHelper;


    private  DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inventoryDbHelper = new InventoryDBHelper(this);


        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        NavigationView navigationView   = findViewById(R.id.nav_view);

        FragmentManager fragmentManager = getFragmentManager();
        Fragment productPragment = (Fragment) ProductListFragment.newInstance();
        fragmentManager.beginTransaction().replace(R.id.content_frame, productPragment).commit();
        navigationView.setCheckedItem(R.id.products_nav);


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        if(menuItem.getItemId() == R.id.products_nav){
                            FragmentManager fragmentManager = getFragmentManager();
                            Fragment productPragment = (Fragment) ProductListFragment.newInstance();
                            fragmentManager.beginTransaction().replace(R.id.content_frame, productPragment).commit();
                        }
                        else if(menuItem.getItemId() == R.id.suppliers_nav){
                            FragmentManager fragmentManager = getFragmentManager();
                            Fragment suppliersListFragment = (Fragment) SuppliersListFragment.newInstance();
                            fragmentManager.beginTransaction().replace(R.id.content_frame, suppliersListFragment).commit();

                        }


                        menuItem.setChecked(true);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayProductsInfo();
        displaySuppliersInfo();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                break;
            default:
                return false;
        }
        return true;
    }



    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayProductsInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = inventoryDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_CATEGORY,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_ID };

        // Perform a query on the pets table
        Cursor cursor = db.query(
                ProductEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        TextView productsView =  new TextView(this);
        productsView.setText("");

        try {
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.


            productsView.setText("The product table contains " + cursor.getCount() + " products.\n\n");

            productsView.append(ProductEntry._ID + " - " +
                    ProductEntry.COLUMN_PRODUCT_NAME + " - " +
                    ProductEntry.COLUMN_PRODUCT_PRICE + " - " +
                    ProductEntry.COLUMN_PRODUCT_QUANTITY + " - " +
                    ProductEntry.COLUMN_PRODUCT_CATEGORY + " - " +
                    ProductEntry.COLUMN_PRODUCT_SUPPLIER_ID + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int categoryColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_CATEGORY);
            int supplierColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_ID);
            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                int currentCategory = cursor.getInt(categoryColumnIndex);
                int currentSupplier = cursor.getInt(supplierColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                productsView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentCategory + " - " +
                        currentSupplier));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }


    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displaySuppliersInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = inventoryDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                SupplierEntry._ID,
                SupplierEntry.COLUMN_SUPPLIER_NAME,
                SupplierEntry.COLUMN_SUPPLIER_PHONE};

        // Perform a query on the pets table
        Cursor cursor = db.query(
                SupplierEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        TextView suppliersView =  new TextView(this);
        suppliersView.setText("");

        try {
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.


            suppliersView.setText("The suppliers table contains " + cursor.getCount() + " suppliers.\n\n");

            suppliersView.append(SupplierEntry._ID + " - " +
                    SupplierEntry.COLUMN_SUPPLIER_NAME + " - " +
                    SupplierEntry.COLUMN_SUPPLIER_PHONE + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(SupplierEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(SupplierEntry.COLUMN_SUPPLIER_NAME);
            int phoneColumnIndex = cursor.getColumnIndex(SupplierEntry.COLUMN_SUPPLIER_PHONE);
            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPhone = cursor.getInt(phoneColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                suppliersView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPhone));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertProduct() {
        // Gets the database in write mode
        SQLiteDatabase db = inventoryDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Leonovo");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 107);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 52);
        values.put(ProductEntry.COLUMN_PRODUCT_CATEGORY, ProductEntry.CATEGORY_LAPTOP);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_ID, 1);
        // Insert a new row for Toto in the database, returning the ID of that new row.
        long rowsInserted = db.insert(ProductEntry.TABLE_NAME, null, values);
        if(rowsInserted == -1){
            Log.d(LOG_TAG, "Problem inserting data...");
            Toast.makeText(getApplicationContext(), "A problem happened while inserting data, please make sure you have a space on your mobile", Toast.LENGTH_LONG).show();
        }else{
            Log.d(LOG_TAG, rowsInserted + " rows inserted successfully...");
            Toast.makeText(getApplicationContext(), "Data inserted successfully", Toast.LENGTH_SHORT).show();
        }

        displayProductsInfo();
    }


    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertSupplier() {
        // Gets the database in write mode
        SQLiteDatabase db = inventoryDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        ContentValues values = new ContentValues();
        values.put(SupplierEntry.COLUMN_SUPPLIER_NAME, "Jarir");
        values.put(SupplierEntry.COLUMN_SUPPLIER_PHONE, 596113439);

        // Insert a new row for Toto in the database, returning the ID of that new row.
        long rowsInserted = db.insert(SupplierEntry.TABLE_NAME, null, values);
        if(rowsInserted == -1){
            Log.d(LOG_TAG, "Problem inserting data...");
            Toast.makeText(getApplicationContext(), "A problem happened while inserting data, please make sure you have a space on your mobile", Toast.LENGTH_LONG).show();
        }else{
            Log.d(LOG_TAG, rowsInserted + " rows inserted successfully...");
            Toast.makeText(getApplicationContext(), "Data inserted successfully", Toast.LENGTH_SHORT).show();
        }
        displaySuppliersInfo();
    }
}
