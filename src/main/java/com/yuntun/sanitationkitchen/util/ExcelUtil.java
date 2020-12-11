package com.yuntun.sanitationkitchen.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tangcx
 */
@Slf4j
public class ExcelUtil {
    /**
     * @param response  返回
     * @param data      数据
     * @param cellTitle 导出列名
     * @param keyList   数据对应的列名
     */
    public static void excelExport(HttpServletResponse response,String fileName, String sheetName, List data, List<String> cellTitle, List<String> keyList) throws Exception {
        if (data == null || data.size() == 0) {
            throw new Exception("没有数据");
        }
        if (keyList==null||keyList.size()==0||cellTitle==null||cellTitle.size()==0){
            throw new Exception("没有选择导出列");
        }
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (int row = 0; row < data.size(); row++) {
            Map<String, Object> map = new HashMap<String, Object>(16);
            for (String key : keyList) {
                if ("number".equals(key)) {
                    //序号
                    map.put(key, row + 1);
                } else {
                    Object fieldValueByName = getFieldValueByName(key, data.get(row));
                    map.put(key, fieldValueByName);
                }
            }
            dataList.add(map);
        }
        export(response, fileName, sheetName, cellTitle, dataList, keyList);
    }

    private static Object getFieldValueByName(String fieldName, Object o) throws Exception {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter);
            Object invoke = method.invoke(o);
            //当数据库中返回null的话 转成空串
            return invoke==null?"":invoke;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new Exception("属性名错误");
        }
    }

    /**
     * 将条件数据写入Excel并导出，提供文件下载
     */
    private static void export(HttpServletResponse response, String fileName, String sheetName, List<String> cellTitle,
                               List<Map<String, Object>> dataList, List<String> keyList) throws IOException {
        // 取到所有数据，创建Excel
        // 创建Excel文件
        XSSFWorkbook myWorkbook = new XSSFWorkbook();
        // 创建sheet
        XSSFSheet excelSheet = myWorkbook.createSheet(sheetName);
        // 创建待用row
        XSSFRow row = excelSheet.createRow(0);
        XSSFCellStyle cellStyle = myWorkbook.createCellStyle();
        // 创建一个居中格式
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        XSSFCell cell = null;
        // 填充列名并设置样式
        for (int i = 0; i < cellTitle.size(); i++) {
            // 创建标题数量的单元格
            cell = row.createCell(i);
            // 填充该单元格数据为列标题
            cell.setCellValue(cellTitle.get(i));
            cell.setCellStyle(cellStyle);
        }
        Map<String, Object> map = null;
        for (int i = 0; i < dataList.size(); i++) {
            // 创建数据条数
            row = excelSheet.createRow(i + 1);
            map = dataList.get(i);

            log.error("map:{}",map);
            for (int j = 0; j < keyList.size(); j++) {
                // 创建数据单元格数量
                row.createCell(j).setCellValue(map.get(keyList.get(j)).toString());
            }
        }
        // 将导出的Excel进行下载
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
        response.flushBuffer();
        OutputStream outputStream = response.getOutputStream();
        myWorkbook.write(outputStream);
        myWorkbook.close();
        outputStream.flush();
        outputStream.close();
    }



}
