package com.example.keepnotes.DBRoom;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Notes_Data")
public class Notes {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "note_title")
    public String note_title;
    @ColumnInfo(name = "note_Subtitle")
    public String note_Subtitle;
    @ColumnInfo(name = "note_date")
    public String date;
    @ColumnInfo(name = "note_note")
    public String note;
    @ColumnInfo(name = "note_priority")
    public String priority;
}
