package com.example.cci.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.cci.R;
import com.example.cci.data.Tools;
import com.example.cci.frament.FragmentHome;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {

    ActionBarDrawerToggle toggle;
    private ProgressDialog pDialog;
    private Context ctx;
    private Toolbar toolbar;
    private ActionBar actionbar;
    private NavigationView navigationView;
    private View parent_view;
    private DrawerLayout drawerLayout;
    private JSONArray jsonArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = getApplicationContext();
        initToolbar();
        initDrawerMenu();
        setupDrawerLayout();
        DisplayFragment(R.id.nav_home, getString(R.string.title_nav_home));
        Tools.systemBarLolipop(this);
        hideKeyboard();
        profileInfo(getApplicationContext());
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
    }

    private void initDrawerMenu() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                hideKeyboard();
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                DisplayFragment(menuItem.getItemId(), menuItem.getTitle().toString());
                drawer.closeDrawers();
                return true;
            }
        });
    }

    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Fragment fragment = null;

        NavigationView view = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();

        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.getTitle().equals(getString(R.string.title_nav_home))) {
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (menuItem.getTitle().equals(getString(R.string.title_nav_close))) {
                    finish();
                    System.exit(0);
                }
                return true;
            }
        });
    }

    private void DisplayFragment(int id, String title) {
        actionbar.setDisplayShowCustomEnabled(false);
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setTitle(title);
        FragmentHome fragment = null;
        switch (id) {
            case R.id.nav_home:
                fragment = new FragmentHome();
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_content, fragment);
            fragmentTransaction.commit();
        }
    }

    private void profileInfo(Context context) {
        NavigationView nv=(NavigationView)findViewById(R.id.nav_view);
        View vi=nv.getHeaderView(0);
        ImageView nav_user= (ImageView)vi.findViewById(R.id.logo);
        nav_user.setImageResource(R.drawable.ic_logo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_refresh){
            if (Tools.InternetConnection(this)) {
                try {
                    item.setEnabled(false);
                    try {
                        FragmentHome fg = new FragmentHome();
                        fg.RefreshData(MainActivity.this);
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                    item.setEnabled(true);
                } catch (Exception e) {
                    Toast.makeText(ctx, getString(R.string.products_not_loaded), Toast.LENGTH_SHORT).show();
                }
            } else{
                Toast.makeText(ctx, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }*/

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}