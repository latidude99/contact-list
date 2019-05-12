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

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final String DEFAULT_ROLE = "ROLE_USER";
    private static final String ADMIN_ROLE = "ROLE_ADMIN";
    private UserRepository userRepository;
    private UserRoleRepository roleRepository;
    private ContactRepository contactRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        UserRole defaultRole = roleRepository.findByRole(DEFAULT_ROLE);
        user.getRoles().add(defaultRole);
        userRepository.save(user);
    }

    public boolean isAvailable(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return false;
        } else {
            return true;
        }

    }

    /*
     * Add administator role to a user defined in data.sql file
     * loaded  to the database at the application start
     */
    public void addAdminRole() {
        User userAdmin = userRepository.findByEmail("latidude99test@gmail.com");
        UserRole adminRole = roleRepository.findByRole(ADMIN_ROLE);
        ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
        userAdmin.setRegistered(currentZonedDateTime);
        userAdmin.getRoles().add(adminRole);
        String password = userAdmin.getPassword();
        userAdmin.setPassword(passwordEncoder.encode(password));
        userAdmin.setEnabled(true);
        userRepository.save(userAdmin);
    }

    /*
     * Add user role to a user defined in data.sql file
     * loaded  to the database at the application start
     */
    public void addDemoUserRole(String name) {
        User userDemo = userRepository.findByFirstName(name.toLowerCase());
        UserRole defaultRole = roleRepository.findByRole(DEFAULT_ROLE);
        ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
        userDemo.setRegistered(currentZonedDateTime);
        userDemo.getRoles().add(defaultRole);
        String password = userDemo.getPassword();
        userDemo.setPassword(passwordEncoder.encode(password));
        userDemo.setEnabled(true);
        userRepository.save(userDemo);
    }


    public List<User> getAllUsersNoAdmins() {
        List<User> usersAll = userRepository.findAll();
        logger.info("usersAll size: " + usersAll.size() + ", " + usersAll.get(0).getRoles().toString());
        logger.info("usersAll size: " + usersAll.size() + ", " + usersAll.get(1).getRoles().toString());
        UserRole defaultRole = roleRepository.findByRole(DEFAULT_ROLE);
        List<User> usersRoleUser = usersAll.stream()
                .filter(u -> u.getRoles().contains(defaultRole))
                .collect(Collectors.toList());
        logger.info("usersRoleUser size: " + usersRoleUser.size());
        return usersRoleUser;
    }

}


















