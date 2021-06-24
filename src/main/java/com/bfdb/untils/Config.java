package com.bfdb.untils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

import java.io.File;

public class Config {
    private static PropertiesConfiguration propConfig;

    private static final Config CONFIG = new Config();

    /**
     * 自动保存
     */
    private static boolean autoSave = true;

    private Config() {
    }

    public static Config getInstance(String propertiesFile) {
        //执行初始化
        init(propertiesFile);
        return CONFIG;
    }

    /**
     * 初始化
     *
     * @param propertiesFile
     * @throws ConfigurationException
     * @see
     */
    private static void init(String propertiesFile){
        try {
            propConfig = new PropertiesConfiguration(propertiesFile);
            //自动重新加载
            propConfig.setReloadingStrategy(new FileChangedReloadingStrategy());
            //自动保存
            propConfig.setAutoSave(autoSave);
        } catch (Exception  e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据Key获得对应的value
     *
     * @param key
     * @return
     * @see
     */
    public Object getValue(String key) {
        return propConfig.getProperty(key);
    }

    /**
     * 设置属性
     *
     * @param key
     * @param value
     * @see
     */
    public void setProperty(String key, String value) {
        propConfig.setProperty(key, value);
    }

    /**
     * 获取图片回显ip
     */
    public static String getPhotoUrl(String key){
            File file  = new File(System.getProperty("user.dir")+File.separator+"application.properties");
            Config phtotUrl = Config.getInstance( file.getPath() );
            if (phtotUrl.getValue(key) == null){
                return null;
            }
            return phtotUrl.getValue(key).toString();
    }
}
