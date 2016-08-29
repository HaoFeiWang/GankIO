package com.whf.gankio.View.Activitys;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.whf.gankio.Presenter.Adapters.MyViewPagerAdapter;
import com.whf.gankio.R;
import com.whf.gankio.View.Fragments.StudyFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private NavigationView  mNavigationView;
    private ViewPager mViewPager;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragments();
        initView();
        initListener();
    }

    private void initFragments() {
        fragments=new ArrayList<>();
        fragments.add(StudyFragment.getInstance("Android"));
        fragments.add(StudyFragment.getInstance("iOS"));
    }

    private void initView() {
        mDrawerLayout= (DrawerLayout) findViewById(R.id.main_drawerlayout);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.main_tab);
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        mNavigationView = (NavigationView) findViewById(R.id.navigationView);

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);//使用Toolbar替换Actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new MyViewPagerAdapter(this.getSupportFragmentManager(),fragments));
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout设为ViewPage的导航条
    }

    private void initListener() {
        ActionBarDrawerToggle drawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,0,0);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.sliding_menu_study:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.sliding_menu_play:
                        startActivity(new Intent(MainActivity.this,PlayActivity.class));
                        break;
                    case R.id.sliding_menu_about:
                        startActivity(new Intent(MainActivity.this,AboutActivity.class));
                        break;
                }
                return false;
            }
        });
    }

}
