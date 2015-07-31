package com.claudioliveira.domain;

/**
 * Constants for system collections.
 * 
 * @author Claudio E. de Oliveira (claudioed.oliveira@gmail.com).
 */
public enum  DomainCollection {
    
    SALES,
    
    MAGAZINES,
    
    CUSTOMERS,
    
    DAILY_SALES,
    
    DELIVERIES;
    
    public String collection(){
        return this.name().toLowerCase();
    }
    
}
