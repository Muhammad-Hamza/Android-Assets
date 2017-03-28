package com.example.ero_sol_pc_04.androidassets;

/**
 * Created by ERO-SOL-PC-04 on 3/28/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;package com.erosol.voip.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.erosol.voip.R;
import com.erosol.voip.adapter.GalleryAdapter;
import com.erosol.voip.adapter.GalleryPageAdapter;
import com.erosol.voip.custom.GridInsetDecorator;
import com.erosol.voip.fragments.GallerySlideshowFragment;
import com.erosol.voip.model.ImageMedia;
import com.erosol.voip.model.VoipMediaBundle;
import com.erosol.voip.util.VoipConstants;

import java.util.ArrayList;

public class tablayout extends BaseActivity {

    Toolbar mToolbar;
    Context context;
    private GalleryPageAdapter mPageAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private VoipMediaBundle mVoipMediaBundle;
    private ArrayList<ImageMedia> imageMedias;
    private int mChatType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_gallery);
        mViewPager = (ViewPager) findViewById(R.id.pager_gallery);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.action_gallery));



        Intent intent = getIntent();
        mChatType = intent.getExtras().getInt(VoipConstants.TYPE);
        mVoipMediaBundle = (VoipMediaBundle) intent.getSerializableExtra(VoipConstants.VOIP_MEDIA_BUNDLE);











        setUpTabLayout();
    }


    private void setUpTabLayout(){

        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.txt_tab_title_Images)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.txt_tab_title_video)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.txt_tab_title_doc)));

        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mPageAdapter = new GalleryPageAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public VoipMediaBundle getVoipMediaBundle() {
        return mVoipMediaBundle;
    }
}

