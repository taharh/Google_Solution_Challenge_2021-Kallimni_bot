package com.example.myapplication.data.model;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Date;
import java.util.UUID;

public class Message implements IMessage,
        MessageContentType.Image, /*this is for default image messages implementation*/
        MessageContentType /*and this one is for custom content type (in this case - voice message)*/ {

    private String id;
    private String text;
    private Date createdAt;
    private Author user;
    private Image image;
    private String voicePath;
    private boolean isWelcomeMessage;

    public Message(Author user, String text) {
        this(UUID.randomUUID().toString(), user, text, new Date(), null, false);
    }

    public Message(Author user, String text, boolean isWelcomeMessage) {
        this(UUID.randomUUID().toString(), user, text, new Date(), null, isWelcomeMessage);
    }

    public Message(Author user, Image image) {
        this(UUID.randomUUID().toString(), user, "test", new Date(), image, false);
    }


    public Message(String voicePath, Author user) {
        this(user, "");
        this.voicePath = voicePath;
    }




    public Message(String id, Author user, String text, Date createdAt, Image image, boolean isWelcomeMessage) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.createdAt = createdAt;
        this.image = image;
        this.isWelcomeMessage = isWelcomeMessage;
    }


    public boolean isWelcomeMessage() {
        return isWelcomeMessage;
    }

    public void setWelcomeMessage(boolean welcomeMessage) {
        isWelcomeMessage = welcomeMessage;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public Author getUser() {
        return this.user;
    }

    @Override
    public String getImageUrl() {
        return image == null ? null : image.url;
    }

    public String getVoicePath() {
        return voicePath;
    }


    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    public static class Image {

        private String url;

        public Image(String url) {
            this.url = url;
        }
    }

}
