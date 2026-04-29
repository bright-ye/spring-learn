package com.minispring.stage3.service;

import com.minispring.stage3.annotation.Component;

@Component("userService")
public class UserService {

    public void sayHello() {
        System.out.println("UserService Hello!");
    }

}
