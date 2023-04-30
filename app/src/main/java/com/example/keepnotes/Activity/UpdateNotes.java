package com.example.keepnotes.Activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
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

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;

import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UpdateNotes extends AppCompatActivity {
    ActivityUpdateNotesBinding binding;
    private static final int STORAGE_CODE = 1000;
    String priority = "1";
    int uid;
    String utitle, usubtitle, udate, upriority, unote;
    String ntitle, nsubtitle, nnote;
    TextToSpeech tts;
    Notes_ViewModel notes_viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        notes_viewModel = new ViewModelProvider(this).get(Notes_ViewModel.class);
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.UK);
                }
                else{
                    Toast.makeText(UpdateNotes.this, "Error While speech", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
    public void onPause(){
        if(tts !=null){
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
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
        } else if (item.getItemId() == R.id.app_bar_pdf) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED) {
                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissions, STORAGE_CODE);
                } else {
                    savePdf();
                }
            } else {

                savePdf();
            }
        } else if (item.getItemId() == R.id.update_data) {
            ntitle = binding.updateTitle.getText().toString();
            nsubtitle = binding.updateSubtitle.getText().toString();
            nnote = binding.updateNote.getText().toString();
            updatenote(ntitle, nsubtitle, nnote);
        }
        else if(item.getItemId() == R.id.update_music){
            if(unote.equals("")){
                Toast.makeText(this, "Please enter Note", Toast.LENGTH_SHORT).show();
            }
            else{
                tts.speak(unote,TextToSpeech.QUEUE_FLUSH,null);
            }
        }

        return true;
    }

    private void savePdf() {

        Document mDoc = new Document();
        String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());
        String mFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + mFileName + ".pdf";

        try {
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
            mDoc.open();
            mDoc.add(new Chunk(""));
            mDoc.add(new Paragraph("Title : " + utitle));
            mDoc.add(new Paragraph("\n\n"));
            mDoc.add(new Paragraph("SubTitle : " + usubtitle));
            mDoc.add(new Paragraph("\n\n"));
            mDoc.add(new Paragraph("Note : " + unote));
            mDoc.close();
            Toast.makeText(this, "PDF saved successfully ", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    savePdf();
                } else {

                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}