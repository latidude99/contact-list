package com.latidude99;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class ZonedDateTimeConverter implements Converter<String, ZonedDateTime> {

    private final DateTimeFormatter formatter;
    
    public ZonedDateTimeConverter() {
        // set the zone in the formatter
        this.formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.of("Europe/London"));
    }
    
    public ZonedDateTimeConverter(ZoneId zoneId) {
        // set the zone in the formatter
        this.formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(zoneId);
    }

    @Override
    public ZonedDateTime convert(String source) {
        // now the formatter has a zone set, so I can parse directly to ZonedDateTime
        return ZonedDateTime.parse(source, this.formatter);
    }
}