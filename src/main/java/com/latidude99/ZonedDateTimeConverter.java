/**Copyright (C) 2018  Piotr Czapik.
 * @author Piotr Czapik
 * @version 0.9
 *
 *  This file is part of Contact List.
 *  Contact List is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Contact List is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Contact List.  If not, see <http://www.gnu.org/licenses/>
 *  or write to: latidude99@gmail.com
 */

package com.latidude99;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class ZonedDateTimeConverter implements Converter<String, ZonedDateTime> {

    private final DateTimeFormatter formatter;

    /*
     * Sets the zone in the formatter
     */
    public ZonedDateTimeConverter() { // default
        this.formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.of("Europe/London"));
    }

    public ZonedDateTimeConverter(ZoneId zoneId) {
        this.formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(zoneId);
    }

    /*
     * Formatter has a zone set, now parses it directly to ZonedDateTime
     */
    @Override
    public ZonedDateTime convert(String source) {
        return ZonedDateTime.parse(source, this.formatter);
    }
}

