package com.latidude99.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
