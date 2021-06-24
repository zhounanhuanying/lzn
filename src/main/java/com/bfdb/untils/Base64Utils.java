package com.bfdb.untils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 项目中图片保存的工具类
 */
public class Base64Utils {
    /**
     * 图片转化成base64字符串
     *
     * @param imgPath
     * @return
     */
    public static String GetImageStr(String imgPath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        String imgFile = imgPath;// 待处理的图片
        InputStream in = null;
        byte[] data = null;
        String encode = null; // 返回Base64编码过的字节数组字符串
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        try {
            // 读取图片字节数组
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            encode = encoder.encode(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return encode;
    }

    /**
     * base64字符串转化成图片
     *  对字节数组字符串进行Base64解码并生成图片存储，并返回路径
     * @param imgData  图片编码
     * @throws IOException
     */
    public static String generateImage(String imgData) throws IOException {
        if (imgData == null) // 图像数据为空
            return "";
        BASE64Decoder decoder = new BASE64Decoder();
        OutputStream out = null;
        //获取地址
        String dPath = Config.getPhotoUrl("filePath");
        String newFileUrl ="";
        try {
            //图片路径公共类
            newFileUrl=addressPublic(dPath);
            File imageFile = new File(dPath+newFileUrl);
            byte[] b = decoder.decodeBuffer(imgData);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            out = new FileOutputStream(imageFile);
            out.write(b);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
            return newFileUrl ;
        }
    }

    /**
     *  图片的url，将图片下载在对应的文件中
     * @param urlString   图片的url
     * @return
     * @throws IOException
     */
    public static String download(String urlString) throws IOException {
        String newFileUrl = null;
        URL url = null;
        // 打开连接
        URLConnection con = null;
        try {
            url = new URL( urlString );
            con = url.openConnection();
            //设置请求超时为5s
            con.setConnectTimeout( 5 * 1000 );
            // 输入流
            DataInputStream dataInputStream = new DataInputStream( url.openStream() );
            String dPath = Config.getPhotoUrl("filePath");
            //图片路径公共类
            newFileUrl=addressPublic(dPath);

            FileOutputStream fileOutputStream = new FileOutputStream( new File(dPath + newFileUrl ) );
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            // 1K的数据缓冲
            byte[] buffer = new byte[1024];
            // 读取到的数据长度
            int length;
            while ((length = dataInputStream.read( buffer )) > 0) {
                output.write( buffer, 0, length );
            }
            fileOutputStream.write( output.toByteArray() );
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFileUrl;
    }


    /**
     *上传图片的公共路径路径
     * @param dPath
     */
    public static String addressPublic(String dPath) {
        String newFileUrl=null;
        // 新的图片文件名 = 编号 +"."图片扩展名
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String format = simpleDateFormat.format( date );
        String[] split = format.split( "/" );
        String urle= "";
        for (int i = 0; i < split.length; i++) {
            urle += split[i] + "/";
            File file = new File( dPath + urle );
            //判断文件夹是否存在
            if (!file.exists() && !file.isDirectory()) {
                //不存在的话,创建文件夹
                file.mkdirs();
            }
        }
        //截取老文件名的后缀
        String uuid = UUID.randomUUID().toString()+ "";
        //返回地址信息
        newFileUrl = format +"/"+ uuid + "." + "jpg";
        return  newFileUrl;
    }

    /**
     * 传递图片的url，将图片下载在对应的文件中
     *
     * @param urlString
     *            图片连接
     * @author xvk
     * @throws Exception
     */

    public  static String downloadPhotos(String urlString) throws Exception {
        String  newFileUrl =null;
                // 新的图片文件名 = 编号 +"."图片扩展名
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        String dateStr = simpleDateFormat.format( date );
        String dPath = Config.getPhotoUrl("filePath");
        String newPath = dPath + dateStr + "FaceImg/";
        File dir = new File(newPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //截取老文件名的后缀
        String uuid = UUID.randomUUID() + "";
        String  filename= dateStr + uuid + "." + "jpg";
        try {
            File file = new File(newPath  +  filename);
            OutputStream os = new FileOutputStream(file);
            // 创建一个url对象
            URL url = new URL(urlString);
            InputStream is = url.openStream();
            byte[] buff = new byte[1024];
            while (true) {
                int readed = is.read(buff);
                if (readed == -1) {
                    break;
                }
                byte[] temp = new byte[readed];
                System.arraycopy(buff, 0, temp, 0, readed);
                // 写入文件
                os.write(temp);
            }
            //返回地址信息
              newFileUrl = newPath + dateStr + uuid + "." + "jpg";
            is.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  newFileUrl;
    }

    /**
     * 去掉图片的base64的头部
     * @param baseurl
     * @return
     * @throws Exception
     */
    public  static String baseurlPhotos(String baseurl) {
        return  baseurl.substring( baseurl.indexOf( "," ) + 1 );
    }

}
