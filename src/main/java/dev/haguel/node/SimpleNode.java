package dev.haguel.node;

import dev.haguel.tree.TreeImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.InvalidObjectException;
import java.util.Comparator;

@AllArgsConstructor
@Data
@ToString(exclude = {"parent"})
public class SimpleNode<T extends Comparable<T>> implements Node<T, T> {
    private T key;
    private T value;
    private SimpleNode<T> right, left;
    private SimpleNode<T> parent;

    public SimpleNode(T value) {
        this.value = value;
        this.key = value;
        this.right = null;
        this.left = null;
        this.parent = null;
    }

    public SimpleNode(T value, SimpleNode<T> parent) {
        this.value = value;
        this.key = value;
        this.right = null;
        this.left = null;
        this.parent = parent;
    }

    private void cleanNode() {
        right = null;
        left = null;
        key = null;
        value = null;
    }

    private <V> void ensureCorrectInstance(Node<T, V> node) throws InvalidObjectException {
        if (!(node instanceof SimpleNode)) {
            throw new InvalidObjectException("Can't make action with current tree because it's not instance of SimpleNode");
        }
    }

    private boolean hasBothChildren() {
        return left != null && right != null;
    }

    private boolean hasOnlyRightChild() {
        return left == null && right != null;
    }

    private boolean hasOnlyLeftChild() {
        return left != null && right == null;
    }

    private boolean hasNoChildren() {
        return left == null && right == null;
    }

    private SimpleNode<T> getDeepestLeft() {
        if(left == null) {
            return this;
        } else {
            return left.getDeepestLeft();
        }
    }

    private SimpleNode<T> getDeepestRight() {
        if(right == null) {
            return this;
        } else {
            return right.getDeepestRight();
        }
    }

    private void exchangeWith(SimpleNode<T> node) {
        T tempKey = key;
        T tempValue = value;

        key = node.getKey();
        value = node.getValue();

        node.setKey(tempKey);
        node.setValue(tempValue);
    }

    private SimpleNode<T> removeThis() throws InvalidObjectException {
        SimpleNode<T> minRight = right == null ? null : right.getDeepestLeft();
        SimpleNode<T> maxLeft = left == null ? null : left.getDeepestRight();
        SimpleNode<T> exchangeNode;

        if (minRight != null) {
            exchangeNode = minRight;
        } else if (maxLeft != null) {
            exchangeNode = maxLeft;
        } else {
            cleanNode();

            return this;
        }

        exchangeWith(exchangeNode);

        SimpleNode<T> exchangeNodeParent = exchangeNode.parent;

        // If exchange node is left child of its parent
        if(exchangeNodeParent.getLeft() == exchangeNode) {
            if(exchangeNode.hasOnlyLeftChild()) {
                // relate its parent to its right child
                exchangeNodeParent.setLeft(exchangeNode.getLeft());
            } else if (exchangeNode.hasOnlyRightChild()) {
                // relate its parent to its left child
                exchangeNodeParent.setLeft(exchangeNode.getRight());
            } else {
                exchangeNodeParent.setLeft(null);
            }
        }
        // If exchange node is right child of its parent
        else {
            if(exchangeNode.hasOnlyRightChild()) {
                // relate its parent to its right child
                exchangeNodeParent.setRight(exchangeNode.getRight());
            } else if (exchangeNode.hasOnlyLeftChild()) {
                // relate its parent to its left child
                exchangeNodeParent.setRight(exchangeNode.getLeft());
            } else {
                exchangeNodeParent.setRight(null);
            }
        }

        exchangeNode.cleanNode();

        return this;
    }

    @Override
    public void addNode(Node<T, T> toAdd) throws InvalidObjectException {
        SimpleNode<T> toAddNode = new SimpleNode<>(toAdd.getValue());
        int compare = toAdd.compareTo(this);

        if(compare > 0) {
            if (right == null) {
                setRight(toAddNode);
            } else {
                right.addNode(toAddNode);
            }
        } else {
            if (left == null) {
                setLeft(toAddNode);
            } else {
                left.addNode(toAddNode);
            }
        }
    }

    @Override
    public SimpleNode<T> removeNode() throws InvalidObjectException {
        return removeThis();
    }


    @Override
    public TreeImpl<T, T> getTree() {
        SimpleNode copy = new SimpleNode(this.getKey());
        try {
            copy.setLeft(this.getLeft());
            copy.setRight(this.getRight());
        } catch (InvalidObjectException e) {
            e.printStackTrace();
        }

        return new TreeImpl<>(copy);
    }


    @Override
    public Node<T, T> findNodeByKey(T key) {
        if (this.key.equals(key)) {
            return this;
        } else if (key.compareTo(this.key) < 0 && left != null) {
            return left.findNodeByKey(key);
        } else if (key.compareTo(this.key) > 0 && right != null) {
            return right.findNodeByKey(key);
        }

        return null;
    }

    @Override
    public Node<T, T> findNodeByValue(T value) {
        if (this.value.equals(value)) {
            return this;
        } else {
            Node<T, T> foundNode = null;
            if (left != null) {
                foundNode = left.findNodeByValue(value);
            }
            if (foundNode == null && right != null) {
                foundNode = right.findNodeByValue(value);
            }

            return foundNode;
        }
    }

    @Override
    public void setRight(Node<T, T> right) throws InvalidObjectException {
        if(right == null) {
            this.right = null;
            return;
        }

        ensureCorrectInstance(right);
        this.right = (SimpleNode<T>) right;
        ((SimpleNode<T>) right).setParent(this);
    }

    @Override
    public void setLeft(Node<T, T> left) throws InvalidObjectException {
        if(left == null) {
            this.left = null;
            return;
        }

        ensureCorrectInstance(left);
        this.left = (SimpleNode<T>) left;
        ((SimpleNode<T>) left).setParent(this);
    }

    @Override
    public void setParent(Node<T, T> parent) throws InvalidObjectException {
        if(parent == null) {
            this.parent = null;

            return;
        }

        ensureCorrectInstance(parent);
        this.parent = (SimpleNode<T>) parent;
    }

    @Override
    public int compareTo(Node<T, T> o) {
        return Comparator.comparing(SimpleNode<T>::getKey)
                .compare(this, (SimpleNode<T>) o);
    }
}
