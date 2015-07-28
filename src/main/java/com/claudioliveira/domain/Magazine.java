package com.claudioliveira.domain;

import io.vertx.core.json.JsonObject;

/**
 * @author Claudio E. de Oliveira on 27/07/15.
 */
public class Magazine implements DomainEntity{
    
    private final String barcode;
    
    private final String name;

    private Magazine(String barcode, String name) {
        this.barcode = barcode;
        this.name = name;
    }

    public static Magazine newMagazine(String barcode, String name) {
        return new Magazine(barcode, name);
    }

    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    @Override
    public JsonObject toJson() {
        return new JsonObject().put("barcode",this.barcode).put("name",this.name);
    }
}
