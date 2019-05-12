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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

/*
 * Info exchange object, Tymeleaf views, post requests
 */

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class FromView {

    private String text;

    private String sortBy; //sorting, pageable
    private int number; //sorting, pageable
    private int direction; //sorting, pageable
    @Min(value = 0, message = "{com.latidude99.model.FromView.result.Min}")
    private int result; //sorting, pageable

    @Min(value = 0, message = "{com.latidude99.model.FromView.result1.Min}")
    private int result1; //sorting, pageable
    @Min(value = 0, message = "{com.latidude99.model.FromView.result2.Min}")
    private int result2; //sorting, pageable
    @Min(value = 0, message = "{com.latidude99.model.FromView.result3.Min}")
    private int result3; //sorting, pageable
    @Min(value = 0, message = "{com.latidude99.model.FromView.result3.Min}")
    private int result4; //sorting, pageable

    private String field; // search by
    private String searchFor; // search by
    private String searchFor2; // search by
    private String searchFor3; // search by
    private String searchFor4; // search by
    private int sorting; // search by

    private String findBy; // find by

    private String selector; //selecting which POST request will be processed @/contacts

    private String dateStartTxt;
    private String dateEndTxt;

    private String records;

    private List<Contact> contactList;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateEnd;

    private List<String> contactsUploadedTokens;
    private String bulkOpsSwitch;

    public FromView() {
        bulkOpsSwitch = "0";
        selector = "0";
        sortBy = "0";
        findBy = "0";
        direction = 1;
        result = 0;
        field = "firstName";
        sorting = 1;
        contactList = new ArrayList<>();
        contactsUploadedTokens = new ArrayList<>();
    }


    public String getBulkOpsSwitch() {
        return bulkOpsSwitch;
    }

    public void setBulkOpsSwitch(String bulkOpsSwitch) {
        this.bulkOpsSwitch = bulkOpsSwitch;
    }

    public List<String> getContactsUploadedTokens() {
        return contactsUploadedTokens;
    }

    public void setContactsUploadedTokens(List<String> contactsUploadedTokens) {
        this.contactsUploadedTokens = contactsUploadedTokens;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public String getRecords() {
        return records;
    }

    public void setRecords(String records) {
        this.records = records;
    }

    public String getDateStartTxt() {
        return dateStartTxt;
    }


    public void setDateStartTxt(String dateStartTxt) {
        this.dateStartTxt = dateStartTxt;
    }


    public String getDateEndTxt() {
        return dateEndTxt;
    }


    public void setDateEndTxt(String dateEndTxt) {
        this.dateEndTxt = dateEndTxt;
    }


    public String getSearchFor4() {
        return searchFor4;
    }


    public void setSearchFor4(String searchFor4) {
        this.searchFor4 = searchFor4;
    }


    public int getResult4() {
        return result4;
    }


    public void setResult4(int result4) {
        this.result4 = result4;
    }


    public LocalDateTime getDateStart() {
        return dateStart;
    }


    public void setDateStart(LocalDateTime dateStart) {
        this.dateStart = dateStart;
    }


    public LocalDateTime getDateEnd() {
        return dateEnd;
    }


    public void setDateEnd(LocalDateTime dateEnd) {
        this.dateEnd = dateEnd;
    }


    public String getSearchFor2() {
        return searchFor2;
    }

    public void setSearchFor2(String searchFor2) {
        this.searchFor2 = searchFor2;
    }

    public String getSearchFor3() {
        return searchFor3;
    }

    public void setSearchFor3(String searchFor3) {
        this.searchFor3 = searchFor3;
    }

    public int getResult1() {
        return result1;
    }

    public void setResult1(int result1) {
        this.result1 = result1;
    }

    public int getResult2() {
        return result2;
    }

    public void setResult2(int result2) {
        this.result2 = result2;
    }

    public int getResult3() {
        return result3;
    }

    public void setResult3(int result3) {
        this.result3 = result3;
    }

    public String getFindBy() {
        return findBy;
    }

    public void setFindBy(String findBy) {
        this.findBy = findBy;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getSearchFor() {
        return searchFor;
    }

    public void setSearchFor(String searchFor) {
        this.searchFor = searchFor;
    }

    public int getSorting() {
        return sorting;
    }

    public void setSorting(int sorting) {
        this.sorting = sorting;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }


}
