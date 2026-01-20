package exe.ex3.mygame;

import assignments.Ex3.PointInt2D;
import assignments.Ex3.Index2D;
import assignments.Ex3.Pixel2D;
import java.util.ArrayList;
import java.util.Random;

/**
 * PacmanGameImpl - Complete Pacman Game Engine
 *
 * RESPONSIBILITIES:
 * 1. Game state management (position, score, status)
 * 2. Board/map initialization
 * 3. Movement and collision detection
 * 4. Ghost AI and behavior
 * 5. Win/lose conditions
 * 6. Score calculation
 *
 * COORDINATE SYSTEM:
 * - X axis: 0 (left) to width-1 (right)
 * - Y axis: 0 (bottom) to height-1 (top)
 * - Direction UP means Y++, DOWN means Y--
 *
 * GAME LOOP:
 * 1. Initialize with init()
 * 2. Call play() to start
 * 3. Each turn: move(direction) moves Pacman & ghosts
 * 4. Check win/lose automatically
 * 5. Call end() to finish
 */
public class PacmanGameImpl implements PacmanGame {

    // ==================== GAME STATE VARIABLES ====================

    private int _level;                    // Current difficulty level (0-4)
    private String _userID;                // Player identifier
    private int _score;                    // Current score (10 per food, 100 per power, 200 per ghost)
    private int _dots;                     // Number of dots remaining
    private int _kills;                    // Number of ghosts eaten
    private int _status;                   // Game state: 0=INIT, 1=PLAY, 2=PAUSE, 3=DONE
    private int _steps;                    // Number of moves made
    private PointInt2D _pos;               // Pacman's current (x,y) position
    private int _dir;                      // Pacman's facing direction (0=right, 90=up, 180=left, 270=down)
    private int[][] _gameMap;             // The 2D board (values: WALL, FOOD, EMPTY, etc.)
    private ArrayList<GhostCL> _ghosts;   // All ghosts in game
    private long _startTime;               // Timestamp when play() was called
    private boolean _cyclic;               // true=wrap edges, false=bounded
    private Random _rand;                  // Random for ghost movement
    private Character _lastKeyChar;        // Last keyboard input (for human play)

    // ==================== INITIALIZATION ====================

    /**
     * Constructor - Initialize all fields to default values
     */
    public PacmanGameImpl() {
        this._score = 0;
        this._kills = 0;
        this._ghosts = new ArrayList<>();
        this._startTime = 0;
        this._rand = new Random(31);
        this._cyclic = true;
        this._status = 0;  // Start in INIT state
        this._steps = 0;
        this._dir = 0;
    }

    /**
     * Initialize the game with parameters
     *
     * @param level Game difficulty (0-4), default 2
     * @param myId Player name/ID for logging
     * @param cyclic true=board wraps at edges, false=bounded
     * @param seed Random seed for ghost movement
     * @param dt Time delta (game speed) - currently unused
     * @param res Resolution - currently unused
     * @param extra Extra parameter - currently unused
     * @return Log string with game initialization info
     *
     * PROCESS:
     * 1. Validate and set level
     * 2. Reset all game state
     * 3. Initialize board with walls and dots
     * 4. Count total food items
     * 5. Create ghosts at starting positions
     */
    @Override
    public String init(int level, String myId, boolean cyclic, long seed,
                       double dt, int res, int extra) {

        // Validate level is in range 0-4
        if (level < 0 || level > 4) {
            this._level = 2;  // Default to medium
        } else {
            this._level = level;
        }

        this._userID = myId;
        this._pos = new PointInt2D(11, 14);      // Pacman starts at center
        this._steps = 0;
        this._cyclic = cyclic;
        this._rand = new Random(seed);
        this._status = 0;  // INIT state
        this._score = 0;
        this._kills = 0;

        // Build the game board with walls, food, and open spaces
        initMap();

        // Count how many dots are in the map
        this._dots = 0;
        for (int i = 0; i < this._gameMap.length; i++) {
            for (int j = 0; j < this._gameMap[i].length; j++) {
                if (this._gameMap[i][j] == PacmanGame.FOOD) {
                    this._dots++;
                }
            }
        }

        // Create 6 ghosts at different starting positions
        this._ghosts.clear();
        this._ghosts.add(new GhostCL(10, 10, 1, 1));  // Ghost type 1
        this._ghosts.add(new GhostCL(10, 10, 1, 2));  // Ghost type 2
        this._ghosts.add(new GhostCL(11, 11, 1, 3));  // Ghost type 3
        this._ghosts.add(new GhostCL(11, 11, 1, 4));  // Ghost type 4
        this._ghosts.add(new GhostCL(12, 12, 1, 5));  // Ghost type 5
        this._ghosts.add(new GhostCL(12, 12, 1, 6));  // Ghost type 6

        return log();
    }

    /**
     * Build the game board
     *
     * BOARD DESIGN:
     * - Size: 22 width x 21 height
     * - Entire map filled with FOOD (dots)
     * - Borders and internal walls set to EMPTY
     * - Power pellets (value 5) at corners
     * - Pacman starting position set to EMPTY
     *
     * CONSTANTS:
     * - FOOD = 0 (edible dots)
     * - EMPTY = -1 (passable space)
     * - POWER = 5 (power pellets - currently unused)
     */
    private void initMap() {
        this._gameMap = new int[22][21];

        // Step 1: Fill entire map with food dots
        for (int i = 0; i < 22; i++) {
            for (int j = 0; j < 21; j++) {
                this._gameMap[i][j] = PacmanGame.FOOD;  // 0 = dot
            }
        }

        // Step 2: Create border walls (top and bottom)
        for (int i = 0; i < 22; i++) {
            this._gameMap[i][0] = PacmanGame.WALL;     // Bottom wall
            this._gameMap[i][20] = PacmanGame.WALL;    // Top wall
        }

        // Step 3: Create border walls (left and right)
        for (int j = 0; j < 21; j++) {
            this._gameMap[0][j] = PacmanGame.WALL;     // Left wall
            this._gameMap[21][j] = PacmanGame.WALL;    // Right wall
        }

        // Step 4: Create internal maze walls
        for (int i = 5; i < 18; i++) {
            this._gameMap[i][10] = PacmanGame.WALL;    // Horizontal wall divider
        }

        // Step 5: Add power pellets at corners (bonus points when eaten)
        this._gameMap[2][2] = 5;     // Corner power-up
        this._gameMap[2][18] = 5;
        this._gameMap[19][2] = 5;
        this._gameMap[19][18] = 5;

        // Step 6: Clear Pacman's starting position
        this._gameMap[11][14] = PacmanGame.EMPTY;
    }

    // ==================== GAME CONTROL ====================

    /**
     * Start or resume the game
     *
     * - If INIT: transition to PLAY and record start time
     * - If PLAY: no effect
     * - If DONE: no effect (game over, must create new game)
     */
    @Override
    public void play() {
        if (this._status == 0) {  // INIT
            this._startTime = System.currentTimeMillis();
        }

        if (this._status != 3) {  // Not DONE
            this._status = 1;  // PLAY
        }
    }

    /**
     * Move Pacman in the specified direction
     *
     * @param direction UP(1), DOWN(3), LEFT(2), RIGHT(4), STAY(0)
     * @return String position "x,y" after move
     *
     * ALGORITHM:
     * 1. Check if game is running (status == PLAY)
     * 2. Calculate new position based on direction
     * 3. Handle wraparound if cyclic mode
     * 4. Check if next cell is passable (not WALL)
     * 5. Move Pacman and process cell contents
     * 6. Move all ghosts
     * 7. Check collisions with ghosts
     * 8. Check win condition (all food eaten)
     *
     * SCORING:
     * - Food (FOOD=0): +10 points
     * - Power pellet: +100 points, ghosts become edible
     * - Eat ghost (when edible): +200 points
     *
     * COLLISION:
     * - Touch dangerous ghost (status=1): LOSE (status=3)
     * - Touch edible ghost (status=2): Ghost dies, +200 points
     */
    @Override
    public String move(int direction) {
        // Only accept moves if game is actively playing
        if (this._status != 1) {  // PLAY = 1
            return getPos(0);
        }

        int newX = this._pos.getX();
        int newY = this._pos.getY();

        // ========== DIRECTION MAPPING ==========
        // IMPORTANT: Direction codes match Game interface constants
        // UP (1) → Y increases (move up in grid)
        // DOWN (3) → Y decreases (move down in grid)
        // LEFT (2) → X decreases (move left)
        // RIGHT (4) → X increases (move right)
        // STAY (0) → no movement

        if (direction == PacmanGame.UP) {      // 1
            newY++;
            this._dir = 90;
        } else if (direction == PacmanGame.DOWN) {  // 3
            newY--;
            this._dir = 270;
        } else if (direction == PacmanGame.LEFT) {  // 2
            newX--;
            this._dir = 180;
        } else if (direction == PacmanGame.RIGHT) { // 4
            newX++;
            this._dir = 0;
        }
        // If STAY (0) or invalid: don't change position

        // ========== HANDLE BOARD BOUNDARIES ==========
        if (this._cyclic) {
            // Wrap around edges: use modulo with proper handling of negatives
            newX = ((newX % getWidth()) + getWidth()) % getWidth();
            newY = ((newY % getHeight()) + getHeight()) % getHeight();
        } else {
            // Clamp to board edges: no wrapping
            newX = Math.max(0, Math.min(newX, getWidth() - 1));
            newY = Math.max(0, Math.min(newY, getHeight() - 1));
        }

        // ========== MOVE PACMAN IF PATH IS CLEAR ==========
        if (isValidMove(newX, newY)) {
            int cellValue = this._gameMap[newX][newY];

            // Move Pacman to new position
            this._pos = new PointInt2D(newX, newY);

            // ========== PROCESS CELL CONTENTS ==========
            if (cellValue == PacmanGame.FOOD) {
                // Eating a regular dot
                this._score += 10;
                this._dots--;
                this._gameMap[newX][newY] = PacmanGame.EMPTY;

                // Check if all food eaten (WIN condition)
                if (this._dots == 0) {
                    this._status = 3;  // DONE (WIN)
                }
            } else if (cellValue == 5) {  // Power pellet
                // Eating a power pellet
                this._score += 100;
                this._gameMap[newX][newY] = PacmanGame.EMPTY;

                // Make all ghosts vulnerable for 10 turns
                for (GhostCL ghost : this._ghosts) {
                    ghost.setStatus(2);  // 2 = EDIBLE
                    ghost.setEatableTime(10);
                }
            }
        }

        this._steps++;

        // ========== MOVE ALL GHOSTS ==========
        for (GhostCL ghost : this._ghosts) {
            moveGhost(ghost);
        }

        // ========== CHECK GHOST COLLISIONS ==========
        for (GhostCL ghost : this._ghosts) {
            Pixel2D ghostPos = ghost.getPos(0);

            // Check if Pacman and ghost occupy same cell
            if (ghostPos.getX() == this._pos.getX() &&
                    ghostPos.getY() == this._pos.getY()) {

                if (ghost.getStatus() == 2) {  // Ghost is edible (eaten power)
                    this._kills++;
                    this._score += 200;
                    ghost.setPos(new Index2D(10, 10));  // Reset ghost to start
                    ghost.setStatus(1);  // Back to dangerous
                } else {  // Ghost is dangerous
                    this._status = 3;  // DONE (LOSE)
                }
            }
        }

        return getPos(0);
    }

    /**
     * Move a single ghost with simple random AI
     *
     * GHOST AI ALGORITHM (Simple):
     * 1. Get ghost's current position
     * 2. Pick random direction (0=UP, 1=DOWN, 2=LEFT, 3=RIGHT)
     * 3. Calculate new position
     * 4. Handle board wrapping if cyclic
     * 5. Only move if next cell is not a wall
     *
     * NOTE: This is basic random movement.
     * For advanced AI, you could implement:
     * - Chase behavior (move toward Pacman)
     * - Scatter behavior (go to corner)
     * - Patrol behavior (follow set path)
     *
     * @param ghost The ghost to move
     */
    private void moveGhost(GhostCL ghost) {
        Pixel2D currentPos = ghost.getPos(0);
        int x = currentPos.getX();
        int y = currentPos.getY();

        // Random direction: 0=UP, 1=DOWN, 2=LEFT, 3=RIGHT
        int dir = this._rand.nextInt(4);
        int newX = x;
        int newY = y;

        if (dir == 0) {        // UP
            newY++;
        } else if (dir == 1) { // DOWN
            newY--;
        } else if (dir == 2) { // LEFT
            newX--;
        } else if (dir == 3) { // RIGHT
            newX++;
        }

        // Handle cyclic boundaries (same as Pacman)
        if (this._cyclic) {
            newX = ((newX % getWidth()) + getWidth()) % getWidth();
            newY = ((newY % getHeight()) + getHeight()) % getHeight();
        } else {
            newX = Math.max(0, Math.min(newX, getWidth() - 1));
            newY = Math.max(0, Math.min(newY, getHeight() - 1));
        }

        // Only move if not hitting a wall
        if (isValidMove(newX, newY)) {
            ghost.setPos(new Index2D(newX, newY));
        }
    }

    /**
     * Check if a cell is passable (not a wall)
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return true if cell is passable, false if wall or out of bounds
     */
    private boolean isValidMove(int x, int y) {
        // Check bounds
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
            return false;
        }
        // Check if it's a wall
        int cellValue = this._gameMap[x][y];
        return cellValue != PacmanGame.WALL;  // WALL = 1
    }

    // ==================== QUERY METHODS ====================

    @Override
    public int[][] getGame(int code) {
        return this._gameMap;
    }

    @Override
    public String getPos(int code) {
        return this._pos.toString();  // Returns "x,y"
    }

    @Override
    public GhostCL[] getGhosts(int code) {
        return this._ghosts.toArray(new GhostCL[0]);
    }

    @Override
    public int getStatus() {
        return this._status;  // 0=INIT, 1=PLAY, 2=PAUSE, 3=DONE
    }

    @Override
    public Character getKeyChar() {
        return this._lastKeyChar;
    }

    @Override
    public String getData(int code) {
        double time = getTimeFromStart();
        return String.format("T: %.1f, S: %d, St: %d, K: %d, P: %s, D: %d",
                time, this._score, this._steps, this._kills,
                this._pos.toString(), this._dots);
    }

    @Override
    public boolean isCyclic() {
        return this._cyclic;
    }

    /**
     * End the game and log results
     *
     * @param code unused
     * @return Statistics string formatted as CSV
     */
    @Override
    public String end(int code) {
        this._status = 3;  // DONE
        return log();
    }

    // ==================== HELPER METHODS ====================

    /**
     * Get elapsed time since game started
     *
     * @return Time in seconds (double)
     */
    private double getTimeFromStart() {
        if (this._startTime == 0) return 0;
        long now = System.currentTimeMillis();
        return (now - this._startTime) / 1000.0;
    }

    /**
     * Generate game result log (CSV format)
     *
     * Used to save game statistics to file
     * Format: userID,level,score,dots,steps,timestamp,kills,checksum
     *
     * @return Log string if DONE, empty string otherwise
     */
    private String log() {
        if (this._status == 3) {  // DONE
            long now = System.currentTimeMillis();
            return String.format("%s,%d,%d,%d,%d,%d,%d,%d",
                    this._userID, this._level, this._score,
                    this._dots, this._steps, now, this._kills, now % 3331);
        }
        return "";
    }

    /**
     * Get board width
     * @return Width in cells
     */
    private int getWidth() {
        return this._gameMap.length;
    }

    /**
     * Get board height
     * @return Height in cells
     */
    private int getHeight() {
        return this._gameMap[0].length;
    }
}