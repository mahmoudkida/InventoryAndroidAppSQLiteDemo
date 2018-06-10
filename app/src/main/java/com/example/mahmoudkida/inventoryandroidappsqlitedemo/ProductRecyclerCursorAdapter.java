package com.example.mahmoudkida.inventoryandroidappsqlitedemo;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryContract.ProductEntry;

/**
 * Created by skyfishjy on 10/31/14.
 */
public class ProductRecyclerCursorAdapter extends CursorRecyclerViewAdapter<ProductRecyclerCursorAdapter.ViewHolder>{

    private Context mContext;
    public ProductRecyclerCursorAdapter(Context context,Cursor cursor){
        super(context,cursor);
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productNameView;
        public TextView productPriceView;
        public TextView productQuantityView;
        public TextView productCategoryView;
        public ViewHolder(View view) {
            super(view);
            productNameView = view.findViewById(R.id.productName);
            productPriceView = view.findViewById(R.id.productPrice);
            productQuantityView = view.findViewById(R.id.productQuantity);
            productCategoryView = view.findViewById(R.id.productCategory);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        // Figure out the index of each column
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int categoryColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_CATEGORY);
        //set views data
        viewHolder.productNameView.setText(cursor.getString(nameColumnIndex));
        viewHolder.productPriceView.setText(mContext.getString(R.string.price) + ": " + Integer.toString(cursor.getInt(priceColumnIndex)) + "$");
        viewHolder.productQuantityView.setText(mContext.getString(R.string.quantity) + ": " + Integer.toString(cursor.getInt(quantityColumnIndex)));
        int productCategory = cursor.getInt(categoryColumnIndex);
        String productCategoryName;
        switch (productCategory) {
            case ProductEntry.CATEGORY_LAPTOP:
                productCategoryName = mContext.getString(R.string.category) + ": " + mContext.getString(R.string.laptop);
                break;
            case ProductEntry.CATEGORY_TABLET:
                productCategoryName = mContext.getString(R.string.category) + ": " + mContext.getString(R.string.tablet);
                break;
            case ProductEntry.CATEGORY_MOBILE:
                productCategoryName = mContext.getString(R.string.category) + ": " + mContext.getString(R.string.mobile);
                break;
            default:
                productCategoryName = mContext.getString(R.string.unkown_category);
                break;
        }
        viewHolder.productCategoryView.setText(productCategoryName);
    }
}