package com.claudioliveira.domain;

/**
 * Constants for system events.
 * 
 *  @author Claudio E. de Oliveira (claudioed.oliveira@gmail.com).
 */
public enum DomainEvent {
    
    NEW_DELIVERY,
    
    NEW_CUSTOMER,

    NEW_FAVORITE_MAGAZINE,
    
    SUCCESS_DELIVERY,
    
    NEW_GATHERING,
    
    UPDATE_MAGAZINE_ON_DELIVERY,
    
    NEW_SALE,
    
    FILL_MAGAZINE_PRICE_IN_HISTORY,
    
    UPDATE_MAGAZINE_STOCK,
    
    UPDATE_DELIVERY_STOCK,
    
    NOTIFY_CUSTOMER_ARRIVE_NEW_MAGAZINE;
    
    public String event(){
        return this.name();
    }

}
