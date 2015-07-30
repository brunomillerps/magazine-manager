package com.claudioliveira.receiver;

import com.claudioliveira.domain.DomainEvent;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;

/**
 * @author Claudio E. de Oliveira (claudioed.oliveira@gmail.com).
 */
public class RegisterMagazinesByDelivery extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterMagazinesByDelivery.class);

    public void start() throws Exception {
        final MongoClient mongoClient = MongoClient.createShared(vertx,
                new JsonObject().put("magazine-manager", "magazine-manager"), "magazine-manager");
        EventBus eb = vertx.eventBus();
        eb.consumer(DomainEvent.SUCCESS_DELIVERY.event(), message -> mongoClient.findOne("deliveries", new JsonObject().put("_id", message.body().toString()), new JsonObject(), handler -> {
            if (handler.succeeded()) {
                JsonArray elements = handler.result().getJsonArray("elements");
                elements.forEach(magazine -> mongoClient.insert("magazines", new JsonObject(magazine.toString()).put("available", Boolean.TRUE).put("delivery", message.body().toString()), result -> {
                    if (result.failed()) {
                        LOGGER.error("Error on save magazines by delivery!!!");
                    }
                }));
            } else {
                LOGGER.error("Error on find delivery!!!");
            }
        }));
    }

}
