package com.example.keepnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keepnotes.Activity.InsertNotes;
import com.example.keepnotes.DBRoom.Notes;
import com.example.keepnotes.DBRoom.Notes_ViewModel;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton button;
    Notes_ViewModel notes_viewModel;
    RecyclerView recyclerView;
    HomeAdapter adapter;
    List<Notes> filternotesdata;
    TextView nofilter, htol, ltoh;
    Bitmap bitmap;

    private  static  final int Request_cam_code =100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA
            },Request_cam_code);
            Log.d("checkdatamain","done3");
        }
        button = findViewById(R.id.newnotes);
        recyclerView = findViewById(R.id.notes_recycle);
        ltoh = findViewById(R.id.ltoh);
        htol = findViewById(R.id.htol);
        nofilter = findViewById(R.id.nofilter);

        nofilter.setBackgroundResource(R.drawable.filterbackselected);

        nofilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loaddata(0);
                nofilter.setBackgroundResource(R.drawable.filterbackselected);
                htol.setBackgroundResource(R.drawable.filterback);
                ltoh.setBackgroundResource(R.drawable.filterback);
            }
        });
        htol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loaddata(1);
                htol.setBackgroundResource(R.drawable.filterbackselected);
                ltoh.setBackgroundResource(R.drawable.filterback);
                nofilter.setBackgroundResource(R.drawable.filterback);
            }
        });
        ltoh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loaddata(2);
                ltoh.setBackgroundResource(R.drawable.filterbackselected);
                nofilter.setBackgroundResource(R.drawable.filterback);
                htol.setBackgroundResource(R.drawable.filterback);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNote();
            }
        });

        notes_viewModel = new ViewModelProvider(this).get(Notes_ViewModel.class);
        notes_viewModel.getAllNotes.observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> notes) {
                setAdapter(notes);
            }
        });


    }
    private void createNote(){
        startActivity(new Intent(MainActivity.this, InsertNotes.class));
    }
    private void loaddata(int i) {
        if (i == 0) {
            notes_viewModel.getAllNotes.observe(this, new Observer<List<Notes>>() {
                @Override
                public void onChanged(List<Notes> notes) {

                    setAdapter(notes);
                    filternotesdata = notes;
                }
            });
        } else if (i == 1) {
            notes_viewModel.HighToLow.observe(this, new Observer<List<Notes>>() {
                @Override
                public void onChanged(List<Notes> notes) {

                    setAdapter(notes);
                    filternotesdata = notes;
                }
            });
        } else if (i == 2) {
            notes_viewModel.LowToHigh.observe(this, new Observer<List<Notes>>() {
                @Override
                public void onChanged(List<Notes> notes) {

                    setAdapter(notes);
                    filternotesdata = notes;
                }
            });
        }
    }

    public void setAdapter(List<Notes> notes) {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter = new HomeAdapter(MainActivity.this, notes);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);

        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search Notes Here ...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {
                    Notessearcher(s);
                } catch (Exception e) {
                    Log.d("search", "" + e);
                }

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.app_bar_ocr){
            ocr();
        }
        else if(item.getItemId() == R.id.app_bar_setting){
            report();
        }

            return super.onOptionsItemSelected(item);


    }
    private void report(){
        String subject = "Please Fix this bug";
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "agarg1107@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(Intent.createChooser(emailIntent, null));
    }
    private void ocr(){
        Log.d("checkdatamain","done2");

        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(MainActivity.this);
    }
    private void Notessearcher(String s) {
        ArrayList<Notes> newnotes = new ArrayList<>();
        for (Notes notes : this.filternotesdata) {
            if (notes.note_title.contains(s) || notes.note_Subtitle.contains(s)) {
                newnotes.add(notes);
            }
        }
        this.adapter.notessearch(newnotes);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("checkdatamain","done5");
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            Log.d("checkdatamain","done6");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Log.d("checkdatamain",requestCode+"");

                Log.d("checkdatamain","done7");
                Uri resultUri = result.getUri();
                try {
                    Log.d("checkdatamain","done8");
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),resultUri);
                    getText(bitmap);
                } catch (IOException e) {
                    Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show();
                }

        }
    }
    private void getText(Bitmap bitmap){
        Log.d("checkdatamain","done9");
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if(!recognizer.isOperational()){
            Log.d("checkdatamain","done10");
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.d("checkdatamain","done11");
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for(int i =0; i<textBlockSparseArray.size();i++){
                Log.d("checkdatamain","done12");
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
            String message = stringBuilder.toString();
            Intent intent = new Intent(MainActivity.this, InsertNotes.class);
            intent.putExtra("message", message);
            startActivity(intent);
        }
    }
}