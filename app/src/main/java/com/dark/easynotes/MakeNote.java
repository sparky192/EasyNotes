package com.dark.easynotes;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dark.easynotes.data.NotesListContract;
import com.dark.easynotes.data.NotesListDbHelper;

public class MakeNote extends AppCompatActivity implements View.OnClickListener {

    private SQLiteDatabase mDb;
    private EditText title, text;
    private final static String LOG_TAG = MakeNote.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        NotesListDbHelper mdbHelper = new NotesListDbHelper(this);
        mDb = mdbHelper.getWritableDatabase();
        title = (EditText) findViewById(R.id.titleNoteET);
        text = (EditText) findViewById(R.id.textNoteET);

    }

    private long addNewNote(String title, String text, int label) {
        ContentValues cv = new ContentValues();
        cv.put(NotesListContract.NotesListEntry.COLUMN_NOTE_TITLE, title);
        cv.put(NotesListContract.NotesListEntry.COLUMN_NOTE_DATA, text);
        cv.put(NotesListContract.NotesListEntry.COLUMN_LABEL, label);
        Log.v(LOG_TAG,title+" : "+text);
        return mDb.insert(NotesListContract.NotesListEntry.TABLE_NAME, null, cv);
    }


    @Override
    public void onClick(View view) {
        String text_string = text.getText().toString();
        String title_string = title.getText().toString();
        addNewNote(title_string,text_string, 1);
        Toast.makeText(this, this.getResources().getString(R.string.note_added), Toast.LENGTH_SHORT)
                .show();
        onBackPressed();
    }
}
