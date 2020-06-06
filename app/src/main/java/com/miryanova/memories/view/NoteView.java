package com.miryanova.memories.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.miryanova.memories.R;
import com.miryanova.memories.model.User;

import java.io.IOException;
import java.util.ArrayList;

import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class NoteView extends AppCompatActivity {
    private TextView title;
    private TextView date;
    private TextView Uuid;
    private EditText content;
    private LinearLayout gallery;
    private LinearLayout videoGallery;
    private String uuid;
    private ArrayList<String> res;
    private MediaPlayer audioPlayer;
    private ImageButton play, pause, stop, prev, next;
    private ArrayList<String> audios;
    private int iterator;
    private AlertDialog.Builder alert;
    private final static int EDIT_RESULT = 1;
    private final static int OK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);
        title = findViewById(R.id.title);
        date = findViewById(R.id.date);
        Uuid = findViewById(R.id.uuid);
        content = findViewById(R.id.content);
        res = new ArrayList<>();
        gallery = findViewById(R.id.gallery);
        videoGallery = findViewById(R.id.video_gallery);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        stop = findViewById(R.id.stop);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        pause.setEnabled(false);
        stop.setEnabled(false);
        play.setEnabled(false);
        prev.setEnabled(false);
        next.setEnabled(false);
        String Title = getIntent().getStringExtra("Title");
        String Date = getIntent().getStringExtra("Date");
        uuid = getIntent().getStringExtra("UUID");
        initValues(Title, Date, uuid);
    }

    public void initValues(String Title, String Date, String UUID) {
        uuid = UUID;
        Uuid.setText(uuid);
        title.setText(Title);
        date.setText(Date);
        content.setText(User.getUser(this.getApplicationContext()).getContent(uuid));
        res = User.getUser(this.getApplicationContext()).getResources(uuid);
        ArrayList<String> images = getResources(uuid, "image");
        try {
            if (images.size() != 0) {
                for (String img : images) {
                    gallery.addView(insertImage(img));
                }
            }
        }catch(IllegalArgumentException e){
            Toast.makeText(this.getApplicationContext(),"Wrong uri to image. Please, check that this image exists!",Toast.LENGTH_SHORT).show();
        }
        ArrayList<String> videos = getResources(uuid, "video");
        if (videos.size() != 0) {
            for (String video : videos) {
                videoGallery.addView(insertVideo(video));
            }
        }
        audios = getResources(uuid, "audio");
        if (audios.size() != 0) {
            iterator = 0;
            try {
                audioPlayer = MediaPlayer.create(this.getApplicationContext(), Uri.parse(audios.get(iterator)));
                if (audioPlayer != null) {
                    audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            stopPlay();
                        }
                    });
                    play.setEnabled(true);
                    next.setEnabled(true);
                    prev.setEnabled(true);
                }
            }catch (IllegalArgumentException e){
                Toast.makeText(this.getApplicationContext(),"Wrong uri to audio. Please, check that this audio exists!",Toast.LENGTH_SHORT).show();
                audioPlayer = null;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_RESULT) {
            if (resultCode == OK) {
                String Title = data.getStringExtra("Title");
                String Date = data.getStringExtra("Date");
                String UUID = data.getStringExtra("UUID");
                initValues(Title, Date, UUID);
            }
        }
    }

    public ArrayList<String> getResources(String uuid, String cont) {
        ArrayList<String> resources = User.getUser(this.getApplicationContext()).getResources(uuid);
        ArrayList<String> result = new ArrayList<>();
        for (String str : resources) {
            if (str.contains(cont)) {
                result.add(str);
            }
        }
        return result;
    }

    public View insertImage(String uri) throws IllegalArgumentException{
        Bitmap bitmap = null;
        LinearLayout layout = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(uri));
            layout = new LinearLayout(getApplicationContext());
            layout.setLayoutParams(new LinearLayout.LayoutParams(350, 350));
            layout.setGravity(Gravity.CENTER);

            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(345, 345));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(bitmap);

            layout.addView(imageView);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return layout;
    }

    public View insertVideo(String uri){
        LinearLayout layout = null;
        layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(350, 350));
        layout.setGravity(Gravity.CENTER);

        VideoView videoPlay = new VideoView(getApplicationContext());
        videoPlay.setVideoURI(Uri.parse(uri));
        videoPlay.setMediaController(new MediaController(this));
        videoPlay.setOnCompletionListener(myVideoViewCompletionListener);
        videoPlay.setOnPreparedListener(MyVideoViewPreparedListener);
        videoPlay.setOnErrorListener(myVideoViewErrorListener);
        videoPlay.setLayoutParams(new LinearLayout.LayoutParams(345, 345));
        videoPlay.requestFocus();
        layout.addView(videoPlay);
        return layout;
    }

    private void stopPlay() {
        audioPlayer.stop();
        pause.setEnabled(false);
        stop.setEnabled(false);
        try {
            audioPlayer.prepare();
            audioPlayer.seekTo(0);
            play.setEnabled(true);
        } catch (Throwable t) {
            Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void play(View view) {
        audioPlayer.start();
        play.setEnabled(false);
        pause.setEnabled(true);
        stop.setEnabled(true);
    }

    public void pause(View view) {
        audioPlayer.pause();
        play.setEnabled(true);
        pause.setEnabled(false);
        stop.setEnabled(true);
    }

    public void stop(View view) {
        stopPlay();
        play.setEnabled(true);
        pause.setEnabled(false);
        stop.setEnabled(false);
    }

    public void next(View view) {
        iterator = (iterator + 1) % audios.size();
        if (audioPlayer.isPlaying()) {
            audioPlayer.stop();
            audioPlayer.release();
            audioPlayer = null;
        }
        audioPlayer = MediaPlayer.create(this.getApplicationContext(), Uri.parse(audios.get(iterator)));
        audioPlayer.start();
    }

    public void prev(View view) {
        iterator = (iterator - 1) % audios.size();
        if (audioPlayer.isPlaying()) {
            audioPlayer.stop();
            audioPlayer.release();
            audioPlayer = null;
        }
        audioPlayer = MediaPlayer.create(this.getApplicationContext(), Uri.parse(audios.get(iterator)));
        audioPlayer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (audioPlayer != null) {
            if (audioPlayer.isPlaying()) {
                stopPlay();
            }
        }
    }

    public void delete(final View view) {
        alert = new android.app.AlertDialog.Builder(view.getContext());
        alert.setTitle("Удаление заметки");
        alert.setMessage("Вы действительно хотите удалить заметку?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                User.getUser(view.getContext()).deleteNote(uuid);
                dialog.dismiss();
                finish();
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

    public void edit(View view) {
        Intent intent = new Intent(this.getApplicationContext(), Edit.class);
        intent.putExtra("Class", "NoteView");
        intent.putExtra("Title", title.getText());
        intent.putExtra("Date", date.getText());
        intent.putExtra("Content", content.getText().toString());
        intent.putExtra("Resources", User.getUser(view.getContext()).getResString(uuid));
        intent.putExtra("UUID", uuid);
        startActivityForResult(intent, EDIT_RESULT);
    }

    MediaPlayer.OnCompletionListener myVideoViewCompletionListener
            = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer arg0) {
            Toast.makeText(getApplicationContext(),
                    "End of Video",
                    Toast.LENGTH_LONG).show();
        }
    };

    MediaPlayer.OnPreparedListener MyVideoViewPreparedListener
            = new MediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer arg0) {
            Toast.makeText(getApplicationContext(),
                    "Media file is loaded and ready to go",
                    Toast.LENGTH_LONG).show();

        }
    };

    MediaPlayer.OnErrorListener myVideoViewErrorListener
            = new MediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
            Toast.makeText(getApplicationContext(),
                    "Wrong uri for video file. Please, check if video file exists",
                    Toast.LENGTH_LONG).show();
            return true;
        }
    };
}