package com.claudioliveira.server;

import com.claudioliveira.api.*;

import com.claudioliveira.receiver.RegisterNewCustomer;
import com.claudioliveira.receiver.RegisterNewDelivery;
import com.claudioliveira.receiver.RegisterSales;
import com.claudioliveira.receiver.SalesDashboard;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * @author Claudio Eduardo de Oliveira (claudioed.oliveira@gmail.com).
 */
public class MagazineManagerServer {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx(new VertxOptions());
        vertx.deployVerticle(new SalesAPI());
        vertx.deployVerticle(new MagazineAPI());
        vertx.deployVerticle(new DeliveryAPI());
        vertx.deployVerticle(new GatheringAPI());
        vertx.deployVerticle(new CustomerAPI());
        vertx.deployVerticle(new RegisterNewCustomer());
        vertx.deployVerticle(new RegisterSales());
        vertx.deployVerticle(new SalesDashboard());
        vertx.deployVerticle(new RegisterNewDelivery());
    }

}
