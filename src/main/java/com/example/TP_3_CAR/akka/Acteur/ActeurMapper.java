package com.example.TP_3_CAR.akka.Acteur;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import java.util.Scanner;

public class ActeurMapper extends UntypedActor {
    private ActorRef[] reducers; // Lignes de l'acteru reducer

    public ActeurMapper(ActorRef[] reducers) {
        this.reducers = reducers;
    }
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            String line = (String) message;
            String[] words = line.split("\\s+");

            // For each word, choose the appropriate Reducer and send the word to it
            for (String word : words) {
                ActorRef reducer = partitionReducer(reducers, word);
                reducer.tell(word, getSelf());
            }
        }
    }

    private ActorRef partitionReducer(ActorRef[] reducers, String word) {
        int reducerIndex = Math.abs(word.hashCode()) % reducers.length;
        return reducers[reducerIndex];
    }

    private Scanner receiveBuilder() {
    }


}
