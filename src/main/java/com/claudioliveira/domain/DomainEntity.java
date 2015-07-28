package com.claudioliveira.domain;

import io.vertx.core.json.JsonObject;

/**
 * @author Claudio E. de Oliveira on 27/07/15.
 */
public interface DomainEntity {
    
    JsonObject toJson();
    
}
