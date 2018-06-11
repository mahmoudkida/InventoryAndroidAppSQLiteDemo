package com.example.mahmoudkida.inventoryandroidappsqlitedemo;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryContract.ProductEntry;

/**
 * Created by skyfishjy on 10/31/14.
 */
public class ProductRecyclerCursorAdapter extends CursorRecyclerViewAdapter<ProductRecyclerCursorAdapter.ViewHolder> {
    private final Context mContext;

    public ProductRecyclerCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return  new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        // Figure out the index of each column
        int productIDIndex = cursor.getColumnIndex(ProductEntry._ID);
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
        final int productId = cursor.getInt(productIDIndex);
        final Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, productId);
        viewHolder.productCategoryView.setText(productCategoryName);
        viewHolder.saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rowsChanged = 0;
                int amount = 1;
                String[] projection = {
                        ProductEntry.COLUMN_PRODUCT_QUANTITY
                };
                Cursor currentItemCursor = mContext.getContentResolver().query(currentProductUri, projection, null, null, null);
                if (currentItemCursor.moveToFirst()) {
                    int productQuantity = currentItemCursor.getInt(0);
                    productQuantity = productQuantity - amount;
                    if (productQuantity < 0) {
                        //means the value became in minus
                        Toast.makeText(mContext, mContext.getString(R.string.editor_shipment_refill),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
                    rowsChanged = mContext.getContentResolver().update(currentProductUri, values, null, null);
                }
                if (rowsChanged == 0) {
                    // If no rows were deleted, then there was an error with the delete.
                    Toast.makeText(mContext, mContext.getString(R.string.editor_shipment_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the delete was successful and we can display a toast.
                    Toast.makeText(mContext, mContext.getString(R.string.editor_shipment_successful),
                            Toast.LENGTH_SHORT).show();
                }
                currentItemCursor.close();
            }
        });
        viewHolder.vewDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                // Form the content URI that represents the specific product that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link ProductEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.example.android.products/products/2"
                // if the product with ID 2 was clicked on.
                // Set the URI on the data field of the intent
                intent.setData(currentProductUri);
                // Launch the {@link EditorActivity} to display the data for the current product.
                mContext.startActivity(intent);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView productNameView;
        public final TextView productPriceView;
        public final TextView productQuantityView;
        public final TextView productCategoryView;
        public final Button saleButton;
        public final Button vewDetailsButton;

        public ViewHolder(View view) {
            super(view);
            productNameView = view.findViewById(R.id.productName);
            productPriceView = view.findViewById(R.id.productPrice);
            productQuantityView = view.findViewById(R.id.productQuantity);
            productCategoryView = view.findViewById(R.id.productCategory);
            saleButton = view.findViewById(R.id.sale);
            vewDetailsButton = view.findViewById(R.id.viewDetails);
        }
    }
}