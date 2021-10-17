package primitives;

import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;

class Point3DTests {

    Point3D p1 = new Point3D(0.0d, 0.5d, 1.0d);
    Point3D p2 = new Point3D(0.0000000000001d, 0.4999999999999999999999999d, 1d);
    Point3D p3 = new Point3D(0, 0.52, 2.4997);

    /**
     * Test method for {@link primitives.Point3D#equals(java.lang.Object)}.
     */
    @Test
    void testEquals() {
        assertEquals(p1, p2);
    }

    /**
     * Test method for {@link primitives.Point3D#toString()}.
     */
    @Test
    void testToString() {
        System.out.println("the first point is : " + p1);
        System.out.println("the second point is : " + p2);
    }

    /**
     * Test method for {@link primitives.Point3D#distance(primitives.Point3D)}.
     */
    @Test
    void testDistance() {
        double result = p3.distance(p2);
        assertEquals(1.5, result, 0.001);
    }

    /**
     * Test method for {@link primitives.Point3D#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        Point3D p1 = new Point3D(1, 2, 3);
        Point3D result = p1.add(new Vector(-1, -2, -3));
        assertEquals(Point3D.ZERO, result, "ERROR: Point + Vector does not work correctly");

    }

    /**
     * Test method for {@link primitives.Point3D#subtract(primitives.Point3D)}.
     */
    @Test
    void testSubtract() {
        Point3D p1 = new Point3D(1, 2, 3);
        Point3D p4 = new Point3D(2, 3, 4);
        Vector result = p4.subtract(p1);
        assertEquals(new Vector(1, 1, 1), result, "ERROR: Point - Point does not work correctly");

    }

}