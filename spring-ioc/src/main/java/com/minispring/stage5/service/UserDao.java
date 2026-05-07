package com.minispring.stage5.service;

import com.minispring.stage5.annotation.Component;

@Component("userDao")
public class UserDao {

    public void queryUser() {
        System.out.println("查询用户信息成功！");
    }
}
