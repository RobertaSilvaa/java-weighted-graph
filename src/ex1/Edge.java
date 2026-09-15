package ex1;

import java.util.Objects;

/** A directed connection with an integer weight. */
public final class Edge {
    private final Vertex source;
    private final Vertex destination;
    private final int weight;

    public Edge(Vertex source, Vertex destination, int weight) {
        this.source = Objects.requireNonNull(source, "Source must not be null.");
        this.destination = Objects.requireNonNull(destination, "Destination must not be null.");
        this.weight = weight;
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getDestination() {
        return destination;
    }

    public int getWeight() {
        return weight;
    }

    public void print() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return source + " --- " + weight + " ---> " + destination;
    }
}
