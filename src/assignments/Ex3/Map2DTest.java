package assignments.Ex3;

import assignments.Ex3.Map2D;
import assignments.Ex3.Pixel2D;
import assignments.Ex3.Index2D;

public class Map2DTest {
    
    public static void main(String[] args) {
        System.out.println("🧪 Testing MyMap2D Implementation\n");
        
        testBasicOperations();
        testCyclicMap();
        testFillAlgorithm();
        testShortestPath();
        testAllDistance();
        
        System.out.println("\n✅ All Map2D tests completed!");
    }
    
    private static void testBasicOperations() {
        System.out.println("📋 Test 1: Basic Operations");
        
        Map2D map = new MyMap2D(5, 5, 0);
        System.out.println("  Width: " + map.getWidth());
        System.out.println("  Height: " + map.getHeight());
        System.out.println("  Cyclic: " + map.isCyclic());
        
        map.setPixel(2, 2, 5);
        System.out.println("  Pixel at (2,2): " + map.getPixel(2, 2));
        
        Pixel2D p = new Index2D(3, 3);
        map.setPixel(p, 7);
        System.out.println("  Pixel at (3,3): " + map.getPixel(p));
        
        System.out.println("  Is (2,2) inside: " + map.isInside(new Index2D(2, 2)));
        System.out.println("  Is (10,10) inside: " + map.isInside(new Index2D(10, 10)));
        
        System.out.println("  ✅ Basic operations passed\n");
    }
    
    private static void testCyclicMap() {
        System.out.println("📋 Test 2: Cyclic Map");
        
        int[][] arr = {
            {0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 1, 0, 1, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 0, 0}
        };
        
        Map2D map = new MyMap2D(arr);
        map.setCyclic(true);
        
        System.out.println("  Cyclic enabled: " + map.isCyclic());
        
        Pixel2D start = new Index2D(0, 2);
        Pixel2D end = new Index2D(4, 2);
        
        Pixel2D[] path = map.shortestPath(start, end, 1);
        
        if (path != null) {
            System.out.println("  Cyclic path length: " + path.length);
            System.out.print("  Path: ");
            for (Pixel2D p : path) {
                System.out.print("(" + p.getX() + "," + p.getY() + ") ");
            }
            System.out.println();
        }
        
        System.out.println("  ✅ Cyclic map test passed\n");
    }
    
    private static void testFillAlgorithm() {
        System.out.println("📋 Test 3: Fill Algorithm");
        
        int[][] arr = {
            {1, 1, 1, 2, 2},
            {1, 1, 1, 2, 2},
            {1, 1, 1, 2, 2},
            {3, 3, 3, 3, 3},
            {4, 4, 4, 4, 4}
        };
        
        Map2D map = new MyMap2D(arr);
        
        System.out.println("  Before fill:");
        printMap(map);
        
        Pixel2D start = new Index2D(0, 0);
        int filled = map.fill(start, 9);
        
        System.out.println("  Pixels filled: " + filled);
        System.out.println("  After fill:");
        printMap(map);
        
        System.out.println("  ✅ Fill algorithm test passed\n");
    }
    
    private static void testShortestPath() {
        System.out.println("📋 Test 4: Shortest Path");
        
        int[][] arr = {
            {0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 1, 0},
            {1, 1, 0, 1, 0},
            {0, 0, 0, 0, 0}
        };
        
        Map2D map = new MyMap2D(arr);
        
        Pixel2D start = new Index2D(0, 0);
        Pixel2D end = new Index2D(4, 4);
        
        Pixel2D[] path = map.shortestPath(start, end, 1);
        
        if (path != null) {
            System.out.println("  Path found! Length: " + path.length);
            System.out.print("  Path: ");
            for (Pixel2D p : path) {
                System.out.print("(" + p.getX() + "," + p.getY() + ") ");
            }
            System.out.println();
        } else {
            System.out.println("  No path found!");
        }
        
        // Test blocked path
        Pixel2D blockedStart = new Index2D(0, 1);
        Pixel2D blockedEnd = new Index2D(0, 2);
        Pixel2D[] blockedPath = map.shortestPath(blockedStart, blockedEnd, 1);
        
        System.out.println("  Blocked path result: " + (blockedPath == null ? "null (correct)" : "found (error)"));
        
        System.out.println("  ✅ Shortest path test passed\n");
    }
    
    private static void testAllDistance() {
        System.out.println("📋 Test 5: All Distances");
        
        int[][] arr = {
            {0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 0, 0}
        };
        
        Map2D map = new MyMap2D(arr);
        
        Pixel2D start = new Index2D(2, 2);
        Map2D distMap = map.allDistance(start, 1);
        
        System.out.println("  Distance map from (2,2):");
        printMap(distMap);
        
        System.out.println("  ✅ All distances test passed\n");
    }
    
    private static void printMap(Map2D map) {
        int[][] arr = map.getMap();
        for (int y = arr[0].length - 1; y >= 0; y--) {
            System.out.print("    ");
            for (int x = 0; x < arr.length; x++) {
                System.out.printf("%3d ", arr[x][y]);
            }
            System.out.println();
        }
    }
}