package com.iyer.rahul.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Aditya Jaijeevan on 04-04-2017.
 */
public class DatabaseHandler extends SQLiteOpenHelper
{
    SQLiteDatabase db;
    Context context;

    public DatabaseHandler(Context context){
        super(context,"DataDB",null,1);
        Log.d("IYER","DBCREATED");
        this.context=context;
        db=this.getWritableDatabase();
    }


    public void add(String phoneNumber, String messageBody)
    {
        /*
        try {
            db.execSQL("INSERT INTO data(phoneNumber,messagebody,date) VALUES (" + phoneNumber + "," + messageBody );
            Log.d("DB123", "INSERTED");
        }
        catch (SQLiteException e){
            Log.d("DB123","INSERT ISSUE");
        }
        */
        ContentValues cv=new ContentValues();
        cv.put("phoneNumber",phoneNumber);
        cv.put("messageBody",messageBody);
        cv.put("date",getDateTime());
        db.insert("data",null,cv);

        Log.d("IYER","data ADDED");
    }

    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE data (phoneNumber TEXT, messageBody TEXT, date TEXT)");
        Log.d("DB123","TABLE CREATED");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS data");

        onCreate(db);
        Log.d("DB123","TABLE DROPPED");
    }
    public String getAll() {
        Cursor cursor = db.rawQuery("SELECT * FROM data",null);
        int phoneNumbercolumn = cursor.getColumnIndex("phoneNumber");
        int messageBodycolumn = cursor.getColumnIndex("messageBody");
        int datecolumn=cursor.getColumnIndex("date");

        StringBuffer sb = new StringBuffer();
        cursor.moveToFirst();

        if (cursor != null && (cursor.getCount() > 0)) {
            do {
                String phoneNumber = cursor.getString(phoneNumbercolumn);
                String messageBody = cursor.getString(messageBodycolumn);
                String date = cursor.getString(datecolumn);


                sb.append("FROM:"+phoneNumber + "\n" + messageBody+"\n"+date+ "\n----------\n");

            } while (cursor.moveToNext());
        }
        return sb.toString();
    }
}
