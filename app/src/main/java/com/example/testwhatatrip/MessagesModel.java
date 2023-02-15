package com.example.testwhatatrip;

public class MessagesModel {
    String message;
    String Uid;
    long timeStamp;

    public MessagesModel() {
    }

    public MessagesModel(String message, String uid, long timeStamp) {
        this.message = message;
        Uid = uid;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
