package com.latidude99.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.latidude99.model.Contact;

@Repository
public interface ContactPageRepository extends PagingAndSortingRepository<Contact, Long>{
	
//	Page<Contact> contactList(Pageable pageable);

}
