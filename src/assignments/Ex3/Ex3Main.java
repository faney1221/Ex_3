package assignments.Ex3;

import exe.ex3.game.Game;
import exe.ex3.mygame.PacManAlgo;
import exe.ex3.game.PacmanGame;

/**
 * Ex3, School of Computer Science, Ariel University.
 *
 * This is the "main" class for Ex3.
 * * REPRASENATION:
 * This class serves as the entry point for the Pacman application.
 * It initializes the MyGame engine using the parameters from GameInfo and
 * manages the game loop. The loop handles user input (starting/pausing)
 * and delegating movement decisions to the provided PacManAlgo implementation.
 */
public class Ex3Main {
    private static Character _cmd;

    public static void main(String[] args) {
        play1();
    }

    public static void play1() {
        // Use the Game interface from the professor's package
        Game ex3 = new Game();

        // Initialize the game using the MyGame specific init method
        ex3.init(GameInfo.CASE_SCENARIO, GameInfo.MY_ID, GameInfo.CYCLIC_MODE,
                GameInfo.RANDOM_SEED, GameInfo.RESOLUTION_NORM, GameInfo.DT, -1);

        ex3.play();

        PacManAlgo man = GameInfo.ALGO;

        // Loop until the game status is DONE
        while(ex3.getStatus() != PacmanGame.DONE) {
            _cmd =  ex3.getKeyChar();

            // Handle Start/Pause with Space
            if(_cmd != null && _cmd == ' ') {
                ( ex3).play();
            }

            // Handle Help menu
            if (_cmd != null && _cmd == 'h') {
                System.out.println("Pacman help: keys: ' '-start, 'w,a,x,d'-directions, all other parameters should be configured via GameInfo.java");
            }

            /* * FIXED: Cast to (Game) instead of (PacmanGame).
             * MyGame implements Game, but it does NOT inherit from PacmanGame.
             * This change prevents the ClassCastException.
             */
            int dir = man.move((Game) ex3);

            ex3.move(dir);
        }

        ( ex3).end(-1);
    }

    public static Character getCMD() {
        return _cmd;
    }
}