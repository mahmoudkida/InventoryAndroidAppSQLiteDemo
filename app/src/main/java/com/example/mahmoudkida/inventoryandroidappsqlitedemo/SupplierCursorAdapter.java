package com.example.mahmoudkida.inventoryandroidappsqlitedemo;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryContract.SupplierEntry;

/**
 * Created by Mahmoud Kida on 6/6/2018.
 */

public class SupplierCursorAdapter extends CursorAdapter {
    public SupplierCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.supplier_item, parent, false);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        // Check if an existing view is being reused, otherwise inflate the view

        //get views
        TextView supplierIdView = view.findViewById(R.id.supplierId);
        TextView supplierNameView = view.findViewById(R.id.supplierName);
        TextView supplierPhoneView = view.findViewById(R.id.supplierPhone);
        //get columns indexes
        int idColumnIndex = cursor.getColumnIndex(SupplierEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(SupplierEntry.COLUMN_SUPPLIER_NAME);
        int phoneColumnIndex = cursor.getColumnIndex(SupplierEntry.COLUMN_SUPPLIER_PHONE);

        //set views data
        supplierIdView.setText("ID: " + Integer.toString(cursor.getInt(idColumnIndex)));
        supplierNameView.setText(cursor.getString(nameColumnIndex));
        supplierPhoneView.setText(cursor.getString(phoneColumnIndex));

    }

}
