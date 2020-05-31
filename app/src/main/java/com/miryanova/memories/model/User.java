package com.miryanova.memories.model;

import java.util.ArrayList;
import java.util.Iterator;

public class User {
    private static User user;
    private String login;
    private String password;
    private String email;
    private ArrayList<Note> notes;

    private User() {
    }

    public static User getUser() {
        if (user == null)
            user = new User();
        return user;
    }

    public String getLogin() {
        return login;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void addNote(Note note) {
        if (notes == null)
            notes = new ArrayList<>();
        this.notes.add(note);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public ArrayList<ShortNote> getShortNotes() {
        ArrayList<ShortNote> shortNotes = new ArrayList<>();
        for (Note note : notes) {
            shortNotes.add(note.getShortNote());
        }
        return shortNotes;
    }

    public String getContent(String uuid) {
        for (Note note : notes) {
            if (note.getUuid().toString().equals(uuid)) {
                return note.getContent();
            }
        }
        return "";
    }

    public ArrayList<String> getResources(String uuid) {
        for (Note note : notes) {
            if (note.getUuid().toString().equals(uuid)) {
                return note.getResources();
            }
        }
        return null;
    }

    public String getResString(String uuid) {
        for (Note note : notes) {
            if (note.getUuid().toString().equals(uuid)) {
                return note.getResString();
            }
        }
        return null;
    }

    public void deleteNote(String uuid) {
        Iterator<Note> noteIterator = notes.iterator();
        while(noteIterator.hasNext()){
            Note note = noteIterator.next();
            if(note.getUuid().toString().equals(uuid))
                noteIterator.remove();
        }
    }

    public void changeNote(String Title, String Date, String Content, String res, String uuid) {
        Iterator<Note> noteIterator = notes.iterator();
        while(noteIterator.hasNext()){
            Note note = noteIterator.next();
            if(note.getUuid().toString().equals(uuid)){
                noteIterator.remove();
            }
        }
        notes.add(new Note(Title, Content, Date, res, uuid));
    }
}