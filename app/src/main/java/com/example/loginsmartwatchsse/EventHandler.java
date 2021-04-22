package com.example.loginsmartwatchsse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface EventHandler {
    void onOpen();

    void onMessage(@NonNull MessageEvent event);

    void onError(@Nullable Exception e);
}
