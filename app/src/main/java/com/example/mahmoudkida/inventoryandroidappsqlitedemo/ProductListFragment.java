package com.example.mahmoudkida.inventoryandroidappsqlitedemo;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
    private ProductRecyclerCursorAdapter mCursorAdapter;
    private RecyclerView productsList;
    private LinearLayout emptyView;

    public static ProductListFragment newInstance() {
        return new ProductListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        productsList = view.findViewById(R.id.productsList);
        emptyView = view.findViewById(R.id.emptyView);
        mCursorAdapter = new ProductRecyclerCursorAdapter(getActivity(), null);
        LinearLayoutManager mLayout = new LinearLayoutManager(getActivity());
        mLayout.setOrientation(LinearLayoutManager.VERTICAL);
        productsList.setLayoutManager(mLayout);
        productsList.setAdapter(mCursorAdapter);
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
        // Update adapter with this new cursor containing updated product data
        if (data == null && data.getCount() < 1) {
            productsList.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            mCursorAdapter.swapCursor(data);
            productsList.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}
