package exe.ex3.mygame;

import exe.ex3.game.Game;
import exe.ex3.game.PacmanGame;
import exe.ex3.game.GhostCL;

/**
 * GameAdapter - Adapter Pattern Implementation
 * 
 * Wraps  Game class (from JAR) and exposes it through
 * PacmanGame interface (from JAR).
 * 
 * This allows :
 * 1. Use professor's game engine (graphics, game loop, etc.)
 * 2. Keep your own package (exe.ex3.mygame)
 * 3. Add custom logic to any method
 * 4. Override behavior without rewriting everything
 * 
 * PATTERN: Adapter Pattern
 * - Client code uses PacmanGame interface
 * - GameAdapter implements PacmanGame
 * - GameAdapter delegates to professor's Game
 * - Can intercept and modify calls
 */
public class GameAdapter implements PacmanGame {
    
    private Game _realGame;        // Professor's game instance
    private int _customMoveCount;  // Example: track your own data
    
    /**
     * Private constructor - use factory method instead
     */
    private GameAdapter(Game realGame) {
        this._realGame = realGame;
        this._customMoveCount = 0;
    }
    
    /**
     * Factory method - creates and initializes the game
     * 
     * This is the main way to create a game with this adapter
     */
    public static GameAdapter create(int level, String userId, 
                                     boolean cyclic, long seed,
                                     double dt, int res, int extra) {
        // Step 1: Create professor's game instance
        Game realGame = new Game();
        
        // Step 2: Initialize it with your parameters
        realGame.init(level, userId, cyclic, seed, dt, res, extra);
        
        // Step 3: Wrap it in adapter and return
        return new GameAdapter(realGame);
    }
    
    // ==================== ADAPTER METHODS ====================
    // These delegate to the professor's game while allowing customization
    
    /**
     * Get the game board (2D int array)
     * Delegates directly - no interception needed
     */
    @Override
    public int[][] getGame(int code) {
        return _realGame.getGame(code);
    }
    
    /**
     * Get pacman position as string "x,y"
     * Delegates directly
     */
    @Override
    public String getPos(int code) {
        return _realGame.getPos(code);
    }
    
    /**
     * Get all ghosts
     * Delegates directly
     */
    @Override
    public GhostCL[] getGhosts(int code) {
        return _realGame.getGhosts(code);
    }
    
    /**
     * Get current game status (0=INIT, 1=PLAY, 2=PAUSE, 3=DONE)
     * Delegates directly
     */
    @Override
    public int getStatus() {
        return _realGame.getStatus();
    }
    
    /**
     * Play/resume the game
     * Delegates directly
     */
    @Override
    public void play() {
        _realGame.play();
    }
    
    /**
     * Move pacman in given direction
     * 
     * THIS IS WHERE YOU CAN ADD YOUR OWN LOGIC!
     * Example: Track moves, validate direction, log events, etc.
     */
    @Override
    public String move(int dir) {
        // ✨ YOUR CUSTOM LOGIC HERE (BEFORE move) ✨
        
        _customMoveCount++;
        
        // Optional: Log every move
        if (_customMoveCount % 10 == 0) {
            System.out.println("[Your Package] Move #" + _customMoveCount);
        }
        
        // Optional: Validate direction
        if (dir != STAY && dir != UP && dir != DOWN && 
            dir != LEFT && dir != RIGHT && dir != ERR) {
            System.err.println("[Your Package] Invalid direction: " + dir);
        }
        
        // ✨ CALL PROFESSOR'S GAME ✨
        String result = _realGame.move(dir);
        
        // ✨ YOUR CUSTOM LOGIC HERE (AFTER move) ✨
        
        // Optional: Check if stuck
        if (_customMoveCount > 1000) {
            System.out.println("[Your Package] Long game - 1000+ moves!");
        }
        
        return result;
    }
    
    /**
     * End the game
     * Delegates directly
     */
    @Override
    public String end(int code) {
        return _realGame.end(code);
    }
    
    /**
     * Get game data string
     * Delegates directly
     */
    @Override
    public String getData(int code) {
        return _realGame.getData(code);
    }
    
    /**
     * Get last key pressed
     * Delegates directly
     */
    @Override
    public Character getKeyChar() {
        return _realGame.getKeyChar();
    }
    
    /**
     * Initialize the game
     * Delegates directly
     */
    @Override
    public String init(int level, String userId, boolean cyclic, 
                      long seed, double dt, int res, int extra) {
        return _realGame.init(level, userId, cyclic, seed, dt, res, extra);
    }
    
    /**
     * Check if map is cyclic (wraps at edges)
     * Delegates directly
     */
    @Override
    public boolean isCyclic() {
        return _realGame.isCyclic();
    }
    
    // ==================== YOUR CUSTOM METHODS ====================
    // Add methods specific to YOUR package
    
    /**
     * Get number of moves made
     * This is YOUR custom data, not from professor's game
     */
    public int getCustomMoveCount() {
        return _customMoveCount;
    }
    
    /**
     * Reset move counter
     */
    public void resetMoveCount() {
        _customMoveCount = 0;
    }
    
    /**
     * Get the underlying professor's game
     * Use carefully - only if you need direct access
     */
    public Game getRealGame() {
        return _realGame;
    }
    
    /**
     * Check if game is running
     * Your custom convenience method
     */
    public boolean isRunning() {
        return getStatus() == PLAY;
    }
    
    /**
     * Check if game is finished
     * Your custom convenience method
     */
    public boolean isFinished() {
        return getStatus() == DONE;
    }
}