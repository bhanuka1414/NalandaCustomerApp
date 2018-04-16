package com.bp.nalandacustomerapp.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "nalanda.db";
    private static final String TABLE_NAME = "cart_table";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME
                +"(id INTEGER PRIMARY KEY AUTOINCREMENT,item_name TEXT,item_id TEXT,qty TEXT,price TEXT,img_url TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

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


}