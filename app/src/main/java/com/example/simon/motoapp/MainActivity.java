package com.example.simon.motoapp;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.simon.motoapp.dummy.DummyContent;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity
        implements MapsFragment.OnFragmentInteractionListener,
        NavigationFragment.OnFragmentInteractionListener,
        TwistiesFragment.OnFragmentInteractionListener,
        DirectionFragment.OnFragmentInteractionListener,
        ServoFragment.OnFragmentInteractionListener,
        FuelUpFragment.OnFragmentInteractionListener,
        SpeedoFragment.OnFragmentInteractionListener,
        StatsFragment.OnListFragmentInteractionListener,
        MotorcycleFragment.OnFragmentInteractionListener
        {

    private DrawerLayout mDrawerLayout;

    private EditText sv;

    private FragmentManager mFragmentManager;

            public LatLng getLocation() {
                return mLocation;
            }

            public void setLocation(LatLng mLocation) {
                this.mLocation = mLocation;
            }

            private LatLng mLocation;

            public LatLng getDestination() {
                return mDestination;
            }

            public void setDestination(LatLng mDestination) {
                this.mDestination = mDestination;
            }

            private LatLng mDestination;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = new FragmentManager(getSupportFragmentManager());

        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_container) != null)
        {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null)
            {
                return;
            }
            mFragmentManager.setFragment(0);
        }

        //Applying toolbar as the appbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //add the button that opens the navigation drawer
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        //finding the drawer layout
        mDrawerLayout = findViewById(R.id.drawer_layout);

        //listening for open/close events and other state changes in the navigation draw
        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset)
                    {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView)
                    {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView)
                    {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState)
                    {
                        // Respond when the drawer motion state changes
                    }
                }
        );

        //to receive callbacks when the user taps a list item in the drawer
        //when an item is tapped set the selected as checked changing the list items style to be highlighted
        //also closes the drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener()
                {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
                    {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);

                        switch(menuItem.getItemId())
                        {
                            case R.id.nav_map:
                                mFragmentManager.setFragment(0);
                                return true;
                            case R.id.nav_compass:
                                mFragmentManager.setFragment(1);
                                return true;
                            case R.id.nav_twisties:
                                mFragmentManager.setFragment(2);
                                return true;
                            case R.id.nav_navigation:
                                mFragmentManager.setFragment(3);
                                return true;
                            case R.id.nav_petrol:
                                mFragmentManager.setFragment(4);
                                return true;
                            case R.id.nav_fuelup:
                                mFragmentManager.setFragment(5);
                                return true;
                            case R.id.nav_speedo:
                                mFragmentManager.setFragment(6);
                                return true;
                            case R.id.nav_stats:
                                mFragmentManager.setFragment(7);
                                return true;
                            case R.id.nav_motorcycle:
                                mFragmentManager.setFragment(8);
                                return true;
                            case R.id.nav_settings:
                                Toast.makeText(getApplicationContext(), "Adding this later!", Toast.LENGTH_SHORT).show();
                                return true;
                        }

                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    //opening the drawer when the user taps the nav drawer button
    //uses options menu framework to listen for when the user taps the item with ID of home then opens the nav drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) { }

            @Override
            public void onListFragmentInteraction(DummyContent.DummyItem item) {

            }
}
