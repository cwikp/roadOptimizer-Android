package com.roadoptimizer.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.Drawer;

import com.roadoptimizer.R;
import com.roadoptimizer.activities.RideMapActivity;


public class NavigationDrawer {

    Activity activity;
    Toolbar toolbar;

    public NavigationDrawer(Activity activity, Toolbar toolbar){
        this.activity=activity;
        this.toolbar=toolbar;
    }

    public void setupDrawer() {
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Share a Toast");
        SecondaryDrawerItem item2 = (SecondaryDrawerItem) new SecondaryDrawerItem().withName("Events List");
        final SecondaryDrawerItem item3 = (SecondaryDrawerItem) new SecondaryDrawerItem().withName("Create new event");

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.drawer_background)
                .addProfiles(
                        new ProfileDrawerItem().withName("John Smith").withEmail("testmail@gmsil.com").withIcon(activity.getResources().getDrawable(R.drawable.main_logo))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        Drawer result = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        item3,
                        new SecondaryDrawerItem().withName("Show map")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.equals(item3)){
                            Intent intent = new Intent(activity, RideMapActivity.class);
                            activity.startActivity(intent);
                        }
                        return true;
                    }
                })
                .build();

        result.setSelection(item2);
    }
}
