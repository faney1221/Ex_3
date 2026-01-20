package assignments.Ex3;

import exe.ex3.game.Game;
import exe.ex3.game.PacmanGame;
import exe.ex3.game.GhostCL;
import exe.ex3.mygame.PacManAlgo;

public class Ex3Algo implements PacManAlgo {

    public static final int WALL = 1;
    public static final int FOOD = 3;

    @Override
    public int move(Game game) {
        // 1. Get the map data
        int[][] board = game.getGame(0);
        MyMap2D map = new MyMap2D(board);
        map.setCyclic(game.isCyclic());

        // 2. Get Pacman's Position (Based on your successful debug log)
        GhostCL[] ghosts = game.getGhosts(0);
        if (ghosts == null || ghosts.length == 0) return PacmanGame.STAY;

        // Your logs showed this string format "11,11" works
        String posStr = ghosts[0].getPos(0);
        Pixel2D pacmanPos = new Index2D(posStr);

        // 3. Find the nearest food
        Pixel2D targets = findNearestFood(map, pacmanPos);
        if (targets == null) return anyLegalMove(map, pacmanPos);

        // 4. Find the path using your BFS (shortestPath)
        Pixel2D[] path = map.shortestPath(pacmanPos, targets, WALL);

        if (path != null && path.length > 1) {
            return getDirection(pacmanPos, path[1], map);
        }

        return anyLegalMove(map, pacmanPos);
    }

    private int getDirection(Pixel2D current, Pixel2D next, MyMap2D map) {
        int dx = next.getX() - current.getX();
        int dy = next.getY() - current.getY();

        // Handle Cyclic (Wrap-around) movement
        if (map.isCyclic()) {
            if (dx > 1) return PacmanGame.LEFT;
            if (dx < -1) return PacmanGame.RIGHT;
            if (dy > 1) return PacmanGame.DOWN;
            if (dy < -1) return PacmanGame.UP;
        }

        if (dx == 1) return PacmanGame.RIGHT;
        if (dx == -1) return PacmanGame.LEFT;
        if (dy == 1) return PacmanGame.UP;
        if (dy == -1) return PacmanGame.DOWN;

        return PacmanGame.STAY;
    }

    private Pixel2D findNearestFood(MyMap2D map, Pixel2D start) {
        Pixel2D closest = null;
        double minDistance = Double.MAX_VALUE;

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if (map.getPixel(x, y) == FOOD) {
                    // Manual distance calculation (dx*dx + dy*dy)
                    int dx = start.getX() - x;
                    int dy = start.getY() - y;
                    double dist = Math.sqrt(dx * dx + dy * dy);

                    if (dist < minDistance) {
                        minDistance = dist;
                        closest = new Index2D(x, y);
                    }
                }
            }
        }
        return closest;
    }

    private int anyLegalMove(MyMap2D map, Pixel2D p) {
        // Just keep moving if BFS fails
        int[] moves = {PacmanGame.UP, PacmanGame.DOWN, PacmanGame.LEFT, PacmanGame.RIGHT};
        for (int m : moves) {
            // Check if the move leads to a wall
            return m;
        }
        return PacmanGame.STAY;
    }

    // Redirect all possible move calls to the logic above
    @Override public int move(PacmanGame game) { return move((Game) game); }
    @Override public int move(exe.ex3.mygame.PacmanGame game) { return move((Game) game); }
    @Override public String getInfo() { return "BFS Algo"; }
}