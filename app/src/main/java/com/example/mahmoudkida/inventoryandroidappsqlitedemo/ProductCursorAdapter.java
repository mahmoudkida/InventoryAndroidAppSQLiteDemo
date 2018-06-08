package com.example.mahmoudkida.inventoryandroidappsqlitedemo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryContract.ProductEntry;

public class ProductCursorAdapter extends CursorAdapter {
    public static final int CATEGORY_LAPTOP = 0;
    public static final int CATEGORY_TABLET = 1;
    public static final int CATEGORY_MOBILE = 2;

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        //get views
        TextView productNameView = view.findViewById(R.id.productName);
        TextView productPriceView = view.findViewById(R.id.productPrice);
        TextView productQuantityView = view.findViewById(R.id.productQuantity);
        TextView productCategoryView = view.findViewById(R.id.productCategory);
        // Figure out the index of each column
        int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int categoryColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_CATEGORY);
        //set views data
        productNameView.setText(cursor.getString(nameColumnIndex));
        productPriceView.setText(cursor.getInt(priceColumnIndex));
        productQuantityView.setText(Integer.toString(cursor.getInt(quantityColumnIndex)));
        int productCategory = cursor.getInt(categoryColumnIndex);
        String productCategoryName;
        switch (productCategory) {
            case CATEGORY_LAPTOP:
                productCategoryName = context.getString(R.string.laptop);
                break;
            case CATEGORY_TABLET:
                productCategoryName = context.getString(R.string.tablet);
                break;
            case CATEGORY_MOBILE:
                productCategoryName = context.getString(R.string.mobile);
                break;
            default:
                productCategoryName = context.getString(R.string.unkown_category);
                break;
        }
        productCategoryView.setText(productCategoryName);
    }
}
