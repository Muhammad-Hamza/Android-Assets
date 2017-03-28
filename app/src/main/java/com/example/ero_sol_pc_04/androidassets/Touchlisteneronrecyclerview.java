package com.example.ero_sol_pc_04.androidassets;

import android.os.Bundle;
import android.view.View;

import java.text.SimpleDateFormat;

/**
 * Created by ERO-SOL-PC-04 on 3/28/2017.
 */

public class Touchlisteneronrecyclerview {


    //First Develop an interface in adapter and enum
    public interface onGalleryItemClickListener{
        void onGalleryItemClick(ImageMedia item, GalleryItemActionType actionType);
    }
    public enum GalleryItemActionType {

        ITEM_CLICK
    }
// and then develop a constructor in adapter to receive the listener from activity or fragment
public GalleryAdapter(onGalleryItemClickListener listener){
    mListener= listener;
    mFormatter = new SimpleDateFormat("HH:mm");
    mDateFormatter = new SimpleDateFormat("MMM d");

}

// use this listener in the activity like this
    mAdapter = new GalleryAdapter(new GalleryAdapter.onGalleryItemClickListener() {
        @Override
        public void onGalleryItemClick(ImageMedia item, GalleryAdapter.GalleryItemActionType actionType) {
            switch (actionType) {
                case ITEM_CLICK: {
                    mVoipMediaBundle = ((GalleryActivity) getActivity()).getVoipMediaBundle();
                    if (mVoipMediaBundle != null) {
                        for (int i = 0; i < mVoipMediaBundle.getImages().size(); i++) {

                            if (mVoipMediaBundle.getImages().get(i).getName().equalsIgnoreCase(item.getReference())) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(VoipConstants.POSITION, i);
                                addGallerySlideshowFragment(bundle);
                                //mToolbar.getMenu().clear();
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
//and in the end set an onclicklistener in the adapter's bindviewholder
        itemViewHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGalleryItemClick(itemViewHolder.mItem,GalleryItemActionType.ITEM_CLICK);
                // Toast.makeText(mContext.getApplicationContext(),"working",Toast.LENGTH_SHORT).show();
            }
        });

}
