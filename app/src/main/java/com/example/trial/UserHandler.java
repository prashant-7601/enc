package com.example.trial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class UserHandler extends SQLiteOpenHelper {
    public UserHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE LOGS ( date VARCHAR2(20), time VARCHAR(20), " +
                "type VARCHAR2(20), security_key VARCHAR(16), " +
                "file_name VARCHAR2(20), PRIMARY KEY(date, time))";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String drop = String.valueOf("DROP TABLE IF EXISTS");
        db.execSQL(drop, new String[] {"LOGS"});
        onCreate(db);
    }

    public void addInfo(UserHelperClass u){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", u.getDate());
        values.put("time", u.getTime());
        values.put("type", u.getType());
        values.put("security_key", u.getKey());
        values.put("file_name", u.getFile_name());
        long k = db.insert("LOGS", null, values);
        Log.d("myTag", String.valueOf(k));
        db.close();
        //getInfo(w.getDate(), w.getTime());
    }

    public UserHelperClass getInfo(String date, String time){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT  * FROM LOGS where date =? and time =?";
        Cursor cursor = db.rawQuery(query, new String[]{date, time});
        Log.d("myTagCursor", String.valueOf(cursor));
        String type = "";
        String key = "";
        String file_name = "";
        if(cursor!=null && cursor.moveToFirst()){
            type = cursor.getString(2);
            key = cursor.getString(3);
            file_name = cursor.getColumnName(4);
        }
        else
            Log.d("myTag", "Some error occurred");
        return new UserHelperClass(type, key, file_name, date, time);
    }

    public ArrayList<UserHelperClass> getAllInfo(){
        ArrayList<UserHelperClass> data = new ArrayList<UserHelperClass>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT  * FROM LOGS";
        Cursor cursor = db.rawQuery(query, new String[]{});
        Log.d("myTagCursor", String.valueOf(cursor));
        String type = "";
        String key = "";
        String file_name = "";
        String date = "";
        String time = "";
        if(cursor!=null && cursor.moveToFirst()) {
            do {
                date = cursor.getString(0);
                time = cursor.getString(1);
                type = cursor.getString(2);
                key = cursor.getString(3);
                file_name = cursor.getString(4);
                data.add(new UserHelperClass(type, key, file_name, date, time));

            }
            while(cursor.moveToNext());
        }
        else
            Log.d("myTag", "Some error occurred");
        return data;
    }
}
