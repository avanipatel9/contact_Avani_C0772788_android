package com.avanipatel9.contact_avani_c0772788_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ContactDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ContactDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "contact_list";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FNAME = "fname";
    private static final String COLUMN_LNAME = "lname";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_EMAIL = "email";

    public ContactDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER NOT NULL CONSTRAINT person_pk PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FNAME + " varchar(200) NOT NULL, " +
                COLUMN_LNAME + " varchar(200) NOT NULL, " +
                COLUMN_PHONE + " varchar(20) NOT NULL, " +
                COLUMN_ADDRESS + " varchar(300) NOT NULL," +
                COLUMN_EMAIL + " varchar(300) NOT NULL); ";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        db.execSQL(sql);
        onCreate(db);
    }

    boolean addContact(String fname, String lname, String phone, String address,String email) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FNAME,fname);
        cv.put(COLUMN_LNAME,lname);
        cv.put(COLUMN_PHONE,phone);
        cv.put(COLUMN_ADDRESS,address);
        cv.put(COLUMN_EMAIL,email);

        return  sqLiteDatabase.insert(TABLE_NAME,null,cv)!= 1;
    }

    Cursor getAllData(){
        SQLiteDatabase sqLiteDatabase =getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME,null);
    }

    boolean updateContact(int id,String fname,String lname,String phone,String address,String email){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FNAME,fname);
        cv.put(COLUMN_LNAME,lname);
        cv.put(COLUMN_PHONE,phone);
        cv.put(COLUMN_ADDRESS,address);
        cv.put(COLUMN_EMAIL,email);

        //returns the affected num of rows;
        return  sqLiteDatabase.update(TABLE_NAME,cv,COLUMN_ID+" = ? ",new String[]{String.valueOf(id)}) >0 ;
    }

    boolean deleteContact(int id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return  sqLiteDatabase.delete(TABLE_NAME,COLUMN_ID+" = ? ",new String[]{String.valueOf(id)}) >0;

    }
}
