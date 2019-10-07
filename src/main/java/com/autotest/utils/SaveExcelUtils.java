package com.autotest.utils;

import com.autotest.config.annotation.ExcelField;
import com.autotest.data.TestResult;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class SaveExcelUtils {

    public static void saveResult(List<? extends TestResult> testResults, String filePath, String sheetName) {
        try {
            Workbook workbook = getWorkbook(filePath);
            File file = new File(filePath);
            FileOutputStream fileOut = new FileOutputStream(file);

            Sheet sheet = workbook.createSheet(sheetName);

            Map<String, ExcelField> headPositionMap = getHeadPositionMap(TestResult.class);
            Map<String, Method> headGetMethodMap = getGetMethod(TestResult.class, headPositionMap.keySet());
            AtomicReference<Integer> rowCount = new AtomicReference<>(0);
            Row rowHead = sheet.createRow(rowCount.getAndSet(rowCount.get() + 1));
            headPositionMap.forEach((key, value) -> {
                Cell cell = rowHead.createCell(value.position());
                cell.setCellValue(value.name());
            });
            testResults.stream()
                    .sorted(Comparator.comparing(TestResult::getOrder))
                    .forEach(testResult -> {
                        Row row = sheet.createRow(rowCount.getAndSet(rowCount.get() + 1));
                        saveObject(row, testResult, headGetMethodMap, headPositionMap);
                    });

            workbook.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Workbook getWorkbook(String excelFilePath) throws IOException {
        Workbook workbook;
        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }
        return workbook;
    }

    private static void saveObject(Row row, TestResult testResult,
                                   Map<String, Method> headGetMethodMap,
                                   Map<String, ExcelField> headPositionMap) {
        Map<String, Object> headValueMap = headGetMethodMap.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, entry -> {
                    try {
                        return entry.getValue().invoke(testResult);
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