package exe.ex3.mygame;

import assignments.Ex3.Pixel2D;

/**
 * Game Interface - Core contract for the Pacman game
 *
 * This interface defines all game operations and constants.
 * Any class implementing this interface MUST provide:
 * - Game state queries (getGame, getPos, getStatus)
 * - Game control methods (play, move, end)
 * - Collision detection (ghosts)
 *
 * CONSTANTS:
 * - Direction codes: UP=1, DOWN=3, LEFT=2, RIGHT=4, STAY=0
 * - Cell values: WALL=1, FOOD=0
 * - Status codes: INIT=0, PLAY=1, PAUSE=2, DONE=3
 * - Error: ERR=-1
 */
public interface Game {

    // ==================== DIRECTION CONSTANTS ====================
    /**
     * Movement directions (DO NOT CHANGE - must match PacmanGame)
     * These are used when calling move(int dir)
     */
    public static final int UP = 1;        // Move up (Y++)
    public static final int DOWN = 3;      // Move down (Y--)
    public static final int LEFT = 2;      // Move left (X--)
    public static final int RIGHT = 4;     // Move right (X++)
    public static final int STAY = 0;      // Stay in place

    // ==================== CELL VALUE CONSTANTS ====================
    /**
     * What's in each cell of the game board
     * Used when reading the int[][] board
     */
    public static final int WALL = 1;      // Impassable wall cell
    public static final int FOOD = 0;      // Edible dot/pellet
    public static final int EMPTY = -1;    // Empty space (no obstruction)

    // ==================== STATUS CONSTANTS ====================
    /**
     * Game state codes
     * Returned by getStatus()
     */
    public static final int INIT = 0;      // Game initialized, not playing
    public static final int PLAY = 1;      // Game is actively running
    public static final int PAUSE = 2;     // Game is paused
    public static final int DONE = 3;      // Game finished (won or lost)

    // ==================== ERROR CONSTANT ====================
    public static final int ERR = -1;      // Error/invalid direction

    // ==================== QUERY METHODS ====================

    /**
     * Get the game board as 2D integer array
     *
     * @param code unused parameter (reserved for future use)
     * @return int[][] where each cell contains WALL, FOOD, EMPTY, etc.
     *         - Dimensions: [width][height]
     *         - Access: board[x][y]
     *
     * EXAMPLE:
     *   int[][] board = game.getGame(0);
     *   int cellType = board[5][10];  // What's at position (5,10)?
     */
    int[][] getGame(int code);

    /**
     * Get Pacman's current position
     *
     * @param code unused parameter (reserved for future use)
     * @return Pixel2D object representing (x, y) coordinates
     *         null if Pacman not on board
     *
     * EXAMPLE:
     *   Pixel2D pos = game.getPos(0);
     *   int x = pos.getX();  // 0 to width-1
     *   int y = pos.getY();  // 0 to height-1
     */
    Pixel2D getPos(int code);

    /**
     * Get all ghosts in the game
     *
     * @param code unused parameter (reserved for future use)
     * @return GhostCL[] array of all ghost objects
     *         Empty array if no ghosts
     *
     * EXAMPLE:
     *   GhostCL[] ghosts = game.getGhosts(0);
     *   for (GhostCL ghost : ghosts) {
     *       Pixel2D ghostPos = ghost.getPos(0);
     *       int status = ghost.getStatus();  // 1=dangerous, 2=edible
     *   }
     */
    GhostCL[] getGhosts(int code);

    /**
     * Get current game status
     *
     * @return One of: INIT(0), PLAY(1), PAUSE(2), DONE(3)
     *
     * EXAMPLE:
     *   if (game.getStatus() == PLAY) {
     *       // Game is running, can accept moves
     *   } else if (game.getStatus() == DONE) {
     *       // Game finished - check score
     *   }
     */
    int getStatus();

    /**
     * Get the last keyboard character pressed
     *
     * @return Character pressed, or null if none
     *         Used for manual input (not for AI)
     */
    Character getKeyChar();

    /**
     * Get game statistics/data as string
     *
     * @param code unused parameter (reserved for future use)
     * @return Formatted string with game info
     *         Example: "T: 15.5, S: 250, St: 42, K: 2, P: 11,14, D: 156"
     *         T=Time, S=Score, St=Steps, K=Kills, P=Position, D=Dots remaining
     */
    String getData(int code);

    /**
     * Check if the game board wraps at edges
     *
     * @return true if cyclic (wraps), false if bounded
     *         If true: going right off edge reappears at left
     *         If false: hitting edge means hit wall
     */
    boolean isCyclic();

    // ==================== CONTROL METHODS ====================

    /**
     * Start/resume the game
     *
     * Transitions from INIT→PLAY or PAUSE→PLAY
     * Has no effect if already PLAY or DONE
     *
     * EXAMPLE:
     *   game.play();
     *   while (game.getStatus() == PLAY) {
     *       int move = ai.move(game);
     *       game.move(move);
     *   }
     */
    void play();

    /**
     * Move Pacman in the specified direction
     *
     * @param dir Direction code: UP(1), DOWN(3), LEFT(2), RIGHT(4), STAY(0)
     * @return String with result (position string or error message)
     *         Only moves if game.getStatus() == PLAY
     *
     * BEHAVIOR:
     * 1. Check direction is valid (1,2,3,4,0)
     * 2. Calculate new position
     * 3. If next cell is not WALL, move Pacman
     * 4. If next cell is FOOD, eat it (+10 points)
     * 5. Move all ghosts
     * 6. Check ghost collisions
     * 7. Return new position
     *
     * EXAMPLE:
     *   String result = game.move(RIGHT);  // Move right
     *   game.move(UP);                      // Move up
     *   game.move(STAY);                    // Don't move this turn
     */
    String move(int dir);

    /**
     * End the game immediately
     *
     * @param code unused parameter (reserved for future use)
     * @return Game statistics string (score, moves, etc.)
     *
     * Transitions status to DONE and logs results
     *
     * EXAMPLE:
     *   String stats = game.end(0);
     *   System.out.println("Game Over: " + stats);
     */
    String end(int code);

}