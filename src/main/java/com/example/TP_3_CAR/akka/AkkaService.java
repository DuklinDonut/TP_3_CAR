package com.example.TP_3_CAR.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.example.TP_3_CAR.akka.Acteur.ActeurMapper;
import com.example.TP_3_CAR.akka.Acteur.ActeurReducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AkkaService {
    private final ActorSystem system;

    @Autowired
    public AkkaService(ActorSystem system) {
        this.system = system;
        setupActors();
    }

    private void setupActors() {
        // Create reducers
        ActorRef reducer1 = system.actorOf(Props.create(ActeurReducer.class), "reducer1");
        ActorRef reducer2 = system.actorOf(Props.create(ActeurReducer.class), "reducer2");

        // Create an array of reducers
        ActorRef[] reducers = {reducer1, reducer2};

        // Create the ActeurMapper actors with the reducers
        ActorRef mapper1 = system.actorOf(Props.create(ActeurMapper.class, (Object) reducers), "mapper1");
        ActorRef mapper2 = system.actorOf(Props.create(ActeurMapper.class, (Object) reducers), "mapper2");
        ActorRef mapper3 = system.actorOf(Props.create(ActeurMapper.class, (Object) reducers), "mapper3");
    }
}
