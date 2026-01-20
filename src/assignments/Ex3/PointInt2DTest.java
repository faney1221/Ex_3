package assignments.Ex3;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive JUnit test class for PointInt2D
 * Tests all functionality including constructors, getters, distance, equals, hashCode, and toString
 */
@DisplayName("PointInt2D Tests")
public class PointInt2DTest {
    
    private PointInt2D origin;
    private PointInt2D p1, p2, p3, p4;
    
    @BeforeEach
    public void setUp() {
        origin = new PointInt2D(0, 0);
        p1 = new PointInt2D(3, 4);
        p2 = new PointInt2D(3, 4);  // Same as p1
        p3 = new PointInt2D(-5, -12);
        p4 = new PointInt2D(8, 15);
    }
    
    // ==================== CONSTRUCTOR TESTS ====================
    
    @Test
    @DisplayName("Default constructor creates point at (0,0)")
    public void testDefaultConstructor() {
        PointInt2D p = new PointInt2D();
        assertEquals(0, p.getX(), "X should be 0");
        assertEquals(0, p.getY(), "Y should be 0");
    }
    
    @Test
    @DisplayName("Parameterized constructor sets coordinates correctly")
    public void testParameterizedConstructor() {
        PointInt2D p = new PointInt2D(7, 13);
        assertEquals(7, p.getX(), "X should be 7");
        assertEquals(13, p.getY(), "Y should be 13");
    }
    
    @Test
    @DisplayName("Constructor handles negative coordinates")
    public void testNegativeCoordinates() {
        PointInt2D p = new PointInt2D(-10, -20);
        assertEquals(-10, p.getX(), "X should be -10");
        assertEquals(-20, p.getY(), "Y should be -20");
    }
    
    @Test
    @DisplayName("Constructor handles mixed positive/negative coordinates")
    public void testMixedCoordinates() {
        PointInt2D p = new PointInt2D(-5, 10);
        assertEquals(-5, p.getX());
        assertEquals(10, p.getY());
    }
    
    @Test
    @DisplayName("Copy constructor creates exact copy")
    public void testCopyConstructor() {
        PointInt2D original = new PointInt2D(15, 25);
        PointInt2D copy = new PointInt2D(original);
        
        assertEquals(original.getX(), copy.getX(), "X coordinates should match");
        assertEquals(original.getY(), copy.getY(), "Y coordinates should match");
        assertNotSame(original, copy, "Should be different objects");
        assertTrue(copy.equals(original), "Should be equal");
    }
    
    @Test
    @DisplayName("Copy constructor throws exception for null input")
    public void testCopyConstructorWithNull() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            new PointInt2D(null);
        });
        assertTrue(exception.getMessage().contains("null"), 
            "Exception message should mention null");
    }
    
    // ==================== GETTER TESTS ====================
    
    @Test
    @DisplayName("getX returns correct X coordinate")
    public void testGetX() {
        assertEquals(3, p1.getX());
        assertEquals(-5, p3.getX());
        assertEquals(8, p4.getX());
    }
    
    @Test
    @DisplayName("getY returns correct Y coordinate")
    public void testGetY() {
        assertEquals(4, p1.getY());
        assertEquals(-12, p3.getY());
        assertEquals(15, p4.getY());
    }
    
    // ==================== DISTANCE2D TESTS ====================
    
    @Test
    @DisplayName("Distance to same point is zero")
    public void testDistanceToSelf() {
        double distance = p1.distance2D(p1);
        assertEquals(0.0, distance, 0.0001, "Distance to self should be 0");
    }
    
    @Test
    @DisplayName("Distance between identical points is zero")
    public void testDistanceBetweenIdenticalPoints() {
        double distance = p1.distance2D(p2);
        assertEquals(0.0, distance, 0.0001, "Distance between identical points should be 0");
    }
    
    @Test
    @DisplayName("Distance calculation: 3-4-5 Pythagorean triple")
    public void testDistance_345Triangle() {
        double distance = origin.distance2D(p1);
        assertEquals(5.0, distance, 0.0001, 
            "Distance from (0,0) to (3,4) should be 5");
    }
    
    @Test
    @DisplayName("Distance calculation: 5-12-13 Pythagorean triple")
    public void testDistance_51213Triangle() {
        double distance = origin.distance2D(p3);
        assertEquals(13.0, distance, 0.0001, 
            "Distance from (0,0) to (-5,-12) should be 13");
    }
    
    @Test
    @DisplayName("Distance calculation: 8-15-17 Pythagorean triple")
    public void testDistance_81517Triangle() {
        double distance = origin.distance2D(p4);
        assertEquals(17.0, distance, 0.0001, 
            "Distance from (0,0) to (8,15) should be 17");
    }
    
    @Test
    @DisplayName("Horizontal distance calculation")
    public void testHorizontalDistance() {
        PointInt2D a = new PointInt2D(2, 5);
        PointInt2D b = new PointInt2D(10, 5);
        double distance = a.distance2D(b);
        assertEquals(8.0, distance, 0.0001, "Horizontal distance should be 8");
    }
    
    @Test
    @DisplayName("Vertical distance calculation")
    public void testVerticalDistance() {
        PointInt2D a = new PointInt2D(7, 3);
        PointInt2D b = new PointInt2D(7, 15);
        double distance = a.distance2D(b);
        assertEquals(12.0, distance, 0.0001, "Vertical distance should be 12");
    }
    
    @Test
    @DisplayName("Distance is symmetric: d(A,B) = d(B,A)")
    public void testDistanceSymmetry() {
        double d1 = p1.distance2D(p3);
        double d2 = p3.distance2D(p1);
        assertEquals(d1, d2, 0.0001, "Distance should be symmetric");
    }
    
    @Test
    @DisplayName("Distance with negative coordinates")
    public void testDistanceWithNegatives() {
        PointInt2D a = new PointInt2D(-6, -8);
        PointInt2D b = new PointInt2D(0, 0);
        double distance = a.distance2D(b);
        assertEquals(10.0, distance, 0.0001, 
            "Distance from (-6,-8) to (0,0) should be 10");
    }
    
    @Test
    @DisplayName("distance2D throws exception for null input")
    public void testDistanceWithNull() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            origin.distance2D(null);
        });
        assertTrue(exception.getMessage().contains("null"), 
            "Exception message should mention null");
    }
    
    @Test
    @DisplayName("Triangle inequality holds")
    public void testTriangleInequality() {
        PointInt2D a = new PointInt2D(0, 0);
        PointInt2D b = new PointInt2D(3, 4);
        PointInt2D c = new PointInt2D(6, 8);
        
        double ab = a.distance2D(b);
        double bc = b.distance2D(c);
        double ac = a.distance2D(c);
        
        assertTrue(ac <= ab + bc + 0.0001, 
            "Triangle inequality: d(A,C) <= d(A,B) + d(B,C)");
    }
    
    // ==================== DISTANCE WITH INDEX2D COMPATIBILITY ====================
    
    @Test
    @DisplayName("PointInt2D can calculate distance to Index2D")
    public void testDistanceToIndex2D() {
        PointInt2D point = new PointInt2D(0, 0);
        Index2D index = new Index2D(3, 4);
        
        double distance = point.distance2D(index);
        assertEquals(5.0, distance, 0.0001, 
            "Should calculate distance to Index2D correctly");
    }
    
    @Test
    @DisplayName("Index2D can calculate distance to PointInt2D")
    public void testIndex2DDistanceToPoint() {
        Index2D index = new Index2D(0, 0);
        PointInt2D point = new PointInt2D(5, 12);
        
        double distance = index.distance2D(point);
        assertEquals(13.0, distance, 0.0001, 
            "Index2D should calculate distance to PointInt2D correctly");
    }
    
    // ==================== TOSTRING TESTS ====================
    
    @Test
    @DisplayName("toString returns correct format: 'x,y'")
    public void testToStringFormat() {
        assertEquals("0,0", origin.toString());
        assertEquals("3,4", p1.toString());
        assertEquals("-5,-12", p3.toString());
        assertEquals("8,15", p4.toString());
    }
    
    @Test
    @DisplayName("toString handles large numbers")
    public void testToStringLargeNumbers() {
        PointInt2D large = new PointInt2D(123456, 789012);
        assertEquals("123456,789012", large.toString());
    }
    
    @Test
    @DisplayName("toString handles negative numbers")
    public void testToStringNegatives() {
        PointInt2D negative = new PointInt2D(-100, -200);
        assertEquals("-100,-200", negative.toString());
    }
    
    // ==================== EQUALS TESTS ====================
    
    @Test
    @DisplayName("Point equals itself (reflexive)")
    public void testEqualsReflexive() {
        assertTrue(p1.equals(p1), "Point should equal itself");
    }
    
    @Test
    @DisplayName("Equal points with same coordinates (symmetric)")
    public void testEqualsSymmetric() {
        assertTrue(p1.equals(p2), "p1 should equal p2");
        assertTrue(p2.equals(p1), "p2 should equal p1");
    }
    
    @Test
    @DisplayName("Equals is transitive")
    public void testEqualsTransitive() {
        PointInt2D a = new PointInt2D(7, 7);
        PointInt2D b = new PointInt2D(7, 7);
        PointInt2D c = new PointInt2D(7, 7);
        
        assertTrue(a.equals(b), "a equals b");
        assertTrue(b.equals(c), "b equals c");
        assertTrue(a.equals(c), "a should equal c (transitive)");
    }
    
    @Test
    @DisplayName("Different points are not equal")
    public void testNotEquals() {
        assertFalse(p1.equals(p3), "Different points should not be equal");
        assertFalse(origin.equals(p4), "Different points should not be equal");
    }
    
    @Test
    @DisplayName("equals(null) returns false")
    public void testEqualsNull() {
        assertFalse(p1.equals(null), "Point should not equal null");
    }
    
    @Test
    @DisplayName("equals with different object type returns false")
    public void testEqualsDifferentType() {
        assertFalse(p1.equals("3,4"), "Should not equal String");
        assertFalse(p1.equals(Integer.valueOf(3)), "Should not equal Integer");
        assertFalse(p1.equals(new Object()), "Should not equal Object");
    }
    
    @Test
    @DisplayName("PointInt2D equals Index2D with same coordinates")
    public void testEqualsWithIndex2D() {
        PointInt2D point = new PointInt2D(5, 10);
        Index2D index = new Index2D(5, 10);
        
        assertTrue(point.equals(index), 
            "PointInt2D should equal Index2D with same coordinates");
        assertTrue(index.equals(point), 
            "Index2D should equal PointInt2D with same coordinates");
    }
    
    @Test
    @DisplayName("PointInt2D not equals Index2D with different coordinates")
    public void testNotEqualsWithIndex2D() {
        PointInt2D point = new PointInt2D(5, 10);
        Index2D index = new Index2D(5, 11);
        
        assertFalse(point.equals(index), 
            "Should not equal Index2D with different coordinates");
    }
    
    @Test
    @DisplayName("Equals is consistent with distance")
    public void testEqualsConsistentWithDistance() {
        assertTrue(p1.equals(p2), "Points should be equal");
        assertEquals(0.0, p1.distance2D(p2), 0.0001, 
            "Equal points should have distance 0");
    }
    
    // ==================== HASHCODE TESTS ====================
    
    @Test
    @DisplayName("Equal objects have same hashCode")
    public void testHashCodeEquality() {
        assertEquals(p1.hashCode(), p2.hashCode(), 
            "Equal objects must have same hashCode");
    }
    
    @Test
    @DisplayName("hashCode is consistent across multiple calls")
    public void testHashCodeConsistency() {
        int hash1 = p1.hashCode();
        int hash2 = p1.hashCode();
        int hash3 = p1.hashCode();
        
        assertEquals(hash1, hash2, "hashCode should be consistent");
        assertEquals(hash2, hash3, "hashCode should be consistent");
    }
    
    @Test
    @DisplayName("Different objects (usually) have different hashCodes")
    public void testHashCodeUniqueness() {
        assertNotEquals(p1.hashCode(), p3.hashCode(), 
            "Different points should (usually) have different hashCodes");
        assertNotEquals(origin.hashCode(), p4.hashCode(), 
            "Different points should (usually) have different hashCodes");
    }
    
    @Test
    @DisplayName("hashCode calculation is correct")
    public void testHashCodeCalculation() {
        // For (3,4): 31 * 3 + 4 = 93 + 4 = 97
        assertEquals(97, p1.hashCode(), "hashCode should be 31 * x + y");
        
        // For (0,0): 31 * 0 + 0 = 0
        assertEquals(0, origin.hashCode(), "hashCode of (0,0) should be 0");
    }
    
    @Test
    @DisplayName("PointInt2D and Index2D with same coords have same hashCode")
    public void testHashCodeCompatibilityWithIndex2D() {
        PointInt2D point = new PointInt2D(7, 11);
        Index2D index = new Index2D(7, 11);
        
        assertEquals(point.hashCode(), index.hashCode(), 
            "PointInt2D and Index2D with same coordinates should have same hashCode");
    }
    
    // ==================== EDGE CASES ====================
    
    @Test
    @DisplayName("Handles very large coordinates")
    public void testLargeCoordinates() {
        PointInt2D large = new PointInt2D(999999, 888888);
        assertEquals(999999, large.getX());
        assertEquals(888888, large.getY());
        assertEquals("999999,888888", large.toString());
    }
    
    @Test
    @DisplayName("Handles zero coordinates")
    public void testZeroCoordinates() {
        assertEquals(0, origin.getX());
        assertEquals(0, origin.getY());
        assertEquals(0.0, origin.distance2D(origin), 0.0001);
    }
    
    @Test
    @DisplayName("Handles minimum integer values")
    public void testMinIntegerValues() {
        PointInt2D min = new PointInt2D(Integer.MIN_VALUE, Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, min.getX());
        assertEquals(Integer.MIN_VALUE, min.getY());
    }
    
    @Test
    @DisplayName("Handles maximum integer values")
    public void testMaxIntegerValues() {
        PointInt2D max = new PointInt2D(Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, max.getX());
        assertEquals(Integer.MAX_VALUE, max.getY());
    }
    
    // ==================== INTEGRATION TESTS ====================
    
    @Test
    @DisplayName("Complete workflow test")
    public void testCompleteWorkflow() {
        // Create points
        PointInt2D start = new PointInt2D(0, 0);
        PointInt2D end = new PointInt2D(6, 8);
        
        // Calculate distance (6-8-10 triangle)
        double distance = start.distance2D(end);
        assertEquals(10.0, distance, 0.0001, "Distance should be 10");
        
        // Create copy
        PointInt2D endCopy = new PointInt2D(end);
        
        // Verify copy
        assertTrue(end.equals(endCopy), "Copy should be equal");
        assertEquals(end.hashCode(), endCopy.hashCode(), "hashCodes should match");
        assertEquals(end.toString(), endCopy.toString(), "toString should match");
        
        // Distance to copy should be 0
        assertEquals(0.0, end.distance2D(endCopy), 0.0001, 
            "Distance to copy should be 0");
    }
    
    @Test
    @DisplayName("Pacman game scenario - ghost tracking")
    public void testPacmanGameScenario() {
        // Pacman position
        PointInt2D pacman = new PointInt2D(11, 14);
        
        // Ghost positions
        PointInt2D ghost1 = new PointInt2D(10, 14);
        PointInt2D ghost2 = new PointInt2D(15, 18);
        PointInt2D ghost3 = new PointInt2D(8, 10);
        
        // Calculate distances
        double dist1 = pacman.distance2D(ghost1);
        double dist2 = pacman.distance2D(ghost2);
        double dist3 = pacman.distance2D(ghost3);
        
        // Ghost1 is closest (distance = 1)
        assertEquals(1.0, dist1, 0.0001, "Ghost1 should be 1 unit away");
        assertTrue(dist1 < dist2, "Ghost1 should be closer than Ghost2");
        assertTrue(dist1 < dist3, "Ghost1 should be closer than Ghost3");
    }
    
    @Test
    @DisplayName("Interoperability with Index2D")
    public void testInteroperabilityWithIndex2D() {
        PointInt2D point = new PointInt2D(5, 12);
        Index2D index = new Index2D(5, 12);
        
        // They should be equal
        assertTrue(point.equals(index));
        assertTrue(index.equals(point));
        
        // Same hashCode
        assertEquals(point.hashCode(), index.hashCode());
        
        // Same toString
        assertEquals(point.toString(), index.toString());
        
        // Distance between them is 0
        assertEquals(0.0, point.distance2D(index), 0.0001);
        assertEquals(0.0, index.distance2D(point), 0.0001);
    }
}