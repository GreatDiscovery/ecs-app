package com.gavin.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author QiangZhi
 * @date 2023/3/5 11:47
 */
@Slf4j
@Controller
public class IndexController {
    @RequestMapping(path = "/hello")
    public ModelAndView hello(){
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }
}
