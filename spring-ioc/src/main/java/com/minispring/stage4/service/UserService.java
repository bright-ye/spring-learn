package com.minispring.stage4.service;

import com.minispring.stage4.annotation.Component;
import com.minispring.stage4.annotation.Autowired;

@Component("userService")
public class UserService {

    @Autowired
    private UserDao userDao;

    public void sayHello() {
        System.out.println("UserService的test方法");
        // 调用UserDao的方法
        userDao.sayHello();
    }

}
