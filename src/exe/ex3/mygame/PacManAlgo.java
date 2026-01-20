package exe.ex3.mygame;

import exe.ex3.game.Game;

/**
 * PacManAlgo Interface - Standard for AI Algorithm Classes
 *
 * PURPOSE:
 * - Define contract for all Pacman AI algorithm implementations
 * - Allows professor's game engine to run different AI strategies
 * - Standard interface that all homework solutions must implement
 *
 * WHAT IS AN AI ALGORITHM?
 * - Takes current game state as input
 * - Analyzes board, ghosts, score, etc.
 * - Returns best move (UP, DOWN, LEFT, RIGHT, or STAY)
 * - Called once per game turn (~100ms)
 *
 * WHO CALLS THIS?
 * 1. Game main loop: while(game.isPlaying()) { game.move(algo.move()); }
 * 2. Professor's grading system tests your algorithm
 * 3. Your testing code
 *
 * IMPLEMENTATION STRATEGIES:
 * 1. Random: Pick random valid direction
 *    ✓ Simple, always works
 *    ✗ Terrible performance
 *
 * 2. Greedy: Always move toward nearest food
 *    ✓ Better than random
 *    ✗ Can get stuck in dead ends
 *
 * 3. BFS (Breadth-First Search): Find shortest path to food
 *    ✓ Very good, finds optimal solutions
 *    ✗ Slower, needs pathfinding
 *
 * 4. A* Search: BFS with heuristic
 *    ✓ Best performance
 *    ✗ Complex implementation
 *
 * 5. Ghost Avoidance: Smart tactics
 *    ✓ Survives longer
 *    ✗ More complex logic
 */
public interface PacManAlgo {

    // ==================== MAIN ALGORITHM METHOD ====================

    /**
     * Decide next move based on game state
     *
     * This is the MAIN METHOD you must implement.
     * Called once per game turn by the game engine.
     *
     * @param game Current game state (read-only from AI perspective)
     *             Contains: board, Pacman position, ghosts, score, etc.
     *             DO NOT call game.end() here
     *
     * @return Direction code for next move:
     *         - PacmanGame.UP (1) = move up
     *         - PacmanGame.DOWN (3) = move down
     *         - PacmanGame.LEFT (2) = move left
     *         - PacmanGame.RIGHT (4) = move right
     *         - PacmanGame.STAY (0) = don't move this turn
     *         - PacmanGame.ERR (-1) = no valid move (fallback)
     *
     * ALGORITHM TEMPLATE:
     *   @Override
     *   public int move(Game game) {
     *       // 1. Read game state
     *       Pixel2D pacman = game.getPacman();
     *       int[][] board = game.getGame(0);
     *
     *       // 2. Check if game is running
     *       if (game.getStatus() != Game.PLAY) {
     *           return Game.STAY;
     *       }
     *
     *       // 3. Analyze board and decide move
     *       // ... your AI logic here ...
     *
     *       // 4. Return best direction
     *       return bestDirection;
     *   }
     *
     * SIMPLE EXAMPLE - Random movement:
     *   @Override
     *   public int move(Game game) {
     *       if (game.getStatus() != Game.PLAY) return Game.STAY;
     *
     *       int[] directions = {Game.UP, Game.DOWN, Game.LEFT, Game.RIGHT};
     *       return directions[random.nextInt(4)];
     *   }
     *
     * BETTER EXAMPLE - Chase food:
     *   @Override
     *   public int move(Game game) {
     *       Pixel2D pac = game.getPos(0);
     *       int[][] board = game.getGame(0);
     *
     *       // Find nearest food
     *       Pixel2D nearestFood = findNearestFood(board, pac);
     *
     *       // Move toward it
     *       if (nearestFood.getX() > pac.getX()) {
     *           return Game.RIGHT;
     *       } else if (nearestFood.getX() < pac.getX()) {
     *           return Game.LEFT;
     *       } else if (nearestFood.getY() > pac.getY()) {
     *           return Game.UP;
     *       } else {
     *           return Game.DOWN;
     *       }
     *   }
     *
     * CONSTRAINTS:
     * - Must return in < 100ms (no infinite loops!)
     * - Must return valid direction code
     * - Should not modify game state
     * - Can't call game.end() or game.play()
     *
     * @see Game interface for available methods
     */
    int move(Game game);

    // ==================== METADATA METHODS ====================

    /**
     * Get description of your algorithm
     *
     * Called by testing framework to identify your strategy
     *
     * @return String describing your AI approach
     *         Examples:
     *         - "BFS to nearest food, avoid ghosts"
     *         - "A* with food attraction, ghost repulsion"
     *         - "Random walk with wall avoidance"
     *         - "Greedy nearest-food chaser"
     *
     * USED FOR:
     * - Logging and debugging
     * - Identifying which algorithm is running
     * - Professor's evaluation
     * - Future improvements/notes
     *
     * EXAMPLE:
     *   @Override
     *   public String getInfo() {
     *       return "Smart Pacman - BFS pathfinding with ghost avoidance";
     *   }
     */
    String getInfo();

    /**
     * Alternative move method for different game interface
     *
     * IMPORTANT: This is a workaround for interface compatibility
     *
     * Some game implementations use PacmanGame interface
     * instead of Game interface. This method handles that.
     *
     * @param game Game instance using PacmanGame interface
     * @return Direction code (same as move(Game game))
     *
     * DEFAULT IMPLEMENTATION in your class:
     *   @Override
     *   public int move(PacmanGame game) {
     *       // Delegate to main move() method
     *       // Or implement differently if needed
     *       return 0;
     *   }
     *
     * NOTE:
     * - Usually just delegates to move(Game)
     * - May have different implementations for different versions
     * - Professor will use whichever method is available
     */
    int move(PacmanGame game);

    int move(exe.ex3.game.PacmanGame game);
}