package com.gavin.app.json.parser;

import com.gavin.app.json.exception.JsonParseException;
import com.gavin.app.json.model.JsonObject;
import com.gavin.app.json.tokenizer.Token;
import com.gavin.app.json.tokenizer.TokenList;
import com.gavin.app.json.tokenizer.TokenType;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

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

    public Parser() {}

    public Parser(TokenList<Token> tokens) {
        this.tokens = tokens;
    }

    public Object parse() {
        if (tokens == null || tokens.size() == 0) {
            throw new IllegalStateException("json token为空");
        }
        Token token = tokens.next();
        if (token == null) {
            return new JsonObject();
        } else if (token.getTokenType() == TokenType.START_OBJ) {
//            return parseJsonObject();
            return null;
        } else if (token.getTokenType() == TokenType.START_ARRAY) {
            return parseJsonArray();
        } else {
            throw new JsonParseException(token.getValue() + "开头格式错误");
        }
    }

//    public Object parseJsonObject() {
//        JsonObject<String, Object> jsonObject = new JsonObject();
//        int expectToken = STRING_TOKEN | END_OBJECT_TOKEN;
//        String key = null;
//        Object value = null;
//        Token token;
//
//        while (tokens.hasNext()) {
//            token = tokens.next();
//            TokenType tokenType = token.getTokenType();
//            String tokenValue = token.getValue();
//
//            switch (tokenType) {
//                case START_OBJ:
//                    checkExpectToken(token, expectToken);
////                    jsonObject.put(key, parseJsonObject());
//                    expectToken = SEP_COMMA_TOKEN | END_OBJECT_TOKEN;
//                    break;
//                case END_OBJ:
//                    checkExpectToken(token, expectToken);
//                    return jsonObject;
//                case START_ARRAY:
//                    checkExpectToken(token, expectToken);
////                    jsonObject.put(key, parseJsonArray());
//                    expectToken = END_ARRAY_TOKEN | SEP_COMMA_TOKEN;
//                    break;
//                case NULL:
//                    checkExpectToken(token, expectToken);
//                    jsonObject.put(key, null);
//                    expectToken = END_ARRAY_TOKEN | SEP_COMMA_TOKEN;
//                    break;
//                case NUMBER:
//                    checkExpectToken(token, expectToken);
//                    jsonObject.put(key, Integer.parseInt(tokenValue));
//                    expectToken = END_ARRAY_TOKEN | SEP_COMMA_TOKEN;
//                    break;
//                case BOOLEAN:
//                    checkExpectToken(token, expectToken);
//                    jsonObject.put(key, Boolean.valueOf(token.getValue()));
//                    expectToken = SEP_COMMA_TOKEN | END_OBJECT_TOKEN;
//                    break;
//                case STRING:
//                    checkExpectToken(token, expectToken);
//                    Token preToken = tokens.peekPrevious();
//                    /*
//                     * 在 JSON 中，字符串既可以作为键，也可作为值。
//                     * 作为键时，只期待下一个 Token 类型为 SEP_COLON。
//                     * 作为值时，期待下一个 Token 类型为 SEP_COMMA 或 END_OBJECT
//                     */
//                    if (preToken.getTokenType() == TokenType.COLON) {
//                        value = token.getValue();
//                        jsonObject.put(key, value);
//                        expectToken = SEP_COMMA_TOKEN | END_OBJECT_TOKEN;
//                    } else {
//                        key = token.getValue();
//                        expectToken = SEP_COLON_TOKEN;
//                    }
//                    break;
//                case COLON:
//                    checkExpectToken(token, expectToken);
//                    expectToken = NULL_TOKEN | NUMBER_TOKEN | BOOLEAN_TOKEN | STRING_TOKEN
//                            | BEGIN_OBJECT_TOKEN | BEGIN_ARRAY_TOKEN;
//                    break;
//                case COMMA:
//                    checkExpectToken(token, expectToken);
//                    expectToken = STRING_TOKEN;
//                    break;
//                case END_DOC:
//                    checkExpectToken(token, expectToken);
//                    return jsonObject;
//                default:
//                    throw new JsonParseException("Unexpected Token.");
//            }
//        }
//        throw new JsonParseException("Parse error, invalid Token.");
//    }

    // todo 数组未做
    public Object parseJsonArray() {
        return null;
    }

    public void checkExpectToken(Token token, int expectToken) {
        if ((token.getTokenType().getCode() & expectToken) == 0) {
            throw new JsonParseException("invalid token " + token.getValue());
        }
    }

}
