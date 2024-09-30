package dev.haguel.tree;

import java.io.InvalidObjectException;

public interface Tree <T, V>{
    Node<T, V> getNode();
    Tree<T, V> getRight();
    Tree<T, V> getLeft();
}
