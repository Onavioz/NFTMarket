package service;

import java.io.IOException;

import model.Product;

public class ServiceFacade {
	// private FileService fileService;
	// private GridService gridService;
	private ApiService apiService;
	private ManualCollectionService manualCollectionService;
	private EmailService emailService;
	private ConvertService converter;
	private DiffService diffService;

	public ServiceFacade() {
		// fileService = new FileService();
		// gridService = new GridService();
		// apiService = new ApiService();
		manualCollectionService = new ManualCollectionService();
		emailService = new EmailService();
		apiService = new ApiService();
		converter = new ConvertService();
		diffService = new DiffService();
	}

	public void UploadFile() {
		// fileService.Upload(String path);
	}

	public void SaveFile() {
		// fileService.Save(String path);
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
