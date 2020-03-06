package com.gavin.app.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author gavin
 * @date 2020/3/5 10:53 下午
 */
@Api(tags = "TextController", description = "文本处理")
@RestController
@RequestMapping("/ecs/util/text")
public class TextController {

    @ApiOperation(value = "合并所有行", httpMethod = "GET")
    @RequestMapping("/join")
    public String joinLine(String text) {
        Objects.requireNonNull(text);
        return text;
    }

    @ApiOperation(value = "去除所有空格", httpMethod = "GET")
    @RequestMapping("/drop")
    public String dropSpace() {
        return "connection";
    }

    @ApiOperation(value = "以特定分割符分割字符串", httpMethod = "GET")
    @RequestMapping("/split")
    public String split() {
        return "connection";
    }

    @ApiOperation(value = "替换", httpMethod = "GET")
    @RequestMapping("/replace")
    public String replace(String text, String replaced, String replace) {
        String result = "";
        if (StringUtils.isEmpty(replaced)) {
            replaced = " ";
        }
        switch (replaced) {
            case "\\n":
                result = text.trim().replaceAll("\n", replace);
            case " " :
                result = text.trim().replaceAll(" ", replace);
        }
        return result;
    }
}
