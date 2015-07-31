
package com.claudioliveira.receiver;

import com.claudioliveira.domain.DomainCollection;
import com.claudioliveira.domain.DomainEvent;
import com.claudioliveira.domain.PlainBarcode;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mail.*;
import io.vertx.ext.mongo.MongoClient;

/**
 * This handler is responsible to send email to customers which have the
 * favorite magazine.
 * 
 * @author Claudio E. de Oliveira (claudioed.oliveira@gmail.com).
 */
public class NewMagazineMailer extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewMagazineMailer.class);

    public void start() throws Exception {
        final MongoClient mongoClient = MongoClient.createShared(vertx,new JsonObject().put("magazine-manager", "magazine-manager"), "magazine-manager");
        MailConfig mailConfig = new MailConfig()
                .setHostname("smtp.example.com")
                .setPort(587)
                .setStarttls(StartTLSOptions.REQUIRED)
                .setLogin(LoginOption.REQUIRED)
                .setAuthMethods("PLAIN")
                .setUsername("username")
                .setPassword("password");
        MailClient mailClient = MailClient.createShared(vertx, mailConfig);
        EventBus eb = vertx.eventBus();
        eb.consumer(
                DomainEvent.NOTIFY_CUSTOMER_ARRIVE_NEW_MAGAZINE.event(),
                message -> {
                    PlainBarcode plainBarcode = new PlainBarcode(new JsonObject(message.toString()).getString("plainBarcode"));
                    JsonObject query = new JsonObject().put("favorite.barcode",plainBarcode.barcode());
                    mongoClient.find(DomainCollection.CUSTOMERS.collection(), query, result -> {
                        if(result.succeeded()){
                            result.result().forEach(customer ->{
                                MailMessage email = new MailMessage()
                                        .setFrom("banca.castelo@gmail.com")
                                        .setTo(customer.getString("email"))
                                        .setSubject("Chegou sua revista favorita")
                                        .setText("Chegou sua revista favorita")
                                        .setHtml("<a href=\"http://vertx.io\">vertx.io</a>");
                                mailClient.sendMail(email,handler ->{
                                    if(result.succeeded()){
                                        LOGGER.info("Email sent with success!!!");
                                    } else {
                                        LOGGER.error("Error on send email with favorite magazine!!!");
                                    }
                                });

                            });

                        } else {
                            LOGGER.error("Error on find customers with favorite");
                        }
                    });
                });

    }

}
