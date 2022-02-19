package com.example.keepnotes.DBRoom;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Notes.class}, version = 1)
public abstract class Notes_Database extends RoomDatabase {

    public abstract Notes_Dao notes_dao();

    public static Notes_Database INSTANCE;

    public static Notes_Database getDatabaseINSTANCE(Context context) {

        if (INSTANCE == null) {
            synchronized (Notes_Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.
                            getApplicationContext(),
                            Notes_Database.class,
                            "Notes_Database").allowMainThreadQueries().build();
                }
            }
        }

        return INSTANCE;

    }

}
