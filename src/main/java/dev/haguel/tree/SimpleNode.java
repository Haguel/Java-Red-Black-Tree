package dev.haguel.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InvalidObjectException;
import java.util.Comparator;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimpleNode<T extends Comparable<T>> implements Node<T, T> {
    private T key;
    private T value;
    private SimpleNode<T> right, left;

    public SimpleNode(T value) {
        this.value = value;
        this.key = value;
        this.right = null;
        this.left = null;
    }

    private void cleanNode() {
        right = null;
        left = null;
        key = null;
        value = null;
    }

    private void transformTo(SimpleNode<T> node) {
        this.right = node.getRight();
        this.left = node.getLeft();
        node.key = null;
        node.value = null;

        node.cleanNode();
    }

    private void transformTo_remainLeft(SimpleNode<T> node) {
        this.right = node.getRight();
        this.value = node.getValue();
        this.key = node.getKey();

        node.cleanNode();
    }

    private boolean isEmpty() {
        return key == null && value == null && right == null && left == null;
    }

    private <V> void ensureCorrectInstance(Node<T, V> node) throws InvalidObjectException {
        if (!(node instanceof SimpleNode)) {
            throw new InvalidObjectException("Can't make action with current tree because it's not instance of BinaryTree");
        }
    }

    private void addBeforeLeft(SimpleNode<T> toAdd) throws InvalidObjectException {
        SimpleNode<T> temp = left;
        left = toAdd;
        left.setLeft(temp);
        left.setRight(null);
    }

    private void addBeforeRight(SimpleNode<T> toAdd) throws InvalidObjectException {
        SimpleNode<T> temp = right;
        right = toAdd;
        right.setLeft(temp);
        right.setRight(null);
    }

    @Override
    public void addNode(Node<T, T> toAdd) throws InvalidObjectException {
        ensureCorrectInstance(toAdd);
        SimpleNode<T> toAddNode = (SimpleNode<T>) toAdd;
        int compare = toAdd.compareTo((Node<T, T>) this);

        if(compare > 0) {
            if (right == null) {
                right = toAddNode;
            } else {
                right.addNode(toAddNode);
            }
        } else {
            if (left == null) {
                left = toAddNode;
            } else {
                left.addNode(toAddNode);
            }
        }
    }

    @Override
    public void removeNode() throws InvalidObjectException {
        if(left == null && right == null) {
            cleanNode();

            return;
        }

        if(left == null) {
            transformTo(getRight());
        } else if (right == null) {
            transformTo(getLeft());
        } else {
            // if right tree DOES NOT have left subtree
            // -> transform to right subtree but insert current left subtree
            if (getRight().getLeft() == null) {
                transformTo_remainLeft(getRight());
            } else {
                // if right subtree DOES have left subtree
                // -> set the least node of left subtree
                SimpleNode<T> deepestLeft = getRight().getLeft();
                SimpleNode<T> deepestLeftAncestor = getRight();

                while (deepestLeft.getLeft() != null) {
                    SimpleNode<T> temp = deepestLeft;
                    deepestLeft = deepestLeft.getLeft();
                    deepestLeftAncestor = temp;
                }

                // set node to the current tree and remove it from deepestLeft subtree
                this.value = deepestLeft.getValue();
                this.key = deepestLeft.getKey();
                deepestLeft.removeNode();

                // if deepest is replaced with its right subtree this part would be skipped
                if(deepestLeftAncestor.left.isEmpty()) {

                    deepestLeftAncestor.setLeft(null);
                }

                toString();
            }
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
    }

    @Override
    public void setLeft(Node<T, T> left) throws InvalidObjectException {
        if(left == null) {
            this.left = null;
            return;
        }

        ensureCorrectInstance(left);
        this.left = (SimpleNode<T>) left;
    }

    @Override
    public int compareTo(Node<T, T> o) {
        return Comparator.comparing(SimpleNode<T>::getKey)
                .compare(this, (SimpleNode<T>) o);
    }
}
