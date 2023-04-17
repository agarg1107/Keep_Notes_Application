package com.example.keepnotes.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keepnotes.DBRoom.Notes;
import com.example.keepnotes.DBRoom.Notes_ViewModel;
import com.example.keepnotes.R;
import com.example.keepnotes.databinding.ActivityInsertNoteBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class InsertNotes extends AppCompatActivity {

    ActivityInsertNoteBinding binding;
    String title, subtitle, note;
    Notes_ViewModel notes_viewModel;
    String priority = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        notes_viewModel = new ViewModelProvider(this).get(Notes_ViewModel.class);

        binding.redPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priority = "1";
                binding.redPriority.setImageResource(R.drawable.ic_baseline_check_24);
                binding.greenPriority.setImageResource(0);
                binding.yellowPriority.setImageResource(0);
            }
        });
        binding.greenPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priority = "3";
                binding.greenPriority.setImageResource(R.drawable.ic_baseline_check_24);
                binding.redPriority.setImageResource(0);
                binding.yellowPriority.setImageResource(0);
            }
        });
        binding.yellowPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priority = "2";
                binding.yellowPriority.setImageResource(R.drawable.ic_baseline_check_24);
                binding.redPriority.setImageResource(0);
                binding.greenPriority.setImageResource(0);
            }
        });



    }

    private void create_note(String title, String subtitle, String note) {

        try {
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("MMMM dd,yyyy", Locale.getDefault());
            String formattedDate = df.format(c);
            Notes notes = new Notes();
            notes.note_title = title;
            notes.note_Subtitle = subtitle;
            notes.note = note;
            notes.priority = priority;
            notes.date = formattedDate.toString();
            notes_viewModel.insert_Note(notes);
            Toast.makeText(InsertNotes.this, "Note created successfully", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Log.d("Create", "Cause :: Something went wrong while creating note ::"+e);
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.insert_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.insert_main_data) {
            title = binding.insertTitle.getText().toString();
            subtitle = binding.insertSubtitle.getText().toString();
            note = binding.insertNote.getText().toString();
            create_note(title, subtitle, note);
        }
        return true;
    }
}