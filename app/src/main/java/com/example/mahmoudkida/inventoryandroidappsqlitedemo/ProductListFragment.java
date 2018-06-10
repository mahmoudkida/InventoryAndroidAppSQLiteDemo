package com.example.mahmoudkida.inventoryandroidappsqlitedemo;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryContract.ProductEntry;

public class ProductListFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * Identifier for the product data loader
     */
    private static final int PRODUCT_LOADER = 0;
    /**
     * Adapter for the ListView
     */
    ProductRecyclerCursorAdapter mCursorAdapter;

    public static ProductListFragment newInstance() {
        return new ProductListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        RecyclerView productsList = view.findViewById(R.id.productsList);
        mCursorAdapter = new ProductRecyclerCursorAdapter(getActivity(),null);
        //mCursorAdapter = new ProductCursorAdapter(getActivity(), null);
        LinearLayoutManager mLayout= new LinearLayoutManager(getActivity());
        mLayout.setOrientation(LinearLayoutManager.VERTICAL);
        productsList.setLayoutManager(mLayout);

        productsList.setAdapter(mCursorAdapter);

        //TODO:set empty view
        //productsList.(view.findViewById(R.id.emptyView));
        // Setup the item click listener
//        productsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                // Create new intent to go to {@link EditorActivity}
//                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
//                // Form the content URI that represents the specific product that was clicked on,
//                // by appending the "id" (passed as input to this method) onto the
//                // {@link ProductEntry#CONTENT_URI}.
//                // For example, the URI would be "content://com.example.android.products/products/2"
//                // if the product with ID 2 was clicked on.
//                Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
//                // Set the URI on the data field of the intent
//                intent.setData(currentProductUri);
//                // Launch the {@link EditorActivity} to display the data for the current product.
//                startActivity(intent);
//            }
//        });
        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
        // Setup FAB to open EditorActivity
        FloatingActionButton fab = view.findViewById(R.id.addProduct);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProductEditActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_CATEGORY,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_ID};
        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(getActivity(),   // Parent activity context
                ProductEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link ProductCursorAdapter} with this new cursor containing updated product data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}
