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

package com.latidude99.model;

public class Load {
	
	private Boolean byFirstName;
	private Boolean byLastName;
	private Boolean byEmail;
	private Boolean byPhone;
	private Boolean byStreet;
	private Boolean byPostcode;
	private Boolean byCity;
	private Boolean byCountry;
	private Boolean byDate;
	private Boolean ascending;
	private Boolean descending;
	private int number;
	private int pages;
	
	public Load() {
		byFirstName = true;
		byLastName = false;
		byEmail = false;
		byPhone = false;
		byStreet = false;
		byPostcode = false;
		byCity = false;
		byCountry = false;
		byDate = false;
		ascending = true;
		descending = false;
		number = -1;
		pages = -1;
		
	}
	
	public Boolean getByFirstName() {
		return byFirstName;
	}
	public void setByFirstName(Boolean byFirstName) {
		this.byFirstName = byFirstName;
	}
	public Boolean getByLastName() {
		return byLastName;
	}
	public void setByLastName(Boolean byLastName) {
		this.byLastName = byLastName;
	}
	public Boolean getByEmail() {
		return byEmail;
	}
	public void setByEmail(Boolean byEmail) {
		this.byEmail = byEmail;
	}
	public Boolean getByPhone() {
		return byPhone;
	}
	public void setByPhone(Boolean byPhone) {
		this.byPhone = byPhone;
	}
	public Boolean getByStreet() {
		return byStreet;
	}
	public void setByStreet(Boolean byStreet) {
		this.byStreet = byStreet;
	}
	public Boolean getByPostcode() {
		return byPostcode;
	}
	public void setByPostcode(Boolean byPostcode) {
		this.byPostcode = byPostcode;
	}
	public Boolean getByCity() {
		return byCity;
	}
	public void setByCity(Boolean byCity) {
		this.byCity = byCity;
	}
	public Boolean getByCountry() {
		return byCountry;
	}
	public void setByCountry(Boolean byCountry) {
		this.byCountry = byCountry;
	}
	public Boolean getByDate() {
		return byDate;
	}
	public void setByDate(Boolean byDate) {
		this.byDate = byDate;
	}
	public Boolean getAscending() {
		return ascending;
	}
	public void setAscending(Boolean ascending) {
		this.ascending = ascending;
	}
	public Boolean getDescending() {
		return descending;
	}
	public void setDescending(Boolean descending) {
		this.descending = descending;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	
	
	

}
