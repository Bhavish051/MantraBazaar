package com.MantraBazaar.Mantra_Bazaar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.sun.jdi.Field;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

public class ExcelHelper {

    private List<String> fieldNames = new ArrayList<>();
    private Workbook workbook = null;
    private String WorkBookName = "";

    public ExcelHelper(String WorkBookName)
    {
        this.WorkBookName = WorkBookName;
        initialize();
    }

    private void initialize() {
        setWorkbook(new HSSFWorkbook());
    }

    public void closeWorksheet() {
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(getWorkbookName());
            getWorkbook().write(fileOut);
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean setupFieldsForClass(Class clas) throws Exception{
        java.lang.reflect.Field[] field = clas.getDeclaredFields();
        for(int i =0; i< field.length; i++)
        {
            fieldNames.add(field[i].getName());
        }
        return true;
    }

    private Sheet getSheetWithName(String Name)
    {
        Sheet sheet = null;
        
        for(int i = 0; i < workbook.getNumberOfSheets(); i++)
        {
            if(Name.compareTo(workbook.getSheetName(i)) == 0)
                {
                    sheet = workbook.getSheetAt(i);
                    break;
                }
        }
        return sheet;
    }

    private void initializeForRead() throws InvalidFormatException, IOException{
        InputStream inp = new FileInputStream(getWorkbookName());
        workbook = WorkbookFactory.create(inp);
    }

    public <T> List<T> readData(String classname) throws Exception{
        initializeForRead();
        Sheet sheet = getSheetWithName(classname);
        Class clas = Class.forName(workbook.getSheetName(0));
        setupFieldsForClass(clas);

        List<T> result = new ArrayList<T>();
        Row row;
        for(int rowCount = 1; rowCount < 4; rowCount++)
        {
            T one = (T) clas.getDeclaredConstructor().newInstance();
            row = sheet.getRow(rowCount);
            int colCount = 0;
            result.add(one);
            for(Cell cell : row)
            {
                String fieldName = fieldNames.get(colCount++);
                Method method = constructMethod(clas, fieldName);
                CellType cellType = cell.getCellType();
                if (cellType == CellType.STRING) {
                    String value = cell.getStringCellValue();
                    Object[] values = new Object[1];
                    values[0] = value;
                    method.invoke(one, values);
                } else if (cellType == CellType.NUMERIC) {
                    Double num = cell.getNumericCellValue();
                    Class returnType = getGetterReturnClass(clas, fieldName);
                    if (returnType == int.class || returnType == Integer.class) {
                        method.invoke(one, num.intValue());
                    } else if (returnType == double.class || returnType == Double.class) {
                        method.invoke(one, num);
                    } else if (returnType == float.class || returnType == Float.class) {
                        method.invoke(one, num.floatValue());
                    } else if (returnType == long.class || returnType == Long.class) {
                        method.invoke(one, num.longValue());
                    } else if (returnType == Date.class) {
                        java.util.Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
                        method.invoke(one, date);
                    }
                } else if (cellType == CellType.BOOLEAN) {
                    boolean num = cell.getBooleanCellValue();
                    Object[] values = new Object[1];
                    values[0] = num;
                    method.invoke(one, values);
                }
            }
        }
        return result;
    }

    private Class getGetterReturnClass(Class clas, String fieldName)
    {
        String methodName = "get" + capitalize(fieldName);
        String methodIsName = "is" + capitalize(fieldName);

        Class returnType = null;

        for(Method method : clas.getMethods()){
            if(method.getName().equals(methodName) || method.getName().equals(methodIsName)){
                returnType = method.getReturnType();
                break;
            }
        }
        return returnType;
    }

    @SuppressWarnings("unchecked")
    private Method constructMethod(Class clazz, String fieldName) throws SecurityException, NoSuchMethodException {
        Class fieldClass = getGetterReturnClass(clazz, fieldName);
        return clazz.getMethod("set" + capitalize(fieldName), fieldClass);
    }

    public <T> void writeData(List<T> data) throws Exception {
        try {
            Sheet sheet = getWorkbook().createSheet(data.get(0).getClass().getName());
            setupFieldsForClass(data.get(0).getClass());
            int rowCount = 0;
            int columnCount = 0;
            Row row = sheet.createRow(rowCount++);
            for (String fieldName : fieldNames) {
                Cell cel = row.createCell(columnCount++);
                cel.setCellValue(fieldName);
            }
            Class<? extends Object> classz = data.get(0).getClass();
            for (T t : data) {
                row = sheet.createRow(rowCount++);
                columnCount = 0;
                for (String fieldName : fieldNames) {
                    Cell cel = row.createCell(columnCount);
                    Method method = classz.getMethod("get" + capitalize(fieldName));
                    Object value = method.invoke(t, (Object[]) null);
                    if (value != null) {
                        if (value instanceof String) {
                            cel.setCellValue((String) value);
                        } else if (value instanceof Long) {
                            cel.setCellValue((Long) value);
                        } else if (value instanceof Integer) {
                            cel.setCellValue((Integer) value);
                        } else if (value instanceof Double) {
                            cel.setCellValue((Double) value);
                        } else if (value instanceof Date) {
                            cel.setCellValue((Date) value);
                            CellStyle styleDate = workbook.createCellStyle();
                            DataFormat dataFormatDate = workbook.createDataFormat();
                            styleDate.setDataFormat(dataFormatDate.getFormat("m/d/yy"));
                            cel.setCellStyle(styleDate);
                        } else if (value instanceof Boolean) {
                            cel.setCellValue((Boolean) value);
                        }
                    }
                    columnCount++;
                }
            }

            // Autofit
            for (int i = 0; i < fieldNames.size(); i++)
                sheet.autoSizeColumn(i);

            FileOutputStream out = new FileOutputStream(new File(WorkBookName));
            workbook.write(out);
            out.close();
            workbook.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public String capitalize(String string) {
        String capital = string.substring(0, 1).toUpperCase();
        return capital + string.substring(1);
    }

    public String getWorkbookName() {
        return WorkBookName;
    }

    public void setWorkbookName(String workbookName) {
        this.WorkBookName = workbookName;
    }

    void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public Workbook getWorkbook() {
        return workbook;
    }
}

