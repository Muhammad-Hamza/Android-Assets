//package com.erosol.voip.dialogs;
//
//import android.app.Dialog;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.DialogFragment;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//
//import com.erosol.voip.R;
//
//
//public class AttachmentDialog extends DialogFragment {
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_attachment_dialogue,container,false);
//
//        Window window = getDialog().getWindow();
//        window.setGravity(Gravity.BOTTOM);
//
//        return rootView;
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        Dialog d = getDialog();
//        if (d!=null){
//            int width = ViewGroup.LayoutParams.MATCH_PARENT;
//            int height = 400;
//            d.getWindow().setLayout(width, height);
//        }
//    }
//
//    @Override
//    public void onResume() {
//        final Window dialogWindow = getDialog().getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.y = 200;        // set Y position here
//        dialogWindow.setAttributes(lp);
//
//        super.onResume();
//    }
//}
