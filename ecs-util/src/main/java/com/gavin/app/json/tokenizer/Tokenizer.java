package com.gavin.app.json.tokenizer;

import com.gavin.app.json.model.JsonParseException;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public TokenList startTokenize() throws IOException {
        Token token = null;
        do {
            token = read();
            tokens.add(token);
        } while (token.getTokenType() != TokenType.END_DOC);
        return tokens;
    }

    private Token read() throws IOException {
        char ch;
        Token token;
        for (; ; ) {
            if ((ch = (char) charReader.read()) == -1) {
                return new Token(TokenType.END_DOC, null);
            }
            if (!isWhiteChar(ch)) break;
        }

        switch (ch) {
            case '{':
                token = new Token(TokenType.START_OBJ, String.valueOf(ch));
                break;
            case '}':
                token = new Token(TokenType.END_OBJ, String.valueOf(ch));
                break;
            case '[':
                token = new Token(TokenType.START_ARRAY, String.valueOf(ch));
                break;
            case ']':
                token = new Token(TokenType.END_ARRAY, String.valueOf(ch));
                break;
            case ',':
                token = new Token(TokenType.COMMA, String.valueOf(ch));
                break;
            case ':':
                token = new Token(TokenType.COLON, String.valueOf(ch));
                break;
            case 'n':
            case 'N':
                token = readNull();
                break;
            case 't':
            case 'f':
                token = readBoolean();
                break;
            case '"':
                token = readString();
                break;
            // 负数和数值
            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                token = readNum();
                break;
            default:
                token = new Token(TokenType.END_DOC, null);
        }
        return token;
    }

    private boolean isWhiteChar(char ch) {
        return (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r');
    }

    private Token readNull() throws IOException {
        if (!(charReader.read() == 'u' && charReader.read() == 'l' && charReader.read() == 'l')) {
            throw new JsonParseException("null值错误");
        }
        return new Token(TokenType.NULL, "null");
    }

    private Token readBoolean() throws IOException {
        if ('t' == charReader.read()) {
            if (!(charReader.read() == 'r' && charReader.read() == 'u' && charReader.read() == 'e')) {
                throw new JsonParseException("true值错误");
            }
            return new Token(TokenType.BOOLEAN, "true");
        } else {
            if (!(charReader.read() == 'a' && charReader.read() == 'l' && charReader.read() == 's' &&
                    charReader.read() == 'e')) {
                throw new JsonParseException("false值错误");
            }
            return new Token(TokenType.BOOLEAN, "false");
        }
    }

    private Token readString() throws IOException {
        StringBuilder sb = new StringBuilder();
        char ch;
        while ((ch = (char) charReader.read()) != '"') {
            sb.append(ch);
        }
        return new Token(TokenType.STRING, sb.toString());
    }

    // todo 未完成,需要加上负数和小数
    private Token readNum() throws IOException {
        StringBuilder sb = new StringBuilder();
        int ch = charReader.peek();
        for (; ;) {
            if (isDigit(ch)) {
                sb.append(ch);
            } else {
                charReader.back();
                break;
            }
        }
        return new Token(TokenType.NUMBER, sb.toString());
    }

    private boolean isDigit(int num) {
        return num >= 48 && num <= 57;
    }

}
