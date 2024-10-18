package dev.haguel.tree;

public interface TreeableNode<T, V> extends Node<T, V> {
    Tree<T, V> getTree();
}
