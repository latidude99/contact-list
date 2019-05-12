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

package com.latidude99.web.controller;

import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.latidude99.model.Contact;
import com.latidude99.model.ContactWrapper;
import com.latidude99.model.FromView;
import com.latidude99.model.User;
import com.latidude99.repository.ContactRepository;
import com.latidude99.service.ContactService;
import com.latidude99.service.UserService;

@Controller
@ControllerAdvice
public class ContactController {
    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);
    private static final String DEFAULT_ROLE = "ROLE_USER";
    private static final String ADMIN_ROLE = "ROLE_ADMIN";

    @Autowired
    UserService userService;

    @Autowired
    ContactService contactService;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    FromView fromView;

    @Autowired
    ContactWrapper contactWrapper;

    /*
     * Exchange object used in multiple Thymeleaf forms to submit information
     */
    @ModelAttribute("fromView")
    public FromView getFromView() {
        return fromView;
    }

    @ModelAttribute("contactWrapper")
    public ContactWrapper getContactWrapper() {
        return contactWrapper;
    }


    @GetMapping("/contacts")
    public String contacts(@ModelAttribute User currentUserRe, @ModelAttribute FromView fromView,
                           Model model, Principal principal, HttpServletRequest request) {
        clearResult(); // clears initial object with contacts viewed by other users
        ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
        model.addAttribute("currentZonedDateTime", currentZonedDateTime);
        model.addAttribute("currentZone", currentZonedDateTime.getZone());
        String currentUserName = principal.getName();
        User currentUser = userService.getUserByUsername(currentUserName);
        model.addAttribute("currentUser", currentUser);
        Integer contactsTotalByUser = contactService.getTotalByUser(currentUser);
        model.addAttribute("contactsTotalByUser", contactsTotalByUser);
        logger.info("GET/contacts - deletedList: " + contactWrapper.getDeletedList().toString());
        if (request.isUserInRole(ADMIN_ROLE)) {
            List<User> users = userService.getAllUsersNoAdmins();
            model.addAttribute("users", users);
            logger.info("users size: " + users.size());
        }
        Contact contact = new Contact();
        model.addAttribute("contact", contact);
        model.addAttribute("noContacts", "yes");
        fromView.setBulkOpsSwitch("0");
        return "contacts";
    }

    @PostMapping("/contacts")
    public String contactsSorted(@ModelAttribute @Valid FromView fromView, BindingResult result,
                                 @ModelAttribute("contactWrapperDeleted") ContactWrapper contactWrapperDeleted,
                                 Model model, Principal principal, HttpServletRequest request) {
        if (result.hasErrors()) {
            ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
            model.addAttribute("currentZonedDateTime", currentZonedDateTime);
            model.addAttribute("currentZone", currentZonedDateTime.getZone());
            String currentUserName = principal.getName();
            User currentUser = userService.getUserByUsername(currentUserName);
            model.addAttribute("currentUser", currentUser);
            Integer contactsTotalByUser = contactService.getTotalByUser(currentUser);
            model.addAttribute("contactsTotalByUser", contactsTotalByUser);
            Contact contact = new Contact();
            model.addAttribute("contact", contact);
            model.addAttribute("noContacts", "yes");
            return "contacts";
        } else {
            ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
            model.addAttribute("currentZonedDateTime", currentZonedDateTime);
            model.addAttribute("currentZone", currentZonedDateTime.getZone());
            String currentUserName = principal.getName();
            User currentUser = userService.getUserByUsername(currentUserName);
            model.addAttribute("currentUser", currentUser);
            Integer contactsTotalByUser = contactService.getTotalByUser(currentUser);
            model.addAttribute("contactsTotalByUser", contactsTotalByUser);
            if (request.isUserInRole(ADMIN_ROLE)) {
                List<User> users = userService.getAllUsersNoAdmins();
                model.addAttribute("users", users);
                logger.info("users size: " + users.size());
            }
            List<Contact> resultContactsBeforeFlagsRemoved;
            String selector = fromView.getSelector();
            switch (selector) {
                case "1":
                    resultContactsBeforeFlagsRemoved = contactService.findNByColumnSortedBy(
                            currentUser, fromView.getResult1(), fromView.getSortBy(), fromView.getDirection());
                    if (resultContactsBeforeFlagsRemoved.size() < 1) {
                        model.addAttribute("noContacts", "yes");
                    } else {
                        model.addAttribute("noContacts", "no");
                    }
                    break;
                case "2":
                    resultContactsBeforeFlagsRemoved = contactService.findByPropertyName(
                            currentUser, fromView.getFindBy(), fromView.getSearchFor2().trim(), fromView.getResult2());
                    if (resultContactsBeforeFlagsRemoved.size() < 1) {
                        model.addAttribute("noContacts", "yes");
                    } else {
                        model.addAttribute("noContacts", "no");
                    }
                    break;
                case "3":
                    resultContactsBeforeFlagsRemoved = contactService.findByPartialPropertyName
                            (currentUser, fromView.getFindBy(), fromView.getSearchFor3().trim(), fromView.getResult3());
                    if (resultContactsBeforeFlagsRemoved.size() < 1) {
                        model.addAttribute("noContacts", "yes");
                    } else {
                        model.addAttribute("noContacts", "no");
                    }
                    break;
                case "4":
                    resultContactsBeforeFlagsRemoved = contactService.findByDate(
                            currentUser, fromView.getFindBy(), fromView.getSearchFor4().trim(),
                            fromView.getResult4(), fromView.getDateStartTxt(), fromView.getDateEndTxt());
                    if (resultContactsBeforeFlagsRemoved.size() < 1) {
                        model.addAttribute("noContacts", "yes");
                    } else {
                        model.addAttribute("noContacts", "no");
                    }
                    break;
                default:
                    resultContactsBeforeFlagsRemoved = contactService.getTop10ByUser(currentUser);
                    if (resultContactsBeforeFlagsRemoved.size() < 1) {
                        model.addAttribute("noContacts", "yes");
                    } else {
                        model.addAttribute("noContacts", "no");
                    }
            }
            logger.info("/contacts - resultContactsBeforeFlagsRemoved: " +
                    resultContactsBeforeFlagsRemoved.size() + resultContactsBeforeFlagsRemoved.toString());
            List<Contact> resultContacts = resultContactsBeforeFlagsRemoved.stream()
                    .filter(c -> !"1".equals(c.getDeleted()))
                    .filter(c -> !"1".equals(c.getDuplicated()))
                    .collect(Collectors.toList());
            logger.info("/contacts - resultContacts: " + resultContacts.size() + resultContacts.toString());
            if (resultContacts.size() > 0) {
                contactWrapper.setResultList(resultContacts);
                Contact contact = new Contact();
                model.addAttribute("contact", contact);
                logger.info("Contacts listed ok/POST");
                return "contacts";
            } else {
                model.addAttribute("noContacts",
                        "You don't have any contacts loaded yet.");
                Contact contact = new Contact();
                model.addAttribute("contact", contact);
                model.addAttribute("noContacts", "yes");
                logger.info("Contacts not listed - user doesn't have any");
            }
        }
        fromView.setBulkOpsSwitch("0");
        return "contacts";
    }

    @GetMapping("/addContact")
    public String addContact(Model model, Principal principal) {
        ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
        model.addAttribute("currentZonedDateTime", currentZonedDateTime);
        model.addAttribute("currentZone", currentZonedDateTime.getZone());
        String currentUserName = principal.getName();
        User currentUser = userService.getUserByUsername(currentUserName);
        model.addAttribute("currentUser", currentUser);
        Integer contactsTotalByUser = contactService.getTotalByUser(currentUser);
        model.addAttribute("contactsTotalByUser", contactsTotalByUser);
        List<Contact> resultContacts = new ArrayList<>();
        model.addAttribute("currentUserContacts", resultContacts);
        Contact contact = new Contact();
        model.addAttribute("contact", contact);
        return "addContact";
    }

    @PostMapping("/addContact")
    public String saveContact(@ModelAttribute @Valid Contact contact, BindingResult result,
                              Principal principal, Model model) {
        if (result.hasErrors()) {
            ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
            model.addAttribute("currentZonedDateTime", currentZonedDateTime);
            model.addAttribute("currentZone", currentZonedDateTime.getZone());
            String currentUserName = principal.getName();
            User currentUser = userService.getUserByUsername(currentUserName);
            model.addAttribute("currentUser", currentUser);
            Integer contactsTotalByUser = contactService.getTotalByUser(currentUser);
            model.addAttribute("contactsTotalByUser", contactsTotalByUser);
            List<Contact> resultContacts = new ArrayList<>();
            model.addAttribute("currentUserContacts", resultContacts);
            return "addContact";
        } else {
            ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
            model.addAttribute("currentZonedDateTime", currentZonedDateTime);
            model.addAttribute("currentZone", currentZonedDateTime.getZone());
            String currentUserName = principal.getName();
            User currentUser = userService.getUserByUsername(currentUserName);
            Integer contactsTotalByUser = contactService.getTotalByUser(currentUser);
            model.addAttribute("contactsTotalByUser", contactsTotalByUser);
            List<Contact> resultContacts = new ArrayList<>();
            model.addAttribute("currentUserContacts", resultContacts);
            contact.setCreated(currentZonedDateTime);
            contact.setDeleted("0");
            currentUser.addContact(contact);
            userService.save(currentUser);
            logger.info("Contact added");
        }
        return "redirect:contacts";
    }

    @RequestMapping(value = "/deleteContact", method = RequestMethod.POST)
    public String deleteContact(@ModelAttribute Contact contact, Principal principal, Model model) {
        ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
        model.addAttribute("currentZonedDateTime", currentZonedDateTime);
        model.addAttribute("currentZone", currentZonedDateTime.getZone());
        String currentUserName = principal.getName();
        User currentUser = userService.getUserByUsername(currentUserName);
        model.addAttribute("currentUser", currentUser);
        Integer contactsTotalByUser = contactService.getTotalByUser(currentUser);
        model.addAttribute("contactsTotalByUser", contactsTotalByUser);
        List<Contact> resultContacts = new ArrayList<>();
        model.addAttribute("currentUserContacts", resultContacts);
        contact = contactRepository.findById(contact.getId());
        model.addAttribute("contact", contact);
        return "deleteContact";
    }

    @RequestMapping(value = "/eraseContact", method = RequestMethod.POST)
    public String eraseContact(@ModelAttribute Contact contact) {
        contactService.deleteContact(contact.getId());
        logger.info("Contact deleted");
        return "redirect:/contacts";
    }

    @RequestMapping(value = "/editContact", method = RequestMethod.POST)
    public String editContact(@ModelAttribute Contact contact, Principal principal, Model model) {
        ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
        model.addAttribute("currentZonedDateTime", currentZonedDateTime);
        model.addAttribute("currentZone", currentZonedDateTime.getZone());
        String currentUserName = principal.getName();
        User currentUser = userService.getUserByUsername(currentUserName);
        model.addAttribute("currentUser", currentUser);
        Integer contactsTotalByUser = contactService.getTotalByUser(currentUser);
        model.addAttribute("contactsTotalByUser", contactsTotalByUser);
        List<Contact> resultContacts = new ArrayList<>();
        model.addAttribute("currentUserContacts", resultContacts);
        contact = contactRepository.findById(contact.getId());
        model.addAttribute("contact", contact);
        return "editContact";
    }

    @PostMapping("/updateContact")
    public String updateContact(@ModelAttribute @Valid Contact contact, BindingResult result,
                                Principal principal, Model model) {
        if (result.hasErrors()) {
            ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
            model.addAttribute("currentZonedDateTime", currentZonedDateTime);
            model.addAttribute("currentZone", currentZonedDateTime.getZone());
            String currentUserName = principal.getName();
            User currentUser = userService.getUserByUsername(currentUserName);
            model.addAttribute("currentUser", currentUser);
            Integer contactsTotalByUser = contactService.getTotalByUser(currentUser);
            model.addAttribute("contactsTotalByUser", contactsTotalByUser);
            List<Contact> resultContacts = new ArrayList<>();
            model.addAttribute("currentUserContacts", resultContacts);
            return "editContact";
        } else {
            String currentUserName = principal.getName();
            User currentUser = userService.getUserByUsername(currentUserName);
            model.addAttribute("currentUser", currentUser);
            currentUser.addContact(contact);
            contactService.updateContact(contact);
            logger.info("Contact updated");
            return "redirect:contacts";
        }
    }

    private void clearResult() {
        contactWrapper.setResultList(new ArrayList<>());
        fromView.setResult1(0);
        fromView.setResult2(0);
        fromView.setResult3(0);
        fromView.setResult4(0);
    }

}















