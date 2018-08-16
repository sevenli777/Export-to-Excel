package com.quitter.quitter.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.quitter.quitter.MyApplication;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import db.DaoMaster;
import db.DaoSession;
import db.DefinediInfoDao;
import db.TanWeiInfoDao;


public class DBHelper {
    public static final String DB_NAME = "quitter-db";
    private static DBHelper instance = new DBHelper();
    private TanWeiInfoDao mTanWeiInfoDao;
    private DefinediInfoDao mDefinediInfoDao;


    private DBHelper() {
        setUpDB(MyApplication.getAppContext());
    }

    public static DBHelper getInstance() {
        return instance;
    }

    private void setUpDB(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db != null) {
            DaoMaster daoMaster = new DaoMaster(db);
            DaoSession daoSession = daoMaster.newSession();
            mTanWeiInfoDao = daoSession.getTanWeiInfoDao();
            mDefinediInfoDao = daoSession.getDefinediInfoDao();
        }
    }


    /*查询所有摊位信息*/
    public List<TanWeiInfo> queryAllTanWeiInfoByLoginId() {
        QueryBuilder queryBuilder = mTanWeiInfoDao.queryBuilder();
        return queryBuilder.list();
    }

    public List<DefinediInfo> queryDefinedInfoById(String id) {
        QueryBuilder queryBuilder = mDefinediInfoDao.queryBuilder();
        return  queryBuilder.where(DefinediInfoDao.Properties.Id.eq(id)).list();
    }

    /*删除摊位信息*/
    public void deleteByTanWeiInfoId(String id) {
        mTanWeiInfoDao.getDatabase().execSQL(
                "DELETE FROM " + TanWeiInfoDao.TABLENAME
                        + " WHERE " + TanWeiInfoDao.Properties.OrderId.columnName + "='" + id + "'");
    }


    public void deleteByDefinedInfoId(String id) {
        mTanWeiInfoDao.getDatabase().execSQL(
                "DELETE FROM " + DefinediInfoDao.TABLENAME
                        + " WHERE " + DefinediInfoDao.Properties.Id.columnName + "='" + id + "'");
    }




    public void deleteAll() {
        mTanWeiInfoDao.getDatabase().execSQL(
                "DELETE FROM " + TanWeiInfoDao.TABLENAME);
    }

    /*保存摊位信息*/
    public void saveTanWeiInfo(TanWeiInfo tanWeiInfo) {
        mTanWeiInfoDao.insertOrReplace(tanWeiInfo);
    }

    /*保存摊位信息*/
    public void saveDefinedInfo(DefinediInfo definediInfo) {
        mDefinediInfoDao.insertOrReplace(definediInfo);
    }


    ///////////////////////////////////////////////////////////////////////////

    public static class ProductOpenHelper extends DaoMaster.OpenHelper {
        public ProductOpenHelper(Context context, String name) {
            super(context, name);
        }

        public ProductOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            if (oldVersion == newVersion) {
                //LogUtils.d("onUpgrade:" + "数据库是最新版本" + oldVersion + "，不需要升级");
                return;
            }
            //            if (oldVersion < 8) {
            DaoMaster.dropAllTables(db, true);
            DaoMaster.createAllTables(db, true);
            //            }
        }
    }
}
