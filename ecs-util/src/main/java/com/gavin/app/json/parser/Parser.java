package com.gavin.app.json.parser;

import com.gavin.app.json.exception.JsonParseException;
import com.gavin.app.json.model.JsonObject;
import com.gavin.app.json.tokenizer.Token;
import com.gavin.app.json.tokenizer.TokenList;
import com.gavin.app.json.tokenizer.TokenType;

/**
 * @author gavin
 * @date 2019-12-22 21:53
 */
public class Parser {
    private static final int BEGIN_OBJECT_TOKEN = 1;
    private static final int END_OBJECT_TOKEN = 1 << 1;
    private static final int BEGIN_ARRAY_TOKEN = 1 << 2;
    private static final int END_ARRAY_TOKEN = 1 << 3;
    private static final int NULL_TOKEN = 1 << 4;
    private static final int NUMBER_TOKEN = 1 << 5;
    private static final int STRING_TOKEN = 1 << 6;
    private static final int BOOLEAN_TOKEN = 1 << 7;
    private static final int SEP_COLON_TOKEN = 1 << 8;
    private static final int SEP_COMMA_TOKEN = 1 << 9;

    private TokenList<Token> tokens;

    private Parser() {}
    public Parser(TokenList<Token> tokens) {
        this.tokens = tokens;
    }

    public Object parse() {
        if (tokens == null || tokens.size() == 0) {
            throw new IllegalStateException("json token为空");
        }
        Token token = tokens.head();
        if (token == null) {
            return new JsonObject();
        } else if (token.getTokenType() == TokenType.START_OBJ) {
            return parseJsonObject();
        } else if (token.getTokenType() == TokenType.START_ARRAY) {
            return parseJsonArray();
        } else {
            throw new JsonParseException(token.getValue() + "开头格式错误");
        }
    }

    public Object parseJsonObject() {
        JsonObject<String, Object> jsonObject = new JsonObject();
        String key = "";
        Object value = null;
        tokens.forEachRemaining((token) -> {
            TokenType tokenType = token.getTokenType();
            String tokenValue = token.getValue();
            switch (tokenType) {
                case START_OBJ:
                    jsonObject.put(tokenValue, parseJsonObject());
            }
        });
        return null;
    }

    public Object parseJsonArray() {
        return null;
    }
}
