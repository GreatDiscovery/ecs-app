package com.gavin.app.dao;

import com.gavin.app.domain.PhotoBytes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author QiangZhi
 * @date 2023/3/5 13:13
 */
@Mapper
public interface PhotoMapper {
    public PhotoBytes getPhoto(@Param("id") Integer id) throws Exception;
}
