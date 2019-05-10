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

package com.latidude99.repository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.latidude99.model.Contact;
import com.latidude99.model.User;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>{
	
	Contact findByEmail(String email);
	
	Contact findById(long id);
	
	Contact findByUniqueToken(String uniqueToken);
	
	List<Contact> findAll();
	
	List<Contact> findByDescription(String desc);
	List<Contact> findByUserId(Long id);
	
	List<Contact> findByDeleted(String deleted);
	List<Contact> findByDuplicated(String duplicated);
	
	List<Contact> findByDeletedAndDuplicated(String deleted, String duplicated);
	
	void removeContactById(long id);
	
	void removeAllByUser(User user);
	
	
	
	// for ContactController switch case 1
	List<Contact> findAllByUserIdAndFirstName(Long userId, String firstName);
	List<Contact> findAllByUserIdAndLastName(Long userId, String lastName);
	List<Contact> findAllByUserIdAndEmail(Long userId, String lastName);
	List<Contact> findAllByUserIdAndPhone(Long userId, String phone);
	List<Contact> findAllByUserIdAndStreet(Long userId, String street);
	List<Contact> findAllByUserIdAndPostcode(Long userId, String postcode);
	List<Contact> findAllByUserIdAndCity(Long userId, String city);
	List<Contact> findAllByUserIdAndCountry(Long userId, String country);
	List<Contact> findAllByUserIdAndDescription(Long userId, String description);
	
	// for ContactController switch case 2	
	List<Contact> findAllByUserIdAndFirstName(Long userId, String firstName, Pageable pageable);
	List<Contact> findAllByUserIdAndLastName(Long userId, String lastName, Pageable pageable);
	List<Contact> findAllByUserIdAndEmail(Long userId, String lastName, Pageable pageable);
	List<Contact> findAllByUserIdAndPhone(Long userId, String phone, Pageable pageable);
	List<Contact> findAllByUserIdAndStreet(Long userId, String street, Pageable pageable);
	List<Contact> findAllByUserIdAndPostcode(Long userId, String postcode, Pageable pageable);
	List<Contact> findAllByUserIdAndCity(Long userId, String city, Pageable pageable);
	List<Contact> findAllByUserIdAndCountry(Long userId, String country, Pageable pageable);
	List<Contact> findAllByUserIdAndDescription(Long userId, String description, Pageable pageable);
	
	// for ContactController switch case 3	
	List<Contact> findAllByUserIdAndFirstNameIgnoreCaseContaining(Long userId, String firstName, Pageable pageable);
	List<Contact> findAllByUserIdAndLastNameIgnoreCaseContaining(Long userId, String lastName, Pageable pageable);
	List<Contact> findAllByUserIdAndEmailIgnoreCaseContaining(Long userId, String lastName, Pageable pageable);
	List<Contact> findAllByUserIdAndPhoneIgnoreCaseContaining(Long userId, String phone, Pageable pageable);
	List<Contact> findAllByUserIdAndStreetIgnoreCaseContaining(Long userId, String street, Pageable pageable);
	List<Contact> findAllByUserIdAndPostcodeIgnoreCaseContaining(Long userId, String postcode, Pageable pageable);
	List<Contact> findAllByUserIdAndCityIgnoreCaseContaining(Long userId, String city, Pageable pageable);
	List<Contact> findAllByUserIdAndCountryIgnoreCaseContaining(Long userId, String country, Pageable pageable);
	List<Contact> findAllByUserIdAndDescriptionIgnoreCaseContaining(Long userId, String description, Pageable pageable);
	
	List<Contact> findAllByUserIdAndFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContainingOrEmailIgnoreCaseContainingOrPhoneIgnoreCaseContainingOrStreetIgnoreCaseContainingOrPostcodeIgnoreCaseContainingOrCityIgnoreCaseContainingOrCountryIgnoreCaseContainingOrDescriptionIgnoreCaseContaining
				(Long userId, String firstName, String lastName, String email,
						String phone, String street, String postcode, String city, 
						String country, String description, Pageable pageable);
	
	Page<Contact> findAllByUserId(Long userId, Pageable pageable);
	
	List<Contact> findTop10ByUserId(Long userId);
	
	List<Contact> findAllByUserId(Long userId);

	
//	@Query("SELECT c FROM Contact c WHERE c.firstName=:firstName")
//	List<Contact> findTop10ByFirstName(@Param("firstName") String firstName);
	
	Integer countByUserIdAndDeletedAndDuplicated(Long userId, String deleted, String duplicated);
	
	// for ContactController switch case 4
	List<Contact> findByUserIdAndCreatedBetween(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, Pageable pageable);
	
	List<Contact> findByUserIdAndCreatedBetweenAndFirstNameIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String firstName, Pageable pageable);
	List<Contact> findByUserIdAndCreatedBetweenAndLastNameIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String lastName, Pageable pageable);
	List<Contact> findByUserIdAndCreatedBetweenAndPhoneIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String phone, Pageable pageable);
	List<Contact> findByUserIdAndCreatedBetweenAndEmailIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String email, Pageable pageable);
	List<Contact> findByUserIdAndCreatedBetweenAndStreetIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String street, Pageable pageable);
	List<Contact> findByUserIdAndCreatedBetweenAndPostcodeIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String postcode, Pageable pageable);
	List<Contact> findByUserIdAndCreatedBetweenAndCityIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String city, Pageable pageable);
	List<Contact> findByUserIdAndCreatedBetweenAndCountryIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String city, Pageable pageable);
	List<Contact> findByUserIdAndCreatedBetweenAndDescriptionIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String description, Pageable pageable);

	List<Contact> findByUserIdAndUpdatedBetweenAndFirstNameIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String firstName, Pageable pageable);
	List<Contact> findByUserIdAndUpdatedBetweenAndLastNameIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String lastName, Pageable pageable);
	List<Contact> findByUserIdAndUpdatedBetweenAndPhoneIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String phone, Pageable pageable);
	List<Contact> findByUserIdAndUpdatedBetweenAndEmailIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String email, Pageable pageable);
	List<Contact> findByUserIdAndUpdatedBetweenAndStreetIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String street, Pageable pageable);
	List<Contact> findByUserIdAndUpdatedBetweenAndPostcodeIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String postcode, Pageable pageable);
	List<Contact> findByUserIdAndUpdatedBetweenAndCityIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String city, Pageable pageable);
	List<Contact> findByUserIdAndUpdatedBetweenAndCountryIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String city, Pageable pageable);
	List<Contact> findByUserIdAndUpdatedBetweenAndDescriptionIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String description, Pageable pageable);

	
	List<Contact> findByUserIdAndCreatedBetweenAndFirstNameIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String firstName);
	List<Contact> findByUserIdAndCreatedBetweenAndLastNameIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String lastName);
	List<Contact> findByUserIdAndCreatedBetweenAndPhoneIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String phone);
	List<Contact> findByUserIdAndCreatedBetweenAndEmailIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String email);
	List<Contact> findByUserIdAndCreatedBetweenAndStreetIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String street);
	List<Contact> findByUserIdAndCreatedBetweenAndPostcodeIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String postcode);
	List<Contact> findByUserIdAndCreatedBetweenAndCityIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String city);
	List<Contact> findByUserIdAndCreatedBetweenAndCountryIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String city);
	List<Contact> findByUserIdAndCreatedBetweenAndDescriptionIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String description);

	List<Contact> findByUserIdAndUpdatedBetweenAndFirstNameIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String firstName);
	List<Contact> findByUserIdAndUpdatedBetweenAndLastNameIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String lastName);
	List<Contact> findByUserIdAndUpdatedBetweenAndPhoneIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String phone);
	List<Contact> findByUserIdAndUpdatedBetweenAndEmailIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String email);
	List<Contact> findByUserIdAndUpdatedBetweenAndStreetIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String street);
	List<Contact> findByUserIdAndUpdatedBetweenAndPostcodeIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String postcode);
	List<Contact> findByUserIdAndUpdatedBetweenAndCityIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String city);
	List<Contact> findByUserIdAndUpdatedBetweenAndCountryIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String city);
	List<Contact> findByUserIdAndUpdatedBetweenAndDescriptionIgnoreCaseContaining(Long userId, ZonedDateTime dateStart, ZonedDateTime dateEnd, String description);
}




















