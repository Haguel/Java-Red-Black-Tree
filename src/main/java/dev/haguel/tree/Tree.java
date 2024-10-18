package dev.haguel.tree;

import java.io.InvalidObjectException;

public interface Tree <T, V>{
    Node<T, V> getRoot();
}
