package com.ecjtu.assistant.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class StudentDb {

    protected static final String TAG = StudentDb.class.getSimpleName();

    protected static final int DB_VERSION = 1;//数据库版本号
    protected static final String DB_NAME = "student_db";//数据库名称
    protected static final String DB_PRIMARY_KEY = "_id";//主键id 1、2、3、4...
    protected static final String DB_TABLE_NAME = "student";//表名

    protected static final String DB_TABLE_COLUMN_name      = "name";//列名 学生名称
    protected static final String DB_TABLE_COLUMN_school    = "school";//列名 学生所在学院
    protected static final String DB_TABLE_COLUMN_headerUrl = "headerUrl";//列名 学生头像图片在手机上的地址
    protected static final String DB_TABLE_COLUMN_tel       = "tel";//列名 学生登录账号
    protected static final String DB_TABLE_COLUMN_pw        = "pw";//列名 学生登录密码

    protected static final String DB_DEFAULT_ORDERBY = DB_TABLE_COLUMN_name + " DESC";//名称列作排序

    protected DatabaseHelper mDBHelper;//数据库帮助类 用于创建数据库
    protected SQLiteDatabase mDB;//表的增删改查方法帮助类

    protected static final StudentDb mInstance = new StudentDb();//单例模式 方便获取对象并调用其方法

    private final String DB_TABLE_CREATE_SQL = "create table " + DB_TABLE_NAME + " (_id integer primary key autoincrement, "
            + DB_TABLE_COLUMN_tel + " text not null, "
            + DB_TABLE_COLUMN_pw + " text not null, "
            + DB_TABLE_COLUMN_name + " text, "
            + DB_TABLE_COLUMN_school + " text, "
            + DB_TABLE_COLUMN_headerUrl + " text);";//创建表和表中的各列名

    public static class Record {//数据库中的表对应javaBean数据类，方便在listview和recyclerview中使用
        public long key = -1;
        public String name;
        public String school;//学院
        public String headerUrl;
        public String tel;
        public String pw;
    }

    protected class DatabaseHelper extends SQLiteOpenHelper {//固定写法，继承seliteHelper并实现两个方法
        public DatabaseHelper(Context context, String dbName, int dbVersion) {
            super(context, dbName, null, dbVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_TABLE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME);
            onCreate(db);
        }
    }

    private StudentDb() {
    }

    public static StudentDb getInstance() {
        return mInstance;
    }

    public boolean open(Context context) {/*打开数据库，准备对其中的表进行增删改查，一般在用到数据库的Activity的onCreate方法中调用，*/
        try {
            mDBHelper = new DatabaseHelper(context, DB_NAME, DB_VERSION);
            mDB = mDBHelper.getWritableDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void close() {/*关闭数据库，需要在使用数据库的activity中的onDestroy方法中关闭数据库 有打开就有关闭*/
        mDB.close();
        mDBHelper.close();
    }

    public int size() {
        int size = 0;
        Cursor mCursor = mDB.query(DB_TABLE_NAME, new String[]{DB_PRIMARY_KEY}, null, null, null, null,
                null, null);
        if (mCursor != null) {
            size = mCursor.getCount();
        }
        mCursor.close();
        return size;
    }

    public boolean insert(Record note) {//通过ContentValues类存储列的值，通过mDB.insert插入新的一行数据，叫做数据库的增加数据操作
        ContentValues values = new ContentValues();
        values.put(DB_TABLE_COLUMN_name      , note.name);
        values.put(DB_TABLE_COLUMN_school    , note.school);
        values.put(DB_TABLE_COLUMN_headerUrl , note.headerUrl);
        values.put(DB_TABLE_COLUMN_tel       , note.tel);
        values.put(DB_TABLE_COLUMN_pw        , note.pw);
        note.key = mDB.insert(DB_TABLE_NAME, null, values);
        if (note.key == -1) {/*insert方法的返回值是-1 说明插入数据失败*/
            Log.e(TAG, "db insert fail!");
            return false;
        }
        return true;
    }


    public boolean delete(int position) {
        long key = getkey(position, null);
        if (key == -1) {
            return false;
        }
        String condition = DB_PRIMARY_KEY + "=" + "\'" + key + "\'";
        return delete(condition, null);
    }
    public boolean delete(long key) {/*根据主键id删除一行数据，列名DB_PRIMARY_KEY 列值 为该方法的入参key值*/

        if (key == -1) {
            return false;
        }
        String condition = DB_PRIMARY_KEY + "=" + "\'" + key + "\'";
        return delete(condition, null);
    }

    protected boolean delete(String whereClause, String[] whereArgs) {
        int rows = mDB.delete(DB_TABLE_NAME, whereClause, whereArgs);
        if (rows <= 0) {
            Log.e(TAG, "db delete fail!");
            return false;
        }
        return true;
    }

    public boolean clear() {
        return delete(null, null);
    }

    public Record get(int position) {
        return get(position, null);
    }

    public Record get(long id) {
        String condition = DB_PRIMARY_KEY + "=" + "\'" + id + "\'";
        List<Record> notes = query(condition);
        if (notes.isEmpty()) {
            return null;
        }
        return notes.get(0);
    }

    public Record get(int position, String condition) {
        Cursor cursor = mDB.query(DB_TABLE_NAME, null, condition, null, null, null,
                DB_DEFAULT_ORDERBY, null);
        List<Record> notes = extract(position, cursor);
        if (notes.isEmpty()) {
            return null;
        }
        return notes.get(0);
    }

    public boolean update( Record note, String selection, String[] selectionArgs) {
        ContentValues values = new ContentValues();
        values.put(DB_TABLE_COLUMN_name      , note.name);
        values.put(DB_TABLE_COLUMN_school    , note.school);
        values.put(DB_TABLE_COLUMN_headerUrl , note.headerUrl);
        values.put(DB_TABLE_COLUMN_tel       , note.tel);
        values.put(DB_TABLE_COLUMN_pw        , note.pw);
        note.key = mDB.update(DB_TABLE_NAME,values, selection, selectionArgs);
        if (note.key == -1) {
            Log.e(TAG, "db update fail!");
            return false;
        }
        return true;
    }

    public List<Record> query() {/*数据库查询方法*/
        Cursor cursor = mDB.query(DB_TABLE_NAME, null, null, null, null, null,
                DB_DEFAULT_ORDERBY, null);
        return extract(0, cursor);
    }

    public List<Record> query(String condition) {
        Cursor cursor = mDB.query(DB_TABLE_NAME, null, condition, null, null, null,
                DB_DEFAULT_ORDERBY, null);
        return extract(0, cursor);
    }



    public List<Record> query(int offset, int limit) {
        return query(null, offset, limit);
    }

    public List<Record> query(String condition, int offset, int limit) {
        Cursor cursor = mDB.query(DB_TABLE_NAME, null, condition, null, null, null,
                DB_DEFAULT_ORDERBY, offset + "," + limit);
        return extract(0, cursor);
    }

    protected List<Record> extract(int offset, Cursor cursor) {/*处理查询出来的数据，转成list泛型是Record数据类的形式*/
        List<Record> notes = new ArrayList<Record>();
        if (cursor == null || cursor.getCount() <= offset) {
            return notes;
        }

        cursor.moveToFirst();/*游标移动到0位置*/
        cursor.moveToPosition(offset);

        do {/*循环起来一个一个取出数据放到record的数据类中再放入list集合中*/
            Record note = new Record();
            note.key = cursor.getLong(cursor.getColumnIndex(DB_PRIMARY_KEY));
            note.name = cursor.getString(cursor.getColumnIndex( DB_TABLE_COLUMN_name        ));
            note.school = cursor.getString(cursor.getColumnIndex(  DB_TABLE_COLUMN_school      ));
            note.headerUrl = cursor.getString(cursor.getColumnIndex(   DB_TABLE_COLUMN_headerUrl  ));
            note.tel = cursor.getString(cursor.getColumnIndex(   DB_TABLE_COLUMN_tel        ));
            note.pw = cursor.getString(cursor.getColumnIndex(DB_TABLE_COLUMN_pw          ));
            notes.add(note);
        } while (cursor.moveToNext());/*每次结束再循环到下一个*/

        cursor.close();/*游标关闭*/

        return notes;
    }

    protected long getkey(int position, String condition) {
        long key = -1;
        Cursor cursor = mDB.query(true, DB_TABLE_NAME, new String[]{DB_PRIMARY_KEY}, condition, null, null, null,
                DB_DEFAULT_ORDERBY, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToPosition(position);
            key = cursor.getLong(cursor.getColumnIndex(DB_PRIMARY_KEY));
            cursor.close();
        }
        return key;
    }
}