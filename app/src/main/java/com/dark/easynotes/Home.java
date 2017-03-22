package com.dark.easynotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dark.easynotes.data.NotesListContract;
import com.dark.easynotes.data.NotesListDbHelper;

public class Home extends AppCompatActivity implements View.OnClickListener,
        NotesListAdapter.ListItemClickListener, PopupMenu.OnMenuItemClickListener, SearchView.OnQueryTextListener {


    private final static String LOG_TAG = Home.class.getSimpleName();
    String order = null;
    private NotesListAdapter mAdapter;
    private SQLiteDatabase mDb;
    private int label = 0;
    private Cursor mCursor;


    @Override
    protected void onResume() {
        super.onResume();
        NotesListDbHelper mdbHelper = new NotesListDbHelper(this);
        mDb = mdbHelper.getWritableDatabase();
        Cursor cursor = getAllNotes(null);
        mAdapter.swapCursor(cursor);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_home, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        MenuInflater menuInflater = getMenuInflater();
        View menuItemView;
        switch (item_id) {
            case R.id.action_filter_label:
                menuItemView = findViewById(R.id.action_filter_label);
                PopupMenu popupMenu = new PopupMenu(this, menuItemView);
                popupMenu.setOnMenuItemClickListener(Home.this);
                menuInflater.inflate(R.menu.menu_home_labels, popupMenu.getMenu());
                popupMenu.show();
                return true;
//            case R.id.action_sort:
//                menuItemView = findViewById(R.id.action_sort);
//                popupMenu = new PopupMenu(this, menuItemView);
//                popupMenu.setOnMenuItemClickListener(Home.this);
//                menuInflater.inflate(R.menu.menu_home_sort, popupMenu.getMenu());
//                popupMenu.show();
//                return true;

        }

        return super.onOptionsItemSelected(item);
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
        Cursor cursor = getAllNotes(null);
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
                mAdapter.swapCursor(getAllNotes(null));
            }

        }).attachToRecyclerView(noteslistRecyclerView);

    }


    @Override
    protected void onPause() {
        super.onPause();
        mDb.close();

    }

    private Cursor getAllNotes(String order) {
        if (order == null || order.isEmpty()) {
            order = NotesListContract.NotesListEntry.COLUMN_TIMESTAMP + " DESC";

        }
        Cursor cursor = mDb.query(
                NotesListContract.NotesListEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                order
        );
        Log.v(LOG_TAG, "" + cursor.getCount());
        mCursor = cursor;
        return cursor;
    }

    private Cursor searchNotes(String text) {
        String s = NotesListContract.NotesListEntry.COLUMN_NOTE_TITLE + " LIKE ?" +
                " OR " +
                NotesListContract.NotesListEntry.COLUMN_NOTE_DATA + " LIKE ?";

        //  Log.v(LOG_TAG,"Inside get all notes");
        Cursor cursor = mDb.query(
                NotesListContract.NotesListEntry.TABLE_NAME,
                null,
                s,
                new String[]{"%" + text + "%", "%" + text + "%"},
                null,
                null,
                NotesListContract.NotesListEntry.COLUMN_NOTE_TITLE);


        Log.v(LOG_TAG, "" + cursor.getCount());
        mCursor = cursor;
        return cursor;
    }

    private Cursor getNotesWithLabel(int label) {
        String s = NotesListContract.NotesListEntry.COLUMN_LABEL + " = " + label;

        //  Log.v(LOG_TAG,"Inside get all notes");
        Cursor cursor = mDb.query(
                NotesListContract.NotesListEntry.TABLE_NAME,
                null,
                s,
                null,
                null,
                null,
                NotesListContract.NotesListEntry.COLUMN_TIMESTAMP
        );
        Log.v(LOG_TAG,""+cursor.getCount());
        mCursor = cursor;
        return cursor;
    }

    private boolean removeNote(long id) {
        return mDb.delete(NotesListContract.NotesListEntry.TABLE_NAME,
                NotesListContract.NotesListEntry._ID + " = " + id, null) > 0;
    }

    @Override
    public void onClick(View view) {
        Intent takeNote = new Intent(this, MakeNote.class);
        startActivity(takeNote);
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent takeNote = new Intent(this, MakeNote.class);
        mCursor.moveToPosition(clickedItemIndex);
        String title = mCursor.getString(mCursor.getColumnIndex(NotesListContract.NotesListEntry.COLUMN_NOTE_TITLE));
        String note_text = mCursor.getString(mCursor.getColumnIndex(NotesListContract.NotesListEntry.COLUMN_NOTE_DATA));
        int id = mCursor.getInt(mCursor.getColumnIndex(NotesListContract.NotesListEntry._ID));
        takeNote.putExtra(NotesListContract.NotesListEntry.COLUMN_NOTE_TITLE, title);
        takeNote.putExtra(NotesListContract.NotesListEntry.COLUMN_NOTE_DATA, note_text);
        takeNote.putExtra(NotesListContract.NotesListEntry._ID, id);
        Toast.makeText(this, mCursor.getString(mCursor.getColumnIndex(NotesListContract.NotesListEntry.COLUMN_TIMESTAMP)), Toast.LENGTH_SHORT)
                .show();
        startActivity(takeNote);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        order = null;
        int item_id = item.getItemId();
        switch (item_id) {
            case R.id.all:
                label = 0;
                break;
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
//            case R.id.name:
//                order = NotesListContract.NotesListEntry.COLUMN_NOTE_TITLE;
//                break;
//            case R.id.label:
//                order = NotesListContract.NotesListEntry.COLUMN_LABEL;
//                break;
//            case R.id.time:
//                order = NotesListContract.NotesListEntry.COLUMN_TIMESTAMP;
//                break;
        }
        Cursor cursor;
//        if (item_id == R.id.name || item_id == R.id.time || item_id == R.id.label){
//            cursor = getAllNotes(order);
//            mAdapter.swapCursor(cursor);
//        }else {
        if (label != 0) {
            cursor = getNotesWithLabel(label);
            mAdapter.swapCursor(cursor);
            //Toast.makeText(this,"Click "+label, Toast.LENGTH_SHORT).show();
        } else {
            cursor = getAllNotes(null);
            mAdapter.swapCursor(cursor);
//            }
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.swapCursor(searchNotes(newText));
        return true;
    }
}
