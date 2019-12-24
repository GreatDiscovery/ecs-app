package com.gavin.app.json.tokenizer;

/**
 * @author gavin
 * @date 2019-12-19 22:20
 */
public enum TokenType {
    START_OBJ(1),
    END_OBJ(1 << 1),
    NULL(1 << 2),
    START_ARRAY(1 << 3),
    END_ARRAY(1 << 4),
    NUMBER(1 << 5),
    STRING(1 << 6),
    BOOLEAN(1 << 7),
    COLON(1 << 8),
    COMMA(1 << 9),
    END_DOC(1 << 10);


    TokenType(int code) {
        this.code = code;
    }
    private int code;

    public int getCode() {
        return code;
    }
}
