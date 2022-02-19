package com.example.keepnotes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keepnotes.Activity.UpdateNotes;
import com.example.keepnotes.DBRoom.Notes;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.viewholder> {
    MainActivity mainActivity;
    List<Notes> notes;
    List<Notes> searchnotes;

    public HomeAdapter(MainActivity mainActivity, List<Notes> notes) {
        this.mainActivity = mainActivity;
        this.notes = notes;
        searchnotes = new ArrayList<>(notes);
    }

    public void notessearch(List<Notes> getallsearch) {
        this.notes = getallsearch;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.home_recycle_layout, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Notes note = notes.get(position);
        switch (note.priority) {
            case "1":
                holder.priorityhome.setBackgroundResource(R.drawable.red_circle);
                break;
            case "3":
                holder.priorityhome.setBackgroundResource(R.drawable.green_circle);
                break;
            case "2":
                holder.priorityhome.setBackgroundResource(R.drawable.yellow_circle);
                break;
        }

        holder.title.setText(note.note_title);
        holder.subtitle.setText(note.note_Subtitle);
        holder.date.setText(note.date);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, UpdateNotes.class);
                intent.putExtra("id", note.id);
                intent.putExtra("title", note.note_title);
                intent.putExtra("subtitle", note.note_Subtitle);
                intent.putExtra("priority", note.priority);
                intent.putExtra("date", note.date);
                intent.putExtra("note", note.note);
                mainActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView title, subtitle, date;
        View priorityhome;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.home_title);
            subtitle = itemView.findViewById(R.id.home_subtitle);
            date = itemView.findViewById(R.id.home_date);
            priorityhome = itemView.findViewById(R.id.home_priority);
        }
    }
}
