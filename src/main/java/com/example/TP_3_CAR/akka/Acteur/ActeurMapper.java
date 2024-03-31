package com.example.TP_3_CAR.akka.Acteur;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import java.io.BufferedReader;
import java.io.FileReader;

public class ActeurMapper extends UntypedActor {
    private ActorRef reducer;

    public ActeurMapper(ActorRef reducer) {
        this.reducer = reducer;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof WordCountRequest) {
            WordCountRequest wordCountRequest = (WordCountRequest) message;
            processFile(wordCountRequest.getFilePath(), wordCountRequest.getWord(), wordCountRequest.getReducers());
        } else {
            unhandled(message);
        }
    }

    private void processFile(String filePath, String word, ActorRef[] reducers) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] words = line.split("\\s+");
            for (String fileWord : words) {
                if (fileWord.equals(word)) {
                    ActorRef reducer = partitionReducer(reducers, word);
                    reducer.tell(word, getSelf());
                }
            }
        }
        reader.close();
    }

    private ActorRef partitionReducer(ActorRef[] reducers, String word) {
        int reducerIndex = Math.abs(word.hashCode()) % reducers.length;
        return reducers[reducerIndex];
    }
}
