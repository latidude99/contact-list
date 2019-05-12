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

package com.latidude99.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.latidude99.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByFirstName(String firstName);

    User findByConfirmationToken(String confirmationToken);

    User findById(long id);

    List<User> findAll();

}
