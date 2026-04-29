package com.minispring.stage2;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClassPathXmlApplicationContext {

    // beanDefinition 缓存
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    // bean 缓存
    private Map<String, Object> singletonMap = new HashMap<>();

    public ClassPathXmlApplicationContext(String config) throws Exception {
        loadBeanDefinitions(config);
    }

    private void loadBeanDefinitions(String config) throws Exception{
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        InputStream inputStream = this.getClass().getResourceAsStream(config);
        Document root = documentBuilder.parse(inputStream);

        NodeList nodeList = root.getElementsByTagName("/beans.xml");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                String beanName = element.getAttribute("id");
                String clazz = element.getAttribute("class");
                BeanDefinition beanDefinition = new BeanDefinition(beanName, clazz);
                beanDefinitionMap.put(beanName, beanDefinition);
            }
        }
    }

    public Object getBean(String beanName) {
        if (singletonMap.containsKey(beanName)) {
            return singletonMap.get(beanName);
        }
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new RuntimeException("beanName " + beanName + " is not exist");
        }
        try {
            Class clazz = Class.forName(beanDefinition.getClassName());
            Object o = clazz.newInstance();
            singletonMap.put(beanName, o);
            return o;
        } catch (Exception e) {
            return null;
        }
    }
}
