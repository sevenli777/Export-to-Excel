package com.quitter.quitter;


import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.quitter.quitter.dao.TanWeiInfo;

import java.util.List;

public class MsgListAdapter extends BaseCompatAdapter<TanWeiInfo, BaseViewHolder> {

    public MsgListAdapter(int layoutResId, @Nullable List<TanWeiInfo> data) {
        super(layoutResId, data);
    }

    public MsgListAdapter(@Nullable List<TanWeiInfo> data) {
        super(data);
    }

    public MsgListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, TanWeiInfo item) {
        List<String> tanweiList = item.getTanweiList();
        helper.addOnClickListener(R.id.delete);
        helper.setText(R.id.date, tanweiList.get(0));
        if (tanweiList.size() >= 3) {
            helper.setText(R.id.name, tanweiList.get(1));
            helper.setText(R.id.phone, tanweiList.get(2));
        }
    }
}