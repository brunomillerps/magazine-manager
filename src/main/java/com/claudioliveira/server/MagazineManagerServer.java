package com.claudioliveira.server;

import com.claudioliveira.api.MagazineAPI;
import com.claudioliveira.api.SalesAPI;

import com.claudioliveira.receiver.RegisterSales;
import com.claudioliveira.receiver.SalesDashboard;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * @author Claudio Eduardo de Oliveira (claudioeduardo.deoliveira@sonymobile.com).
 */
public class MagazineManagerServer {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx(new VertxOptions());
        vertx.deployVerticle(new SalesAPI());
        vertx.deployVerticle(new MagazineAPI());
        vertx.deployVerticle(new RegisterSales());
        vertx.deployVerticle(new SalesDashboard());
    }

}
