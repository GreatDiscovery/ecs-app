package com.gavin.app.json.internal;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 按照插入顺序进行迭代的map，区别于TreeMap使用比较序
 *
 * @author gavin
 * @date 2019-12-29 21:11
 */
public final class LinkedTreeMap<K, V> extends AbstractMap<K, V> implements Serializable {

    private static final Comparator<Comparable> NATURA_ORDER = (a, b) -> {
        return a.compareTo(b);
    };
    private Comparator<? super K> comparator;

    final Node<K, V> header = new Node<>();
    Node<K, V> root;
    int size = 0;
    int modCount = 0;


    public LinkedTreeMap() {
        this((Comparator<? super K>) NATURA_ORDER);
    }

    public LinkedTreeMap(Comparator<? super K> comparator) {
        this.comparator = comparator != null ? comparator : (Comparator<? super K>) NATURA_ORDER;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public V put(K key, V value) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        Node<K, V> create = find(key, true);
        // 用于返回旧值
        V result = create.value;
        create.value = value;
        return result;
    }

    public Node<K, V> find(K key, boolean create) {
        Comparator<? super K> comparator = this.comparator;
        Node<K, V> nearest = root;
        int comparison = 0;

        while (true) {
            // 如果按照自然排序，需要强制转成Comparable接口，防止子类多态重写comparaTo方法
            Comparable<Object> comparableKey = comparator == NATURA_ORDER ? (Comparable<Object>) key : null;
            // 查找用二叉树查找
            if (nearest != null) {
                comparison = (comparableKey == null) ? comparator.compare(key, nearest.key) :
                        comparableKey.compareTo(nearest.key);
            }

            if (comparison == 0) {
                return nearest;
            }

            // 递归往下找
            Node<K, V> child = comparison < 0 ? nearest.left : nearest.right;
            if (child == null) {
                break;
            }

            nearest = child;
        }

        if (!create) {
            return null;
        }

        Node<K, V> header = this.header;
        Node<K, V> created;

        // 创建新的节点
        if (nearest == null) {

            // 双向链表的插入
            created = new Node(nearest, key, header, header.pre);
            root = created;
        } else {

            // 插入用的双向链表插入
            created = new Node(nearest, key, header, header.pre);

            if (comparison < 0) {
                nearest.left = created;
            } else {
                nearest.right = created;
            }
            // 树的高度是从下往上的，所以新创建的节点的高度默认为1
            rebalance(nearest, true);
        }

        size++;
        modCount++;
        return created;
    }

    private void rebalance(Node<K, V> unbalanced, boolean insert) {
        for (Node<K, V> node = unbalanced; node != null; node = node.parent) {
            Node<K, V> left = node.left;
            Node<K, V> right = node.right;

            int leftHeight = left != null ? left.height : 0;
            int rightHeight = right != null ? right.height : 0;
            int delta = leftHeight - rightHeight;

            if (delta == -2) {
                Node<K, V> rightLeft = right.left;
                Node<K, V> rightRight = right.right;
                int rightLeftHeight = rightLeft != null ? rightLeft.height : 0;
                int rightRightHeight = rightRight != null ? rightRight.height : 0;

                int rightDelta = rightLeftHeight - rightRightHeight;
                if (rightDelta == -1 || (rightDelta == 0 && !insert)) {
                    rotateLeft(node);
                } else {
                    rotateRight(node);
                    rotateLeft(node);
                }
                if (insert) {
                    break;
                }

            } else if (delta == 2) {
                Node<K, V> leftLeft = left.left;
                Node<K, V> leftRight = left.right;
                int leftLeftHeight = leftLeft != null ? leftLeft.height : 0;
                int leftRightHeight = leftRight != null ? leftRight.height : 0;

                int leftDelta = leftLeftHeight - leftRightHeight;
                if (leftDelta == 1 || (leftDelta == 0 && !insert)) {
                    rotateRight(node);
                } else {
                    rotateLeft(node);
                    rotateRight(node);
                }
                if (insert) {
                    break;
                }
            } else if (delta == 0) {
                node.height = leftHeight + 1;
                if (insert) {
                    break;
                }
            } else {
                if (delta == 1 || delta == -1) {
                    node.height = Math.max(leftHeight, rightHeight) + 1;
                }
                if (!insert) {
                    break;
                }
            }
        }
    }

    private void rotateLeft(Node<K, V> node) {
    }

    private void rotateRight(Node<K, V> node) {

    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return null;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {

    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {

    }

    @Override
    public V putIfAbsent(K key, V value) {
        return null;
    }

    @Override
    public boolean remove(Object key, Object value) {
        return false;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return false;
    }

    @Override
    public V replace(K key, V value) {
        return null;
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return null;
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    static final class Node<K, V> implements Entry<K, V> {
        Node<K, V> parent;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> next;
        Node<K, V> pre;
        final K key;
        int height;
        V value;

        Node() {
            key = null;
            pre = next = this;
        }

        Node(Node<K, V> parent, K key, Node<K, V> next, Node<K, V> pre) {
            this.parent = parent;
            this.key = key;
            this.next = next;
            this.pre = pre;
            this.height = 1;
            next.pre = this;
            pre.next = this;
        }

        @Override
        public K getKey() {
            return null;
        }

        @Override
        public V getValue() {
            return null;
        }

        @Override
        public V setValue(V value) {
            return null;
        }
    }
}
