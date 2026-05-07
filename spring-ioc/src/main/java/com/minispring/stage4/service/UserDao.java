package com.minispring.stage4.service;

import com.minispring.stage4.annotation.Component;

@Component("userDao")
public class UserDao {

    public void sayHello() {
        System.out.println("UserDao Hello!");
    }
}
