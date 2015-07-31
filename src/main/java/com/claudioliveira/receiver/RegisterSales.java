
package com.claudioliveira.receiver;

import com.claudioliveira.domain.DomainCollection;
import com.claudioliveira.domain.DomainEvent;
import com.claudioliveira.domain.SalesCodeGenerator;
import com.claudioliveira.infra.DateTimeMongoFormat;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This handler is responsible to stores sales in database.
 *
 * @author Claudio Eduardo de Oliveira (claudioed.oliveira@gmail.com).
 */
public class RegisterSales extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterSales.class);


    @Override
    public void start() throws Exception {
        final MongoClient mongoClient = MongoClient.createShared(vertx,
                new JsonObject().put("magazine-manager", "magazine-manager"), "magazine-manager");
        EventBus eb = vertx.eventBus();
        eb.consumer(DomainEvent.NEW_SALE.event(), message ->
                        mongoClient.insert(DomainCollection.SALES.collection(), new JsonObject(message
                                .body().toString())
                                .put("creationAt", new JsonObject().put("$date", DateTimeMongoFormat.format(LocalDateTime.now())))
                                .put("code", new SalesCodeGenerator(LocalDateTime.now()).newCode()), result -> {
                            if (result.succeeded()) {
                                eb.publish(DomainEvent.UPDATE_DELIVERY_STOCK.event(), new JsonObject().put("saleId", result.result()));
                                eb.publish(DomainEvent.UPDATE_MAGAZINE_STOCK.event(), new JsonObject().put("saleId", result.result()));
                            } else {
                                LOGGER.error("Error on save sale!!!");
                            }
                        })
        );
    }

}
