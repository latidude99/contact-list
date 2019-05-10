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
 */package com.latidude99.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.latidude99.ZonedDateTimeConverter;
import com.latidude99.model.Contact;
import com.latidude99.model.ContactProperty;
import com.latidude99.model.User;
import com.latidude99.repository.ContactPageRepository;
import com.latidude99.repository.ContactRepository;
import com.latidude99.repository.UserRepository;
import com.latidude99.repository.UserRoleRepository;
import com.latidude99.web.controller.UserController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ContactService {
	private static final Logger logger = LoggerFactory.getLogger(ContactService.class);
	
	private UserRepository userRepository;
	private UserRoleRepository roleRepository;
	private ContactRepository contactRepository;
	private ContactPageRepository contactPageRepository;
	ZonedDateTimeConverter zonedDateTimeConverter;
	
	@Autowired
	public void setContactPageRepository(ContactPageRepository contactPageRepository) {
		this.contactPageRepository = contactPageRepository;
	}
	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	@Autowired
	public void setRoleRepository(UserRoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
	@Autowired
	public void setContactRepository(ContactRepository contactRepository) {
		this.contactRepository = contactRepository;
	}
	@Autowired
	public void setZonedDateTimeConverter(ZonedDateTimeConverter zonedDateTimeConverter) {
		this.zonedDateTimeConverter = zonedDateTimeConverter;
	}
	public void saveContact(Contact contact) {
		contactRepository.save(contact);
	}
	public Contact findById(long id) {
		return contactRepository.findById(id);
	}
	
	public Contact findByUniqueToken(String uniqueToken) {
		return contactRepository.findByUniqueToken(uniqueToken);
	}
	
	
	public void deleteContact(long id) {
		Contact contact = contactRepository.findById(id);
		contact.setUser(null);
		contactRepository.delete(id);
		
		//User currentUser = contact.getUser();
		//contact.getUser().getContacts().remove((int) id);
		//User currentUser = contact.getUser();
		//userRepository.save(currentUser);
		//contactRepository.removeContactById(id);
		
	}
	
	public void removeByTokens(List<String> tokens) {
		for(String token :tokens) {
			Contact contact = findByUniqueToken(token);
			if(contact != null) {
				contact.setUser(null);
				contactRepository.delete(contact.getId());
			}
		}
	}
	
	public void deleteAllByUser(User user) {
		contactRepository.removeAllByUser(user);
		user.setContacts(new ArrayList<>());
	}
	
	public Integer getTotalByUser(User user) {
		return contactRepository.countByUserIdAndDeletedAndDuplicated(user.getId(), "0", "0");
	}
	
	public List<Contact> getFlaggedDeleted() {
		return contactRepository.findByDeleted("1");
	}
	
	public List<Contact> getFlaggedDuplicated() {
		return contactRepository.findByDuplicated("1");
	}
	
	public void flagAsDeletedAndRemoveLast(List<Contact> deletedList) {
		if(deletedList != null && deletedList.size() > 0) {
			contactRepository.findByDeletedAndDuplicated("1", "0").forEach(c -> deleteContact(c.getId()));
			deletedList.forEach(c -> {c.setDeleted("1");
									updateContact(c);
			});
		}
	}
	
	public void flagAsDuplicatedAndRemoveLast(List<Contact> duplicatedList) {
		if(duplicatedList != null && duplicatedList.size() > 0) {
			contactRepository.findByDeletedAndDuplicated("0", "1").forEach(c -> deleteContact(c.getId()));
			duplicatedList.forEach(c -> {c.setDuplicated("1");
									updateContact(c);
			});
		}
	}
	
	public void updateContact(Contact contact) {
		System.out.println("Contact updated id: --> "+ contact.getId());
//		System.out.println("Contact updated user: --> "+ contact.getUser());
		Contact updated = new Contact();
		updated.setId(contact.getId());
		ZonedDateTime created = contactRepository.findById(contact.getId()).getCreated();
//		System.out.println("Contact updated before setting new values: --> "+ updated);
		updated.setCreated(created);
		System.out.println("Contact updated Created: --> " + created);
		updated.setFirstName(contact.getFirstName());
		updated.setLastName(contact.getLastName());
		updated.setPhone(contact.getPhone());
		updated.setEmail(contact.getEmail().trim().replace(" ", ""));
		updated.setStreet(contact.getStreet());
		updated.setPostcode(contact.getPostcode());
		updated.setCity(contact.getCity());
		updated.setCountry(contact.getCountry());
		updated.setDescription(contact.getDescription());
		updated.setUser(contact.getUser());
		updated.setDeleted(contact.getDeleted());
		updated.setDuplicated(contact.getDuplicated());
		System.out.println("Contact updated after setting new values: --> "+ updated.getDuplicated());
//		System.out.println("Contact updated after setting new values - user: --> "+ updated.getUser());
		contactRepository.save(updated);
//		updated.setDate(contact.getDate());
	}
	
	// for ContactController switch case 1
	public List<Contact> findNByColumnSortedBy(User user, int number, String column, int sorting){
		logger.info("case 1");
		Direction direction;
		if(sorting == 1) {
			direction = Direction.ASC;
		}else {
			direction = Direction.DESC;
		}
		if("0".equals(column))
			column = "firstName";
		if(number < 1)
			number = Integer.MAX_VALUE;
		Sort sort = new Sort(new Sort.Order(direction, column));
		Pageable pageable = new PageRequest(0, number, sort);
		Page<Contact> contactsPage = contactRepository.findAllByUserId(user.getId(), pageable);
		List<Contact> contacts = contactsPage.getContent();
		logger.info("case 1: " + contacts.size());
		return contacts;
	}
	
	// for ContactController switch case 2
	public List<Contact> findByPropertyName(User user, String field, String searched, int number){
		logger.info("case 2");
		if(number < 1)
			number = Integer.MAX_VALUE;
		if("0".equals(field))
			return searchAllColumnsLimitResult(user, searched, number);
		ContactProperty property = propertyFromText(field);
		Sort sort = new Sort(new Sort.Order(Direction.ASC, field));
		Pageable pageable = new PageRequest(0, number, sort);
		switch(property) {
		case FIRST_NAME:
			return contactRepository.findAllByUserIdAndFirstName(user.getId(), searched, pageable);
		case LAST_NAME:
			return contactRepository.findAllByUserIdAndLastName(user.getId(), searched, pageable);
		case EMAIL:
			return contactRepository.findAllByUserIdAndEmail(user.getId(), searched, pageable);
		case PHONE:
			return contactRepository.findAllByUserIdAndPhone(user.getId(), searched, pageable);
		case STREET:
			return contactRepository.findAllByUserIdAndStreet(user.getId(), searched, pageable);
		case POSTCODE:
			return contactRepository.findAllByUserIdAndPostcode(user.getId(), searched, pageable);
		case CITY:
			return contactRepository.findAllByUserIdAndCity(user.getId(), searched, pageable);
		case COUNTRY:
			return contactRepository.findAllByUserIdAndCountry(user.getId(), searched, pageable);
		case DESCRIPTION:
			return contactRepository.findAllByUserIdAndDescription(user.getId(), searched, pageable);
		default:
			return searchAllColumnsLimitResult(user, searched, number);
		}
	}
	
	// for ContactController switch case 3
	public List<Contact> findByPartialPropertyName(User user, String field, String searched, int number){
		logger.info("case 3");
		if(number < 1)
			number = Integer.MAX_VALUE;
		Sort sort = new Sort(new Sort.Order(Direction.ASC, "firstName"));
		Pageable pageable = new PageRequest(0, number, sort);
		// eliminating other users contacts from the result list, 
		// don't know (yet) why getAllByUserAndAnyLike method returns contacts from all users
		if("0".equals(field)) {
			List<Contact> resultByUser = getAllByUserAndAnyLike(user, searched, pageable).stream()
			     .filter(contact -> contact.getUser().getId() == user.getId())
			     .collect(Collectors.toList());
			System.err.println(resultByUser.size());
			return resultByUser;
		}
		ContactProperty property = propertyFromText(field);
		switch(property) {
		case FIRST_NAME:
			return contactRepository.findAllByUserIdAndFirstNameIgnoreCaseContaining(user.getId(), searched, pageable);
		case LAST_NAME:
			return contactRepository.findAllByUserIdAndLastNameIgnoreCaseContaining(user.getId(), searched, pageable);
		case EMAIL:
			return contactRepository.findAllByUserIdAndEmailIgnoreCaseContaining(user.getId(), searched, pageable);
		case PHONE:
			return contactRepository.findAllByUserIdAndPhoneIgnoreCaseContaining(user.getId(), searched, pageable);
		case STREET:
			return contactRepository.findAllByUserIdAndStreetIgnoreCaseContaining(user.getId(), searched, pageable);
		case POSTCODE:
			return contactRepository.findAllByUserIdAndPostcodeIgnoreCaseContaining(user.getId(), searched, pageable);
		case CITY:
			return contactRepository.findAllByUserIdAndCityIgnoreCaseContaining(user.getId(), searched, pageable);
		case COUNTRY:
			return contactRepository.findAllByUserIdAndCountryIgnoreCaseContaining(user.getId(), searched, pageable);
		case DESCRIPTION:
			return contactRepository.findAllByUserIdAndDescriptionIgnoreCaseContaining(user.getId(), searched, pageable);
		default:
			return filterByUserId(getAllByUserAndAnyLike(user, searched, pageable), user);
		}
	}
	
	// for ContactController switch case 4
	public List<Contact> findByDate(User user, String field, String searched, int number, String dateStartTxt, String dateEndTxt){
		logger.info("case 4");
		logger.info("dateStartTxt before: " + dateStartTxt);
		if(number < 1)
			number = Integer.MAX_VALUE;
		Sort sort = new Sort(new Sort.Order(Direction.ASC, "firstName"));
		Pageable pageable = new PageRequest(0, number, sort);
		if(dateStartTxt.equals("") || dateStartTxt.equals(null))
			dateStartTxt = "1500-01-01T10:00";
		if(dateEndTxt.equals("") || dateEndTxt.equals(null))
			dateEndTxt = "2500-01-01T10:00";
		logger.info("dateStartTxt after: " + dateStartTxt);
		LocalDateTime dateStart = LocalDateTime.parse(dateStartTxt + ":00");
		LocalDateTime dateEnd = LocalDateTime.parse(dateEndTxt + ":00");
		ZonedDateTime dateStartZoned = dateStart.atZone(ZoneId.of("UTC"));
		ZonedDateTime dateEndZoned = dateEnd.atZone(ZoneId.of("UTC"));
		System.out.println(dateStartZoned.toString() + "   " + dateEndZoned.toString());
		if(searched.equals(null)){
			logger.info("case 4a");
//			if(dateStart.equals(null))
//				dateStart = LocalDateTime.of(1500, 01, 00, 00, 00);
//			if(dateEnd.equals(null))
//				dateEnd = LocalDateTime.of(2500, 01, 01, 00, 00, 00);
//			ZonedDateTime dateStartZoned = dateStart.atZone(ZoneId.of("UTC"));
//			ZonedDateTime dateEndZoned = dateEnd.atZone(ZoneId.of("UTC"));
//			System.out.println(dateStartZoned.toString() + "   " + dateEndZoned.toString());
			return filterByUserAndDate(user, dateStartZoned, dateEndZoned, pageable);
		}else if(searched != null && !field.equals("0")){
			logger.info("case 4b");
//			if(dateStart.equals(null))
//				dateStart = LocalDateTime.of(1500, 01, 00, 00, 00);
//			if(dateEnd.equals(null))
//				dateEnd = LocalDateTime.of(2500, 01, 01, 00, 00, 00);
//			ZonedDateTime dateStartZoned = dateStart.atZone(ZoneId.of("UTC"));
//			ZonedDateTime dateEndZoned = dateEnd.atZone(ZoneId.of("UTC"));
//			System.out.println(dateStartZoned.toString() + "   " + dateEndZoned.toString());
			ContactProperty property = propertyFromText(field);
			logger.info(propertyFromText(field).getText());
			switch(property) {
			case FIRST_NAME:
				return contactRepository.findByUserIdAndCreatedBetweenAndFirstNameIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched, pageable);
			case LAST_NAME:
				return contactRepository.findByUserIdAndCreatedBetweenAndLastNameIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched, pageable);
			case EMAIL:
				return contactRepository.findByUserIdAndCreatedBetweenAndEmailIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched, pageable);
			case PHONE:
				return contactRepository.findByUserIdAndCreatedBetweenAndPhoneIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched, pageable);
			case STREET:
				return contactRepository.findByUserIdAndCreatedBetweenAndStreetIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched, pageable);
			case POSTCODE:
				return contactRepository.findByUserIdAndCreatedBetweenAndPostcodeIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched, pageable);
			case CITY:
				return contactRepository.findByUserIdAndCreatedBetweenAndCityIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched, pageable);
			case COUNTRY:
				return contactRepository.findByUserIdAndCreatedBetweenAndCountryIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched, pageable);
			case DESCRIPTION:
				return contactRepository.findByUserIdAndCreatedBetweenAndDescriptionIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched, pageable);
//			default:
//				return filterByUserId(getAllByUserAndAnyLike(user, searched, pageable), user);
			}
		}else if(searched != null & "0".equals(field)) { 
				logger.info("case 4c");
				logger.info("searched: " + searched);
//				if(dateStart.equals(null))
//					dateStart = LocalDateTime.of(1500, 01, 00, 00, 00);
//				if(dateEnd.equals(null))
//					dateEnd = LocalDateTime.of(2500, 01, 01, 00, 00, 00);
//				ZonedDateTime dateStartZoned = dateStart.atZone(ZoneId.of("UTC"));
//				ZonedDateTime dateEndZoned = dateEnd.atZone(ZoneId.of("UTC"));
//				System.out.println(dateStartZoned.toString() + "   " + dateEndZoned.toString());
				List<Contact> contactsByCreatedAndFirstName = contactRepository.findByUserIdAndCreatedBetweenAndFirstNameIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched);
				List<Contact> contactsByCreatedAndLastName =  contactRepository.findByUserIdAndCreatedBetweenAndLastNameIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched);
				List<Contact> contactsByCreatedAndEmailName =  contactRepository.findByUserIdAndCreatedBetweenAndEmailIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched);
				List<Contact> contactsByCreatedAndPhoneName =  contactRepository.findByUserIdAndCreatedBetweenAndPhoneIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched);
				List<Contact> contactsByCreatedAndStreetName =  contactRepository.findByUserIdAndCreatedBetweenAndStreetIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched);
				List<Contact> contactsByCreatedAndPostcodeName =  contactRepository.findByUserIdAndCreatedBetweenAndPostcodeIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched);
				List<Contact> contactsByCreatedAndCityName =  contactRepository.findByUserIdAndCreatedBetweenAndCityIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched);
				List<Contact> contactsByCreatedAndCountryName =  contactRepository.findByUserIdAndCreatedBetweenAndCountryIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched);
				List<Contact> contactsByCreatedAndDescriptionName =  contactRepository.findByUserIdAndCreatedBetweenAndDescriptionIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched);
				logger.info("list before stream: " + contactsByCreatedAndFirstName.size());
				Stream<Contact> resultAllColumnsCreatedStream = Stream.of(
								contactsByCreatedAndFirstName, contactsByCreatedAndLastName,    
								contactsByCreatedAndEmailName, contactsByCreatedAndPhoneName,	contactsByCreatedAndStreetName,
								contactsByCreatedAndPostcodeName, contactsByCreatedAndCountryName, contactsByCreatedAndCityName,
								contactsByCreatedAndDescriptionName)
						  .flatMap(Collection::stream);
				List<Contact> resultAllColumnsCreated = resultAllColumnsCreatedStream.distinct().collect(Collectors.toList());
				logger.info("stream created before removing duplicates:  " +  resultAllColumnsCreated.size());
				
				List<Contact> contactsByUpdatedAndFirstName = contactRepository.findByUserIdAndUpdatedBetweenAndFirstNameIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched);
				List<Contact> contactsByUpdatedAndLastName =  contactRepository.findByUserIdAndUpdatedBetweenAndLastNameIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched);
				List<Contact> contactsByUpdatedAndEmailName =  contactRepository.findByUserIdAndUpdatedBetweenAndEmailIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched);
				List<Contact> contactsByUpdatedAndPhoneName =  contactRepository.findByUserIdAndUpdatedBetweenAndPhoneIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched);
				List<Contact> contactsByUpdatedAndStreetName =  contactRepository.findByUserIdAndUpdatedBetweenAndStreetIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched);
				List<Contact> contactsByUpdatedAndPostcodeName =  contactRepository.findByUserIdAndUpdatedBetweenAndPostcodeIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched);
				List<Contact> contactsByUpdatedAndCityName =  contactRepository.findByUserIdAndUpdatedBetweenAndCityIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched);
				List<Contact> contactsByUpdatedAndCountryName =  contactRepository.findByUserIdAndUpdatedBetweenAndCountryIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched);
				List<Contact> contactsByUpdatedAndDescriptionName =  contactRepository.findByUserIdAndUpdatedBetweenAndDescriptionIgnoreCaseContaining(user.getId(), dateStartZoned, dateEndZoned, searched);
				logger.info("list before stream: " + contactsByUpdatedAndFirstName.size());
				Stream<Contact> resultAllColumnsUpdatedStream = Stream.of(
								contactsByUpdatedAndFirstName, contactsByUpdatedAndLastName,    
								contactsByUpdatedAndEmailName, contactsByUpdatedAndPhoneName,	contactsByUpdatedAndStreetName,
								contactsByUpdatedAndPostcodeName, contactsByUpdatedAndCountryName, contactsByUpdatedAndCityName,
								contactsByUpdatedAndDescriptionName)
						  .flatMap(Collection::stream);
				List<Contact> resultAllColumnsUpdated = resultAllColumnsUpdatedStream.distinct().collect(Collectors.toList());
				logger.info("stream updated before removing duplicates:  " + resultAllColumnsUpdated.size());
				
				List<Contact> resultAllColumnsFiltered = Stream.of(resultAllColumnsCreated, resultAllColumnsUpdated)
						.flatMap(Collection::stream)
//						.sorted(Comparator.comparing(Contact::getCreated).reversed())
						.distinct()
						.limit(number)
						.collect(Collectors.toList());
				
				logger.info("stream after removing duplicates: " + resultAllColumnsFiltered.size());
/*				Stream<Contact> resultAllColumnStream = Stream.of(resultAllColumnsCreated, resultAllColumnsUpdated)
						.flatMap(Collection::stream);
				List<Contact> resultAllColumnsFilteredCreated = resultAllColumnStream
						.filter(contact -> contact.getUpdated() == null)
						.collect(Collectors.toList());
				List<Contact> resultAllColumnsFilteredUpdated = resultAllColumnStream
						.filter(contact -> contact.getUpdated() != null)
						.collect(Collectors.toList());
*/								
		//		List<Contact> resultAllColumns = resultAllColumnStream.distinct().limit(number).collect(Collectors.toList());
				
				return resultAllColumnsFiltered;
		}
		
		
		
		logger.info("case 4d");
		return findByPartialPropertyName(user, field, searched, number); //if dateStart & dateEnd = null, 
																		// then goes to the similar method
																		// but without dates (switch case 3)
	}

	public List<Contact> searchAllColumnsLimitResult(User user, String searched, int number){
		List<Contact> resultFirstName = contactRepository.findAllByUserIdAndFirstName(user.getId(), searched);
		List<Contact> resultLastName = contactRepository.findAllByUserIdAndLastName(user.getId(), searched);
		List<Contact> resultEmail = contactRepository.findAllByUserIdAndEmail(user.getId(), searched);
		List<Contact> resultPhone = contactRepository.findAllByUserIdAndPhone(user.getId(), searched);
		List<Contact> resultStreet = contactRepository.findAllByUserIdAndStreet(user.getId(), searched);
		List<Contact> resultPostcode = contactRepository.findAllByUserIdAndPostcode(user.getId(), searched);
		List<Contact> resultCity = contactRepository.findAllByUserIdAndCity(user.getId(), searched);
		List<Contact> resultCountry = contactRepository.findAllByUserIdAndCountry(user.getId(), searched);
		List<Contact> resultDescription = contactRepository.findAllByUserIdAndDescription(user.getId(), searched);
		
		Stream<Contact> resultAllColumnsStream = Stream.of(resultFirstName, resultLastName, resultEmail, resultPhone,
				resultStreet, resultPostcode, resultCity, resultCountry, resultDescription)
				  .flatMap(Collection::stream);
		List<Contact> resultAllColumns = resultAllColumnsStream.limit(number).distinct().collect(Collectors.toList());
		return resultAllColumns;
	}	
		
	
		
	public List<Contact> filterByUserAndDate(User user, ZonedDateTime dateStart, ZonedDateTime dateEnd, Pageable pageable){
		return contactRepository.findByUserIdAndCreatedBetween(user.getId(), dateStart, dateEnd, pageable);
	}
	
	private  List<Contact> filterByUserId(List<Contact> contacts, User user) {
        List<Contact> resultByUser = new ArrayList<>();
        for (Contact contact : contacts) {
            if (contact.getUser().getId() == user.getId()) {
            	resultByUser.add(contact);
            }
        }
        return resultByUser;
    }
	
	public List<Contact> getAllByUserAndAnyLike(User user, String searched, Pageable pageable){
		return contactRepository.findAllByUserIdAndFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContainingOrEmailIgnoreCaseContainingOrPhoneIgnoreCaseContainingOrStreetIgnoreCaseContainingOrPostcodeIgnoreCaseContainingOrCityIgnoreCaseContainingOrCountryIgnoreCaseContainingOrDescriptionIgnoreCaseContaining
		(user.getId(), searched, searched, searched, searched, searched, searched, searched, searched, searched, pageable);
	}
	
	
	public static ContactProperty propertyFromText(String text) {
		for(ContactProperty p: ContactProperty.values()) {
			if(p.getText().equals(text))
				return p;
		}
		return ContactProperty.FIRST_NAME;
	}
	
		
/*  not used for now	
	public List<Contact> removeListDuplicates(List<Contact> duplicates) {
		List<Contact> noDuplicates = new ArrayList<>(new HashSet<>(duplicates));
		return noDuplicates;
	}
*/	
/*	not used for now
  	List<Contact> listWithoutDuplicates = getAllByUserAndAnyLike(user, searched, pageable).stream()
			     .distinct()
			     .collect(Collectors.toList());
			System.err.println(listWithoutDuplicates.size());
			return listWithoutDuplicates;
 */
	public List<Contact> getAllByUser(User user){
		return contactRepository.findAllByUserId(user.getId());
	}
	
	public List<Contact> getTop10ByUser(User user){
		return contactRepository.findTop10ByUserId(user.getId());
	}
	
			
}


















