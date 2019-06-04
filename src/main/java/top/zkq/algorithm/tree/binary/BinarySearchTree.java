package top.zkq.algorithm.tree.binary;

import javax.swing.*;

/**
 * @author zkq
 * @version 1.0
 * @date 2019/5/13 14:37
 */
public class BinarySearchTree<V extends Comparable<? super V>> {
    private BinaryNode<V> root;

    BinarySearchTree() {
    }

    BinarySearchTree(V v) {
        root = new BinaryNode<>(v);
    }

    public void makeEmpty() {
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public boolean contains(V v) {
        return contains(v, root);
    }


    public void remove(V v) {
        root = remove(v, root);
    }

    public void add(V v) {
        if (root == null) {
            root = new BinaryNode<>(v);
        } else {
            insert(v, root);
        }
    }

    public V findMin() {
        if (isEmpty()) {
            return null;
        }
        return findMin(root).e;
    }

    public V findMax() {
        if (isEmpty()) {
            return null;
        }
        return findMax(root).e;
    }


    public void printTree() {
        // TODO: 2019/5/13 14:59
        JFrame jFrame = new JFrame();
        jFrame.add(print(root));
        jFrame.setVisible(true);
        jFrame.pack();
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private NodePanel print(BinaryNode<V> node) {
        if (node == null) {
            return new NodePanel("");
        }
        NodePanel l = print(node.l);
        NodePanel r = print(node.r);
        return new NodePanel(String.valueOf(node.e)).setL(l).setR(r);
    }

    private BinaryNode<V> remove(V v, BinaryNode<V> node) {
        // TODO: 2019/5/13 14:59
        if (node == null) {
            return node;
        }
        int i = v.compareTo(node.e);
        if (i > 0) {
            node.r = remove(v, node.r);
        } else if (i < 0) {
            node.l = remove(v, node.l);
        } else {
            node = removeNode(node);
        }

        return node;
    }

    private BinaryNode<V> removeNode(BinaryNode<V> node) {
        if (node.l == null) {
            return node.r;
        } else if (node.r == null) {
            return node.l;
        }

        //两边均有值,找到并移除右边最min，赋值到node
        BinaryNode<V> pre = node;
        BinaryNode<V> min = node.r;
        if (min.l == null) {
            pre.r = min.r;
        } else {
            while (min.l != null) {
                pre = min;
                min = min.l;
            }
            pre.l = min.r;
        }
        node.e = min.e;
        return node;

    }


    private void insert(V v, BinaryNode<V> node) {
        // TODO: 2019/5/13 14:57

        int i = v.compareTo(node.e);
        if (i < 0) {
            if (node.l == null) {
                node.l = new BinaryNode<>(v);
            } else {
                insert(v, node.l);
            }
        } else if (i > 0) {
            if (node.r == null) {
                node.r = new BinaryNode<>(v);
            } else {
                insert(v, node.r);
            }
        }

        // else :  重复 v ignore


    }

    private BinaryNode<V> findMax(BinaryNode<V> node) {
        return node.r == null ? node : findMax(node.r);
    }

    private BinaryNode<V> findMin(BinaryNode<V> node) {
        // TODO: 2019/5/13 14:52
        return node.l == null ? node : findMin(node.l);
    }

    private boolean contains(V v, BinaryNode<V> node) {
        if (node == null) {
            return false;
        }

        int i = v.compareTo(node.e);
        if (i > 0) {
            return contains(v, node.r);
        } else if (i < 0) {
            return contains(v, node.l);
        } else {
            return true;
        }
    }
}
