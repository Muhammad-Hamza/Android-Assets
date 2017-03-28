package com.example.ero_sol_pc_04.androidassets.fragments;

/**
 * Created by ERO-SOL-PC-04 on 3/28/2017.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;package com.erosol.voip.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erosol.voip.R;
import com.erosol.voip.activities.GalleryActivity;
import com.erosol.voip.adapter.GalleryAdapter;
import com.erosol.voip.custom.GridInsetDecorator;
import com.erosol.voip.model.ImageMedia;
import com.erosol.voip.model.VoipMediaBundle;
import com.erosol.voip.util.VoipConstants;

import java.util.ArrayList;

public class ImagesFragment extends Fragment {

    GridInsetDecorator decorator = new GridInsetDecorator(2, 2);
    private TextView mNoItems;
    private GalleryAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private VoipMediaBundle mVoipMediaBundle;
    private ArrayList<ImageMedia> imageMedias;

    public static ImagesFragment newInstance() {
        ImagesFragment imagesFragment = new ImagesFragment();
        return imagesFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_images, container, false);
        mNoItems = (TextView) rootView.findViewById(R.id.txt_view_no_images_items);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.images_recycler_view);
        mVoipMediaBundle = ((GalleryActivity) getActivity()).getVoipMediaBundle();
        imageMedias = new ArrayList<ImageMedia>();
        mAdapter.setData(getActivity().getApplicationContext(), mVoipMediaBundle.getImages());
        GridLayoutManager glm = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()

        {
            @Override
            public int getSpanSize(int position) {
                return (position == 0) ? 2 : 1;
            }
        });
        mRecyclerView.setLayoutManager(glm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(decorator);
        mRecyclerView.setAdapter(mAdapter);
        checkRecyclerViewIsEmpty();



//        mRecyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(
//
//                getApplicationContext(),mRecyclerView, new GalleryAdapter.ClickListener()
//
//        {
//            @Override
//            public void onClick (View view,int position){
////                Bundle bundle = new Bundle();
////                bundle.putSerializable("imageMedias", imageMedias);
////                bundle.putInt("position", position);
////
////                FragmentManager fragmentManager = getSupportFragmentManager();
////                FragmentTransaction ft = fragmentManager.beginTransaction().addToBackStack("");
////                GallerySlideshowFragment newFragment = new GallerySlideshowFragment();
////                newFragment.setArguments(bundle);
////                newFragment.show(ft, "slideshow");
//            }

//            @Override
//            public void onLongClick (View view,int position){
//
//            }
//        }));


        return rootView;
    }


    private void addGallerySlideshowFragment(Bundle bundle) {
        GallerySlideshowFragment gallerySlideshowFragment = GallerySlideshowFragment.newInstance();
        gallerySlideshowFragment.setImages(mVoipMediaBundle.getImages());
        gallerySlideshowFragment.setArguments(bundle);
        gallerySlideshowFragment.show(getActivity().getSupportFragmentManager().beginTransaction(), "");
    }
    private void checkRecyclerViewIsEmpty() {
        if (mAdapter.getItemCount() == 0) {
            mNoItems.setVisibility(View.VISIBLE);
        }
    }
}
