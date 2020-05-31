package com.miryanova.memories.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.miryanova.memories.R;
import com.miryanova.memories.model.ShortNote;
import com.miryanova.memories.model.User;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;

public class NoteFragment extends ListFragment {
    private ArrayList<HashMap<String, String>> notes = new ArrayList<>();
    private HashMap<String, String> map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNotes();
        SimpleAdapter adapter = new SimpleAdapter(this.getContext(), notes,
                R.layout.note_fragment,
                new String[]{"Title", "Date", "UUID"},
                new int[]{R.id.title, R.id.date,
                        R.id.uuid});
        setListAdapter(adapter);
    }

    private void setNotes() {
        ArrayList<ShortNote> shortNotes = User.getUser().getShortNotes();
        for (ShortNote note : shortNotes) {
            map = new HashMap<>();
            map.put("Title", note.getTitle());
            map.put("Date", note.getDate());
            map.put("UUID", note.getUuid().toString());
            notes.add(map);
        }
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        TextView title = v.findViewById(R.id.title);
        TextView date = l.getChildAt(position).findViewById(R.id.date);
        TextView uuid = v.findViewById(R.id.uuid);
        Toast.makeText(getActivity(), title.getText().toString() + date.getText().toString() + uuid.getText().toString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this.getContext(), NoteView.class);
        intent.putExtra("Title", title.getText().toString());
        intent.putExtra("Date", date.getText().toString());
        intent.putExtra("UUID", uuid.getText().toString());
        startActivity(intent);
    }
}