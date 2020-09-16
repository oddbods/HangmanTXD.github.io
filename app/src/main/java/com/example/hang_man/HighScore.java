package com.example.hang_man;

public class HighScore {
    private int id;
    private int score;
    private String name;

    public HighScore(int score, String name) {
        this.id = id;
        this.score = score;
        this.name = name;
    }

    public int getScore() { return score; }

    public String getName() { return name; }

    public void setScore(int score) { this.score = score; }

    public void setName(String name) { this.name = name; }
}
