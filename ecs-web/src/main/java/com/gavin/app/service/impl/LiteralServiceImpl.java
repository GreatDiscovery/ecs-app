package com.gavin.app.service.impl;

import com.gavin.app.json.model.JsonElement;
import com.gavin.app.json.parser.JsonParser;
import com.gavin.app.service.LiteralService;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author gavin
 * @date 2020/1/11 11:16 下午
 */
@Service("literalService")
public class LiteralServiceImpl implements LiteralService {
    @Override
    public String parseJson(String unformat) {
        if (StringUtils.isEmpty(unformat)) {
            return "";
        }
        String escaped = StringEscapeUtils.unescapeJava(unformat);
        JsonParser jsonParser = new JsonParser();
        JsonElement o = jsonParser.parse(escaped);
        return o.prettyPrint();
    }
}
