
package com.claudioliveira.receiver;

import com.claudioliveira.domain.DomainCollection;
import com.claudioliveira.domain.DomainEvent;
import com.claudioliveira.infra.DateTimeMongoFormat;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.time.LocalDateTime;

/**
 * @author Claudio Eduardo de Oliveira
 *         (claudioed.oliveira@gmail.com).
 */
public class RegisterSales extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        final MongoClient mongoClient = MongoClient.createShared(vertx,
                new JsonObject().put("magazine-manager", "magazine-manager"), "magazine-manager");
        EventBus eb = vertx.eventBus();
        eb.consumer(DomainEvent.NEW_SALE.event(), message -> mongoClient.insert(DomainCollection.SALES.collection(), new JsonObject(message
                .body().toString()).put("creationAt", new JsonObject().put("$date", DateTimeMongoFormat.format(LocalDateTime.now()))), result -> {
        }));
    }

}
