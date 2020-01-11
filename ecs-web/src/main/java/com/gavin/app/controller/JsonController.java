package com.gavin.app.controller;

import com.gavin.app.json.model.JsonElement;
import com.gavin.app.json.parser.JsonParser;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Json在线转换接口
 *
 * @author gavin
 * @date 2019-12-17 22:26
 */

@Controller
@RequestMapping("/util")
public class JsonController {

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
    public String escapeString(ModelAndView modelAndView) {
        String str = (String) modelAndView.getModel().get("unescapedString");
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        str = StringEscapeUtils.escapeJava(str);
        return str;
    }

    @RequestMapping("parse")
    public String parse(String json) {
        if (StringUtils.isEmpty(json)) {
            return "";
        }

        JsonParser jsonParser = new JsonParser();
        JsonElement o = jsonParser.parse(json);
        return o.toString();
    }
}
