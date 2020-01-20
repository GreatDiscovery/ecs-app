package com.gavin.app.json.parser;


import com.gavin.app.json.model.JsonElement;
import com.gavin.app.json.model.JsonObject;
import com.gavin.app.json.tokenizer.CharReader;
import com.gavin.app.json.tokenizer.Tokenizer;

import java.io.IOException;
import java.io.StringReader;

/**
 * @author gavin
 * @date 2019-12-25 00:10
 */
public class ParserTest {
    public static void main(String[] args) throws IOException {
        String json = "{\"name\":\"gavin\",\"sex\":\"male\"}";
        CharReader charReader = new CharReader(new StringReader(json));
        Tokenizer tokenizer = new Tokenizer(charReader);
        Parser parser = new Parser(tokenizer.startTokenize());
        Object o = parser.parse();
        System.out.println(o);
    }
}

class GJsonParse {
    public static void main(String[] args) {
        String json = "{\"name\":[\"gavin\", \"sbw\"]}";
        JsonParser jsonParser = new JsonParser();
        JsonElement o = jsonParser.parse(json);
        System.out.println(o.prettyPrint());
    }
}