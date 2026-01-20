package assignments.Ex3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * MyMap2D - Complete implementation of Map2D interface
 * Provides 2D raster map operations including pathfinding and flood fill
 *
 * FEATURES:
 * - BFS-based shortest path finding
 * - Flood fill (connected component fill)
 * - All distance computation from source
 * - Cyclic/non-cyclic boundary handling
 */
public class MyMap2D implements Map2D {

    private int[][] _map;
    private boolean _cyclic;

    // ==================== CONSTRUCTORS ====================

    public MyMap2D() {
        this._map = null;
        this._cyclic = false;
    }

    public MyMap2D(int w, int h, int v) {
        init(w, h, v);
    }

    public MyMap2D(int[][] arr) {
        init(arr);
    }

    // ==================== INIT METHODS ====================

    @Override
    public void init(int w, int h, int v) {
        if (w <= 0 || h <= 0) {
            throw new RuntimeException("Width and height must be positive");
        }

        this._map = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                this._map[i][j] = v;
            }
        }
        this._cyclic = false;
    }

    @Override
    public void init(int[][] arr) {
        if (arr == null || arr.length == 0) {
            throw new RuntimeException("Array is null or empty");
        }

        // Validate rectangular array
        int height = arr[0].length;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null || arr[i].length != height) {
                throw new RuntimeException("Not a valid rectangular 2D array");
            }
        }

        // Deep copy
        this._map = new int[arr.length][height];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < height; j++) {
                this._map[i][j] = arr[i][j];
            }
        }
        this._cyclic = false;
    }

    // ==================== GETTERS ====================

    @Override
    public int[][] getMap() {
        if (this._map == null) return null;

        int[][] copy = new int[this._map.length][this._map[0].length];
        for (int i = 0; i < this._map.length; i++) {
            for (int j = 0; j < this._map[i].length; j++) {
                copy[i][j] = this._map[i][j];
            }
        }
        return copy;
    }

    @Override
    public int getWidth() {
        if (this._map == null) return 0;
        return this._map.length;
    }

    @Override
    public int getHeight() {
        if (this._map == null || this._map.length == 0) return 0;
        return this._map[0].length;
    }

    @Override
    public int getPixel(int x, int y) {
        if (!isValidCoordinate(x, y)) {
            throw new RuntimeException("Coordinate out of bounds: (" + x + "," + y + ")");
        }
        return this._map[x][y];
    }

    @Override
    public int getPixel(Pixel2D p) {
        if (p == null) throw new RuntimeException("Pixel is null");
        return getPixel(p.getX(), p.getY());
    }

    // ==================== SETTERS ====================

    @Override
    public void setPixel(int x, int y, int v) {
        if (!isValidCoordinate(x, y)) {
            throw new RuntimeException("Coordinate out of bounds: (" + x + "," + y + ")");
        }
        this._map[x][y] = v;
    }

    @Override
    public void setPixel(Pixel2D p, int v) {
        if (p == null) throw new RuntimeException("Pixel is null");
        setPixel(p.getX(), p.getY(), v);
    }

    // ==================== PROPERTIES ====================

    @Override
    public boolean isInside(Pixel2D p) {
        if (p == null) return false;
        return isValidCoordinate(p.getX(), p.getY());
    }

    @Override
    public boolean isCyclic() {
        return this._cyclic;
    }

    @Override
    public void setCyclic(boolean cy) {
        this._cyclic = cy;
    }

    // ==================== ALGORITHMS ====================

    /**
     * Flood fill - fill connected component with new color
     * Uses BFS to find all connected cells with same color
     */
    @Override
    public int fill(Pixel2D p, int new_v) {
        if (!isInside(p)) {
            return 0;
        }

        int old_v = getPixel(p);
        if (old_v == new_v) {
            return 0;
        }

        int count = 0;
        Queue<Pixel2D> queue = new LinkedList<>();
        queue.add(p);

        while (!queue.isEmpty()) {
            Pixel2D current = queue.poll();
            int x = current.getX();
            int y = current.getY();

            if (getPixel(x, y) != old_v) {
                continue;
            }

            setPixel(x, y, new_v);
            count++;

            // Add neighbors with same old color
            addFillNeighbors(queue, x, y, old_v);
        }

        return count;
    }

    /**
     * Find shortest path avoiding obstacles
     * Uses BFS to find path from p1 to p2, avoiding obsColor
     */
    @Override
    public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {

            if (!isInside(p1) || !isInside(p2)) return null;
            if (getPixel(p1) == obsColor || getPixel(p2) == obsColor) return null;
            if (p1.equals(p2)) return new Pixel2D[]{p1};

            // BFS Setup
            Queue<Pixel2D> queue = new LinkedList<>();
            // Maps each pixel to the pixel that "discovered" it
            Pixel2D[][] parents = new Pixel2D[getWidth()][getHeight()];

            queue.add(p1);
            parents[p1.getX()][p1.getY()] = p1; // Mark start as visited by itself

            boolean found = false;
            while (!queue.isEmpty() && !found) {
                Pixel2D curr = queue.poll();

                // Standard 4-direction movement
                int[] dx = {1, -1, 0, 0};
                int[] dy = {0, 0, 1, -1};

                for (int i = 0; i < 4; i++) {
                    int nx = curr.getX() + dx[i];
                    int ny = curr.getY() + dy[i];

                    // --- CYCLIC LOGIC ---
                    if (this._cyclic) {
                        nx = (nx + getWidth()) % getWidth();
                        ny = (ny + getHeight()) % getHeight();
                    }

                    if (isValidCoordinate(nx, ny) && parents[nx][ny] == null && getPixel(nx, ny) != obsColor) {
                        parents[nx][ny] = curr;
                        Pixel2D next = new Index2D(nx, ny);
                        queue.add(next);
                        if (next.equals(p2)) {
                            found = true;
                            break;
                        }
                    }
                }
            }

            // --- PATH RECONSTRUCTION ---
            if (!found) return null;

            LinkedList<Pixel2D> pathList = new LinkedList<>();
            Pixel2D temp = p2;
            while (!temp.equals(p1)) {
                pathList.addFirst(temp);
                temp = parents[temp.getX()][temp.getY()];
            }
            pathList.addFirst(p1);

            return pathList.toArray(new Pixel2D[0]);
        }


    /**
     * Compute shortest distances from start to all reachable cells
     * Returns new map with distances (-1 for unreachable)
     */
    @Override
    public Map2D allDistance(Pixel2D start, int obsColor) {
        Map2D distMap = new MyMap2D(getWidth(), getHeight(), -1);

        if (!isInside(start)) {
            return distMap;
        }

        Queue<Pixel2D> queue = new LinkedList<>();
        queue.add(start);
        distMap.setPixel(start, 0);

        int[] dx = {0, 0, -1, 1};
        int[] dy = {1, -1, 0, 0};

        while (!queue.isEmpty()) {
            Pixel2D current = queue.poll();
            int x = current.getX();
            int y = current.getY();
            int currentDist = distMap.getPixel(x, y);

            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];

                // Handle cyclic
                if (this._cyclic) {
                    nx = ((nx % getWidth()) + getWidth()) % getWidth();
                    ny = ((ny % getHeight()) + getHeight()) % getHeight();
                }

                if (isValidCoordinate(nx, ny)) {
                    if (getPixel(nx, ny) != obsColor && distMap.getPixel(nx, ny) == -1) {
                        distMap.setPixel(nx, ny, currentDist + 1);
                        queue.add(new Index2D(nx, ny));
                    }
                }
            }
        }

        distMap.setCyclic(this._cyclic);
        return distMap;
    }

    // ==================== HELPER METHODS ====================

    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
    }

    private void addFillNeighbors(Queue<Pixel2D> queue, int x, int y, int old_v) {
        int[] dx = {0, 0, -1, 1};
        int[] dy = {1, -1, 0, 0};

        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            if (this._cyclic) {
                nx = ((nx % getWidth()) + getWidth()) % getWidth();
                ny = ((ny % getHeight()) + getHeight()) % getHeight();
            }

            if (isValidCoordinate(nx, ny) && getPixel(nx, ny) == old_v) {
                queue.add(new Index2D(nx, ny));
            }
        }
    }

    private void addPathNeighbors(Queue<Pixel2D> queue, boolean[][] visited,
                                  int x, int y, int obsColor, int[][] parent) {
        int[] dx = {0, 0, -1, 1};
        int[] dy = {1, -1, 0, 0};

        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            if (this._cyclic) {
                nx = ((nx % getWidth()) + getWidth()) % getWidth();
                ny = ((ny % getHeight()) + getHeight()) % getHeight();
            }

            if (isValidCoordinate(nx, ny)) {
                if (!visited[nx][ny] && getPixel(nx, ny) != obsColor) {
                    visited[nx][ny] = true;
                    parent[nx][ny] = x * getHeight() + y;
                    queue.add(new Index2D(nx, ny));
                }
            }
        }
    }

    private Pixel2D[] reconstructPath(Pixel2D p1, Pixel2D p2, int[][] parent) {
        ArrayList<Pixel2D> path = new ArrayList<>();

        int x = p2.getX();
        int y = p2.getY();

        while (!(x == p1.getX() && y == p1.getY())) {
            path.add(0, new Index2D(x, y));

            int parentIdx = parent[x][y];
            if (parentIdx == -1) break;

            x = parentIdx / getHeight();
            y = parentIdx % getHeight();
        }

        path.add(0, p1);
        return path.toArray(new Pixel2D[0]);
    }
}