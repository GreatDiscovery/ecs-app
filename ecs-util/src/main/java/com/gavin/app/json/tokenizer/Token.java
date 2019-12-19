package com.gavin.app.json.tokenizer;

/**
 * @author gavin
 * @date 2019-12-19 22:23
 */
public class Token {
    private TokenType tokenType;
    private String value;

    public Token(TokenType tokenType, String value) {
        this.tokenType = tokenType;
        this.value = value;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getValue() {
        return value;
    }
}
