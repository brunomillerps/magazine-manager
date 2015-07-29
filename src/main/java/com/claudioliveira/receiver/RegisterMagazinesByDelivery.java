package com.claudioliveira.receiver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * @author Claudio E. de Oliveira (claudioed.oliveira@gmail.com).
 */
public class RegisterMagazinesByDelivery extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        final MongoClient mongoClient = MongoClient.createShared(vertx,
                new JsonObject().put("magazine-manager", "magazine-manager"), "magazine-manager");
        EventBus eb = vertx.eventBus();
        eb.consumer("new-delivery-success", message -> {
            JsonObject entry = new JsonObject(message.body().toString());
            JsonArray elements = entry.getJsonArray("elements");
            elements.forEach(magazine -> mongoClient.insert("magazines", new JsonObject(magazine.toString()).put("available",Boolean.TRUE), result -> {
                if (result.failed()) {
                    return;
                }
            }));
        });
    }

}
