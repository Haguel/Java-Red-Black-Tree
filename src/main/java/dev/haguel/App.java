package dev.haguel;

import dev.haguel.factory.NodeFactory;
import dev.haguel.service.TreePrinter;
import dev.haguel.tree.BinaryTree;

import java.io.InvalidObjectException;

public class App {
    public static void main(String[] args) {
        BinaryTree<Integer, Integer> tree = new BinaryTree<>(NodeFactory.generateIntNode(1, 100));

        // If you are using InteliJ Idea console, tree output might be crooked
        // because its console can't handle large tree.
        // Therefore, it's not recommend to set node count bigger than 8
        int nodeCount = 8;

        try {
            for(int i = 0; i < nodeCount; i++) {
                tree.addNode(NodeFactory.generateIntNode(1, 100));
                System.out.println();
                TreePrinter.print(tree);
            }

            System.out.println("Whole tree constructed!");

            for(int i = 0; i < nodeCount; i++) {
                tree.removeNode();
                System.out.println();
                TreePrinter.print(tree);
            }

            System.out.println("Whole tree removed!");
        } catch (InvalidObjectException exception) {
            exception.printStackTrace();
        }
    }
}
