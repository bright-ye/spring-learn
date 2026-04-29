package com.minispring.stage2;

import com.minispring.stage2.service.UserDao;
import com.minispring.stage2.service.UserService;

public class Stage2Test {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        UserService userService = (UserService) context.getBean("userService");
        userService.sayHello();
        UserDao userDao = (UserDao) context.getBean("userDao");
        userDao.sayHello();
    }

}
