package com.example.myapplication.utils;

import com.example.myapplication.data.model.Author;

public class utils {
    public static final String CURRENT_USER = "0";
    public static final String BOT_USER = "1";
    public static final byte CONTENT_TYPE_VOICE = 1;
    public static final byte CONTENT_TYPE_WELCOME = 2;

    public static Author getBotAuthor(){
        return new Author(BOT_USER, "PsyBot", "avatar");
    }

    public static Author getUserAuthor(){
        return new Author(CURRENT_USER, "Hamza",null);
    }
}
