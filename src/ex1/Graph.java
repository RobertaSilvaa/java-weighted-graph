package ex1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** A directed weighted graph with a fixed vertex capacity. */
public final class Graph {
    public static final int MAX_VERTICES = 20;

    private final Vertex[] vertices;
    private final List<Edge> edges = new ArrayList<>();
    private int vertexCount;

    public Graph(int vertexCapacity) {
        if (vertexCapacity < 1 || vertexCapacity > MAX_VERTICES) {
            throw new IllegalArgumentException("Vertex capacity must be between 1 and 20.");
        }
        vertices = new Vertex[vertexCapacity];
    }

    public void addEdge(int weight, Object source, Object destination) {
        Vertex sourceCandidate = createVertex(source);
        Vertex destinationCandidate = createVertex(destination);
        Vertex sourceVertex = findVertex(sourceCandidate.getLabel());
        Vertex destinationVertex = findVertex(destinationCandidate.getLabel());
        boolean sameVertex = sourceCandidate.getLabel().equals(destinationCandidate.getLabel());
        int requiredVertices = sourceVertex == null ? 1 : 0;
        if (destinationVertex == null && !sameVertex) {
            requiredVertices++;
        }
        // Validate both endpoints and capacity before changing the graph.
        if (vertexCount + requiredVertices > vertices.length) {
            throw new IllegalArgumentException("Cannot add the edge: vertex capacity has been reached.");
        }
        if (sourceVertex == null) {
            sourceVertex = sourceCandidate;
            vertices[vertexCount++] = sourceVertex;
        }
        if (destinationVertex == null) {
            if (sameVertex) {
                destinationVertex = sourceVertex;
            } else {
                destinationVertex = destinationCandidate;
                vertices[vertexCount++] = destinationVertex;
            }
        }
        edges.add(new Edge(sourceVertex, destinationVertex, weight));
    }

    private Vertex createVertex(Object label) {
        if (label instanceof String) {
            return new Vertex((String) label);
        }
        if (label instanceof Integer) {
            return new Vertex((Integer) label);
        }
        throw new IllegalArgumentException("Vertices must be names or nonnegative integers.");
    }

    private Vertex findVertex(Object label) {
        for (int index = 0; index < vertexCount; index++) {
            if (vertices[index].getLabel().equals(label)) {
                return vertices[index];
            }
        }
        return null;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getEdgeCount() {
        return edges.size();
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public void print() {
        if (edges.isEmpty()) {
            System.out.println("The graph has no edges.");
        } else {
            for (Edge edge : edges) {
                edge.print();
            }
        }
    }
}
