package assignments.Ex3;

import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

import java.awt.*;

/**
 * This is the major algorithmic class for Ex3 - the PacMan game:
 *
 * This code is a very simple example (random-walk algorithm).
 * Your task is to implement (here) your PacMan algorithm.
 */
public class Ex3Algo implements PacManAlgo{
	private int _count;
	public Ex3Algo() {_count=0;}
	@Override
	/**
	 *  Add a short description for the algorithm as a String.
	 */
	public String getInfo() {
		return null;
	}
	@Override
	/**
	 * This ia the main method - that you should design, implement and test.
	 */
	public int move(PacmanGame game) {
		if(_count==0 || _count==300) {
			int code = 0;
			int[][] board = game.getGame(0);
			printBoard(board);
			int blue = Game.getIntColor(Color.BLUE, code);
			int pink = Game.getIntColor(Color.PINK, code);
			int black = Game.getIntColor(Color.BLACK, code);
			int green = Game.getIntColor(Color.GREEN, code);
			System.out.println("Blue=" + blue + ", Pink=" + pink + ", Black=" + black + ", Green=" + green);
			String pos = game.getPos(code).toString();
			System.out.println("Pacman coordinate: "+pos);
			GhostCL[] ghosts = game.getGhosts(code);
			printGhosts(ghosts);
			int up = Game.UP, left = Game.LEFT, down = Game.DOWN, right = Game.RIGHT;
		}
		_count++;
		int dir = randomDir();
		return dir;
	}
	private static void printBoard(int[][] b) {
		for(int y =0;y<b[0].length;y++){
			for(int x =0;x<b.length;x++){
				int v = b[x][y];
				System.out.print(v+"\t");
			}
			System.out.println();
		}
	}
	private static void printGhosts(GhostCL[] gs) {
		for(int i=0;i<gs.length;i++){
			GhostCL g = gs[i];
			System.out.println(i+") status: "+g.getStatus()+",  type: "+g.getType()+",  pos: "+g.getPos(0)+",  time: "+g.remainTimeAsEatable(0));
		}
	}
	private static int randomDir() {
		int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
		int ind = (int)(Math.random()*dirs.length);
		return dirs[ind];
	}

    /**
     * This class represents a 2D map as a "screen" or a raster matrix or maze over integers.
     * @author boaz.benmoshe
     *
     */
    public static class Map implements Map2D {
        private int[][] _map;
        private boolean _cyclicFlag = true;

        /**
         * Constructs a w*h 2D raster map with an init value v.
         * @param w
         * @param h
         * @param v
         */
        public Map(int w, int h, int v) {init(w,h, v);}
        /**
         * Constructs a square map (size*size).
         * @param size
         */
        public Map(int size) {this(size,size, 0);}

        /**
         * Constructs a map from a given 2D array.
         * @param data
         */
        public Map(int[][] data) {
            init(data);
        }
        @Override
        public void init(int w, int h, int v) {
            /////// add your code below ///////

            ///////////////////////////////////
        }
        @Override
        public void init(int[][] arr) {
            /////// add your code below ///////

            ///////////////////////////////////
        }
        @Override
        public int[][] getMap() {
            int[][] ans = null;
            /////// add your code below ///////

            ///////////////////////////////////
            return ans;
        }
        @Override
        /////// add your code below ///////
        public int getWidth() {return 0;}
        @Override
        /////// add your code below ///////
        public int getHeight() {return 0;}
        @Override
        /////// add your code below ///////
        public int getPixel(int x, int y) { return 0;}
        @Override
        /////// add your code below ///////
        public int getPixel(Pixel2D p) {
            return this.getPixel(p.getX(),p.getY());
        }
        @Override
        /////// add your code below ///////
        public void setPixel(int x, int y, int v) {;}
        @Override
        /////// add your code below ///////
        public void setPixel(Pixel2D p, int v) {
            ;
        }
        @Override
        /**
         * Fills this map with the new color (new_v) starting from p.
         * https://en.wikipedia.org/wiki/Flood_fill
         */
        public int fill(Pixel2D xy, int new_v) {
            int ans=0;
            /////// add your code below ///////

            ///////////////////////////////////
            return ans;
        }

        @Override
        /**
         * BFS like shortest the computation based on iterative raster implementation of BFS, see:
         * https://en.wikipedia.org/wiki/Breadth-first_search
         */
        public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
            Pixel2D[] ans = null;  // the result.
            /////// add your code below ///////

            ///////////////////////////////////
            return ans;
        }
        @Override
        /////// add your code below ///////
        public boolean isInside(Pixel2D p) {
            return false;
        }

        @Override
        /////// add your code below ///////
        public boolean isCyclic() {
            return false;
        }
        @Override
        /////// add your code below ///////
        public void setCyclic(boolean cy) {;}
        @Override
        /////// add your code below ///////
        public Map2D allDistance(Pixel2D start, int obsColor) {
            Map2D ans = null;  // the result.
            /////// add your code below ///////

            ///////////////////////////////////
            return ans;
        }
    }
}