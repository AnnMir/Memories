package com.miryanova.memories.model;

import java.util.UUID;

public class ShortNote {
    private String title;
    private String date;
    private UUID uuid;

    ShortNote(String Title, String Date, UUID Uuid) {
        title = Title;
        date = Date;
        uuid = Uuid;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public UUID getUuid() {
        return uuid;
    }
}
