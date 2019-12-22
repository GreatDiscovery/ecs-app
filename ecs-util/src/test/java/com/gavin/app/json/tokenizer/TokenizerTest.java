package com.gavin.app.json.tokenizer;

import java.io.IOException;
import java.io.StringReader;

/**
 * @author gavin
 * @date 2019-12-22 22:09
 */
public class TokenizerTest {
    public static void main(String[] args) throws IOException {
        String json = "{\"name\":\"gavin\",\"sex\":\"male\"}";
        CharReader charReader = new CharReader(new StringReader(json));
        Tokenizer tokenizer = new Tokenizer(charReader);
        TokenList list = tokenizer.startTokenize();
    }
}