package com.gavin.app.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gavin
 * @date 2020/3/1 9:35 上午
 */
@Api(tags = "TimeController", description = "time处理")
@RestController
@RequestMapping("/ecs/util/time")
public class TimeController {
    @ApiOperation(value = "获取10位时间戳", httpMethod = "GET")
    @RequestMapping("/timestamp10")
    public long timestamp10() {
        return System.currentTimeMillis() / 1000;
    }

    @ApiOperation(value = "获取13位时间戳", httpMethod = "GET")
    @RequestMapping("/timestamp13")
    public long timestamp13() {
        return System.currentTimeMillis();
    }
}
