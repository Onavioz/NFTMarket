package service;
import java.io.IOException;

import com.mailer.Mailer.*;

public class ServiceFacade {
	//private FileService fileService;
	//private GridService gridService;
	//private APIService apiService;
	//private ManualCollectionService manualCollectionService;
	//private EmailService emailService;
	
	public ServiceFacade() {
		//fileService = new FileService();
		//gridService = new GridService();
		//apiService = new ApiService();
		//manualCollectionService = new ManualCollectionService();
		//emailService = new EmailService();
	}
	
	public void UploadFile() {
		//fileService.Upload(String path);
	}
	
	public void SaveFile() {
		//fileService.Save(String path);
	}
	
	public void Grid() {
		//gridService.GetData();
		//gridServie.SetData();
		//gridService.Setentries();
		//gridService.SetPaginator();
		//gridService.SetSort();
		//gridService.SetService();
		
	}
	
	public void APICollection() {
		//apiService.GetCollection(Object Instance);
		//apiService.SetCollection();
	}
	
	public void ManualCollection() {
		//manualCollectionService.SetCollection(String CollectionName);
		//manualCollectionService.GetCollection(String CollectionName);
	}
	
	public static void Email(String to,String what) {
		try {
		Sender sender = Sender.GetSender();
		sender.SendMail(to,what);
		}catch(Exception e) {System.out.println("there was an exeption in mail service");}
		//emailService.GetAddress(String address);
		//emailService.GetData();
		//emailService.SendMail();
	}
}
