package com.minispring.stage3;

import com.minispring.stage3.service.UserDao;
import com.minispring.stage3.service.UserService;

public class Stage3Test {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("com.minispring.stage3.service");
        UserService userService = (UserService) context.getBean("userService");
        userService.sayHello();
        UserDao userDao = (UserDao) context.getBean("userDao");
        userDao.sayHello();
    }

}
