package com.autotest.utils;

import com.autotest.config.annotation.ExcelField;
import com.autotest.data.ExcelRow;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class SaveExcelUtils {

    public static void saveResultToFile(List<? extends ExcelRow> objs,
                                        String filePath, String sheetName) {
        try {
            File file = new File(filePath);
            Workbook workbook;
            FileInputStream inputStream = null;
            if (file.exists()) {
                inputStream = new FileInputStream(file);
                workbook = WorkbookFactory.create(inputStream);
            } else {
                workbook = new HSSFWorkbook();
            }
            Sheet sheet = workbook.createSheet(sheetName);

            Map<String, ExcelField> headPositionMap = getHeadPositionMap(TestResult.class);
            Map<String, Method> headGetMethodMap = getGetMethod(TestResult.class, headPositionMap.keySet());
            AtomicReference<Integer> rowCount = new AtomicReference<>(0);
            Row rowHead = sheet.createRow(rowCount.getAndSet(rowCount.get() + 1));
            headPositionMap.forEach((key, value) -> {
                Cell cell = rowHead.createCell(value.position());
                cell.setCellValue(value.name());
            });
            objs.stream()
                    .sorted(Comparator.comparing(ExcelRow::getOrder))
                    .forEach(testResult -> {
                        Row row = sheet.createRow(rowCount.getAndSet(rowCount.get() + 1));
                        saveObject(row, testResult, headGetMethodMap, headPositionMap);
                    });
            if (inputStream != null) inputStream.close();
            FileOutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void saveResult(List<? extends ExcelRow> objs, String filePath, String sheetName) {
        if (objs.isEmpty()) return;
        InputStream is;
        Workbook workbook;
        try {
            is = new FileInputStream(filePath);
            if (filePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(is);
            } else {
                workbook = new XSSFWorkbook(is);
            }
            is.close();
            Sheet sheet = workbook.getSheet(sheetName);

            Class<? extends ExcelRow> aClass = objs.get(0).getClass();
            Map<String, ExcelField> headPositionMap = getHeadPositionMap(aClass);
            Map<String, Method> headGetMethodMap = getGetMethod(aClass, headPositionMap.keySet());

            objs.stream()
                    .sorted(Comparator.comparing(ExcelRow::getOrder))
                    .forEach(testResult -> {
                        Row row = sheet.getRow(testResult.getOrder());
                        saveObject(row, testResult, headGetMethodMap, headPositionMap);
                    });
            FileOutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void saveObject(Row row, Object object,
                                   Map<String, Method> headGetMethodMap,
                                   Map<String, ExcelField> headPositionMap) {
        Map<String, Object> headValueMap = headGetMethodMap.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, entry -> {
                    try {
                        return entry.getValue().invoke(object);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        return "";
                    }
                }));
        headPositionMap.forEach((key, excelField) -> {
            Cell cell = row.createCell(excelField.position());
            Object obj = headValueMap.get(key);
            if (obj instanceof Date)
                cell.setCellValue((Date) obj);
            else if (obj instanceof Boolean)
                cell.setCellValue((Boolean) obj);
            else if (obj instanceof String)
                cell.setCellValue((String) obj);
            else if (obj instanceof Double)
                cell.setCellValue((Double) obj);
            else if (obj instanceof Integer)
                cell.setCellValue((Integer) obj);
            else if (obj instanceof Float)
                cell.setCellValue((Float) obj);
            else if (obj != null) {
                cell.setCellValue(obj.toString());
            }
        });
    }

    private static Map<String, ExcelField> getHeadPositionMap(Class<?> aClass) {
        return Arrays.stream(aClass.getDeclaredFields())
                .map(field -> Pair.of(field.getName(), field.getAnnotation(ExcelField.class)))
                .filter(pair -> pair.getRight() != null)
                .collect(toMap(Pair::getLeft, Pair::getRight));
    }

    private static Map<String, Method> getGetMethod(Class<?> clz, Collection<String> heads) {
        Map<String, String> methodHeadMap = heads.stream()
                .map(s -> {
                    String first = s.substring(0, 1);
                    return Pair.of("get" + s.replaceFirst(first, first.toUpperCase()), s);
                })
                .collect(toMap(Pair::getLeft, Pair::getRight));
        Method[] methods = clz.getMethods();
        return Arrays.stream(methods)
                .filter(method -> method.getName().startsWith("get") &&
                        methodHeadMap.containsKey(method.getName()))
                .collect(toMap(method -> methodHeadMap.get(method.getName()), Function.identity()));
    }
}
