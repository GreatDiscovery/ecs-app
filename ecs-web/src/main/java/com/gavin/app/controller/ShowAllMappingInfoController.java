package com.gavin.app.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gavin
 * @date 2020/2/22 12:31 下午
 */
@RestController
@RequestMapping("/seminar")
public class ShowAllMappingInfoController {

    @Autowired
    WebApplicationContext appContext;

    @RequestMapping(value = "/showAllMappingInfo", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray index(HttpServletRequest request)
    {

        //请求url和处理方法的映射
        JSONArray requestToMethodItemJSONArray = new JSONArray();

        //获取所有的RequestMapping
        Map<String, HandlerMapping> allRequestMappings = BeanFactoryUtils.beansOfTypeIncludingAncestors(appContext,
                HandlerMapping.class, true, false);

        for (HandlerMapping handlerMapping : allRequestMappings.values())
        {
            //本项目只需要RequestMappingHandlerMapping中的URL映射
            if (handlerMapping instanceof RequestMappingHandlerMapping)
            {
                RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) handlerMapping;
                Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
                for (Map.Entry<RequestMappingInfo, HandlerMethod> requestMappingInfoHandlerMethodEntry : handlerMethods.entrySet())
                {
                    RequestMappingInfo requestMappingInfo = requestMappingInfoHandlerMethodEntry.getKey();
                    HandlerMethod mappingInfoValue = requestMappingInfoHandlerMethodEntry.getValue();

                    RequestMethodsRequestCondition methodCondition = requestMappingInfo.getMethodsCondition();

                    PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
                    String requestUrl = patternsCondition.getPatterns().toString();

                    String controllerName = mappingInfoValue.getBeanType().toString();
                    String requestMethodName = mappingInfoValue.getMethod().getName();
                    Class<?>[] methodParamTypes = mappingInfoValue.getMethod().getParameterTypes();


                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("requestUrl", requestUrl);
                    jsonObject.put("controllerName", controllerName);
                    jsonObject.put("requestMethodName", requestMethodName);

                    requestToMethodItemJSONArray.add(jsonObject);
                }
            }
        }
        return requestToMethodItemJSONArray;
    }
}
