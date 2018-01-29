package com.zhurui.bunnymall.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoux on 2017/8/28.
 */

public class RecordSQLiteManager {

    RecordSQLiteOpenHelper helper = null;
    private SQLiteDatabase db;
    private Context context;

    public RecordSQLiteManager(Context context) {
        this.context = context;
        helper = RecordSQLiteOpenHelper.getInstance(context);
        db= helper.getReadableDatabase();
    }

    /**
     * 插入数据
     */
    public void insertData(String tempName) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into records(name) values('" + tempName + "')");
        db.close();
    }

    /**
     * 模糊查询数据
     */
    public List<String> queryData(String tempName) {
        List<String> strlist = new ArrayList<>();
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);
        while (cursor.moveToNext()){
            strlist.add(cursor.getString(cursor
                    .getColumnIndex("name")));
        }
        return strlist;
    }

    /**
     * 检查数据库中是否已经有该条记录
     */
    public boolean hasData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //判断是否有下一个
        return cursor.moveToNext();
    }

    /**
     * 清空数据
     */
    public void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
    }
}
