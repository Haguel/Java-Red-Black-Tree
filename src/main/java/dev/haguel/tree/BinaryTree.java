package dev.haguel.tree;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InvalidObjectException;

@NoArgsConstructor
@Data
public class BinaryTree<T, V> implements Tree<T, V> {
    private Node<T, V> node;
    private BinaryTree<T, V> right, left;

    public BinaryTree(Node<T, V> node) {
        this.node = node;
        this.right = null;
        this.left = null;
    }

    private void cleanTree(BinaryTree tree) {
        tree.right = null;
        tree.left = null;
        tree.node = null;
    }

    private void transformTo(BinaryTree tree) {
        this.right = tree.getRight();
        this.left = tree.getLeft();
        this.node = tree.getNode();

        cleanTree(tree);
    }

    private boolean isEmpty() {
        return node == null && right == null && left == null;
    }

    private void transformTo_remainLeft(BinaryTree tree) {
        this.right = tree.getRight();
        this.node = tree.getNode();

        cleanTree(tree);
    }

    @Override
    public boolean addNode(Node<T, V> toAdd) {
        int compare = toAdd.compareTo(node);

        if(compare > 0) {
            if (right == null) {
                right = new BinaryTree<>(toAdd);
            } else {
                return right.addNode(toAdd);
            }
        } else {
            if (left == null) {
                left = new BinaryTree<>(toAdd);
            } else {
                return left.addNode(toAdd);
            }
        }

        return true;
    }

    @Override
    public void removeNode() throws InvalidObjectException {
        if(left == null && right == null) {
            cleanTree(this);

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
                BinaryTree deepestLeft = getRight().getLeft();
                BinaryTree deepestLeftAncestor = getRight();

                while (deepestLeft.getLeft() != null) {
                    BinaryTree temp = deepestLeft;
                    deepestLeft = deepestLeft.getLeft();
                    deepestLeftAncestor = temp;
                }

                // set node to the current tree and remove it from deepestLeft subtree
                this.node = deepestLeft.getNode();
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
    public void setRight(Tree<T, V> right) throws InvalidObjectException {
        if(right == null) {
            this.right = null;
            return;
        }

        ensureCorrectInstance(right);
        this.right = (BinaryTree<T, V>) right;
    }

    @Override
    public void setLeft(Tree<T, V> left) throws InvalidObjectException {
        if(left == null) {
            this.left = null;
            return;
        }

        ensureCorrectInstance(left);
        this.left = (BinaryTree<T, V>) left;
    }



    public void ensureCorrectInstance(Tree tree) throws InvalidObjectException {
        if (!(tree instanceof BinaryTree)) {
            throw new InvalidObjectException("Can't make action with current tree because it's not instance of BinaryTree");
        }
    }
}
