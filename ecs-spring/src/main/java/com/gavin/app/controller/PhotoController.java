package com.gavin.app.controller;

import com.gavin.app.domain.PhotoBytes;
import com.gavin.app.service.PhotoService;
import com.gavin.app.util.Base64ImageUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 上传/下载blob文件照片
 *
 * @author QiangZhi
 * @date 2023/3/5 13:08
 */
@Controller
public class PhotoController {
    @Autowired
    private PhotoService service;

    @ApiOperation(value = "查询mysql-blob格式照片", httpMethod = "GET")
    @RequestMapping(path = "/photo/{id}")
    public ModelAndView getPhoto(@PathVariable(name = "id") Integer id) throws Exception {
        PhotoBytes photoBytes = service.getPhoto(id);
        byte[] photoBytes2 = photoBytes.getPhotoBytes();
        //将二进制转为Base64格式字符串
        String photo64 = Base64ImageUtil.byteArr2String(photoBytes2);
        ModelAndView modelAndView = new ModelAndView("image");
        modelAndView.addObject("image", "data:image/png;base64," + photo64);
        return modelAndView;
    }
}
