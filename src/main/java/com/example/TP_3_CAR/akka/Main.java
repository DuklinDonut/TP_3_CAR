package com.example.TP_3_CAR.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.example.TP_3_CAR.akka.Acteur.ActeurMapper;
import com.example.TP_3_CAR.akka.Acteur.ActeurReducer;
import com.example.TP_3_CAR.akka.Acteur.WordCountRequest;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Create actor system
        ActorSystem system = ActorSystem.create("MySystem");

        // Create reducers
        ActorRef reducer1 = system.actorOf(Props.create(ActeurReducer.class), "reducer1");
        ActorRef reducer2 = system.actorOf(Props.create(ActeurReducer.class), "reducer2");

        // Create an array of reducers
        ActorRef[] reducers = {reducer1, reducer2};

        // Create the ActeurMapper actors with the reducers
        ActorRef mapper1 = system.actorOf(Props.create(ActeurMapper.class, reducer1), "mapper1");
        ActorRef mapper2 = system.actorOf(Props.create(ActeurMapper.class, reducer2), "mapper2");
        ActorRef mapper3 = system.actorOf(Props.create(ActeurMapper.class, reducer2), "mapper3");



        // Create scanner object to read input from the user
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter a word
        System.out.println("Enter a word to count its occurrences in the file (or type 'exit' to quit): ");
        String word = scanner.nextLine();

        // Hardcoded file path
        String filePath = "C:\\Users\\FATIMA EZZAHRA MAJID\\OneDrive\\Documents\\M1-Lille\\TP_3_CAR\\src\\main\\java\\com\\example\\TP_3_CAR\\Words.txt";

        // Send the word and file path to the mapper actors for processing
        WordCountRequest request = new WordCountRequest(word, filePath, reducers);
        mapper1.tell(new WordCountRequest(word, filePath, reducers), ActorRef.noSender());
        mapper2.tell(new WordCountRequest(word, filePath, reducers), ActorRef.noSender());
        mapper3.tell(new WordCountRequest(word, filePath, reducers), ActorRef.noSender());


        // Close the scanner and terminate the actor system
        scanner.close();
        system.terminate();
    }
}
