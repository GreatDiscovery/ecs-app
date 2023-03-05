package com.gavin.app.controller;

import com.gavin.app.dao.UserDao;
import com.gavin.app.domain.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        System.out.println(this.hashCode());
        System.out.println("hello");
        return user;
    }

    @ApiOperation(value = "获取批量用户", httpMethod = "GET")
    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public List<User> findOneUsers(@RequestParam(value = "userNames", required = true) List<String> userNames) {
        List<User> users = userDao.findByNames(userNames);
        return users;
    }
}
