package com.gavin.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Json在线转换接口
 *
 * @author gavin
 * @date 2019-12-17 22:26
 */

@Controller
@RequestMapping("/util")
public class JsonController {

    @RequestMapping("/hello")
    public String hello() {
        return "index";
    }

    @RequestMapping("json")
    public String formatJson(Model model) {
       return "";
    }
}
