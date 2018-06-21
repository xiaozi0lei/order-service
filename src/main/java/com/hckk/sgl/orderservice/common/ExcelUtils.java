package com.hckk.sgl.orderservice.common;

import com.hckk.sgl.orderservice.entity.Order;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通过模板创建 Excel
 */
public class ExcelUtils {

    private final static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    /**
     * 获取 excel 数据
     *
     * @return 返回 excel 文件 Workbook 对象
     */
    public static Workbook getExcel(List<Order> orderList, String departmentName) {

        InputStream inputStream;

        try {
            // 查看当前系统的默认 ClassLoader
            ClassLoader cl = ClassUtils.getDefaultClassLoader();
            URL url = cl.getResource("excel/易鑫加班申请表-模板.xlsx");
            if (url != null) {
                logger.info("模板地址： {}", url.toString());
            }

            // 拿到模板
            Resource resource = new ClassPathResource("excel/易鑫加班申请表-模板.xlsx");
            inputStream = resource.getInputStream();
            // 往 sheet1 中写数据
            String sheetName = "sheet1";
            return writeNewExcel(inputStream, sheetName, orderList, departmentName);
        } catch (FileNotFoundException e) {
            logger.info("excel/易鑫加班申请表-模板.xlsx 模板文件不存在");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将数据库中的数据写入
     *
     * @param inFile    输入的模板
     * @param sheetName 编辑的 sheet 名字
     * @param orderList 要写入的来自数据库的数据
     * @return 完成的工作簿
     */
    private static Workbook writeNewExcel(InputStream inFile, String sheetName, List<Order> orderList, String departmentName) {
        Workbook wb = null;
        Row row;
        Cell cell;

        try {
            // 获取模板工作薄
            wb = new XSSFWorkbook(inFile);
            // 获取模板表
            Sheet sheet = wb.getSheet(sheetName);

            // 替换表中的变量 date 为当天日期
            Map<String, String> map = new HashMap<>();
            map.put("date", LocalDate.now().toString());
            map.put("departmentName", departmentName);

            replaceFinalData(map, sheet);

            // 设置列宽
            sheet.setDefaultColumnWidth(15);

            //调整第2列宽度
            sheet.setColumnWidth(1, 30 * 256);

            // 循环插入数据
            // 获取最后一行，在下一行进行插入
            int lastRow = sheet.getLastRowNum() + 1;
            // Excel 单元格样式
            CellStyle cs = setSimpleCellStyle(wb);

            // 循环插入订餐数据
            for (int i = 0; i < orderList.size(); i++) {
                //创建新的 ROW，用于数据插入
                row = sheet.createRow(lastRow + i);

                // 在该处将对象数据插入到Excel中
                Order order = orderList.get(i);
                if (null == order) {
                    break;
                }
                //Cell赋值开始
                cell = row.createCell(0);
                cell.setCellValue(i + 1);
                cell.setCellStyle(cs);

                cell = row.createCell(1);
                cell.setCellValue(order.getNickname());
                cell.setCellStyle(cs);

                cell = row.createCell(2);
                cell.setCellStyle(cs);
                cell.setCellValue(order.getReason());

                cell = row.createCell(3);
                cell.setCellValue("19:00-21:00");
                cell.setCellStyle(cs);

                cell = row.createCell(4);
                cell.setCellValue("晚餐");
                cell.setCellStyle(cs);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }

    /**
     * 描述：设置简单的Cell样式
     *
     * @return 单元格样式
     */
    private static CellStyle setSimpleCellStyle(Workbook wb) {
        CellStyle cs = wb.createCellStyle();

        //下边框
        cs.setBorderBottom(BorderStyle.THIN);
        //左边框
        cs.setBorderLeft(BorderStyle.THIN);
        //上边框
        cs.setBorderTop(BorderStyle.THIN);
        //右边框
        cs.setBorderRight(BorderStyle.THIN);
        // 居中
        cs.setAlignment(HorizontalAlignment.CENTER);

        return cs;
    }

    /**
     * 根据map替换相应的常量，通过Map中的值来替换#开头的值
     *
     * @param stringMap 要替换的变量 map
     */
    private static void replaceFinalData(Map<String, String> stringMap, Sheet sheet) {
        if (stringMap == null) return;

        for (Row row : sheet) {
            for (Cell c : row) {
                String str = c.getStringCellValue().trim();
                if (str.startsWith("#")) {
                    if (stringMap.containsKey(str.substring(1))) {
                        c.setCellValue(stringMap.get(str.substring(1)));
                    }
                }
            }
        }
    }
}
