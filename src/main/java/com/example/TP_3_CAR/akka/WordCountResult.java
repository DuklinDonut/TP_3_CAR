package com.example.TP_3_CAR.akka;

import java.io.Serializable;

public class WordCountResult implements Serializable {
    private String word;
    private int count;

    public WordCountResult(String word, int count) {
        this.word = word;
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public int getCount() {
        return count;
    }
}
