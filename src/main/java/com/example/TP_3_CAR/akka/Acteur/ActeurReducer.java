package com.example.TP_3_CAR.akka.Acteur;

import akka.actor.UntypedActor;

import java.util.HashMap;
import java.util.Map;

public class ActeurReducer extends UntypedActor {

    private Map<String, Integer> wordCounts;

    public ActeurReducer() {
        this.wordCounts = new HashMap<>();
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof WordCount) {
            WordCount wordCount = (WordCount) message;
            updateWordCount(wordCount.word, wordCount.count);
        } else if (message instanceof getWord) {
            getWord getWordMessage = (getWord) message;
            String word = getWordMessage.word;
            int count = getWordCount(word);
            getSender().tell(new WordCount(word, count), getSelf());
        } else {
            unhandled(message);
        }
    }

    private void updateWordCount(String word, int count) {
        wordCounts.put(word, wordCounts.getOrDefault(word, 0) + count);
    }

    private int getWordCount(String word) {
        return wordCounts.getOrDefault(word, 0);
    }

    public static class WordCount {
        public final String word;
        public final int count;

        public WordCount(String word, int count) {
            this.word = word;
            this.count = count;
        }
    }

    public static class getWord {
        public final String word;

        public getWord(String word) {
            this.word = word;
        }
    }
}
