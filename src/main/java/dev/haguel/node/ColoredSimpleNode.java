package dev.haguel.node;

import dev.haguel.tree.TreeImpl;
import lombok.*;

import java.io.InvalidObjectException;
import java.util.Comparator;

@AllArgsConstructor
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

    private void cleanNode() throws InvalidObjectException {
        right = null;
        left = null;
        key = null;
        value = null;

        if(parent != null) {
            if(parent.getLeft() == this) {
                parent.setLeft(null);
            } else {
                parent.setRight(null);
            }
        }
    }

    private <V> void ensureCorrectInstance(Node<T, V> node) throws InvalidObjectException {
        if (!(node instanceof ColoredSimpleNode)) {
            throw new InvalidObjectException("Can't make action with current tree because it's not instance of ColoredSimpleNode");
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

    private ColoredSimpleNode<T> getDeepestLeft() {
        if(left == null) {
            return this;
        } else {
            return left.getDeepestLeft();
        }
    }

    private ColoredSimpleNode<T> getDeepestRight() {
        if(right == null) {
            return this;
        } else {
            return right.getDeepestRight();
        }
    }

    private ColoredSimpleNode<T> getSibling() {
        if(parent == null) {
            return null;
        }

        if(parent.getLeft() == this) {
            return parent.getRight();
        } else {
            return parent.getLeft();
        }
    }

    private void setRed() {
        this.isBlack = false;
    }

    private void setBlack() {
        this.isBlack = true;
    }

    private void updateRelations(ColoredSimpleNode<T> newNode, ColoredSimpleNode<T> oldNode, ColoredSimpleNode<T> oldNodeParent) throws InvalidObjectException {
        if(oldNodeParent == null) {
            newNode.setParent(null);
        } else {
            if(oldNodeParent.getLeft() == oldNode) {
                oldNodeParent.setLeft(newNode);
            } else {
                oldNodeParent.setRight(newNode);
            }
        }
    }

    private void makeRotationToRight() throws InvalidObjectException {
        ColoredSimpleNode<T> parentLink = parent;
        ColoredSimpleNode<T> grandParent = parent.getParent();

        parentLink.setLeft(this.getRight());
        this.setRight(parentLink);

        updateRelations(this, parentLink, grandParent);
    }

    private void makeRotationToLeft() throws InvalidObjectException {
        ColoredSimpleNode<T> parentLink = parent;
        ColoredSimpleNode<T> grandParent = parent.getParent();

        parentLink.setRight(this.getLeft());
        this.setLeft(parentLink);

        updateRelations(this, parentLink, grandParent);
    }

    private void rotateRedSibling() throws InvalidObjectException {
        ColoredSimpleNode<T> grandParent = parent.getParent();

        // if sibling is right child of parent
        if(parent.getLeft() == this) {
            ColoredSimpleNode<T> sibling = parent.getRight();
            ColoredSimpleNode<T> siblingLeftChild = sibling.getLeft();
            ColoredSimpleNode<T> siblingRightChild;

            parent.setRight(siblingLeftChild);
            sibling.setLeft(parent);

            updateRelations(sibling, parent, grandParent);

            parent.setRed();
            sibling.setBlack();

            // after rotation parent becomes red node with right black node and black sibling -> need rotation
            // previous sibling is now sibling of parent.
            sibling = parent.getRight();

            if(sibling.hasBothChildren() || sibling.hasOnlyLeftChild()) {
                siblingLeftChild = sibling.getLeft();
                siblingLeftChild.makeRLRotation();

                siblingLeftChild.setRed();
                siblingLeftChild.getRight().setBlack();
                siblingLeftChild.getLeft().setBlack();
            } else if (sibling.hasOnlyRightChild()) {
                siblingRightChild = sibling.getRight();
                siblingRightChild.makeRRRotationRecolor(siblingRightChild.makeRRRotation());
            } else {
                sibling.makeRotationToLeft();
            }
        } else {
            ColoredSimpleNode<T> sibling = parent.getLeft();
            ColoredSimpleNode<T> siblingRightChild = sibling.getRight();
            ColoredSimpleNode<T> siblingLeftChild;

            parent.setLeft(siblingRightChild);
            sibling.setRight(parent);

            updateRelations(sibling, parent, grandParent);

            parent.setRed();
            sibling.setBlack();

            // after rotation parent becomes red node with right black node and black sibling -> need rotation
            // previous sibling is now sibling of parent.
            sibling = parent.getLeft();

            if(sibling.hasBothChildren() || sibling.hasOnlyRightChild()) {
                siblingRightChild = sibling.getRight();
                siblingRightChild.makeLRRotation();

                siblingRightChild.setRed();
                siblingRightChild.getRight().setBlack();
                siblingRightChild.getLeft().setBlack();
            } else if (sibling.hasOnlyLeftChild()) {
                siblingLeftChild = sibling.getLeft();
                siblingLeftChild.makeLLRotationRecolor(siblingLeftChild.makeLLRotation());
            } else {
                sibling.makeRotationToRight();
            }
        }
    }

    private void rotateBlackSibling() throws InvalidObjectException {
        ColoredSimpleNode<T> grandParent = parent.getParent();
        ColoredSimpleNode<T> parentLink = parent;
        ColoredSimpleNode<T> sibling = getSibling();
        ColoredSimpleNode<T> siblingLeftChild = sibling.getLeft();
        ColoredSimpleNode<T> siblingRightChild = sibling.getRight();

        // if both children of sibling are black (null children are considered as black)
        if((sibling.hasBothChildren() && siblingLeftChild.isBlack && siblingRightChild.isBlack) || sibling.hasNoChildren()) {
            sibling.setRed();

            if(!parent.isBlack) {
                parent.setBlack();
            } else {
                parent.rebalanceAfterRemoval();
            }
        }

        // if sibling is right child of parent
        if(parent.getRight() == sibling) {
            // if left child of sibling is red and the right one is black
            if (sibling.hasBothChildren() && !siblingLeftChild.isBlack && siblingRightChild.isBlack) {
                siblingLeftChild.makeRotationToRight();

                siblingLeftChild.setBlack();
                sibling.setRed();

                // rotate again but now sibling must be with right red child (see next else if case)
                rotateBlackSibling();
            }
            // if right child of sibling is red
            else if (siblingRightChild != null && !siblingRightChild.isBlack) {
                parent.setRight(siblingLeftChild);
                sibling.setLeft(parent);

                updateRelations(sibling, parent, grandParent);

                siblingRightChild.setBlack();
                sibling.isBlack = parentLink.isBlack;
                parentLink.setBlack();
            }
            // if left child of sibling is red
            else if (siblingLeftChild != null && !siblingLeftChild.isBlack) {
                siblingLeftChild.makeRLRotation();

                siblingLeftChild.isBlack = parentLink.isBlack;
                parentLink.setBlack();
            }
        // if sibling is left child of parent
        } else {
            // if right child of sibling is red and the left one is black
            if (sibling.hasBothChildren() && !siblingRightChild.isBlack && siblingLeftChild.isBlack) {
                siblingRightChild.makeRotationToLeft();

                siblingRightChild.setBlack();
                sibling.setRed();

                // rotate again but now sibling must be with right red child (see next else if case)
                rotateBlackSibling();
            }
            // if left child of sibling is red
            else if (siblingLeftChild != null && !siblingLeftChild.isBlack) {
                parent.setLeft(siblingRightChild);
                sibling.setRight(parent);

                updateRelations(sibling, parent, grandParent);

                siblingLeftChild.setBlack();
                sibling.isBlack = parentLink.isBlack;
                parentLink.setBlack();
            }
            // if right child of sibling is red
            else if (siblingRightChild != null && !siblingRightChild.isBlack) {
                siblingRightChild.makeLRRotation();

                siblingRightChild.isBlack = parentLink.isBlack;
                parentLink.setBlack();
            }
        }
    }

    private void rebalanceAfterRemoval() throws InvalidObjectException {
        if(this.getParent() == null) return;

        ColoredSimpleNode<T> sibling;
        // define sibling
        if(parent.getLeft() == this) {
            sibling = parent.getRight();
        } else {
            sibling = parent.getLeft();
        }

        // rotate sibling
        if(sibling.isBlack) {
            rotateBlackSibling();
        } else {
            rotateRedSibling();
        }
    }

    private void exchangeWith(ColoredSimpleNode<T> node) {
        T tempKey = key;
        T tempValue = value;

        key = node.getKey();
        value = node.getValue();

        node.setKey(tempKey);
        node.setValue(tempValue);
    }

    // returns parent of removed node
    private ColoredSimpleNode<T> removeThis() throws InvalidObjectException {
        ColoredSimpleNode<T> parentLink = parent;

        if(this.hasBothChildren()) {
            ColoredSimpleNode<T> minRight = right.getDeepestLeft();
            ColoredSimpleNode<T> maxLeft = left.getDeepestRight();
            ColoredSimpleNode<T> exchangeNode;

            if(minRight != null && !minRight.isBlack) {
                exchangeNode = minRight;
            } else if (maxLeft != null && !maxLeft.isBlack) {
                exchangeNode = maxLeft;
            } else if (minRight != null) {
                exchangeNode = minRight;
            } else if (maxLeft != null) {
                exchangeNode = maxLeft;
            } else {
                exchangeNode = right;
            }

            exchangeWith(exchangeNode);
            exchangeNode.removeThis();

            return this;
        }

        if(!this.isBlack) {
            // if node is red and has no children
            if(right == null && left == null) {
                cleanNode();

                return parentLink;
            }
            // red node with 1 child is impossible
        } else {
            // if node is black and has only right child
            if(right != null && left == null) {
                exchangeWith(right);
                right.removeThis();

                return this;
            }
            // if node is black and has only left child
            else if(left != null && right == null) {
                exchangeWith(left);
                left.removeThis();

                return this;
            }
            // if node is black and has no children
            else {
                rebalanceAfterRemoval();
                parentLink = parent;
                cleanNode();

                return parentLink;
            }
        }

        return parentLink;
    }

    public boolean isBalanced() {
        if(parent == null) {
            return true;
        }

        ColoredSimpleNode<T> grandParent = parent.getParent();

        if(grandParent == null) {
            return true;
        }

        // if not balanced
        if (!this.isBlack && !parent.isBlack) {
            return false;
        }

        return true;
    }

    private void rebalanceAfterAddition() throws InvalidObjectException {
        ColoredSimpleNode<T> grandParent = parent.getParent();

        if(grandParent.getLeft() == parent) {
            ColoredSimpleNode<T> uncle = grandParent.getRight();

            if(uncle == null || uncle.isBlack) {
                if(parent.getRight() == this) {
                    makeLRRotation();
                    makeLRRotationRecolor();
                } else {
                    makeLLRotationRecolor(makeLLRotation());
                }
            } else {
                recolorRedUncleCase(uncle);
            }
        } else {
            ColoredSimpleNode<T> uncle = grandParent.getLeft();

            if(uncle == null || uncle.isBlack) {
                if(parent.getLeft() == this) {
                    makeRLRotation();
                    makeRLRotationRecolor();
                } else {
                    makeRRRotationRecolor(makeRRRotation());
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
        updateRelations(this, grandParent, grandGrandParent);
    }

    private void makeLRRotationRecolor() {
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
        updateRelations(this, grandParent, grandGrandParent);
    }

    private void makeRLRotationRecolor() {
        this.getLeft().setRed();
        this.setBlack();
    }

    private ColoredSimpleNode<T> makeLLRotation() throws InvalidObjectException {
        ColoredSimpleNode<T> grandParent = parent.getParent();
        ColoredSimpleNode<T> grandGrandParent = grandParent.getParent();
        ColoredSimpleNode<T> parentLink = parent;

        grandParent.setLeft(parentLink.getRight());
        parentLink.setRight(grandParent);

        updateRelations(parentLink, grandParent, grandGrandParent);

        ColoredSimpleNode<T> newUncle = parentLink.getRight();

        return newUncle;
    }

    private void makeLLRotationRecolor(ColoredSimpleNode<T> newUncle) {
        newUncle.setRed();
        if(newUncle.getLeft() != null) newUncle.getLeft().setBlack();

        parent.setBlack();
    }

    private ColoredSimpleNode<T> makeRRRotation() throws InvalidObjectException {
        ColoredSimpleNode<T> grandParent = parent.getParent();
        ColoredSimpleNode<T> grandGrandParent = grandParent.getParent();
        ColoredSimpleNode<T> parentLink = parent;

        grandParent.setRight(parentLink.getLeft());
        parentLink.setLeft(grandParent);

        updateRelations(parentLink, grandParent, grandGrandParent);

        ColoredSimpleNode<T> newUncle = parentLink.getLeft();

        return newUncle;
    }

    private void makeRRRotationRecolor(ColoredSimpleNode<T> newUncle) {
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
                else {
                    if(!grandParent.isBalanced()) {
                        grandParent.rebalanceAfterAddition();
                    }
                }
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
                    if(!right.isBalanced()) {
                        right.rebalanceAfterAddition();
                    }
                }
            } else {
                right.addNode(toAddNode);
            }
        } else {
            if (left == null) {
                left = toAddNode;

                // if parent is red and left child is red -> rebalance
                if(!isBlack && !left.isBlack) {
                    if(!left.isBalanced()) {
                        left.rebalanceAfterAddition();
                    }
                }
            } else {
                left.addNode(toAddNode);
            }
        }
    }

    @Override
    public ColoredSimpleNode<T> removeNode() throws InvalidObjectException {
        ColoredSimpleNode<T> parentLink = removeThis();

        if(parentLink == null) {
            return null;
        }

        return parentLink.getRoot();
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

    @Override
    public Node<T, T> findNodeByKey(T key) {
        if (this.getKey().equals(key)) {
            return this;
        } else if (key.compareTo(this.getKey()) < 0 && getLeft() != null) {
            return getLeft().findNodeByKey(key);
        } else if (key.compareTo(this.getKey()) > 0 && getRight() != null) {
            return getRight().findNodeByKey(key);
        }

        return null;
    }

    @Override
    public Node<T, T> findNodeByValue(T value) {
        if (this.getValue().equals(value)) {
            return this;
        } else {
            Node<T, T> foundNode = null;
            if (getLeft() != null) {
                foundNode = getLeft().findNodeByValue(value);
            }
            if (foundNode == null && getRight() != null) {
                foundNode = getRight().findNodeByValue(value);
            }

            return foundNode;
        }
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
    public void setParent(Node<T, T> parent) throws InvalidObjectException {
        if(parent == null) {
            this.parent = null;

            return;
        }

        ensureCorrectInstance(parent);
        this.parent = (ColoredSimpleNode<T>) parent;
    }

    @Override
    public int compareTo(Node<T, T> o) {
        return Comparator.comparing(ColoredSimpleNode<T>::getKey)
                .compare(this, (ColoredSimpleNode<T>) o);
    }
}
