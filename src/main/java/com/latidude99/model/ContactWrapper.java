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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/*
 * Used to manage contact records from database
 */

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ContactWrapper {

    private List<Contact> resultList;
    private List<Contact> deletedList;
    private List<Contact> duplicatedList;
    private List<Contact> removedList;
    private String selector;

    public ContactWrapper() {
        resultList = new ArrayList<>();
        deletedList = new ArrayList<>();
        duplicatedList = new ArrayList<>();
        removedList = new ArrayList<>();
        selector = "0";
    }


    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public List<Contact> getRemovedList() {
        return removedList;
    }

    public void setRemovedList(List<Contact> removedList) {
        this.removedList = removedList;
    }

    public List<Contact> getDuplicatedList() {
        return duplicatedList;
    }

    public void setDuplicatedList(List<Contact> duplicatedList) {
        this.duplicatedList = duplicatedList;
    }

    public List<Contact> getDeletedList() {
        return deletedList;
    }

    public void setDeletedList(List<Contact> deletedList) {
        this.deletedList = deletedList;
    }

    public List<Contact> getResultList() {
        return resultList;
    }

    public void setResultList(List<Contact> resultList) {
        this.resultList = resultList;
    }


}
