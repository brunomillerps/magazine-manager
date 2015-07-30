
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
public class MagazineAPI extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx(new VertxOptions());
        vertx.deployVerticle(new MagazineAPI());
    }

    @Override
    public void start() throws Exception {

        final MongoClient mongoClient = MongoClient.createShared(vertx,
                new JsonObject().put("magazine-manager", "magazine-manager"), "magazine-manager");
        final Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.get("/api/magazines").handler(
                ctx -> mongoClient.find("magazines", new JsonObject(), lookup -> {
                    if (lookup.failed()) {
                        ctx.fail(lookup.cause());
                        return;
                    }
                    final JsonArray json = new JsonArray();
                    lookup.result().forEach(json::add);
                    ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                    ctx.response().end(json.encode());
                }));

        router.get("/api/magazine/:id").handler(ctx -> mongoClient.findOne("magazines", new JsonObject().put("_id", ctx.request().getParam("id")), new JsonObject(), lookup -> {
            if (lookup.failed()) {
                ctx.fail(lookup.cause());
                return;
            }
            ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            ctx.response().end(lookup.result().encode());
        }));

        router.put("/api/delivery/:deliveryId/magazine/:barcode").handler(ctx -> {
            vertx.eventBus().publish(DomainEvent.UPDATE_MAGAZINE_ON_DELIVERY.event(), ctx.getBodyAsJson().put("deliveryId",ctx.request().getParam("deliveryId")).put("barcode",ctx.request().getParam("barcode")));
            ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            ctx.response().end();
        });

        router.get("/api/magazine/barcode/:barcode").handler(ctx -> mongoClient.find("magazines", new JsonObject().put("barcode", ctx.request().getParam("barcode")).put("available",Boolean.TRUE), lookup -> {
            if (lookup.failed()) {
                ctx.fail(lookup.cause());
                return;
            }
            final JsonArray json = new JsonArray();
            lookup.result().forEach(json::add);
            ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            ctx.response().end(json.encode());
        }));

        vertx.createHttpServer().requestHandler(router::accept).listen(9000);

    }

}
