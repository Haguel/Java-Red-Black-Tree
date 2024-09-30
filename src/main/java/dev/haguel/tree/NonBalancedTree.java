package dev.haguel.tree;

import java.io.InvalidObjectException;

public interface NonBalancedTree<T, V> extends Tree<T, V> {
    void addNode(Node<T, V> node);
    void removeNode() throws InvalidObjectException;
}
