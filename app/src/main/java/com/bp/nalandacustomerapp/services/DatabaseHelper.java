package com.bp.nalandacustomerapp.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "nalanda.db";
    private static final String TABLE_NAME = "cart_table";
    private static final String TABLE_NAME_2 = "user_table";
    private static final String TABLE_NAME_3 = "messages_table";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME
                +"(id INTEGER PRIMARY KEY AUTOINCREMENT,item_name TEXT,item_id TEXT,qty TEXT,price TEXT,img_url TEXT);");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_2
                + "(cid INTEGER PRIMARY KEY,name TEXT);");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_3
                + "(id INTEGER PRIMARY KEY AUTOINCREMENT,msg TEXT,title TEXT,subject TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_3);
        onCreate(sqLiteDatabase);
    }
    public boolean insertCart(String name, String id, String qty, String price, String img){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("item_name", name);
        contentValues.put("item_id",id);
        contentValues.put("qty", qty);
        contentValues.put("price",price);
        contentValues.put("img_url", img);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
            return  false;
        else
            return true;
    }

    public Cursor getCartItems(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor results = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return results;
    }

    public Integer clearCart(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,null,null);
    }


    public Integer removeCartItemById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"id=?",new String[]{String.valueOf(id)});
    }

    public void saveUserData(int id, String n) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("cid", id);
        contentValues.put("name", n);

        long result = db.insert(TABLE_NAME_2, null, contentValues);

    }
   /* public void updareUserData(String id, String n){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("cid", id);
        contentValues.put("name",n);

        long result = db.update(TABLE_NAME_2, contentValues, null,null);

    }*/

    public Cursor getUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor results = db.rawQuery("SELECT * FROM " + TABLE_NAME_2 + " WHERE cid=1", null);
        return results;
    }

    public Integer clearUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_2, null, null);
    }

    public void saveMsg(String msg, String title, String subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("msg", msg);
        contentValues.put("title", title);
        contentValues.put("subject", subject);

        long result = db.insert(TABLE_NAME_3, null, contentValues);

    }

    public Cursor getMsgs() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor results = db.rawQuery("SELECT * FROM " + TABLE_NAME_3, null);
        return results;
    }


}