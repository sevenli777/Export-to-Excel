package com.quitter.quitter.dialog;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.quitter.quitter.R;


public class WaitingDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_wait_dialog, container,
                false);

        TextView tvMsg = view.findViewById(R.id.message);

        String mMessage = getTag();
        if (TextUtils.isEmpty(mMessage)) {
            tvMsg.setVisibility(View.GONE);
        } else {
            tvMsg.setText(mMessage);
        }
        return view;
    }

}
