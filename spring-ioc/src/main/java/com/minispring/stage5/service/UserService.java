package com.minispring.stage5.service;

import com.minispring.stage5.annotation.Autowired;
import com.minispring.stage5.annotation.Component;
import com.minispring.stage5.annotation.PostConstruct;

@Component("userService")
public class UserService {

    @Autowired
    private UserDao userDao;

    // 初始化方法，依赖注入完成后执行
    @PostConstruct
    public void init() {
        System.out.println("UserService的初始化方法执行了！");
    }

    public void test() {
        System.out.println("UserService的test方法");
        userDao.queryUser();
    }

}
