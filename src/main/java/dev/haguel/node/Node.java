package dev.haguel.node;

import dev.haguel.tree.Tree;

import java.io.InvalidObjectException;

public interface Node<T, V> extends Comparable<Node<T, V>> {
    T getKey();
    V getValue();
    Node<T, V> getRight();
    Node<T, V> getLeft();
    void setKey(T key);
    void setValue(V value);
    void setRight(Node<T, V> right) throws InvalidObjectException;
    void setLeft(Node<T, V> left) throws InvalidObjectException;

    void addNode(Node<T, V> node) throws InvalidObjectException;
    Node<T, V> removeNode() throws InvalidObjectException;
    Tree<T, V> getTree();
}
