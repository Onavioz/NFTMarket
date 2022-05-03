package viewmodel;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Market;
import service.ServiceFacade;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

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
	private TableColumn<SampleViewModel.Product, Integer> CurrCol;

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
	ObservableList<SampleViewModel.Product> productsrows = FXCollections.observableArrayList();
	List<String> EmailsToSendList;
	int EmailThreshold, TimeToSendEmail, TimeToRefreshTable, RowsInTable;

	public class Product {
		String name;
		int curr;

		public Product(String name, int curr) {
			this.name = name;
			this.curr = curr;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getCurr() {
			return curr;
		}

		public void setCurr(int curr) {
			this.curr = curr;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		NameColumn = new TableColumn("Collections Name");
		NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		CurrCol = new TableColumn("Curr");
		CurrCol.setCellValueFactory(new PropertyValueFactory<>("curr"));

		productsrows = getProduct();
		CollectionTable.setItems(productsrows);
		CollectionTable.getColumns().addAll(NameColumn, CurrCol);

		NumOfEntries.getItems().addAll(5, 10, 15, 20, 25);

		EmailsToSendList = new ArrayList<String>();

		pagination.setPageFactory(this::createPage);
		// SetTableSize();

		RowsInTable = 5;

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
		products.add(new Product("first", 1));
		products.add(new Product("second", 2));
		products.add(new Product("first", 1));
		products.add(new Product("second", 2));
		products.add(new Product("first", 1));
		products.add(new Product("second", 2));
		products.add(new Product("second", 2));
		products.add(new Product("first", 1));
		products.add(new Product("second", 2));
		products.add(new Product("second", 2));
		products.add(new Product("first", 1));
		products.add(new Product("second", 2));
		products.add(new Product("second", 2));
		products.add(new Product("first", 1));
		products.add(new Product("second", 2));

		return products;
	}

	public void SearchBtn() {
		String searchWord = SearchTextBox.getText();
		ObservableList<SampleViewModel.Product> FilteredtableItems = FXCollections.observableArrayList();
		for (Product curr : productsrows) {
			if (curr.getName().equals(searchWord))
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
		boolean flag = true;
		if (NewCollectionName.getText().isEmpty() || NewCurrName.getText().isEmpty())
			flag = false;
		if (flag) // only if both of the text field are not empty enter to add new collection
		{
			productsrows.add(new Product(NewCollectionName.getText(), Integer.parseInt(NewCurrName.getText())));
			RefreshTableBtn();
			NewCollectionName.clear();
			NewCurrName.clear();
		}
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
	}

}
