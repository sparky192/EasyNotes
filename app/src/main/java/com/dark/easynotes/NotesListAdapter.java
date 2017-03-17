package com.dark.easynotes;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dark.easynotes.data.NotesListContract;

/**
 * Created by mandar on 3/16/17.
 */

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NotesViewHolder> {

        // Holds on to the cursor to display the waitlist
        private Cursor mCursor;
        private Context mContext;
        private final static String LOG_TAG = NotesListAdapter.class.getSimpleName();



    public NotesListAdapter(Context context, Cursor cursor) {
            this.mContext = context;
            this.mCursor = cursor;
        }

        @Override
        public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.notes_list_item, parent, false);
            return new NotesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NotesViewHolder holder, int position) {
            if (!mCursor.moveToPosition(position)) {
                return;
            }

            // Update the view holder with the information needed to display
            String title = mCursor.getString(mCursor.getColumnIndex(NotesListContract.NotesListEntry.COLUMN_NOTE_TITLE));
            String note_text = mCursor.getString(mCursor.getColumnIndex(NotesListContract.NotesListEntry.COLUMN_NOTE_DATA));
            // COMPLETED (6) Retrieve the id from the cursor and
            long id = mCursor.getLong(mCursor.getColumnIndex(NotesListContract.NotesListEntry._ID));

            // Display the guest name
            holder.noteTitleTextView.setText(title);
            // Display the party count
            holder.noteTextView.setText(String.valueOf(note_text));
            // COMPLETED (7) Set the tag of the itemview in the holder to the id
            holder.itemView.setTag(id);
            Log.v(LOG_TAG, title+" : "+note_text);

        }


        @Override
        public int getItemCount() {
            return mCursor.getCount();
        }


        public void swapCursor(Cursor newCursor) {
            if (mCursor != null) mCursor.close();
            mCursor = newCursor;
            if (newCursor != null) {
                this.notifyDataSetChanged();
            }
        }

        /**
         * Inner class to hold the views needed to display a single item in the recycler-view
         */
        class NotesViewHolder extends RecyclerView.ViewHolder {

            TextView noteTitleTextView;
            TextView noteTextView;


            public NotesViewHolder(View itemView) {
                super(itemView);
                noteTitleTextView = (TextView) itemView.findViewById(R.id.noteTitle);
                noteTextView = (TextView) itemView.findViewById(R.id.noteText);
            }

        }
    }

