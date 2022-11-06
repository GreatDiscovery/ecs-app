package com.gavin.app.dao;

import com.gavin.app.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author gavin
 * @date 2021/4/17 上午12:07
 */
@Mapper
public interface UserDao {
    User findByName(String name);

    List<User> findByNames(List<String> nameList);
}
