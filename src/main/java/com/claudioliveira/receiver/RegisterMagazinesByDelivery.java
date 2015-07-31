package com.claudioliveira.receiver;

import com.claudioliveira.domain.DomainEvent;
import com.claudioliveira.infra.DateTimeMongoFormat;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;

import java.time.LocalDateTime;

/**
 * Event handler when the delivery has saved in database, is necessary to publish
 * a event to find price of magazines on history and notify the customers.
 *
 * @author Claudio E. de Oliveira (claudioed.oliveira@gmail.com).
 */
public class RegisterMagazinesByDelivery extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterMagazinesByDelivery.class);

    public void start() throws Exception {
        final MongoClient mongoClient = MongoClient.createShared(vertx,
                new JsonObject().put("magazine-manager", "magazine-manager"), "magazine-manager");
        EventBus eb = vertx.eventBus();
        eb.consumer(DomainEvent.SUCCESS_DELIVERY.event(), message -> {
            mongoClient.findOne("deliveries", new JsonObject().put("_id", new JsonObject(message.body().toString()).getString("deliveryId")), new JsonObject(), handler -> {
                if (handler.succeeded()) {
                    JsonArray elements = handler.result().getJsonArray("elements");
                    elements.forEach(magazine ->
                    {
                        JsonObject jsonElement = new JsonObject(magazine.toString());
                        PlainBarcode plainBarcode = new PlainBarcode(jsonElement.getString("barcode"));
                        JsonObject jsonObject = new JsonObject().put("available", Boolean.TRUE)
                                .put("delivery", new JsonObject(message.body().toString()).getString("deliveryId"))
                                .put("withPrice", Boolean.FALSE)
                                .put("plainBarcode", plainBarcode.plainBarcode())
                                .put("edition", plainBarcode.edition())
                                .put("barcode", plainBarcode.barcode());
                        mongoClient.insert("magazines", jsonObject, result -> {
                            if (result.failed()) {
                                LOGGER.error("Error on save magazines by delivery!!!");
                            } else {
                                eb.publish(DomainEvent.FILL_MAGAZINE_PRICE_IN_HISTORY.event(),
                                        new JsonObject().put("magazine", new JsonObject(magazine.toString())).put("deliveryTimestamp", new JsonObject().put("$date", DateTimeMongoFormat.format(LocalDateTime.now()))));
                                eb.publish(DomainEvent.NOTIFY_CUSTOMER_ARRIVE_NEW_MAGAZINE.event(),
                                        new JsonObject().put("magazine", new JsonObject(magazine.toString())).put("deliveryTimestamp", new JsonObject().put("$date", DateTimeMongoFormat.format(LocalDateTime.now()))));
                            }
                        });
                    });
                } else {
                    LOGGER.error("Error on find delivery!!!");
                }
            });
        });
    }

    private class PlainBarcode {

        private final String plainBarcode;

        public PlainBarcode(String plainBarcode) {
            this.plainBarcode = plainBarcode;
        }

        public String barcode() {
            return this.plainBarcode.substring(0, 13);
        }

        public String plainBarcode() {
            return this.plainBarcode;
        }

        public String edition() {
            return this.plainBarcode.substring(13, this.plainBarcode.length());

        }

    }


}
