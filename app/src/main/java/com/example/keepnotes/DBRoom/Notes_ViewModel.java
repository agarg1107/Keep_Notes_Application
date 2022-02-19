package com.example.keepnotes.DBRoom;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class Notes_ViewModel extends AndroidViewModel {
    public Notes_Repo notes_repo;
    public LiveData<List<Notes>> getAllNotes;
    public LiveData<List<Notes>> HighToLow;
    public LiveData<List<Notes>> LowToHigh;
    public Notes_ViewModel(Application application) {
        super(application);
        notes_repo = new Notes_Repo(application);
        getAllNotes = notes_repo.getAllNotes;
        HighToLow = notes_repo.HighToLow;
        LowToHigh = notes_repo.LowToHigh;
    }
    public void insert_Note(Notes notes)
    {
        notes_repo.insert_Notes(notes);
    }
    public void delete_Note(int id)
    {
        notes_repo.delete_Notes(id);
    }
    public void update_Note(Notes notes)
    {
        notes_repo.Update_Notes(notes);
    }
}
