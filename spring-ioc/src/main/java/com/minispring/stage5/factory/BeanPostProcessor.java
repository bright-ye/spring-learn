package com.minispring.stage5.factory;

public interface BeanPostProcessor {

    /**
     * 初始化之前的处理
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    /**
     * 初始化之后的处理
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

}
