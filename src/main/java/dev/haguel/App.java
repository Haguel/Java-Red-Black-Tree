package dev.haguel;

import dev.haguel.factory.NodeFactory;
import dev.haguel.service.TreePrinter;
import dev.haguel.tree.BinaryTree;
import dev.haguel.tree.BinaryTreeSimpleNode;

import java.io.InvalidObjectException;

public class App {
    public static void main(String[] args) {
        BinaryTreeSimpleNode<Integer> node = NodeFactory.generateBinaryTreeSimpleNode(1, 100);

        // If you are using InteliJ Idea console, tree output might be crooked
        // because its console can't handle large tree.
        // Therefore, it's not recommend to set node count bigger than 8
        int nodeCount = 8;

        try {
            for(int i = 0; i < nodeCount; i++) {
                node.addNode(NodeFactory.generateBinaryTreeSimpleNode(1, 100));
                System.out.println();
                TreePrinter.print(node);
            }

            System.out.println("Whole tree constructed!");

            for(int i = 0; i < nodeCount; i++) {
                node.removeNode();
                System.out.println();
                TreePrinter.print(node);
            }

            System.out.println("Whole tree removed!");
        } catch (InvalidObjectException exception) {
            exception.printStackTrace();
        }
    }
}
