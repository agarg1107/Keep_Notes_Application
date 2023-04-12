package com.example.keepnotes.DBRoom;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class    Notes_Repo {

    public Notes_Dao notes_dao;
    public LiveData<List<Notes>> getAllNotes;
    public LiveData<List<Notes>> HighToLow;
    public LiveData<List<Notes>> LowToHigh;
    public Notes_Repo(Application application) {
        Notes_Database notes_database = Notes_Database.getDatabaseINSTANCE(application);
        notes_dao= notes_database.notes_dao();
        getAllNotes = notes_dao.get_All_Notes();
        HighToLow = notes_dao.getHighToLow();
        LowToHigh = notes_dao.getLowToHigh();
    }
    public void insert_Notes(Notes notes)
    {
        notes_dao.Insert_Notes(notes);
    }
    public void delete_Notes(int id)
    {
        notes_dao.Delete_Notes(id);
    }
    public void Update_Notes(Notes notes)
    {
        notes_dao.Update_Notes(notes);
    }

}
