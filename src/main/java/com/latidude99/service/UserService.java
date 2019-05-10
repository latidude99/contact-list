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

package com.latidude99.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.latidude99.model.Contact;
import com.latidude99.model.User;
import com.latidude99.model.UserRole;
import com.latidude99.repository.ContactRepository;
import com.latidude99.repository.UserRepository;
import com.latidude99.repository.UserRoleRepository;
import com.latidude99.web.controller.UserController;
//import com.latidude99.web.controller.users;

@Service
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	private static final String DEFAULT_ROLE = "ROLE_USER";
	private static final String ADMIN_ROLE = "ROLE_ADMIN";
	private UserRepository userRepository;
	private UserRoleRepository roleRepository;
	private ContactRepository contactRepository;

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
	private PasswordEncoder passwordEncoder;
	
	public User getUserByUsername(String username) {
		User user = userRepository.findByEmail(username);
		return user;
	}
	
	public User save(User user) {
		userRepository.save(user);
		return user;
	}
	
	public User findByConfirmationToken(String confirmationToken) {
		return userRepository.findByConfirmationToken(confirmationToken);
	}

	public User findById(long id) {
		return userRepository.findById(id);
	}

	public void addWithDefaultRole(User user) {
//		ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
//		user.setZonedTime(currentZonedDateTime);
		String password = user.getPassword();
		user.setPassword(passwordEncoder.encode(password));
		UserRole defaultRole = roleRepository.findByRole(DEFAULT_ROLE);
		user.getRoles().add(defaultRole);
		userRepository.save(user);
		System.out.println("user--> " + user);
	}
	
	public boolean isAvailable(User user) {
		if(userRepository.findByEmail(user.getEmail()) != null){
			return false;
		}else {
			return true;
		}
		
	}
	
	public void addAdminRole() {
		User userAdmin = userRepository.findByEmail("latidude99test@gmail.com");
	    UserRole adminRole = roleRepository.findByRole(ADMIN_ROLE);
//	    UserRole defaultRole = roleRepository.findByRole(DEFAULT_ROLE);
	//    List<Contact> adminContacts = contactRepository.findByUserId(userAdmin.getId());
//	    System.out.println("Contacts (started) before adding to adminUser--> " + adminContacts);
	    ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
		userAdmin.setRegistered(currentZonedDateTime);
		userAdmin.getRoles().add(adminRole);
//		userAdmin.getRoles().add(defaultRole);
	//	userAdmin.addAllContacts(adminContacts);
//	    userAdmin.getContacts().forEach(c -> c.setCreated(currentZonedDateTime));
		String password = userAdmin.getPassword();
		userAdmin.setPassword(passwordEncoder.encode(password));
		userAdmin.setEnabled(true);
//		userAdmin.getContacts().addAll(adminContacts);
//		System.out.println("userAdmin (started) --> " + userAdmin);
		userRepository.save(userAdmin);
	}
	
	public void addDemoUserRole(String name) {
		User userDemo = userRepository.findByFirstName(name.toLowerCase());
	    UserRole defaultRole = roleRepository.findByRole(DEFAULT_ROLE);
	  //  List<Contact> demoContacts = contactRepository.findByUserId(userDemo.getId());
	 //   System.out.println("Contacts (started) before adding to userDemo--> " + demoContacts);
	    ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
		userDemo.setRegistered(currentZonedDateTime);
	    userDemo.getRoles().add(defaultRole);
	 //   userDemo.addAllContacts(demoContacts);
//	    userDemo.getContacts().forEach(c -> c.setCreated(currentZonedDateTime));
	    String password = userDemo.getPassword();
		userDemo.setPassword(passwordEncoder.encode(password));
		userDemo.setEnabled(true);
//		userAdmin.getContacts().addAll(adminContacts);
//		System.out.println("userDemo (started) --> " + userDemo);
		userRepository.save(userDemo);
	}
	
/*	public void updateDetails(User user) {
		userRepository;
	}
*/	
		public List<User> getAllUsersNoAdmins(){
			List<User> usersAll = userRepository.findAll();
			logger.info("usersAll size: " + usersAll.size() + ", " + usersAll.get(0).getRoles().toString());
			logger.info("usersAll size: " + usersAll.size() +  ", " + usersAll.get(1).getRoles().toString());
			UserRole defaultRole = roleRepository.findByRole(DEFAULT_ROLE);
			List<User> usersRoleUser = usersAll.stream()
					.filter(u -> u.getRoles().contains(defaultRole))
					.collect(Collectors.toList());
			logger.info("usersRoleUser size: " + usersRoleUser.size());
			return usersRoleUser;
		}

}


















