package com.example.xo_game;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class UserDatabase extends SQLiteOpenHelper {
    static final int DATABASE_VERSION = 1;
    static final String TABLE_USER = "user";
    public UserDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_USER+"(name Text,email text unique,password Text unique)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_USER);
        onCreate(db);
    }

    public boolean insertUser(User user){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("name",user.getName());
        values.put("email",user.getEmail());
        values.put("password",user.getPassword());
        long res=db.insert(TABLE_USER,null,values);

        return res!=-1;
    }
    public boolean updateUser(User user){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("name",user.getName());
        long res=db.update(TABLE_USER,values,"phone=?",new String[]{user.getName()});

        return res!=-1;
    }

    public ArrayList<User> retrieveAllData(){
        ArrayList<User>users=new ArrayList<>();
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.rawQuery("select * from "+TABLE_USER,null);

        if(c!=null&&c.moveToFirst()){
            do{
                String name=c.getString(0);
                String password=c.getString(1);
                String email=c.getString(2);
                users.add(new User(name,email,password));
            }while(c.moveToNext());
        }
        c.close();
        return users;
    }
    public Boolean CheckEmailAndPassword(String Email,String Password){
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.rawQuery("select * from user where email=? and password=?",new String[]{Email,Password});
        return c.getCount()>0; //if equally 0 this mean this email not found
    }
    public boolean CheckEmail(String Email){
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.rawQuery("select * from user where email=?",new String[]{Email});
        return c.getCount()>0; //if equally 0 this mean this email not found

    }

}
