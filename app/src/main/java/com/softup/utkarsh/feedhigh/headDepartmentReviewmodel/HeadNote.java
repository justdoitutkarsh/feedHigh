package com.softup.utkarsh.feedhigh.headDepartmentReviewmodel;

public class HeadNote {

    private String firebaseKey;
    private String title;
    private String body;
    private String body2;
    private String body3;
    private String body4;
    private String body5;
    private int priority;

    public HeadNote() {
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
    public String getBody2() {
        return body2;
    }
    public String getBody3() {
        return body3;
    }
    public String getBody4() {
        return body4;
    }
    public String getBody5() {
        return body5;
    }

    public int getPriority() {
        return priority;
    }

    public HeadNote(String firebaseKey, String title, String body, String body2, String body3, String body4, String body5, int priority) {
        this.firebaseKey = firebaseKey;
        this.title = title;
        this.body = body;
        this.body2 = body2;
        this.body3 = body3;
        this.body4 = body4;
        this.body5 = body5;
        this.priority = priority;
    }
}
