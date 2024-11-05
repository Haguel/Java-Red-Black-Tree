package dev.haguel.starter;

import dev.haguel.service.Generator;
import dev.haguel.node.ColoredSimpleNode;
import dev.haguel.service.ColoredNodePrinter;

import java.io.InvalidObjectException;

public class RedBlackTreeStarter {
    public static void main(String[] args) {
        // Count of nodes in the tree without root value.
        // If you are using InteliJ Idea console, tree output might be crooked
        // because its console can't handle large tree.
        // Therefore, it's not recommend to set node count bigger than 9
        int nodeCount = 9;

        try {
            int toAdd = Generator.generateRandomInt(1, 100);
            System.out.println("Node to add: " + toAdd);

            ColoredSimpleNode<Integer> coloredNode = new ColoredSimpleNode<>(toAdd);
            ColoredNodePrinter.print(coloredNode);

            for(int i = 0; i < nodeCount; i++) {
                toAdd = Generator.generateRandomInt(1, 100);
                System.out.println("Node to add: " + toAdd);

                coloredNode.addNode(new ColoredSimpleNode<>(toAdd));
                ColoredNodePrinter.print(coloredNode);
            }

            System.out.println("Whole tree constructed!");
        } catch (InvalidObjectException exception) {
            exception.printStackTrace();
        }
    }
}
