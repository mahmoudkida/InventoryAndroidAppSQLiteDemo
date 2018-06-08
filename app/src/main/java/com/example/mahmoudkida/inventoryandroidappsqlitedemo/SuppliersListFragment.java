package com.example.mahmoudkida.inventoryandroidappsqlitedemo;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryContract.SupplierEntry;
import com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryDBHelper;

import java.util.ArrayList;


public class SuppliersListFragment extends Fragment {

    public static SuppliersListFragment newInstance() {
        return new SuppliersListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_suppliers_list, container, false);

        final ArrayList<Supplier> suppliers = fetchAllSuppliers();
        SupplierAdapter supplierAdapter = new SupplierAdapter(getActivity(), suppliers);
        ListView suppliersList = view.findViewById(R.id.suppliersList);
        suppliersList.setAdapter(supplierAdapter);

        return view;
    }

    private ArrayList<Supplier> fetchAllSuppliers (){
        InventoryDBHelper inventoryDbHelper = new InventoryDBHelper(getActivity());

        ArrayList<Supplier> suppliersList = new ArrayList<Supplier>();
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

        try {
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.



            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(SupplierEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(SupplierEntry.COLUMN_SUPPLIER_NAME);
            int phoneColumnIndex = cursor.getColumnIndex(SupplierEntry.COLUMN_SUPPLIER_PHONE);
            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {

                //create
                suppliersList.add(new Supplier(cursor.getInt(idColumnIndex),
                        cursor.getString(nameColumnIndex),
                        cursor.getInt(phoneColumnIndex))) ;
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
        return suppliersList;

    }
}
