# Java Weighted Graph

A Java console application for adding and printing directed edges with integer weights. Vertices are identified by names or nonnegative integers and are created when edges are added.

## Features

- Vertex capacity from 1 to 20, stored in a fixed-size array.
- Vertex reuse, self-loops, and parallel edges.
- Positive, zero, and negative integer weights.
- A growing edge list, allowing more edges than vertices.
- Validation before insertion: rejected edges leave the graph unchanged.
- Recovery from invalid input and clean exit at end-of-input.

## Requirements and Execution

A JDK is required; verified with JDK 21. No external libraries are needed.
Run from the project root (the local `exercicio1_aed2` folder):

```shell
javac -encoding UTF-8 -d bin src/ex1/*.java
java -cp bin ex1.Main
```

In VS Code, open this folder and run `ex1.Main`.

## Usage

1. Enter a vertex capacity from 1 to 20.
2. Choose **1 - Add edge** and enter the source, destination, and weight on separate lines.
3. Enter **1** to add another edge or **0** to return to the menu.
4. Choose **2 - Print graph** or **3 - Exit**.

An edge from `A` to `B` with weight `7` is printed as:

```text
A --- 7 ---> B
```

### Input Rules

- Names are case-sensitive, may contain spaces, and are trimmed. Blank names are rejected.
- Numeric vertex labels range from `0` to `2147483647`.
- Console input containing only an optional sign and digits is parsed as an integer. Other nonblank vertex input is a name.
- Weights accept the Java `int` range: `-2147483648` to `2147483647`.
- Invalid input prompts for a replacement. Exceeding vertex capacity rejects the entire edge.
- End-of-input exits cleanly, including midway through entering an edge.

## Project Structure

```text
java-weighted-graph/
├── .vscode/
│   └── settings.json
├── src/
│   └── ex1/
│       ├── Edge.java
│       ├── Graph.java
│       ├── Main.java
│       └── Vertex.java
├── tests/
│   └── ex1/
│       └── GraphTest.java
├── .gitignore
└── README.md
```

| File | Responsibility |
| --- | --- |
| `Main.java` | Console menu, input validation, and messages |
| `Graph.java` | Vertex capacity, vertex reuse, and edge insertion |
| `Vertex.java` | Immutable named or numeric label |
| `Edge.java` | Immutable endpoints, weight, and output representation |

`bin/` contains generated files and is ignored by Git. The optional `lib/` folder is empty. The original package name `ex1` is retained.

## Tests

```shell
javac -encoding UTF-8 -d bin src/ex1/*.java tests/ex1/GraphTest.java
java -cp bin ex1.GraphTest
```

Regression checks cover independent graphs, vertex reuse, mixed labels, self-loops, parallel edges, capacity and endpoint validation, integer limits, and console recovery from invalid or exhausted input.

## Scope

This exercise stores and prints graphs. It does not implement traversal, shortest paths, persistence, or deletion. In the Java API, named and numeric labels are distinct (for example, the string `"1"` and the integer `1`); the console interprets integer-looking input as numeric.
