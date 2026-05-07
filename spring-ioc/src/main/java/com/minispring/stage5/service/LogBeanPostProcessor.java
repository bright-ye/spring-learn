package com.minispring.stage5.service;

import com.minispring.stage5.annotation.Component;
import com.minispring.stage5.factory.BeanPostProcessor;

@Component
public class LogBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        System.out.println("【前置处理】Bean：" + beanName + "，初始化之前");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        System.out.println("【后置处理】Bean：" + beanName + "，初始化之后");
        return bean;
    }
}
