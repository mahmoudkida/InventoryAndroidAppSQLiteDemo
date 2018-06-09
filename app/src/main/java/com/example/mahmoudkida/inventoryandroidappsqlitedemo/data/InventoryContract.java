package com.example.mahmoudkida.inventoryandroidappsqlitedemo.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mahmoud Kida on 6/5/2018.
 */

public final class InventoryContract {
    private InventoryContract() { }
    public static final String CONTENT_AUTHORITY_PRODUCT = "com.example.android.product";
    public static final String CONTENT_AUTHORITY_SUPPLIER = "com.example.android.supplier";
    public static final Uri BASE_CONTENT_URI_PRODUCT = Uri.parse("content://" + CONTENT_AUTHORITY_PRODUCT);
    public static final Uri BASE_CONTENT_URI_SUPPLIER = Uri.parse("content://" + CONTENT_AUTHORITY_SUPPLIER);
    public static final String PATH_PRODUCTS = "products";
    public static final String PATH_SUPPLIERS = "suppliers";



    public static final class ProductEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI_PRODUCT, PATH_PRODUCTS);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY_PRODUCT + "/" + PATH_PRODUCTS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY_PRODUCT + "/" + PATH_PRODUCTS;

        public final static String TABLE_NAME = "products";

        /**
         * Unique ID number for the inventory item (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        //column names
        public final static String COLUMN_PRODUCT_NAME ="name";
        public final static String COLUMN_PRODUCT_PRICE ="price";
        public final static String COLUMN_PRODUCT_QUANTITY ="quantity";
        public final static String COLUMN_PRODUCT_CATEGORY ="category";
        public final static String COLUMN_PRODUCT_SUPPLIER_ID ="supplier_id";


        //category types
        public static final int CATEGORY_LAPTOP = 0;
        public static final int CATEGORY_TABLET = 1;
        public static final int CATEGORY_MOBILE = 2;
        public static final int CATEGORY_OTHER = 3;
        public static boolean isValidCategory(int category) {
            return category == CATEGORY_LAPTOP || category == CATEGORY_TABLET || category == CATEGORY_MOBILE || category == CATEGORY_OTHER;
        }
    }

    public static final class SupplierEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI_SUPPLIER, PATH_SUPPLIERS);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY_SUPPLIER + "/" + PATH_SUPPLIERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY_SUPPLIER + "/" + PATH_SUPPLIERS;
        public final static String TABLE_NAME = "suppliers";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_SUPPLIER_NAME ="name";
        public final static String COLUMN_SUPPLIER_PHONE ="phone";

    }
}
