package viewmodel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.Collection;
import model.CollectionFactory;
import model.Product;
import service.ServiceFacade;
import javafx.scene.control.TableView;

public class SampleViewModel implements Initializable {

	@FXML
	private Button SaveListBtn;

	@FXML
	private Button UploadListBtn;

	@FXML
	private TextField SearchTextBox;

	@FXML
	private Button SearchBtn;

	@FXML
	private Button AddCollectionBtn;

	@FXML
	private ComboBox<Integer> NumOfEntries;

	@FXML
	private Pagination pagination;

	@FXML
	private TableView<Product> CollectionTable;

	@FXML
	private TableColumn<Product, String> NameColumn;

	@FXML
	private TableColumn<Product, String> OpenseaCol;

	@FXML
	private TableColumn<Product, String> MagicEdenCol;

	@FXML
	private TableColumn<Product, String> DiffCol;

	@FXML
	private Button RefreshBtn;

	@FXML
	private TextField NewCollectionName;

	@FXML
	private TextField MinToRefreshText;

	@FXML
	private Button SaveRefMinBtn;

	@FXML
	private TextField MinToCheckEmailTxt;

	@FXML
	private Button SaveMinToCheckEmailBtn;

	@FXML
	private Button SaveEmailThresholdBtn;

	@FXML
	private TextField ThresholdPercentTxt;

	@FXML
	private TextField RecipientEmailTxt;

	@FXML
	private Button SaveRecipientEmailBtn;

	@FXML
	private TextField CurrentRefreshTime;

	@FXML
	private TextField CurrentSendEmailTime;

	@FXML
	private Text AddedCollectionText;

	@FXML
	private TextField CurrentThresholdPercent;

	private ServiceFacade serviceFacade;

	private CollectionFactory collectionFactory;

	Collection openSeaMarket;
	Collection edenMarket;
	Collection diff;
	HashMap<String, String> openSeaCollection;
	HashMap<String, String> edenCollection;
	HashMap<String, String> diffCollection;
	Set<String> keySet;
	Set<String> symbols = new HashSet<String>();
	ArrayList<Product> product_list = new ArrayList<Product>();
	ObservableList<Product> productsrows = FXCollections.observableArrayList();
	ObservableList<Product> FilteredtableItems = FXCollections.observableArrayList();
	List<String> EmailsToSendList;
	int EmailThreshold, TimeToSendEmail = 60000, TimeToRefreshTable = 10000, RowsInTable, numOfItems, counterToSendEmail = 0;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		collectionFactory = new CollectionFactory();
		EmailsToSendList = new ArrayList<String>();
		serviceFacade = new ServiceFacade();
		openSeaMarket = collectionFactory.GetCollection("OpenSeaMarketCollection");
		edenMarket = collectionFactory.GetCollection("EdenMarketCollection");
		diff = collectionFactory.GetCollection("diffCollection");

		InitializeGrid();
		TableUpdate();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void InitializeGrid() {
		NameColumn = new TableColumn("Collections Name");
		NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		NameColumn.prefWidthProperty().bind(CollectionTable.widthProperty().multiply(0.3));
		OpenseaCol = new TableColumn("Opensea [Sol]");
		OpenseaCol.setCellValueFactory(new PropertyValueFactory<>("Opensea_curr"));
		OpenseaCol.prefWidthProperty().bind(CollectionTable.widthProperty().multiply(0.25));
		MagicEdenCol = new TableColumn("Magic eden [Sol]");
		MagicEdenCol.setCellValueFactory(new PropertyValueFactory<>("MagicEden_curr"));
		MagicEdenCol.prefWidthProperty().bind(CollectionTable.widthProperty().multiply(0.25));
		DiffCol = new TableColumn("Diff [%]");
		DiffCol.setCellValueFactory(new PropertyValueFactory<>("Diff_curr"));
		DiffCol.prefWidthProperty().bind(CollectionTable.widthProperty().multiply(0.2));

		// set columns in table
		CollectionTable.getColumns().addAll(NameColumn, OpenseaCol, MagicEdenCol, DiffCol);
		// set entries for the table.
		NumOfEntries.getItems().addAll(5, 10, 15, 20, 25);
		RowsInTable = 5;
		PaginatorRefrash();
		FilteredtableItems = FXCollections.observableArrayList();
		CurrentRefreshTime.setText(Integer.toString(TimeToRefreshTable / 1000));
		CurrentSendEmailTime.setText(Integer.toString(TimeToSendEmail / 1000));
	}

	public void SetTableSize() {
		/*
		 * CollectionTable.setPrefHeight(269); CollectionTable.setMaxHeight(269);
		 * CollectionTable.setPrefWidth(496); CollectionTable.setMaxWidth(496);
		 */
		CollectionTable.setLayoutX(38);
		CollectionTable.setLayoutY(200);
		CollectionTable.setPrefSize(496, 269);
	}

	public Node createPage(int pageIndex) {
		int fromIndex, toIndex;
		if (!FilteredtableItems.isEmpty()) {
			fromIndex = pageIndex * RowsInTable;
			toIndex = Math.min(fromIndex + RowsInTable, FilteredtableItems.size());
			CollectionTable.setItems(FXCollections.observableArrayList(FilteredtableItems.subList(fromIndex, toIndex)));
			numOfItems = FilteredtableItems.size();
		} else {
			fromIndex = pageIndex * RowsInTable;
			toIndex = Math.min(fromIndex + RowsInTable, productsrows.size());
			CollectionTable.setItems(FXCollections.observableArrayList(productsrows.subList(fromIndex, toIndex)));
			numOfItems = productsrows.size();	
		}
		return CollectionTable;
	}

	public void SearchBtn() {
		String searchWord = SearchTextBox.getText();
		FilteredtableItems.clear();
		for (Product curr : productsrows) {
			if (curr.getName().contains(searchWord) && !FilteredtableItems.contains(curr))
				FilteredtableItems.add(curr);
		}
		if (!FilteredtableItems.isEmpty())
			CollectionTable.setItems(FilteredtableItems);
		PaginatorRefrash();
		SearchTextBox.clear();
	}

	public void RefreshTableBtn() {
		PaginatorRefrash();
	}

	public void AddNewCollectionBtn() {
		if (!NewCollectionName.getText().isEmpty()) {
			String collectionName = NewCollectionName.getText().toLowerCase();
			Product newProduct = serviceFacade.ManualCollection(collectionName);
			if (!isCollectionExistAlready(collectionName)) {
				product_list.add(newProduct);
				AddedCollectionText.setText(collectionName + " added succesfully");
			}
			NumOfEntries.getSelectionModel().getSelectedItem();
			PaginatorRefrash();
			NewCollectionName.clear();

		}
	}

	/// checks if the new collection in the list
	private boolean isCollectionExistAlready(String collectionName) {
		return product_list.stream().filter(o -> o.getName().equals(collectionName)).findFirst().isPresent();
	}

	private void PaginatorRefrash() {
		int pageNumber = pagination.getCurrentPageIndex();
		pagination.setPageFactory(this::createPage);
		pagination.setPageCount(numOfItems / RowsInTable);
		pagination.setCurrentPageIndex(pageNumber);
		CollectionTable.refresh();
	}

	public void SaveListBtn() {

	}

	public void UploadListBtn() {

	}

	@FXML
	void SaveEmailThreshold(ActionEvent event) {
		int emailThresholdInt;
		try {
			emailThresholdInt = Integer.parseInt(ThresholdPercentTxt.getText());
		} catch (NumberFormatException nfe) {
			return;
		}

		if (emailThresholdInt >= 0 && emailThresholdInt <= 100) {
			EmailThreshold = emailThresholdInt;
			CurrentThresholdPercent.setText(String.valueOf(EmailThreshold));
			ThresholdPercentTxt.clear();
		}

	}

	@FXML
	void SaveListBtn(ActionEvent event) {

	}

	@FXML
	void SaveMinToCheckEmail(ActionEvent event) {
		int SecToCheckEmailInt;;
		try {
			SecToCheckEmailInt = Integer.parseInt(MinToCheckEmailTxt.getText());
		} catch (NumberFormatException nfe) {
			return;
		}

		if (SecToCheckEmailInt > 0 && SecToCheckEmailInt >= (TimeToRefreshTable / 1000)) {
			TimeToSendEmail = SecToCheckEmailInt * 1000;
			CurrentSendEmailTime.setText(String.valueOf(SecToCheckEmailInt));
			MinToCheckEmailTxt.clear();
			counterToSendEmail = TimeToSendEmail / TimeToRefreshTable;
		}
	}

	@FXML
	void SaveRecipientEmail(ActionEvent event) {
		if (!RecipientEmailTxt.getText().isEmpty()) {
			EmailsToSendList.add(RecipientEmailTxt.getText());
			RecipientEmailTxt.clear();
		}
	}

	@FXML
	void SaveRefMin(ActionEvent event) {
		int getNewTimeFromUser;;
		try {
			getNewTimeFromUser = Integer.parseInt(MinToRefreshText.getText());
		} catch (NumberFormatException nfe) {
			return;
		}

		if (getNewTimeFromUser > 0 && getNewTimeFromUser <= (TimeToSendEmail / 1000)) {
			TimeToRefreshTable = getNewTimeFromUser * 1000;
			CurrentRefreshTime.setText(String.valueOf(getNewTimeFromUser));
			MinToRefreshText.clear();
			counterToSendEmail = TimeToSendEmail / TimeToRefreshTable;
		}

	}

	@FXML
	void SaveChosenEntriesNum(ActionEvent event) {
		RowsInTable = NumOfEntries.getSelectionModel().getSelectedItem();
		if (RowsInTable > productsrows.size())
			RowsInTable = productsrows.size();
		PaginatorRefrash();
	}

	public void TableUpdate() {
		int time = 5000;
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				Runnable updater = new Runnable() {

					@Override
					public void run() {
						updateData();
						keySet = edenCollection.keySet();
						symbols.addAll(keySet);
						productsrows = getProduct();
						PaginatorRefrash();
					}
				};

				while (true) {
					try {
						Thread.sleep(time);
						if (counterToSendEmail > 0)
							counterToSendEmail--;
						if (counterToSendEmail == 0) {
							counterToSendEmail = TimeToSendEmail / TimeToRefreshTable;
							SendMails();
						}
					} catch (InterruptedException ex) {
					}
					Platform.runLater(updater);

				}
			}
		});
		thread.setDaemon(true);
		thread.start();
	}

	void updateData() {
		openSeaCollection = openSeaMarket.getCollection();
		edenCollection = edenMarket.getCollection();
		diffCollection = diff.getCollection();
	}

	public ObservableList<Product> getProduct() {

		ObservableList<Product> products_obs_list = FXCollections.observableArrayList();

		// inserting all product names that already inserted into an array of strings.
		ArrayList<String> curr_products = GetCurProductsNames(product_list);
		String[] symbolList = symbols.toArray(new String[symbols.size()]);

		for (int i = 0; i < edenCollection.size(); i++) {
			// if the product is not in the list yet

			if (!curr_products.contains(symbolList[i]))

				product_list.add(new Product(symbolList[i], openSeaCollection.get(symbolList[i]),
						edenCollection.get(symbolList[i]), diffCollection.get(symbolList[i])));

			else {
				// if the product already in table: update the data.
				if (openSeaCollection.size() == edenCollection.size()) {
					product_list.get(i).setOpensea_curr(openSeaCollection.get(symbolList[i]));
					product_list.get(i).setMagicEden_curr(edenCollection.get(symbolList[i]));
					product_list.get(i).setDiff_curr(diffCollection.get(symbolList[i]));
				}
			}
		}
		// clear old data, load new data.
		products_obs_list.clear();
		products_obs_list.addAll(product_list);

		return products_obs_list;
	}

	// Creates a list of strings of the symbols that are products right now.
	ArrayList<String> GetCurProductsNames(ArrayList<Product> product_list) {
		ArrayList<String> curr_products = new ArrayList<String>();
		for (Product product : product_list)
			curr_products.add(product.getName());
		return curr_products;
	}

	public SampleViewModel getViewModelInstace() {
		return this;
	}
	
	public void SendMails() {
		StringBuilder MessageToEmailsInList = new StringBuilder();
		MessageToEmailsInList.append("");
		if (!EmailsToSendList.isEmpty()) {
			for (String curr : diffCollection.values()) {
				try {
					if (Float.parseFloat(curr) >= EmailThreshold) {
						MessageToEmailsInList.append("Collection Name : ");
						String currentName = getKey(diffCollection, curr);
						MessageToEmailsInList.append(currentName + ", ");
						MessageToEmailsInList.append("OpenSea : ");
						MessageToEmailsInList.append(openSeaCollection.get(currentName) + ", ");
						MessageToEmailsInList.append("MagicEden : ");
						MessageToEmailsInList.append(edenCollection.get(currentName) + ", Diff[%] : " + curr + "\n");
					}
				} catch (NumberFormatException e) {

				}
			}
			for (String curr : EmailsToSendList)
				serviceFacade.Email(curr, MessageToEmailsInList.toString());
		}
	}

	public <K, V> K getKey(Map<K, V> map, V value) {
		for (Map.Entry<K, V> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}
}
