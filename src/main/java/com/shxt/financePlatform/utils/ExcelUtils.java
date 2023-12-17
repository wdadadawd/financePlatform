package com.shxt.financePlatform.utils;

import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author zt
 * @create 2023-07-11 19:39
 * Excel工具类
 */
public class ExcelUtils {

    public static List<T> excelToShopIdList(InputStream inputStream, String suffix) {
        Class<T> tClass = T.class;
        List<T> list = new ArrayList<>();
        Workbook workbook = null;
        try {
            // 1.根据传递过来的文件输入流创建一个workbook对象(对应Excel中的工作簿)
            workbook = WorkbookFactory.create(inputStream);
            inputStream.close();                            // 关闭输入流
            //2.获取第一张表
            Sheet sheet = workbook.getSheetAt(0);
            //3.获取工作表的总行数rowLength和总列数colLength
            int rowLength = sheet.getLastRowNum() + 1;
            Row row = sheet.getRow(0);                         //获取工作表第一行数据
            //获取列数
            int colLength = 0;
            Field[] fields = new  Field[row.getLastCellNum()];          //T类型的字段
            for (int i=0;i<row.getLastCellNum();i++){           //去除空列名,获得真正的列数
                Cell cell = row.getCell(i);
                if (cell == null || cell.getCellType() == CellType.BLANK)
                    continue;
                String fieldName = cell.getStringCellValue();       //通过放射获取T类型的所以字段,并使用数组存放
                Field declaredField = tClass.getDeclaredField(fieldName);
                declaredField.setAccessible(true);
                fields[colLength++] = declaredField;
            }
//            System.out.println(colLength);
            //4.遍历全部单元格,将每行保存为一个预测客流量对象
            Cell cell;                  // 创建一个单元格对象
            for (int a = 1; a < rowLength; a++) {           // 遍历表
                T t = null;
                for (int b = 0; b < colLength; b++) {
                    Row nowRow = sheet.getRow(a);
                    if (nowRow==null)               //行不存在
                        continue;
                    cell = nowRow.getCell(b);      // 取出第a行b列的单元格数据
                    if (cell == null || cell.getCellType() == CellType.BLANK)     //判断单元格是否有值
                        continue;
                    if (t == null)                   //第一次通过创建类型
                        t = tClass.newInstance();
                    //设置属性
                    Field field = fields[b];
                    setFieldValue(t,field,cell);
                }
                System.out.println(t);
                if (t != null)
                    list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //5.返回集合
//        System.out.println(list == null);
        return list;
    }


    public static void setFieldValue(T t,Field field,Cell cell) throws IllegalAccessException {
        Class<?> type = field.getType();
        if (type.isAssignableFrom(String.class)) {            //String
            field.set(t,cell.getStringCellValue());
        } else if (type.isAssignableFrom(Date.class)) {       //Date
            field.set(t,cell.getDateCellValue());
        } else if (type.isAssignableFrom(Double.class) || type.isAssignableFrom(int.class)) {    //Double
            field.set(t,cell.getNumericCellValue());
        } else if (type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(boolean.class)) {  //Boolean
            field.set(t,cell.getBooleanCellValue());
        } else if (type.isAssignableFrom(Integer.class) || type.isAssignableFrom(int.class)) {  //Integer
            field.set(t, (int) cell.getNumericCellValue());
        }
    }
}
