package com.example.myapplication.data.model;

public class SentimentDict {
    private String sentiment;
    private float min;
    private float max;
    private String message;
    private String imgUrl;

    public SentimentDict(String sentiment, float min, float max, String message, String imgUrl) {
        this.sentiment = sentiment;
        this.min = min;
        this.max = max;
        this.message = message;
        this.imgUrl = imgUrl;
    }

    public String getSentiment() {
        return sentiment;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public String getMessage() {
        return message;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    @Override
    public String toString() {
        return "SentimentDict{" +
                "sentiment='" + sentiment + '\'' +
                ", min=" + min +
                ", max=" + max +
                ", message='" + message + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
