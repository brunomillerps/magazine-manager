package com.claudioliveira.infra;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Claudio E. de Oliveira (claudioed.oliveira@gmail.com).
 */
public class DateTimeMongoFormat {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss+00:00");
    
    public static String format(LocalDateTime date){
        return FORMATTER.format(date);
    }

}
