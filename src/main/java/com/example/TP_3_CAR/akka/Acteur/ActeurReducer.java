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
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            String word = (String) message;
            // Update the count of the word
            int count = wordCounts.getOrDefault(word, 0);
            wordCounts.put(word, count + 1);
        } else {
            unhandled(message);
        }
    }

    @Override
    public void postStop() throws Exception {
        // Print the final word counts when the actor stops
        System.out.println("Word Counts: " + wordCounts);
    }
}
