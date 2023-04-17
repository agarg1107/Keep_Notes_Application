package com.example.keepnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.keepnotes.Activity.InsertNotes;
import com.example.keepnotes.DBRoom.Notes;
import com.example.keepnotes.DBRoom.Notes_ViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton button;
    Notes_ViewModel notes_viewModel;
    RecyclerView recyclerView;
    HomeAdapter adapter;
    List<Notes> filternotesdata;
    TextView nofilter, htol, ltoh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
        switch (item.getItemId()){
            case R.id.app_bar_setting:
                report();
            default:return super.onOptionsItemSelected(item);
        }
    }
    private void report(){
        String subject = "Please Fix this bug";
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "agarg1107@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(Intent.createChooser(emailIntent, null));
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
}