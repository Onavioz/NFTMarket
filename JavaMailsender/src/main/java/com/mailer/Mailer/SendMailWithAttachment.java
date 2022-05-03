package com.mailer.Mailer;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class SendMailWithAttachment {
 
 public void Send(String reciver,String msgToReciver) throws IOException {
  String to = reciver; // to address. It can be any like gmail, hotmail etc.
  final String from = "dpcmailer34@gmail.com"; // from address. As this is using Gmail SMTP.
  final String password = "124578Dp"; // password for from mail address. 
 
  Properties prop = new Properties();
  prop.put("mail.smtp.host", "smtp.gmail.com");
  prop.put("mail.smtp.port", "465");
  prop.put("mail.smtp.auth", "true");
  prop.put("mail.smtp.socketFactory.port", "465");
  prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
 
  Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
   protected PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(from, password);
   }
  });
 
  try {
 
   Message message = new MimeMessage(session);
   message.setFrom(new InternetAddress(from));
   message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
   message.setSubject("Message from Design pettern course");
   message.setText(msgToReciver); 

 
   Transport.send(message);
 
   System.out.println("Mail successfully sent..");
 
  } catch (MessagingException e) {
   e.printStackTrace();
  }
 }
}