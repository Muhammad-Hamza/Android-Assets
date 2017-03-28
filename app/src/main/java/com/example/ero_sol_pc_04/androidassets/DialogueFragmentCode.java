package com.example.ero_sol_pc_04.androidassets;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by ERO-SOL-PC-04 on 3/21/2017.
 */


package com.erosol.voip.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.erosol.voip.R;
import com.erosol.voip.util.Logger;


    /**
     * Created by TEXON WARE on 1/5/2017.
     */

    public class DialogueFragmentCode  extends DialogFragment implements View.OnClickListener {
        public static final String TAG = DialogueFragmentCode.class.getSimpleName();

        private TextView dialogHeader;
        private TextView dialogTxtViewBody;
        //private EditText dialogEditTextBody;
        private Button okButton;
        private String okButtonText;
        private String cancelButtonText;
        private Button cancelButton;
        private ButtonType buttonType = ButtonType.OK;
        private DialogType dialogType = DialogType.TEXT_BODY;
        private String title;
        private String body;
        private static CustomDialogFragment instance;

        private OnDialogListener listener;



        public enum DialogType{
            TEXT_BODY,
            EDIT_BODY
        }

        public enum ButtonType{
            OK,
            CANCEL,
            OK_CANCEL
        }


        public interface OnDialogListener {
            void onClick(ActionType actionType, String body);

            enum ActionType{
                OK,
                CANCEL
            }
        }


        public static CustomDialogFragment newInstance(String title, String body) {
            if (instance == null){
                instance = new CustomDialogFragment();
            }
            instance.title = title;
            instance.body = body;
            return instance;
        }

        public void setButtonType(ButtonType buttonType) {
            this.buttonType = buttonType;
        }

        public void setDialogType(DialogType dialogType) {
            this.dialogType = dialogType;
        }

        public void setButtonText(String text, ButtonType buttonType) {
            if (buttonType == ButtonType.OK){
                this.okButtonText = text;
            } else {
                this.cancelButtonText = text;
            }

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Logger.d(TAG,"| onCreateDialog");
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View layout = inflater.inflate(R.layout.fragment_custom_dialog, null);

            dialogHeader = (TextView) layout.findViewById(R.id.dialog_title);
            dialogTxtViewBody = (TextView) layout.findViewById(R.id.dialog_txt_view_body);
            //dialogEditTextBody = (EditText) layout.findViewById(R.id.dialog_edit_txt_body);
            okButton = (Button) layout.findViewById(R.id.dialog_button_ok);
            cancelButton = (Button) layout.findViewById(R.id.dialog_button_cancel);

            dialogTxtViewBody.setVisibility(View.GONE);
            //dialogEditTextBody.setVisibility(View.GONE);

            dialogHeader.setText(title);

            if (body == null || body.isEmpty()) {
                // if there is no message that we can show to the user, just dismiss the dialog
                Log.d(TAG, "no message found for the dialog");
                dismiss();
                onDestroy();
            }

            switch (dialogType){
                case TEXT_BODY:
                    dialogTxtViewBody.setVisibility(View.VISIBLE);
                    dialogTxtViewBody.setText(body);
                    break;
                case EDIT_BODY:
//                dialogEditTextBody.setVisibility(View.VISIBLE);
//                dialogEditTextBody.setHint(body);
                    break;
            }


            okButton.setOnClickListener(this);
            if (okButtonText == null){
                okButton.setText(getString(R.string.txt_yes));
            } else {
                okButton.setText(okButtonText);
            }

            if (buttonType == ButtonType.OK){
                cancelButton.setVisibility(View.GONE);
            }
            if (cancelButtonText == null){
                cancelButton.setText(getString(R.string.txt_edit));
            } else {
                cancelButton.setText(cancelButtonText);
            }

            cancelButton.setOnClickListener(this);

            ContextThemeWrapper context = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
            return new android.app.AlertDialog.Builder(context).setView(layout).create();
        }

        public void showDialog(FragmentManager manager, String tag, OnDialogListener listener) {
            this.listener = listener;
            super.show(manager, tag);
        }

        @Override
        public int show(FragmentTransaction transaction, String tag) {
            return super.show(transaction, tag);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_button_ok:
                    if (listener != null){
                        switch (dialogType){
                            case TEXT_BODY:
                                this.dismiss();
                                listener.onClick(OnDialogListener.ActionType.OK, dialogTxtViewBody.getText().toString());
                                break;
                            case EDIT_BODY:
//                            if (dialogEditTextBody.getText().length() > 0){
//                                this.dismiss();
//                                listener.onClick(OnDialogListener.ActionType.OK, dialogEditTextBody.getText().toString());
//                            }
                                break;
                        }
                    }
                    break;
                case R.id.dialog_button_cancel:
                    this.dismiss();
                    if (listener != null){
                        listener.onClick(OnDialogListener.ActionType.CANCEL, null);
                    }
                    break;
            }
        }

    }



}

