package com.example.TP_3_CAR.akka.Acteur;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ActeurMapper extends UntypedActor {

    private final List<ActorRef> reducersList;
    private Map<String, Integer> wordCounts;
    private Set<String> wordsInCurrentLine;

    public ActeurMapper(List<ActorRef> reducersList) {
        this.reducersList = reducersList;
        this.wordCounts = new HashMap<>();
        this.wordsInCurrentLine = new HashSet<>();
    }


    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String) {
            String line = (String) message;
            String[] words = line.split("\\s+");
            for (String word : words) {
                // chaque mot vers le reducer correct
                ActorRef reducer = partition(word);
                reducer.tell(new ActeurReducer.WordCount(word, 1), getSelf());
            }
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
        int reducerIndex = (word.hashCode() & Integer.MAX_VALUE) % reducersList.size();
        return reducersList.get(reducerIndex);
    }
}
