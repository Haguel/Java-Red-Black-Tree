package dev.haguel.tree;

public interface Tree <T, V>{
    boolean addNode(Node<T, V> node);
    void removeNode() throws Exception;

    Node<T, V> getNode();
    Tree<T, V> getRight();
    Tree<T, V> getLeft();
    void setNode(Node<T, V> node);
    void setRight(Tree<T, V> right) throws Exception;
    void setLeft(Tree<T, V> left) throws Exception;
}
