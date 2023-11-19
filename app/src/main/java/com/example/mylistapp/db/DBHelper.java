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
    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, "myListDB", null, 1);

        db = getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String listSql = "CREATE TABLE list("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "item VARCHAR NOT NULL UNIQUE,"
                + "checked BOOLEAN)";

        String productSql = "CREATE TABLE product("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name VARCHAR NOT NULL,"
                + "image VARCHAR,"
                + "price INTEGER NOT NULL,"
                + "price_unit VARCHAR NOT NULL,"
                + "link VARCHAR NOT NULL,"
                + "etc VARCHAR,"
                + "list_id INTEGER,"
                + "FOREIGN KEY(list_id) REFERENCES list(id) ON DELETE CASCADE);";

        db.execSQL(listSql);
        db.execSQL(productSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE if exists list");
        onCreate(db);
    }

    public Cursor getItemList() {
        return db.rawQuery("SELECT * FROM list WHERE checked = false;", null);
    }

    public Cursor getSearchItemList(String word) {
        return db.rawQuery("SELECT * FROM list WHERE item like '%" + word + "%';", null);
    }

    public Cursor getCheckedItemList() {
        return db.rawQuery("SELECT * FROM list WHERE checked = true;", null);
    }

    public Cursor getProductList(int listId) {
        return db.rawQuery("SELECT * FROM product WHERE list_id = " + listId + ";", null);
    }

    public Cursor getProductView(int id) {
        return db.rawQuery("SELECT * FROM product WHERE id = " + id + ";", null);
    }

    public void insertItem(String item) {
        ContentValues values = new ContentValues();
        values.put("item", item);
        values.put("checked", false);

        long result = db.insert("list", null, values);

        if (result == -1) {
            Log.println(Log.ASSERT, "t", "Data not inserted");
        } else {
            Log.println(Log.ASSERT, "t", "Data inserted");
        }
    }

    public void insertProduct(String name, String image, int price, String priceUnit, String link, String etc, int listId) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("image", image);
        values.put("price", price);
        values.put("price_unit", priceUnit);
        values.put("link", link);
        values.put("etc", etc);
        values.put("list_id", listId);

        long result = db.insert("product", null, values);

        if (result == -1) {
            Log.println(Log.ASSERT, "t", "Data not inserted");
        } else {
            Log.println(Log.ASSERT, "t", "Data inserted");
        }
    }

    public void updateItemCheck(int id) {
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

    public void closeDB() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
