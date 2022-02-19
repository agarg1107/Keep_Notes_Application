package com.example.keepnotes.DBRoom;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface Notes_Dao {
    @Insert
    void Insert_Notes(Notes... notes);
    @Query("Delete FROM Notes_Data WHERE id =:id")
    void Delete_Notes(int id);
    @Query("SELECT * FROM Notes_Data")
    LiveData<List<Notes>> get_All_Notes();
    @Query("SELECT * FROM Notes_Data ORDER BY note_priority ASC")
    LiveData<List<Notes>> getHighToLow();
    @Query("SELECT * FROM Notes_Data ORDER BY note_priority DESC")
    LiveData<List<Notes>> getLowToHigh();
    @Update
    void Update_Notes(Notes notes);
}
