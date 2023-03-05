package com.gavin.app.service.impl;

import com.gavin.app.dao.PhotoMapper;
import com.gavin.app.domain.PhotoBytes;
import com.gavin.app.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author QiangZhi
 * @date 2023/3/5 13:12
 */
@Service
public class PhotoServiceImpl implements PhotoService {

    @Autowired
    private PhotoMapper photoMapper;

    @Override
    public PhotoBytes getPhoto(Integer id) throws Exception {
        return photoMapper.getPhoto(id);
    }
}
