package com.example.mahmoudkida.inventoryandroidappsqlitedemo;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryContract.ProductEntry;
import com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryContract.SupplierEntry;

public class ProductDetailsActivity extends AppCompatActivity  implements
        LoaderManager.LoaderCallbacks<Cursor>{
/** Identifier for the pet data loader */
private static final int EXISTING_PRODUCT_LOADER = 0;
/** Content URI for the existing pet (null if it's a new pet) */
private Uri mCurrentProductUri;

private TextView viewProductName;
private TextView viewProductPrice;
private TextView viewProductCategory;
private TextView viewProductQuantity;
private TextView viewSupplierPhone;
private TextView viewSupplierName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        // If the intent DOES NOT contain a pet content URI, then we know that we are
        // creating a new pet.
        if (mCurrentProductUri == null){

        throw new IllegalArgumentException("You Requested a product with no details");
        }
        else{
        setTitle(getString(R.string.productDetails));
        getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);

        }
        // Find all relevant views that we will need to read user input from
        viewProductName = findViewById(R.id.productName);
        viewProductPrice =  findViewById(R.id.productPrice);
        viewProductCategory =  findViewById(R.id.productCategory);
        viewProductQuantity =  findViewById(R.id.productQuantity);
        viewSupplierPhone =  findViewById(R.id.productSupplierPhone);
        viewSupplierName =  findViewById(R.id.productSupplierName);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all pet attributes, define a projection that contains
        // all columns from the pet table
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_CATEGORY,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_ID};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentProductUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int categoryColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_CATEGORY);
            int supplierColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_ID);

            // Extract out the value from the Cursor for the given column index
            int supplierId = cursor.getInt(supplierColumnIndex);
            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int qauntity = cursor.getInt(quantityColumnIndex);
            int category = cursor.getInt(categoryColumnIndex);
            String categoryName = "";

            switch (category){
                case ProductEntry.CATEGORY_LAPTOP:
                    categoryName = getString(R.string.laptop);
                    break;
                case ProductEntry.CATEGORY_TABLET:
                    categoryName = getString(R.string.tablet);
                    break;
                case ProductEntry.CATEGORY_MOBILE:
                    categoryName = getString(R.string.mobile);
                    break;
                default:
                    categoryName = getString(R.string.unkown_category);
                    break;
            }


            // Update the views on the screen with the values from the database
            viewSupplierName.setText(name);
            viewProductPrice.setText(Integer.toString(price));
            viewProductCategory.setText(categoryName);
            viewProductQuantity.setText(Integer.toString(qauntity));

            //getting supplier information

            String[] supplierProjection = {
                    SupplierEntry._ID,
                    SupplierEntry.COLUMN_SUPPLIER_NAME,
                    SupplierEntry.COLUMN_SUPPLIER_PHONE };

            // This loader will execute the ContentProvider's query method on a background thread
            Cursor supplierCursor = getContentResolver().query(
                    ContentUris.withAppendedId(SupplierEntry.CONTENT_URI, supplierId)         // Query the content URI for the current pet
                    , supplierProjection
                    , null
                    , null);


            if (supplierCursor.moveToFirst()) {
                // Find the columns of pet attributes that we're interested in
                int supplierNameColumnIndex = cursor.getColumnIndex(SupplierEntry.COLUMN_SUPPLIER_NAME);
                int supplierPhoneColumnIndex = cursor.getColumnIndex(SupplierEntry.COLUMN_SUPPLIER_PHONE);


                // Extract out the value from the Cursor for the given column index
                String supplierName = cursor.getString(supplierNameColumnIndex);
                String supplierPhone = cursor.getString(supplierPhoneColumnIndex);


                // Update the views on the screen with the values from the database
                viewSupplierName.setText(supplierName);
                viewSupplierPhone.setText(supplierPhone);
            }
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        viewSupplierName.setText("");
        viewProductPrice.setText("");
        viewProductCategory.setText("");
        viewProductQuantity.setText("");
        viewSupplierName.setText("");
        viewSupplierPhone.setText("");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_edit:
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(this, ProductEditActivity.class);

                // Form the content URI that represents the specific product that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link ProductEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.example.android.products/products/2"
                // if the product with ID 2 was clicked on.
                Uri currentProductUri = ProductEntry.CONTENT_URI;

                // Set the URI on the data field of the intent
                intent.setData(currentProductUri);

                // Launch the {@link EditorActivity} to display the data for the current product.
                startActivity(intent);
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Prompt the user to confirm that they want to delete this pet.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    /**
     * Perform the deletion of the pet in the database.
     */
    private void deleteProduct() {
        // Only perform the delete if this is an existing pet.
        if (mCurrentProductUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentSupplierUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_supplier_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_supplier_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }



}
