
package com.claudioliveira.receiver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * @author Claudio Eduardo de Oliveira
 *         (claudioeduardo.deoliveira@sonymobile.com).
 */
public class RegisterSales extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        final MongoClient mongoClient = MongoClient.createShared(vertx,
                new JsonObject().put("magazine-manager", "magazine-manager"), "magazine-manager");
        EventBus eb = vertx.eventBus();
        eb.consumer("new-sale", message -> mongoClient.insert("sales", new JsonObject(message
                .body().toString()), result -> {
        }));
    }

}
