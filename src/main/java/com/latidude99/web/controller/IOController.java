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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.latidude99.model.Contact;
import com.latidude99.model.ContactWrapper;
import com.latidude99.model.FromView;
import com.latidude99.model.User;
import com.latidude99.service.ContactService;
import com.latidude99.service.UserService;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

/*
 * Responsible for batch operations in TAB Backup, Restore & Cleanup
 * 'Selected' refers to contacts loaded in the leftpanel (TAB Contacts)
 */
@Controller
public class IOController {
    private static final Logger logger = LoggerFactory.getLogger(IOController.class);

    @Autowired
    UserService userService;

    @Autowired
    ContactService contactService;

    @Autowired
    FromView fromView;

    @Autowired
    ContactWrapper contactWrapper;


    @ModelAttribute("fromView")
    public FromView getFromView() {
        return fromView;
    }

    @ModelAttribute("contactWrapper")
    public ContactWrapper getContactWrapper() {
        return contactWrapper;
    }

    @PostMapping("/clearBuffers")
    public String clearBuffers(@ModelAttribute("contactWrapper") ContactWrapper contactWrapper,
                               HttpServletResponse response, Principal principal) throws IOException {
        clearResult();
        contactWrapper.setDeletedList(new ArrayList<>());
        contactWrapper.setDuplicatedList(new ArrayList<>());
        contactWrapper.setRemovedList(new ArrayList<>());
        fromView.setBulkOpsSwitch("0");
        return "redirect:/contacts.html";
    }

    @PostMapping("/clearSelectedContacts")
    public String clearSelectedContacts(HttpServletResponse response, Principal principal) throws IOException {
        clearResult();
        fromView.setBulkOpsSwitch("0");
        return "redirect:/contacts.html";
    }


    @PostMapping("/downloadAllCSV")
    public void downloadAllCSV(HttpServletResponse response, Principal principal) {
        try {
            String currentUserName = principal.getName();
            User currentUser = userService.getUserByUsername(currentUserName);
            List<Contact> currentUserContacts = contactService.getAllByUser(currentUser);

            String csvFileName = "contacts.csv";
            response.setContentType("text/csv; charset=UTF-8");
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
            response.setHeader(headerKey, headerValue);

            // uses the Super CSV API to generate CSV data from the model data
            ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

            String[] header =
                    {"firstName", "lastName", "email", "phone", "street", "postcode", "city", "country", "description"};
            csvWriter.writeHeader(header);
            for (Contact c : currentUserContacts) {
                csvWriter.write(c, header);
            }
            csvWriter.close();
            fromView.setBulkOpsSwitch("downloadOK");
        } catch (IOException e) {
            fromView.setBulkOpsSwitch("downloadError");
        }
    }

    @PostMapping("/downloadSelectedCSV")
    public void downloadSelectedCSV(@ModelAttribute("resultList") List<Contact> contactList,
                                    HttpServletResponse response, Principal principal) {
        try {
            String csvFileName = "contacts.csv";
            response.setContentType("text/csv; charset=UTF-8");
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
            response.setHeader(headerKey, headerValue);

            // uses the Super CSV API to generate CSV data from the model data
            ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

            String[] header =
                    {"firstName", "lastName", "email", "phone", "street", "postcode", "city", "country", "description"};
            csvWriter.writeHeader(header);
            for (Contact c : contactList) {
                csvWriter.write(c, header);
            }
            csvWriter.close();
            fromView.setBulkOpsSwitch("downloadOK");
        } catch (IOException e) {
            fromView.setBulkOpsSwitch("downloadError");
        }
    }

    @PostMapping("/uploadCSV")
    public String uploadCSV(@ModelAttribute("fileUpload") MultipartFile fileUpload,
                            Principal principal, RedirectAttributes redirect) {
        final String[] CSV_HEADER =
                {"firstName", "lastName", "email", "phone", "street", "postcode", "city", "country", "description"};

        String currentUserName = principal.getName();
        User currentUser = userService.getUserByUsername(currentUserName);

        List<String> contactsUploadedTokens = new ArrayList<>();

        List<String> uniqueTokensToRollBack = new ArrayList<>();
        CsvToBean<Contact> csvToBean = null;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(fileUpload.getInputStream(), "UTF-8"))) {
            ColumnPositionMappingStrategy<Contact> mappingStrategy = new ColumnPositionMappingStrategy<Contact>();
            mappingStrategy.setType(Contact.class);
            mappingStrategy.setColumnMapping(CSV_HEADER);
            csvToBean = new CsvToBeanBuilder<Contact>(br)
                    .withMappingStrategy(mappingStrategy)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<Contact> contacts = csvToBean.parse();
            for (Contact contact : contacts) {
                System.out.println(contact);
                String uniqueToken = UUID.randomUUID().toString();
                contact.setUser(currentUser);
                contact.setCreated(ZonedDateTime.now());
                contact.setUniqueToken(uniqueToken);
                uniqueTokensToRollBack.add(uniqueToken);
                contact.setEmail(contact.getEmail().trim().replace(" ", ""));
                contactService.saveContact(contact);
                contactsUploadedTokens.add(uniqueToken);
            }
            fromView.setBulkOpsSwitch("uploadOK");
        } catch (Exception e) {
            contactService.removeByTokens(uniqueTokensToRollBack);
            fromView.setBulkOpsSwitch("uploadError");
        }
        return "redirect:/contacts.html";
    }

    @PostMapping("/deleteAll")
    public String deleteAll(@ModelAttribute("currentUser") User currentUserFromView,
                            Model model, Principal principal) {
        String currentUserName = principal.getName();
        if (currentUserName.equals(currentUserFromView.getEmail())) {
            User currentUser = userService.getUserByUsername(currentUserName);
            List<Contact> deleteList = contactService.getAllByUser(currentUser);
            contactWrapper.setDeletedList(deleteList);
            contactService.flagAsDeletedAndRemoveLast(deleteList);
            clearResult();
            fromView.setBulkOpsSwitch("deleteOK");
            contactWrapper.setDeletedList(contactService.getFlaggedDeleted());
            return "redirect:/contacts.html";
        } else {
            fromView.setBulkOpsSwitch("deleteFailed");
        }
        return "redirect:/contacts.html";
    }

    @PostMapping("/deleteSelected")
    public String deleteSelected(@ModelAttribute("contactWrapper") ContactWrapper contactWrapper,
                                 Model model, Principal principal) {
        try {
            List<Contact> deleteList = removeNulls(contactWrapper.getResultList());
            contactWrapper.setDeletedList(deleteList);
            contactService.flagAsDeletedAndRemoveLast(deleteList);
            clearResult();
            contactWrapper.setDuplicatedList(new ArrayList<>());
            fromView.setBulkOpsSwitch("deleteOK");
            contactWrapper.setDeletedList(contactService.getFlaggedDeleted());
            return "redirect:/contacts.html";
        } catch (Exception e) {
            fromView.setBulkOpsSwitch("deleteError");
        }
        return "redirect:/contacts.html";
    }

    @PostMapping("/undoLastDelete")
    public String undoLastDelete(@ModelAttribute("contactWrapper") ContactWrapper contactWrapper,
                                 Model model, Principal principal) {
        String currentUserName = principal.getName();
        User currentUser = userService.getUserByUsername(currentUserName);
        List<Contact> flaggedDeleted = contactService.getFlaggedDeleted();
        if (flaggedDeleted.size() > 0) {
            for (Contact c : flaggedDeleted) {
                c.setUser(currentUser);
                c.setEmail(c.getEmail().trim().replace(" ", ""));
                c.setDeleted("0");
                contactService.saveContact(c);
            }
            contactWrapper.setDeletedList(new ArrayList<>());
            fromView.setBulkOpsSwitch("restoreDeletedOK");
            return "redirect:/contacts.html";
        } else {
            fromView.setBulkOpsSwitch("restoreDeletedError");
        }
        return "redirect:/contacts.html";
    }

    @PostMapping("/checkDuplicates")
    public String checkDuplicates(Model model, Principal principal) {
        try {
            String currentUserName = principal.getName();
            User currentUser = userService.getUserByUsername(currentUserName);
            Set<Contact> duplicatedContactsRemovedSet = new HashSet<>();
            List<Contact> allContacts = contactService.getAllByUser(currentUser);
            Set<Contact> duplicatedContactsSet = allContacts.stream()
                    .filter(c -> !"1".equals(c.getDuplicated()))
                    .filter(c -> !duplicatedContactsRemovedSet.add(c))
                    .collect(Collectors.toSet());
            List<Contact> duplicatedContactList = new ArrayList<>(duplicatedContactsSet);
            contactWrapper.setDuplicatedList(removeNulls(duplicatedContactList));
            contactWrapper.setResultList(removeNulls(duplicatedContactList));
            fromView.setBulkOpsSwitch("duplicatesCheckOK");
            return "redirect:/contacts.html";
        } catch (Exception e) {
            fromView.setBulkOpsSwitch("duplicatesCheckError");
        }
        return "redirect:/contacts.html";

    }

    @PostMapping("/removeDuplicates")
    public String removeDuplicates(@ModelAttribute("contactWrapper") ContactWrapper contactWrapper,
                                   Model model, Principal principal) {
        try {
            List<Contact> duplicatedContactList = removeNulls(contactWrapper.getDuplicatedList());
            contactWrapper.setRemovedList(duplicatedContactList);
            contactService.flagAsDuplicatedAndRemoveLast(duplicatedContactList);
            clearResult();
            contactWrapper.setDuplicatedList(new ArrayList<>());
            contactWrapper.setRemovedList(contactService.getFlaggedDuplicated());
            fromView.setBulkOpsSwitch("duplicatesRemoveOK");
            return "redirect:/contacts.html";
        } catch (Exception e) {
            fromView.setBulkOpsSwitch("duplicatesRemoveError");
        }
        return "redirect:/contacts.html";
    }

    @PostMapping("/undoRemovedDuplicates")
    public String undoRemovedDuplicates(@ModelAttribute("contactWrapper") ContactWrapper contactWrapper, Model model, Principal principal) {
        String currentUserName = principal.getName();
        User currentUser = userService.getUserByUsername(currentUserName);
        List<Contact> flaggedDuplicated = contactService.getFlaggedDuplicated();
        if (flaggedDuplicated.size() > 0) {
            for (Contact c : flaggedDuplicated) {
                c.setUser(currentUser);
                c.setEmail(c.getEmail().trim().replace(" ", ""));
                c.setDuplicated("0");
                contactService.saveContact(c);
            }
            contactWrapper.setDuplicatedList(new ArrayList<>());
            contactWrapper.setRemovedList(new ArrayList<>());
            clearResult();
            fromView.setBulkOpsSwitch("restoreDuplicatesOK");
            return "redirect:/contacts.html";
        } else {
            fromView.setBulkOpsSwitch("restoreDuplicatesError");
        }
        return "redirect:/contacts.html";
    }


    private void clearResult() {
        contactWrapper.setResultList(new ArrayList<>());
        fromView.setResult1(0);
        fromView.setResult2(0);
        fromView.setResult3(0);
        fromView.setResult4(0);
    }

    public List<Contact> removeNulls(List<Contact> listNullsPossible) {
        List<Contact> listNoNulls = listNullsPossible.stream().filter(Objects::nonNull).collect(Collectors.toList());
        return listNoNulls;
    }

}






