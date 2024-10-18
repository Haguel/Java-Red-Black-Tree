package dev.haguel.factory;

import dev.haguel.tree.BinaryTreeSimpleNode;
import dev.haguel.tree.SimpleNode;

import java.util.Random;

public class NodeFactory {
    public static final Random random;

    static {
        random = new Random();
    }

    public static BinaryTreeSimpleNode generateBinaryTreeSimpleNode(int min, int max) {
        int number = random.nextInt(max + 1 - min) + min;

        return new BinaryTreeSimpleNode(number);
    }
}
