package com.minispring.stage5;


import com.minispring.stage5.service.UserDao;
import com.minispring.stage5.service.UserService;

public class Stage5Test {

    public static void main(String[] args) throws Exception {
        // 启动容器
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("com.minispring.stage5.service");

        // 获取Bean
        UserService userService = (UserService) context.getBean("userService");

        // 调用方法
        userService.test();
    }

}
