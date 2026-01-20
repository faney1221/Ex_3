package assignments.Ex3;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test class for Index2D
 * Tests all functionality of the Index2D implementation
 */
public class Index2DTest {
    
    private Index2D p1, p2, p3, p4;
    
    @BeforeEach
    public void setUp() {
        p1 = new Index2D(0, 0);
        p2 = new Index2D(3, 4);
        p3 = new Index2D(3, 4);
        p4 = new Index2D(-5, -12);
    }
    
    // ========== Constructor Tests ==========
    
    @Test
    public void testDefaultConstructor() {
        Index2D p = new Index2D();
        assertEquals(0, p.getX(), "Default constructor should create point at (0,0)");
        assertEquals(0, p.getY(), "Default constructor should create point at (0,0)");
    }
    
    @Test
    public void testParameterizedConstructor() {
        Index2D p = new Index2D(5, 10);
        assertEquals(5, p.getX(), "X coordinate should be 5");
        assertEquals(10, p.getY(), "Y coordinate should be 10");
    }
    
    @Test
    public void testNegativeCoordinates() {
        Index2D p = new Index2D(-3, -7);
        assertEquals(-3, p.getX(), "Should handle negative X");
        assertEquals(-7, p.getY(), "Should handle negative Y");
    }
    
    @Test
    public void testCopyConstructor() {
        Index2D original = new Index2D(8, 15);
        Index2D copy = new Index2D(original);
        
        assertEquals(original.getX(), copy.getX(), "Copy should have same X");
        assertEquals(original.getY(), copy.getY(), "Copy should have same Y");
        assertNotSame(original, copy, "Copy should be a different object");
    }
    
    @Test
    public void testCopyConstructorWithNull() {
        assertThrows(RuntimeException.class, () -> {
            // Tell Java this is a Pixel2D null, not a String null
            new Index2D((Pixel2D) null);
        }, "Copy constructor should throw exception for null input");
    }
    
    // ========== Getter Tests ==========
    
    @Test
    public void testGetX() {
        assertEquals(3, p2.getX(), "getX() should return 3");
        assertEquals(-5, p4.getX(), "getX() should return -5");
    }
    
    @Test
    public void testGetY() {
        assertEquals(4, p2.getY(), "getY() should return 4");
        assertEquals(-12, p4.getY(), "getY() should return -12");
    }
    
    // ========== Distance Tests ==========
    
    @Test
    public void testDistance2D_SamePoint() {
        double distance = p2.distance2D(p3);
        assertEquals(0.0, distance, 0.0001, 
            "Distance between identical points should be 0");
    }
    
    @Test
    public void testDistance2D_345Triangle() {
        // 3-4-5 triangle
        double distance = p1.distance2D(p2);
        assertEquals(5.0, distance, 0.0001, 
            "Distance from (0,0) to (3,4) should be 5");
    }
    
    @Test
    public void testDistance2D_Negative() {
        // Distance from (0,0) to (-5,-12) should be 13 (5-12-13 triangle)
        double distance = p1.distance2D(p4);
        assertEquals(13.0, distance, 0.0001, 
            "Distance from (0,0) to (-5,-12) should be 13");
    }
    
    @Test
    public void testDistance2D_Horizontal() {
        Index2D a = new Index2D(0, 0);
        Index2D b = new Index2D(10, 0);
        double distance = a.distance2D(b);
        assertEquals(10.0, distance, 0.0001, 
            "Horizontal distance should be 10");
    }
    
    @Test
    public void testDistance2D_Vertical() {
        Index2D a = new Index2D(0, 0);
        Index2D b = new Index2D(0, 7);
        double distance = a.distance2D(b);
        assertEquals(7.0, distance, 0.0001, 
            "Vertical distance should be 7");
    }
    
    @Test
    public void testDistance2D_Symmetric() {
        double d1 = p2.distance2D(p4);
        double d2 = p4.distance2D(p2);
        assertEquals(d1, d2, 0.0001, 
            "Distance should be symmetric: d(A,B) = d(B,A)");
    }
    
    @Test
    public void testDistance2D_WithNull() {
        assertThrows(RuntimeException.class, () -> {
            p1.distance2D(null);
        }, "distance2D should throw exception for null input");
    }
    
    // ========== ToString Tests ==========
    
    @Test
    public void testToString() {
        assertEquals("0,0", p1.toString(), 
            "toString should return 'x,y' format");
        assertEquals("3,4", p2.toString(), 
            "toString should return 'x,y' format");
        assertEquals("-5,-12", p4.toString(), 
            "toString should handle negative coordinates");
    }
    
    // ========== Equals Tests ==========
    
    @Test
    public void testEquals_SameObject() {
        assertTrue(p2.equals(p2), 
            "Point should equal itself");
    }
    
    @Test
    public void testEquals_EqualPoints() {
        assertTrue(p2.equals(p3), 
            "Points with same coordinates should be equal");
        assertTrue(p3.equals(p2), 
            "Equality should be symmetric");
    }
    
    @Test
    public void testEquals_DifferentPoints() {
        assertFalse(p1.equals(p2), 
            "Points with different coordinates should not be equal");
        assertFalse(p2.equals(p4), 
            "Points with different coordinates should not be equal");
    }
    
    @Test
    public void testEquals_WithNull() {
        assertFalse(p1.equals(null), 
            "Point should not equal null");
    }
    
    @Test
    public void testEquals_WithDifferentType() {
        assertFalse(p1.equals("not a pixel"), 
            "Point should not equal non-Pixel2D object");
        assertFalse(p1.equals(Integer.valueOf(5)), 
            "Point should not equal Integer");
    }
    
    @Test
    public void testEquals_Consistency() {
        // Test that equals is consistent with distance2D
        assertTrue(p2.equals(p3), 
            "Equal points should have distance 0");
        assertEquals(0.0, p2.distance2D(p3), 0.0001, 
            "Equal points should have distance 0");
    }
    
    // ========== HashCode Tests ==========
    
    @Test
    public void testHashCode_EqualObjects() {
        assertEquals(p2.hashCode(), p3.hashCode(), 
            "Equal objects should have same hashCode");
    }
    
    @Test
    public void testHashCode_DifferentObjects() {
        assertNotEquals(p1.hashCode(), p2.hashCode(), 
            "Different objects should (usually) have different hashCodes");
    }
    
    @Test
    public void testHashCode_Consistency() {
        int hash1 = p2.hashCode();
        int hash2 = p2.hashCode();
        assertEquals(hash1, hash2, 
            "hashCode should be consistent across calls");
    }
    
    // ========== Edge Cases ==========
    
    @Test
    public void testLargeCoordinates() {
        Index2D large = new Index2D(1000000, 2000000);
        assertEquals(1000000, large.getX());
        assertEquals(2000000, large.getY());
    }
    
    @Test
    public void testZeroCoordinates() {
        Index2D zero = new Index2D(0, 0);
        assertEquals(0, zero.getX());
        assertEquals(0, zero.getY());
        assertEquals(0.0, zero.distance2D(zero), 0.0001);
    }
    
    @Test
    public void testMixedCoordinates() {
        Index2D mixed = new Index2D(-10, 20);
        assertEquals(-10, mixed.getX());
        assertEquals(20, mixed.getY());
    }
    
    // ========== Integration Tests ==========
    
    @Test
    public void testCompleteWorkflow() {
        // Create points
        Index2D start = new Index2D(0, 0);
        Index2D end = new Index2D(6, 8);
        
        // Calculate distance (should be 10 - Pythagorean triple 6-8-10)
        double distance = start.distance2D(end);
        assertEquals(10.0, distance, 0.0001);
        
        // Create copy
        Index2D copy = new Index2D(end);
        
        // Verify copy
        assertTrue(end.equals(copy));
        assertEquals(end.hashCode(), copy.hashCode());
        assertEquals(end.toString(), copy.toString());
        
        // Verify distance to copy is 0
        assertEquals(0.0, end.distance2D(copy), 0.0001);
    }
    
    @Test
    public void testTriangleInequality() {
        // For any three points A, B, C: d(A,C) <= d(A,B) + d(B,C)
        Index2D a = new Index2D(0, 0);
        Index2D b = new Index2D(3, 4);
        Index2D c = new Index2D(6, 8);
        
        double ab = a.distance2D(b);
        double bc = b.distance2D(c);
        double ac = a.distance2D(c);
        
        assertTrue(ac <= ab + bc + 0.0001, 
            "Triangle inequality should hold");
    }
}