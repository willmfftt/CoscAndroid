package com.cosc.bandfanapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cosc.bandfanapp.R;
import com.cosc.bandfanapp.model.User;
import com.orm.SugarRecord;

import java.util.Iterator;

/**
 * @author William Moffitt
 * @version 1.0 12/8/15
 */
public class NavController implements NavigationView.OnNavigationItemSelectedListener {

    private Activity mActivity;
    private Toolbar mToolbar;

    public NavController(Activity activity, Toolbar toolbar) {
        mActivity = activity;
        mToolbar = toolbar;

        initialize();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        boolean changingScreens = false;

        if (id == R.id.nav_create_band) {
            if (mActivity.getClass() != CreateBandActivity.class) {
                Intent intent = new Intent(mActivity, CreateBandActivity.class);
                mActivity.startActivity(intent);
                changingScreens = true;
            }
        }
        if (id == R.id.nav_view_bands) {
            if (mActivity.getClass() != ListBandsActivity.class) {
                Intent intent = new Intent(mActivity, ListBandsActivity.class);
                mActivity.startActivity(intent);
                changingScreens = true;
            }
        }
        if (id == R.id.nav_create_band_member) {
            if (mActivity.getClass() != CreateBandMemberActivity.class) {
                Intent intent = new Intent(mActivity, CreateBandMemberActivity.class);
                mActivity.startActivity(intent);
                changingScreens = true;
            }
        }

        if (changingScreens) {
            if (mActivity.getClass() != ListBandsActivity.class) {
                mActivity.finish();
            }
        }

        DrawerLayout drawer = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else {
            return false;
        }
    }

    private void initialize() {
        DrawerLayout drawer = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(mActivity, drawer, mToolbar
                , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) mActivity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = LayoutInflater.from(mActivity).inflate(R.layout.nav_header_main
                , navigationView, false);
        navigationView.addHeaderView(header);

        Iterator<User> users = SugarRecord.findAll(User.class);
        User user = null;
        while (users.hasNext()) {
            user = users.next();
        }

        if (user != null) {
            TextView fullName = (TextView) header.findViewById(R.id.full_name);
            StringBuilder sb = new StringBuilder();
            sb.append(user.getFirstName());
            sb.append(" ");
            sb.append(user.getLastName());
            fullName.setText(sb);

            TextView email = (TextView) header.findViewById(R.id.email);
            email.setText(user.getEmail());
        }
    }

}
