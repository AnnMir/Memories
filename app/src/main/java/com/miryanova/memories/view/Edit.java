package com.miryanova.memories.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.miryanova.memories.R;
import com.miryanova.memories.controller.NotesController;
import com.miryanova.memories.model.Note;
import com.miryanova.memories.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class Edit extends AppCompatActivity {
    private EditText title;
    private EditText content;
    private TextView date;
    private String day;
    private String uuid;
    private EditText resources;
    private StringBuilder res;
    Calendar dateTime = Calendar.getInstance();
    static final int RESOURCES_REQUEST = 1;
    private final static String LOG_TAG = "Edit";
    private final static int EDIT_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        date = findViewById(R.id.date);
        resources = findViewById(R.id.resources);
        res = new StringBuilder();
        String Title = getIntent().getStringExtra("Title");
        String Date = getIntent().getStringExtra("Date");
        if(Date.equals(""))
            setInitialDateTime();
        else {
            date.setText(Date);
            day = Date;
        }
        String Content = getIntent().getStringExtra("Content");
        uuid = getIntent().getStringExtra("UUID");
        res.append(getIntent().getStringExtra("Resources"));
        resources.setText(res.toString());
        title.setText(Title);
        content.setText(Content);
    }

    // установка начальных даты и времени
    private void setInitialDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        day = sdf.format(Calendar.getInstance().getTime());
        date.setText(day);
    }

    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int m = month+1;
                day = dayOfMonth + "-" + m + "-" + year;
                date.setText(day);
            }
        };
        new DatePickerDialog(Edit.this, listener,
                dateTime.get(Calendar.YEAR),
                dateTime.get(Calendar.MONTH),
                dateTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void save(View view) {
        String Title = title.getText().toString();
        String Content = content.getText().toString();
        String Resources = resources.getText().toString();
        if (uuid.equals("")) {
            if(!Title.equals("") || !Content.equals("") || !Resources.equals("")) {
                Note note = new Note(Title, Content, day, Resources);
                User.getUser(this.getApplicationContext()).addNote(note.getUuid().toString(), note.getTitle(), note.getContent(), note.getDate(), note.getResString());
                Toast.makeText(view.getContext(), Title + " " + Content + " " + Resources + " " + day + " added", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(view.getContext(),"Blank note won't be saved", Toast.LENGTH_LONG).show();
            }
        } else {
            User.getUser(this.getApplicationContext()).changeNote(Title, day, Content, Resources, uuid);
            Log.i("Notesd","intent");
            Intent intent = new Intent();
            intent.putExtra("Title",Title);
            intent.putExtra("Date",day);
            intent.putExtra("UUID",uuid);
            setResult(EDIT_RESULT, intent);
            Toast.makeText(view.getContext(), Title + " " + Content + " " + Resources + " " + day + " changed", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    public void cancel(View view) {
        finish();
    }

    public void addPicture(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        res = new StringBuilder();
        res.append(resources.getText().toString());
        startActivityForResult(photoPickerIntent, RESOURCES_REQUEST);
    }

    public void addVideo(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        res = new StringBuilder();
        res.append(resources.getText().toString());
        startActivityForResult(intent, RESOURCES_REQUEST);
    }

    public void addMusic(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        res = new StringBuilder();
        res.append(resources.getText().toString());
        startActivityForResult(intent, RESOURCES_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);
        if (resultCode == RESULT_OK) {
            Uri selectedRes = returnedIntent.getData();
            if (selectedRes != null) {
                res.append(selectedRes.toString());
                res.append("?");
                resources.setText(res.toString());
                //Log.i(LOG_TAG,res.toString());
                Toast.makeText(this.getApplicationContext(), resources.getText().toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.getApplicationContext(), "Nothing was selected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}