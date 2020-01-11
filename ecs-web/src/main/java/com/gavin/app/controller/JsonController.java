package com.gavin.app.controller;

import com.gavin.app.json.model.JsonElement;
import com.gavin.app.json.parser.JsonParser;
import com.gavin.app.service.LiteralService;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Json在线转换接口
 *
 * @author gavin
 * @date 2019-12-17 22:26
 */

@RestController
@RequestMapping("/util")
public class JsonController {

    @Autowired
    private LiteralService literalService;

    @RequestMapping("/con")
    public String connect() {
        return "connection";
    }

    @RequestMapping("/index")
    public String hello() {
        return "index";
    }

    @RequestMapping("format")
    public String formatJson(Model model) {
       return "";
    }

    @RequestMapping("/escape")
    public String escapeString(String unescaped) {
        if (StringUtils.isEmpty(unescaped)) {
            return "";
        }
        return StringEscapeUtils.escapeJava(unescaped);
    }

    @RequestMapping("parse")
    public String parse(String unformat) {
        return literalService.parseJson(unformat);
    }
}
