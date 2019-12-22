package com.gavin.app.json.tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gavin
 * @date 2019-12-19 22:42
 */
public class TokenList<E>{
    private List<E> tokens;
    private int index;

    public TokenList() {
        tokens = new ArrayList<>();
        index = 0;
    }

    public void add(E e) {
        tokens.add(e);
    }

}
