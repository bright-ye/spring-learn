package com.minispring.stage3.service;

import com.minispring.stage3.annotation.Component;

@Component("userDao")
public class UserDao {

    public void sayHello() {
        System.out.println("UserDao Hello!");
    }
}
