package com.prachi.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ExcelUtils — reads test data from Excel (.xlsx) for data-driven testing.
 * Author: Prachi Dahibhate
 *
 * Excel format:
 *   Row 0  → headers (column names)
 *   Row 1+ → data rows
 */
public class ExcelUtils {

    private static final Logger log = LogManager.getLogger(ExcelUtils.class);

    private ExcelUtils() {}

    /**
     * Returns all rows from the given sheet as a list of maps.
     * Key = column header, Value = cell value.
     */
    public static List<Map<String, String>> getSheetData(String filePath, String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();

        try (FileInputStream fis     = new FileInputStream(filePath);
             Workbook workbook       = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }

            Row headerRow = sheet.getRow(0);
            int colCount  = headerRow.getLastCellNum();

            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                Map<String, String> rowData = new HashMap<>();
                for (int c = 0; c < colCount; c++) {
                    String header    = getCellValue(headerRow.getCell(c));
                    String cellValue = getCellValue(row.getCell(c));
                    rowData.put(header, cellValue);
                }
                data.add(rowData);
            }

            log.info("✅ Loaded {} rows from sheet '{}' in {}", data.size(), sheetName, filePath);

        } catch (IOException e) {
            log.error("❌ Failed to read Excel: {}", e.getMessage());
            throw new RuntimeException("Excel read failed", e);
        }

        return data;
    }

    /**
     * Returns test data as Object[][] for TestNG @DataProvider.
     */
    public static Object[][] getDataProviderData(String filePath, String sheetName) {
        List<Map<String, String>> rows = getSheetData(filePath, sheetName);
        Object[][] data = new Object[rows.size()][1];
        for (int i = 0; i < rows.size(); i++) {
            data[i][0] = rows.get(i);
        }
        return data;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue().trim();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                    ? cell.getDateCellValue().toString()
                    : String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default      -> "";
        };
    }
}
