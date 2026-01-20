package exe.ex3.mygame;

import assignments.Ex3.Index2D;
import assignments.Ex3.Pixel2D;

/**
 * MyGame - Adapter Pattern Bridge
 *
 * DESIGN PATTERN: Adapter Pattern
 *
 * PURPOSE:
 * - Wraps PacmanGameImpl and exposes it through Game interface
 * - Converts between interface requirements
 * - Allows easy swapping of implementations
 * - Decouples client code from concrete implementation
 *
 * ARCHITECTURE:
 *   Game Interface (contract)
 *        ↑
 *        │ implements
 *        │
 *   MyGame (adapter)
 *        │ delegates to
 *        ↓
 *   PacmanGameImpl (concrete implementation)
 *
 * BENEFITS:
 * 1. Your code uses Game interface (abstract)
 * 2. Can swap implementations without changing client
 * 3. Can add logging/monitoring in adapter
 * 4. PacmanGameImpl stays simple and focused
 * 5. Easy to mock for testing
 *
 * FLOW EXAMPLE:
 *   MyGame game = new MyGame();
 *   game.init(0, "Player1", true, 1234, 0.1, 10, 0);
 *        ↓
 *   MyGame.init() calls gameImpl.init(...)
 *        ↓
 *   PacmanGameImpl initializes the game
 */
public class MyGame implements Game {

    // ==================== INSTANCE VARIABLES ====================

    /**
     * The actual game implementation we're wrapping
     *
     * This is the "real" game that does all the work
     * MyGame just delegates calls to it
     */
    private PacmanGameImpl gameImpl;

    // ==================== CONSTRUCTOR ====================

    /**
     * Constructor - Create a new game
     *
     * Creates and initializes the wrapped PacmanGameImpl instance
     *
     * EXAMPLE:
     *   MyGame game = new MyGame();  // Creates fresh game instance
     */
    public MyGame() {
        gameImpl = new PacmanGameImpl();
    }

    // ==================== INITIALIZATION ====================

    /**
     * Initialize the game with configuration parameters
     *
     * Simply delegates to wrapped implementation
     *
     * @param level Game difficulty (0-4)
     *              0 = Easy (lots of points)
     *              2 = Medium (default)
     *              4 = Hard (fewer points, smarter ghosts)
     *
     * @param myId Player identifier (for logging/scoring)
     *             Used in game result CSV
     *
     * @param cyclic true = board wraps at edges (Pac-Man style)
     *               false = bounded board (Pacman dies at edge)
     *
     * @param seed Random seed for ghost movement
     *             Same seed = same ghost behavior pattern
     *             Used for reproducible testing
     *
     * @param dt Time delta (game speed)
     *           Currently unused, reserved for frame rate control
     *
     * @param res Resolution (graphics)
     *           Currently unused, reserved for rendering
     *
     * @param extra Extra parameters for future features
     *            Currently unused
     *
     * EXAMPLE:
     *   MyGame game = new MyGame();
     *   game.init(2, "John_Score123", true, 42, 0.1, 10, 0);
     *   // Medium difficulty, cyclic board, seed=42
     */
    public void init(int level, String myId, boolean cyclic, long seed,
                     double dt, int res, int extra) {
        gameImpl.init(level, myId, cyclic, seed, dt, res, extra);
    }

    // ==================== ADAPTER METHODS ====================
    // These delegate to PacmanGameImpl while satisfying Game interface

    /**
     * Get the game board
     *
     * @param code Unused (reserved for future use)
     * @return int[][] board where board[x][y] is:
     *         - Game.WALL (1) = impassable wall
     *         - Game.FOOD (0) = edible dot
     *         - Game.EMPTY (-1) = empty space
     *         - Positive number = special cell (power pellet, etc.)
     *
     * DELEGATES TO: gameImpl.getGame()
     *
     * USAGE:
     *   int[][] board = game.getGame(0);
     *   if (board[pacX][pacY] == Game.WALL) {
     *       // Hit a wall!
     *   }
     */
    @Override
    public int[][] getGame(int code) {
        return gameImpl.getGame(code);
    }

    /**
     * Get Pacman's current position
     *
     * @param code Unused
     * @return Pixel2D with current (x, y) coordinates
     *         null if Pacman not on board (should not happen)
     *
     * DELEGATES TO: gameImpl.getPos() which returns "x,y"
     *              This adapter parses it into Pixel2D
     *
     * CONVERSION:
     *   gameImpl returns: "11,14" (String)
     *   This method returns: Index2D(11, 14) (Pixel2D)
     *
     * ERROR HANDLING:
     *   - Returns (0,0) if parsing fails
     *   - Prints error message to stderr
     *
     * USAGE:
     *   Pixel2D pos = game.getPos(0);
     *   int x = pos.getX();
     *   int y = pos.getY();
     */
    @Override
    public Pixel2D getPos(int code) {
        String posStr = gameImpl.getPos(code);
        if (posStr == null || posStr.isEmpty()) {
            return new Index2D(0, 0);
        }

        try {
            // Parse "x,y" format
            String[] parts = posStr.split(",");
            int x = Integer.parseInt(parts[0].trim());
            int y = Integer.parseInt(parts[1].trim());
            return new Index2D(x, y);
        } catch (Exception e) {
            System.err.println("Error parsing position: " + posStr);
            return new Index2D(0, 0);
        }
    }

    /**
     * Get all ghosts in the game
     *
     * @param code Unused
     * @return GhostCL[] array of all ghost objects
     *         Empty array if no ghosts
     *
     * DELEGATES TO: gameImpl.getGhosts()
     *
     * USAGE:
     *   GhostCL[] ghosts = game.getGhosts(0);
     *   for (GhostCL ghost : ghosts) {
     *       // Check ghost status, position, etc.
     *   }
     */
    @Override
    public GhostCL[] getGhosts(int code) {
        return gameImpl.getGhosts(code);
    }

    /**
     * Get current game status
     *
     * @return One of:
     *         - Game.INIT (0) = Initialized, not playing
     *         - Game.PLAY (1) = Currently running
     *         - Game.PAUSE (2) = Paused
     *         - Game.DONE (3) = Game over (win or lose)
     *
     * DELEGATES TO: gameImpl.getStatus()
     *
     * USAGE:
     *   while (game.getStatus() == Game.PLAY) {
     *       int move = ai.move(game);
     *       game.move(move);
     *   }
     */
    @Override
    public int getStatus() {
        return gameImpl.getStatus();
    }

    /**
     * Move Pacman in specified direction
     *
     * @param dir Direction code:
     *            - Game.UP (1) = move up
     *            - Game.DOWN (3) = move down
     *            - Game.LEFT (2) = move left
     *            - Game.RIGHT (4) = move right
     *            - Game.STAY (0) = don't move
     *
     * @return String result (position or error)
     *         Returns "x,y" after move
     *         Only moves if game.getStatus() == Game.PLAY
     *
     * DELEGATES TO: gameImpl.move()
     *
     * SIDE EFFECTS:
     * - Moves Pacman
     * - Moves ghosts
     * - Checks collisions
     * - Updates score
     * - May end game (win/lose)
     *
     * USAGE:
     *   String result = game.move(Game.RIGHT);
     *   game.move(Game.UP);
     *   game.move(Game.STAY);
     */
    @Override
    public String move(int dir) {
        return gameImpl.move(dir);
    }

    /**
     * Start or resume the game
     *
     * - If status == INIT: transition to PLAY
     * - If status == PLAY: no effect
     * - If status == DONE: no effect (game over)
     *
     * DELEGATES TO: gameImpl.play()
     *
     * MUST CALL THIS before calling move()
     *
     * USAGE:
     *   MyGame game = new MyGame();
     *   game.init(0, "Player", true, 1234, 0.1, 10, 0);
     *   game.play();  // Start the game
     *
     *   while (game.getStatus() == Game.PLAY) {
     *       game.move(Game.RIGHT);
     *   }
     */
    @Override
    public void play() {
        gameImpl.play();
    }

    /**
     * Get the last keyboard character pressed
     *
     * @return Character pressed, or null
     *         Used for human player (not AI)
     *
     * DELEGATES TO: gameImpl.getKeyChar()
     *
     * NOT USED in AI games
     */
    @Override
    public Character getKeyChar() {
        return gameImpl.getKeyChar();
    }

    /**
     * End the game immediately
     *
     * @param code Unused
     * @return Game statistics string (CSV format)
     *         Format: userID,level,score,dots,steps,time,kills,checksum
     *
     * DELEGATES TO: gameImpl.end()
     *
     * TRANSITIONS: status → DONE (3)
     *
     * USAGE:
     *   String stats = game.end(0);
     *   System.out.println("Final Score: " + stats);
     */
    @Override
    public String end(int code) {
        return gameImpl.end(code);
    }

    /**
     * Get game statistics/data
     *
     * @param code Unused
     * @return Formatted string with current game info:
     *         "T: 15.5, S: 250, St: 42, K: 2, P: 11,14, D: 156"
     *         T = Time elapsed (seconds)
     *         S = Score
     *         St = Steps (moves made)
     *         K = Kills (ghosts eaten)
     *         P = Pacman position
     *         D = Dots remaining
     *
     * DELEGATES TO: gameImpl.getData()
     *
     * USAGE:
     *   String info = game.getData(0);
     *   System.out.println(info);  // Print status bar
     */
    @Override
    public String getData(int code) {
        return gameImpl.getData(code);
    }

    /**
     * Check if board wraps at edges
     *
     * @return true if board is cyclic (Pac-Man wraps)
     *         false if board is bounded
     *
     * DELEGATES TO: gameImpl.isCyclic()
     *
     * DETERMINES:
     * - When Pacman goes off right edge → reappears at left (if true)
     * - Same for up/down edges
     *
     * USAGE:
     *   if (game.isCyclic()) {
     *       // Board wraps
     *   } else {
     *       // Board is bounded
     *   }
     */
    @Override
    public boolean isCyclic() {
        return gameImpl.isCyclic();
    }

}