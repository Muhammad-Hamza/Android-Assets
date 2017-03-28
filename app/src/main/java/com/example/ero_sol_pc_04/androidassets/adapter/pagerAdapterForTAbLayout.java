package com.example.ero_sol_pc_04.androidassets.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;





public class pagerAdapterForTAbLayoutpackage extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public GalleryPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ImagesFragment imagesFragment =  ImagesFragment.newInstance();
                return imagesFragment;
            case 1:
                VideoFragment videoFragment = VideoFragment.newInstance();
                return videoFragment;
            case 2:
                DocumentsFragment documentsFragment = DocumentsFragment.newInstance();
                return documentsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
 {
}
