package com.gavin.app.json.tokenizer;

/**
 * @author gavin
 * @date 2019-12-19 22:35
 */
public class Tokenizer {
    private CharReader charReader;
    private TokenList<Token> tokens;

    public Tokenizer(CharReader charReader) {
        this.charReader = charReader;
        this.tokens = new TokenList<>();
    }

    public void startTokenize() {
        Token token = null;
        do {
            tokens.add(token);
        } while ((token = charReader.read()).getTokenType() != TokenType.END_DOC);
    }
}
