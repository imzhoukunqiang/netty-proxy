package top.zkq.algorithm.tree.binary;

/**
 * @author zkq
 * @version 1.0
 * @date 2019/5/13 14:35
 */
public class BinaryNode<T> {
    T e;
    BinaryNode<T> l;
    BinaryNode<T> r;

    public BinaryNode(T e) {
        this(e, null, null);
    }

    public BinaryNode(T e, BinaryNode<T> l, BinaryNode<T> r) {
        if (e == null) {
            throw new NullPointerException("node element can not be null");
        }
        this.e = e;
        this.l = l;
        this.r = r;
    }
}
