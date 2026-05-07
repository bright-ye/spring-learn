package com.minispring.stage5;

import com.minispring.stage5.annotation.Autowired;
import com.minispring.stage5.annotation.Component;
import com.minispring.stage5.annotation.PostConstruct;
import com.minispring.stage5.factory.BeanPostProcessor;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassPathXmlApplicationContext {
    // Bean定义Map
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    // 单例池
    private Map<String, Object> singletonObjects = new HashMap<>();
    // 存储所有的BeanPostProcessor
    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public ClassPathXmlApplicationContext(String config) throws Exception {
        // 1. 扫描包，注册Bean定义
        scanPackage(config);

        // 2. 实例化所有BeanPostProcessor，因为他们要提前初始化，用来处理其他Bean
        instantiateAllBeanPostProcessors();

        // 3. 实例化所有Bean, 完成生命周期
        instantiateAllBeans();
    }

    private void instantiateAllBeanPostProcessors() throws Exception {
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            Class<?> clazz = Class.forName(beanDefinition.getClazz());
            if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                BeanPostProcessor processor = (BeanPostProcessor) clazz.newInstance();
                beanPostProcessors.add(processor);
                singletonObjects.put(beanName, processor);
            }
        }
    }

    private void instantiateAllBeans() throws Exception {
        for (String beanName : beanDefinitionMap.keySet()) {
            if (singletonObjects.containsKey(beanName)) {
                continue;
            }
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            String clazzName = beanDefinition.getClazz();
            Class clazz = Class.forName(clazzName);

            // 1. 实例化：创建对象
            Object bean = clazz.getConstructor().newInstance();

            // 2. 属性填充：依赖注入
            populateBean(bean);

            // 3. BeanPostProcessor前置处理
            bean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

            // 4. 初始化：执行@PostConstruct方法
            invokeInitMethod(bean);

            // 5. BeanPostProcessor后置处理
            bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);

            singletonObjects.put(beanName, bean);
        }
    }

    private Object applyBeanPostProcessorsAfterInitialization(Object bean, String beanName) throws Exception {
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessAfterInitialization(bean, beanName);
        }
        return bean;
    }

    private void invokeInitMethod(Object bean) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                method.invoke(bean);
            }
        }
    }

    private Object applyBeanPostProcessorsBeforeInitialization(Object bean, String beanName) throws Exception {
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessBeforeInitialization(bean, beanName);
        }
        return bean;
    }

    private void populateBean(Object o) throws IllegalAccessException {
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                String fieldName = field.getName();
                Object o1 = singletonObjects.get(fieldName);
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
        if (singletonObjects.containsKey(beanName)) {
            return singletonObjects.get(beanName);
        }
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new RuntimeException("beanName " + beanName + " is not exist");
        }
        try {
            Class clazz = Class.forName(beanDefinition.getClazz());
            Object o = clazz.newInstance();
            singletonObjects.put(beanName, o);
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
