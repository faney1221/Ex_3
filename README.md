C:\Users\faney\.jdks\openjdk-21.0.2\bin\java.exe --enable-preview "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.4\lib\idea_rt.jar=64426" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath "C:\Users\faney\IdeaProjects\Ex 3\out\production\Ex 3;C:\Users\faney\IdeaProjects\Ex 3\libs\Ex3_v09e.jar-out.jar;C:\Users\faney\.m2\repository\junit\junit\4.13.1\junit-4.13.1.jar;C:\Users\faney\.m2\repository\org\hamcrest\hamcrest-core\1.3\hamcrest-core-1.3.jar;C:\Users\faney\.m2\repository\org\junit\jupiter\junit-jupiter\5.14.0\junit-jupiter-5.14.0.jar;C:\Users\faney\.m2\repository\org\junit\jupiter\junit-jupiter-api\5.14.0\junit-jupiter-api-5.14.0.jar;C:\Users\faney\.m2\repository\org\opentest4j\opentest4j\1.3.0\opentest4j-1.3.0.jar;C:\Users\faney\.m2\repository\org\junit\platform\junit-platform-commons\1.14.0\junit-platform-commons-1.14.0.jar;C:\Users\faney\.m2\repository\org\apiguardian\apiguardian-api\1.1.2\apiguardian-api-1.1.2.jar;C:\Users\faney\.m2\repository\org\junit\jupiter\junit-jupiter-params\5.14.0\junit-jupiter-params-5.14.0.jar;C:\Users\faney\.m2\repository\org\junit\jupiter\junit-jupiter-engine\5.14.0\junit-jupiter-engine-5.14.0.jar;C:\Users\faney\.m2\repository\org\junit\platform\junit-platform-engine\1.14.0\junit-platform-engine-1.14.0.jar" assignments.Ex3.Ex3Main

345060354, 1, 167, 225, 191, 1768941044436, 0, -915428924, 1304617922096this is my result to the game
Ex3 – Pacman Smart Algorithm (Shortest Path)



This project implements a smart Pacman algorithm that uses the shortest path (BFS) to reach the nearest food while correctly handling walls and dead-ends.

Project Structure
src/
 ├─ Ex3Main.java        // Given – DO NOT CHANGE
 ├─ Ex3Algo.java        // Your smart Pacman algorithm
 ├─ Map.java            // Map implementation
 ├─ Index2D.java        // 2D index helper class
 └─ (JUnit tests)       // Optional but recommended

🎯 Goal of the Algorithm

Automatically control Pacman

Find the shortest path to the nearest food

Avoid walls (-1)

Never get stuck in corridors or corners

Work correctly on all 5 game levels (0–4)

Algorithm Overview

The Ex3Algo class implements PacManAlgo and uses:

Breadth-First Search (BFS) on the grid

Treats the board as a graph

Stops BFS when the closest food is found

Backtracks to determine the first move only





✅ Wall-safe movement
