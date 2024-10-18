package dev.haguel.tree;

import java.io.InvalidObjectException;

public class BinaryTreeSimpleNode<T extends Comparable<T>> extends SimpleNode<T> implements TreeableNode<T, T> {
    public BinaryTreeSimpleNode(T value) {
        super(value);
    }

    @Override
    public BinaryTree<T, T> getTree() {
        BinaryTreeSimpleNode copy = new BinaryTreeSimpleNode(this.getKey());
        try {
            copy.setLeft(this.getLeft());
            copy.setRight(this.getRight());
        } catch (InvalidObjectException e) {
            e.printStackTrace();
        }

        return new BinaryTree<>(copy);
    }
}
