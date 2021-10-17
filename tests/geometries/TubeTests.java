package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import primitives.*;

/**
 * JUnit5 for geometries.Sphere class
 * @author Deborah Lellouche
 */
class TubeTests {

    /**
     * Test method for {@link geometries.Tube#getNormal(primitives.Point3D)}.
     */
    @Test
    void getNormal() {
        // Test if getNormal(Point3D) return the correct normal
        // WHILE presumption that the Point3D in parameter is at the surface of the tube
        Tube t = new Tube(new Ray(new Point3D(0, 0, 1), new Vector(0, 0, 1)), 1);

        // Test with some point on the surface
        Vector result = t.getNormal(new Point3D(1, 0, 0));
        assertEquals(new Vector(1, 0, 0), result, "ERROR: getNormal(Point3D) does not return the correct normal");

        // Test with a point on the surface at the level of the origin of the ray
        result = t.getNormal(new Point3D(1, 0, 1));
        assertEquals(new Vector(1, 0, 0), result, "ERROR: getNormal(Point3D) does not return the correct normal");

    }
}