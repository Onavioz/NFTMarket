package service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import viewmodel.SampleViewModel.Product;
import java.util.ArrayList;


import javafx.util.Callback;


public class FileService {
	
	private TableView<Collection> collectionTableView = createTable();
	ObservableList<Collection> data = FXCollections.observableArrayList();

	int rawPerPage = 10;

	
	public ObservableList<Product> Upload() {
				
		try {
			ObservableList<Collection> list=FXCollections.observableList(ServicesClass.uploadList("collections", rawPerPage, 4));
			collectionTableView.setItems(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public void Save(ObservableList<Product> productList) {
		ServicesClass.listToExcel(new ArrayList<Collection>(collectionTableView.getItems()));
	}

}
