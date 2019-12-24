package com.gavin.app.json.tokenizer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author gavin
 * @date 2019-12-19 22:42
 */
public class TokenList<E> implements Iterator<E> {
    private List<E> tokens;
    private int index;

    public TokenList() {
        tokens = new ArrayList<>();
        index = 0;
    }

    public void add(E e) {
        tokens.add(e);
    }

    public int size() {
        return tokens.size();
    }

    @Override
    public boolean hasNext() {
        return index < tokens.size();
    }

    @Override
    public E next() {
        return tokens.get(index++);
    }

    public E head() {
        return tokens.get(0);
    }

    public E peekPrevious() {
        return tokens.get(Math.max(0, index - 2));
    }
}
