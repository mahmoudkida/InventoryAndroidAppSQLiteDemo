package com.example.mahmoudkida.inventoryandroidappsqlitedemo;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryContract.ProductEntry;
import com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryContract.SupplierEntry;

public class ProductEditActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * Identifier for the pet data loader
     */
    private static final int EXISTING_PRODUCT_LOADER = 0;
    /**
     * Content URI for the existing pet (null if it's a new pet)
     */
    private Uri mCurrentProductUri;
    private EditText viewProductName;
    private EditText viewProductPrice;
    private Spinner viewProductCategory;
    private EditText viewProductQuantity;
    private Spinner viewProductSupplier;
    private int mCategory = -1;
    private int mSupplierId = -1;
    /**
     * Boolean flag that keeps track of whether the supplier has been edited (true) or not (false)
     */
    private boolean mProductHasChanged = false;
    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mPetHasChanged boolean to true.
     */
    private final View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mProductHasChanged = true;
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);
        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();
        // If the intent DOES NOT contain a pet content URI, then we know that we are
        // creating a new pet.
        if (mCurrentProductUri == null) {
            // This is a new pet, so change the app bar to say "Add a Pet"
            setTitle(getString(R.string.editor_activity_title_new_Product));
            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a pet that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing pet, so change app bar to say "Edit Pet"
            setTitle(getString(R.string.edit_product));
            // Initialize a loader to read the pet data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }
        viewProductName = findViewById(R.id.productName);
        viewProductPrice = findViewById(R.id.productPrice);
        viewProductQuantity = findViewById(R.id.productQuantity);
        viewProductCategory = findViewById(R.id.productCategory);
        viewProductSupplier = findViewById(R.id.productSupplier);
        //set touch listner
        viewProductName.setOnTouchListener(mTouchListener);
        viewProductPrice.setOnTouchListener(mTouchListener);
        viewProductQuantity.setOnTouchListener(mTouchListener);
        viewProductCategory.setOnTouchListener(mTouchListener);
        viewProductSupplier.setOnTouchListener(mTouchListener);
        setupCategorySpinner();
        setupSupplierSpinner();
    }

    private void setupSupplierSpinner() {
        String[] supplierProjection = {
                SupplierEntry._ID,
                SupplierEntry.COLUMN_SUPPLIER_NAME,
                SupplierEntry.COLUMN_SUPPLIER_PHONE};
        // This loader will execute the ContentProvider's query method on a background thread
        Cursor supplierCursor = getContentResolver().query(
                SupplierEntry.CONTENT_URI         // Query the content URI for the current pet
                , supplierProjection
                , null
                , null
                ,null);
        if (supplierCursor.getCount() > 0) {
            String[] from = new String[]{SupplierEntry.COLUMN_SUPPLIER_NAME};
            // create an array of the display item we want to bind our data to
            int[] to = new int[]{android.R.id.text1};
            SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item,
                    supplierCursor, from, to);
            mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            viewProductSupplier.setAdapter(mAdapter);
        }
        // Set the integer mSelected to the constant values
        viewProductSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView parent, View view,
                                       int pos, long log) {
                Cursor c = (Cursor) parent.getItemAtPosition(pos);
                mSupplierId = c.getInt(c.getColumnIndexOrThrow(SupplierEntry._ID));
            }

            public void onNothingSelected(AdapterView arg0) {
            }
        });
    }

    private void setupCategorySpinner() {
// Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_category_options, android.R.layout.simple_spinner_item);
        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // Apply the adapter to the spinner
        viewProductCategory.setAdapter(genderSpinnerAdapter);
        // Set the integer mSelected to the constant values
        viewProductCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.laptop))) {
                        mCategory = ProductEntry.CATEGORY_LAPTOP;
                    } else if (selection.equals(getString(R.string.tablet))) {
                        mCategory = ProductEntry.CATEGORY_TABLET;
                    } else if (selection.equals(getString(R.string.mobile))) {
                        mCategory = ProductEntry.CATEGORY_MOBILE;
                    } else {
                        mCategory = ProductEntry.CATEGORY_OTHER;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCategory = ProductEntry.CATEGORY_OTHER;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be u dated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentProductUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                Boolean closeActivity = saveProduct();
                // Exit activity
                if(closeActivity){
                    finish();
                }
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(ProductEditActivity.this);
                    return true;
                }
                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(ProductEditActivity.this);
                            }
                        };
                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Get user input from editor and save pet into database.
     */
    private Boolean saveProduct() {
        Boolean closeActivity = false;
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String productName = viewProductName.getText().toString().trim();
        String productPrice = viewProductPrice.getText().toString().trim();
        String productQuantity = viewProductQuantity.getText().toString().trim();

        // Check if this is supposed to be a new pet
        // and check if all the fields in the editor are blank
        if (mCurrentProductUri == null &&
                TextUtils.isEmpty(productName) && TextUtils.isEmpty(productPrice)
                && TextUtils.isEmpty(productQuantity)) {
            // Since no fields were modified, we can return early without creating a new pet.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            closeActivity = true;
            return closeActivity;
        }

        //prevent submit if one item is empty
        if (TextUtils.isEmpty(productName) || TextUtils.isEmpty(productPrice)
                || TextUtils.isEmpty(productQuantity) || mCategory == -1 || mSupplierId == -1) {
            // Since no fields were modified, we can return early without creating a new pet.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            Toast.makeText(this, getString(R.string.editor_fill_all_fields),
                    Toast.LENGTH_LONG).show();
            closeActivity = false;
            return closeActivity;
        }

        //get integer values of price and quantity
        int productPriceInt = Integer.parseInt(productPrice);
        int productQuantityInt = Integer.parseInt(productQuantity);
        //prevent submit of minus values
        if(productPriceInt < 0 || productQuantityInt < 0){
            Toast.makeText(this, getString(R.string.editor_filled_minus_value),
                    Toast.LENGTH_LONG).show();
            closeActivity = false;
            return closeActivity;
        }

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, productPriceInt);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantityInt);
        values.put(ProductEntry.COLUMN_PRODUCT_CATEGORY, mCategory);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_ID, mSupplierId);
        // Determine if this is a new or existing pet by checking if mCurrentPetUri is null or not
        if (mCurrentProductUri == null) {
            // This is a NEW pet, so insert a new pet into the provider,
            // returning the content URI for the new pet.
            Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);
            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_supplier_failed),
                        Toast.LENGTH_SHORT).show();
                closeActivity = false;
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_supplier_successful),
                        Toast.LENGTH_SHORT).show();
                closeActivity = true;
            }
        } else {
            // Otherwise this is an EXISTING pet, so update the pet with content URI: mCurrentPetUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentPetUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);
            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_supplier_failed),
                        Toast.LENGTH_SHORT).show();
                closeActivity = false;
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_supplier_successful),
                        Toast.LENGTH_SHORT).show();
                closeActivity = true;
            }
        }
        return closeActivity;
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }
        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };
        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
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
            viewProductName.setText(name);
            viewProductPrice.setText(Integer.toString(price));
            viewProductQuantity.setText(Integer.toString(quantity));
            viewProductCategory.setSelection(category);
            for (int i = 0; i < viewProductSupplier.getCount(); i++) {
                Cursor value = (Cursor) viewProductSupplier.getItemAtPosition(i);
                long id = value.getLong(value.getColumnIndex("_id"));
                if (id == supplierId) {
                    viewProductSupplier.setSelection(i);
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        viewProductName.setText("");
        viewProductPrice.setText("");
        viewProductQuantity.setText("");
        viewProductCategory.setSelection(0);
        viewProductSupplier.setSelection(0);
    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
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
     * Prompt the user to confirm that they want to delete this pet.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteSupplier();
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
    private void deleteSupplier() {
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
