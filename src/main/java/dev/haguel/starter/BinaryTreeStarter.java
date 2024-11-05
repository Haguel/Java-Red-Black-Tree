package dev.haguel.starter;

import dev.haguel.service.Generator;
import dev.haguel.node.SimpleNode;
import dev.haguel.service.BinaryNodePrinter;

import java.io.InvalidObjectException;

public class BinaryTreeStarter {
    public static void main(String[] args) {
        // Count of nodes in the tree without root value.
        // If you are using InteliJ Idea console, tree output might be crooked
        // because its console can't handle large tree.
        // Therefore, it's not recommend to set node count bigger than 8
        int nodeCount = 8;

        try {
            int toAdd = Generator.generateRandomInt(1, 100);
            System.out.println("Node to add: " + toAdd);

            SimpleNode<Integer> binaryNode = new SimpleNode<>(toAdd);
            BinaryNodePrinter.print(binaryNode);

            for(int i = 0; i < nodeCount; i++) {
                toAdd = Generator.generateRandomInt(1, 100);
                System.out.println("Node to add: " + toAdd);

                binaryNode.addNode(new SimpleNode<>(toAdd));
                BinaryNodePrinter.print(binaryNode);
            }

            System.out.println("Whole tree constructed!");

            for(int i = 0; i < nodeCount; i++) {
                binaryNode.removeNode();
                BinaryNodePrinter.print(binaryNode);
                System.out.println();
            }

            System.out.println("Whole tree removed!");
        } catch (InvalidObjectException exception) {
            exception.printStackTrace();
        }
    }
}
