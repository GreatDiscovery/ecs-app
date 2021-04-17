package com.gavin.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.gavin.app.dao.UserDao;
import com.gavin.app.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gavin
 * @date 2021/4/17 上午9:54
 */
@RestController
public class UserController {
    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public User findOneUser(@RequestParam(value = "userName", required = true) String userName) {
        User user = userDao.findByName(userName);
        System.out.println(JSONObject.toJSONString(user));
        return user;
    }
}
