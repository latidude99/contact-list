package com.latidude99;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class ContactsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContactsApplication.class, args);
		
		//System.out.println(Arrays.asList(Locale.getAvailableLocales()));
		//Locale.setDefault(new Locale("pl_PL"));
	}
}
