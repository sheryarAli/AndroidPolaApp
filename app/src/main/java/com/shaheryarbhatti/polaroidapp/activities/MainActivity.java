package com.shaheryarbhatti.polaroidapp.activities;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.shaheryarbhatti.polaroidapp.fragments.DashboardFragment;
import com.shaheryarbhatti.polaroidapp.fragments.FeaturedFragment;
import com.shaheryarbhatti.polaroidapp.fragments.PopularFragment;
import com.shaheryarbhatti.polaroidapp.viewpagers.MainFragmentPagerAdapter;
import com.shaheryarbhatti.polaroidapp.R;

public class MainActivity extends AppCompatActivity implements DashboardFragment.OnFragmentInteractionListener
        , FeaturedFragment.OnFragmentInteractionListener, PopularFragment.OnFragmentInteractionListener {

    private final String TAG = "MainActivity";
    private TabLayout tabs;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tabs = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.searchAction:
                Toast.makeText(this, "Will Perform Search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.takePhotoAction:
                Toast.makeText(this, "Will Take Snap", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
