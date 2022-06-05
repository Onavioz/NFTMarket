package service;

import model.Product;

import java.io.FileInputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;

import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;


public class FileService {
	int rawPerPage = 2500;

	
	public ArrayList<Product> Upload() {
				
		try {
			ArrayList<Product> productList=uploadList(rawPerPage, 4);
			return productList;
		} catch (Exception e) {
			System.out.println("Upload process faild, possibly because Excel file already opened");
		}
		return null;

	}

	public void Save(ArrayList<Product> productList) {
		listToExcel(productList);
	}
	
	public static void listToExcel(ArrayList<Product> productList) {
		try {
		URI root = FileService.class.getProtectionDomain().getCodeSource().getLocation().toURI();
		String path = Path.of(root).getParent().toString();
		Workbook workbook = new Workbook();

		Worksheet worksheet = workbook.getWorksheets().get(0);

		String[][] array2D = new String[productList.size() + 1][4];

		array2D[0][0] = "Collection name";
		array2D[0][1] = "Opensea price[SOL]";
		array2D[0][2] = "Magic eden price[SOL]";
		array2D[0][3] = "Diff[%]";
		int j = 0;
		for (int i = 1; i < productList.size() + 1; i++) {
			array2D[i][j++] = productList.get(i - 1).getName();
			array2D[i][j++] = String.valueOf(productList.get(i - 1).getOpensea_curr());
			array2D[i][j++] = String.valueOf(productList.get(i - 1).getMagicEden_curr());
			array2D[i][j] = String.valueOf(productList.get(i - 1).getDiff_curr());
			j = 0;
		}
	
		try {
			worksheet.getCells().importArray(array2D, 0, 0);
			workbook.save(path+"/Collections.xlsx",SaveFormat.XLSX);
			System.out.println(("Exported data to excel."));
		} catch (Exception e) {
			System.out.println("Save process faild, possibly because Excel file already opened");
		}
		} catch (Exception e1) {
			
			e1.printStackTrace();
		}
	}

	public static ArrayList<Product> uploadList(int rowNum, int colNum) throws Exception {
		URI root = FileService.class.getProtectionDomain().getCodeSource().getLocation().toURI();
		String path = Path.of(root).getParent().toString();
		FileInputStream fstream = new FileInputStream(path+"/Collections.xlsx");

		Workbook workbook = new Workbook(fstream);

		Worksheet worksheet = workbook.getWorksheets().get(0);

		Object dataTable[][] = worksheet.getCells().exportArray(0, 0, rowNum, colNum);

		fstream.close();

		ArrayList<Product> list = new ArrayList<>();
		for (int i = 1; i < dataTable.length; i++) {
			if (dataTable[i][1] == null)
				break;
			Product temp;
			if (dataTable[i][1] instanceof Integer)
				temp = new Product((String) dataTable[i][0], ((Integer) dataTable[i][1]).toString(),((Integer) dataTable[i][2]).toString(), ((Integer) dataTable[i][3]).toString());
			else
				temp = new Product((String) dataTable[i][0], (String) dataTable[i][1],(String) dataTable[i][2], (String) dataTable[i][3]);
			list.add(temp);
			
		}
		System.out.println("Uploaded Successfull");
		return list;
	}

}
