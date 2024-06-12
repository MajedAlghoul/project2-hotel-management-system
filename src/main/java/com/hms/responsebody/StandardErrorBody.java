package com.hms.responsebody;

import lombok.Getter;

import java.util.HashMap;

@Getter // Generates Getters for all non-transient fields
public class StandardErrorBody {
    private final HashMap<String, String> content;

    public StandardErrorBody(String errorMessage, String timestamp) {
        HashMap<String, String> content = new HashMap<>();
        content.put("timestamp", timestamp);
        content.put("error", errorMessage);
        this.content = content;
    }
}
