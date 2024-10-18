package dev.haguel.tree;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BinaryTree<T, V> implements Tree<T, V> {
    private Node<T, V> root;

    public BinaryTree(Node<T, V> root) {
        this.root = root;
    }
}
