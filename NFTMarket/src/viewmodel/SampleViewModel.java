package viewmodel;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
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
import model.Collection;
import model.CollectionFactory;
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
	private TableView<SampleViewModel.Product> CollectionTable;

	@FXML
	private TableColumn<SampleViewModel.Product, String> NameColumn;

	@FXML
	private TableColumn<SampleViewModel.Product, String> OpenseaCol;

	@FXML
	private TableColumn<SampleViewModel.Product, String> MagicEdenCol;

	@FXML
	private TableColumn<SampleViewModel.Product, String> DiffCol;

	@FXML
	private Button RefreshBtn;

	@FXML
	private TextField NewCollectionName;

	@FXML
	private TextField NewCurrName;

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
	private TextField CurrentThresholdPercent;

	private ServiceFacade serviceFacade;

	private CollectionFactory collectionFactory;

	private HashMap<String, String> openSeaCollection;

	private HashMap<String, String> edenCollection;

	private HashMap<String, String> diffCollection;

	private String[] keyArray;

	ObservableList<SampleViewModel.Product> productsrows = FXCollections.observableArrayList();
	List<String> EmailsToSendList;
	int EmailThreshold, TimeToSendEmail, TimeToRefreshTable, RowsInTable;

	public class Product {
		String name;
		String Opensea_curr;
		String MagicEden_curr;
		String Diff_curr;

		public Product(String name, String Opensea_curr, String MagicEden_curr, String Diff_curr) {
			this.name = name;
			this.Opensea_curr = Opensea_curr;
			this.MagicEden_curr = MagicEden_curr;
			this.Diff_curr = Diff_curr;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getOpensea_curr() {
			return Opensea_curr;
		}

		public void setOpensea_curr(String Opensea_curr) {
			this.Opensea_curr = Opensea_curr;
		}

		public String getMagicEden_curr() {
			return MagicEden_curr;
		}

		public void setMagicEden_curr(String MagicEden_curr) {
			this.MagicEden_curr = MagicEden_curr;
		}

		public String getDiff_curr() {
			return Diff_curr;
		}

		public void setDiff_curr(String Diff_curr) {
			this.Diff_curr = Diff_curr;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		collectionFactory = new CollectionFactory();
		Collection openSeaMarket = collectionFactory.createCollection("OpenSeaMarketCollection");
		Collection edenMarket = collectionFactory.createCollection("EdenMarketCollection");
		Collection diff = collectionFactory.createCollection("diffCollection");
		openSeaCollection = openSeaMarket.getCollection();
		edenCollection = edenMarket.getCollection();
		diffCollection = diff.getCollection();
		Set<String> keySet = openSeaCollection.keySet();
		keyArray = keySet.toArray(new String[keySet.size()]);

		NameColumn = new TableColumn("Collections Name");
		NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		OpenseaCol = new TableColumn("Opensea [Sol]");
		OpenseaCol.setCellValueFactory(new PropertyValueFactory<>("Opensea_curr"));
		MagicEdenCol = new TableColumn("Magic eden [Sol]");
		MagicEdenCol.setCellValueFactory(new PropertyValueFactory<>("MagicEden_curr"));
		DiffCol = new TableColumn("Diff [%]");
		DiffCol.setCellValueFactory(new PropertyValueFactory<>("Diff_curr"));

		productsrows = getProduct();
		CollectionTable.setItems(productsrows);
		CollectionTable.getColumns().addAll(NameColumn, OpenseaCol, MagicEdenCol, DiffCol);

		NumOfEntries.getItems().addAll(5, 10, 15, 20, 25);

		EmailsToSendList = new ArrayList<String>();

		pagination.setPageFactory(this::createPage);

		// SetTableSize();

		RowsInTable = 5;
		pagination.setPageCount(openSeaCollection.size() / RowsInTable);

		serviceFacade = new ServiceFacade();

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
		int fromIndex = pageIndex * RowsInTable;
		int toIndex = Math.min(fromIndex + RowsInTable, productsrows.size());
		CollectionTable.setItems(FXCollections.observableArrayList(productsrows.subList(fromIndex, toIndex)));
		// SetTableSize();

		return CollectionTable;
	}

	public ObservableList<SampleViewModel.Product> getProduct() {
		ObservableList<SampleViewModel.Product> products = FXCollections.observableArrayList();
		for (int i = 0; i < openSeaCollection.size(); i++) {
			products.add(new Product(keyArray[i], openSeaCollection.get(keyArray[i]), edenCollection.get(keyArray[i]),
					diffCollection.get(keyArray[i])));
		}
		return products;
	}

	public void SearchBtn() {
		String searchWord = SearchTextBox.getText();
		ObservableList<SampleViewModel.Product> FilteredtableItems = FXCollections.observableArrayList();
		for (Product curr : productsrows) {
			if (curr.getName().contains(searchWord))
				FilteredtableItems.add(curr);
		}
		if (!FilteredtableItems.isEmpty())
			CollectionTable.setItems(FilteredtableItems);

	}

	public void RefreshTableBtn() {
		CollectionTable.setItems(productsrows);
		pagination.setPageFactory(this::createPage);
	}

	public void AddNewCollectionBtn() {
//		boolean flag = true;
//		if (NewCollectionName.getText().isEmpty() || NewCurrName.getText().isEmpty() || )
//			flag = false;
//		if (flag) // only if both of the text field are not empty enter to add new collection
//		{
//			productsrows.add(new Product(NewCollectionName.getText(), Integer.parseInt(NewCurrName.getText())));
//			RefreshTableBtn();
//			NewCollectionName.clear();
//			NewCurrName.clear();
//		}
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
		int MinToCheckEmailInt;
		try {
			MinToCheckEmailInt = Integer.parseInt(MinToCheckEmailTxt.getText());
		} catch (NumberFormatException nfe) {
			return;
		}

		if (MinToCheckEmailInt > 0) {
			TimeToSendEmail = MinToCheckEmailInt;
			CurrentSendEmailTime.setText(String.valueOf(TimeToSendEmail));
			MinToCheckEmailTxt.clear();
		}
	}

	@FXML
	void SaveRecipientEmail(ActionEvent event) {
		if (!RecipientEmailTxt.getText().isEmpty()) {
			EmailsToSendList.add(RecipientEmailTxt.getText());
			RecipientEmailTxt.clear();
		}
		for (String curr : EmailsToSendList)
			serviceFacade.Email(curr, "check");
	}

	@FXML
	void SaveRefMin(ActionEvent event) {
		int RefInt;
		try {
			RefInt = Integer.parseInt(MinToRefreshText.getText());
		} catch (NumberFormatException nfe) {
			return;
		}

		if (RefInt > 0) {
			TimeToRefreshTable = RefInt;
			CurrentRefreshTime.setText(String.valueOf(TimeToRefreshTable));
			MinToRefreshText.clear();
		}

	}

	@FXML
	void SaveChosenEntriesNum(ActionEvent event) {
		RowsInTable = NumOfEntries.getSelectionModel().getSelectedItem();
		if (RowsInTable > productsrows.size())
			RowsInTable = productsrows.size();
		pagination.setPageFactory(this::createPage);
		pagination.setPageCount(openSeaCollection.size() / RowsInTable);

	}

}
