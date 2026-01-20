package assignments.Ex3;

/**
 * Index2D - Implementation of Pixel2D interface
 * Represents a 2D integer coordinate (x,y)
 */
public class Index2D implements Pixel2D {
    private int _x;
    private int _y;

    public Index2D() {
        this(0, 0);
    }

    public Index2D(int x, int y) {
        this._x = x;
        this._y = y;
    }

    /**
     * Copy constructor - throws RuntimeException if p is null
     */
    public Index2D(Pixel2D p) {
        if (p == null) {
            throw new RuntimeException("Pixel2D cannot be null");
        }
        this._x = p.getX();
        this._y = p.getY();
    }

    /**
     * String constructor - parses "x,y" strings (e.g., "12,5")
     * Used by the game engine to load positions.
     */
    public Index2D(String pos) {
        if (pos == null || !pos.contains(",")) {
            throw new RuntimeException("Invalid position string: " + pos);
        }
        try {
            String[] parts = pos.split(",");
            this._x = Integer.parseInt(parts[0].trim());
            this._y = Integer.parseInt(parts[1].trim());
        } catch (Exception e) {
            throw new RuntimeException("Error parsing position string: " + pos);
        }
    }

    @Override
    public int getX() {
        return this._x;
    }

    @Override
    public int getY() {
        return this._y;
    }

    @Override
    public double distance2D(Pixel2D p) {
        if (p == null) {
            throw new RuntimeException("ERR: got null for the Pixel2D:distance2D method");
        }
        double dx = p.getX() - this._x;
        double dy = p.getY() - this._y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return this._x + "," + this._y;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pixel2D)) {
            return false;
        }
        Pixel2D other = (Pixel2D) obj;
        return this._x == other.getX() && this._y == other.getY();
    }

    @Override
    public int hashCode() {
        // Standard formula to ensure unique points have unique hash codes
        return 31 * this._x + this._y;
    }
}