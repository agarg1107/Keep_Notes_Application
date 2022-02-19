package com.example.keepnotes.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

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
import com.example.keepnotes.databinding.ActivityUpdateNotesBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UpdateNotes extends AppCompatActivity {
    ActivityUpdateNotesBinding binding;
    String priority = "1";
    int uid;
    String utitle, usubtitle, udate, upriority, unote;
    String ntitle, nsubtitle, nnote;
    Notes_ViewModel notes_viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        notes_viewModel = new ViewModelProvider(this).get(Notes_ViewModel.class);

        uid = getIntent().getIntExtra("id", 0);
        utitle = getIntent().getStringExtra("title");
        usubtitle = getIntent().getStringExtra("subtitle");
        upriority = getIntent().getStringExtra("priority");
        udate = getIntent().getStringExtra("date");
        unote = getIntent().getStringExtra("note");
        binding.updateTitle.setText(utitle);
        binding.updateSubtitle.setText(usubtitle);
        binding.updateNote.setText(unote);
        switch (upriority) {
            case "1":
                binding.redPriorityUp.setImageResource(R.drawable.ic_baseline_check_24);
                binding.greenPriorityUp.setImageResource(0);
                binding.yellowPriorityUp.setImageResource(0);
                break;
            case "3":
                binding.greenPriorityUp.setImageResource(R.drawable.ic_baseline_check_24);
                binding.redPriorityUp.setImageResource(0);
                binding.yellowPriorityUp.setImageResource(0);
                break;
            case "2":
                binding.yellowPriorityUp.setImageResource(R.drawable.ic_baseline_check_24);
                binding.redPriorityUp.setImageResource(0);
                binding.greenPriorityUp.setImageResource(0);
                break;
        }

        binding.redPriorityUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priority = "1";
                binding.redPriorityUp.setImageResource(R.drawable.ic_baseline_check_24);
                binding.greenPriorityUp.setImageResource(0);
                binding.yellowPriorityUp.setImageResource(0);
            }
        });
        binding.greenPriorityUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priority = "3";
                binding.greenPriorityUp.setImageResource(R.drawable.ic_baseline_check_24);
                binding.redPriorityUp.setImageResource(0);
                binding.yellowPriorityUp.setImageResource(0);
            }
        });
        binding.yellowPriorityUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priority = "2";
                binding.yellowPriorityUp.setImageResource(R.drawable.ic_baseline_check_24);
                binding.redPriorityUp.setImageResource(0);
                binding.greenPriorityUp.setImageResource(0);
            }
        });
        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ntitle = binding.updateTitle.getText().toString();
                nsubtitle = binding.updateSubtitle.getText().toString();
                nnote = binding.updateNote.getText().toString();
                updatenote(ntitle, nsubtitle, nnote);
            }


        });
    }

    private void updatenote(String ntitle, String nsubtitle, String nnote) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MMMM dd,yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        try {
            Notes notes = new Notes();
            notes.id = uid;
            notes.note = nnote;
            notes.note_title = ntitle;
            notes.note_Subtitle = nsubtitle;
            notes.date = formattedDate.toString();
            notes.priority = priority;
            notes_viewModel.update_Note(notes);
            Toast.makeText(UpdateNotes.this, "Note Updated", Toast.LENGTH_SHORT).show();
            finish();

        } catch (Exception e) {
            Log.d("update", "Cause :: Something went wrong while updating the note ::" + e);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deletemenuicon) {
            BottomSheetDialog sheetDialog = new BottomSheetDialog(UpdateNotes.this, R.style.BottomSheetStyle);
            View view = LayoutInflater.from(UpdateNotes.this).inflate(R.layout.delete_layout, (ConstraintLayout) findViewById(R.id.mainlayout));
            sheetDialog.setContentView(view);
            TextView yes, no;
            yes = view.findViewById(R.id.delete_yes_button);
            no = view.findViewById(R.id.delete_no_button);

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notes_viewModel.delete_Note(uid);
                    finish();
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sheetDialog.dismiss();
                }
            });

            sheetDialog.show();
        }
        return true;
    }
}