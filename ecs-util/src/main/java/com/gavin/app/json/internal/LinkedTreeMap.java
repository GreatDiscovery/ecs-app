package com.gavin.app.json.internal;


import java.io.Serializable;
import java.util.*;

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

    @Override
    public V get(Object key) {
        Node<K, V> node = findObject(key);
        return node != null ? node.value : null;
    }

    @Override
    public V remove(Object key) {
        Node<K, V> node = removeInternalByKey(key);
        return node != null ? node.value : null;
    }

    private Node<K, V> findObject(Object key) {
        return key != null ? find((K) key, false) : null;
    }

    public Node<K, V> find(K key, boolean create) {
        Comparator<? super K> comparator = this.comparator;
        Node<K, V> nearest = root;
        int comparison = 0;

        if (nearest != null) {
            while (true) {
                // 如果按照自然排序，需要强制转成Comparable接口，防止子类多态重写comparaTo方法
                Comparable<Object> comparableKey = comparator == NATURA_ORDER ? (Comparable<Object>) key : null;
                // 查找用二叉树查找
                comparison = (comparableKey == null) ? comparator.compare(key, nearest.key) :
                        comparableKey.compareTo(nearest.key);
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
            // 插入用的循环双向链表插入，最后形成环，最后的节点next指向header
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
                    rotateRight(right);
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
                    rotateLeft(left);
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

    private void rotateLeft(Node<K, V> root) {
        Node<K, V> left = root.left;
        Node<K, V> pivot = root.right;
        Node<K, V> pivotLeft = pivot.left;
        Node<K, V> pivotRight = pivot.right;

        root.right = pivotLeft;
        if (pivotLeft != null) {
            pivotLeft.parent = root;
        }

        replaceInParent(root, pivot);

        pivot.left = root;
        root.parent = pivot;

        root.height = Math.max(root.left != null ? root.left.height : 0,
                root.right != null ? root.right.height : 0);
        pivot.height = Math.max(root.height, pivot.right != null ? pivot.right.height : 0);
    }

    private void rotateRight(Node<K, V> root) {
        Node<K, V> pivot = root.left;
        Node<K, V> right = root.right;
        Node<K, V> pivotLeft = pivot.left;
        Node<K, V> pivotRight = pivot.right;

        root.left = pivotRight;
        if (pivotRight != null) {
            pivotRight.parent = root;
        }

        replaceInParent(root, pivot);

        pivot.right = root;
        root.parent = pivot;

        root.height = Math.max(right != null ? right.height : 0, root.left != null ? root.left.height : 0);
        pivot.height = Math.max(root.height, pivotLeft != null ? pivotLeft.height : 0);
    }

    private void replaceInParent(Node<K, V> node, Node<K, V> replacement) {
        Node<K, V> parent = node.parent;
        node.parent = null;

        if (replacement != null) {
            replacement.parent = parent;
        }
        if (parent != null) {
            if (parent.left == node) {
                parent.left = replacement;
            } else {
                parent.right = replacement;
            }
        } else {
            root = replacement;
        }
    }

    public Node<K, V> removeInternalByKey(Object key) {
        Node<K, V> node = findObject(key);
        if (node != null) {
            removeInternal(node, true);
        }
        return node;
    }

    public void removeInternal(Node<K, V> node, boolean unlink) {
        // 先解开双向链表
        if (unlink) {
            node.pre.next = node.next;
            node.next.pre = node.pre;
        }

        Node<K, V> left = node.left;
        Node<K, V> right = node.right;
        Node<K, V> originParent = node.parent;

        if (left != null && right != null) {
            // 这里按照中序遍历，找左边小于root、右边大于root的相邻节点去补上删除的节点
            Node<K, V> adjacent = left.height > right.height ? left.last() : right.first();
            // 递归先把临近的节点给删掉，然后再去替换node节点位置
            removeInternal(adjacent, false);

            int leftHeight = 0;
            if (left != null) {
                leftHeight = left.height;
                adjacent.left = left;
                left.parent = adjacent;
                node.left = null;
            }

            int rightHeight = 0;
            if (right != null) {
                rightHeight = right.height;
                adjacent.right = right;
                right.parent = adjacent;
                node.right = null;
            }

            adjacent.height = Math.max(leftHeight, rightHeight) + 1;
            replaceInParent(node, adjacent);
            return;
        } else if (left != null) {
            replaceInParent(node, left);
            node.left = null;
        } else if (right != null) {
            replaceInParent(node, right);
            node.right = null;
        } else {
            replaceInParent(node, null);
        }

        // 所有remove操作都会运行以下三步
        rebalance(originParent, false);
        size--;
        modCount++;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    public static final class Node<K, V> implements Entry<K, V> {
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
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V value) {
            return this.value = value;
        }

        private Node<K, V> last() {
            Node<K, V> node = this;
            Node<K, V> child = node.right;
            while (child != null) {
                node = child;
                child = child.right;
            }
            return node;
        }

        private Node<K, V> first() {
            Node<K, V> node = this;
            Node<K, V> child = node.left;
            while (child != null) {
                node = child;
                child = child.left;
            }
            return node;
        }
    }

    private abstract class LinkedTreeMapIterator<T> implements Iterator<T> {
        Node<K, V> next = header.next;
        // 记录上一次返回的节点
        Node<K, V> lastReturned = null;
        int expectedModCount = modCount;

        @Override
        public boolean hasNext() {
            return next != header;
        }

        public Node<K, V> nextNode() {
            Node<K, V> e = next;
            if (e == header) {
                throw new NoSuchElementException();
            }
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            next = e.next;
            return lastReturned = e;
        }
    }
}
