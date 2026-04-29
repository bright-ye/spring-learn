package com.minispring.stage3;

public class BeanDefinition {

    private String beanName;
    private String className;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public BeanDefinition(String beanName, String className) {
        this.beanName = beanName;
        this.className = className;
    }
}
