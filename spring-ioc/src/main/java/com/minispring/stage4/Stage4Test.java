package com.minispring.stage4;

import com.minispring.stage4.service.UserDao;
import com.minispring.stage4.service.UserService;

public class Stage4Test {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("com.minispring.stage4.service");
        UserService userService = (UserService) context.getBean("userService");
        userService.sayHello();
        UserDao userDao = (UserDao) context.getBean("userDao");
        userDao.sayHello();
    }

}
