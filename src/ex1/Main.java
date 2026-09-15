package ex1;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Integer capacity = readInteger(scanner, "Enter the vertex capacity (1-20): ", 1, Graph.MAX_VERTICES);
            if (capacity == null) {
                return;
            }
            Graph graph = new Graph(capacity);
            while (true) {
                Integer option = readInteger(scanner,
                        "Choose an option:\n1 - Add edge\n2 - Print graph\n3 - Exit\n> ", 1, 3);
                if (option == null) {
                    return;
                }
                if (option == 3) {
                    System.out.println("Program finished.");
                    return;
                }
                if (option == 2) {
                    graph.print();
                } else if (!addEdges(scanner, graph)) {
                    return;
                }
            }
        }
    }

    private static boolean addEdges(Scanner scanner, Graph graph) {
        while (true) {
            Object source = readVertex(scanner, "Source: ");
            if (source == null) {
                return false;
            }
            Object destination = readVertex(scanner, "Destination: ");
            if (destination == null) {
                return false;
            }
            Integer weight = readInteger(scanner, "Weight: ", Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (weight == null) {
                return false;
            }
            try {
                graph.addEdge(weight, source, destination);
                System.out.println("Edge added.");
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
            Integer continueAdding = readInteger(scanner, "Add another edge? 1 - Yes | 0 - No: ", 0, 1);
            if (continueAdding == null) {
                return false;
            }
            if (continueAdding == 0) {
                return true;
            }
        }
    }

    private static Object readVertex(Scanner scanner, String prompt) {
        while (true) {
            String input = readLine(scanner, prompt);
            if (input == null) {
                return null;
            }
            if (input.isEmpty()) {
                System.out.println("Vertex names must not be blank.");
                continue;
            }
            if (input.matches("[+-]?[0-9]+")) {
                try {
                    int value = Integer.parseInt(input);
                    if (value >= 0) {
                        return value;
                    }
                } catch (NumberFormatException exception) {
                    // Report out-of-range numeric labels instead of treating them as names.
                }
                System.out.println("Numeric vertices must be integers between 0 and 2147483647.");
            } else {
                return input;
            }
        }
    }

    private static Integer readInteger(Scanner scanner, String prompt, int minimum, int maximum) {
        while (true) {
            String input = readLine(scanner, prompt);
            if (input == null) {
                return null;
            }
            try {
                int value = Integer.parseInt(input);
                if (value >= minimum && value <= maximum) {
                    return value;
                }
            } catch (NumberFormatException exception) {
                // Retry after invalid or out-of-range input.
            }
            System.out.println("Enter an integer between " + minimum + " and " + maximum + ".");
        }
    }

    private static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            System.out.println("\nInput ended. Program finished.");
            return null;
        }
        return scanner.nextLine().trim();
    }
}
