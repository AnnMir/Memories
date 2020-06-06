package com.miryanova.memories.model;

import android.content.Context;

import com.miryanova.memories.controller.NotesController;

import java.util.ArrayList;
import java.util.Iterator;

public class User {
    private static User user;
    private String login;
    private String password;
    private String email;
    private static NotesController notesController;

    private User() {
    }

    public static User getUser(Context context) {
        if (user == null){
            user = new User();
            notesController = new NotesController(context);
        }
        return user;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addNote(String uuid, String title, String content, String date, String res){
        notesController.addNote(uuid,login,title,content,date,res);
    }

    public ArrayList<ShortNote> getShortNotes() {
        ArrayList<ShortNote> shortNotes = new ArrayList<>();
        ArrayList<Note> notes = notesController.selectUserNotes(login);
        for (Note note : notes) {
            shortNotes.add(note.getShortNote());
        }
        return shortNotes;
    }

    public String getContent(String uuid) {
        Note note = notesController.selectNote(uuid);
        return note.getContent();
    }

    public ArrayList<String> getResources(String uuid) {
        Note note = notesController.selectNote(uuid);
        return note.getResources();
    }

    public String getResString(String uuid) {
        Note note = notesController.selectNote(uuid);
        return note.getResString();
    }

    public void deleteNote(String uuid) {
        notesController.deleteNote(uuid);
    }

    public void changeNote(String Title, String Date, String Content, String res, String uuid) {
        notesController.updateNote(uuid,login,Title,Content,Date,res);
    }
}