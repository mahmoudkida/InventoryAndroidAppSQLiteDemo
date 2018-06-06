package com.example.mahmoudkida.inventoryandroidappsqlitedemo.data;

import android.provider.BaseColumns;

/**
 * Created by Mahmoud Kida on 6/5/2018.
 */

public final class InventoryContract {
    private InventoryContract() {
    }

    public static final class ProductEntry implements BaseColumns {

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


    }
    public static final class SupplierEntry implements BaseColumns {

        public final static String TABLE_NAME = "suppliers";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_SUPPLIER_NAME ="name";
        public final static String COLUMN_SUPPLIER_PHONE ="phone";

    }
}
