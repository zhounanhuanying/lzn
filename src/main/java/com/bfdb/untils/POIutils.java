package com.bfdb.untils;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class POIutils {


    public static <T> List<T> importExcel(String filePath, Class<T> pojoClass){
        ImportParams params = new ImportParams();
       /* params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);*/
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        }catch (NoSuchElementException e){
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return list;
    }
    public static <T> List<T> importExcel(File file, Class<T> pojoClass){
        ImportParams params = new ImportParams();
       /* params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);*/
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(file, pojoClass, params);
        }catch (NoSuchElementException e){
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return list;
    }
    public static <T> List<T> importExcel(MultipartFile file, Class<T> pojoClass){
        if (file == null){
            return null;
        }
        ImportParams params = new ImportParams();
        /*params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);*/
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
        }catch (NoSuchElementException e){
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static <T> List<T> importExcel(InputStream input, Class<T> pojoClass){
        if (input == null){
            return null;
        }
        ImportParams params = new ImportParams();

        List<T> list = null;
        try{

            try {
                list = ExcelImportUtil.importExcel(input, pojoClass, params);
            }catch (Exception e){
                list.add((T)"");
            }
        }catch (NoSuchElementException e){
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
