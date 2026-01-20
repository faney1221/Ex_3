package exe.ex3.mygame;

import assignments.Ex3.Index2D;
import assignments.Ex3.Pixel2D;

/**
 * GhostCL - Represents a Ghost in the Pacman Game
 *
 * Each ghost has:
 * - Position (x, y coordinates on the board)
 * - Status (DANGEROUS or EDIBLE - eaten power pellet)
 * - Type/Color (1-6 for different ghost types)
 * - Edible Time (how long it stays vulnerable)
 *
 * GHOST STATUS CODES:
 * - 1 = DANGEROUS: Normal ghost, touching Pacman causes LOSE
 * - 2 = EDIBLE: After power pellet eaten, can be eaten by Pacman for points
 *
 * GHOST TYPES:
 * - 1-6: Different ghost colors/personalities (for future AI development)
 *   Type 1: Red (Blinky) - chase Pacman directly
 *   Type 2: Pink (Pinky) - intercept Pacman
 *   Type 3: Blue (Inky) - complex AI
 *   Type 4: Orange (Clyde) - chase or scatter
 *   Type 5-6: Additional ghosts for difficulty
 *
 * LIFECYCLE:
 * 1. Created with initial position and status=1 (dangerous)
 * 2. Moves randomly each game turn
 * 3. When power pellet eaten: status=2 (edible), timer set
 * 4. If Pacman touches while edible: removed, placed back at start
 * 5. If timer expires: status reverts to 1 (dangerous)
 */
public class GhostCL {

    // ==================== INSTANCE VARIABLES ====================

    private Pixel2D _position;          // Current (x, y) position on board
    private int _status;                // 1=DANGEROUS, 2=EDIBLE
    private int _type;                  // 1-6: different ghost personalities
    private int _eatableTime;           // Frames remaining as edible (0 = not edible)

    // ==================== CONSTRUCTORS ====================

    /**
     * Default constructor
     *
     * Creates a ghost at position (11, 11), status DANGEROUS (1), type 1
     *
     * Used when you don't care about specific properties
     */
    public GhostCL() {
        this(11, 11, 1, 1);
    }

    /**
     * Full constructor
     *
     * @param x Starting X coordinate (0 to board width-1)
     * @param y Starting Y coordinate (0 to board height-1)
     * @param status Ghost status: 1=DANGEROUS, 2=EDIBLE
     * @param type Ghost type: 1-6 (different colors/behaviors)
     *
     * EXAMPLE:
     *   GhostCL redGhost = new GhostCL(10, 10, 1, 1);  // Start dangerous
     *   GhostCL eatenGhost = new GhostCL(12, 12, 2, 3); // Already edible
     */
    public GhostCL(int x, int y, int status, int type) {
        this._position = new Index2D(x, y);
        this._status = status;
        this._type = type;
        this._eatableTime = 0;  // Not edible initially
    }

    // ==================== POSITION METHODS ====================

    /**
     * Get the ghost's current position
     *
     * @param code Unused parameter (reserved for future use)
     * @return Pixel2D object with (x, y) coordinates
     *
     * EXAMPLE:
     *   Pixel2D ghostPos = ghost.getPos(0);
     *   int ghostX = ghostPos.getX();
     *   int ghostY = ghostPos.getY();
     *
     *   if (ghostPos.getX() == pacmanPos.getX() &&
     *       ghostPos.getY() == pacmanPos.getY()) {
     *       // Collision detected!
     *   }
     */
    public Pixel2D getPos(int code) {
        return this._position;
    }

    /**
     * Set the ghost's position (move the ghost)
     *
     * @param pos New position as Pixel2D
     * @throws RuntimeException if pos is null
     *
     * IMPORTANT:
     * - This creates a NEW Index2D from the provided position
     * - Used by game engine to move ghost
     * - Used to reset ghost to start position when eaten
     *
     * EXAMPLES:
     *   // Move ghost to random location
     *   ghost.setPos(new Index2D(newX, newY));
     *
     *   // Reset ghost after being eaten
     *   ghost.setPos(new Index2D(10, 10));  // Back to starting position
     */
    public void setPos(Pixel2D pos) {
        if (pos == null) {
            throw new RuntimeException("Position cannot be null");
        }
        // Create new Index2D to avoid shared references
        this._position = new Index2D(pos.getX(), pos.getY());
    }

    // ==================== STATUS METHODS ====================

    /**
     * Get the ghost's current status
     *
     * @return 1 = DANGEROUS (normal ghost)
     *         2 = EDIBLE (can be eaten by Pacman)
     *
     * USAGE:
     *   if (ghost.getStatus() == 1) {
     *       // Touching this ghost = LOSE
     *   } else if (ghost.getStatus() == 2) {
     *       // Touching this ghost = +200 points, ghost dies
     *   }
     */
    public int getStatus() {
        return this._status;
    }

    /**
     * Set the ghost's status
     *
     * @param status 1 = DANGEROUS
     *               2 = EDIBLE (after power pellet eaten)
     *
     * USED BY:
     * - Game engine when power pellet is eaten: setStatus(2)
     * - Game engine when ghost is caught: setStatus(1)
     *
     * EXAMPLE:
     *   // When Pacman eats power pellet:
     *   for (GhostCL ghost : ghosts) {
     *       ghost.setStatus(2);           // All ghosts become edible
     *       ghost.setEatableTime(10);     // For 10 turns
     *   }
     */
    public void setStatus(int status) {
        this._status = status;
    }

    // ==================== TYPE METHODS ====================

    /**
     * Get the ghost's type (color/personality)
     *
     * @return 1-6: Different ghost types
     *   1 = Red ghost (Blinky) - direct chase
     *   2 = Pink ghost (Pinky) - intercept
     *   3 = Blue ghost (Inky) - complex AI
     *   4 = Orange ghost (Clyde) - smart hunter
     *   5-6 = Additional ghosts (random movement for now)
     *
     * Used for:
     * - Rendering (different colors)
     * - AI behavior selection
     * - Future: sophisticated chase algorithms
     */
    public int getType() {
        return this._type;
    }

    /**
     * Set the ghost's type
     *
     * @param type 1-6: Ghost type
     *
     * Less commonly used after initialization
     */
    public void setType(int type) {
        this._type = type;
    }

    // ==================== EDIBLE TIME METHODS ====================

    /**
     * Get remaining time this ghost is edible
     *
     * @param time Unused parameter (reserved for future use)
     * @return Number of game turns remaining as EDIBLE (0 = not edible)
     *
     * BEHAVIOR:
     * - When power pellet eaten: set to ~10 turns
     * - Game decrements each turn
     * - When reaches 0: ghost reverts to DANGEROUS
     *
     * EXAMPLE:
     *   if (ghost.getStatus() == 2) {
     *       int turnsLeft = ghost.remainTimeAsEatable(0);
     *       if (turnsLeft == 0) {
     *           ghost.setStatus(1);  // No longer edible
     *       }
     *   }
     */
    public int remainTimeAsEatable(int time) {
        return this._eatableTime;
    }

    /**
     * Set the edible timer (how long ghost stays vulnerable)
     *
     * @param time Number of game turns to remain edible (typically 10-20)
     *
     * CALLED WHEN:
     * - Power pellet is eaten: setEatableTime(10)
     * - Game turn: setEatableTime(timeLeft - 1) to decrement
     *
     * EXAMPLE:
     *   // Pacman eats power pellet
     *   ghost.setStatus(2);          // EDIBLE
     *   ghost.setEatableTime(10);    // For 10 turns
     *
     *   // Each turn:
     *   if (--turnsLeft <= 0) {
     *       ghost.setStatus(1);      // Back to DANGEROUS
     *   }
     */
    public void setEatableTime(int time) {
        this._eatableTime = time;
    }

    // ==================== UTILITY METHODS ====================

    /**
     * String representation for debugging
     *
     * @return Formatted string showing all ghost properties
     *
     * EXAMPLE OUTPUT:
     *   "Ghost[Type=1, Status=Dangerous, Pos=10,10, EatableTime=0]"
     *   "Ghost[Type=3, Status=Edible, Pos=15,8, EatableTime=5]"
     */
    @Override
    public String toString() {
        String statusStr = (this._status == 1) ? "Dangerous" : "Edible";
        return "Ghost[Type=" + this._type + ", Status=" + statusStr +
                ", Pos=" + this._position + ", EatableTime=" + this._eatableTime + "]";
    }

}