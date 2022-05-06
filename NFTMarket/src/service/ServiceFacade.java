package service;

import java.io.IOException;

public class ServiceFacade {
	// private FileService fileService;
	// private GridService gridService;
	 private ApiService apiService;
	// private ManualCollectionService manualCollectionService;
	private EmailService emailService;
	private CronJobTask cronJobTask;
	private CronStarter cronStarter;
	private ConvertService converter;
	private DiffService diffService;

	public ServiceFacade() {
		// fileService = new FileService();
		// gridService = new GridService();
		// apiService = new ApiService();
		// manualCollectionService = new ManualCollectionService();
		emailService = new EmailService();
		cronJobTask = new CronJobTask();
		cronStarter = new CronStarter();
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

	public void Grid() {
		// gridService.GetData();
		// gridServie.SetData();
		// gridService.Setentries();
		// gridService.SetPaginator();
		// gridService.SetSort();
		// gridService.SetService();

	}

	public void InitializeCollection() throws IOException {
		converter.GatherCurrency();
		apiService.makeCollections(converter);
		diffService.makeDiffCollection();
	}

	public void ManualCollection() {
		// manualCollectionService.SetCollection(String CollectionName);
		// manualCollectionService.GetCollection(String CollectionName);
	}

	public void Email(String to, String what) {

		try {
			emailService.SendMail(to, what);
		} catch (IOException e) {

			System.out.println("Error in email service");
		}
	}
	
	public void CronJobForApi(long delayFromStart,long timeToWaitBetweenRuns) {
		cronStarter.cronJob(cronJobTask, delayFromStart, timeToWaitBetweenRuns);
	}
}
