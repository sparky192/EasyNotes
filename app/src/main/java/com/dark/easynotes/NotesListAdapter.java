package com.dark.easynotes;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dark.easynotes.data.NotesListContract;



public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NotesViewHolder> {

    private final static String LOG_TAG = NotesListAdapter.class.getSimpleName();
    final private ListItemClickListener mOnClickListener;
    private Cursor mCursor;
    private Context mContext;

    public NotesListAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
        mOnClickListener = (ListItemClickListener) context;
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

        String title = mCursor.getString(mCursor.getColumnIndex(NotesListContract.NotesListEntry.COLUMN_NOTE_TITLE));
        String note_text = mCursor.getString(mCursor.getColumnIndex(NotesListContract.NotesListEntry.COLUMN_NOTE_DATA));
        long id = mCursor.getLong(mCursor.getColumnIndex(NotesListContract.NotesListEntry._ID));
        int label = mCursor.getInt(mCursor.getColumnIndex(NotesListContract.NotesListEntry.COLUMN_LABEL));

        holder.noteTitleTextView.setText(title);
        holder.noteTextView.setText(String.valueOf(note_text));
        holder.itemView.setTag(id);

        switch (label) {
            case 1:
                holder.tag.setImageResource(R.drawable.ic_tag_red);
                break;
            case 2:
                holder.tag.setImageResource(R.drawable.ic_tag_orange);
                break;
            case 3:
                holder.tag.setImageResource(R.drawable.ic_tag_yellow);
                break;
            case 4:
                holder.tag.setImageResource(R.drawable.ic_tag_green);
                break;
            case 5:
                holder.tag.setImageResource(R.drawable.ic_tag_blue);
                break;
        }

        Log.v(LOG_TAG, title + " : " + note_text);

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


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    /**
     * Inner class to hold the views needed to display a single item in the recycler-view
     */
    class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView noteTitleTextView;
        TextView noteTextView;
        ImageButton tag;


        public NotesViewHolder(View itemView) {
            super(itemView);
            noteTitleTextView = (TextView) itemView.findViewById(R.id.noteTitle);
            noteTextView = (TextView) itemView.findViewById(R.id.noteText);
            tag = (ImageButton) itemView.findViewById(R.id.imageButton2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}

