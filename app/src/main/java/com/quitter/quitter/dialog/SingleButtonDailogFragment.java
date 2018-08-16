package com.quitter.quitter.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by km on 2015/12/1.
 */
public class SingleButtonDailogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String msg = args.getString("msg");
        String btnText = args.getString("btn_text_cancel");
        Activity activity = getActivity();
        CustomedDialog.Builder customBuilder = new CustomedDialog.Builder(
                activity);
        DialogInterface.OnClickListener onClickListener = (DialogInterface.OnClickListener) activity;
        customBuilder.setMessage(msg)
                .setNegativeButton(btnText, onClickListener);
        return customBuilder.create();
    }
}
