package com.mailer.Mailer;
import java.io.IOException;

public class Sender {
	
	private static Sender sender ;
	
	private Sender(){}
	
	public static Sender GetSender() {
		if (sender==null)
			return new Sender();
		else 
			return sender;
	}
	
	public void SendMail(String to,String what) throws IOException{
	SendMailWithAttachment sender = new SendMailWithAttachment();
	sender.Send(to,what);
	}
}
