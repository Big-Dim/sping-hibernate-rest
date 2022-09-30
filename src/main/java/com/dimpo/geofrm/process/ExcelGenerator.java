package com.dimpo.geofrm.process;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.dimpo.geofrm.dto.SectionDto;
import com.dimpo.geofrm.entity.GeologicalClasses;
import com.dimpo.geofrm.entity.Section;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

public class ExcelGenerator {

    private List <GeologicalClasses> geoList;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private int numCols;

    private List <SectionDto> secDtoList;

    public ExcelGenerator(List <GeologicalClasses> geoList) {
        this.geoList = geoList;
        workbook = new XSSFWorkbook();
    }
    private void writeHeader() {
        this.secDtoList = ConvertDto.geoListToSecDTOList(geoList);
        this.numCols = numOfClasses();

        sheet = workbook.createSheet("Geo Classes");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "Section name", style);
        for(int i=1; i <= numCols; i++) {
            createCell(row, i*2-1, "Class " + String.valueOf(i) + " name", style);
            createCell(row, i*2, "Class " + String.valueOf(i) + " code", style);
        }
    }
    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else {
            cell.setCellValue("");
        }
        cell.setCellStyle(style);
    }
    private void write() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (SectionDto sd : secDtoList) {
            int columnCount = 0;
            Row row = sheet.createRow(rowCount++);
            createCell(row, columnCount++,sd.getName(), style);

            for(GeologicalClasses gc : sd.getGeoClass()) {
                createCell(row, columnCount++, gc.getGeoName(), style);
                createCell(row, columnCount++, gc.getCode(), style);
            }
        }
    }
    public void generateExcelFile(HttpServletResponse response) throws IOException {
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
    public void generateExcelFile(File file) throws IOException {
        writeHeader();
        write();
        FileOutputStream outputStream =  new FileOutputStream(file);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    public static List<SectionDto> uploadFromExcel(MultipartFile file) throws IOException {
        List<SectionDto> lst = new ArrayList<>();

        if (file != null && !file.isEmpty()) {
            XSSFWorkbook workBook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = workBook.getSheetAt(0);
            // looping through each row
            for (int rowIndex = 1; rowIndex < getNumberOfNonEmptyCells(sheet, 0) - 1; rowIndex++) {
                // current row
                XSSFRow row = sheet.getRow(rowIndex);
                String className;
                String code;
                int cellind =0;
                String secName = String.valueOf(row.getCell(cellind++));
                Set<GeologicalClasses>  geoClasses = new HashSet<GeologicalClasses>();

                do {
                    className = String.valueOf(row.getCell(cellind++));
                    code = String.valueOf(row.getCell(cellind++));
                    GeologicalClasses gcl = new GeologicalClasses(null, className, code, null);
                    geoClasses.add(gcl);
                }while(className != null && !className.isEmpty() && !className.equals("null"));
                SectionDto sd = new SectionDto(null,secName, geoClasses);
                lst.add(sd);
            }
        }
        return lst;
    }

    private Object getValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case ERROR:
                return cell.getErrorCellValue();
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return null;
            case _NONE:
                return null;
            default:
                break;
        }
        return null;
    }
    public static int getNumberOfNonEmptyCells(XSSFSheet sheet, int columnIndex) {
        int numOfNonEmptyCells = 0;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row != null) {
                XSSFCell cell = row.getCell(columnIndex);
                if (cell != null && cell.getCellType() != CellType.BLANK) {
                    numOfNonEmptyCells++;
                }
            }
        }
        return numOfNonEmptyCells;
    }
    private int numOfClasses(){
        OptionalInt max = secDtoList.stream().mapToInt((sec)->sec.getGeoClass().size()).max();
        int value = max.orElseThrow();
        return value;

    }
}
