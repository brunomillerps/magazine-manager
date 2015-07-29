
package com.claudioliveira.receiver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * @author Claudio E. de Oliveira (claudioed.oliveira@gmail.com).
 */
public class UpdateMagazineByDelivery extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        final MongoClient mongoClient = MongoClient.createShared(vertx,
                new JsonObject().put("magazine-manager", "magazine-manager"), "magazine-manager");
        EventBus eb = vertx.eventBus();
        eb.consumer(
                "update-magazine-by-delivery",
                message -> {
                    JsonObject jsonMessage = new JsonObject(message.body().toString());
                    JsonObject query = new JsonObject(jsonMessage.getString("id"));
                    JsonObject update = new JsonObject().put(
                            "$set",
                            new JsonObject().put("name", jsonMessage.getString("name")).put(
                                    "price", jsonMessage.getDouble("price")));
                    mongoClient.update("magazines", query, update, res -> {
                        if (res.succeeded()) {
                            System.out.println("Magazine updated !");
                        } else {
                            res.cause().printStackTrace();
                        }

                    });

                });
    }
    
}
