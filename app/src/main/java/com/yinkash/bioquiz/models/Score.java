package com.yinkash.bioquiz.models;

public class Score {

    private String userName;
    private int savedScore;

    public Score() {
    }

    public Score(String userName, int savedScore) {
        this.userName = userName;
        this.savedScore = savedScore;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setSavedScore(int savedScore) {
        this.savedScore = savedScore;
    }

    public int getSavedScore() {
        return this.savedScore;
    }
}
