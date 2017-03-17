package com.dark.easynotes;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.dark.easynotes.data.NotesListContract;
import com.dark.easynotes.data.NotesListDbHelper;

public class Home extends AppCompatActivity implements View.OnClickListener{

    private NotesListAdapter mAdapter;
    private SQLiteDatabase mDb;
    private final static String LOG_TAG = Home.class.getSimpleName();

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = getAllNotes();
        mAdapter.swapCursor(cursor);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        RecyclerView noteslistRecyclerView = (RecyclerView) this.findViewById(R.id.notes_list_rv);
        noteslistRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        NotesListDbHelper mdbHelper = new NotesListDbHelper(this);
        mDb = mdbHelper.getWritableDatabase();
        Cursor cursor = getAllNotes();
        mAdapter = new NotesListAdapter(this, cursor);
        noteslistRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                long id = (long) viewHolder.itemView.getTag();
                removeNote(id);
                mAdapter.swapCursor(getAllNotes());
            }

        }).attachToRecyclerView(noteslistRecyclerView);

    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {


        return super.onCreateView(parent, name, context, attrs);
    }



    private Cursor getAllNotes() {
        Log.v(LOG_TAG,"Inside get all notes");
        Cursor cursor = mDb.query(
                NotesListContract.NotesListEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                NotesListContract.NotesListEntry.COLUMN_TIMESTAMP
        );
        Log.v(LOG_TAG,""+cursor.getCount());
        return cursor;
    }

    private boolean removeNote(long id) {
        return mDb.delete(NotesListContract.NotesListEntry.TABLE_NAME,
                NotesListContract.NotesListEntry._ID + "=" + id, null) > 0;
    }

    @Override
    public void onClick(View view) {
        Intent takeNote = new Intent(this, MakeNote.class);
        startActivity(takeNote);
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
    }
}
