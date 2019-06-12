package com.ytbdmhy.SSO_demo.common.utils;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.monitorjbl.xlsx.StreamingReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @Copyright: weface
 * @Description:
 * @author: miaohaoyun
 * @since:
 * @history: created in 10:02 2019-06-11 created by miaohaoyun
 * @Remarks: poi 3.17; poi-ooxml 3.17; poi-scratchpad 3.17
 */
@Slf4j
public class POIUtil {

    public static void readExcel2(String filePath) {
        readExcel2(new File(filePath));
    }

    public static void readExcel2(File file) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            log.error("add message error", e);
        }
        ExcelListener excelListener = new ExcelListener();
        EasyExcelFactory.readBySax(inputStream, new com.alibaba.excel.metadata.Sheet(1,1), excelListener);
        List result = excelListener.getDatas();
        System.out.println("over");
    }

    public static List readExcel(String filePath) {
        File file = new File(filePath);
        return readExcel(file);
    }

    public static List readExcel(File file) {
        if (file == null)
            throw new NullPointerException("excel不存在");
        String fileName = file.getName();
        if (!fileName.toLowerCase().endsWith(".xls") && !fileName.toLowerCase().endsWith(".xlsx"))
            throw new NullPointerException("读取的文件不是excel");
        List<String[]> result = new LinkedList<>();

        Workbook workbook = null;
        try {
            // 获取文件的IO流
            InputStream inputStream = new FileInputStream(file);
            if (fileName.toLowerCase().endsWith(".xls")) {
                workbook = WorkbookFactory.create(inputStream);
            } else if (fileName.toLowerCase().endsWith(".xlsx")) {
                workbook = StreamingReader.builder()
                        .rowCacheSize(100)
                        .bufferSize(4096)
                        .open(inputStream);
            }
        } catch (FileNotFoundException e) {
            log.error("add message error", e);
        } catch (InvalidFormatException e) {
            log.error("add message error", e);
        } catch (IOException e) {
            log.error("add message error", e);
        }
        if (workbook != null) {
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                if (fileName.toLowerCase().endsWith(".xls")) {
                    Sheet sheet = workbook.getSheetAt(sheetNum);
//                    if (sheet == null)
//                        continue;
                    for (int rowNum = sheet.getFirstRowNum(); rowNum < sheet.getLastRowNum(); rowNum++) {
                        String[] rowValue = new String[sheet.getLastRowNum() - sheet.getFirstRowNum() + 1];
                        Row row = sheet.getRow(rowNum);
//                        if (row == null)
//                            continue;
                        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
                            Cell cell = row.getCell(cellNum);
//                            if (cell == null)
//                                continue;
                            rowValue[cellNum] = cell.getStringCellValue();
                        }
                        result.add(rowValue);
                    }
                } else {
                    Sheet sheet = workbook.getSheetAt(sheetNum);
                    for (Row row : sheet) {
                        String[] rowValue = new String[row.getPhysicalNumberOfCells()];
                        int i = 0;
                        for (Cell cell : row) {
                            rowValue[i] = cell.getStringCellValue();
                            i++;
                        }
                        result.add(rowValue);
                    }
                }
            }
        }
        try {
            workbook.close();
        } catch (IOException e) {
            log.error("add message error", e);
        }

        System.out.println("------------------------------------");
        System.out.println(Arrays.toString(result.get(0)));
        System.out.println(Arrays.toString(result.get(result.size()-1)));
        return result;
    }

    public static void exportExcel(String exportPath,List<Object[]> dataList) {
        exportExcel(exportPath, new String[]{}, dataList);
    }

    public static void exportExcel(String exportPath, String[] firstRow, List<Object[]> dataList) {
        if (dataList == null || dataList.size() == 0)
            throw new NullPointerException("将要导出excel的数据为空");
        if (!exportPath.toLowerCase().endsWith(".xls") && !exportPath.toLowerCase().endsWith(".xlsx"))
            exportPath += ".xlsx";
        // 创建工作簿
        Workbook workbook;
        if (dataList.size() > 60000) {
            workbook = new SXSSFWorkbook();
        } else {
            workbook = new XSSFWorkbook();
        }
        // 创建工作表
        Sheet sheet = workbook.createSheet();
        // 创建单元格格式
        CellStyle cellStyle = workbook.createCellStyle();
        // 创建字体
        Font font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 11);
        // 设置字体是否加粗
        font.setBold(true);
        // 设置字体名称
        font.setFontName("Calibri");
        // 在样式应用设置的字体
        cellStyle.setFont(font);
        // 设置字体换行
        cellStyle.setWrapText(false);
        int i = 0;
        if (firstRow.length > 0) {
            Row row = sheet.createRow(0);
            // 设置行高
            row.setHeight((short) (20 * 15));
            for (String colData : firstRow) {
                // 创建对应的单元格
                Cell cell = row.createCell(i);
                // 设置单元格的数据类型为文本
                cell.setCellType(CellType.STRING);
                // 设置单元格的数值
                cell.setCellValue(colData);
                i++;
            }
            i = 1;
        }
        for (Object[] rowData : dataList) {
            Row row = sheet.createRow(i);
            int j = 0;
            for (Object colData : rowData) {
                Cell cell = row.createCell(j, CellType.STRING);
                cell.setCellValue(String.valueOf(colData));
                j++;
            }
            i++;
        }
        if (workbook != null) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(exportPath);
                workbook.write(fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                log.error("add message error", e);
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("start");
        long startTime = System.currentTimeMillis();

        // readExcel2
//        readExcel2("D:\\WorkFile\\0606-副本\\大名已制卡名单\\合并excel\\allExcel.xlsx");

        // readExcel
        List excelData = readExcel("D:\\WorkFile\\0606-副本\\大名已制卡名单\\合并excel\\allExcellogDate.xlsx");

        // exportExcel
//        String exportPath = "D:/WorkFile/test.xlsx";
//        List<Object[]> dataList = new LinkedList<>();
//        for (int i = 0;i < 1000000;i++) {
//            dataList.add(new String[]{"data-"+i+"-1", "data-"+i+"-2", "data-"+i+"-3"});
//        }
//        exportExcel(exportPath, new String[]{"id","name","age"}, dataList);

        System.out.println("over,用时:" + (System.currentTimeMillis() - startTime) + "毫秒");
    }
}
