package com.minispring.stage4.service;

import com.minispring.stage4.annotation.Autowired;
import com.minispring.stage4.annotation.Component;

@Component("userService")
public class UserService {

    @Autowired
    private UserDao userDao;

    public void sayHello() {
        System.out.println("UserService Hello!");
        userDao.sayHello();
    }

}
