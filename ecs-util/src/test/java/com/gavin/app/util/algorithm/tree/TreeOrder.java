package com.gavin.app.util.algorithm.tree;

import org.junit.Test;

import java.util.*;

/**
 * 树的三种非递归遍历方式
 * 和递归的三种方式
 *    5
 *   3  6
 *  1 4
 *
 * @author gavin
 * @date 2020/8/15 5:00 下午
 */
public class TreeOrder {
    Node root = new Node(5);

    public TreeOrder() {
        root.left = new Node(3);
        root.right = new Node(6);
        root.left.left = new Node(1);
        root.left.right = new Node(4);
    }

    // 前序遍历：根左右 5->3->1->4->6
    @Test
    public void preOrder() {
        List<Integer> result = new ArrayList<>();
        Stack<Node> stack = new Stack<>();
        Node tmp = root;
        stack.push(tmp);
        while (!stack.empty()) {
            Node node = stack.pop();
            result.add(node.value);
            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }
        System.out.println(result.toString());
    }

    // 中序遍历：左根右 1->3->4->5->6
    @Test
    public void midOrder() {
        List<Integer> result = new ArrayList<>();
        Stack<Node> stack = new Stack<>();
        Node tmp = root;
        while (tmp != null || !stack.empty()) {
            while (tmp != null) {
                stack.push(tmp);
                tmp = tmp.left;
            }
            tmp = stack.pop();
            result.add(tmp.value);
            tmp = tmp.right;
        }
        System.out.println(result);
    }

    // 后序遍历：左右根 1->4->3->6->5
    @Test
    public void lastOrder() {
        List<Integer> result = new ArrayList<>();
        Stack<Node> stack = new Stack();
        stack.push(root);
        while (!stack.empty()) {
            Node tmp = stack.pop();
            result.add(0, tmp.value);
            if (tmp.left != null) {
                stack.push(tmp.left);
            }
            if (tmp.right != null) {
                stack.push(tmp.right);
            }
        }
        System.out.println(result);
    }

    // 层序遍历：5->3->6->1->4
    @Test
    public void bfsOrder() {
        List<Integer> result = new ArrayList<>();
        Queue<Node> q = new LinkedList<>();
        q.offer(root);
        while (!q.isEmpty()) {
            int size = q.size();
            while (size > 0) {
                Node tmp = q.poll();
                result.add(tmp.value);
                if (tmp.left != null) {
                    q.offer(tmp.left);
                }
                if (tmp.right != null) {
                    q.offer(tmp.right);
                }
                size--;
            }
        }
        System.out.println(result);
    }
}

class Node {
    int value;
    Node left;
    Node right;

    public Node(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
