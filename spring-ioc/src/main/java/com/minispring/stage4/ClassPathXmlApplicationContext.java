package com.minispring.stage4;

import com.minispring.stage4.annotation.Autowired;
import com.minispring.stage4.annotation.Component;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ClassPathXmlApplicationContext {

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private Map<String, Object> beanObjectMap = new HashMap<>();

    public ClassPathXmlApplicationContext(String config) throws Exception {
        scanPackage(config);

        instantiateAllBeans();
    }

    private void instantiateAllBeans() {
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            String clazzName = beanDefinition.getClassName();
            try {
                Class clazz = Class.forName(clazzName);
                Object o = clazz.newInstance();

                populateBean(o);

                beanObjectMap.put(beanName, o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void populateBean(Object o) throws IllegalAccessException {
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                String fieldName = field.getName();
                Object o1 = beanObjectMap.get(fieldName);
                field.setAccessible(true);
                field.set(o, o1);
            }
        }
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
                        if (clazz.isAnnotationPresent(Component.class)) {
                            beanDefinitionMap.put(toLowerFirstClass(clazz.getSimpleName()), new BeanDefinition(toLowerFirstClass(clazz.getSimpleName()), clazz.getName()));
                        }
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
