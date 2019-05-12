/**
 * Copyright (C) 2018  Piotr Czapik.
 *
 * @author Piotr Czapik
 * @version 0.9
 * <p>
 * This file is part of Contact List.
 * Contact List is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Contact List is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Contact List.  If not, see <http://www.gnu.org/licenses/>
 * or write to: latidude99@gmail.com
 */

package com.latidude99.model;

public enum ContactProperty {

    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    EMAIL("email"),
    PHONE("phone"),
    STREET("street"),
    POSTCODE("postcode"),
    CITY("city"),
    COUNTRY("country"),
    DESCRIPTION("description");

    private String text;

    ContactProperty(String t) {
        text = t;
    }

    public String getText() {
        return text;
    }

}
