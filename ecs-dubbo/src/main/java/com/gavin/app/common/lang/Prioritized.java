package com.gavin.app.common.lang;

import static java.lang.Integer.compare;

/**
 * @author gavin
 * @date 2021/6/12 下午11:19
 */
public interface Prioritized extends Comparable<Prioritized> {
    int MAX_PRIORITY = Integer.MAX_VALUE;
    int MIN_PRIORITY = Integer.MIN_VALUE;
    int NORMAL_PRIORITY = 0;

    default int getPriority() {
        return NORMAL_PRIORITY;
    }

    @Override
    default int compareTo(Prioritized that) {
        return compare(this.getPriority(), that.getPriority());
    }
}

