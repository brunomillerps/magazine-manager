
package com.claudioliveira.receiver;

import com.claudioliveira.domain.DomainCollection;
import com.claudioliveira.domain.DomainEvent;
import com.claudioliveira.infra.DateTimeMongoFormat;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * This handler is responsible to register deliveries in database.
 *
 * @author Claudio E. de Oliveira (claudioed.oliveira@gmail.com).
 */
public class RegisterNewDelivery extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        final MongoClient mongoClient = MongoClient.createShared(vertx,
                new JsonObject().put("magazine-manager", "magazine-manager"), "magazine-manager");
        EventBus eb = vertx.eventBus();
        eb.consumer(
                DomainEvent.NEW_DELIVERY.event(),
                message -> {
                    String code = UUID.randomUUID().toString();
                    JsonObject jsonDelivery = new JsonObject(message
                            .body().toString()).put("code", code).put(
                            "creationAt",
                            new JsonObject().put("$date",
                                    DateTimeMongoFormat.format(LocalDateTime.now())));
                    mongoClient.insert(
                            DomainCollection.DELIVERIES.collection(),jsonDelivery,
                            result -> {
                                if (result.succeeded()) {
                                    eb.publish(DomainEvent.SUCCESS_DELIVERY.event(),
                                            new JsonObject().put("deliveryId", result.result()));
                                }
                            });
                });
    }

}
