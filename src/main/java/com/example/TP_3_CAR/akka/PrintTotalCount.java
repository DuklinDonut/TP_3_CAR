package com.example.TP_3_CAR.akka;

import java.io.Serializable;

public class PrintTotalCount implements Serializable {
    private String word;

    public PrintTotalCount(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
