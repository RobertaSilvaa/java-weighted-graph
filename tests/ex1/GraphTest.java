package ex1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/** Dependency-free regression tests for graph insertion and console input. */
public class GraphTest {
    public static void main(String[] args) throws Exception {
        testGraph();
        testValidation();
        testConsole();
        System.out.println("PASS: graph insertion, validation, and console regression tests.");
    }

    private static void testGraph() {
        Graph graph = new Graph(4);
        graph.addEdge(7, "A", "B");
        graph.addEdge(2, "A", "C");
        graph.addEdge(-3, "C", 0);
        require(graph.getVertexCount() == 4, "Reusing a source must not leave gaps.");
        graph.addEdge(0, "B", "A");
        graph.addEdge(Integer.MIN_VALUE, "B", "B");
        graph.addEdge(Integer.MAX_VALUE, "A", "B");
        require(graph.getEdgeCount() == 6, "Edges may outnumber vertices.");
        require(graph.getEdges().get(0).toString().equals("A --- 7 ---> B"), "Directed output.");
        require(graph.getEdges().get(2).getWeight() == -3, "Negative weight.");
        require(graph.getEdges().get(4).getWeight() == Integer.MIN_VALUE, "Minimum weight.");
        require(graph.getEdges().get(5).getWeight() == Integer.MAX_VALUE, "Maximum weight.");
        require(graph.getEdges().get(0).getSource() == graph.getEdges().get(1).getSource(), "Vertex reuse.");
        require(graph.getEdges().get(4).getSource() == graph.getEdges().get(4).getDestination(), "Self-loop reuse.");
        Graph independent = new Graph(1);
        independent.addEdge(1, "solo", "solo");
        require(independent.getVertexCount() == 1 && independent.getEdgeCount() == 1, "Independent graphs.");
        Graph mixed = new Graph(4);
        mixed.addEdge(1, 1, "1");
        mixed.addEdge(2, "nulo", "  100% complete  ");
        require(mixed.getVertexCount() == 4, "Named and numeric labels are distinct.");
        require(mixed.getEdges().get(1).toString().equals("nulo --- 2 ---> 100% complete"),
                "Names must not be treated as sentinels or format strings.");
        Graph full = new Graph(20);
        for (int index = 0; index < 20; index++) {
            full.addEdge(index, index, index);
        }
        require(full.getVertexCount() == 20, "Maximum capacity is usable.");
    }

    private static void testValidation() {
        expectInvalid(() -> new Graph(0));
        expectInvalid(() -> new Graph(21));
        Graph graph = new Graph(2);
        Object[] invalidLabels = {null, -1, "", "   ", 1.5, new Object()};
        for (Object label : invalidLabels) {
            expectInvalid(() -> graph.addEdge(1, "valid", label));
            expectInvalid(() -> graph.addEdge(1, label, "valid"));
            require(graph.getVertexCount() == 0 && graph.getEdgeCount() == 0, "Invalid endpoints must not mutate state.");
        }
        graph.addEdge(1, "A", "A");
        expectInvalid(() -> graph.addEdge(2, "B", "C"));
        require(graph.getVertexCount() == 1 && graph.getEdgeCount() == 1, "Capacity failure must not partially insert.");
        graph.addEdge(3, "B", "A");
        expectInvalid(() -> graph.addEdge(4, "A", "C"));
        graph.addEdge(5, "A", "B");
        require(graph.getVertexCount() == 2 && graph.getEdgeCount() == 3, "Existing vertices remain usable at capacity.");
        try {
            graph.getEdges().clear();
            throw new AssertionError("The edge view must be read-only.");
        } catch (UnsupportedOperationException expected) {
            // External callers cannot remove graph edges through the returned view.
        }
    }

    private static void testConsole() throws Exception {
        String normal = runConsole("3\n1\nA\nB\n7\n1\nA\nC\n-2\n0\n2\n3\n");
        require(normal.contains("A --- 7 ---> B") && normal.contains("A --- -2 ---> C"), "Add, continue, print, exit.");
        String invalid = runConsole("wrong\n0\n21\n2\n9\nwrong\n1\n\n-1\n999999999999999\nA\nB\nwrong\n2147483648\n7\n9\n0\n2\n3\n");
        require(invalid.contains("Vertex names must not be blank.")
                && invalid.contains("Numeric vertices must be integers")
                && invalid.contains("A --- 7 ---> B") && invalid.contains("Program finished."), "Invalid input recovery.");
        String full = runConsole("1\n1\nA\nB\n1\n1\nA\nA\n0\n0\n2\n3\n");
        require(full.contains("vertex capacity has been reached") && full.contains("A --- 0 ---> A"), "Capacity recovery.");
        require(runConsole("2\n1\n0\n2147483647\n-2147483648\n0\n2\n3\n")
                .contains("0 --- -2147483648 ---> 2147483647"), "Integer input limits.");
        require(runConsole("2\n1\n100% complete\nNew York\n1\n0\n2\n3\n")
                .contains("100% complete --- 1 ---> New York"), "Literal names with spaces and percent signs.");
        require(runConsole("1\n2\n3\n").contains("The graph has no edges."), "Empty graph output.");
        String[] prefixes = {"", "2\n", "2\n1\n", "2\n1\nA\n", "2\n1\nA\nB\n",
                "2\n1\nA\nB\n7\n", "2\n1\nA\nB\n7\n0\n"};
        for (String prefix : prefixes) {
            require(runConsole(prefix).contains("Input ended. Program finished."), "End-of-input at every prompt.");
        }
    }

    private static String runConsole(String input) throws Exception {
        InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (PrintStream capture = new PrintStream(output, true, "UTF-8")) {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(capture);
            Main.main(new String[0]);
            return output.toString("UTF-8");
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
        }
    }

    private static void expectInvalid(Runnable action) {
        try {
            action.run();
            throw new AssertionError("Expected invalid argument rejection.");
        } catch (IllegalArgumentException expected) {
            // Rejection is the expected outcome.
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
