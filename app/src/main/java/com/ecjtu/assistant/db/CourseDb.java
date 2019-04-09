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

public class CourseDb {

    protected static final String TAG = CourseDb.class.getSimpleName();

    protected static final int DB_VERSION = 5;
    protected static final String DB_NAME = "course_db";
    protected static final String DB_PRIMARY_KEY = "_id";
    protected static final String DB_TABLE_NAME = "course";


    protected static final String DB_TABLE_COLUMN_courseName = "courseName";
    protected static final String DB_TABLE_COLUMN_courseTime= "courseTime";
    protected static final String DB_TABLE_COLUMN_DATE = "date";
    protected static final String DB_TABLE_COLUMN_courseScore = "courseScore";
    protected static final String DB_TABLE_COLUMN_courseType = "courseType";
    protected static final String DB_TABLE_COLUMN_courseTestWay = "courseTestWay";

    protected static final String DB_DEFAULT_ORDERBY = DB_TABLE_COLUMN_DATE + " DESC";

    protected static final String DB_TABLE_NAME_usercourse = "usercourse";
    protected static final String DB_TABLE_COLUMN_courseId = "courseId";
    protected static final String DB_TABLE_COLUMN_userId= "userId";

    protected DatabaseHelper mDBHelper;
    protected SQLiteDatabase mDB;

    protected static final CourseDb mInstance = new CourseDb();

    private final String DB_TABLE_CREATE_SQL = "create table " + DB_TABLE_NAME + " (_id integer primary key autoincrement, "
            + DB_TABLE_COLUMN_courseName + " text, "
            + DB_TABLE_COLUMN_courseTime + " text, "
            + DB_TABLE_COLUMN_courseScore + " text, "
            + DB_TABLE_COLUMN_courseType + " text, "
            + DB_TABLE_COLUMN_courseTestWay + " text, "
            + DB_TABLE_COLUMN_DATE + " text);";

    private final String DB_TABLE_CREATE_SQL2 = "create table " + DB_TABLE_NAME_usercourse + " (_id integer primary key autoincrement, "
            + DB_TABLE_COLUMN_courseId + " text, "
            + DB_TABLE_COLUMN_userId + " text, "
            + DB_TABLE_COLUMN_DATE + " text);";

    public static class UserCourse {
        public long key = -1;
        public String courseId;
        public String userId;
    }
    public static class Record {
        public long key = -1;
        public String courseName;
        public String courseTime;
        public String courseScore;
        public String courseType;
        public String courseTestWay;
    }

    protected class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String dbName, int dbVersion) {
            super(context, dbName, null, dbVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_TABLE_CREATE_SQL);
            db.execSQL(DB_TABLE_CREATE_SQL2);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME_usercourse);
            onCreate(db);
        }
    }

    private CourseDb() {
    }

    public static CourseDb getInstance() {
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
        values.put(DB_TABLE_COLUMN_courseName, note.courseName);
        values.put(DB_TABLE_COLUMN_courseScore, note.courseScore);
        values.put(DB_TABLE_COLUMN_courseTestWay, note.courseTestWay);
        values.put(DB_TABLE_COLUMN_courseType, note.courseType);
        values.put(DB_TABLE_COLUMN_courseTime, note.courseTime);
        note.key = mDB.insert(DB_TABLE_NAME, null, values);
        if (note.key == -1) {
            Log.e(TAG, "db insert fail!");
            return false;
        }
        return true;
    }

    public boolean insertUserCourse(UserCourse note) {
        ContentValues values = new ContentValues();
        values.put(DB_TABLE_COLUMN_courseId, note.courseId);
        values.put(DB_TABLE_COLUMN_userId, note.userId);
        note.key = mDB.insert(DB_TABLE_NAME_usercourse, null, values);
        if (note.key == -1) {
            Log.e(TAG, "db insert fail!");
            return false;
        }
        return true;
    }

    public boolean deleteUserCourse(String whereClause, String[] whereArgs) {
        int rows = mDB.delete(DB_TABLE_NAME_usercourse, whereClause, whereArgs);
        Log.e("gaom", "db delete rows="+rows);

        if (rows <= 0) {
            Log.e(TAG, "db delete fail!");
            return false;
        }
        return true;
    }

    public List<UserCourse> queryUserCourse(String condition) {
        Cursor cursor = mDB.query(DB_TABLE_NAME_usercourse, null, condition, null, null, null,
                DB_DEFAULT_ORDERBY, null);
        return extractUserCourse(0, cursor);
    }
    public boolean delete(int position) {
        long key = getkey(position, null);
        if (key == -1) {
            return false;
        }
        String condition = DB_PRIMARY_KEY + "=" + "\'" + key + "\'";
        return delete(condition, null);
    }
    public boolean deleteUserCourse(long key) {
        if (key == -1) {
            return false;
        }
        String condition = DB_PRIMARY_KEY + "=" + "\'" + key + "\'";
        return deleteUserCourse(condition, null);
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

    public List< Record>  rawQuery(String current_sql_sel) {
        Cursor cursor = mDB.rawQuery(current_sql_sel, null);
//        Log.e("gaom getCount",cursor.getCount()+"");
//        Log.e("gaom keyword",keyword+"");
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
            note.courseName = cursor.getString(cursor.getColumnIndex(DB_TABLE_COLUMN_courseName));
            note.courseScore = cursor.getString(cursor.getColumnIndex(DB_TABLE_COLUMN_courseScore));
            note.courseTestWay = cursor.getString(cursor.getColumnIndex(DB_TABLE_COLUMN_courseTestWay));
            note.courseTime = cursor.getString(cursor.getColumnIndex(DB_TABLE_COLUMN_courseTime));
            note.courseType = cursor.getString(cursor.getColumnIndex(DB_TABLE_COLUMN_courseType));
            notes.add(note);
        } while (cursor.moveToNext());

        cursor.close();

        return notes;
    }
 protected List<UserCourse> extractUserCourse(int offset, Cursor cursor) {
        List<UserCourse> notes = new ArrayList<UserCourse>();
        if (cursor == null || cursor.getCount() <= offset) {
            return notes;
        }

        cursor.moveToFirst();
        cursor.moveToPosition(offset);

        do {
            UserCourse note = new UserCourse();
            note.key = cursor.getLong(cursor.getColumnIndex(DB_PRIMARY_KEY));
            note.courseId = cursor.getString(cursor.getColumnIndex(DB_TABLE_COLUMN_courseId));
            note.userId = cursor.getString(cursor.getColumnIndex(DB_TABLE_COLUMN_userId));
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