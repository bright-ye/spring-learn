package com.minispring.stage3;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ClassPathXmlApplicationContext {

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private Map<String, Object> beanObjectMap = new HashMap<>();

    public ClassPathXmlApplicationContext(String config) throws Exception {
        scanPackage(config);
    }

    private void scanPackage(String config) throws ClassNotFoundException {
        String path = config.replace(".", "/");

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource == null) {
            return;
        }

        File file = new File(resource.getFile());
        if (file.isDirectory()) {
            for (File listFile : file.listFiles()) {
                if (listFile.isFile()) {
                    String fileName = listFile.getAbsolutePath();
                    if (fileName.endsWith(".class")) {
                        Class clazz = Class.forName(config + "." + listFile.getName().replace(".class", ""));
                        beanDefinitionMap.put(toLowerFirstClass(clazz.getSimpleName()), new BeanDefinition(toLowerFirstClass(clazz.getSimpleName()), clazz.getName()));
                    }
                } else {
                    scanPackage(config + "." + listFile.getName());
                }
            }
        }
    }

    public Object getBean(String beanName) {
        if (beanObjectMap.containsKey(beanName)) {
            return beanObjectMap.get(beanName);
        }
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new RuntimeException("beanName " + beanName + " is not exist");
        }
        try {
            Class clazz = Class.forName(beanDefinition.getClassName());
            Object o = clazz.newInstance();
            beanObjectMap.put(beanName, o);
            return o;
        } catch (Exception e) {
            return null;
        }
    }

    private String toLowerFirstClass(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return new String(chars);
    }

}
