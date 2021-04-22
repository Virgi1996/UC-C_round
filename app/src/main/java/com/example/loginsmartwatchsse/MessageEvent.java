package com.example.loginsmartwatchsse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MessageEvent {

    @Nullable
    private String event;

    @Nullable
    private String id;

    @NonNull
    private String data;

    public MessageEvent(@Nullable String event, @Nullable String id, @NonNull String data) {
        this.event = event;
        this.id = id;
        this.data = data;
    }

    @Nullable
    public String getEvent() {
        return event;
    }

    @Nullable
    public String getId() {
        return id;
    }

    @NonNull
    public String getData() {
        return data;
    }
}

