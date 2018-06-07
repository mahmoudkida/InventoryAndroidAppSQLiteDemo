package com.example.mahmoudkida.inventoryandroidappsqlitedemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product>{
    public ProductAdapter(Context context, ArrayList<Product> products) {
        super(context, 0, products);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.product_item, parent, false);
        }
        Product product = getItem(position);
        //get views
        TextView productNameView = listItemView.findViewById(R.id.productName);
        TextView productPriceView = listItemView.findViewById(R.id.productPrice);
        TextView productQuantityView = listItemView.findViewById(R.id.productQuantity);
        //set views data
        productNameView.setText(product.getName());
        productPriceView.setText(product.getPrice());
        productQuantityView.setText(product.getQuantity());

        return listItemView;
    }
}
