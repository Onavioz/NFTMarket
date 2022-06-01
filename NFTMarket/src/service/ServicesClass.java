package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;


public class ServicesClass {
	
	
	public static void listToExcel(ArrayList<Collection> rows) {

		Workbook workbook = new Workbook();

		Worksheet worksheet = workbook.getWorksheets().get(0);

		String[][] array2D = new String[rows.size() + 1][4];

		array2D[0][0] = "Collection name";
		array2D[0][1] = "Opensea price[SOL]";
		array2D[0][2] = "Magic eden price[SOL]";
		array2D[0][3] = "Diff[%]";
		int j = 0;
		for (int i = 1; i < rows.size() + 1; i++) {
			array2D[i][j++] = rows.get(i - 1).getCollection_name();
			array2D[i][j++] = String.valueOf(rows.get(i - 1).getOpensea_price());
			array2D[i][j++] = String.valueOf(rows.get(i - 1).getMagic_eden_price());
			array2D[i][j] = String.valueOf(rows.get(i - 1).getDiff());
			j = 0;
		}
	
		try {
			worksheet.getCells().importArray(array2D, 0, 0);
			workbook.save("collections.xlsx");
			System.out.println(("Exported data to excel."));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Collection> uploadList(String listName, int rowNum, int colNum) throws Exception {
		FileInputStream fstream = new FileInputStream(listName + ".xlsx");

		Workbook workbook = new Workbook(fstream);

		Worksheet worksheet = workbook.getWorksheets().get(0);

		Object dataTable[][] = worksheet.getCells().exportArray(0, 0, rowNum, colNum);

		fstream.close();

		ArrayList<Collection> list = new ArrayList<>();
		for (int i = 1; i < dataTable.length; i++) {
			if (dataTable[i][1] == null)
				break;
			Collection temp;
			if (dataTable[i][1] instanceof Integer)
				temp = new Collection((String) dataTable[i][0], ((Integer) dataTable[i][1]).toString(),((Integer) dataTable[i][2]).toString(), ((Integer) dataTable[i][3]).toString());
			else
				temp = new Collection((String) dataTable[i][0], (String) dataTable[i][1],(String) dataTable[i][2], (String) dataTable[i][3]);
			list.add(temp);
		}
		
		return list;
	}


}
