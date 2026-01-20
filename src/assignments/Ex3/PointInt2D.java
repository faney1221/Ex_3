package assignments.Ex3;

import assignments.Ex3.Pixel2D;
import assignments.Ex3.Index2D;

/**
 * PointInt2D - Internal coordinate class for the game (internal use only)
 * Used by PacmanGameImpl to track positions
 * 
 * YOUR MISTAKE: You called this in toString() but it wasn't defined
 * CORRECTION: Created this helper class with proper string conversion
 */
public class PointInt2D implements Pixel2D {
    private int _x;
    private int _y;

    /**
     * Default constructor
     */
    public PointInt2D() {
        this(0, 0);
    }

    /**
     * Constructor with coordinates
     */
    public PointInt2D(int x, int y) {
        this._x = x;
        this._y = y;
    }

    /**
     * Copy constructor
     */
    public PointInt2D(PointInt2D other) {
        if (other == null) {
            throw new RuntimeException("Cannot copy null PointInt2D");
        }
        this._x = other._x;
        this._y = other._y;
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
        return 31 * this._x + this._y;
    }
}