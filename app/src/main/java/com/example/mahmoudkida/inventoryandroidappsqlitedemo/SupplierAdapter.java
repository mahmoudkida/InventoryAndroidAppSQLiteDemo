package com.example.mahmoudkida.inventoryandroidappsqlitedemo;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by Mahmoud Kida on 6/6/2018.
 */

public class SupplierAdapter extends ArrayAdapter<Supplier> {
    public SupplierAdapter(Context context, ArrayList<Supplier> suppliers) {
        super(context, 0, suppliers);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.supplier_item, parent, false);
        }
        Supplier supplier = getItem(position);
        //get views
        TextView supplierIdView = listItemView.findViewById(R.id.supplierId);
        TextView supplierNameView = listItemView.findViewById(R.id.supplierName);
        TextView supplierPhoneView = listItemView.findViewById(R.id.supplierPhone);
        //set views data
        supplierIdView.setText("ID: " + Integer.toString(supplier.getID()));
        supplierNameView.setText(supplier.getName());
        supplierPhoneView.setText(Integer.toString(supplier.getPhone()));

        return listItemView;
    }

}
