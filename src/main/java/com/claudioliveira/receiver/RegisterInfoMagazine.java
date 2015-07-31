package com.claudioliveira.receiver;

import com.claudioliveira.domain.DomainCollection;
import com.claudioliveira.domain.DomainEvent;
import com.claudioliveira.domain.PlainBarcode;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;

/**
 * Register additional info on magazine, after the fill some information.
 *
 * @author Claudio E. de Oliveira (claudioed.oliveira@gmail.com).
 */
public class RegisterInfoMagazine extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterInfoMagazine.class);

    @Override
    public void start() throws Exception {
        final MongoClient mongoClient = MongoClient.createShared(vertx,
                new JsonObject().put("magazine-manager", "magazine-manager"), "magazine-manager");
        EventBus eb = vertx.eventBus();
        eb.consumer(DomainEvent.UPDATE_MAGAZINE_ON_DELIVERY.event(), message -> {
            JsonObject jsonMessage = new JsonObject(message.body().toString());
            PlainBarcode plainBarcode = new PlainBarcode(jsonMessage.getString("barcode"));
            JsonObject query = new JsonObject().put("delivery", jsonMessage.getString("deliveryId")).put("plainBarcode", plainBarcode.plainBarcode());
            JsonObject update = new JsonObject().put("$set", new JsonObject()
                    .put("price", jsonMessage.getDouble("price"))
                    .put("name", jsonMessage.getString("name"))
                    .put("withPrice", Boolean.TRUE));
            mongoClient.update(DomainCollection.MAGAZINES.collection(), query, update, handler -> {
                if (handler.succeeded()) {
                    LOGGER.info("Magazine updated with success!!!");
                } else {
                    LOGGER.error("Error on update magazine");
                }
            });
        });
    }

}
