package com.claudioliveira.domain;

/**
 * @author Claudio E. de Oliveira (claudioed.oliveira@gmail.com).
 */
public enum DomainEvent {
    
    NEW_DELIVERY,
    
    NEW_CUSTOMER,
    
    SUCCESS_DELIVERY,
    
    NEW_GATHERING,
    
    UPDATE_MAGAZINE_ON_DELIVERY,
    
    NEW_SALE,
    
    FILL_MAGAZINE_PRICE_IN_HISTORY;
    
    public String event(){
        return this.name();
    }

}
