package dev.haguel.node;

import dev.haguel.tree.TreeImpl;
import lombok.*;

import java.io.InvalidObjectException;
import java.util.Comparator;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = {"parent"})
public class ColoredSimpleNode<T extends Comparable<T>> implements Node<T, T> {
    private T key;
    private T value;
    private boolean isBlack;
    private ColoredSimpleNode<T> right, left;
    private ColoredSimpleNode<T> parent;

    public ColoredSimpleNode(T value) {
        this.value = value;
        this.key = value;
        this.right = null;
        this.left = null;
        this.isBlack = true;
    }

    public ColoredSimpleNode(T value, ColoredSimpleNode<T> parent, boolean isBlack) {
        this.value = value;
        this.key = value;
        this.right = null;
        this.left = null;
        this.isBlack = isBlack;
        this.parent = parent;
    }

    private void cleanNode() {
        right = null;
        left = null;
        key = null;
        value = null;
    }

    private void transformTo(ColoredSimpleNode<T> node) {
        this.right = node.getRight();
        this.left = node.getLeft();
        node.key = null;
        node.value = null;

        node.cleanNode();
    }

    private void transformTo_remainLeft(ColoredSimpleNode<T> node) {
        this.right = node.getRight();
        this.value = node.getValue();
        this.key = node.getKey();

        node.cleanNode();
    }

    private boolean isEmpty() {
        return key == null && value == null && right == null && left == null;
    }

    private <V> void ensureCorrectInstance(Node<T, V> node) throws InvalidObjectException {
        if (!(node instanceof ColoredSimpleNode)) {
            throw new InvalidObjectException("Can't make action with current tree because it's not instance of BinaryTree");
        }
    }

    public void checkBalance() throws InvalidObjectException {
        if(parent == null) {
            return;
        }

        ColoredSimpleNode<T> grandParent = parent.getParent();

        if(grandParent == null) {
            return;
        }

        // if not balanced
        if (!this.isBlack && !parent.isBlack) {
            rebalance();
        }
    }

    public void rebalance() throws InvalidObjectException {
        ColoredSimpleNode<T> grandParent = parent.getParent();

        if(grandParent.getLeft() == parent) {
            ColoredSimpleNode<T> uncle = grandParent.getRight();

            if(uncle == null || uncle.isBlack) {
                if(parent.getRight() == this) {
                    makeLRRotation();
                } else {
                    makeLLRotation();
                }
            } else {
                recolorRedUncleCase(uncle);
            }
        } else {
            ColoredSimpleNode<T> uncle = grandParent.getLeft();

            if(uncle == null || uncle.isBlack) {
                if(parent.getLeft() == this) {
                    makeRLRotation();
                } else {
                    makeRRRotation();
                }
            } else {
                recolorRedUncleCase(uncle);
            }
        }
    }

    private void makeLRRotation() throws InvalidObjectException {
        ColoredSimpleNode<T> grandParent = parent.getParent();
        ColoredSimpleNode<T> grandGrandParent = grandParent.getParent();
        ColoredSimpleNode<T> parentLink = parent;

        parentLink.setRight(this.getLeft());
        grandParent.setLeft(this.getRight());
        this.setLeft(parentLink);
        this.setRight(grandParent);

        // After this node replaced grandparent it needs to set grandparent's parent as parent
        fixRelations(this, grandParent, grandGrandParent);

        this.getRight().setRed();
        this.setBlack();
    }

    private void makeRLRotation() throws InvalidObjectException {
        ColoredSimpleNode<T> grandParent = parent.getParent();
        ColoredSimpleNode<T> grandGrandParent = grandParent.getParent();
        ColoredSimpleNode<T> parentLink = parent;

        parentLink.setLeft(this.getRight());
        grandParent.setRight(this.getLeft());
        this.setRight(parentLink);
        this.setLeft(grandParent);

        // After this node replaced grandparent it needs to set grandparent's parent as parent
        fixRelations(this, grandParent, grandGrandParent);

        this.getLeft().setRed();
        this.setBlack();
    }

    private void makeLLRotation() throws InvalidObjectException {
        ColoredSimpleNode<T> grandParent = parent.getParent();
        ColoredSimpleNode<T> grandGrandParent = grandParent.getParent();
        ColoredSimpleNode<T> parentLink = parent;

        grandParent.setLeft(parentLink.getRight());
        parentLink.setRight(grandParent);
        fixRelations(parentLink, grandParent, grandGrandParent);

        ColoredSimpleNode<T> newUncle = parentLink.getRight();
        newUncle.setRed();
        if(newUncle.getLeft() != null) newUncle.getLeft().setBlack();

        parent.setBlack();
    }

    private void makeRRRotation() throws InvalidObjectException {
        ColoredSimpleNode<T> grandParent = parent.getParent();
        ColoredSimpleNode<T> grandGrandParent = grandParent.getParent();
        ColoredSimpleNode<T> parentLink = parent;

        grandParent.setRight(parentLink.getLeft());
        parentLink.setLeft(grandParent);
        fixRelations(parentLink, grandParent, grandGrandParent);

        ColoredSimpleNode<T> newUncle = parentLink.getLeft();
        newUncle.setRed();
        if(newUncle.getRight() != null) newUncle.getRight().setBlack();

        parent.setBlack();
    }

    private void recolorRedUncleCase(ColoredSimpleNode<T> uncle) throws InvalidObjectException {
        ColoredSimpleNode<T> grandParent = parent.getParent();
        ColoredSimpleNode<T> grandGrandParent = grandParent.getParent();

        parent.setBlack();
        uncle.setBlack();
        this.setRed();

        if(grandGrandParent == null) {
            grandParent.setBlack();
        } else {
            grandParent.setRed();

            if(!grandGrandParent.isBlack) {
                // if grandGrandParent is root -> recolor grandParent to black
                if(grandGrandParent.getRoot() == null) grandParent.setBlack();
                    // else -> check balance
                else grandParent.checkBalance();
            }
        }
    }

    private void fixRelations(ColoredSimpleNode<T> newGrandParent, ColoredSimpleNode<T> oldGrandParent, ColoredSimpleNode<T> grandGrandParent) throws InvalidObjectException {
        if(grandGrandParent == null) {
            newGrandParent.setParent(null);
        } else {
            if(grandGrandParent.getLeft() == oldGrandParent) {
                grandGrandParent.setLeft(newGrandParent);
            } else {
                grandGrandParent.setRight(newGrandParent);
            }
        }
    }

    @Override
    public void addNode(Node<T, T> toAdd) throws InvalidObjectException {
        ColoredSimpleNode<T> toAddNode = new ColoredSimpleNode<>(toAdd.getValue(), this, false);
        int compare = toAdd.compareTo((Node<T, T>) this);

        if(compare > 0) {
            if (right == null) {
                right = toAddNode;

                // if parent is red and right child is red -> rebalance
                if(!isBlack && !right.isBlack) {
                    right.checkBalance();
                }
            } else {
                right.addNode(toAddNode);
            }
        } else {
            if (left == null) {
                left = toAddNode;

                // if parent is red and left child is red -> rebalance
                if(!isBlack && !left.isBlack) {
                    left.checkBalance();
                }
            } else {
                left.addNode(toAddNode);
            }
        }
    }

    @Override
    public void removeNode() throws InvalidObjectException {

    }

    @Override
    public TreeImpl<T, T> getTree() {
        ColoredSimpleNode copy = new ColoredSimpleNode(this.getKey());
        try {
            copy.setLeft(this.getLeft());
            copy.setRight(this.getRight());
            copy.setParent(null);
        } catch (InvalidObjectException e) {
            e.printStackTrace();
        }

        return new TreeImpl<>(copy);
    }

    public ColoredSimpleNode<T> getRoot() {
        if(parent == null) {
            return this;
        } else {
            return parent.getRoot();
        }
    }

    @Override
    public void setRight(Node<T, T> right) throws InvalidObjectException {
        if(right == null) {
            this.right = null;
            return;
        }

        ensureCorrectInstance(right);
        this.right = (ColoredSimpleNode<T>) right;
        ((ColoredSimpleNode<T>) right).setParent(this);
    }

    @Override
    public void setLeft(Node<T, T> left) throws InvalidObjectException {
        if(left == null) {
            this.left = null;
            return;
        }

        ensureCorrectInstance(left);
        this.left = (ColoredSimpleNode<T>) left;
        ((ColoredSimpleNode<T>) left).setParent(this);
    }

    @Override
    public int compareTo(Node<T, T> o) {
        return Comparator.comparing(ColoredSimpleNode<T>::getKey)
                .compare(this, (ColoredSimpleNode<T>) o);
    }

    public void setRed() {
        this.isBlack = false;
    }

    public void setBlack() {
        this.isBlack = true;
    }
}
