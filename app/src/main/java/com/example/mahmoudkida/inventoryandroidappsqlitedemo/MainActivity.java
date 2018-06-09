package com.example.mahmoudkida.inventoryandroidappsqlitedemo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.mahmoudkida.inventoryandroidappsqlitedemo.data.InventoryDBHelper;

public class MainActivity extends AppCompatActivity {
    private InventoryDBHelper inventoryDbHelper;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inventoryDbHelper = new InventoryDBHelper(this);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_view);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment productPragment = ProductListFragment.newInstance();
        fragmentManager.beginTransaction().replace(R.id.content_frame, productPragment).commit();
        navigationView.setCheckedItem(R.id.products_nav);
        setTitle(getString(R.string.product_list));
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.products_nav) {
                            setTitle(getString(R.string.product_list));
                            FragmentManager fragmentManager = getFragmentManager();
                            Fragment productPragment = ProductListFragment.newInstance();
                            fragmentManager.beginTransaction().replace(R.id.content_frame, productPragment).commit();
                        } else if (menuItem.getItemId() == R.id.suppliers_nav) {
                            setTitle(getString(R.string.supplier_list));
                            FragmentManager fragmentManager = getFragmentManager();
                            Fragment suppliersListFragment = SuppliersListFragment.newInstance();
                            fragmentManager.beginTransaction().replace(R.id.content_frame, suppliersListFragment).commit();
                        }
                        menuItem.setChecked(true);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
