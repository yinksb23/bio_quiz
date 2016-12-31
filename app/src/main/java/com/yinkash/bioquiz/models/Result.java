package com.yinkash.bioquiz.models;

public class Result {

    private int id;
    private int userId;
    private int score;

    public Result() {
    }

    public Result(int userId, int score) {
        this.userId = userId;
        this.score = score;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }
}
