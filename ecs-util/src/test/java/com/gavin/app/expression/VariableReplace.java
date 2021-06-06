package com.gavin.app.expression;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.HashMap;
import java.util.Map;

/**
 * 占位符替换
 *
 * @author: Gavin
 * @date: 2021/6/6 9:43
 * @description:
 */
public class VariableReplace {
    public static void main(String[] args) {
        String phoneNo = "13812341234";
        String smsTemplate = "验证码:#{[code]},您正在登录管理后台，5分钟内输入有效。";
        Map<String, Object> params = new HashMap<>();
        params.put("code", 1234);

        ExpressionParser parser = new SpelExpressionParser();
        TemplateParserContext parserContext = new TemplateParserContext();
        String content = parser.parseExpression(smsTemplate, parserContext).getValue(params, String.class);

        System.out.println(content);
    }
}
