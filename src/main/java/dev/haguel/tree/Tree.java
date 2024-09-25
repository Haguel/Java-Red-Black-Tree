package dev.haguel.tree;

import java.io.InvalidObjectException;

public interface Tree <T, V>{
    void addNode(Node<T, V> node);
    void removeNode() throws InvalidObjectException;

    Node<T, V> getNode();
    Tree<T, V> getRight();
    Tree<T, V> getLeft();
    void setNode(Node<T, V> node);
    void setRight(Tree<T, V> right) throws InvalidObjectException;
    void setLeft(Tree<T, V> left) throws InvalidObjectException;
}
