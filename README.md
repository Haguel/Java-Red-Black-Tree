# Red-Black Tree Project

This project implements binary & red-black trees with nodes that can be added and removed. The nodes are implementations of the `Node` class, and the tree is printed using the `BinaryNodePrinter` or `ColoredNodePrinter` class.

## Technologies and Libraries

- **Lombok**: A Java library that helps to reduce boilerplate code. [Lombok](https://projectlombok.org/)
- **Maven**: A build automation tool used for managing project dependencies. [Maven](https://maven.apache.org/)

## Project Structure

```plaintext
src/
├── main/
│   ├── java/
│   │   └── dev/
│   │       └── haguel/
│   │           ├── node/
│   │           ├── service/
│   │           └── starter/
|   |           └── tree/
│   └── resources/
└── test/
```

- `src/main/java/dev/haguel/node/`: Contains the `SimpleNode` and `ColoredSimpleNode` classes.
- `src/main/java/dev/haguel/service/`: Contains utility classes.
- `src/main/java/dev/haguel/starter/`: Contains the main starter classes like `RedBlackTreeStarter`.
- `src/main/java/dev/haguel/tree/`: Contains the tree interface and implementation.

## Classes and Methods

### Node - Interface

- `addNode(Node<T, T> toAdd)`: Adds a node to the binary tree.
- `removeNode()`: Removes a node from the binary tree.
- `getTree()`: Returns a copy of the tree.
- `setRight(Node<T, T> right)`: Sets the right child of the node.
- `setLeft(Node<T, T> left)`: Sets the left child of the node.
- `setParent(Node<T, T> parent)`: Sets the parent of the node.
- `getKey()`: Returns the key of the node.
- `getValue()`: Returns the value of the node.
- `getRight()`: Returns the right child of the node.
- `getLeft()`: Returns the left child of the node.
- `findNode()`: Finds and returns the current node.
- `findNodeByKey(T key)`: Finds and returns a node with the specified key.
- `findNodeByValue(V value)`: Finds and returns a node with the specified value.

### SimpleNode - Represents a node in the binary tree

- Implements Node interface

### ColoredSimpleNode - Represents a node in the red-black tree

- Implements Node interface

### BinaryNodePrinter

- `print(SimpleNode<T> node)`: Prints the binary tree.

### ColoredNodePrinter

- `print(ColoredSimpleNode<T> node)`: Prints the red-black tree tree.