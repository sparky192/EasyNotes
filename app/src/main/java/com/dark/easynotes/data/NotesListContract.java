package com.dark.easynotes.data;

import android.provider.BaseColumns;

/**
 * Created by mandar on 3/16/17.
 */

public class NotesListContract {

    public static final class NotesListEntry implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_NOTE_TITLE = "title";
        public static final String COLUMN_NOTE_DATA = "content";
        public static final String COLUMN_LABEL = "label";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
