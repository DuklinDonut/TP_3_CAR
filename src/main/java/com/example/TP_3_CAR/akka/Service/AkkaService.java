package com.example.TP_3_CAR.akka.Service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.AskTimeoutException;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.example.TP_3_CAR.akka.Acteur.ActeurMapper;
import com.example.TP_3_CAR.akka.Acteur.ActeurReducer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import scala.compat.java8.FutureConverters;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

@Service
public class AkkaService {

    private ActorSystem actorSystem;
    private ActorRef[] mappers;
    private ActorRef[] reducers;

    private List<ActorRef> reducersList;

    public void initializeActors() {
        actorSystem = ActorSystem.create("MapReduceSystem");
        mappers = new ActorRef[3];
        reducers = new ActorRef[2];

        for (int i = 0; i < 2; i++) {
            String reducerName = "reducer" + i;
            reducers[i] = actorSystem.actorOf(Props.create(ActeurReducer.class), reducerName);
            System.out.println(reducerName);
        }

        reducersList = Arrays.asList(reducers);

        for (int i = 0; i < mappers.length; i++) {
            String mapperName = "mapper" + System.currentTimeMillis() + "_" + i;
            mappers[i] = actorSystem.actorOf(Props.create(ActeurMapper.class, reducersList), mapperName);
            System.out.println(mapperName);
        }
    }

    public void distributeLines(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Distributing line: " + line);
                for (ActorRef mapper : mappers) {
                    mapper.tell(line, ActorRef.noSender());
                }
            }
        } catch (Exception e) {
            System.out.println("Error distributing file lines" + e);
        }
    }

    public int searchWordOccurrences(String word) {
        int totalOccurrences = 0;
        Timeout timeout = new Timeout(Duration.create(5, "seconds"));

        // Utilisez un ensemble pour stocker les réductions déjà interrogées
        Set<ActorRef> queriedReducers = new HashSet<>();

        for (ActorRef reducer : reducersList) {
            if (!queriedReducers.contains(reducer)) {
                try {
                    Future<Object> future = Patterns.ask(reducer, new ActeurReducer.getWord(word), timeout);
                    CompletionStage<Object> javaFuture = FutureConverters.toJava(future);

                    CompletionStage<Integer> processedFuture = javaFuture.thenApply(response -> {
                        if (response instanceof ActeurReducer.WordCount) {
                            return ((ActeurReducer.WordCount) response).count;
                        } else {
                            return 0;
                        }
                    });

                    Integer result = processedFuture.toCompletableFuture().get(5, TimeUnit.SECONDS);
                    totalOccurrences += result;

                    // Marquez ce réducteur comme interrogé
                    queriedReducers.add(reducer);
                } catch (AskTimeoutException e) {
                    System.err.println("Timeout occurred while waiting for response from reducer: " + e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Total occurrences: " + totalOccurrences);
        return totalOccurrences;
    }
}
