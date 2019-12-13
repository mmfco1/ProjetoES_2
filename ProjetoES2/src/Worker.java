import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Worker {

	private XSSFWorkbook wb;
	private XSSFSheet sheet;

//	private final int ROW_LENGTH = 12;
	public enum RuleType {
		BIGGER, SMALLER, EQUAL
	};

	public enum Metric {
		LOC, CYCLO, ATFD, LAA
	};

	public Worker() {

	}

	public String[][] createCols(File file) throws InvalidFormatException, IOException {
		wb = new XSSFWorkbook(file);
		sheet = wb.getSheetAt(0);
		int lastRowNum = sheet.getLastRowNum();
		int length = 0;
		for (Row row : sheet) {
			if (length < row.getLastCellNum())
				length = row.getLastCellNum();
		}
		DataFormatter dataFormatter = new DataFormatter();
		String[][] cols = new String[lastRowNum + 1][length];

		// PERCORRER O EXCEL E PREENCHER A MATRIZ
		for (Row row : sheet) {
			for (Cell cell : row) {
				String cellValue = dataFormatter.formatCellValue(cell);
				cols[cell.getRowIndex()][cell.getColumnIndex()] = cellValue;
			}
		}
		return cols;
	}

	public boolean testar(int a, int b) {
		boolean bool = true;
		if (b < 0) {
			b = b * (-1);
			return a < b;
		}
		if (b > 0)
			return a > b;

		return bool;
	}

	public String[][] adicionaRegra(Regras regra, String[][] sheet) {
		int lastsheetCol = sheet[0].length;
		int lastCol = lastsheetCol + 1;

		String[][] temp = new String[sheet.length][lastCol];

		int loc = regra.getLoc();
		int cyclo = regra.getCyclos();
		int atfd = regra.getAftd();
		int laa = regra.getLaa();

//		for(int i = 0; i < sheet.length; i++)
//			for(int j = 0; j < lastCol; j++)
//				temp[i][j] = sheet[i][j];

		for (int i = 0; i < sheet.length; i++) {
			for (int j = 0; j < lastCol; j++) {
				if (j < lastsheetCol) {
					//SE DESCOMENTAR ESTA LINHA JA N�O FUNCIONA
//					temp[i][j] = sheet[i][j];
				} else {
					if (i == 0) {
						temp[i][lastsheetCol] = regra.getNome();
					} else {
						if (testar(Integer.parseInt(sheet[i][4]), loc) && testar(Integer.parseInt(sheet[i][5]), cyclo)
								&& testar(Integer.parseInt(sheet[i][6]), atfd)
								&& testar(Integer.parseInt(sheet[i][7]), laa)) {
							temp[i][lastsheetCol] = "TRUE";
						} else {
							temp[i][lastsheetCol] = "FALSE";
						}
					}
				}
			}
		}

		return temp;
	}

}
