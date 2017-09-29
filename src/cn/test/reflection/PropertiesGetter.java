package cn.test.reflection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Yaya on 2017/5/17.
 *    验证 getClass().getClassLoader().getResourceAsStream(fileName),中fileName对应的classpath是否指的是系统环境变量中classpath(classloader类加载器中的)
 *  还是只是音译重名,单指的项目工程的路径而已
 *  warning:properties配置文件要放在项目目录下
 *  result:classpath就是指的 java项目的根目录 ,与系统环境变量无关
 */

public class PropertiesGetter {

    public static String propertiesFileName = "appconfig.properties";
    private static PropertiesGetter pg = null;

    InputStream inputStream;
    Properties p;

    private PropertiesGetter(String fileName) {
        inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        p = new Properties();
        try {
            p.load(inputStream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static PropertiesGetter getInstance() {
        return getInstance(propertiesFileName);
    }
    public static PropertiesGetter getInstance(String fileName) {
        if (pg == null) {
            pg = new PropertiesGetter(fileName);
        }
        return pg;
    }

    public String get(String property) {
        String v = p.getProperty(property);
        return v;
    }


    public static void main(String[] args) {
        PropertiesGetter pg = PropertiesGetter.getInstance();
    }
}

