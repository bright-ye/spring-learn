package com.minispring.stage5;

public class BeanDefinition {

    private String beanName;
    private String clazz;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public BeanDefinition(String beanName, String className) {
        this.beanName = beanName;
        this.clazz = className;
    }
}
