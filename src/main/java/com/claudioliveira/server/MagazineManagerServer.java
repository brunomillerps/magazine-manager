package com.claudioliveira.server;

import com.claudioliveira.api.*;

import com.claudioliveira.receiver.*;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * 
 * The startup server of Magazine Manager.
 *  
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
        vertx.deployVerticle(new RegisterMagazinesByDelivery());
        vertx.deployVerticle(new RegisterInfoMagazine());
        vertx.deployVerticle(new FavoriteMagazine());
    }

}
