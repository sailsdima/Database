package com.example.sails.database;

/**
 * Created by sails on 17.09.2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by sails on 11.09.2016.
 */
public class DBHelperUniv extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Universitet";
    public static final int DATABASE_VERSION = 9;

    public static final String TABLE_VYKLADACH = "vykladach";
    public static final String TABLE_VYKLADACH_KODVYKL = "KodVykl";
    public static final String TABLE_VYKLADACH_PRIZVVYKL = "PrizvVykl";
    public static final String TABLE_VYKLADACH_KODPOST = "KodPost";
    public static final String TABLE_VYKLADACH_NOMKAF = "NomKaf";

    public static final String TABLE_POSADA = "posada";
    public static final String TABLE_POSADA_KODPOST = "KodPost";
    public static final String TABLE_POSADA_POSTVYKL = "PostVykl";
    public static final String TABLE_POSADA_NORMPOST = "NormPost";

    public static final String LOG_TAG = "dbLogs";

    ArrayList <String> tables;

    public DBHelperUniv(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        Log.d(LOG_TAG, "DBHelperUniv constructor worked.");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.d(LOG_TAG, "class DBHelperUniv, method onCreate is working.");

        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_VYKLADACH + " (" + TABLE_VYKLADACH_KODVYKL + " TEXT, " + TABLE_VYKLADACH_PRIZVVYKL + " TEXT, " +
                TABLE_VYKLADACH_KODPOST + " TEXT, " + TABLE_VYKLADACH_NOMKAF + " TEXT);";
        sqLiteDatabase.execSQL(query);
        Log.d(LOG_TAG, query);

        query = "CREATE TABLE IF NOT EXISTS " + TABLE_POSADA + " (" + TABLE_POSADA_KODPOST + " TEXT, " + TABLE_POSADA_POSTVYKL + " TEXT, " + TABLE_POSADA_NORMPOST + " TEXT);";
        sqLiteDatabase.execSQL(query);

        Log.d(LOG_TAG, query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(LOG_TAG, "Method onUpgrade is working. Current version: " + i + "; New version: " + i1);

        onCreate(sqLiteDatabase);

        if(i1 > i){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_VYKLADACH + ";");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_POSADA + ";");

            onCreate(sqLiteDatabase);

            Log.d(LOG_TAG, "New version > Current version. Upgrading DB.");
        }

    }
}
