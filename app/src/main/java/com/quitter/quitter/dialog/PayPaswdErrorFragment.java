package com.quitter.quitter.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by km on 2015/12/10.
 */
public class PayPaswdErrorFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String msg = args.getString("msg");
        String btnTextCancel = args.getString("btn_text_cancel");
        String btnTextOK = args.getString("btn_text_ok");
        int remainder = args.getInt("num");
        Activity activity = getActivity();
        CustomedDialog.Builder customBuilder = new CustomedDialog.Builder(
                activity);
        DialogInterface.OnClickListener onClickListener = (DialogInterface.OnClickListener) activity;
        if (remainder > 0) {
            customBuilder.setMessage("支付密码不正确，您还可以输入" + remainder + "次")
                    .setNegativeButton(btnTextCancel, onClickListener).setPositiveButton(btnTextOK, onClickListener);
        } else {
            customBuilder.setMessage("密码多次输错，请3小时后再试或找回密码")
                    .setNegativeButton(btnTextCancel, onClickListener).setPositiveButton(btnTextOK, onClickListener);
        }
        return customBuilder.create();
    }
}
