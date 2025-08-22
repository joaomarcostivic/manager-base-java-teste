package com.tivic.manager.print;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import sol.util.Result;

public class EMLServices {
	
	 public static Result getParts(File emlFile) {
		 
		 try {
		 
	        Properties props = System.getProperties();
	        props.put("mail.host", "smtp.dummydomain.com");
	        props.put("mail.transport.protocol", "smtp");

	        Session mailSession = Session.getDefaultInstance(props, null);
	        InputStream source = new FileInputStream(emlFile);
	        MimeMessage message = new MimeMessage(mailSession, source);

	        Result r = new Result(1, message.getSubject());
	        
	        ArrayList<String> from = new ArrayList<>();
	        
	        for (Address addr : message.getFrom()) {
				from.add(addr.toString());
			}
	        
	        r.addObject("SUBJECT", message.getSubject());
	        r.addObject("FROM", from);
	        r.addObject("BODY", message.getContent().toString());
	        
	        return r;
	    }
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao buscar partes do arquivo EML.");
		}
	 }
}
