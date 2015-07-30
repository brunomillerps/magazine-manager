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
    
    NEW_SALE;
    
    public String event(){
        return this.name();
    }

}
