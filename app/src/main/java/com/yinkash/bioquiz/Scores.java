package com.yinkash.bioquiz;

public class Scores {

    private String uname1;
    private int savedScore;

    public Scores() {
    }

    public Scores(String uname1, int savedScore) {
        this.uname1 = uname1;
        this.savedScore = savedScore;
    }

    public void setUname1(String uname1) {
        this.uname1 = uname1;
    }

    public String getUname1() {
        return this.uname1;
    }

    public void setSavedScore(int savedScore) {
        this.savedScore = savedScore;
    }

    public int getSavedScore() {
        return this.savedScore;
    }
}
