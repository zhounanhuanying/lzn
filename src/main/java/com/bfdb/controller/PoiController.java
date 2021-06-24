package com.bfdb.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.bfdb.config.Constant;
import com.bfdb.entity.*;
import com.bfdb.mapper.BaseOrganizitionMapper;
import com.bfdb.service.*;
import com.bfdb.untils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 导入导出人员
 */
@RequestMapping("/poi")
@RestController
public class PoiController {
    @Autowired
    private PersonTableService personTableService;
    @Autowired
    private PersonFaceInfomationTableService personFaceInfomationTableService;
    @Autowired
    private AnonymousUrlService anonymousUrlService;
    @Autowired
    private CommonUrlService commonUrlService;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private BaseOrganizitionMapper baseOrganizitionMapper;

    private Map<String, Object> percentMap = new HashMap<>();

    @Autowired
    PersonDataOperationTableService personDataOperationTableService;
    @Autowired
    BaseParkManagementService baseParkManagementService;

    /**
     * 导出  excel表格   有图片的
     * 根据条件导出人员信息
     *
     * @param response
     * @param personTable
     * @param personIds
     */
    @RequestMapping(value = "/exportZipZiAndPhoto", method = RequestMethod.GET)
    public void exportZipZi(HttpServletResponse response, PersonTable personTable, String personIds) {
        String path = Config.getPhotoUrl("filePath"); //指定存储地址
        Integer limit = Integer.valueOf(Config.getPhotoUrl("excelNUmber")); //导出excel文件的数量
        String fileMulu = "人员信息";
        String ExeclPath = path + fileMulu;//指定生成excel的地址
        String zipName = fileMulu + ".zip";
        //创建文件夹;
        createFile(ExeclPath);
        //创建Excel文件;
        // 获取总条数
        Integer total = 0;

        //调用公共方法 把string字符串转成string数组
        String[] strings = null;
        int[] ints = null;
        if (StringUtils.isNotBlank(personIds)) {
            strings = stringInArrays(personIds);
            //string数组转成int类型的数组，用的Java8 中的Lambdas特性
            ints = Arrays.asList(strings).stream().mapToInt(Integer::parseInt).toArray();
            total = ints.length;
        } else {
            total = personTableService.getTotal();
        }
        // 设置分页参数
        Integer page = Math.round(total / limit);
        for (int i = 0; i <= page; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("page", i * limit);
            map.put("limit", limit);
            map.put("personName", personTable.getPersonName());
            map.put("identityTypeCode", personTable.getIdentityTypeCode());
            map.put("identicationInfo", personTable.getIdenticationInfo());
            map.put("departments", personTable.getDepartments());
            map.put("personId", ints);
            List<PersonTable> data = null;
            List<PersonTable> personTableList = personTableService.getData(map);
            //调用的人员查询公共类
            data = PersonTableUtils.personTableList(personTableList, personTableService);

            exportZiAndPhoto(ExeclPath, data, "人员基础信息(" + (i + 1) + ").xls", path);
        }
        //生成.zip文件;
        try {
            craeteZipPath(ExeclPath, fileMulu);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // 以流的形式下载文件。
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream(ExeclPath + ".zip"));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("人员基础信息.zip", "UTF-8"));
            //设置cookie信息
            Cookie cookies = new Cookie("configDownloadToken", "1");
            cookies.setMaxAge(60 * 60 * 60);
            cookies.setPath("/");
            response.addCookie(cookies);
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            /* file.delete();  //将生成的服务器端文件删除*/
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //删除目录下所有的文件;
        File file = new File(ExeclPath);
        //删除文件;
        deleteFile(file);
        // 删除生成的zip包
        File file1 = new File(path);
        delete(file1, zipName);
    }

    /**
     * String   字符串转成String 数组
     *
     * @param fields
     * @return
     */
    public static String[] stringInArrays(String fields) {
        String[] arr = fields.split(",");
        return arr;
    }

    /**
     * 导出不带照片  excel文档的
     * 根据条件导出人员信息
     * 刘周南
     *
     * @param response
     * @param personTable
     * @param personIds
     */
    @RequestMapping(value = "/exportZipZi", method = RequestMethod.GET)
    public void exportZipZiAndPhoto(HttpServletResponse response, PersonTable personTable, String personIds) {
        String path = Config.getPhotoUrl("filePath"); //指定存储地址
        Integer limit = Integer.valueOf(Config.getPhotoUrl("excelNUmber")); //导出excel文件的数量
        String fileMulu = "人员信息";
        String ExeclPath = path + fileMulu;//指定生成excel的地址
        String zipName = fileMulu + ".zip";
        //创建文件夹;
        createFile(ExeclPath);
        //创建Excel文件;
        // 获取总条数
        Integer total = 0;

        //调用公共方法 把string字符串转成string数组
        String[] strings = null;
        int[] ints = null;
        if (StringUtils.isNotBlank(personIds)) {
            strings = stringInArrays(personIds);
            //string数组转成int类型的数组，用的Java8 中的Lambdas特性
            ints = Arrays.asList(strings).stream().mapToInt(Integer::parseInt).toArray();
            total = ints.length;
        } else {
            total = personTableService.getTotal();
        }
        // 设置分页参数
        Integer page = Math.round(total / limit);
        for (int i = 0; i <= page; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("page", i * limit);
            map.put("limit", limit);
            map.put("personName", personTable.getPersonName());
            map.put("identityTypeCode", personTable.getIdentityTypeCode());
            map.put("identicationInfo", personTable.getIdenticationInfo());
            map.put("departments", personTable.getDepartments());
            map.put("personId", ints);
            List<PersonTable> data = new ArrayList<>();
            List<PersonTable> personTableList = personTableService.getData(map);
            //调用的人员查询公共类
            data = PersonTableUtils.personTableList(personTableList, personTableService);

            exportZi(ExeclPath, data, "人员基础信息(" + (i + 1) + ").xls");
        }
        //生成.zip文件;
        try {
            craeteZipPath(ExeclPath, fileMulu);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // 以流的形式下载文件。
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream(ExeclPath + ".zip"));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("人员基础信息.zip", "UTF-8"));
            //设置cookie信息
            Cookie cookies = new Cookie("configDownloadToken", "1");
            cookies.setMaxAge(60 * 60 * 60);
            cookies.setPath("/");
            response.addCookie(cookies);
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            /* file.delete();  //将生成的服务器端文件删除*/
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //删除目录下所有的文件;
        File file = new File(ExeclPath);
        //删除文件;
        deleteFile(file);
        // 删除生成的zip包
        File file1 = new File(path);
        delete(file1, zipName);
    }

    // 删除生成的zip包
    public static void delete(File f, String name) {
        //数组指向文件夹中的文件和文件夹
        File[] fi = f.listFiles();
        //遍历文件和文件夹
        for (File file : fi) {
            if (file.isFile()) {
                //是文件的话，把文件名放到一个字符串中
                String filename = file.getName();
                if (filename.equals(name)) {
                    file.delete();
                }
            }
        }
    }

    // 创建一个临时的文件夹存放execl
    public static String createFile(String path) {
        File file = new File(path);
        //判断文件是否存在;
        if (!file.exists()) {
            //创建文件;
            boolean bol = file.mkdirs();
            if (bol) {
                System.out.println(path + " 路径创建成功!");
            } else {
                System.out.println(path + " 路径创建失败!");
            }
        } else {
            System.out.println(path + " 文件已经存在!");
        }
        return path;
    }

    public static void deleteFile(File file) {
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()) {
            System.out.println("文件删除失败,请检查文件路径是否正确");
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f : files) {

            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()) {
                deleteFile(f);
            } else {
                f.delete();
            }
        }
        //删除空文件夹  for循环已经把上一层节点的目录清空。
        file.delete();
    }


    /**
     * 生成.zip文件;
     */
    public static void craeteZipPath(String path, String fileMulu) throws IOException {
        ZipOutputStream zipOutputStream = null;
        File file = new File(path + ".zip");
        zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        File[] files = new File(path).listFiles();
        FileInputStream fileInputStream = null;
        byte[] buf = new byte[1024];
        int len = 0;
        if (files != null && files.length > 0) {
            for (File excelFile : files) {
                String fileName = excelFile.getName();
                fileInputStream = new FileInputStream(excelFile);
                //放入压缩zip包中;
                zipOutputStream.putNextEntry(new ZipEntry(fileMulu + "/" + fileName));
                //读取文件;
                while ((len = fileInputStream.read(buf)) > 0) {
                    zipOutputStream.write(buf, 0, len);
                }
                //关闭;
                zipOutputStream.closeEntry();
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            }
        }
        if (zipOutputStream != null) {
            zipOutputStream.close();
        }
    }

    /**
     * 导出文本和图片
     * @param filePath
     * @param data
     * @param name
     * @param panfu
     */
    public void exportZiAndPhoto(String filePath, List<PersonTable> data, String name, String panfu) {
        /*  Map<String, Object> map = new HashMap<>();*/
        try {
            // 设置响应输出的头类型
          /*  response.setHeader("content-Type", "application/vnd.ms-excel");
            // 下载文件的默认名称
            response.setHeader("Content-Disposition", "attachment;filename=person.xlsx");*/
            // =========easypoi部分
//            List<PersonTable> data = Constant.personTableList; 当前页数据
            //获取所有要导出的数据
//            List<PersonTable> data=personTableService.getData();
            //查询民族
            List<DataDictionary> ethnicityList = dataDictionaryService.setlectDataDictionaryList(Constant.ETHNICITY);
            //查询人员信息
            List<DataDictionary> identicationInfoList = dataDictionaryService.setlectDataDictionaryList( Constant.IDENTICATIONINFO );
            //查询园区list
            List<BasePark>  baseParkList=baseParkManagementService.fallBasePark();
            // 以新的实体类格式导出
            List<ExcelPersonTable> excelPersonTables = new ArrayList<>();
            for (PersonTable personTable : data) {
                ExcelPersonTable excelPersonTable = new ExcelPersonTable();
                excelPersonTable.setPersonId(personTable.getPersonId());
                excelPersonTable.setPersonName(personTable.getPersonName());
                excelPersonTable.setGender(personTable.getGender());
                excelPersonTable.setIdentityNo(personTable.getIdentityNo());
                excelPersonTable.setBirthday(personTable.getBirthday());
                excelPersonTable.setResidentialAddress(personTable.getResidentialAddress());
                //民族
                if (StringUtils.isBlank(personTable.getEthnicity())) {
                    excelPersonTable.setEthnicity(" ");
                } else {
                    //判空 民族
                    if ((ListUtils.isNotNullAndEmptyList(ethnicityList))) {
                        for (DataDictionary dataDictionary : ethnicityList) {
                            if (dataDictionary.getDicCode().equals(personTable.getEthnicity())) {
                                excelPersonTable.setEthnicity(dataDictionary.getDicName());
                            }
                        }
                    }
                }
                //人员类型
                if (StringUtils.isBlank(personTable.getIdenticationInfo())) {
                    excelPersonTable.setIdenticationInfo(" ");
                } else {
                    //判空 人员类型
                    if ((ListUtils.isNotNullAndEmptyList(identicationInfoList))) {
                        for (DataDictionary dataDictionary : identicationInfoList) {
                            if (dataDictionary.getDicCode().equals(personTable.getIdenticationInfo())) {
                                excelPersonTable.setIdenticationInfo(dataDictionary.getDicName());
                            }
                        }
                    }
                }
                excelPersonTable.setTelephone(personTable.getTelephone());
                if (personTable.getStudentLevel() == null) {
                    excelPersonTable.setStudentLevel(" ");
                } else {
                    excelPersonTable.setStudentLevel(personTable.getStudentLevel());
                }
                if (personTable.getGrade() == null) {
                    excelPersonTable.setGrade(" ");
                } else {
                    excelPersonTable.setGrade(personTable.getGrade());
                }
                if (("weizhi").equals(personTable.getDepartments())) {
                    excelPersonTable.setDepartments("");
                } else {
                    excelPersonTable.setDepartments(personTable.getDepartments());
                }
                excelPersonTable.setIdentityTypeCode(personTable.getIdentityTypeCode());
                excelPersonTable.setDescription(personTable.getDescription());
                excelPersonTable.setSchoolclass(personTable.getSchoolClass());
                //判空 园区list
                if ((ListUtils.isNotNullAndEmptyList(baseParkList))) {
                    for (BasePark basePark : baseParkList) {
                        if (basePark.getId().toString().equals(personTable.getParkId())) {
                            excelPersonTable.setParkname(basePark.getParkName());
                        }
                    }
                }
                // 将图片信息放入新的实体类里
                int i = 0;
                String facePhoto = "";
                for (PersonFaceInfomationTable personFaceInfomationTable : personTable.getPersonFaceInfomationTable()) {
                    i = i + 1;
                    if (personFaceInfomationTable.getDataSource() != null) {
                        excelPersonTable.setDataSource(personFaceInfomationTable.getDataSource());
                    } else {
                        excelPersonTable.setDataSource(" ");
                    }

                    if (personFaceInfomationTable.getFaceAddress() != null) {
                        //截取老文件名的后缀
                        String prefix = personFaceInfomationTable.getFaceAddress().substring(personFaceInfomationTable.getFaceAddress().lastIndexOf(".") + 1);
                        String FaceAddress = "";
                        if (personTable.getIdentityNo() != null && !("".equals(personTable.getIdentityNo()))) {
                            FaceAddress = excelPersonTable.getPersonName() + personTable.getIdentityNo() + "人脸照片" + i + "." + prefix;
                        } else {
                            FaceAddress = excelPersonTable.getPersonName() + personTable.getIdentityTypeCode() + "人脸照片" + i + "." + prefix;
                        }
                        File file = new File(panfu + personFaceInfomationTable.getFaceAddress());
                        if (file.exists()) {
                            //将图片转存到需要压缩得文件里
                            ZipUtils.savePictoExecl(file.getParent(), filePath, file.getName(), FaceAddress);
                        }
                        facePhoto = facePhoto.concat(FaceAddress + ",");
                        excelPersonTable.setFaceAddress(facePhoto);

                    }
//                    else if (personFaceInfomationTable.getCampusCardAddress() != null) {
//                        //截取老文件名的后缀
//                        String prefix = personFaceInfomationTable.getCampusCardAddress().substring(personFaceInfomationTable.getCampusCardAddress().lastIndexOf(".") + 1);
//                        String CampusCardAddress = "";
//                        if (personTable.getIdentityNo() != null && !("".equals(personTable.getIdentityNo()))) {
//                            CampusCardAddress = excelPersonTable.getPersonName() + personTable.getIdentityNo() + "校园卡照片" + "." + prefix;
//                        } else {
//                            CampusCardAddress = excelPersonTable.getPersonName() + personTable.getIdentityTypeCode() + "校园卡照片" + "." + prefix;
//                        }
//                        File file = new File(panfu + personFaceInfomationTable.getCampusCardAddress());
//                        if (file.exists()) {
//                            ZipUtils.savePictoExecl(file.getParent(), filePath + "/", file.getName(), CampusCardAddress);
//                        }
//                        excelPersonTable.setCampusCardAddress(CampusCardAddress);
//                    } else if (personFaceInfomationTable.getIdcardImage() != null) {
//                        //截取老文件名的后缀
//                        String prefix = personFaceInfomationTable.getIdcardImage().substring(personFaceInfomationTable.getIdcardImage().lastIndexOf(".") + 1);
//                        String IdcardImage = "";
//                        if (personTable.getIdentityNo() != null && !("".equals(personTable.getIdentityNo()))) {
//                            IdcardImage = excelPersonTable.getPersonName() + personTable.getIdentityNo() + "身份证照片" + "." + prefix;
//                        } else {
//                            IdcardImage = excelPersonTable.getPersonName() + personTable.getIdentityTypeCode() + "身份证照片" + "." + prefix;
//                        }
//                        File file = new File(panfu + personFaceInfomationTable.getIdcardImage());
//                        if (file.exists()) {
//                            ZipUtils.savePictoExecl(file.getParent(), filePath + "/", file.getName(), IdcardImage);
//                        }
//                        excelPersonTable.setIdcardImage(IdcardImage);
//                    }
//                else if (personFaceInfomationTable.getFaceImage() != null) {
//                        //截取老文件名的后缀
//                        String prefix = personFaceInfomationTable.getFaceImage().substring(personFaceInfomationTable.getFaceImage().lastIndexOf(".") + 1);
//                        String FaceImage = "";
//                        if (personTable.getIdentityNo() != null && !("".equals(personTable.getIdentityNo()))) {
//                            FaceImage = excelPersonTable.getPersonName() + personTable.getIdentityNo() + "人证校验照片" + "." + prefix;
//                        } else {
//                            FaceImage = excelPersonTable.getPersonName() + personTable.getIdentityTypeCode() + "人证校验照片" + "." + prefix;
//                        }
//                        File file = new File(panfu + personFaceInfomationTable.getFaceImage());
//                        if (file.exists()) {
//                            ZipUtils.savePictoExecl(file.getParent(), filePath + "/", file.getName(), FaceImage);
//                        }
//                        excelPersonTable.setFaceImage(FaceImage);
//                    }
                }
//                人员图片为空时
                if (personTable.getPersonFaceInfomationTable().size() == 0) {
                    excelPersonTable.setDataSource(" ");
                }
                excelPersonTables.add(excelPersonTable);
            }

            ExportParams deptExportParams = new ExportParams();
            // 设置sheet得名称
            deptExportParams.setSheetName("人员信息表");

            Map<String, Object> deptExportMap = new HashMap<>();
            // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
            deptExportMap.put("title", deptExportParams);
            // 模版导出对应得实体类型
            deptExportMap.put("entity", ExcelPersonTable.class);
            // sheet中要填充得数据
            deptExportMap.put("data", excelPersonTables);

            List<Map<String, Object>> sheetsList = new ArrayList<>();
            sheetsList.add(deptExportMap);
            Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
            FileOutputStream fos;
//            String path = filePath + "/人员基础信息.xls";
            String path = filePath + "/" + name;
            fos = new FileOutputStream(path);
            workbook.write(fos);
            fos.close();
           /* ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 导出文本
    public void exportZi(String filePath, List<PersonTable> data, String name) {
        /*  Map<String, Object> map = new HashMap<>();*/
        try {
            // 设置响应输出的头类型
          /*  response.setHeader("content-Type", "application/vnd.ms-excel");
            // 下载文件的默认名称
            response.setHeader("Content-Disposition", "attachment;filename=person.xlsx");*/
            // =========easypoi部分
            //查询民族
            List<DataDictionary> ethnicityList = dataDictionaryService.setlectDataDictionaryList(Constant.ETHNICITY);
            //查询人员信息
            List<DataDictionary> identicationInfoList = dataDictionaryService.setlectDataDictionaryList( Constant.IDENTICATIONINFO );
            //查询园区list
            List<BasePark>  baseParkList=baseParkManagementService.fallBasePark();
            //获取所有要导出的数据
            // 以新的实体类格式导出
            List<ExcelPersonTableZi> excelPersonTables = new ArrayList<>();
            for (PersonTable personTable : data) {
                ExcelPersonTableZi excelPersonTable = new ExcelPersonTableZi();
                excelPersonTable.setPersonId(personTable.getPersonId());
                excelPersonTable.setPersonName(personTable.getPersonName());
                excelPersonTable.setGender(personTable.getGender());
                excelPersonTable.setIdentityNo(personTable.getIdentityNo());
                excelPersonTable.setBirthday(personTable.getBirthday());
                excelPersonTable.setResidentialAddress(personTable.getResidentialAddress());
                //民族
                if (StringUtils.isBlank(personTable.getEthnicity())) {
                    excelPersonTable.setEthnicity(" ");
                } else {
                    //判空 民族
                    if ((ListUtils.isNotNullAndEmptyList(ethnicityList))) {
                        for (DataDictionary dataDictionary : ethnicityList) {
                            if (dataDictionary.getDicCode().equals(personTable.getEthnicity())) {
                                excelPersonTable.setEthnicity(dataDictionary.getDicName());
                            }
                        }
                    }
                }
                excelPersonTable.setTelephone(personTable.getTelephone());
                //人员类型
                if (StringUtils.isBlank(personTable.getIdenticationInfo())) {
                    excelPersonTable.setIdenticationInfo(" ");
                } else {
                    //判空 人员类型
                    if ((ListUtils.isNotNullAndEmptyList(identicationInfoList))) {
                        for (DataDictionary dataDictionary : identicationInfoList) {
                            if (dataDictionary.getDicCode().equals(personTable.getIdenticationInfo())) {
                                excelPersonTable.setIdenticationInfo(dataDictionary.getDicName());
                            }
                        }
                    }
                }
                if (personTable.getStudentLevel() == null) {
                    excelPersonTable.setStudentLevel(" ");
                } else {
                    excelPersonTable.setStudentLevel(personTable.getStudentLevel());
                }
                if (personTable.getGrade() == null) {
                    excelPersonTable.setGrade(" ");
                } else {
                    excelPersonTable.setGrade(personTable.getGrade());
                }
                if (("weizhi").equals(personTable.getDepartments())) {
                    excelPersonTable.setDepartments("");
                } else {
                    excelPersonTable.setDepartments(personTable.getDepartments());
                }
                excelPersonTable.setIdentityTypeCode(personTable.getIdentityTypeCode());
                excelPersonTable.setDescription(personTable.getDescription());
                // 将图片信息放入新的实体类里
                for (PersonFaceInfomationTable personFaceInfomationTable : personTable.getPersonFaceInfomationTable()) {
                    if (personFaceInfomationTable.getDataSource() != null) {
                        excelPersonTable.setDataSource(personFaceInfomationTable.getDataSource());
                    } else {
                        excelPersonTable.setDataSource(" ");
                    }
                }
                //人员图片为空时
                if (personTable.getPersonFaceInfomationTable().size() == 0) {
                    excelPersonTable.setDataSource(" ");
                }
                //判空 园区list
                if ((ListUtils.isNotNullAndEmptyList(baseParkList))) {
                    for (BasePark basePark : baseParkList) {
                        if (basePark.getId().toString().equals(personTable.getParkId())) {
                            excelPersonTable.setParkname(basePark.getParkName());
                        }
                    }
                }
                excelPersonTable.setSchoolclass(personTable.getSchoolClass());
                excelPersonTables.add(excelPersonTable);
            }

            ExportParams deptExportParams = new ExportParams();
            // 设置sheet得名称
            deptExportParams.setSheetName("人员信息表");

            Map<String, Object> deptExportMap = new HashMap<>();
            // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
            deptExportMap.put("title", deptExportParams);
            // 模版导出对应得实体类型
            deptExportMap.put("entity", ExcelPersonTableZi.class);
            // sheet中要填充得数据
            deptExportMap.put("data", excelPersonTables);

            List<Map<String, Object>> sheetsList = new ArrayList<>();
            sheetsList.add(deptExportMap);
            Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
            FileOutputStream fos;
//                String path = filePath + "/人员基础信息.xls";
            String path = filePath + "/" + name;
            fos = new FileOutputStream(path);
            workbook.write(fos);
            fos.close();
           /* ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刘周南
     * 获取表格中的内容
     *
     * @param file
     * @param entity 转换实体类型
     * @return
     * @throws Exception
     */
    private Map getExcelData(MultipartFile file, Class entity) throws Exception {


        Map<String, Object> map = new HashMap<>();
        ZipUtils zipUtils = new ZipUtils();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dateStr = simpleDateFormat.format(date);
        // 存放图片的地址
        String dPath = Config.getPhotoUrl("filePath");
        String savaPicUrl = dPath + dateStr;
        //将后缀放在新文件名的后面
        /*//清空临时文件夹
        zipUtils.deleteDir(url);*/
        // 保存文件到指定目录
        String filePath = savaPicUrl + "/" + file.getOriginalFilename();
        //判断目录是否存在
        FileUtils.mdkirFile();
        if (!file.isEmpty()) {
            // 将rar包转存文件到服务器temp下
            file.transferTo(new File(filePath));
        } else {    //破损包判断
            map.put("code", 2);
            return map;
        }
        //保证文件夹路径最后是"/"或者"\"
        char lastChar = savaPicUrl.charAt(savaPicUrl.length() - 1);
        if (lastChar != '/' && lastChar != '\\') {
            savaPicUrl += File.separator;
        }
        // 压缩包里面的文件名
        String s = ZipUtils.readZipFile(filePath);
        //解压后的文件路径
        String fileUrl = savaPicUrl + s.split("/")[0];
        //根据类型，进行相应的解压缩
        String type = filePath.substring(filePath.lastIndexOf(".") + 1);
        if (type.equals("zip")) {
//            zipUtils.unzip( filePath, url+file.getOriginalFilename().substring( 0, file.getOriginalFilename().lastIndexOf( "." ) )+"/" );
            zipUtils.unzip(filePath, savaPicUrl);
        } else if (type.equals("rar")) {
            zipUtils.unrar(filePath, savaPicUrl);
        } else {
            throw new Exception("只支持zip和rar格式的压缩包！");
        }
        //判断导入的包是否为空
        if (zipUtils.getExcelFile(savaPicUrl) == null) {
            map.put("code", 2);
            // 删除压缩包以及文件夹
            File file1 = new File(savaPicUrl);
            delete(file1, file.getOriginalFilename());
            deleteFile(file1);
            return map;
        }
        //读取Excel文件
        File newFile = zipUtils.getExcelFile(fileUrl);
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Object> excelPersonDataList = POIutils.importExcel(inputStream, entity);
        inputStream.close();
        if (excelPersonDataList == null) {
            map.put("code", 3);
            File file1 = new File(savaPicUrl);
            delete(file1, file.getOriginalFilename());
            deleteFile(file1);
            return map;
        }
        map.put("dateStr", dateStr);
        map.put("dPath", dPath);
        map.put("fileUrl", fileUrl);
        map.put("savaPicUrl", savaPicUrl);
        map.put("excelData", excelPersonDataList);
        return map;
    }

    /**
     * 上传人员信息
     * 刘周南
     *
     * @param file
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/importZip", method = RequestMethod.POST)
    public Map<String, Object> importStaffZip(MultipartFile file, HttpSession session) throws Exception {
//    public  Map<String, Object> importStaffZip(MultipartHttpServletRequest request) throws Exception {
//        MultipartFile file = request.getFile("file");
        Map map1 = getExcelData(file, ExcelPersonTable.class);
        if (map1.get("code") != null) {
            return map1;
        }
        Map map = new HashMap();
        //人员数据操作记录表
        PersonDataOperationTable personDataOperationTable = null;
        String savaPicUrl = map1.get("savaPicUrl").toString();
        String fileUrl = map1.get("fileUrl").toString();
        String dateStr = map1.get("dateStr").toString();
        List<ExcelPersonTable> excelPersonTableList = (List<ExcelPersonTable>) map1.get("excelData");

        //  过滤表格中的空格行
        excelPersonTableList = excelPersonTableList.stream()
                .filter(excelPersonTable -> (excelPersonTable.getPersonName() != null && excelPersonTable.getPersonName().trim().length() != 0) || (excelPersonTable.getIdentityNo() != null && excelPersonTable.getIdentityNo().trim().length() != 0) || (excelPersonTable.getBirthday() != null && excelPersonTable.getBirthday().trim().length() != 0) || (excelPersonTable.getResidentialAddress() != null && excelPersonTable.getResidentialAddress().trim().length() != 0) || (excelPersonTable.getEthnicity() != null && excelPersonTable.getEthnicity().trim().length() != 0) || (excelPersonTable.getTelephone() != null && excelPersonTable.getTelephone().trim().length() != 0) || (excelPersonTable.getIdenticationInfo() != null && excelPersonTable.getIdenticationInfo().trim().length() != 0) || (excelPersonTable.getDepartments() != null && excelPersonTable.getDepartments().trim().length() != 0))
                .collect(Collectors.toList());

        System.out.println("导入数据一共【" + (excelPersonTableList.size()) + "】行");
        percentMap.put("AllSize", (double) excelPersonTableList.size());
        percentMap.put("successTotal", 0.00);
        percentMap.put("errorTotal", 0.00);
        // 统一入库时间
        Date createTime = new Date();
        // 定义一个异常人员信息集合
        List<ErrorPersonExcel> errorList = new ArrayList<>();
        try {
            for (ExcelPersonTable excelPersonTable : excelPersonTableList) {
                List<String> stringList = new ArrayList<>();
//                if (!(excelPersonTable.getPersonName() == null||"".equals(excelPersonTable.getPersonName()))&&(excelPersonTable.getBirthday() == null||"".equals(excelPersonTable.getBirthday()))&&(excelPersonTable.getIdentityNo() == null||"".equals(excelPersonTable.getIdentityNo()))&&(excelPersonTable.getIdentityTypeCode() == null||"".equals(excelPersonTable.getIdentityTypeCode()))&&(excelPersonTable.getResidentialAddress() == null||"".equals(excelPersonTable.getResidentialAddress()))){
                // 定义一个判断照片是否对应
                List<Boolean> imgExists = new ArrayList<>();
                //创建一个存储人脸照片对象的集合
                List<ExcelPersonTable> excelPersonTableList1 = new ArrayList<>();
                // 将每个人脸照片都创建一个对象

                // 判断照片为几张
                if (excelPersonTable.getFaceAddress() != null) {
                    //根据 英文逗号 判断是几张照片
                    String[] split = excelPersonTable.getFaceAddress().split(",");
                    //判断人脸照片是否超出3张
                    if (split.length <= 3) {
                        int j = split.length;
                        //判断最后一个是否为逗号
                        if (excelPersonTable.getFaceAddress().endsWith(",")) {
                            j = j - 1;
                        }
                        for (int i = 0; i < j; i++) {
                            ExcelPersonTable excelPersonTable1 = new ExcelPersonTable();
                            excelPersonTable1.setFaceAddress(split[i]);
                            //如果有人脸照片为null则剔除
                            if (StringUtils.isNotBlank(split[i])) {
                                excelPersonTableList1.add(excelPersonTable1);
                            }
                            // 判断是否存在照片
                            File file1 = new File(savaPicUrl + file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")) + "/" + split[i]);
                            if (file1.exists()) {
                                imgExists.add(true);
                            } else {
                                imgExists.add(false);
                            }
                        }
                    }

                }
                //循环遍历存储人脸照片对象的集合
                List<PersonFaceInfomationTable> personFaceInfomationTables = new ArrayList<>();
                PersonTable identityNo = null;
                // 判断是否存在身份证
                if (excelPersonTable.getIdentityNo() != null) {
                    identityNo = personTableService.selectPersonTableByIdentityNo(excelPersonTable.getIdentityNo());
                }
                // 判断是否存在学号或者教工号
                /*PersonTable IdentityTypeCode=null;
                if(excelPersonTable.getIdentityTypeCode()!=null){
                    IdentityTypeCode=personTableService.selectPersonTableByIdentityTypeCode(excelPersonTable.getIdentityTypeCode());
                }*/


                for (ExcelPersonTable table : excelPersonTableList1) {
                    if (table.getFaceAddress() != null) {
                        if (!("jpg".equals(table.getFaceAddress().substring(table.getFaceAddress().lastIndexOf(".") + 1))) && !("png".equals(table.getFaceAddress().substring(table.getFaceAddress().lastIndexOf(".") + 1)))) {
                            stringList.add("人脸照片格式错误！");
                        }
                    }/*else if(table.getCampusCardAddress()!=null){
                        if (!("jpg".equals( table.getCampusCardAddress().substring( table.getCampusCardAddress().lastIndexOf( "." ) + 1 ) )) && !("png".equals( table.getCampusCardAddress().substring( table.getCampusCardAddress().lastIndexOf( "." ) + 1 ) ))) {
                            stringList.add("校园卡正面照格式错误！");
                        }
                    }else if(table.getIdcardImage()!=null){
                        if (!("jpg".equals( table.getIdcardImage().substring( table.getIdcardImage().lastIndexOf( "." ) + 1 ) )) && !("png".equals( table.getIdcardImage().substring( table.getIdcardImage().lastIndexOf( "." ) + 1 ) ))) {
                            stringList.add("身份证照片格式错误！");
                        }
                    }else if(table.getFaceImage()!=null){
                        if (!("jpg".equals( table.getFaceImage().substring( table.getFaceImage().lastIndexOf( "." ) + 1 ) )) && !("png".equals( table.getFaceImage().substring( table.getFaceImage().lastIndexOf( "." ) + 1 ) ))) {
                            stringList.add("人证核验图片格式错误！");
                        }
                    }*/
                }
                // 姓名不为空、手机号码不为空且必须是11位、身份证号码不为空且必须是18位、至少上传一张照片、且照片必须与照片地址对应
//                DataDictionary dataDictionary = new DataDictionary();
//                dataDictionary.setDicType(Constant.DEPARTMENT);
//                dataDictionary.setDicName(excelPersonTable.getDepartments());
//                DataDictionary dataDictionary1 = dataDictionaryService.selectDataDictionaryByDicTypeAndDicName(dataDictionary);
                //验证办公地点
//                String isdepartments = IDCardUtil.isdepartments(excelPersonTable.getDepartments(), dataDictionaryService);
                if (excelPersonTable.getPersonName() == null) {
                    String ss = "姓名为必填项";
                    //姓名为空返回前端
                    ErrorPersonExcel errorPerson = new ErrorPersonExcel();
                    errorPerson.setErrorMsg(ss);
                    errorPerson.setPersonName(excelPersonTable.getPersonName());
                    errorPerson.setGender(excelPersonTable.getGender());
                    errorPerson.setIdentityNo(excelPersonTable.getIdentityNo());
                    errorPerson.setBirthday(excelPersonTable.getBirthday());
                    errorPerson.setResidentialAddress(excelPersonTable.getResidentialAddress());
                    errorPerson.setEthnicity(excelPersonTable.getEthnicity());
                    errorPerson.setTelephone(excelPersonTable.getTelephone());
                    errorPerson.setIdenticationInfo(excelPersonTable.getIdenticationInfo());
                    if (excelPersonTable.getStudentLevel() == null) {
                        errorPerson.setStudentLevel(" ");
                    } else {
                        errorPerson.setStudentLevel(excelPersonTable.getStudentLevel());
                    }
                    if (excelPersonTable.getGrade() == null) {
                        errorPerson.setGrade(" ");
                    } else {
                        errorPerson.setGrade(excelPersonTable.getGrade());
                    }
                    if (excelPersonTable.getDepartments() == null) {
                        errorPerson.setDepartments(" ");
                    } else {
                        errorPerson.setDepartments(excelPersonTable.getDepartments());
                    }
                    errorPerson.setIdentityTypeCode(excelPersonTable.getIdentityTypeCode());
                    errorPerson.setDescription(excelPersonTable.getDescription());
//                            errorPerson.setFaceImage( excelPersonTable.getFaceImage() );
//                            errorPerson.setCampusCardAddress( excelPersonTable.getCampusCardAddress() );
                    errorPerson.setFaceAddress(excelPersonTable.getFaceAddress());
//                            errorPerson.setIdcardImage( excelPersonTable.getIdcardImage() );
                    errorList.add(errorPerson);
//                    percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);

                    percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);

                } else {
                    if (excelPersonTable.getIdentityNo() == null && excelPersonTable.getIdentityTypeCode() == null) {
                        //                       身份证和工牌号都为空返回前端
                        String ss = "身份证和工牌号为二选一";
                        ErrorPersonExcel errorPerson = new ErrorPersonExcel();
                        errorPerson.setErrorMsg(ss);
                        errorPerson.setPersonName(excelPersonTable.getPersonName());
                        errorPerson.setGender(excelPersonTable.getGender());
                        errorPerson.setIdentityNo(excelPersonTable.getIdentityNo());
                        errorPerson.setBirthday(excelPersonTable.getBirthday());
                        errorPerson.setResidentialAddress(excelPersonTable.getResidentialAddress());
                        errorPerson.setEthnicity(excelPersonTable.getEthnicity());
                        errorPerson.setTelephone(excelPersonTable.getTelephone());
                        errorPerson.setIdenticationInfo(excelPersonTable.getIdenticationInfo());
                        if (excelPersonTable.getStudentLevel() == null) {
                            errorPerson.setStudentLevel(" ");
                        } else {
                            errorPerson.setStudentLevel(excelPersonTable.getStudentLevel());
                        }
                        if (excelPersonTable.getGrade() == null) {
                            errorPerson.setGrade(" ");
                        } else {
                            errorPerson.setGrade(excelPersonTable.getGrade());
                        }
                        if (excelPersonTable.getDepartments() == null) {
                            errorPerson.setDepartments(" ");
                        } else {
                            errorPerson.setDepartments(excelPersonTable.getDepartments());
                        }
                        errorPerson.setIdentityTypeCode(excelPersonTable.getIdentityTypeCode());
                        errorPerson.setDescription(excelPersonTable.getDescription());
//                            errorPerson.setFaceImage( excelPersonTable.getFaceImage() );
//                            errorPerson.setCampusCardAddress( excelPersonTable.getCampusCardAddress() );
                        errorPerson.setFaceAddress(excelPersonTable.getFaceAddress());
//                            errorPerson.setIdcardImage( excelPersonTable.getIdcardImage() );
                        errorList.add(errorPerson);

                        percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                    } else {
                        if (excelPersonTable.getIdentityNo() != null && !IDCardUtil.isIDCard(excelPersonTable.getIdentityNo())) {
//                       身份证格式错误
                            String ss = "身份证格式有误";
                            ErrorPersonExcel errorPerson = new ErrorPersonExcel();
                            errorPerson.setErrorMsg(ss);
                            errorPerson.setPersonName(excelPersonTable.getPersonName());
                            errorPerson.setGender(excelPersonTable.getGender());
                            errorPerson.setIdentityNo(excelPersonTable.getIdentityNo());
                            errorPerson.setBirthday(excelPersonTable.getBirthday());
                            errorPerson.setResidentialAddress(excelPersonTable.getResidentialAddress());
                            errorPerson.setEthnicity(excelPersonTable.getEthnicity());
                            errorPerson.setTelephone(excelPersonTable.getTelephone());
                            errorPerson.setIdenticationInfo(excelPersonTable.getIdenticationInfo());
                            if (excelPersonTable.getStudentLevel() == null) {
                                errorPerson.setStudentLevel(" ");
                            } else {
                                errorPerson.setStudentLevel(excelPersonTable.getStudentLevel());
                            }
                            if (excelPersonTable.getGrade() == null) {
                                errorPerson.setGrade(" ");
                            } else {
                                errorPerson.setGrade(excelPersonTable.getGrade());
                            }
                            if (excelPersonTable.getDepartments() == null) {
                                errorPerson.setDepartments(" ");
                            } else {
                                errorPerson.setDepartments(excelPersonTable.getDepartments());
                            }
                            errorPerson.setIdentityTypeCode(excelPersonTable.getIdentityTypeCode());
                            errorPerson.setDescription(excelPersonTable.getDescription());
//                            errorPerson.setFaceImage( excelPersonTable.getFaceImage() );
//                            errorPerson.setCampusCardAddress( excelPersonTable.getCampusCardAddress() );
                            errorPerson.setFaceAddress(excelPersonTable.getFaceAddress());
//                            errorPerson.setIdcardImage( excelPersonTable.getIdcardImage() );
                            errorList.add(errorPerson);
                            percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                        } else {
                            if (excelPersonTable.getIdentityTypeCode() != null && !IDCardUtil.isIdentityTypeCode(excelPersonTable.getIdentityTypeCode(), personTableService)) {
//                        工牌号格式错误或工牌号已存在
                                String ss = "工牌号格式错误或工牌号已存在";
                                ErrorPersonExcel errorPerson = new ErrorPersonExcel();
                                errorPerson.setErrorMsg(ss);
                                errorPerson.setPersonName(excelPersonTable.getPersonName());
                                errorPerson.setGender(excelPersonTable.getGender());
                                errorPerson.setIdentityNo(excelPersonTable.getIdentityNo());
                                errorPerson.setBirthday(excelPersonTable.getBirthday());
                                errorPerson.setResidentialAddress(excelPersonTable.getResidentialAddress());
                                errorPerson.setEthnicity(excelPersonTable.getEthnicity());
                                errorPerson.setTelephone(excelPersonTable.getTelephone());
                                errorPerson.setIdenticationInfo(excelPersonTable.getIdenticationInfo());
                                if (excelPersonTable.getStudentLevel() == null) {
                                    errorPerson.setStudentLevel(" ");
                                } else {
                                    errorPerson.setStudentLevel(excelPersonTable.getStudentLevel());
                                }
                                if (excelPersonTable.getGrade() == null) {
                                    errorPerson.setGrade(" ");
                                } else {
                                    errorPerson.setGrade(excelPersonTable.getGrade());
                                }
                                if (excelPersonTable.getDepartments() == null) {
                                    errorPerson.setDepartments(" ");
                                } else {
                                    errorPerson.setDepartments(excelPersonTable.getDepartments());
                                }
                                errorPerson.setIdentityTypeCode(excelPersonTable.getIdentityTypeCode());
                                errorPerson.setDescription(excelPersonTable.getDescription());
//                            errorPerson.setFaceImage( excelPersonTable.getFaceImage() );
//                            errorPerson.setCampusCardAddress( excelPersonTable.getCampusCardAddress() );
                                errorPerson.setFaceAddress(excelPersonTable.getFaceAddress());
//                            errorPerson.setIdcardImage( excelPersonTable.getIdcardImage() );
                                errorList.add(errorPerson);

                                percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                            } else {
                                if (excelPersonTable.getIdentityNo() != null && personTableService.selectPersonTableByIdentityNo(excelPersonTable.getIdentityNo()) != null) {
                                    //身份证存在
                                    String ss = "身份证号已存在";
                                    ErrorPersonExcel errorPerson = new ErrorPersonExcel();
                                    errorPerson.setErrorMsg(ss);
                                    errorPerson.setPersonName(excelPersonTable.getPersonName());
                                    errorPerson.setGender(excelPersonTable.getGender());
                                    errorPerson.setIdentityNo(excelPersonTable.getIdentityNo());
                                    errorPerson.setBirthday(excelPersonTable.getBirthday());
                                    errorPerson.setResidentialAddress(excelPersonTable.getResidentialAddress());
                                    errorPerson.setEthnicity(excelPersonTable.getEthnicity());
                                    errorPerson.setTelephone(excelPersonTable.getTelephone());
                                    errorPerson.setIdenticationInfo(excelPersonTable.getIdenticationInfo());
                                    if (excelPersonTable.getStudentLevel() == null) {
                                        errorPerson.setStudentLevel(" ");
                                    } else {
                                        errorPerson.setStudentLevel(excelPersonTable.getStudentLevel());
                                    }
                                    if (excelPersonTable.getGrade() == null) {
                                        errorPerson.setGrade(" ");
                                    } else {
                                        errorPerson.setGrade(excelPersonTable.getGrade());
                                    }
                                    if (excelPersonTable.getDepartments() == null) {
                                        errorPerson.setDepartments(" ");
                                    } else {
                                        errorPerson.setDepartments(excelPersonTable.getDepartments());
                                    }
                                    errorPerson.setIdentityTypeCode(excelPersonTable.getIdentityTypeCode());
                                    errorPerson.setDescription(excelPersonTable.getDescription());
//                            errorPerson.setFaceImage( excelPersonTable.getFaceImage() );
//                            errorPerson.setCampusCardAddress( excelPersonTable.getCampusCardAddress() );
                                    errorPerson.setFaceAddress(excelPersonTable.getFaceAddress());
//                            errorPerson.setIdcardImage( excelPersonTable.getIdcardImage() );
                                    errorList.add(errorPerson);

                                    percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                } else {
                                    if (excelPersonTable.getTelephone() != null && !IDCardUtil.isPhone(excelPersonTable.getTelephone())) {
//                                         手机号异常
                                        String ss = "手机号格式不正确";
                                        ErrorPersonExcel errorPerson = new ErrorPersonExcel();
                                        errorPerson.setErrorMsg(ss);
                                        errorPerson.setPersonName(excelPersonTable.getPersonName());
                                        errorPerson.setGender(excelPersonTable.getGender());
                                        errorPerson.setIdentityNo(excelPersonTable.getIdentityNo());
                                        errorPerson.setBirthday(excelPersonTable.getBirthday());
                                        errorPerson.setResidentialAddress(excelPersonTable.getResidentialAddress());
                                        errorPerson.setEthnicity(excelPersonTable.getEthnicity());
                                        errorPerson.setTelephone(excelPersonTable.getTelephone());
                                        errorPerson.setIdenticationInfo(excelPersonTable.getIdenticationInfo());
                                        if (excelPersonTable.getStudentLevel() == null) {
                                            errorPerson.setStudentLevel(" ");
                                        } else {
                                            errorPerson.setStudentLevel(excelPersonTable.getStudentLevel());
                                        }
                                        if (excelPersonTable.getGrade() == null) {
                                            errorPerson.setGrade(" ");
                                        } else {
                                            errorPerson.setGrade(excelPersonTable.getGrade());
                                        }
                                        if (excelPersonTable.getDepartments() == null) {
                                            errorPerson.setDepartments(" ");
                                        } else {
                                            errorPerson.setDepartments(excelPersonTable.getDepartments());
                                        }
                                        errorPerson.setIdentityTypeCode(excelPersonTable.getIdentityTypeCode());
                                        errorPerson.setDescription(excelPersonTable.getDescription());
//                            errorPerson.setFaceImage( excelPersonTable.getFaceImage() );
//                            errorPerson.setCampusCardAddress( excelPersonTable.getCampusCardAddress() );
                                        errorPerson.setFaceAddress(excelPersonTable.getFaceAddress());
//                            errorPerson.setIdcardImage( excelPersonTable.getIdcardImage() );
                                        errorList.add(errorPerson);
                                        percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);

                                    } else {
                                        String i = baseOrganizitionMapper.queryBaseOrganizitionByPid(excelPersonTable.getSchoolclass());
                                        if (i == null || "".equals(i)) {
                                            String ss = "组织机构信息暂无录入系统";
                                            ErrorPersonExcel errorPerson = new ErrorPersonExcel();
                                            errorPerson.setErrorMsg(ss);
                                            errorPerson.setPersonName(excelPersonTable.getPersonName());
                                            errorPerson.setGender(excelPersonTable.getGender());
                                            errorPerson.setIdentityNo(excelPersonTable.getIdentityNo());
                                            errorPerson.setBirthday(excelPersonTable.getBirthday());
                                            errorPerson.setResidentialAddress(excelPersonTable.getResidentialAddress());
                                            errorPerson.setEthnicity(excelPersonTable.getEthnicity());
                                            errorPerson.setTelephone(excelPersonTable.getTelephone());
                                            errorPerson.setIdenticationInfo(excelPersonTable.getIdenticationInfo());
                                            if (excelPersonTable.getStudentLevel() == null) {
                                                errorPerson.setStudentLevel(" ");
                                            } else {
                                                errorPerson.setStudentLevel(excelPersonTable.getStudentLevel());
                                            }
                                            if (excelPersonTable.getGrade() == null) {
//                                                errorPerson.setGrade(" ");
//                                            } else {
//                                                errorPerson.setGrade(excelPersonTable.getGrade());
                                            }
                                            if (excelPersonTable.getDepartments() == null) {
                                                errorPerson.setDepartments(" ");
                                            } else {
                                                errorPerson.setDepartments(excelPersonTable.getDepartments());
                                            }
                                            errorPerson.setIdentityTypeCode(excelPersonTable.getIdentityTypeCode());
                                           errorPerson.setDescription(excelPersonTable.getDescription());
//                            errorPerson.setFaceImage( excelPersonTable.getFaceImage() );
//                          errorPerson.setCampusCardAddress( excelPersonTable.getCampusCardAddress() );
                                            errorPerson.setFaceAddress(excelPersonTable.getFaceAddress());
//                            errorPerson.setIdcardImage( excelPersonTable.getIdcardImage() );
                                            errorList.add(errorPerson);
                                           percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);

                                        } else {
                                        if (excelPersonTable.getBirthday() != null && !IDCardUtil.isLegalDate(excelPersonTable.getBirthday())) {
                                            String ss = "生日格式不正确";
                                            ErrorPersonExcel errorPerson = new ErrorPersonExcel();
                                            errorPerson.setErrorMsg(ss);
                                            errorPerson.setPersonName(excelPersonTable.getPersonName());
                                            errorPerson.setGender(excelPersonTable.getGender());
                                            errorPerson.setIdentityNo(excelPersonTable.getIdentityNo());
                                            errorPerson.setBirthday(excelPersonTable.getBirthday());
                                            errorPerson.setResidentialAddress(excelPersonTable.getResidentialAddress());
                                            errorPerson.setEthnicity(excelPersonTable.getEthnicity());
                                            errorPerson.setTelephone(excelPersonTable.getTelephone());
                                            errorPerson.setIdenticationInfo(excelPersonTable.getIdenticationInfo());
                                            if (excelPersonTable.getStudentLevel() == null) {
                                                errorPerson.setStudentLevel(" ");
                                            } else {
                                                errorPerson.setStudentLevel(excelPersonTable.getStudentLevel());
                                            }
                                            if (excelPersonTable.getGrade() == null) {
                                                errorPerson.setGrade(" ");
                                            } else {
                                                errorPerson.setGrade(excelPersonTable.getGrade());
                                            }
                                            if (excelPersonTable.getDepartments() == null) {
                                                errorPerson.setDepartments(" ");
                                            } else {
                                                errorPerson.setDepartments(excelPersonTable.getDepartments());
                                            }
                                            errorPerson.setIdentityTypeCode(excelPersonTable.getIdentityTypeCode());
                                            errorPerson.setDescription(excelPersonTable.getDescription());
//                            errorPerson.setFaceImage( excelPersonTable.getFaceImage() );
//                            errorPerson.setCampusCardAddress( excelPersonTable.getCampusCardAddress() );
                                            errorPerson.setFaceAddress(excelPersonTable.getFaceAddress());
//                            errorPerson.setIdcardImage( excelPersonTable.getIdcardImage() );
                                            errorList.add(errorPerson);
                                            percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                        } else {
                                            if (excelPersonTableList1 != null && excelPersonTableList1.size() == 0) {
                                                String ss = "人脸照片最多三张";
                                                ErrorPersonExcel errorPerson = new ErrorPersonExcel();
                                                errorPerson.setErrorMsg(ss);
                                                errorPerson.setPersonName(excelPersonTable.getPersonName());
                                                errorPerson.setGender(excelPersonTable.getGender());
                                                errorPerson.setIdentityNo(excelPersonTable.getIdentityNo());
                                                errorPerson.setBirthday(excelPersonTable.getBirthday());
                                                errorPerson.setResidentialAddress(excelPersonTable.getResidentialAddress());
                                                errorPerson.setEthnicity(excelPersonTable.getEthnicity());
                                                errorPerson.setTelephone(excelPersonTable.getTelephone());
                                                errorPerson.setIdenticationInfo(excelPersonTable.getIdenticationInfo());
                                                if (excelPersonTable.getStudentLevel() == null) {
                                                    errorPerson.setStudentLevel(" ");
                                                } else {
                                                    errorPerson.setStudentLevel(excelPersonTable.getStudentLevel());
                                                }
                                                if (excelPersonTable.getGrade() == null) {
                                                    errorPerson.setGrade(" ");
                                                } else {
                                                    errorPerson.setGrade(excelPersonTable.getGrade());
                                                }
                                                if (excelPersonTable.getDepartments() == null) {
                                                    errorPerson.setDepartments(" ");
                                                } else {
                                                    errorPerson.setDepartments(excelPersonTable.getDepartments());
                                                }
                                                errorPerson.setIdentityTypeCode(excelPersonTable.getIdentityTypeCode());
                                                errorPerson.setDescription(excelPersonTable.getDescription());
//                            errorPerson.setFaceImage( excelPersonTable.getFaceImage() );
//                            errorPerson.setCampusCardAddress( excelPersonTable.getCampusCardAddress() );
                                                errorPerson.setFaceAddress(excelPersonTable.getFaceAddress());
//                            errorPerson.setIdcardImage( excelPersonTable.getIdcardImage() );
                                                errorList.add(errorPerson);
                                                percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                            } else {
                                                //正确信息
                                                PersonTable personTable = new PersonTable();
                                                if("8".equals(excelPersonTable.getIdenticationInfo())){
                                                    //查询所选 学校/年级/班级 信息进行拼接 学校编码_年级编码_班级编码_学工号 = 人员唯一标识
                                                    ExcelPersonTable personNewTable1 = personTableService.queryExcelPersonTableByOrgId(excelPersonTable);
                                                    if(personNewTable1 != null){
                                                        personTable.setPersonnelUniqueCode(personNewTable1.getOrgName()+"_"+personNewTable1.getIdentityTypeCode());
                                                    }
                                                }
                                                personTable.setPersonName(excelPersonTable.getPersonName());
                                                personTable.setGender(excelPersonTable.getGender());
                                                personTable.setIdentityNo(excelPersonTable.getIdentityNo());
                                                personTable.setResidentialAddress(excelPersonTable.getResidentialAddress());
                                                personTable.setBirthday(excelPersonTable.getBirthday());
                                                personTable.setEthnicity(excelPersonTable.getEthnicity());
                                                personTable.setTelephone(excelPersonTable.getTelephone());
                                                personTable.setIdenticationInfo(excelPersonTable.getIdenticationInfo());
                                                personTable.setStudentLevel(excelPersonTable.getStudentLevel());
                                                personTable.setGrade(excelPersonTable.getGrade());
                                                personTable.setDepartments(excelPersonTable.getDepartments());
//                                                    personTable.setDepartments(isdepartments);
                                                personTable.setIdentityTypeCode(excelPersonTable.getIdentityTypeCode());
                                                personTable.setDescription(excelPersonTable.getDescription());
                                                personTable.setSchoolClass(excelPersonTable.getSchoolclass());
                                                personTable.setParkId(excelPersonTable.getParkname());
                                                // 统一数据入库时间
                                                personTable.setCreateTime(createTime);
                                                PersonTable existPerson = personTableService.queryPersonTableByIdentityNo(excelPersonTable.getIdentityNo());
                                                if (existPerson != null) {
                                                    Integer[] personId = {existPerson.getPersonId()};
                                                    personTableService.deletePersontableByPersonId(personId);
                                                    personFaceInfomationTableService.deletePersonTableByPersonId(existPerson.getPersonId());
                                                    // 将人员基础信息存入数据库
                                                    personTableService.insertPersonNewTable(personTable);
                                                    //添加人员更新日志  添加到人员数据更新表中
                                                    personDataOperationTable = PersonDataOperationTableUtils.psotPersonDataOperationTable(personTable);
                                                    personDataOperationTableService.insertPersonDataOperationTable(personDataOperationTable);
                                                    // 将图片信息添加到数据库
                                                    for (ExcelPersonTable excelPersonTable5 : excelPersonTableList1) {
                                                        PersonFaceInfomationTable personFaceInfomationTable = new PersonFaceInfomationTable();
                                                        // 通过添加人员信息返回的personid 给人脸照片的personid赋值
                                                        personFaceInfomationTable.setPersonId(personTable.getPersonId());
                                                        if (excelPersonTable5.getFaceAddress() != null) {
                                                            String uuid = UUID.randomUUID().toString() + ".";
                                                            String prefix = excelPersonTable5.getFaceAddress().substring(excelPersonTable5.getFaceAddress().lastIndexOf(".") + 1);
                                                            String newName = uuid + prefix;
                                                            int flag = ZipUtils.savePic(fileUrl, savaPicUrl, excelPersonTable5.getFaceAddress(), newName);
                                                            personFaceInfomationTable.setDataSource("3");
                                                            personFaceInfomationTable.setIdentification("2");
                                                            personFaceInfomationTable.setFaceAddress(dateStr + "/" + newName);
                                                        }

                                                        personFaceInfomationTables.add(personFaceInfomationTable);
                                                    }
                                                    personFaceInfomationTableService.insertPersonNewFaceInfomationTable(personFaceInfomationTables);
                                                } else {
                                                    // 将人员基础信息存入数据库
                                                    personTableService.insertPersonNewTable(personTable);
                                                    //添加人员更新日志  添加到人员数据更新表中
                                                    personDataOperationTable = PersonDataOperationTableUtils.psotPersonDataOperationTable(personTable);
                                                    personDataOperationTableService.insertPersonDataOperationTable(personDataOperationTable);
                                                    // 将图片信息添加到数据
                                                    for (ExcelPersonTable excelPersonTable5 : excelPersonTableList1) {
                                                        PersonFaceInfomationTable personFaceInfomationTable = new PersonFaceInfomationTable();
                                                        // 通过添加人员信息返回的personid 给人脸照片的personid赋值
                                                        personFaceInfomationTable.setPersonId(personTable.getPersonId());
                                                        if (excelPersonTable5.getFaceAddress() != null) {
                                                            String uuid = UUID.randomUUID().toString() + ".";
                                                            String prefix = excelPersonTable5.getFaceAddress().substring(excelPersonTable5.getFaceAddress().lastIndexOf(".") + 1);
                                                            String newName = uuid + prefix;
                                                            int flag = ZipUtils.savePic(fileUrl, savaPicUrl, excelPersonTable5.getFaceAddress(), newName);
                                                            personFaceInfomationTable.setDataSource("3");
                                                            personFaceInfomationTable.setIdentification("2");
                                                            personFaceInfomationTable.setFaceAddress(dateStr + "/" + newName);
                                                        }
                                                        personFaceInfomationTables.add(personFaceInfomationTable);
                                                    }
                                                    personFaceInfomationTableService.insertPersonNewFaceInfomationTable(personFaceInfomationTables);
                                                }
                                                percentMap.replace("successTotal", (double) percentMap.get("successTotal") + 1.00);

                                            }
                                        }
                                        }

                                    }


                                }
                            }
                        }


                    }


                }
                session.setAttribute("errorPersonList", errorList);
                // 判断导入的数据是否有异常数据
                if (new Double((double) percentMap.get("errorTotal")).intValue() == 0) {
                    map.put("code", 0);
                    map.put("successTotal", new Double((double) percentMap.get("successTotal")).intValue());
                } else {
                    map.put("code", 1);
                    map.put("successTotal", new Double((double) percentMap.get("successTotal")).intValue());
                    map.put("errorTotal", new Double((double) percentMap.get("errorTotal")).intValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 删除压缩包以及文件夹
            File file1 = new File(savaPicUrl);
            delete(file1, file.getOriginalFilename());
            File file2 = new File(fileUrl);
            deleteFile(file2);
        }
        return map;
    }

    /**
     * 刘周南
     *
     * @param response
     * @throws Exception
     */
    // 下载上传人员信息模板
    @RequestMapping(value = "/downloadZip")
    public void downloadZip(HttpServletResponse response) throws Exception {
        // File file = ResourceUtils.getFile("classpath:templates/uploadfile/人员信息模板及说明.zip");
        InputStream in = getClass().getResourceAsStream("/templates/uploadfile/批量新增人员信息模板及说明.zip");
        try (// 以流的形式下载文件。
             BufferedInputStream fis = new BufferedInputStream(in);
             BufferedOutputStream toClient = new BufferedOutputStream(response.getOutputStream())) {
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("批量新增人员信息模板及说明.zip", "UTF-8"));
            int len = 0;
            while ((len = fis.read()) != -1) {
                toClient.write(len);
                toClient.flush();
            }
            // 关闭流
            fis.close();
            toClient.close();
        } catch (IOException ex) {
            throw new Exception("下载打包的文件失败！！");
        }
    }

    // 下载删除人员信息模板
    @RequestMapping(value = "/downdeleteZip")
    public void downdeleteZip(HttpServletResponse response) throws Exception {
        // File file = ResourceUtils.getFile("classpath:templates/uploadfile/人员信息模板及说明.zip");
        InputStream in = getClass().getResourceAsStream("/templates/uploadfile/删除人员信息模板及说明.zip");
        try (// 以流的形式下载文件。
             BufferedInputStream fis = new BufferedInputStream(in);
             BufferedOutputStream toClient = new BufferedOutputStream(response.getOutputStream())) {
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("删除人员信息模板及说明.zip", "UTF-8"));
            int len = 0;
            while ((len = fis.read()) != -1) {
                toClient.write(len);
                toClient.flush();
            }
            // 关闭流
            fis.close();
            toClient.close();
        } catch (IOException ex) {
            throw new Exception("下载打包的文件失败！！");
        }
    }


    // 下载修改人员信息模板
    @RequestMapping(value = "/downupdateZip")
    public void downupdateZip(HttpServletResponse response) throws Exception {
        // File file = ResourceUtils.getFile("classpath:templates/uploadfile/人员信息模板及说明.zip");
        InputStream in = getClass().getResourceAsStream("/templates/uploadfile/修改人员信息说明.zip");
        try (// 以流的形式下载文件。
             BufferedInputStream fis = new BufferedInputStream(in);
             BufferedOutputStream toClient = new BufferedOutputStream(response.getOutputStream())) {
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("修改人员信息说明.zip", "UTF-8"));
            int len = 0;
            while ((len = fis.read()) != -1) {
                toClient.write(len);
                toClient.flush();
            }
            // 关闭流
            fis.close();
            toClient.close();
        } catch (IOException ex) {
            throw new Exception("下载打包的文件失败！！");
        }
    }

    File file1 = new File(System.getProperty("user.dir") + File.separator + "application.properties");

    Config jdbcCg = Config.getInstance(file1.getPath());

    // 导出异常人员信息
    @RequestMapping(value = "/exportErrorExcel")
    public void exportErrorExcel(HttpServletResponse response, HttpSession session) {
        try {
            // 设置响应输出的头类型
            response.setHeader("content-Type", "application/vnd.ms-excel");
            // 下载文件的默认名称
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode("异常人员信息" + ".xls", "UTF-8"));

            ExportParams deptExportParams = new ExportParams();
            // 设置sheet得名称
            deptExportParams.setSheetName("异常人员信息表");

            Map<String, Object> deptExportMap = new HashMap<>();
            // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
            deptExportMap.put("title", deptExportParams);
            // 模版导出对应得实体类型
            deptExportMap.put("entity", ErrorPersonExcel.class);
            // sheet中要填充得数据
            List<ExcelPersonTable> errorList = (List) session.getAttribute("errorPersonList");
            deptExportMap.put("data", errorList);
            session.removeAttribute("errorPersonList");
            List<Map<String, Object>> sheetsList = new ArrayList<>();
            sheetsList.add(deptExportMap);
            Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/uploadTxt")
    public Map<String, Object> upload(MultipartFile file) throws IOException {
        Map<String, Object> map = new HashMap<>();
        OutputStream out = null;
        try {
            File desFile = new File(ResourceUtils.getURL("classpath:static").getPath().replace("%20", " ").replace('/', '\\') + "/" + file.getOriginalFilename());
            if (!desFile.getParentFile().exists()) {
                System.out.println(desFile.getPath());
                //writable --如果为true，允许写访问权限;如果为false，写访问权限是不允许的。
                //ownerOnly --如果为true，则写访问权限仅适用于所有者，否则它适用于所有人。
                desFile.setWritable(true, false);
                desFile.getParentFile().mkdirs();
                desFile.createNewFile();
                desFile.setReadable(true);
            }
            file.transferTo(new File(System.getProperty("user.dir") + File.separator + String.valueOf(desFile)));
//                    file.transferTo(desFile);
            AnonymousAddress anonymousAddress = new AnonymousAddress();
            anonymousAddress.setAnonymousUrl("/" + file.getOriginalFilename());

            jdbcCg.setProperty("wxConfig", file.getOriginalFilename());
            anonymousAddress.setAnonymousName("配置微信公众号上传txt文件至项目根目录");

//            查询微信公众号配置路径是否在权限表存在

            AnonymousAddress anonymousAddress1 = anonymousUrlService.selectByAnonymousUrl(anonymousAddress);
            CommonAddress commonAddress = new CommonAddress();
            if (anonymousAddress1 == null) {
                anonymousUrlService.insertSelective(anonymousAddress);
            } else {
                commonAddress.setCommonUrl(anonymousAddress1.getAnonymousUrl());
                commonAddress.setUrlName(" ");
                commonAddress.setSort(commonUrlService.selectMaxSort() - 1);
                commonAddress.setFilter("anon");
                anonymousAddress1.setAnonymousUrl("/" + file.getOriginalFilename());
                anonymousUrlService.updateByPrimaryKey(anonymousAddress1);
            }
            //            查询微信公众号配置路径是否在权限表存在  根据url
            CommonAddress commonAddress1 = commonUrlService.selectByCommonUrl(commonAddress);
            commonAddress.setCommonUrl("/" + file.getOriginalFilename());
//            不存在添加
            if (commonAddress1 == null)
                commonUrlService.insertSelective(commonAddress);
            else {
//                先设置最新的url
                commonAddress1.setCommonUrl("/" + file.getOriginalFilename());
                commonUrlService.updateByPrimaryKey(commonAddress1);
            }
            map.put("code", 0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
            }
        }
        return map;

    }


    // 获取上传进度
    @RequestMapping(value = "/uploadStatus")
    public Map<String, Object> getProgress() {
        Map<String, Object> map = new HashMap<>();
        if (percentMap.get("AllSize") != null && percentMap.get("successTotal") != null) {
            double allSize = (double) percentMap.get("AllSize");
            double successTotal = (double) percentMap.get("successTotal");
            double errorTotal = (double) percentMap.get("errorTotal");
            double precent = (successTotal / allSize) * 100;
            Double D1 = new Double(allSize);
            map.put("allSize", D1.intValue());
            Double D2 = new Double(successTotal);
            map.put("successTotal", D2.intValue());
            Double D3 = new Double(errorTotal);
            map.put("errorTotal", D3.intValue());
            Double D4 = new Double(precent);
            map.put("precent", D4.intValue());
        }
        return map;
    }


    /*
     * excel批量删除
     * */
    @RequestMapping(value = "/deletePersonByExcel", method = RequestMethod.POST)
    public Map<String, Object> deletePersonByExcel(MultipartFile file, HttpSession session) throws Exception {

        Map map1 = getExcelData(file, ExcelPersonDelete.class);
        if (map1.get("code") != null) {
            return map1;
        }
        Map map = new HashMap();
        //人员数据操作记录表
        PersonDataOperationTable personDataOperationTable = null;
        String savaPicUrl = map1.get("savaPicUrl").toString();
        String fileUrl = map1.get("fileUrl").toString();
        String dPath = map1.get("dPath").toString();
        List<ExcelPersonDelete> ExcelPersonDeleteList = (List<ExcelPersonDelete>) map1.get("excelData");
        //  过滤表格中的空格行
        ExcelPersonDeleteList = ExcelPersonDeleteList.stream()
                .filter(excelPersonDelete -> (excelPersonDelete.getPersonName() != null && excelPersonDelete.getPersonName().trim().length() != 0) || (excelPersonDelete.getIdentityNo() != null && excelPersonDelete.getIdentityNo().trim().length() != 0) || (excelPersonDelete.getIdentityTypeCode() != null && excelPersonDelete.getIdentityTypeCode().trim().length() != 0))
                .collect(Collectors.toList());

        System.out.println("导入数据一共【" + (ExcelPersonDeleteList.size()) + "】行");

        percentMap.put("AllSize", (double) ExcelPersonDeleteList.size());
        percentMap.put("successTotal", 0.00);
        percentMap.put("errorTotal", 0.00);

        List<ErrorExcelPersonDelete> errorList = new ArrayList<>();
        try {
            for (ExcelPersonDelete excelPersonDelete : ExcelPersonDeleteList) {

                if (excelPersonDelete.getPersonName() != null) {
                    // 身份证号码与教工号都不为空
                    if (excelPersonDelete.getIdentityNo() != null && excelPersonDelete.getIdentityTypeCode() != null) {
                        // 身份证号18位
                        if (excelPersonDelete.getIdentityNo().length() == 18) {
                            // 根据 姓名 身份证 教工号查询该人员信息
                            PersonTable personTable = personTableService.SelectDeletePersonId(excelPersonDelete);
                            if (personTable != null) {
                                Integer[] personId = {personTable.getPersonId()};
                                // 删除人员基础信息
                                int i = personTableService.deletePersonTableByUserId(personId);
                                if (i == 1) {
                                    List<PersonFaceInfomationTable> personFaceInfomationTables = personFaceInfomationTableService.selectImageList(personTable.getPersonId());
                                    // 删除人员照片信息
                                    int i1 = personFaceInfomationTableService.deletePersonTableByImagePersonId(personTable.getPersonId());
                                    for (PersonFaceInfomationTable personFaceInfomationTable : personFaceInfomationTables) {
                                        if (personFaceInfomationTable.getFaceAddress() != null) {
                                            File file1 = new File(dPath + personFaceInfomationTable.getFaceAddress());
                                            if (file1.exists()) {
                                                file1.delete();
                                            }
                                        }
                                        if (personFaceInfomationTable.getCampusCardAddress() != null) {
                                            File file1 = new File(dPath + personFaceInfomationTable.getCampusCardAddress());
                                            if (file1.exists()) {
                                                file1.delete();
                                            }
                                        }
                                        if (personFaceInfomationTable.getFaceImage() != null) {
                                            File file1 = new File(dPath + personFaceInfomationTable.getFaceImage());
                                            if (file1.exists()) {
                                                file1.delete();
                                            }
                                        }
                                        if (personFaceInfomationTable.getIdcardImage() != null) {
                                            File file1 = new File(dPath + personFaceInfomationTable.getIdcardImage());
                                            if (file1.exists()) {
                                                file1.delete();
                                            }
                                        }
                                    }
                                    percentMap.replace("successTotal", (double) percentMap.get("successTotal") + 1.00);
                                }
                            } else {
                                String ss = "该人员不存在！";
                                ErrorExcelPersonDelete errorExcelPersonDelete = new ErrorExcelPersonDelete();
                                errorExcelPersonDelete.setErrorMsg(ss);
                                errorExcelPersonDelete.setPersonName(excelPersonDelete.getPersonName());
                                errorExcelPersonDelete.setIdentityNo(excelPersonDelete.getIdentityNo());
                                errorExcelPersonDelete.setIdentityTypeCode(excelPersonDelete.getIdentityTypeCode());
                                errorList.add(errorExcelPersonDelete);
                                percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                            }
                        } else {

                            // 身份证长度不符
                            excelPersonDelete.setIdentityNo("");
                            // 根据 姓名 身份证 教工号查询该人员信息
                            PersonTable personTable = personTableService.SelectDeletePersonId(excelPersonDelete);
                            if (personTable != null) {
                                Integer[] personId = {personTable.getPersonId()};
                                // 删除人员基础信息
                                int i = personTableService.deletePersonTableByUserId(personId);
                                if (i == 1) {
                                    List<PersonFaceInfomationTable> personFaceInfomationTables = personFaceInfomationTableService.selectImageList(personTable.getPersonId());
                                    // 删除人员照片信息
                                    int i1 = personFaceInfomationTableService.deletePersonTableByImagePersonId(personTable.getPersonId());
                                    for (PersonFaceInfomationTable personFaceInfomationTable : personFaceInfomationTables) {
                                        if (personFaceInfomationTable.getFaceAddress() != null) {
                                            File file1 = new File(dPath + personFaceInfomationTable.getFaceAddress());
                                            if (file1.exists()) {
                                                file1.delete();
                                            }
                                        }
                                        if (personFaceInfomationTable.getCampusCardAddress() != null) {
                                            File file1 = new File(dPath + personFaceInfomationTable.getCampusCardAddress());
                                            if (file1.exists()) {
                                                file1.delete();
                                            }
                                        }
                                        if (personFaceInfomationTable.getFaceImage() != null) {
                                            File file1 = new File(dPath + personFaceInfomationTable.getFaceImage());
                                            if (file1.exists()) {
                                                file1.delete();
                                            }
                                        }
                                        if (personFaceInfomationTable.getIdcardImage() != null) {
                                            File file1 = new File(dPath + personFaceInfomationTable.getIdcardImage());
                                            if (file1.exists()) {
                                                file1.delete();
                                            }
                                        }
                                    }
                                    percentMap.replace("successTotal", (double) percentMap.get("successTotal") + 1.00);
                                }
                            } else {
                                String ss = "该人员不存在！";
                                ErrorExcelPersonDelete errorExcelPersonDelete = new ErrorExcelPersonDelete();
                                errorExcelPersonDelete.setErrorMsg(ss);
                                errorExcelPersonDelete.setPersonName(excelPersonDelete.getPersonName());
                                errorExcelPersonDelete.setIdentityNo(excelPersonDelete.getIdentityNo());
                                errorExcelPersonDelete.setIdentityTypeCode(excelPersonDelete.getIdentityTypeCode());
                                errorList.add(errorExcelPersonDelete);
                                percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                            }
                        }
                    } else if (excelPersonDelete.getIdentityTypeCode() == null && excelPersonDelete.getIdentityNo() != null) {

                        if (excelPersonDelete.getIdentityNo().length() == 18) {

                            // 根据 姓名 身份证 教工号查询该人员信息
                            PersonTable personTable = personTableService.SelectDeletePersonId(excelPersonDelete);
                            if (personTable != null) {
                                Integer[] personId = {personTable.getPersonId()};
                                // 删除人员基础信息
                                int i = personTableService.deletePersonTableByUserId(personId);
                                if (i == 1) {
                                    List<PersonFaceInfomationTable> personFaceInfomationTables = personFaceInfomationTableService.selectImageList(personTable.getPersonId());
                                    // 删除人员照片信息
                                    int i1 = personFaceInfomationTableService.deletePersonTableByImagePersonId(personTable.getPersonId());
                                    for (PersonFaceInfomationTable personFaceInfomationTable : personFaceInfomationTables) {
                                        if (personFaceInfomationTable.getFaceAddress() != null) {
                                            File file1 = new File(dPath + personFaceInfomationTable.getFaceAddress());
                                            if (file1.exists()) {
                                                file1.delete();
                                            }
                                        }
                                        if (personFaceInfomationTable.getCampusCardAddress() != null) {
                                            File file1 = new File(dPath + personFaceInfomationTable.getCampusCardAddress());
                                            if (file1.exists()) {
                                                file1.delete();
                                            }
                                        }
                                        if (personFaceInfomationTable.getFaceImage() != null) {
                                            File file1 = new File(dPath + personFaceInfomationTable.getFaceImage());
                                            if (file1.exists()) {
                                                file1.delete();
                                            }
                                        }
                                        if (personFaceInfomationTable.getIdcardImage() != null) {
                                            File file1 = new File(dPath + personFaceInfomationTable.getIdcardImage());
                                            if (file1.exists()) {
                                                file1.delete();
                                            }
                                        }
                                    }
                                    percentMap.replace("successTotal", (double) percentMap.get("successTotal") + 1.00);
                                }
                            } else {
                                String ss = "该人员不存在！";
                                ErrorExcelPersonDelete errorExcelPersonDelete = new ErrorExcelPersonDelete();
                                errorExcelPersonDelete.setErrorMsg(ss);
                                errorExcelPersonDelete.setPersonName(excelPersonDelete.getPersonName());
                                errorExcelPersonDelete.setIdentityNo(excelPersonDelete.getIdentityNo());
                                errorExcelPersonDelete.setIdentityTypeCode(excelPersonDelete.getIdentityTypeCode());
                                errorList.add(errorExcelPersonDelete);
                                percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                            }
                        } else {
                            String ss = "身份证号码长度不符！";
                            ErrorExcelPersonDelete errorExcelPersonDelete = new ErrorExcelPersonDelete();
                            errorExcelPersonDelete.setErrorMsg(ss);
                            errorExcelPersonDelete.setPersonName(excelPersonDelete.getPersonName());
                            errorExcelPersonDelete.setIdentityNo(excelPersonDelete.getIdentityNo());
                            errorExcelPersonDelete.setIdentityTypeCode(excelPersonDelete.getIdentityTypeCode());
                            errorList.add(errorExcelPersonDelete);
                            percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                        }

                    } else if (excelPersonDelete.getIdentityNo() == null && excelPersonDelete.getIdentityTypeCode() != null) {

                        // 根据 姓名 身份证 教工号查询该人员信息
                        PersonTable personTable = personTableService.SelectDeletePersonId(excelPersonDelete);
                        if (personTable != null) {
                            Integer[] personId = {personTable.getPersonId()};
                            // 删除人员基础信息
                            int i = personTableService.deletePersonTableByUserId(personId);
                            if (i == 1) {
                                List<PersonFaceInfomationTable> personFaceInfomationTables = personFaceInfomationTableService.selectImageList(personTable.getPersonId());
                                // 删除人员照片信息
                                int i1 = personFaceInfomationTableService.deletePersonTableByImagePersonId(personTable.getPersonId());
                                for (PersonFaceInfomationTable personFaceInfomationTable : personFaceInfomationTables) {
                                    if (personFaceInfomationTable.getFaceAddress() != null) {
                                        File file1 = new File(dPath + personFaceInfomationTable.getFaceAddress());
                                        if (file1.exists()) {
                                            file1.delete();
                                        }
                                    }
                                    if (personFaceInfomationTable.getCampusCardAddress() != null) {
                                        File file1 = new File(dPath + personFaceInfomationTable.getCampusCardAddress());
                                        if (file1.exists()) {
                                            file1.delete();
                                        }
                                    }
                                    if (personFaceInfomationTable.getFaceImage() != null) {
                                        File file1 = new File(dPath + personFaceInfomationTable.getFaceImage());
                                        if (file1.exists()) {
                                            file1.delete();
                                        }
                                    }
                                    if (personFaceInfomationTable.getIdcardImage() != null) {
                                        File file1 = new File(dPath + personFaceInfomationTable.getIdcardImage());
                                        if (file1.exists()) {
                                            file1.delete();
                                        }
                                    }
                                }
                                percentMap.replace("successTotal", (double) percentMap.get("successTotal") + 1.00);
                            }
                        } else {
                            String ss = "该人员不存在！";
                            ErrorExcelPersonDelete errorExcelPersonDelete = new ErrorExcelPersonDelete();
                            errorExcelPersonDelete.setErrorMsg(ss);
                            errorExcelPersonDelete.setPersonName(excelPersonDelete.getPersonName());
                            errorExcelPersonDelete.setIdentityNo(excelPersonDelete.getIdentityNo());
                            errorExcelPersonDelete.setIdentityTypeCode(excelPersonDelete.getIdentityTypeCode());
                            errorList.add(errorExcelPersonDelete);
                            percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                        }
                    } else if (excelPersonDelete.getIdentityNo() == null && excelPersonDelete.getIdentityTypeCode() == null) {
                        String ss = "身份证号码与工牌号二选一！";
                        ErrorExcelPersonDelete errorExcelPersonDelete = new ErrorExcelPersonDelete();
                        errorExcelPersonDelete.setErrorMsg(ss);
                        errorExcelPersonDelete.setPersonName(excelPersonDelete.getPersonName());
                        errorExcelPersonDelete.setIdentityNo(excelPersonDelete.getIdentityNo());
                        errorExcelPersonDelete.setIdentityTypeCode(excelPersonDelete.getIdentityTypeCode());
                        errorList.add(errorExcelPersonDelete);
                        percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                    }
                } else {
                    String ss = "姓名为必填项！";
                    ErrorExcelPersonDelete errorExcelPersonDelete = new ErrorExcelPersonDelete();
                    errorExcelPersonDelete.setErrorMsg(ss);
                    errorExcelPersonDelete.setPersonName(excelPersonDelete.getPersonName());
                    errorExcelPersonDelete.setIdentityNo(excelPersonDelete.getIdentityNo());
                    errorExcelPersonDelete.setIdentityTypeCode(excelPersonDelete.getIdentityTypeCode());
                    errorList.add(errorExcelPersonDelete);
                    percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                }
            }
            session.setAttribute("errorDeletePersonList", errorList);

            if (new Double((double) percentMap.get("errorTotal")).intValue() == 0) {
                map.put("code", 0);
                map.put("successTotal", new Double((double) percentMap.get("successTotal")).intValue());
            } else {
                map.put("code", 1);
                map.put("successTotal", new Double((double) percentMap.get("successTotal")).intValue());
                map.put("errorTotal", new Double((double) percentMap.get("errorTotal")).intValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 删除压缩包以及文件夹
            File file1 = new File(savaPicUrl);
            delete(file1, file.getOriginalFilename());
            File file2 = new File(fileUrl);
            deleteFile(file2);
        }

        return map;
    }

    // 导出异常删除人员信息
    @RequestMapping(value = "/exportErrorDeleteExcel")
    public void exportErrorDeleteExcel(HttpServletResponse response, HttpSession session) {
        try {
            // 设置响应输出的头类型
            response.setHeader("content-Type", "application/vnd.ms-excel");
            // 下载文件的默认名称
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode("异常人员信息" + ".xls", "UTF-8"));

            ExportParams deptExportParams = new ExportParams();
            // 设置sheet得名称
            deptExportParams.setSheetName("异常人员信息表");

            Map<String, Object> deptExportMap = new HashMap<>();
            // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
            deptExportMap.put("title", deptExportParams);
            // 模版导出对应得实体类型
            deptExportMap.put("entity", ErrorExcelPersonDelete.class);
            // sheet中要填充得数据
            List<ErrorExcelPersonDelete> errorList = (List) session.getAttribute("errorDeletePersonList");
            deptExportMap.put("data", errorList);
            session.removeAttribute("errorPersonList");
            List<Map<String, Object>> sheetsList = new ArrayList<>();
            sheetsList.add(deptExportMap);
            Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * excel批量修改
     * */
    @RequestMapping(value = "/updatePersonByExcel", method = RequestMethod.POST)
    public Map<String, Object> updatePersonByExcel(MultipartFile file, HttpSession session) throws Exception {
        Map map1 = getExcelData(file, ExcelPersonUpdate.class);
        if (map1.get("code") != null) {
            return map1;
        }
        Map map = new HashMap();
        //人员数据操作记录表
        PersonDataOperationTable personDataOperationTable = null;
        String savaPicUrl = map1.get("savaPicUrl").toString();
        String fileUrl = map1.get("fileUrl").toString();
        List<ExcelPersonUpdate> ExcelPersonUpdateList = (List<ExcelPersonUpdate>) map1.get("excelData");
        //  过滤表格中的空格行
        ExcelPersonUpdateList = ExcelPersonUpdateList.stream()
                .filter(excelPersonUpdate -> (excelPersonUpdate.getPersonName() != null && excelPersonUpdate.getPersonName().trim().length() != 0) || (excelPersonUpdate.getIdentityNo() != null && excelPersonUpdate.getIdentityNo().trim().length() != 0) || (excelPersonUpdate.getBirthday() != null && excelPersonUpdate.getBirthday().trim().length() != 0) || (excelPersonUpdate.getResidentialAddress() != null && excelPersonUpdate.getResidentialAddress().trim().length() != 0) || (excelPersonUpdate.getEthnicity() != null && excelPersonUpdate.getEthnicity().trim().length() != 0) || (excelPersonUpdate.getTelephone() != null && excelPersonUpdate.getTelephone().trim().length() != 0) || (excelPersonUpdate.getIdenticationInfo() != null && excelPersonUpdate.getIdenticationInfo().trim().length() != 0))
                .collect(Collectors.toList());

        System.out.println("导入数据一共【" + (ExcelPersonUpdateList.size()) + "】行");

        percentMap.put("AllSize", (double) ExcelPersonUpdateList.size());
        percentMap.put("successTotal", 0.00);
        percentMap.put("errorTotal", 0.00);

        List<ErrorExcelPersonUpdate> errorList = new ArrayList<>();

        try {
            for (ExcelPersonUpdate excelPersonUpdate : ExcelPersonUpdateList) {
                if (excelPersonUpdate.getPersonName() != null) {
                    if (excelPersonUpdate.getIdentityNo() != null && excelPersonUpdate.getIdentityTypeCode() != null) {
                        if (IDCardUtil.isIDCard(excelPersonUpdate.getIdentityNo())) {

                            PersonTable IdentityNo = null;
                            // 现根据当前id去查询该人员信息
                            PersonTable thisPersonTable = personTableService.selectByPersonId(excelPersonUpdate.getPersonId());
                            //判断比较身份证号是否重复公共类
                            if (excelPersonUpdate.getIdentityNo() != null) {
                                if (thisPersonTable.getIdentityNo() != null) {
                                    // 判断当前修改的身份证号跟之前的身份证号是否一样
                                    if (!thisPersonTable.getIdentityNo().equals(excelPersonUpdate.getIdentityNo())) {
                                        IdentityNo = personTableService.selectPersonTableByIdentityNo(excelPersonUpdate.getIdentityNo());
                                    }
                                } else {
                                    IdentityNo = personTableService.selectPersonTableByIdentityNo(excelPersonUpdate.getIdentityNo());
                                }
                            }
                            PersonTable IdentityTypeCode = null;
                            // 现根据当前id去查询该人员信息
                            PersonTable thisPersonTable1 = personTableService.selectByPersonId(excelPersonUpdate.getPersonId());
                            //判断比较身份证号是否重复公共类
                            if (excelPersonUpdate.getIdentityTypeCode() != null) {
                                if (thisPersonTable1.getIdentityTypeCode() != null) {
                                    // 判断当前修改的身份证号跟之前的身份证号是否一样
                                    if (!thisPersonTable1.getIdentityTypeCode().equals(excelPersonUpdate.getIdentityTypeCode())) {
                                        IdentityTypeCode = personTableService.selectPersonTableByIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                    }
                                } else {
                                    IdentityTypeCode = personTableService.selectPersonTableByIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                }
                            }

                            // 根据 personId查询该人员信息
//                        PersonTable  personTable=personTableService.SelectUpdatePersonId(excelPersonUpdate);
                            PersonTable personTable = personTableService.selectByPersonId(excelPersonUpdate.getPersonId());

                            if (personTable != null && IdentityNo == null && IdentityTypeCode == null) {
                                // 判断办公地点是否为空
                                if (excelPersonUpdate.getDepartments() != null) {
                                    // 判断是否存在此部门
                                    DataDictionary dataDictionary = new DataDictionary();
                                    dataDictionary.setDicType(Constant.DEPARTMENT);
                                    dataDictionary.setDicCode(excelPersonUpdate.getDepartments());
                                    DataDictionary dataDictionary1 = dataDictionaryService.selectDataDictionaryByDicTypeAndDicName(dataDictionary);
                                    if (dataDictionary1 != null) {
                                        if (excelPersonUpdate.getBirthday() != null) {
                                            if (IDCardUtil.isLegalDate(excelPersonUpdate.getBirthday())) {
                                                personTable.setPersonName(excelPersonUpdate.getPersonName());
                                                personTable.setGender(excelPersonUpdate.getGender());
                                                personTable.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                                personTable.setBirthday(excelPersonUpdate.getBirthday());
                                                personTable.setEthnicity(excelPersonUpdate.getEthnicity());
                                                personTable.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                                personTable.setTelephone(excelPersonUpdate.getTelephone());
                                                personTable.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                                personTable.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                                personTable.setGrade(excelPersonUpdate.getGrade());
                                                personTable.setDepartments(dataDictionary1.getDicCode());
                                                personTable.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                                personTable.setDescription(excelPersonUpdate.getDescription());
                                                int i = personTableService.updateByPrimaryKeySelective(personTable);

                                                personDataOperationTable = PersonDataOperationTableUtils.putPersonDataOperationTable(personTable);
                                                personDataOperationTableService.insertPersonDataOperationTable(personDataOperationTable);
                                                percentMap.replace("successTotal", (double) percentMap.get("successTotal") + 1.00);
                                            } else {
                                                String ss = "生日日期格式错误！";
                                                ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                                errorExcelPersonUpdate.setErrorMsg(ss);
                                                errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                                errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                                errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                                errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                                errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                                errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                                errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                                errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                                errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                                errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                                errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                                errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                                errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                                errorList.add(errorExcelPersonUpdate);
                                                percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                            }
                                        } else {
                                            personTable.setPersonName(excelPersonUpdate.getPersonName());
                                            personTable.setGender(excelPersonUpdate.getGender());
                                            personTable.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                            personTable.setBirthday(excelPersonUpdate.getBirthday());
                                            personTable.setEthnicity(excelPersonUpdate.getEthnicity());
                                            personTable.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                            personTable.setTelephone(excelPersonUpdate.getTelephone());
                                            personTable.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                            personTable.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                            personTable.setGrade(excelPersonUpdate.getGrade());
                                            personTable.setDepartments(dataDictionary1.getDicCode());
                                            personTable.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                            personTable.setDescription(excelPersonUpdate.getDescription());
                                            int i = personTableService.updateByPrimaryKeySelective(personTable);
                                            personDataOperationTable = PersonDataOperationTableUtils.putPersonDataOperationTable(personTable);
                                            personDataOperationTableService.insertPersonDataOperationTable(personDataOperationTable);
                                            percentMap.replace("successTotal", (double) percentMap.get("successTotal") + 1.00);
                                        }
                                    } else {
                                        if (excelPersonUpdate.getBirthday() != null) {
                                            if (IDCardUtil.isLegalDate(excelPersonUpdate.getBirthday())) {
                                                String ss = "暂无该办公地点！";
                                                ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                                errorExcelPersonUpdate.setErrorMsg(ss);
                                                errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                                errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                                errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                                errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                                errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                                errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                                errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                                errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                                errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                                errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                                errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                                errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                                errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                                errorList.add(errorExcelPersonUpdate);
                                                percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                            } else {
                                                String ss = "生日日期格式错误！暂无该办公地点！";
                                                ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                                errorExcelPersonUpdate.setErrorMsg(ss);
                                                errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                                errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                                errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                                errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                                errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                                errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                                errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                                errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                                errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                                errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                                errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                                errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                                errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                                errorList.add(errorExcelPersonUpdate);
                                                percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                            }
                                        } else {
                                            String ss = "暂无该办公地点！";
                                            ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                            errorExcelPersonUpdate.setErrorMsg(ss);
                                            errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                            errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                            errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                            errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                            errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                            errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                            errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                            errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                            errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                            errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                            errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                            errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                            errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                            errorList.add(errorExcelPersonUpdate);
                                            percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                        }
                                    }
                                } else {
                                    if (excelPersonUpdate.getBirthday() != null) {
                                        if (IDCardUtil.isLegalDate(excelPersonUpdate.getBirthday())) {
                                            personTable.setPersonName(excelPersonUpdate.getPersonName());
                                            personTable.setGender(excelPersonUpdate.getGender());
                                            personTable.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                            personTable.setBirthday(excelPersonUpdate.getBirthday());
                                            personTable.setEthnicity(excelPersonUpdate.getEthnicity());
                                            personTable.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                            personTable.setTelephone(excelPersonUpdate.getTelephone());
                                            personTable.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                            personTable.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                            personTable.setGrade(excelPersonUpdate.getGrade());
                                            personTable.setDepartments(excelPersonUpdate.getDepartments());
                                            personTable.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                            personTable.setDescription(excelPersonUpdate.getDescription());
                                            int i = personTableService.updateByPrimaryKeySelective(personTable);
                                            personDataOperationTable = PersonDataOperationTableUtils.putPersonDataOperationTable(personTable);
                                            personDataOperationTableService.insertPersonDataOperationTable(personDataOperationTable);
                                            percentMap.replace("successTotal", (double) percentMap.get("successTotal") + 1.00);
                                        } else {
                                            String ss = "生日日期格式错误！";
                                            ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                            errorExcelPersonUpdate.setErrorMsg(ss);
                                            errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                            errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                            errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                            errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                            errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                            errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                            errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                            errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                            errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                            errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                            errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                            errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                            errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                            errorList.add(errorExcelPersonUpdate);
                                            percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                        }
                                    } else {
                                        personTable.setPersonName(excelPersonUpdate.getPersonName());
                                        personTable.setGender(excelPersonUpdate.getGender());
                                        personTable.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                        personTable.setBirthday(excelPersonUpdate.getBirthday());
                                        personTable.setEthnicity(excelPersonUpdate.getEthnicity());
                                        personTable.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                        personTable.setTelephone(excelPersonUpdate.getTelephone());
                                        personTable.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                        personTable.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                        personTable.setGrade(excelPersonUpdate.getGrade());
                                        personTable.setDepartments(excelPersonUpdate.getDepartments());
                                        personTable.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                        personTable.setDescription(excelPersonUpdate.getDescription());
                                        int i = personTableService.updateByPrimaryKeySelective(personTable);
                                        personDataOperationTable = PersonDataOperationTableUtils.putPersonDataOperationTable(personTable);
                                        personDataOperationTableService.insertPersonDataOperationTable(personDataOperationTable);
                                        percentMap.replace("successTotal", (double) percentMap.get("successTotal") + 1.00);
                                    }
                                }
                            } else {
                                if (personTable == null) {
                                    String ss = "未查询到该人员信息！";
                                    ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                    errorExcelPersonUpdate.setErrorMsg(ss);
                                    errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                    errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                    errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                    errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                    errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                    errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                    errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                    errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                    errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                    errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                    errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                    errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                    errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                    errorList.add(errorExcelPersonUpdate);
                                    percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                }
                                if (IdentityTypeCode != null && IdentityNo != null) {
                                    String ss = "身份证号和工牌号都已存在！";
                                    ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                    errorExcelPersonUpdate.setErrorMsg(ss);
                                    errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                    errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                    errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                    errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                    errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                    errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                    errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                    errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                    errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                    errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                    errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                    errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                    errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                    errorList.add(errorExcelPersonUpdate);
                                    percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                } else if (IdentityNo != null) {
                                    String ss = "身份证号已经存在！";
                                    ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                    errorExcelPersonUpdate.setErrorMsg(ss);
                                    errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                    errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                    errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                    errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                    errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                    errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                    errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                    errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                    errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                    errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                    errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                    errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                    errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                    errorList.add(errorExcelPersonUpdate);
                                    percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                } else if (IdentityTypeCode != null) {
                                    String ss = "工牌号已存在！";
                                    ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                    errorExcelPersonUpdate.setErrorMsg(ss);
                                    errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                    errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                    errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                    errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                    errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                    errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                    errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                    errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                    errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                    errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                    errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                    errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                    errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                    errorList.add(errorExcelPersonUpdate);
                                    percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);

                                }


                            }
                        } else {
                            String ss = "身份证格式错误！";
                            ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                            errorExcelPersonUpdate.setErrorMsg(ss);
                            errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                            errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                            errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                            errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                            errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                            errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                            errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                            errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                            errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                            errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                            errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                            errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                            errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                            errorList.add(errorExcelPersonUpdate);
                            percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                        }
                    } else if (excelPersonUpdate.getIdentityTypeCode() == null && excelPersonUpdate.getIdentityNo() != null) {
                        if (IDCardUtil.isIDCard(excelPersonUpdate.getIdentityNo())) {

                            PersonTable IdentityNo = null;
                            // 现根据当前id去查询该人员信息
                            PersonTable thisPersonTable = personTableService.selectByPersonId(excelPersonUpdate.getPersonId());
                            //判断比较身份证号是否重复公共类
                            if (excelPersonUpdate.getIdentityNo() != null) {
                                if (thisPersonTable.getIdentityNo() != null) {
                                    // 判断当前修改的身份证号跟之前的身份证号是否一样
                                    if (!thisPersonTable.getIdentityNo().equals(excelPersonUpdate.getIdentityNo())) {
                                        IdentityNo = personTableService.selectPersonTableByIdentityNo(excelPersonUpdate.getIdentityNo());
                                    }
                                } else {
                                    IdentityNo = personTableService.selectPersonTableByIdentityNo(excelPersonUpdate.getIdentityNo());
                                }
                            }

                            // 根据 personId查询该人员信息
//                        PersonTable  personTable=personTableService.SelectUpdatePersonId(excelPersonUpdate);
                            PersonTable personTable = personTableService.selectByPersonId(excelPersonUpdate.getPersonId());

                            if (personTable != null && IdentityNo == null) {
                                if (excelPersonUpdate.getDepartments() != null) {

                                    DataDictionary dataDictionary = new DataDictionary();
                                    dataDictionary.setDicType(Constant.DEPARTMENT);
                                    dataDictionary.setDicCode(excelPersonUpdate.getDepartments());
                                    DataDictionary dataDictionary1 = dataDictionaryService.selectDataDictionaryByDicTypeAndDicName(dataDictionary);
                                    if (dataDictionary1 != null) {
                                        if (excelPersonUpdate.getBirthday() != null) {
                                            if (IDCardUtil.isLegalDate(excelPersonUpdate.getBirthday())) {
                                                personTable.setPersonName(excelPersonUpdate.getPersonName());
                                                personTable.setGender(excelPersonUpdate.getGender());
                                                personTable.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                                personTable.setBirthday(excelPersonUpdate.getBirthday());
                                                personTable.setEthnicity(excelPersonUpdate.getEthnicity());
                                                personTable.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                                personTable.setTelephone(excelPersonUpdate.getTelephone());
                                                personTable.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                                personTable.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                                personTable.setGrade(excelPersonUpdate.getGrade());
                                                personTable.setDepartments(dataDictionary1.getDicCode());
                                                personTable.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                                personTable.setDescription(excelPersonUpdate.getDescription());
                                                int i = personTableService.updateByPrimaryKeySelective(personTable);
                                                personDataOperationTable = PersonDataOperationTableUtils.putPersonDataOperationTable(personTable);
                                                personDataOperationTableService.insertPersonDataOperationTable(personDataOperationTable);
                                                percentMap.replace("successTotal", (double) percentMap.get("successTotal") + 1.00);
                                            } else {
                                                String ss = "生日日期格式错误！";
                                                ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                                errorExcelPersonUpdate.setErrorMsg(ss);
                                                errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                                errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                                errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                                errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                                errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                                errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                                errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                                errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                                errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                                errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                                errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                                errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                                errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                                errorList.add(errorExcelPersonUpdate);
                                                percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                            }
                                        } else {
                                            personTable.setPersonName(excelPersonUpdate.getPersonName());
                                            personTable.setGender(excelPersonUpdate.getGender());
                                            personTable.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                            personTable.setBirthday(excelPersonUpdate.getBirthday());
                                            personTable.setEthnicity(excelPersonUpdate.getEthnicity());
                                            personTable.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                            personTable.setTelephone(excelPersonUpdate.getTelephone());
                                            personTable.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                            personTable.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                            personTable.setGrade(excelPersonUpdate.getGrade());
                                            personTable.setDepartments(dataDictionary1.getDicCode());
                                            personTable.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                            personTable.setDescription(excelPersonUpdate.getDescription());
                                            int i = personTableService.updateByPrimaryKeySelective(personTable);
                                            personDataOperationTable = PersonDataOperationTableUtils.putPersonDataOperationTable(personTable);
                                            personDataOperationTableService.insertPersonDataOperationTable(personDataOperationTable);
                                            percentMap.replace("successTotal", (double) percentMap.get("successTotal") + 1.00);
                                        }
                                    } else {
                                        if (excelPersonUpdate.getBirthday() != null) {
                                            if (IDCardUtil.isLegalDate(excelPersonUpdate.getBirthday())) {
                                                String ss = "暂无该办公地点！";
                                                ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                                errorExcelPersonUpdate.setErrorMsg(ss);
                                                errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                                errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                                errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                                errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                                errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                                errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                                errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                                errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                                errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                                errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                                errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                                errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                                errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                                errorList.add(errorExcelPersonUpdate);
                                                percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                            } else {
                                                String ss = "生日日期格式错误！暂无该办公地点！";
                                                ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                                errorExcelPersonUpdate.setErrorMsg(ss);
                                                errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                                errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                                errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                                errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                                errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                                errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                                errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                                errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                                errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                                errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                                errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                                errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                                errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                                errorList.add(errorExcelPersonUpdate);
                                                percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                            }
                                        } else {
                                            String ss = "暂无该办公地点！";
                                            ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                            errorExcelPersonUpdate.setErrorMsg(ss);
                                            errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                            errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                            errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                            errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                            errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                            errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                            errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                            errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                            errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                            errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                            errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                            errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                            errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                            errorList.add(errorExcelPersonUpdate);
                                            percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                        }
                                    }
                                } else {
                                    if (excelPersonUpdate.getBirthday() != null) {
                                        if (IDCardUtil.isLegalDate(excelPersonUpdate.getBirthday())) {
                                            personTable.setPersonName(excelPersonUpdate.getPersonName());
                                            personTable.setGender(excelPersonUpdate.getGender());
                                            personTable.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                            personTable.setBirthday(excelPersonUpdate.getBirthday());
                                            personTable.setEthnicity(excelPersonUpdate.getEthnicity());
                                            personTable.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                            personTable.setTelephone(excelPersonUpdate.getTelephone());
                                            personTable.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                            personTable.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                            personTable.setGrade(excelPersonUpdate.getGrade());
                                            personTable.setDepartments(excelPersonUpdate.getDepartments());
                                            personTable.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                            personTable.setDescription(excelPersonUpdate.getDescription());
                                            int i = personTableService.updateByPrimaryKeySelective(personTable);
                                            personDataOperationTable = PersonDataOperationTableUtils.putPersonDataOperationTable(personTable);
                                            personDataOperationTableService.insertPersonDataOperationTable(personDataOperationTable);
                                            percentMap.replace("successTotal", (double) percentMap.get("successTotal") + 1.00);
                                        } else {
                                            String ss = "生日日期格式错误！";
                                            ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                            errorExcelPersonUpdate.setErrorMsg(ss);
                                            errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                            errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                            errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                            errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                            errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                            errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                            errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                            errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                            errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                            errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                            errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                            errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                            errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                            errorList.add(errorExcelPersonUpdate);
                                            percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                        }
                                    } else {
                                        personTable.setPersonName(excelPersonUpdate.getPersonName());
                                        personTable.setGender(excelPersonUpdate.getGender());
                                        personTable.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                        personTable.setBirthday(excelPersonUpdate.getBirthday());
                                        personTable.setEthnicity(excelPersonUpdate.getEthnicity());
                                        personTable.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                        personTable.setTelephone(excelPersonUpdate.getTelephone());
                                        personTable.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                        personTable.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                        personTable.setGrade(excelPersonUpdate.getGrade());
                                        personTable.setDepartments(excelPersonUpdate.getDepartments());
                                        personTable.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                        personTable.setDescription(excelPersonUpdate.getDescription());
                                        int i = personTableService.updateByPrimaryKeySelective(personTable);
                                        personDataOperationTable = PersonDataOperationTableUtils.putPersonDataOperationTable(personTable);
                                        personDataOperationTableService.insertPersonDataOperationTable(personDataOperationTable);
                                        percentMap.replace("successTotal", (double) percentMap.get("successTotal") + 1.00);
                                    }
                                }
                            } else {
                                if (personTable == null) {
                                    String ss = "未查询到该人员信息！";
                                    ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                    errorExcelPersonUpdate.setErrorMsg(ss);
                                    errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                    errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                    errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                    errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                    errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                    errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                    errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                    errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                    errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                    errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                    errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                    errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                    errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                    errorList.add(errorExcelPersonUpdate);
                                    percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                }
                                if (IdentityNo != null) {
                                    String ss = "身份证号已存在！";
                                    ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                    errorExcelPersonUpdate.setErrorMsg(ss);
                                    errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                    errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                    errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                    errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                    errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                    errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                    errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                    errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                    errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                    errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                    errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                    errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                    errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                    errorList.add(errorExcelPersonUpdate);
                                    percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                }

                            }
                        } else {
                            String ss = "身份证格式错误！";
                            ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                            errorExcelPersonUpdate.setErrorMsg(ss);
                            errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                            errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                            errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                            errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                            errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                            errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                            errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                            errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                            errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                            errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                            errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                            errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                            errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                            errorList.add(errorExcelPersonUpdate);
                            percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                        }
                    } else if (excelPersonUpdate.getIdentityNo() == null && excelPersonUpdate.getIdentityTypeCode() != null) {


                        PersonTable IdentityTypeCode = null;
                        // 现根据当前id去查询该人员信息
                        PersonTable thisPersonTable1 = personTableService.selectByPersonId(excelPersonUpdate.getPersonId());
                        //判断比较身份证号是否重复公共类
                        if (excelPersonUpdate.getIdentityTypeCode() != null) {
                            if (thisPersonTable1.getIdentityTypeCode() != null) {
                                // 判断当前修改的身份证号跟之前的身份证号是否一样
                                if (!thisPersonTable1.getIdentityTypeCode().equals(excelPersonUpdate.getIdentityTypeCode())) {
                                    IdentityTypeCode = personTableService.selectPersonTableByIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                }
                            } else {
                                IdentityTypeCode = personTableService.selectPersonTableByIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                            }
                        }

                        // 根据 personId查询该人员信息
//                        PersonTable  personTable=personTableService.SelectUpdatePersonId(excelPersonUpdate);
                        PersonTable personTable = personTableService.selectByPersonId(excelPersonUpdate.getPersonId());

                        if (personTable != null && IdentityTypeCode == null) {
                            if (excelPersonUpdate.getDepartments() != null) {
                                DataDictionary dataDictionary = new DataDictionary();
                                dataDictionary.setDicType(Constant.DEPARTMENT);
                                dataDictionary.setDicCode(excelPersonUpdate.getDepartments());
                                DataDictionary dataDictionary1 = dataDictionaryService.selectDataDictionaryByDicTypeAndDicName(dataDictionary);
                                if (dataDictionary1 != null) {
                                    if (excelPersonUpdate.getBirthday() != null) {
                                        if (IDCardUtil.isLegalDate(excelPersonUpdate.getBirthday())) {
                                            personTable.setPersonName(excelPersonUpdate.getPersonName());
                                            personTable.setGender(excelPersonUpdate.getGender());
                                            personTable.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                            personTable.setBirthday(excelPersonUpdate.getBirthday());
                                            personTable.setEthnicity(excelPersonUpdate.getEthnicity());
                                            personTable.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                            personTable.setTelephone(excelPersonUpdate.getTelephone());
                                            personTable.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                            personTable.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                            personTable.setGrade(excelPersonUpdate.getGrade());
                                            personTable.setDepartments(dataDictionary1.getDicCode());
                                            personTable.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                            personTable.setDescription(excelPersonUpdate.getDescription());
                                            int i = personTableService.updateByPrimaryKeySelective(personTable);
                                            personDataOperationTable = PersonDataOperationTableUtils.putPersonDataOperationTable(personTable);
                                            personDataOperationTableService.insertPersonDataOperationTable(personDataOperationTable);
                                            percentMap.replace("successTotal", (double) percentMap.get("successTotal") + 1.00);
                                        } else {
                                            String ss = "生日日期格式错误！";
                                            ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                            errorExcelPersonUpdate.setErrorMsg(ss);
                                            errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                            errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                            errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                            errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                            errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                            errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                            errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                            errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                            errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                            errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                            errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                            errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                            errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                            errorList.add(errorExcelPersonUpdate);
                                            percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                        }
                                    } else {
                                        personTable.setPersonName(excelPersonUpdate.getPersonName());
                                        personTable.setGender(excelPersonUpdate.getGender());
                                        personTable.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                        personTable.setBirthday(excelPersonUpdate.getBirthday());
                                        personTable.setEthnicity(excelPersonUpdate.getEthnicity());
                                        personTable.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                        personTable.setTelephone(excelPersonUpdate.getTelephone());
                                        personTable.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                        personTable.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                        personTable.setGrade(excelPersonUpdate.getGrade());
                                        personTable.setDepartments(dataDictionary1.getDicCode());
                                        personTable.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                        personTable.setDescription(excelPersonUpdate.getDescription());
                                        int i = personTableService.updateByPrimaryKeySelective(personTable);
                                        personDataOperationTable = PersonDataOperationTableUtils.putPersonDataOperationTable(personTable);
                                        personDataOperationTableService.insertPersonDataOperationTable(personDataOperationTable);
                                        percentMap.replace("successTotal", (double) percentMap.get("successTotal") + 1.00);
                                    }
                                } else {
                                    if (excelPersonUpdate.getBirthday() != null) {
                                        if (IDCardUtil.isLegalDate(excelPersonUpdate.getBirthday())) {
                                            String ss = "暂无该办公地点！";
                                            ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                            errorExcelPersonUpdate.setErrorMsg(ss);
                                            errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                            errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                            errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                            errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                            errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                            errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                            errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                            errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                            errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                            errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                            errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                            errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                            errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                            errorList.add(errorExcelPersonUpdate);
                                            percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                        } else {
                                            String ss = "生日日期格式错误！暂无该办公地点！";
                                            ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                            errorExcelPersonUpdate.setErrorMsg(ss);
                                            errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                            errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                            errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                            errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                            errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                            errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                            errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                            errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                            errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                            errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                            errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                            errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                            errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                            errorList.add(errorExcelPersonUpdate);
                                            percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                        }
                                    } else {
                                        String ss = "暂无该办公地点！";
                                        ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                        errorExcelPersonUpdate.setErrorMsg(ss);
                                        errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                        errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                        errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                        errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                        errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                        errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                        errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                        errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                        errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                        errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                        errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                        errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                        errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                        errorList.add(errorExcelPersonUpdate);
                                        percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                    }
                                }
                            } else {
                                if (excelPersonUpdate.getBirthday() != null) {
                                    if (IDCardUtil.isLegalDate(excelPersonUpdate.getBirthday())) {
                                        personTable.setPersonName(excelPersonUpdate.getPersonName());
                                        personTable.setGender(excelPersonUpdate.getGender());
                                        personTable.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                        personTable.setBirthday(excelPersonUpdate.getBirthday());
                                        personTable.setEthnicity(excelPersonUpdate.getEthnicity());
                                        personTable.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                        personTable.setTelephone(excelPersonUpdate.getTelephone());
                                        personTable.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                        personTable.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                        personTable.setGrade(excelPersonUpdate.getGrade());
                                        personTable.setDepartments(excelPersonUpdate.getDepartments());
                                        personTable.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                        personTable.setDescription(excelPersonUpdate.getDescription());
                                        int i = personTableService.updateByPrimaryKeySelective(personTable);
                                        personDataOperationTable = PersonDataOperationTableUtils.putPersonDataOperationTable(personTable);
                                        personDataOperationTableService.insertPersonDataOperationTable(personDataOperationTable);
                                        percentMap.replace("successTotal", (double) percentMap.get("successTotal") + 1.00);
                                    } else {
                                        String ss = "生日日期格式错误！";
                                        ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                        errorExcelPersonUpdate.setErrorMsg(ss);
                                        errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                        errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                        errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                        errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                        errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                        errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                        errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                        errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                        errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                        errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                        errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                        errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                        errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                        errorList.add(errorExcelPersonUpdate);
                                        percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                                    }
                                } else {
                                    personTable.setPersonName(excelPersonUpdate.getPersonName());
                                    personTable.setGender(excelPersonUpdate.getGender());
                                    personTable.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                    personTable.setBirthday(excelPersonUpdate.getBirthday());
                                    personTable.setEthnicity(excelPersonUpdate.getEthnicity());
                                    personTable.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                    personTable.setTelephone(excelPersonUpdate.getTelephone());
                                    personTable.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                    personTable.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                    personTable.setGrade(excelPersonUpdate.getGrade());
                                    personTable.setDepartments(excelPersonUpdate.getDepartments());
                                    personTable.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                    personTable.setDescription(excelPersonUpdate.getDescription());
                                    int i = personTableService.updateByPrimaryKeySelective(personTable);
                                    personDataOperationTable = PersonDataOperationTableUtils.putPersonDataOperationTable(personTable);
                                    personDataOperationTableService.insertPersonDataOperationTable(personDataOperationTable);
                                    percentMap.replace("successTotal", (double) percentMap.get("successTotal") + 1.00);
                                }
                            }
                        } else {
                            if (personTable == null) {
                                String ss = "未查询到该人员信息！";
                                ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                errorExcelPersonUpdate.setErrorMsg(ss);
                                errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                errorList.add(errorExcelPersonUpdate);
                                percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                            }
                            if (IdentityTypeCode != null) {
                                String ss = "工牌号已存在！";
                                ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                                errorExcelPersonUpdate.setErrorMsg(ss);
                                errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                                errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                                errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                                errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                                errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                                errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                                errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                                errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                                errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                                errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                                errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                                errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                                errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                                errorList.add(errorExcelPersonUpdate);
                                percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                            }

                        }
                    } else if (excelPersonUpdate.getIdentityNo() == null && excelPersonUpdate.getIdentityTypeCode() == null) {
                        String ss = "身份证号码与工牌号二选一！";
                        ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                        errorExcelPersonUpdate.setErrorMsg(ss);
                        errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                        errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                        errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                        errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                        errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                        errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                        errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                        errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                        errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                        errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                        errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                        errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                        errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                        errorList.add(errorExcelPersonUpdate);
                        percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                    }
                } else {
                    String ss = "姓名为必填项！";
                    ErrorExcelPersonUpdate errorExcelPersonUpdate = new ErrorExcelPersonUpdate();
                    errorExcelPersonUpdate.setErrorMsg(ss);
                    errorExcelPersonUpdate.setPersonName(excelPersonUpdate.getPersonName());
                    errorExcelPersonUpdate.setGender(excelPersonUpdate.getGender());
                    errorExcelPersonUpdate.setIdentityNo(excelPersonUpdate.getIdentityNo());
                    errorExcelPersonUpdate.setBirthday(excelPersonUpdate.getBirthday());
                    errorExcelPersonUpdate.setEthnicity(excelPersonUpdate.getEthnicity());
                    errorExcelPersonUpdate.setResidentialAddress(excelPersonUpdate.getResidentialAddress());
                    errorExcelPersonUpdate.setTelephone(excelPersonUpdate.getTelephone());
                    errorExcelPersonUpdate.setIdenticationInfo(excelPersonUpdate.getIdenticationInfo());
                    errorExcelPersonUpdate.setStudentLevel(excelPersonUpdate.getStudentLevel());
                    errorExcelPersonUpdate.setGrade(excelPersonUpdate.getGrade());
                    errorExcelPersonUpdate.setDepartments(excelPersonUpdate.getDepartments());
                    errorExcelPersonUpdate.setIdentityTypeCode(excelPersonUpdate.getIdentityTypeCode());
                    errorExcelPersonUpdate.setDescription(excelPersonUpdate.getDescription());
                    errorList.add(errorExcelPersonUpdate);
                    percentMap.replace("errorTotal", (double) percentMap.get("errorTotal") + 1.00);
                }
            }
            session.setAttribute("errorUpdatePersonList", errorList);

            if (new Double((double) percentMap.get("errorTotal")).intValue() == 0) {
                map.put("code", 0);
                map.put("successTotal", new Double((double) percentMap.get("successTotal")).intValue());
            } else {
                map.put("code", 1);
                map.put("successTotal", new Double((double) percentMap.get("successTotal")).intValue());
                map.put("errorTotal", new Double((double) percentMap.get("errorTotal")).intValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 删除压缩包以及文件夹
            File file1 = new File(savaPicUrl);
            delete(file1, file.getOriginalFilename());
            File file2 = new File(fileUrl);
            deleteFile(file2);
        }

        return map;
    }

    // 导出异常删除人员信息
    @RequestMapping(value = "/exportErrorUpdateExcel")
    public void exportErrorUpdateExcel(HttpServletResponse response, HttpSession session) {
        try {
            // 设置响应输出的头类型
            response.setHeader("content-Type", "application/vnd.ms-excel");
            // 下载文件的默认名称
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode("异常人员信息" + ".xls", "UTF-8"));

            ExportParams deptExportParams = new ExportParams();
            // 设置sheet得名称
            deptExportParams.setSheetName("异常人员信息表");

            Map<String, Object> deptExportMap = new HashMap<>();
            // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
            deptExportMap.put("title", deptExportParams);
            // 模版导出对应得实体类型
            deptExportMap.put("entity", ErrorExcelPersonUpdate.class);
            // sheet中要填充得数据
            List<ErrorExcelPersonUpdate> errorList = (List) session.getAttribute("errorUpdatePersonList");
            deptExportMap.put("data", errorList);
            session.removeAttribute("errorUpdatePersonList");
            List<Map<String, Object>> sheetsList = new ArrayList<>();
            sheetsList.add(deptExportMap);
            Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param response 导出字典信息
     */
    @RequestMapping(value = "/exportDataDictionary")
    public void exportDataDictionary(HttpServletResponse response) {
        try {
            // 设置响应输出的头类型
            response.setHeader("content-Type", "application/vnd.ms-excel");
            // 下载文件的默认名称
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode("字典模板" + ".xls", "UTF-8"));
            //查询所在部门或院系信息
            List<DataDictionary> departmentsList = dataDictionaryService.setlectDataDictionaryList(Constant.DEPARTMENT);
            List<ExcelDataDictionary> yuanxi = new ArrayList<>();
            departmentsList.forEach(dataDictionary -> {
                if (!("weizhi".equals(dataDictionary.getDicCode()))) {
                    ExcelDataDictionary excelDataDictionary = new ExcelDataDictionary();
                    excelDataDictionary.setDicCode(dataDictionary.getDicCode());
                    excelDataDictionary.setDicName(dataDictionary.getDicName());
                    yuanxi.add(excelDataDictionary);
                }
            });
            //查询学历
            List<DataDictionary> studentLevelList = dataDictionaryService.setlectDataDictionaryList(Constant.EDUCATION);
            List<ExcelDataDictionary> xueli = new ArrayList<>();
            studentLevelList.forEach(dataDictionary -> {
                ExcelDataDictionary excelDataDictionary = new ExcelDataDictionary();
                excelDataDictionary.setDicCode(dataDictionary.getDicCode());
                excelDataDictionary.setDicName(dataDictionary.getDicName());
                xueli.add(excelDataDictionary);
            });
            //查询学级
            List<DataDictionary> gradeList = dataDictionaryService.setlectDataDictionaryList(Constant.GRADE);
            List<ExcelDataDictionary> xueji = new ArrayList<>();
            gradeList.forEach(dataDictionary -> {
                ExcelDataDictionary excelDataDictionary = new ExcelDataDictionary();
                excelDataDictionary.setDicCode(dataDictionary.getDicCode());
                excelDataDictionary.setDicName(dataDictionary.getDicName());
                xueji.add(excelDataDictionary);
            });
            //查询人员信息
            List<DataDictionary> identicationInfoList = dataDictionaryService.setlectDataDictionaryList(Constant.IDENTICATIONINFO);
            List<ExcelDataDictionary> shenfenxinxi = new ArrayList<>();
            identicationInfoList.forEach(dataDictionary -> {
                ExcelDataDictionary excelDataDictionary = new ExcelDataDictionary();
                excelDataDictionary.setDicCode(dataDictionary.getDicCode());
                excelDataDictionary.setDicName(dataDictionary.getDicName());
                shenfenxinxi.add(excelDataDictionary);
            });
            //查询民族
            List<DataDictionary> ethnicityList = dataDictionaryService.setlectDataDictionaryList(Constant.ETHNICITY);
            List<ExcelDataDictionary> mingzu = new ArrayList<>();
            ethnicityList.forEach(dataDictionary -> {
                ExcelDataDictionary excelDataDictionary = new ExcelDataDictionary();
                excelDataDictionary.setDicCode(dataDictionary.getDicCode());
                excelDataDictionary.setDicName(dataDictionary.getDicName());
                mingzu.add(excelDataDictionary);
            });
            //查询性别
            List<DataDictionary> sexList = dataDictionaryService.setlectDataDictionaryList(Constant.SEX);
            List<ExcelDataDictionary> xingbie = new ArrayList<>();
            sexList.forEach(dataDictionary -> {
                ExcelDataDictionary excelDataDictionary = new ExcelDataDictionary();
                excelDataDictionary.setDicCode(dataDictionary.getDicCode());
                excelDataDictionary.setDicName(dataDictionary.getDicName());
                xingbie.add(excelDataDictionary);
            });
            ExportParams deptExportParams = new ExportParams();
            // 设置sheet得名称
            deptExportParams.setSheetName("办公地点模板");
            Map<String, Object> deptExportMap = new HashMap<>();
            // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
            deptExportMap.put("title", deptExportParams);
            // 模版导出对应得实体类型
            deptExportMap.put("entity", ExcelDataDictionary.class);
            // sheet中要填充得数据
            deptExportMap.put("data", yuanxi);
            ExportParams deptExportParams1 = new ExportParams();
            // 设置sheet得名称
            deptExportParams1.setSheetName("学历模板");
            Map<String, Object> deptExportMap1 = new HashMap<>();
            // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
            deptExportMap1.put("title", deptExportParams1);
            // 模版导出对应得实体类型
            deptExportMap1.put("entity", ExcelDataDictionary.class);
            // sheet中要填充得数据
            deptExportMap1.put("data", xueli);
            ExportParams deptExportParams2 = new ExportParams();
            // 设置sheet得名称
            deptExportParams2.setSheetName("学级模板");
            Map<String, Object> deptExportMap2 = new HashMap<>();
            // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
            deptExportMap2.put("title", deptExportParams2);
            // 模版导出对应得实体类型
            deptExportMap2.put("entity", ExcelDataDictionary.class);
            // sheet中要填充得数据
            deptExportMap2.put("data", xueji);
            ExportParams deptExportParams3 = new ExportParams();
            // 设置sheet得名称
            deptExportParams3.setSheetName("身份信息模板");
            Map<String, Object> deptExportMap3 = new HashMap<>();
            // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
            deptExportMap3.put("title", deptExportParams3);
            // 模版导出对应得实体类型
            deptExportMap3.put("entity", ExcelDataDictionary.class);
            // sheet中要填充得数据
            deptExportMap3.put("data", shenfenxinxi);
            ExportParams deptExportParams4 = new ExportParams();
            // 设置sheet得名称
            deptExportParams4.setSheetName("民族模板");
            Map<String, Object> deptExportMap4 = new HashMap<>();
            // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
            deptExportMap4.put("title", deptExportParams4);
            // 模版导出对应得实体类型
            deptExportMap4.put("entity", ExcelDataDictionary.class);
            // sheet中要填充得数据
            deptExportMap4.put("data", mingzu);
            ExportParams deptExportParams5 = new ExportParams();
            // 设置sheet得名称
            deptExportParams5.setSheetName("性别模板");
            Map<String, Object> deptExportMap5 = new HashMap<>();
            // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
            deptExportMap5.put("title", deptExportParams5);
            // 模版导出对应得实体类型
            deptExportMap5.put("entity", ExcelDataDictionary.class);
            // sheet中要填充得数据
            deptExportMap5.put("data", xingbie);
            List<Map<String, Object>> sheetsList = new ArrayList<>();
            sheetsList.add(deptExportMap);
            sheetsList.add(deptExportMap1);
            sheetsList.add(deptExportMap2);
            sheetsList.add(deptExportMap3);
            sheetsList.add(deptExportMap4);
            sheetsList.add(deptExportMap5);
            Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
