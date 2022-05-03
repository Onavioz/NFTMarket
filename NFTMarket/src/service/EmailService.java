package service;

import java.io.IOException;

import com.mailer.Mailer.Sender;

public class EmailService {
	private Sender sender = Sender.GetSender();

	public void SendMail(String to, String what) throws IOException {
		try {
			sender.SendMail(to, what);
		} catch (Exception e) {
			System.out.println("Error in email service");
		}
	}
}
