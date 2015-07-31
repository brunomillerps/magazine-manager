package com.claudioliveira.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 
 * It is responsible to generate code for sales.
 *  
 * @author Claudio E. de Oliveira on 30/07/15.
 */
public class SalesCodeGenerator {
    
    private final LocalDateTime dateTime;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-M-yyyy");

    public SalesCodeGenerator(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    
    public String newCode(){
        return FORMATTER.format(dateTime) + "-" + UUID.randomUUID().toString();
    }

}
