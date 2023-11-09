package com.example.mylistapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteTableLockedException;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "myListDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE list("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "item VARCHAR NOT NULL UNIQUE,"
                + "checked BOOLEAN)";

        db.execSQL(sql);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE if exists list");
        onCreate(db);
        db.close();
    }

    public Cursor getItemList() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM list WHERE checked = false;", null);

        return cursor;
    }

    public Cursor getSearchItemList(String word) {
        SQLiteDatabase db = getWritableDatabase();   // v포함
        Cursor cursor = db.rawQuery("SELECT * FROM list WHERE item like '%" + word + "%';", null);

        return cursor;
    }

    public Cursor getCheckedItemList() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM list WHERE checked = true;", null);

        return cursor;
    }

    public Cursor getProductList() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM product WHERE id = '';", null);

        return cursor;
    }

    public Cursor getProductView() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM product WHERE id = '';", null);

        return cursor;
    }

    public void insertItem(String item) { // 중복으로 값이 들어올 경우 에러처리 시키기
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO list (item, checked) VALUES('" + item + "', '" + false + "');";
        db.execSQL(sql);

//        ContentValues values = new ContentValues();
//        values.put("item", item);
//
//        long result = db.insert("list", null, values);
//
//        if (result == -1) {
//            Log.println(Log.ASSERT, "t", "Data not inserted");
//        } else {
//            Log.println(Log.ASSERT, "t", "Data inserted");
//        }

        db.close();
    }

    public void updateItemCheck(int id) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM list WHERE id = " + id + ";", null);
        boolean isChecked;

        if (cursor.moveToFirst()) {
            int checkColumnIndex = cursor.getColumnIndex("checked");
            if (cursor.getInt(checkColumnIndex) == 1) {
                isChecked = false;
            } else {
                isChecked = true;
            }

            String sql = "UPDATE list SET checked = " + isChecked + " " +
                    "WHERE id = " + id + ";";
            db.execSQL(sql);
        }
        cursor.close();
    }
}
