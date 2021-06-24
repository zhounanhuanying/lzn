package com.bfdb.untils;


import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * 解压上传zip文件
 */
public class ZipUtils {
    public ZipUtils() {
    }
    /**
     * @param sourcefiles       源文件(服务器上的zip包存放地址)
     * @param decompreDirectory 解压缩后文件存放的目录
     * @throws IOException IO异常
     *
     * 例子 unzip(new File("E:/Study/java.zip"), "E:/Study/unzip/");
     */
    @SuppressWarnings("unchecked")
    public static void unzip(String sourcefiles, String decompreDirectory) throws IOException {
        ZipFile readfile = null;
        try {
            readfile = new ZipFile(sourcefiles, Charset.forName("GBK"));
            //，枚举
            Enumeration takeentrie = readfile.entries();
            ZipEntry zipEntry = null;
            File credirectory = new File(decompreDirectory);
            credirectory.mkdirs();
            while (takeentrie.hasMoreElements()) {
                zipEntry = (ZipEntry) takeentrie.nextElement();
                String entryName = zipEntry.getName();
                InputStream in = null;
                FileOutputStream out = null;
                try {
                    if (zipEntry.isDirectory()) {
                        String name = zipEntry.getName();
                        name = name.substring(0, name.length() - 1);
                        File createDirectory = new File(decompreDirectory + File.separator + name);
                        createDirectory.mkdirs();
                    } else {
                        int index = entryName.lastIndexOf("\\");
                        if (index != -1) {
                            File createDirectory = new File(decompreDirectory + File.separator + entryName.substring(0, index));
                            createDirectory.mkdirs();
                        }
                        index = entryName.lastIndexOf("/");
                        if (index != -1) {
                            File createDirectory = new File(decompreDirectory + File.separator + entryName.substring(0, index));
                            createDirectory.mkdirs();
                        }
                        File unpackfile = new File(decompreDirectory + File.separator + zipEntry.getName());
                        in = readfile.getInputStream(zipEntry);
                        out = new FileOutputStream(unpackfile);
                        int c;
                        byte[] by = new byte[1024];
                        while ((c = in.read(by)) != -1) {
                            out.write(by, 0, c);
                        }
                        out.flush();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    throw new IOException("解压失败：" + ex.toString());
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ex) {

                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    in = null;
                    out = null;
                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IOException("解压失败：" + ex.toString());
        } finally {
            if (readfile != null) {
                try {
                    readfile.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    throw new IOException("解压失败：" + ex.toString());
                }
            }
        }
    }


    /**
     * 解压rar格式压缩包。
     * 对应的是java-unrar-0.3.jar，但是java-unrar-0.3.jar又会用到commons-logging-1.1.1.jar
     */
    public static void unrar(String sourceRar,String destDir) throws Exception{
        Archive a = null;
        FileOutputStream fos = null;
        try{
            a = new Archive(new File(sourceRar));
            FileHeader fh = a.nextFileHeader();
            while(fh!=null){
                if(!fh.isDirectory()){
                    //1 根据不同的操作系统拿到相应的 destDirName 和 destFileName
                    //String compressFileName = fh.getFileNameString().trim();
                    String compressFileName=fh.getFileNameW().trim();
                    if(!existZH(compressFileName)){
                        compressFileName = fh.getFileNameString().trim();
                    }
                    String destFileName = "";
                    String destDirName = "";
                    //非windows系统
                    if(File.separator.equals("/")){
                        destFileName = destDir + compressFileName.replaceAll("\\\\", "/");
                        destDirName = destFileName.substring(0, destFileName.lastIndexOf("/"));
                        //windows系统
                    }else{
                        destFileName = destDir + compressFileName.replaceAll("/", "\\\\");
                        destDirName = destFileName.substring(0, destFileName.lastIndexOf("\\"));
                    }
                    //2创建文件夹
                    File dir = new File(destDirName);
                    if(!dir.exists()||!dir.isDirectory()){
                        dir.mkdirs();
                    }
                    //3解压缩文件
                    fos = new FileOutputStream(new File(destFileName));
                    a.extractFile(fh, fos);
                    fos.close();
                    fos = null;
                }
                fh = a.nextFileHeader();
            }
            a.close();
            a = null;
        }catch(Exception e){
            throw e;
        }finally{
            if(fos!=null){
                try{fos.close();fos=null;}catch(Exception e){e.printStackTrace();}
            }
            if(a!=null){
                try{a.close();a=null;}catch(Exception e){e.printStackTrace();}
            }
        }
    }


    /**
     * 清空某文件夹下的内容
     * @param path
     * @return
     *例子： String path = "G:\\temp"; deleteDir(path);
     */
    public static boolean deleteDir(String path){
            File file = new File(path);
            if(!file.exists()){//判断是否待删除目录是否存在
                System.err.println("The dir are not exists!");
                return false;
            }
            String[] content = file.list();//取得当前目录下所有文件和文件夹
            for(String name : content){
                File temp = new File(path, name);
                if(temp.isDirectory()){//判断是否是目录
                    deleteDir(temp.getAbsolutePath());//递归调用，删除目录里的内容
                    temp.delete();//删除空目录
                }else{
                    if(!temp.delete()){//直接删除文件
                        System.err.println("Failed to delete " + name);
                    }
                }
            }
            return true;
    }

    //获得以.xls为后缀的Excel文件
    public static File getExcelFile(String path) {
        File file = new File(path);
        if (!file.exists()) {//判断当前目录是否存在
            System.err.println("The dir are not exists!");
            return null;
        }
        String[] content = file.list();//取得当前目录下所有文件和文件夹
        for(String name : content){
            File temp = new File(path, name);
            if(temp.isDirectory()){//判断是否是目录
               String[] con = temp.list();
               for(String name1:con){
                   if(name1.endsWith(".xls")||name1.endsWith(".xlsx")){
                       File excelFile= new File(temp,name1);
                       return excelFile;
                   }
               }
            }else if(name.endsWith(".xls")||name.endsWith(".xlsx")){
                return temp;
            }
        }
        return null;
    }
    public static int savePic(String path,String picPath,String picName,String newName) throws IOException {
        File file = new File(path);
        if (!file.exists()) {//判断当前目录是否存在
            System.err.println("The dir are not exists!");
        }
        /*String[] split = dateStr.split("/");
        String url = "";
        for (int i=0;i<split.length;i++){
            url += split[i]+"/";
            File fileS = new File(dPath + url);
            //判断文件夹是否存在
            if(!fileS.exists()&& !fileS.isDirectory()){
                //不存在的话,创建文件夹
                fileS.mkdirs();
            }
        }*/

        String[] content = file.list();//取得当前目录下所有文件和文件夹
        for(String name : content){
//            File temp = new File(path, name);
            /*if(temp.isDirectory()){//判断是否是目录
                String[] con = temp.list();
                for(String name1:con){*/
                    if(name.equals(picName)){

                        //转存路径
                        String filePath = picPath+newName;
                        //file文件转为MultipartFile文件
                        File picFile = new File(path, name);
                        String resolution1 = getResolution1(picFile);
                        String[] xes = resolution1.split("x");
                        /*int width = Integer.parseInt(xes[0]);
                        int height = Integer.parseInt(xes[1]);
                        if(!(width<1080) || !(height<1920)) {
                            return -2;
                        }*/
                        FileInputStream fileInputStream = new FileInputStream(picFile);
                        MultipartFile multipartFile = new MockMultipartFile(picFile.getName(), picFile.getName(),
                                ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
                       /* long filesize=multipartFile.getSize();
                        if(filesize>(512*1024)){//不超过512k
                            return -1;
                        }*/
                        //存放文件
                        multipartFile.transferTo(new File(filePath));
                    }
        }
        return 1;
    }

    /**
     * 将图片转存到需要压缩得文件里
     * @param path
     * @param savaPicUrl
     * @param picName
     * @param newName
     * @throws IOException
     */
    public static void savePictoExecl(String path,String savaPicUrl,String picName,String newName) throws IOException {
        File file = new File(path);
        if (!file.exists()) {//判断当前目录是否存在
            System.err.println("The dir are not exists!");
        }
        String[] content = file.list();//取得当前目录下所有文件和文件夹
        for(String name : content){
            File temp = new File(path, name);
                    if(name.equals(picName)){
                        // 读取存在得照片
                        FileInputStream in=new FileInputStream(temp.getPath());
                        // 将照片复制到需要压缩得文件里
                        FileOutputStream out= new FileOutputStream(savaPicUrl+"/"+newName);
                        BufferedInputStream bufferedIn=new BufferedInputStream(in);
                        BufferedOutputStream bufferedOut=new BufferedOutputStream(out);
                        byte[] by=new byte[1];
                        while (bufferedIn.read(by)!=-1) {
                            bufferedOut.write(by);
                        }
                        bufferedOut.flush();
                        bufferedIn.close();
                        bufferedOut.close();
                    }
        }
    }
    public static String getResolution1(File file) throws IOException {
               BufferedImage image = ImageIO.read(file);
               return image.getWidth() + "x" + image.getHeight();
           }

    public static void deletePic(String savaPicUrl,String picName) throws IOException {
        File file = new File(savaPicUrl);
        if (!file.exists()) {//判断当前目录是否存在
            System.err.println("The dir are not exists!");
        }
        String[] content = file.list();//取得当前目录下所有文件和文件夹
        for(String name : content){
                    if(name.equals(picName)){
                        File picFile = new File(savaPicUrl, name);
                        picFile.delete();
                    }
                }
    }
    /**
     * 解压rar格式压缩包。
     * 对应的是java-unrar-0.3.jar，但是java-unrar-0.3.jar又会用到commons-logging-1.1.1.jar
     */
/*    public static void unrar(String sourceRar,String destDir) throws Exception{
        Archive a = null;
        FileOutputStream fos = null;
        try{
            a = new Archive(new File(sourceRar));
            FileHeader fh = a.nextFileHeader();
            while(fh!=null){
                if(!fh.isDirectory()){
                    //1 根据不同的操作系统拿到相应的 destDirName 和 destFileName
                    //String compressFileName = fh.getFileNameString().trim();
                    String compressFileName=fh.getFileNameW().trim();
                    if(!existZH(compressFileName)){
                        compressFileName = fh.getFileNameString().trim();
                    }
                    String destFileName = "";
                    String destDirName = "";
                    //非windows系统
                    if(File.separator.equals("/")){
                        destFileName = destDir + compressFileName.replaceAll("\\\\", "/");
                        destDirName = destFileName.substring(0, destFileName.lastIndexOf("/"));
                        //windows系统
                    }else{
                        destFileName = destDir + compressFileName.replaceAll("/", "\\\\");
                        destDirName = destFileName.substring(0, destFileName.lastIndexOf("\\"));
                    }
                    //2创建文件夹
                    File dir = new File(destDirName);
                    if(!dir.exists()||!dir.isDirectory()){
                        dir.mkdirs();
                    }
                    //3解压缩文件
                    fos = new FileOutputStream(new File(destFileName));
                    a.extractFile(fh, fos);
                    fos.close();
                    fos = null;
                }
                fh = a.nextFileHeader();
            }
            a.close();
            a = null;
        }catch(Exception e){
            throw e;
        }finally{
            if(fos!=null){
                try{fos.close();fos=null;}catch(Exception e){e.printStackTrace();}
            }
            if(a!=null){
                try{a.close();a=null;}catch(Exception e){e.printStackTrace();}
            }
        }
    }*/

    /**
     * 删除指定文件夹及下所有文件
     * @param file
     * @return
     */
    public static boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
        }
        return file.delete();
    }
    /**
     * 解决linux下rar包名字中文乱码
     * @param str
     * @return
     */
    public static boolean existZH(String str) {
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        while (m.find()) {
            return true;
        }
        return false;
    }


    /**
     * 获取zip包下的文件夹名称
     * @param path
     * @return
     * @throws IOException
     */
    public static String readZipFile(String path) {
        String str = null;
        ZipEntry zipEntry = null;
        ZipInputStream zipInputStream =null;
        File file = new File(path);
        try {
            if (file.exists()) { //判断文件是否存在
                zipInputStream = new ZipInputStream(new FileInputStream(path), Charset.forName("GBK")); //解决包内文件存在中文时的中文乱码问题
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    //只读取包内根目录文件的文件名
                    if (zipEntry.isDirectory()) {
                        str = zipEntry.getName();
//                    System.out.println(str);
                    }
//                else{
//                    str+=";"+zipEntry.getName().substring(zipEntry.getName().lastIndexOf("/")+1);
//                    System.out.println(str);
//                }

                }
            }
        }catch (Exception e){
        }finally {
            if (zipInputStream!=null) {
                try {
                    zipInputStream.close();
                } catch (IOException e) {
                }
            }
        }

        return  str;

    }

}



