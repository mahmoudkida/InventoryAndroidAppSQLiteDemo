package com.example.mahmoudkida.inventoryandroidappsqlitedemo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryContract.ProductEntry;
import com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryContract.SupplierEntry;
import com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryDBHelper;

import java.util.ArrayList;

public class ProductsListActivity extends AppCompatActivity {

    private InventoryDBHelper inventoryDbHelper = new InventoryDBHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        final ArrayList<Product> products = fetchAllProducts();
        ProductAdapter productAdapter = new ProductAdapter(this, products);
        ListView productsList = findViewById(R.id.productsList);
        productsList.setAdapter(productAdapter);
        productsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product currentProduct = products.get(i);
               Intent redirect = new Intent(getApplicationContext(), ProductDetailsActivity.class);
                redirect.putExtra("productName", currentProduct.getName());
                redirect.putExtra("productPrice", currentProduct.getPrice());
                redirect.putExtra("productQuantity", currentProduct.getQuantity());
                redirect.putExtra("productCategory", currentProduct.getCategory());
                redirect.putExtra("productSupplier", currentProduct.getSupplierId());
                startActivity(redirect);
            }
        });
    }


    private ArrayList<Product> fetchAllProducts() {

        ArrayList<Product> productsList = new ArrayList<Product>();
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


        try {
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.

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

                int supplierId = cursor.getInt(supplierColumnIndex);
                Product currentProduct = new Product(cursor.getInt(idColumnIndex),
                        cursor.getString(nameColumnIndex),
                        cursor.getInt(priceColumnIndex),
                        cursor.getInt(quantityColumnIndex),
                        cursor.getInt(categoryColumnIndex),
                        supplierId);

                String[] supplierProjection = {  SupplierEntry.COLUMN_SUPPLIER_NAME };

                String whereCluaseColumns = SupplierEntry._ID+ " =?";
                String[] whereColumns = {Integer.toString(supplierId)};
                Cursor supplierCursor = db.query(
                        SupplierEntry.TABLE_NAME,   // The table to query
                        supplierProjection,            // The columns to return
                        whereCluaseColumns,                  // The columns for the WHERE clause
                        whereColumns,                  // The values for the WHERE clause
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        null);                   // The sort order
                try {
                    int supplierNameColumnIndex = cursor.getColumnIndex(SupplierEntry.COLUMN_SUPPLIER_NAME);
                    while (supplierCursor.moveToNext()) {
                       currentProduct.setSupplierName(supplierCursor.getString(0));
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    supplierCursor.close();
                }
                //add this product to the list
                productsList.add(currentProduct);
            }
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
        return productsList;
    }
}
