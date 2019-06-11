package com.ytbdmhy.SSO_demo.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * @Copyright: weface
 * @Description:
 * @author: miaohaoyun
 * @since:
 * @history: created in 10:02 2019-06-11 created by miaohaoyun
 */
@Slf4j
public class POIUtil {

    public static List readExcel(File file) {
        List result = new LinkedList();
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
            for (String colData : firstRow) {
                Row row = sheet.createRow(0);
                // 设置行高
//            row.setHeight((short) (20 * 15));
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

    public static void main(String[] args) {
        System.out.println("start");
        long startTime = System.currentTimeMillis();

        String exportPath = "D:/WorkFile/test.xlsx";
        List<Object[]> dataList = new LinkedList<>();
        for (int i = 0;i < 1000000;i++) {
            dataList.add(new String[]{"data-"+i+"-1", "data-"+i+"-2", "data-"+i+"-3"});
        }
        exportExcel(exportPath, new String[]{"id","name","age"}, dataList);

        System.out.println("over,用时:" + (System.currentTimeMillis() - startTime) + "毫秒");
    }
}
