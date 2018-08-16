package com.quitter.quitter.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by km on 2015/12/1.
 */
public class TwoButtonDialogFragment extends DialogFragment {

    private Fragment baseCompatFragment;
    @SuppressLint("ValidFragment")
    public TwoButtonDialogFragment(Fragment baseCompatFragment) {
        this.baseCompatFragment=baseCompatFragment;
    }
    public TwoButtonDialogFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String msg = args.getString("msg");
        String btnTextCancel = args.getString("btn_text_cancel");
        String btnTextOK = args.getString("btn_text_ok");
        Activity activity = getActivity();
        CustomedDialog.Builder customBuilder = new CustomedDialog.Builder(
                activity);
        DialogInterface.OnClickListener onClickListener;
        if(baseCompatFragment!=null){
            onClickListener= (DialogInterface.OnClickListener) baseCompatFragment;
        }else{
            onClickListener= (DialogInterface.OnClickListener) activity;
        }
        customBuilder.setMessage(msg)
                .setNegativeButton(btnTextCancel, onClickListener).setPositiveButton(btnTextOK, onClickListener);
        return customBuilder.create();
    }
}
