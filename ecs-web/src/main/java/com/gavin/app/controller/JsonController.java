package com.gavin.app.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gavin.app.json.model.JsonElement;
import com.gavin.app.json.parser.JsonParser;
import com.gavin.app.service.LiteralService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Json在线转换接口
 *
 * @author gavin
 * @date 2019-12-17 22:26
 */
@Api(tags = "JsonController", description = "json处理")
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

    @ApiOperation("json格式化")
    @RequestMapping("parse")
    public String parse(String unformat) {
        return literalService.parseJson(unformat);
    }

}
