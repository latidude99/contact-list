package com.latidude99.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class User implements Serializable{
	private static final long serialVersionUID = 8404628707234409704L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
//	@NotEmpty(message = "{com.latidude99.model.User.firstName.NotEmpty}")
	private String firstName;
//	@NotEmpty(message = "{com.latidude99.model.User.lastName.NotEmpty}")
	private String lastName;
	@NotEmpty(message = "{com.latidude99.model.User.email.NotEmpty}")
	@Email(message = "{com.latidude99.model.User.email.Email}")
	private String email;
	//@NotEmpty(message = "{com.latidude99.model.User.password.NotEmpty}")
	@Size(min=6, message = "{com.latidude99.model.User.password.Size}")
	private String password;
	
	@ManyToMany(cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
	private Set<UserRole> roles = new HashSet<>();
	
//	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch=FetchType.EAGER)
//	@JoinColumn(name = "user_id", referencedColumnName="id")
	@OneToMany(mappedBy = "user", 
			fetch = FetchType.EAGER, 
			cascade = {CascadeType.MERGE, CascadeType.REMOVE }, 
			orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();
	
	private ZonedDateTime registered;
	private ZonedDateTime logged;
	
	@Column(columnDefinition="BOOLEAN DEFAULT false")
	private boolean enabled;
	private String confirmationToken;

	
	@PrePersist
	protected void onCreate() {
		registered = ZonedDateTime.now();
	  }

	@PreUpdate
	protected void onUpdate() {
		logged = ZonedDateTime.now();
	  }
	
	public void addContact(Contact contact) {
        contact.setUser(this);
        getContacts().add(contact);
    }
	
	public void addAllContacts(List<Contact> contacts) {
        contacts.forEach(contact -> contact.setUser(this));
        getContacts().addAll(contacts);
    }
	
	  
	/* setters, getters and toString */
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	public ZonedDateTime getRegistered() {
		return registered;
	}

	public void setRegistered(ZonedDateTime registered) {
		this.registered = registered;
	}

	public ZonedDateTime getLogged() {
		return logged;
	}

	public void setLogged(ZonedDateTime logged) {
		this.logged = logged;
	}
			
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getConfirmationToken() {
		return confirmationToken;
	}

	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", password=" + password + ", roles=" + roles + ", contacts=" + contacts + ", registered="
				+ registered + ", logged=" + logged + "]";
	}

	

}




























