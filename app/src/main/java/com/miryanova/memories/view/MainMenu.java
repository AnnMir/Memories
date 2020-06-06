package com.miryanova.memories.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miryanova.memories.controller.NotesController;
import com.miryanova.memories.controller.RecyclerItemClickListener;
import com.miryanova.memories.model.ShortNote;
import com.miryanova.memories.model.User;
import com.miryanova.memories.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainMenu extends AppCompatActivity {

    private ArrayList<ShortNote> shortNotes;
    private NotesController notesController;
    private RecyclerView recyclerView;

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView titleView;
        private TextView dateView;
        private TextView uuid;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.titleView = itemView.findViewById(R.id.title);
            this.dateView = itemView.findViewById(R.id.date);
            this.uuid = itemView.findViewById(R.id.uuid);
        }

        public void setItem(String title, String date, String Uuid){
            titleView.setText(title);
            dateView.setText(date);
            uuid.setText(Uuid);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        shortNotes = User.getUser(this.getApplicationContext()).getShortNotes();
        notesController = new NotesController(this.getApplicationContext());

        recyclerView = findViewById(R.id.recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new RecyclerView.Adapter() {

            @Override
            public int getItemViewType(int position) {
                return super.getItemViewType(position);
            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(MainMenu.this);
                View view = inflater.inflate(R.layout.note_fragment, parent, false);
                return new MyViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ((MyViewHolder) holder).setItem(shortNotes.get(position).getTitle(), shortNotes.get(position).getDate(), shortNotes.get(position).getUuid().toString());
            }

            @Override
            public int getItemCount() {
                return shortNotes.size();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this.getApplicationContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                choose(shortNotes.get(position).getTitle(), shortNotes.get(position).getDate(), shortNotes.get(position).getUuid().toString());
            }

            @Override public void onLongItemClick(View view, final int position) {
                AlertDialog.Builder alert = new android.app.AlertDialog.Builder(view.getContext());
                alert.setTitle("Удаление контакта");
                alert.setMessage("Вы действительно хотите удалить заметку из списка?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNote(shortNotes.get(position).getUuid().toString());
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        }));
    }

    @Override
    protected void onStart() {
        loadNotes();
        super.onStart();
    }

    public void choose(String Title, String Date, String UUID){
        Intent intent = new Intent(this.getApplicationContext(), NoteView.class);
        intent.putExtra("Title", Title);
        intent.putExtra("Date", Date);
        intent.putExtra("UUID", UUID);
        intent.putExtra("Content", "");
        intent.putExtra("Resources", "");
        startActivity(intent);
    }

    public void loadNotes(){
        shortNotes = User.getUser(this.getApplicationContext()).getShortNotes();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void deleteNote(String UUID){
        User.getUser(this.getApplicationContext()).deleteNote(UUID);
        notesController.deleteNote(UUID);
        shortNotes.clear();
        loadNotes();
    }

    public void edit(View view) {
        Intent intent = new Intent(this, Edit.class);
        intent.putExtra("Title", "");
        intent.putExtra("Date", "");
        intent.putExtra("UUID", "");
        intent.putExtra("Content", "");
        intent.putExtra("Resources", "");
        startActivity(intent);
    }
}
