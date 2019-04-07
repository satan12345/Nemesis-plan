package com.able;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author jipeng
 * @date 2019-03-25 15:54
 * @description
 */
public class BST<E extends Comparable<E>> {

    public static void main(String[] args) {
        BST<Integer> bst = new BST<>();
        int[] nums = {5, 3, 6, 8, 4, 2};
        Arrays.stream(nums).forEach(i -> bst.add(i));
//        bst.preOrder();
//        System.out.println("=========");
//        bst.midOrder();
//        System.out.println("============");
//        bst.lastOrder();
//        System.out.println("==============");
        bst.levelOrder();
    }

    private Node root;
    private int size;

    public BST() {
        this.root = null;
        this.size = 0;
    }

    public E removeMax(){
        E e=maxMum();
        root=removeMax(root);
        return e;
    }

    private Node removeMax(Node node) {
        if (node.right==null) {
            Node left=node.left;
            node.left=null;
            size--;
            return left;
        }
        node.right=removeMax(node.right);
        return node;
    }

    public E removeMin(){
     E e=minMum();
     root=removeMin(root);
     return e;
    }

    private Node removeMin(Node node) {
        if (node.left==null) {
          Node right=  node.right;
          node.right=null;
          size--;
          return right;
        }
        node.left= removeMin(node.left);
        return node;
    }

    public E maxMum(){
        if (size==0) {
            throw  new IllegalArgumentException("BST is empty");
        }
        return maxMum(root).e;
    }

    private Node maxMum(Node node) {
        if (node.right==null) {
            return node;
        }
        return maxMum(node.right);
    }

    public E minMum(){
        if (size==0) {
            throw  new IllegalArgumentException("BST is empty");
        }
        return minMum(root).e;
    }

    private Node minMum(Node node) {
        if (node.left==null) {
            return node;
        }
        return minMum(node.left);
    }

    public boolean contains(E e) {
        return contains(e, root);
    }

    public void levelOrder() {
        Queue<Node> queue = new LinkedBlockingDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            if (cur == null) {
                break;
            }
            System.out.println(cur.e);
            if (cur.left != null) {

                queue.add(cur.left);
            }
            if (cur.right != null) {

                queue.add(cur.right);
            }
        }
    }

    public void lastOrder() {
        lastOrder(root);
    }

    private void lastOrder(Node node) {
        if (node != null) {

            lastOrder(node.left);
            lastOrder(node.right);
            System.out.println(node.e);
        }
    }

    public void midOrder() {
        midOrder(root);
    }

    private void midOrder(Node node) {
        if (node == null) {
            return;
        }
        midOrder(node.left);
        System.out.println(node.e);
        midOrder(node.right);
    }

    //前序遍历
    public void preOrder() {
        preOrder(root);
    }

    private void preOrder(Node node) {
        if (node == null) {
            return;
        }
        System.out.println(node.e);
        preOrder(node.left);
        preOrder(node.right);

    }

    private Boolean contains(E e, Node node) {
        if (node == null) {
            return Boolean.FALSE;
        }
        if (node.e.compareTo(e) < 0) {
            return contains(e, node.right);
        } else if (node.e.compareTo(e) > 0) {
            return contains(e, node.left);
        } else {
            return true;
        }


    }

    public void add(E e) {
//        if (root==null) {
//            root=new Node(e);
//            size++;
//            return;
//        }

        root = add(e, root);
    }

    private Node add(E e, Node node) {
        if (node == null) {
            size++;
            return new Node(e);

        }
        if (node.e.compareTo(e) < 0) {
            node.right = add(e, node.right);
            size++;
        } else if (node.e.compareTo(e) > 0) {
            node.left = add(e, node.left);
            size++;
        }
        return node;
    }

    class Node {
        E e;
        Node left;
        Node right;

        public Node(E e) {
            this.e = e;
            this.left = null;
            this.right = null;
        }


    }
}

