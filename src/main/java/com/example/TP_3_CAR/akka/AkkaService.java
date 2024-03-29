package com.example.TP_3_CAR.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.dispatch.Mapper;
import com.example.TP_3_CAR.akka.Acteur.ActeurMapper;
import com.example.TP_3_CAR.akka.Acteur.ActeurReducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.springframework.stereotype.Service;

@Service
public class AkkaService {
    private final ActorSystem system = ActorSystem.create("WordCountSystem");

    public AkkaService() {
        ActorRef mapper1 = system.actorOf(Props.create(ActeurMapper.class), "mapper1");
        ActorRef mapper2 = system.actorOf(Props.create(ActeurMapper.class), "mapper2");
        ActorRef mapper3 = system.actorOf(Props.create(ActeurMapper.class), "mapper3");
        ActorRef reducer1 = system.actorOf(Props.create(ActeurReducer.class), "reducer1");
        ActorRef reducer2 = system.actorOf(Props.create(ActeurReducer.class), "reducer2");
    }


}
