package com.example.TP_3_CAR.akka.Acteur;

import akka.actor.UntypedActor;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ActeurReducer extends UntypedActor {

    private Map<String, Set<String>> processedTexts; // pour garder une trace des textes déjà traités par chaque reducer
    private Map<String, Integer> wordCounts;

    public ActeurReducer() {
        this.processedTexts = new ConcurrentHashMap<>();
        this.wordCounts = new ConcurrentHashMap<>();
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
        // Supprimer la ponctuation du mot
        word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();

        // Mettre à jour le comptage de mots
        wordCounts.compute(word, (key, existingValue) -> (existingValue == null) ? count : existingValue + count);
        System.out.println("Updated count for word '" + word + "': " + wordCounts.get(word));
    }



    private int getWordCount(String word) {
        if (wordCounts.containsKey(word)) {
            return wordCounts.get(word);
        } else {
            return 0;
        }
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
