package dev.haguel.tree;

public interface Node <T, V> extends Comparable<Node<T, V>> {
    T getKey();
    V getValue();
    void setKey(T key);
    void setValue(V value);
}
