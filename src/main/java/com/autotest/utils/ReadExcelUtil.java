package com.autotest.utils;

import com.alibaba.fastjson.JSONObject;
import com.autotest.data.ExcelRow;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.poi.ss.usermodel.CellType.FORMULA;

public class ReadExcelUtil {
    /**
     * Đọc data tù file excel
     *
     * @param clz  Tên class
     * @param path đường dẫn đên file
     * @return
     */
    public static <T> List<T> readExcel(Class<T> clz, String path) {
        System.out.println(path);
        if (null == path || "".equals(path)) {
            return null;
        }
        InputStream is;
        Workbook xssfWorkbook;
        try {
            is = new FileInputStream(path);
            if (path.endsWith(".xls")) {
                xssfWorkbook = new HSSFWorkbook(is);
            } else {
                xssfWorkbook = new XSSFWorkbook(is);
            }
            is.close();
            int sheetNumber = xssfWorkbook.getNumberOfSheets();
            List<T> allData = new ArrayList<T>();
            for (int i = 0; i < sheetNumber; i++) {
                allData.addAll(transToObject(clz, xssfWorkbook, xssfWorkbook.getSheetName(i)).values());
            }
            return allData;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Có lỗi xãy ra khi đọc file excel ：" + e.getMessage());
        }
    }

    /**
     * Đọc data từ một sheet vào một list object
     *
     * @param clz
     * @param path
     * @param sheetName
     * @return
     */
    public static <T> List<T> readExcel(Class<T> clz, String path, String sheetName) {
        if (null == path || "".equals(path)) {
            return null;
        }
        InputStream is;
        Workbook xssfWorkbook;
        try {
            is = new FileInputStream(path);
            if (path.endsWith(".xls")) {
                xssfWorkbook = new HSSFWorkbook(is);
            } else {
                xssfWorkbook = new XSSFWorkbook(is);
            }
            is.close();
            Map<Integer, T> rowDataMap = transToObject(clz, xssfWorkbook, sheetName);
            if (clz.getSuperclass().getSimpleName().equals(ExcelRow.class.getSimpleName())) {
                rowDataMap.forEach((key, value) -> ((ExcelRow) value).setRow(key));
            }
            return new ArrayList<>(rowDataMap.values());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("có lỗi xãy ra khi đọc sheet " + sheetName + " ：" + e.getMessage());
        }

    }

    private static <T> Map<Integer, T> transToObject(Class<T> clz, Workbook xssfWorkbook, String sheetName)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
//        List<T> list = new ArrayList<T>();
        Map<Integer, T> rowDatta = new HashMap<>();
        Sheet xssfSheet = xssfWorkbook.getSheet(sheetName);
        Row firstRow = xssfSheet.getRow(0);
        if (null == firstRow) {
            return rowDatta;
        }
        List<Object> heads = getRow(firstRow);
        heads.add("sheetName");
        Map<String, Method> headMethod = getSetMethod(clz, heads);
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            try {
                Row xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow == null) {
                    continue;
                }
                T t = clz.newInstance();
                List<Object> data = getRow(xssfRow);
                while (data.size() + 1 < heads.size()) {
                    data.add("");
                }
                data.add(sheetName);
                setValue(t, data, heads, headMethod);
                rowDatta.put(rowNum, t);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return rowDatta;
    }

    private static Map<String, Method> getSetMethod(Class<?> clz,
                                                    List<Object> heads) {
        Map<String, Method> map = new HashMap<>();
        Method[] methods = clz.getMethods();
        for (Object head : heads) {
            // boolean find = false;
            for (Method method : methods) {
                if (method.getName().toLowerCase().equals("set" + head.toString().toLowerCase())
                        && method.getParameterTypes().length == 1) {
                    map.put(head.toString(), method);
                    break;
                }
            }
        }
        return map;
    }

    private static void setValue(Object obj, List<Object> data, List<Object> heads, Map<String, Method> methods)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        for (Map.Entry<String, Method> entry : methods.entrySet()) {
            Object value = "";
            int dataIndex = heads.indexOf(entry.getKey());
            if (dataIndex < data.size()) {
                value = data.get(heads.indexOf(entry.getKey()));
            }
            Method method = entry.getValue();
            Class<?> param = method.getParameterTypes()[0];
            if (String.class.equals(param)) {
                method.invoke(obj, value);
            } else if (Integer.class.equals(param) || int.class.equals(param)) {
                if (StringUtils.isEmpty(value.toString())) {
                    value = 0;
                }
                method.invoke(obj, new BigDecimal(value.toString()).intValue());
            } else if (Long.class.equals(param) || long.class.equals(param)) {
                if (isEmpty(value.toString())) {
                    value = 0;
                }
                method.invoke(obj, new BigDecimal(value.toString()).longValue());
            } else if (Short.class.equals(param) || short.class.equals(param)) {
                if (isEmpty(value.toString())) {
                    value = 0;
                }
                method.invoke(obj, new BigDecimal(value.toString()).shortValue());
            } else if (Boolean.class.equals(param) || boolean.class.equals(param)) {
                method.invoke(obj, Boolean.parseBoolean(value.toString())
                        || value.toString().toLowerCase().equals("y"));
            } else if (JSONObject.class.equals(param)) {
                method.invoke(obj, JSONObject.parseObject(value.toString()));
            } else {
                // Date
                method.invoke(obj, value);
            }
        }
    }

    private static List<Object> getRow(Row xssfRow) {
        List<Object> cells = new ArrayList<>();
        if (xssfRow != null) {
            for (short cellNum = 0; cellNum < xssfRow.getLastCellNum(); cellNum++) {
                Cell xssfCell = xssfRow.getCell(cellNum);
                cells.add(getValue(xssfCell));
            }
        }
        return cells;
    }

    private static String getValue(Cell cell) {
        if (null == cell) {
            return "";
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == FORMULA) {
            String cellText = String.valueOf(cell.getNumericCellValue());
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                // format in form of M/D/YY
                double d = cell.getNumericCellValue();
                Calendar cal = Calendar.getInstance();
                cal.setTime(HSSFDateUtil.getJavaDate(d));
                cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
                cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + 1 + "/" + cellText;
            }
            return cellText;
        } else if (cell.getCellType() == CellType.BLANK) return "";
        else return String.valueOf(cell.getStringCellValue());
    }
}
