package com.gavin.app.service;

import com.gavin.app.domain.PhotoBytes;

/**
 * @author QiangZhi
 * @date 2023/3/5 13:11
 */
public interface PhotoService {
    public PhotoBytes getPhoto(Integer id) throws Exception;
}
