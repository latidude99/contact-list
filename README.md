# Contact List

[![](https://img.shields.io/badge/release-0.9-blue.svg)](https://github.com/latidude99/contact-list/tree/master/release)
[![License: GPL v3](https://img.shields.io/badge/license-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![GitHub issues](https://img.shields.io/badge/issues-open%200-greenred.svg)](https://GitHub.com/latidude99/contact-list/issues/)
[![Maintenance](https://img.shields.io/badge/maintained-no-green.svg)](https://GitHub.com/latidude99/contact-list/graphs/commit-activity)
[![](https://img.shields.io/badge/%20$%20-buy%20me%20a%20coffe-yellow.svg)](https://www.buymeacoffee.com/zWn1I6bVf)


A simple address book in English, Polish, Spanish and Russian.


## Table of contents
* [General Info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Launch](#launch)
* [Features](#features)
* [Status](#status)
* [Screenshots](#screenshots)
* [License](#license)
* [Contact](#contact)


# General Info
My first foray into web apps world. Written after finishing a Spring Framework course mainly to practise what I have learnt
on the course. It is as the name suggests a list or database of contacts with user registration and criteria based 
search functionality. 

You can visit live demo at http://contacts.latidude99.com 

<p align="center">
  <img src="images/contacts_15.jpg" width=100%>
</p>


# Technologies
- Java JDK 1.8
- Maven 3.60
- Spring Boot 1.5.12 with Spring Security 4, Spring Validation and Spring Data
- Thymeleaf 2 with Thymeleaf Extras for Spring Security and Java 8 Time API support
- Bootstrap 3
- Google Maps iFrame
- Hibernate
- MySQL
- Apache Tomcat 8.0
- VPS with Debian 8
- Eclipse / STS 3.9 , switched to IntelliJ IDEA 

**Plugins and libraries**
- Spring Boot Maven Plugin

# Setup

Clone the repository to a folder on your computer and import it in your favourite IDE as a Maven project. It uses Tomcat 8.0 as a web server so you need to set it up in your IDE as well.

### Build

Run maven command: 'mvn clean package'. This should create an executable 'jar' with all libraries needed included.

# Launch
 - you should have MySQL empty database named 'contacts' up and running on your machine. The database should be availabe on the 3306 port although you can change the default settings in the 'application.properties' file
- you also need to change XXX in the 'application.properties' file for your username/password to the database as well as the email address username/password (including spring.mail.host property - for Gmail this would be smtp.gmail.com)
- then, after successful build process navigate to the 'target/' folder of the project and run 'java -jar contacts.jar' command. 
The application should be available at localhost://8080 in your Internet browser
- in windows instead of command line you should also be able to double click the 'contacts.jar' file although you will not get any information about start-up process and its possible failure. You can check in Task Manager if process is running (JVM process).

# Features

### Registration

Two step registration proces
  - registration form with an email address as username and a password with minimum of 6 characters 
    (with a unique username validation check against the database)
  - an email with a confirmation link, user account created on confirmation
    
### Login
  - email addres as USERNAME
  - 'Remember Me' option (may not work properly if 'Open Tabs From Last Time' or similar option is turned on in the browser

### Contacts
The main screen shows the information about who is logged in along with the current date, total number of user’s contacts and number of contacts displayed at the moment. 
You can:
- add, edit and delete contacts
- display address in an embedded Google Maps iFrame for each contacts
- load all records or select records based on search criteria
- search records by: 
  - an exact match of a phrase in one or all contact’s properties (first name / last name / phone number / email / street / 
    postcode / city / country), the result is limited to the specified number
  - a partial match of a phrase in one or all contact’s properties (same as the exact match search)
  - search by contact creation date (specific date or from.. to..) or add the creation date limit to the criteria above
  - display contact's address (if there is any) in a Google Maps iframe with the contact's location marked
- use one of the following batch operations:
  - download and save to a CSV file all contacts
  - download and save to a CSV file selected contacts
  - upload and save to the database contacts in CSV file
  - delete all contacts
  - delete selected contacts
  - restore last deleted contacts
  - check and find duplicate entries
  - remove duplicate entries
  - restore last removed duplicaes

Entries in the database are not deleted when you click on 'Delete' contacts or 'Remove' duplicates buttons, they are only flagged as deleted and sit safely in the database. If you select another group of contacts or duplicates and clicks on the Delete button the flagged contacts are permanently removed from the database and the new group of contacts gets flagged. If you want to restore last deleted entries or removed duplicates, the flags are simply removed and contacts are again visible to you. This buffered approach applies only to batch operations, not individually deleted records directly in the contacts table.

If you delete contacts individually there is no 'undo' option. However, the delete process has two stages so you can see the contact's details before comitting it to be removed prmanently.

The application takes advantage of Spring Boot’s internationalization (i18n) capabilities and is available in four languages: English, Polish, Spanish and Russian.

There are quite a few things I could and would like to improve on or add to this application but I had to draw a line somewhere and move on to the next project.

# Status
Closed

# Screenshots
<p align="center">
  <img src="images/contacts_00.jpg" width=100%>
  <img src="images/contacts_01.jpg" width=100%>
  <img src="images/contacts_02.jpg" width=100%>
  <img src="images/contacts_03.jpg" width=100%>
  <img src="images/contacts_04.jpg" width=100%>
  <img src="images/contacts_05.jpg" width=100%>
  <img src="images/contacts_06.jpg" width=100%>
  <img src="images/contacts_07.jpg" width=100%>
  <img src="images/contacts_08.jpg" width=100%>
  <img src="images/contacts_09.jpg" width=100%>
  <img src="images/contacts_10.jpg" width=100%>
  <img src="images/contacts_11.jpg" width=100%>
  <img src="images/contacts_12.jpg" width=100%>
  <img src="images/contacts_13.jpg" width=100%>
  <img src="images/contacts_14.jpg" width=100%>
  <img src="images/contacts_15.jpg" width=100%>
  <img src="images/contacts_16.jpg" width=100%>
  <img src="images/contacts_17.jpg" width=100%>
  <img src="images/contacts_18.jpg" width=100%>
</p>

# License
Contact List is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
Contact List is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with Contact List.  If not, see http://www.gnu.org/licenses/ or write to: latidude99@gmail.com

# Contact
You can email me at latidude99@gmail.com











