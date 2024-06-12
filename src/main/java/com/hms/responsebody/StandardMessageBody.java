package com.hms.responsebody;

import lombok.Getter;

import java.util.HashMap;

@Getter // Generates Getters for all non-transient fields
public class StandardMessageBody {
    private final HashMap<String, String> content;

    public StandardMessageBody(String errorMessage) {
        HashMap<String, String> content = new HashMap<>();
        content.put("message", errorMessage);
        this.content = content;
    }
}
