package com.example.mahmoudkida.inventoryandroidappsqlitedemo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryContract.ProductEntry;
import com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryContract.SupplierEntry;

public class ProductDetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * Identifier for the pet data loader
     */
    private static final int EXISTING_PRODUCT_LOADER = 0;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 668;
    /**
     * Content URI for the existing pet (null if it's a new pet)
     */
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
        if (mCurrentProductUri == null) {
            throw new IllegalArgumentException("You Requested a product with no details");
        } else {
            setTitle(getString(R.string.productDetails));
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }
        // Find all relevant views that we will need to read user input from
        viewProductName = findViewById(R.id.productName);
        viewProductPrice = findViewById(R.id.productPrice);
        viewProductCategory = findViewById(R.id.productCategory);
        viewProductQuantity = findViewById(R.id.productQuantity);
        viewSupplierPhone = findViewById(R.id.productSupplierPhone);
        viewSupplierName = findViewById(R.id.productSupplierName);
        //ship to buyers click handling
        View.OnClickListener decreaseProductQauntityListner = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show a toast message depending on whether or not the delete was successful.
                changeProductAmount(1, "decrease", getString(R.string.editor_shipment_successful), getString(R.string.editor_shipment_failed), getString(R.string.editor_shipment_refill));
            }
        };
        View.OnClickListener increaseProductQauntityListner = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show a toast message depending on whether or not the delete was successful.
                changeProductAmount(1, "increase", getString(R.string.editor_shipment_successful), getString(R.string.editor_shipment_failed), getString(R.string.editor_shipment_refill));
            }
        };
        //add click lisnter to decrease and ship product button
        Button ShipButton = findViewById(R.id.sellProduct);
        ShipButton.setOnClickListener(decreaseProductQauntityListner);
        Button decreaseQuantity = findViewById(R.id.decreaseQuantity);
        decreaseQuantity.setOnClickListener(decreaseProductQauntityListner);
        //add click lisnter to increase product
        Button increaseQuantity = findViewById(R.id.increaseQuantity);
        increaseQuantity.setOnClickListener(increaseProductQauntityListner);
        //refill product handling
        Button refillProduct = findViewById(R.id.refillProduct);
        refillProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ProductDetailsActivity.this);
                alert.setTitle(R.string.refill_header);
                alert.setMessage(R.string.refill_message);
                // Set an EditText view to get user input
                final EditText input = new EditText(ProductDetailsActivity.this);
                input.setId(R.id.productQuantity);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); //for decimal numbers
                alert.setView(input);
                alert.setPositiveButton(R.string.refill, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        EditText input = ((AlertDialog) dialog).findViewById(R.id.productQuantity);
                        int refillAmount = Integer.parseInt(input.getText().toString());
                        changeProductAmount(refillAmount, "increase", getString(R.string.editor_shipment_successful), getString(R.string.editor_shipment_failed), getString(R.string.editor_shipment_refill));
                    }
                });
                alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                        EditText input = ((AlertDialog) dialog).findViewById(R.id.productQuantity);
                        input.setText("");
                    }
                });
                alert.show();
            }
        });
        //call button code
        Button callButton = findViewById(R.id.callSupplier);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ProductDetailsActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ProductDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_CODE_ASK_PERMISSIONS);
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + viewSupplierPhone.getText()));
                    startActivity(intent);
                }
            }
        });
    }

    //generic method to change the product quantity
    private int changeProductAmount(int amount, String method, String successMessage, String failedMessage, String refillMessage) {
        int rowsChanged = 0;
        String[] projection = {
                ProductEntry.COLUMN_PRODUCT_QUANTITY
        };
        Cursor currentItemCursor = getContentResolver().query(mCurrentProductUri, projection, null, null, null);
        if (currentItemCursor.moveToFirst()) {
            int productQuantity = currentItemCursor.getInt(0);
            if (productQuantity >= 0) {
                if (method.equals("increase")) {
                    productQuantity = productQuantity + amount;
                } else if (method.equals("decrease")) {
                    productQuantity = productQuantity - amount;
                    if (productQuantity < 0) {
                        //means the value became in minus
                        Toast.makeText(ProductDetailsActivity.this, refillMessage,
                                Toast.LENGTH_SHORT).show();
                        rowsChanged = -1;
                        return rowsChanged;
                    }
                }
                ContentValues values = new ContentValues();
                values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
                rowsChanged = getContentResolver().update(mCurrentProductUri, values, null, null);
            }
        }
        if (rowsChanged == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(ProductDetailsActivity.this, failedMessage,
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(ProductDetailsActivity.this, successMessage,
                    Toast.LENGTH_SHORT).show();
        }
        currentItemCursor.close();
        return rowsChanged;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Call Permission Granted..Please dial again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Call permission not granted", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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
            int quantity = cursor.getInt(quantityColumnIndex);
            int category = cursor.getInt(categoryColumnIndex);
            String categoryName;
            switch (category) {
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
            viewProductName.setText(name);
            viewProductPrice.setText(getString(R.string.price) + ": " + Integer.toString(price) + "$");
            viewProductCategory.setText(getString(R.string.category) + ": " + categoryName);
            viewProductQuantity.setText(getString(R.string.quantity) + ": " + Integer.toString(quantity));
            //getting supplier information
            String[] supplierProjection = {
                    SupplierEntry._ID,
                    SupplierEntry.COLUMN_SUPPLIER_NAME,
                    SupplierEntry.COLUMN_SUPPLIER_PHONE};
            // This loader will execute the ContentProvider's query method on a background thread
            Cursor supplierCursor = getContentResolver().query(
                    // Query the content URI for the current pet
                    ContentUris.withAppendedId(SupplierEntry.CONTENT_URI, supplierId)
                    , supplierProjection
                    , null
                    , null,
                    null);
            if (supplierCursor != null && cursor.getCount() == 1) {
                if (supplierCursor.moveToFirst()) {
                    // Find the columns of pet attributes that we're interested in
                    int supplierNameColumnIndex = supplierCursor.getColumnIndex(SupplierEntry.COLUMN_SUPPLIER_NAME);
                    int supplierPhoneColumnIndex = supplierCursor.getColumnIndex(SupplierEntry.COLUMN_SUPPLIER_PHONE);
                    // Extract out the value from the Cursor for the given column index
                    String supplierName = supplierCursor.getString(supplierNameColumnIndex);
                    String supplierPhone = supplierCursor.getString(supplierPhoneColumnIndex);
                    // Update the views on the screen with the values from the database
                    viewSupplierName.setText(getString(R.string.supplier_name_label) + ": " + supplierName);
                    viewSupplierPhone.setText(getString(R.string.supplier_phone_label) + ": " + supplierPhone);
                }
            }
            supplierCursor.close();
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
        getMenuInflater().inflate(R.menu.details_menu, menu);
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
                // Set the URI on the data field of the intent
                intent.setData(mCurrentProductUri);
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
                finish();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        Intent myIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
        finish();
    }
}
