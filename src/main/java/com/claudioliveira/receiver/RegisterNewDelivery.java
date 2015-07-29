package com.claudioliveira.receiver;

import com.claudioliveira.infra.DateTimeMongoFormat;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author Claudio E. de Oliveira (claudioed.oliveira@gmail.com).
 */
public class RegisterNewDelivery extends AbstractVerticle {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public void start() throws Exception {
        final MongoClient mongoClient = MongoClient.createShared(vertx,
                new JsonObject().put("magazine-manager", "magazine-manager"), "magazine-manager");
        EventBus eb = vertx.eventBus();
        eb.consumer("new-delivery", message ->
                mongoClient.insert("deliveries", new JsonObject(message
                        .body().toString()).put("code", UUID.randomUUID().toString()).put("creationAt", new JsonObject().put("$date", DateTimeMongoFormat.format(LocalDateTime.now()))), result -> {
                }));
    }

}