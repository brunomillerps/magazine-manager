
package com.claudioliveira.receiver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Claudio Eduardo de Oliveira
 *         (claudioed.oliveira@gmail.com).
 */
public class SalesDashboard extends AbstractVerticle {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;

    @Override
    public void start() throws Exception {
        final MongoClient mongoClient = MongoClient.createShared(vertx,
                new JsonObject().put("magazine-manager", "magazine-manager"), "magazine-manager");
        EventBus eb = vertx.eventBus();
        eb.consumer("new-sale", message -> mongoClient.insert("daily-sales", new JsonObject(message
                .body().toString()).put("date", FORMATTER.format(LocalDateTime.now())), result -> {
        }));
    }

}
