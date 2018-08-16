package com.quitter.quitter.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

/**
 * Created by Jack on 2015/10/27.
 */
public class DialogFactory {
    private static DialogFragment dialogFragment;

    public static void showProgressDialog(Activity activity, String message,
                                          boolean isCancel) {
        if (dialogFragment != null && dialogFragment instanceof WaitingDialogFragment) {
            if (dialogFragment.isRemoving() || dialogFragment.isHidden()) {
                dialogFragment.setCancelable(isCancel);
                showDialog(dialogFragment, activity, message);
            }
        } else {
            dialogFragment = new WaitingDialogFragment();
            dialogFragment.setCancelable(isCancel);
            showDialog(dialogFragment, activity, message);
        }
    }

    private static void showDialog(DialogFragment dialogFragment, Activity activity, String msg) {
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        transaction.add(dialogFragment, msg);
        transaction.commitAllowingStateLoss();
    }

    public static void showSingleBtnDialog(Activity activity, String message, String btnText) {
        showSingleBtnDialog(activity, message, btnText, true);
    }

    public static void showSingleBtnDialog(Activity activity, String message, String btnText, boolean isCancel) {
        dialogFragment = new SingleButtonDailogFragment();
        dialogFragment.setCancelable(isCancel);
        Bundle args = new Bundle();
        args.putString("msg", message);
        args.putString("btn_text_cancel", btnText);
        dialogFragment.setArguments(args);
        showDialog(dialogFragment, activity, null);
    }


    public static void showTwoButtonDailog(Activity activity, String message, String txt_cancel, String txt_ok) {
        showTwoButtonDailog(activity, message, txt_cancel, txt_ok, true);
    }



    public static void showTwoButtonDailog(Activity activity, String message, String txt_cancel, String txt_ok, boolean isCanle) {
        dialogFragment = new TwoButtonDialogFragment();
        dialogFragment.setCancelable(isCanle);
        Bundle args = new Bundle();
        args.putString("msg", message);
        args.putString("btn_text_cancel", txt_cancel);
        args.putString("btn_text_ok", txt_ok);
        dialogFragment.setArguments(args);
        showDialog(dialogFragment, activity, null);
    }

    public static void showTwoButtonDailogSingleton(Activity activity, String message, String txt_cancel, String txt_ok) {
        if (dialogFragment != null && dialogFragment instanceof TwoButtonDialogFragment) {
            if (!dialogFragment.isAdded()) {
                Bundle args = new Bundle();
                args.putString("msg", message);
                args.putString("btn_text_cancel", txt_cancel);
                args.putString("btn_text_ok", txt_ok);
                dialogFragment.setArguments(args);
                dialogFragment.show(activity.getFragmentManager(), message);
            }
        } else {
            dialogFragment = new TwoButtonDialogFragment();
            Bundle args = new Bundle();
            args.putString("msg", message);
            args.putString("btn_text_cancel", txt_cancel);
            args.putString("btn_text_ok", txt_ok);
            dialogFragment.setArguments(args);
            dialogFragment.show(activity.getFragmentManager(), message);
        }
    }

    public static void payPaswdErrorDailog(Activity activity, int num, String txt_cancel, String txt_ok) {
        dialogFragment = new PayPaswdErrorFragment();
        Bundle args = new Bundle();
        args.putString("btn_text_cancel", txt_cancel);
        args.putString("btn_text_ok", txt_ok);
        args.putInt("num", num);
        dialogFragment.setArguments(args);
        dialogFragment.show(activity.getFragmentManager(), "payDialog");
    }

    public static void showTrasferRiskDailog(Activity activity, String title, String message, String btnText) {
        showTrasferRiskDailog(activity, title,message, btnText, true);
    }

    public static void showTrasferRiskDailog(Activity activity, String title, String message, String btnText, boolean isCancel) {
        dialogFragment = new TrasferRiskDailogFragment();
        dialogFragment.setCancelable(isCancel);
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("msg", message);
        args.putString("btn_text_cancel", btnText);
        dialogFragment.setArguments(args);
        showDialog(dialogFragment, activity, message);
        /*dialogFragment.show(activity.getFragmentManager(),
                message);*/
    }

    public synchronized static void dismissDialog() {
        if (dialogFragment != null) {
            dialogFragment.dismissAllowingStateLoss();
            dialogFragment = null;
        }
    }
}



