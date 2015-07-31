package com.claudioliveira.receiver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * 
 * This handler is responsible to send email to customers which have the
 * favorite magazine.
 *  
 * @author Claudio E. de Oliveira (claudioed.oliveira@gmail.com).
 */
public class NewMagazineMailer extends AbstractVerticle{

    public void start() throws Exception {
        final MongoClient mongoClient = MongoClient.createShared(vertx,
                new JsonObject().put("magazine-manager", "magazine-manager"), "magazine-manager");
    }
    
}
