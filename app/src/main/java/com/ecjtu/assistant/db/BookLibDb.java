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

public class BookLibDb {

    protected static final String TAG = BookLibDb.class.getSimpleName();

    protected static final int DB_VERSION = 6;
    protected static final String DB_NAME = "booklib_db";
    protected static final String DB_PRIMARY_KEY = "_id";
    protected static final String DB_TABLE_NAME = "booklib";

    protected static final String DB_TABLE_COLUMN_bookName = "bookName";
    protected static final String DB_TABLE_COLUMN_bookLink = "bookLink";
    protected static final String DB_TABLE_COLUMN_bookInfo = "bookInfo";
    protected static final String DB_TABLE_COLUMN_bookInfo1 = "bookInfo1";
    protected static final String DB_TABLE_COLUMN_bookInfo2 = "bookInfo2";

    protected static final String DB_DEFAULT_ORDERBY = DB_TABLE_COLUMN_bookName + " DESC";

    protected DatabaseHelper mDBHelper;
    protected SQLiteDatabase mDB;

    protected static final BookLibDb mInstance = new BookLibDb();

    private final String DB_TABLE_CREATE_SQL = "create table " + DB_TABLE_NAME + " (_id integer primary key autoincrement, "
            + DB_TABLE_COLUMN_bookName + " text, "
            + DB_TABLE_COLUMN_bookLink + " text, "
            + DB_TABLE_COLUMN_bookInfo + " text, "
            + DB_TABLE_COLUMN_bookInfo1 + " text, "
            + DB_TABLE_COLUMN_bookInfo2 + " text);";

    public static class Record {
        public long key = -1;
        public String bookName;
        public String bookLink;
        public String bookInfo;
        public String bookInfo1;
        public String bookInfo2;
    }

    protected class DatabaseHelper extends SQLiteOpenHelper {
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

    private BookLibDb() {
    }

    public static BookLibDb getInstance() {
        return mInstance;
    }

    public boolean open(Context context) {
        try {
            mDBHelper = new DatabaseHelper(context, DB_NAME, DB_VERSION);
            mDB = mDBHelper.getWritableDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void close() {
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

    public boolean insert(Record note) {
        ContentValues values = new ContentValues();
        values.put(DB_TABLE_COLUMN_bookName , note.bookName);
        values.put(DB_TABLE_COLUMN_bookLink , note.bookLink);
        values.put(DB_TABLE_COLUMN_bookInfo , note.bookInfo);
        values.put(DB_TABLE_COLUMN_bookInfo1, note.bookInfo1);
        values.put(DB_TABLE_COLUMN_bookInfo2, note.bookInfo2);
        note.key = mDB.insert(DB_TABLE_NAME, null, values);
        if (note.key == -1) {
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

    public List<Record> query() {
        Cursor cursor = mDB.query(DB_TABLE_NAME, null, null, null, null, null,
                DB_DEFAULT_ORDERBY, null);
        return extract(0, cursor);
    }

    public List<Record> query(String condition) {
        Cursor cursor = mDB.query(DB_TABLE_NAME, null, condition, null, null, null,
                DB_DEFAULT_ORDERBY, null);
        return extract(0, cursor);
    }


    public List<Record>  rawQuery(String keyword) {
//        Cursor cursor = mDB.query(DB_TABLE_NAME,null, DB_TABLE_COLUMN_bookName+" LIKE ? ",
//                new String[] { "%" + keyword + "%" }, null, null, null);

//        Cursor cursor=mDB.query(DB_TABLE_NAME, null,DB_TABLE_COLUMN_bookName+" like '%" +keyword  + "%'", null, null, null, null);

        String current_sql_sel = "SELECT * FROM "+DB_TABLE_NAME +" where "+DB_TABLE_COLUMN_bookName+" like '%"+keyword +"%'";
        Cursor cursor = mDB.rawQuery(current_sql_sel, null);
        Log.e("gaom getCount",cursor.getCount()+"");
        Log.e("gaom keyword",keyword+"");
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

    protected List<Record> extract(int offset, Cursor cursor) {
        List<Record> notes = new ArrayList<Record>();
        if (cursor == null || cursor.getCount() <= offset) {
            return notes;
        }

        cursor.moveToFirst();
        cursor.moveToPosition(offset);

        do {
            Record note = new Record();
            note.key = cursor.getLong(cursor.getColumnIndex(DB_PRIMARY_KEY));
            note.bookName = cursor.getString(cursor.getColumnIndex(      DB_TABLE_COLUMN_bookName      ));
            note.bookLink = cursor.getString(cursor.getColumnIndex(       DB_TABLE_COLUMN_bookLink  ));
            note.bookInfo = cursor.getString(cursor.getColumnIndex(        DB_TABLE_COLUMN_bookInfo      ));
            note.bookInfo1 = cursor.getString(cursor.getColumnIndex(        DB_TABLE_COLUMN_bookInfo1 ));
            note.bookInfo2 = cursor.getString(cursor.getColumnIndex(     DB_TABLE_COLUMN_bookInfo2 ));
            notes.add(note);
        } while (cursor.moveToNext());

        cursor.close();

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