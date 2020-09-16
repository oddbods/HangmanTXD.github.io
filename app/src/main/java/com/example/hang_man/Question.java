package com.example.hang_man;

public class Question {
    private int id;
    private String hint;
    private String word;

    public Question() {
    }

    public Question(int id, String hint, String word) {
        this.id = id;
        this.hint = hint;
        this.word = word;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
