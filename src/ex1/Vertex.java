package ex1;

/** A vertex identified by a name or a nonnegative integer. */
public final class Vertex {
    private final Object label;

    public Vertex(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Vertex names must not be blank.");
        }
        label = name.trim();
    }

    public Vertex(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Numeric vertices must be nonnegative.");
        }
        label = value;
    }

    public Object getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label.toString();
    }
}
