
package com.claudioliveira.api;

import com.claudioliveira.domain.DomainEvent;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * @author Claudio E. de Oliveira (claudioed.oliveira@gmail.com).
 */
public class CustomerAPI extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx(new VertxOptions());
        vertx.deployVerticle(new CustomerAPI());
    }

    @Override
    public void start() throws Exception {

        final MongoClient mongoClient = MongoClient.createShared(vertx,
                new JsonObject().put("magazine-manager", "magazine-manager"), "magazine-manager");
        final Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.get("/api/customers").handler(
                ctx -> mongoClient.find("customers", new JsonObject(), lookup -> {
                    if (lookup.failed()) {
                        ctx.fail(lookup.cause());
                        return;
                    }
                    final JsonArray json = new JsonArray();
                    lookup.result().forEach(json::add);
                    ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                    ctx.response().end(json.encode());
                }));

        router.post("/api/customer").handler(ctx -> {
            vertx.eventBus().publish(DomainEvent.NEW_CUSTOMER.event(), ctx.getBodyAsJson());
            ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            ctx.response().end();
        });

        vertx.createHttpServer().requestHandler(router::accept).listen(9004);
    }

}
