package primitives;

import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * JUnit5 for primitives.Vector class
 * @author Deborah Lellouche
 */
class VectorTests {

    Vector v1 = new Vector(1, 2, 3);
    Vector v2 = new Vector(-2, -4, -6);

    /**
     * Test method for {@link primitives.Vector#Vector(primitives.Point3D)}.
     */
    @Test
    void testZeroPoint() {

        // Test if Vector constructor throws an exception while trying to build a vector
        // from Point3D (0, 0, 0)
        try {
            Point3D p0 = new Point3D(0, 0, 0);
            new Vector(p0);
            out.println("ERROR: zero vector does not throw an exception");
        } catch (IllegalArgumentException e) {
            out.println("Good : Vector 0 not made.");
        }

    }

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        Vector result = v1.add(v2);

        // Test if add() returns a vector with the correct values
        assertEquals(new Vector(-1, -2, -3), result);
    }

    /**
     * Test method for {@link primitives.Vector#subtract(primitives.Vector)}.
     */
    @Test
    void testSubtract() {
        Vector result = v1.subtract(v2);

        // Test if subtract() returns a vector with the correct values
        assertEquals(new Vector(3, 6, 9), result);
    }

    /**
     * Test method for {@link primitives.Vector#scale(double))}.
     */
    @Test
    void testScale() {
        Vector result = v1.scale(-1);

        // Test if scale() returns a vector with the correct values
        assertEquals(new Vector(-1, -2, -3), result);
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(Vector))}.
     */
    @Test
    void testDotProduct() {
        Vector v3 = new Vector(0, 3, -2);
        Vector v4 = new Vector(-2, -4, -6);

        // ============ BVA Tests ============
        // Test dotProduct() result for orthogonal vectors
        double result = alignZero(v1.dotProduct(v3));
        assertEquals(0, result, "ERROR: dotProduct() for orthogonal vectors is not zero");

        // ============ EP Tests ============
        // Test dotProduct() result for other vectors
        double result2 = alignZero(v1.dotProduct(v4) + 28);
        assertEquals(0, result2, "ERROR: dotProduct() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}
     */
    @Test
    void testCrossProduct() {

        // ========== Equivalence Partitions Tests ============
        Vector v3 = new Vector(0, 3, -2);
        Vector vr = v1.crossProduct(v3);

        // Test that length of cross-product is proper (orthogonal vectors taken for simplicity)
        assertEquals(v1.length() * v3.length(), vr.length(), 0.00001, "crossProduct() wrong result length");

        // Test cross-product result orthogonality to its operands
        assertTrue(isZero(vr.dotProduct(v1)), "crossProduct() result is not orthogonal to its 1st operand");
        assertTrue(isZero(vr.dotProduct(v3)), "crossProduct() result is not orthogonal to its 2nd operand");

        // ============ Boundary Values Tests ============
        // Test zero vector from cross-product of co-lined vectors
        try {
            v1.crossProduct(v2);
            fail("crossProduct() for parallel vectors does not throw an exception");
            // Vector constructor must throw an exception if receives point (0,0,0)
            // and cross-product result of co-lined vectors is (0,0,0)
        }
        catch (Exception e) {}

    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}
     */
    @Test
    void testLengthSquared() {

        // Test if lengthSquared() returns the correct value
        double result = alignZero(v1.lengthSquared() - 14);
        assertEquals(0, result, "ERROR: lengthSquared() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#length()}
     */
    @Test
    void testLength() {

        // Test if length() returns the correct value
        double result = new Vector(0, 3, 4).length();
        assertEquals(5, result, "ERROR: length() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#normalize()}
     */
    @Test
    void testNormalize() {
        Vector v1Copy = new Vector(v1.getHead());
        Vector v1CopyNormalize = v1Copy.normalize();

        // Test if normalize() function update the original vector or makes a new one
        assertEquals(v1Copy, v1CopyNormalize, "ERROR: normalize() function creates a new vector");

        // Test if normalize() function makes an unit vector
        assertEquals(1, v1CopyNormalize.length(), "ERROR: normalize() result is not a unit vector");

    }

    @Test
    void testNormalized() {

        // Test if normalized() makes a new vector with the correct values
        Vector v = new Vector(1, 2, 3);
        Vector u = v.normalized();
        v.normalize();
        if (u == v)
            fail("ERROR: normalized() function does not create a new vector");
        if (u.equals(v))
            out.println("Good : normalize() make a new vector with the values " +
                    "of the original vector after normalisation");
    }
}