package com.miryanova.memories.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

public class Note {
    private final UUID uuid;
    private String title;
    private String content;
    private String date;
    private ArrayList<String> resources;
    private String resString;
    private ShortNote shortNote;

    public Note(String uuid){
        Log.i("Notesd",uuid);
        this.uuid = UUID.fromString(uuid);
        title="";
        date="";
        content="";
        resources = new ArrayList<>();
        resString = "";
        shortNote = new ShortNote(title,date,this.uuid);
    }

    public Note(String Title, String Content, String Date, String Resources) {
        title = Title;
        content = Content;
        date = Date;
        this.uuid = UUID.randomUUID();
        resString = Resources;
        resources = new ArrayList<>();
        shortNote = new ShortNote(title, date, uuid);
        parseResources(Resources);
    }

    public Note(String Title, String Content, String Date, String Resources, String Uuid) {
        title = Title;
        content = Content;
        date = Date;
        uuid = UUID.fromString(Uuid);
        resString = Resources;
        resources = new ArrayList<>();
        shortNote = new ShortNote(title, date, uuid);
        parseResources(Resources);
    }

    /**
     * Функция парсинга строки ресурсов
     *
     * @param res строка в виде путей к файлам ресурсов, разделенных символом "?"
     */
    private void parseResources(String res) {
        String[] result = res.split("\\?");
        for (String str : result) {
            if (str != null)
                resources.add(str);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<String> getResources() {
        return resources;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setResources(ArrayList<String> resources) {
        this.resources = resources;
    }

    ShortNote getShortNote() {
        return shortNote;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getResString() {
        return resString;
    }
}