package com.dark.easynotes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mandar on 3/16/17.
 */

public class NotesListDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "notes.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public NotesListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold waitlist data
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + NotesListContract.NotesListEntry.TABLE_NAME + " (" +
                NotesListContract.NotesListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NotesListContract.NotesListEntry.COLUMN_NOTE_TITLE + " TEXT NOT NULL, " +
                NotesListContract.NotesListEntry.COLUMN_NOTE_DATA + " TEXT NOT NULL, " +
                NotesListContract.NotesListEntry.COLUMN_LABEL + " INTEGER DEFAULT 1, " +
                NotesListContract.NotesListEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NotesListContract.NotesListEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
