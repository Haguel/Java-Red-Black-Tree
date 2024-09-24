package dev.haguel.factory;

import dev.haguel.tree.IntNode;

import java.util.Random;

public class NodeFactory {
    public static final Random random;

    static {
        random = new Random();
    }

    public static IntNode generateIntNode(int min, int max) {
        int number = random.nextInt(max + 1 - min) + min;

        return new IntNode(number, number);
    }
}
