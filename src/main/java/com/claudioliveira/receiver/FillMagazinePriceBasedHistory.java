
package com.claudioliveira.receiver;

import com.claudioliveira.domain.DomainCollection;
import com.claudioliveira.domain.DomainEvent;
import com.claudioliveira.domain.PlainBarcode;
import com.claudioliveira.infra.DateTimeMongoFormat;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;

import java.time.LocalDateTime;

/**
 * Register magazine price based in magazine history.
 *
 * @author Claudio E. de Oliveira (claudioed.oliveira@gmail.com).
 */
public class FillMagazinePriceBasedHistory extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(FillMagazinePriceBasedHistory.class);

    @Override
    public void start() throws Exception {
        final MongoClient mongoClient = MongoClient.createShared(vertx,
                new JsonObject().put("magazine-manager", "magazine-manager"), "magazine-manager");
        EventBus eb = vertx.eventBus();
        eb.consumer(DomainEvent.FILL_MAGAZINE_PRICE_IN_HISTORY.event(),
                message -> {
                    PlainBarcode plainBarcode = new PlainBarcode(new JsonObject(message.toString())
                            .getString("plainBarcode"));
                    FindOptions queryOptions = new FindOptions().setSort(new JsonObject().put("deliveryTimestamp", new JsonObject()
                            .put("$date", DateTimeMongoFormat.format(LocalDateTime.now())))).setLimit(1);
                    JsonObject query = new JsonObject().put("barcode", plainBarcode.barcode());
                    mongoClient.findWithOptions(DomainCollection.MAGAZINES.collection(),query,queryOptions,handler ->{
                        if(handler.succeeded()){
                            handler.result().forEach(history ->{
                                JsonObject newMagazineQuery = new JsonObject().put("plainBarcode", plainBarcode.plainBarcode());
                                JsonObject update = new JsonObject().put("$set", new JsonObject()
                                        .put("price", history.getDouble("price"))
                                        .put("withPrice", Boolean.TRUE))
                                        .put("byHistory",Boolean.TRUE);
                                mongoClient.update(DomainCollection.MAGAZINES.collection(),newMagazineQuery,update,updateHandler ->{
                                   if(updateHandler.succeeded()){
                                        LOGGER.info("Update magazine by History!!!");
                                   }
                                });
                            });
                        }else{
                            LOGGER.error("Error on find magazines!!!");
                        }
                    });
                });
    }

}
