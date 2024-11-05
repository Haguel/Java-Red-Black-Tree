package dev.haguel.tree;

import dev.haguel.node.Node;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TreeImpl<T, V> implements Tree<T, V> {
    private Node<T, V> root;

    public TreeImpl(Node<T, V> root) {
        this.root = root;
    }
}
