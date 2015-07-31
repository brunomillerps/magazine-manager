package com.claudioliveira.receiver;

import com.claudioliveira.domain.DomainCollection;
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
 * Event handler when the add a favorite magazine for customer.
 *
 * @author Claudio E. de Oliveira (claudioed.oliveira@gmail.com).
 */
public class FavoriteMagazine extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(FavoriteMagazine.class);

    public void start() throws Exception {
        final MongoClient mongoClient = MongoClient.createShared(vertx,
                new JsonObject().put("magazine-manager", "magazine-manager"), "magazine-manager");
        EventBus eb = vertx.eventBus();
        eb.consumer(DomainEvent.NEW_FAVORITE_MAGAZINE.event(), message -> {
            mongoClient.findOne(DomainCollection.CUSTOMERS.collection(), new JsonObject().put("_id", new JsonObject(message.body().toString()).getString("customerId")), new JsonObject(), handler -> {
                if (handler.succeeded()) {
                    JsonObject customer = handler.result();
                    mongoClient.findOne("magazines", new JsonObject().put("barcode", new JsonObject(message.body().toString()).getString("barcode")), new JsonObject(), handlerMagazine -> {
                        if (handlerMagazine.succeeded()) {
                            JsonArray favorite = customer.getJsonArray("favorite");
                            JsonObject jsonFavorite = new JsonObject().put("barcode", handlerMagazine.result().getString("barcode")).put("name", handlerMagazine.result().getString("name"));
                            if (favorite == null) {
                                favorite = new JsonArray().add(jsonFavorite);
                                customer.put("favorite", favorite);
                            } else {
                                favorite.add(jsonFavorite);
                            }
                            mongoClient.save("customers", customer, customerHandler -> {
                                if (customerHandler.failed()) {
                                    LOGGER.error("Error on save customer!!!");
                                }
                            });
                        } else {
                            LOGGER.error("Error on find magazine by ID!!!");
                        }
                    });
                } else {
                    LOGGER.error("Error on find delivery!!!");
                }
            });
        });
    }

}
