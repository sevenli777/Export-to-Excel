package com.quitter.quitter;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.quitter.quitter.dao.DBHelper;
import com.quitter.quitter.dao.DefinediInfo;
import com.quitter.quitter.dao.TanWeiInfo;
import com.quitter.quitter.dialog.DialogFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity implements BaseQuickAdapter.OnItemChildClickListener, DialogInterface.OnClickListener {

    private RecyclerView rvList;
    private MsgListAdapter mAdapter;
    /*8.2+180802-373044675630199+王燕+13663198575+3.8+怦然心动饰品*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv_submit = findViewById(R.id.tv_submit);
        TextView tv_open = findViewById(R.id.tv_open);
        TextView tv_defind = findViewById(R.id.tv_defind);
        TextView tv_delete_all = findViewById(R.id.tv_delete_all);
        final EditText edit = findViewById(R.id.edit);
        final EditText defingd = findViewById(R.id.defingd);
        rvList = findViewById(R.id.recyclerView);
        //先请求相关权限
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        String message = "禁止将影响您使用功能";
        if (!PermissionUtil.hasPermissons(this, permissions)) {
            PermissionUtil.requestPermissions(permissions, MainActivity.this, message, 123, new PermissionUtil.onGrantedPermissionsResultCallbacks() {
                @Override
                public void onGranted() {
                    updateData();
                }
            });
        } else {
            updateData();
        }

        tv_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                List<DefinediInfo> definediInfos = DBHelper.getInstance().queryDefinedInfoById("0");
                if (CommonUtils.isListEmpty(definediInfos)) {
                    ShowToast.showToast("请先自定义格式");
                    return;
                }

                String data = edit.getText().toString().replace(" ", "").trim();
                if (TextUtils.isEmpty(data)) {
                    ShowToast.showToast("请添加数据");
                    return;
                }

                if (data.contains("/")) {
                    String[] split = data.split("/");
                    for (int i = 0; i < split.length; i++) {
                        addNewData(split[i]);
                    }
                } else {
                    addNewData(data);
                }

                updateData();
            }
        });

        tv_open.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Boolean aBoolean = true;
                try {
                    List<TanWeiInfo> tanWeiInfos = DBHelper.getInstance().queryAllTanWeiInfoByLoginId();
                    if (tanWeiInfos != null && tanWeiInfos.size() > 0) {
                        ExcelUtil.writeExcel(MainActivity.this, tanWeiInfos, "周禹舜");
                        aBoolean = true;
                    } else {
                        ShowToast.showToast("没有数据可导出");
                        aBoolean = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (aBoolean) {
                        File file = new File(ExcelUtil.root, "周禹舜" + ".xls");
                        if (!file.exists()) {
                            ShowToast.showToast("请添加数据");
                            return;
                        }
                        getExcelFileIntent(file);
                    }
                }
            }
        });

        tv_defind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = defingd.getText().toString().replace(" ", "").trim();
                if (!trim.contains("+")) {
                    ShowToast.showToast("自定义格式不对,中间用➕号隔开");
                    return;
                }

                String[] split = trim.split("\\+");
                if (split == null) {
                    ShowToast.showToast("自定义格式不对,中间用➕号隔开");
                    return;
                }

                List<String> list = new ArrayList<>();
                for (int i = 0; i < split.length; i++) {
                    list.add(split[i]);
                }
                DefinediInfo definediInfo = new DefinediInfo();
                definediInfo.setDefinedList(list);
                definediInfo.setId("0");
                DBHelper.getInstance().saveDefinedInfo(definediInfo);
                ShowToast.showToast("自定义格式成功");
            }
        });

        tv_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFactory.showTwoButtonDailog(MainActivity.this, "确定要删除所有的数据吗?", "取消", "确定", true);
            }
        });
    }

    private void addNewData(String trim) {
        String[] split = trim.split("\\+");
        if (split == null) {
            ShowToast.showToast("数据格式不对");
            return;
        }
        List<String> list = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            list.add(split[i]);
        }

        TanWeiInfo order = new TanWeiInfo();
        order.setOrderId(new Date().getTime() + "");
        order.setTanweiList(list);
        DBHelper.getInstance().saveTanWeiInfo(order);
    }

    private void updateData() {
        List<TanWeiInfo> tanWeiInfos = DBHelper.getInstance().queryAllTanWeiInfoByLoginId();
        if (tanWeiInfos != null && tanWeiInfos.size() > 0) {
            initRecycleView(tanWeiInfos);
        } else {
            mAdapter = new MsgListAdapter(R.layout.item_msglist);
            rvList.setAdapter(mAdapter);
            rvList.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    //Android获取一个用于打开Excel文件的intent
    public void getExcelFileIntent(File param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(param);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        startActivity(Intent.createChooser(intent, "选择浏览工具"));
    }


    private void initRecycleView(List<TanWeiInfo> list) {
        mAdapter = new MsgListAdapter(R.layout.item_msglist, list);
        rvList.setAdapter(mAdapter);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnItemChildClickListener(this);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        List<TanWeiInfo> data = adapter.getData();
        TanWeiInfo info = data.get(position);
        DBHelper.getInstance().deleteByTanWeiInfoId(info.orderId);
        data.remove(position);
        adapter.notifyItemRemoved(position);
        ShowToast.showToast("已删除");

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        if (which == DialogInterface.BUTTON_POSITIVE) {
            DBHelper.getInstance().deleteAll();
            updateData();
            ShowToast.showToast("已全部删除");
        }
    }
}
