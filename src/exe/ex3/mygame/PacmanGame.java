package exe.ex3.mygame;

/**
 * PacmanGame Interface - Contract for AI Algorithms
 *
 * This interface is used by AI algorithm classes (Ex3Algo, etc.)
 * It defines the same methods as Game, but with PacmanGame naming
 * to distinguish it from the professor's Game interface
 *
 * WHY TWO INTERFACES?
 * - Game = Your game implementation (MyGame → PacmanGameImpl)
 * - PacmanGame = Algorithm input interface (for AI code)
 * - Allows switching between different game engines
 * - Helps with testing and modularity
 *
 * CONSTANTS:
 * All constants are the same as Game interface
 * Make sure they match! Otherwise direction commands won't work.
 *
 * USAGE:
 * public class Ex3Algo implements PacManAlgo {
 *     public int move(Game game) {
 *         // AI algorithm reads game state and decides move
 *         Pixel2D pac = game.getPos(0);
 *         int[][] board = game.getGame(0);
 *         // ... AI logic ...
 *         return direction;  // UP, DOWN, LEFT, or RIGHT
 *     }
 * }
 */
public interface PacmanGame {

    // ==================== DIRECTION CONSTANTS ====================
    /**
     * Movement directions
     * MUST MATCH Game interface constants!
     *
     * These are used as parameters to move(int direction)
     */
    public static final int UP = 1;         // Move up (Y++)
    public static final int DOWN = 3;       // Move down (Y--)
    public static final int LEFT = 2;       // Move left (X--)
    public static final int RIGHT = 4;      // Move right (X++)
    public static final int STAY = 0;       // No movement

    // ==================== CELL VALUE CONSTANTS ====================
    /**
     * What each cell contains on the game board
     * Used when analyzing board state for AI decisions
     */
    public static final int WALL = 1;       // Impassable cell
    public static final int FOOD = 0;       // Edible dot
    public static final int EMPTY = -1;     // Empty space

    // ==================== ERROR CONSTANT ====================
    public static final int ERR = -1;       // Error/invalid direction

    // ==================== STATUS CONSTANTS ====================
    /**
     * Game state codes
     */
    public static final int INIT = 0;       // Not started
    public static final int PLAY = 1;       // Running
    public static final int PAUSE = 2;      // Paused
    public static final int DONE = 5;       // Finished (win/lose)

    // ==================== QUERY METHODS ====================
    // Same as Game interface - for AI to read game state

    /**
     * Get the game board (2D integer array)
     *
     * @param code Unused (reserved)
     * @return int[][] board where board[x][y] contains:
     *         - WALL (1) = cannot pass through
     *         - FOOD (0) = edible dot, gives +10 points
     *         - EMPTY (-1) = passable space
     *         - Other values = special cells (power pellets, etc.)
     *
     * ARRAY DIMENSIONS:
     * - board.length = width (x-axis)
     * - board[0].length = height (y-axis)
     * - Access: int cell = board[x][y]
     *
     * EXAMPLE - AI checks what's ahead:
     *   int[][] board = game.getGame(0);
     *   int cellAhead = board[pacX+1][pacY];  // Right of Pacman
     *   if (cellAhead == WALL) {
     *       // Can't move right
     *   } else if (cellAhead == FOOD) {
     *       // Food ahead! Move that way
     *   }
     */
    int[][] getGame(int code);

    /**
     * Get Pacman's position
     *
     * @param code Unused
     * @return String in format "x,y"
     *         Example: "11,14" means x=11, y=14
     *         null if Pacman not found (rare error)
     *
     * NOTE: Returns String, not Pixel2D!
     * This is different from Game interface
     * Parse it yourself: String[] parts = pos.split(",");
     *
     * EXAMPLE - AI finds Pacman:
     *   String posStr = game.getPos(0);  // Returns "11,14"
     *   String[] parts = posStr.split(",");
     *   int pacX = Integer.parseInt(parts[0]);
     *   int pacY = Integer.parseInt(parts[1]);
     */
    String getPos(int code);

    /**
     * Get all ghosts in the game
     *
     * @param code Unused
     * @return GhostCL[] array with all ghost objects
     *         Empty array if no ghosts
     *
     * EXAMPLE - AI checks ghost positions:
     *   GhostCL[] ghosts = game.getGhosts(0);
     *   for (GhostCL ghost : ghosts) {
     *       Pixel2D ghostPos = ghost.getPos(0);
     *       int ghostX = ghostPos.getX();
     *       int ghostY = ghostPos.getY();
     *       int ghostStatus = ghost.getStatus();  // 1=dangerous, 2=edible
     *   }
     */
    GhostCL[] getGhosts(int code);

    /**
     * Get current game status
     *
     * @return One of: INIT(0), PLAY(1), PAUSE(2), DONE(5)
     *
     * EXAMPLE - AI checks if game is running:
     *   if (game.getStatus() != PLAY) {
     *       // Game not running, can't move
     *       return STAY;
     *   }
     */
    int getStatus();

    /**
     * Get the last keyboard key pressed
     *
     * @return Character, or null if none
     *
     * Not used in AI games
     * Only for human/manual control
     */
    Character getKeyChar();

    /**
     * Get formatted game statistics
     *
     * @param code Unused
     * @return String with game info
     *         Example: "T: 15.5, S: 250, St: 42, K: 2, P: 11,14, D: 156"
     *         T=Time, S=Score, St=Steps, K=Ghosts eaten, P=Position, D=Dots left
     *
     * Useful for logging or debugging AI performance
     */
    String getData(int code);

    /**
     * Check if board wraps at edges (cyclic mode)
     *
     * @return true = board wraps (like original Pac-Man)
     *         false = bounded board (can't go off edges)
     *
     * AFFECTS:
     * - When moving off right edge: reappear at left (if cyclic)
     * - When moving off top: reappear at bottom (if cyclic)
     *
     * AI USAGE:
     *   if (game.isCyclic()) {
     *       // Can safely go off edges
     *   } else {
     *       // Avoid going off edges
     *   }
     */
    boolean isCyclic();

    // ==================== CONTROL METHODS ====================
    // AI uses these to control the game

    String init(int level, String myId, boolean cyclic, long seed,
                double dt, int res, int extra);

    /**
     * Start or resume the game
     *
     * Transitions: INIT → PLAY or PAUSE → PLAY
     *
     * MUST call this before move()
     * No parameters needed
     *
     * EXAMPLE:
     *   MyGame game = new MyGame();
     *   game.init(...);
     *   game.play();  // Start game
     */
    void play();

    /**
     * Move Pacman in specified direction
     *
     * @param direction UP(1), DOWN(3), LEFT(2), RIGHT(4), or STAY(0)
     * @return String result with new position "x,y"
     *         Returns current position if move failed
     *
     * ONLY MOVES IF status == PLAY
     *
     * ALGORITHM (engine does this):
     * 1. Calculate new position based on direction
     * 2. Check if next cell is passable (not WALL)
     * 3. Move Pacman
     * 4. If cell has FOOD: eat it, +10 points
     * 5. Move all ghosts
     * 6. Check ghost collisions
     * 7. Check win condition (all food eaten)
     *
     * AI EXAMPLE - Basic movement:
     *   game.move(RIGHT);
     *   game.move(UP);
     *   game.move(LEFT);
     *
     * AI EXAMPLE - Smart movement:
     *   if (shouldGoRight) {
     *       game.move(RIGHT);
     *   } else if (shouldGoUp) {
     *       game.move(UP);
     *   } else {
     *       game.move(STAY);  // Wait
     *   }
     */
    String move(int direction);

    /**
     * End the game and get statistics
     *
     * @param code Unused
     * @return CSV formatted statistics string
     *         Format: userID,level,score,dots,steps,time,kills,checksum
     *
     * Transitions status to DONE
     *
     * EXAMPLE:
     *   String stats = game.end(0);
     *   System.out.println("Game Statistics: " + stats);
     */
    String end(int code);

}