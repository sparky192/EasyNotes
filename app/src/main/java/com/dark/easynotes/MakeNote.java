package com.dark.easynotes;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dark.easynotes.data.NotesListContract;
import com.dark.easynotes.data.NotesListDbHelper;

public class MakeNote extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private final static String LOG_TAG = MakeNote.class.getSimpleName();
    private EditText title, text;
    private int id = 0;
    private int label = 0;
    private boolean isNew = true;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_makenote, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.action_label:
                View menuItemView = findViewById(R.id.action_label);
                PopupMenu popupMenu = new PopupMenu(this, menuItemView);
                popupMenu.setOnMenuItemClickListener(this);
                MenuInflater menuInflater = getMenuInflater();
                menuInflater.inflate(R.menu.labels_menu, popupMenu.getMenu());
                popupMenu.show();
                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getString(R.string.alert_save_note_title));

        // set dialog message
        // alertDialogBuilder.setMessage(R.string.alert_save_note_text);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

                String text_string = text.getText().toString();
                String title_string = title.getText().toString();
                if (isNew) {
                    addNewNote(title_string, text_string, label);
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.note_added), Toast.LENGTH_SHORT)
                            .show();
                } else {
                    updateNote(title_string, text_string, label, id);
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.note_updated), Toast.LENGTH_SHORT)
                            .show();
                }
                backPress();

            }
        });
        alertDialogBuilder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if this button is clicked, just close
                // the dialog box and do nothing
                dialog.cancel();
                backPress();

            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        //super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar



        title = (EditText) findViewById(R.id.titleNoteET);
        text = (EditText) findViewById(R.id.textNoteET);
        if (intent.hasExtra(NotesListContract.NotesListEntry._ID)) {
            id = intent.getIntExtra(NotesListContract.NotesListEntry._ID, 0);
            isNew = false;
        }
        if (intent.hasExtra(NotesListContract.NotesListEntry.COLUMN_NOTE_TITLE)) {
            title.setText(intent.getStringExtra(NotesListContract.NotesListEntry.COLUMN_NOTE_TITLE));
            id = intent.getIntExtra(NotesListContract.NotesListEntry._ID, 0);
            isNew = false;

        }
        if (intent.hasExtra(NotesListContract.NotesListEntry.COLUMN_NOTE_DATA)) {
            text.setText(intent.getStringExtra(NotesListContract.NotesListEntry.COLUMN_NOTE_DATA));
            isNew = false;

        }



    }

    private long addNewNote(String title, String text, int label) {
        NotesListDbHelper mdbHelper = new NotesListDbHelper(this);
        SQLiteDatabase mDb = mdbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NotesListContract.NotesListEntry.COLUMN_NOTE_TITLE, title);
        cv.put(NotesListContract.NotesListEntry.COLUMN_NOTE_DATA, text);
        if (label == 0) {
            cv.put(NotesListContract.NotesListEntry.COLUMN_LABEL, 3);
        } else {
            cv.put(NotesListContract.NotesListEntry.COLUMN_LABEL, label);
        }
        Log.v(LOG_TAG, title + " : " + text + " add");
        return mDb.insert(NotesListContract.NotesListEntry.TABLE_NAME, null, cv);
    }


    private long updateNote(String title, String text, int label, int ID) {
        NotesListDbHelper mdbHelper = new NotesListDbHelper(this);
        SQLiteDatabase mDb = mdbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String strFilter = "_id = " + ID;
        cv.put(NotesListContract.NotesListEntry.COLUMN_NOTE_TITLE, title);
        cv.put(NotesListContract.NotesListEntry.COLUMN_NOTE_DATA, text);
        cv.put(NotesListContract.NotesListEntry.COLUMN_LABEL, label);
        Log.v(LOG_TAG, ID + ": " + title + " :-- " + text + " update");
        return mDb.update(NotesListContract.NotesListEntry.TABLE_NAME, cv, strFilter, null);
    }

    @Override
    public void onClick(View view) {
        String text_string = text.getText().toString();
        String title_string = title.getText().toString();
        if (isNew) {
            addNewNote(title_string, text_string, label);
            Toast.makeText(this, this.getResources().getString(R.string.note_added), Toast.LENGTH_SHORT)
                    .show();
        } else {
            updateNote(title_string, text_string, label, id);
            Toast.makeText(this, this.getResources().getString(R.string.note_updated), Toast.LENGTH_SHORT)
                    .show();
        }
        backPress();
//        Intent back = new Intent(this, Home.class);
//        startActivity(back);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int i = item.getItemId();
        switch (i) {
            case R.id.red:
                label = 1;
                break;
            case R.id.orange:
                label = 2;
                break;
            case R.id.yellow:
                label = 3;
                break;
            case R.id.green:
                label = 4;
                break;
            case R.id.blue:
                label = 5;
                break;
        }
        Toast.makeText(this, "Label set!", Toast.LENGTH_SHORT).show();
        return true;
    }

    public void backPress() {
        NavUtils.navigateUpFromSameTask(this);
    }


}
