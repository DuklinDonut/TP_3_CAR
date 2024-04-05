package com.example.TP_3_CAR.akka.Acteur;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActeurMapper extends UntypedActor {

    private final List<ActorRef> reducersList;
    private Map<String, Integer> wordCounts;

    public ActeurMapper(List<ActorRef> reducersList) {
        this.reducersList = reducersList;
        this.wordCounts = new HashMap<>();
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String) {
            String line = (String) message;
            String[] words = line.split("\\s+");
            for (String word : words) {
                ActorRef reducer = partition(word);
                if (!wordCounts.containsKey(word)) {
                    wordCounts.put(word, 1);
                } else {
                    wordCounts.put(word, wordCounts.get(word) + 1);
                }
            }
            sendWordCountsToReducers();
        } else {
            unhandled(message);
        }
    }

    private void sendWordCountsToReducers() {
        for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            String word = entry.getKey();
            int count = entry.getValue();
            ActorRef reducer = partition(word);
            reducer.tell(new ActeurReducer.WordCount(word, count), getSelf());
        }
        wordCounts.clear();
    }

    private ActorRef partition(String word) {
        int reducerIndex = Math.abs(word.hashCode()) % reducersList.size();
        return reducersList.get(reducerIndex);
    }
}
