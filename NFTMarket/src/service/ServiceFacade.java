package service;

import java.io.IOException;
import java.util.ArrayList;

import model.Product;

public class ServiceFacade {
    private FileService fileService;
	private ApiService apiService;
	private ManualCollectionService manualCollectionService;
	private EmailService emailService;
	private ConvertService converter;
	private DiffService diffService;

	public ServiceFacade() {
		 fileService = new FileService();
		manualCollectionService = new ManualCollectionService();
		emailService = new EmailService();
		apiService = new ApiService();
		converter = new ConvertService();
		diffService = new DiffService();
	}

	public ArrayList<Product> UploadFile() {
		ArrayList<Product> productList = fileService.Upload();
		 return productList;
	}

	public void SaveFile(ArrayList<Product> product_list) {
		 fileService.Save(product_list);
	}

	public void InitializeCollection() throws IOException {
		converter.GatherCurrency();
		apiService.makeCollections(converter, diffService);
	}

	public Product ManualCollection(String CollectionName) {
		manualCollectionService.AddCollection(CollectionName);
		return manualCollectionService.GetCollection();
	}

	public void Email(String to, String what) {

		try {
			emailService.SendMail(to, what);
		} catch (IOException e) {

			System.out.println("Error in email service");
		}
	}
}
