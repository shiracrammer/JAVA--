package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import primitives.*;

import java.util.List;

/**
 * JUnit5 for geometries.Plane class
 * @author Deborah Lellouche
 */
class PlaneTests {

    /**
     * Test method for {@link geometries.Plane#getNormal(primitives.Point3D)}.
     */
    @Test
    void testGetNormal() {
        // Testing for the two constructors
        // WHILE presumption that the Point3D in parameter is on the plane
        Plane p1 = new Plane(new Point3D(1, 0, 0), new Vector(1, 1, 1));
        Plane p2 = new Plane(new Point3D(2, 0, 0), new Point3D(0, 2, 0), new Point3D(0, 0, 2));
        Vector result1 = p1.getNormal(new Point3D(0, 0, 0));
        Vector result2 = p2.getNormal(new Point3D(0, 0, 0));

        // Test if getNormal(Point3D) returns a normal with the good direction
        try {
            result1.crossProduct(new Vector(1, 1, 1));
            result2.crossProduct(new Vector(1, 1, 1));
            fail("ERROR : getNormal(Point3D) does not return a normal with the good direction");
            // Vector constructor already tested for Point3D (0, 0, 0)
        } catch (IllegalArgumentException e) {}

        // Test if getNormal(Point3D) returns an unit vector
        assertEquals(result1.normalized(), result1, "ERROR : getNormal(Point3D) does not return an unit vector");
        assertEquals(result2.normalized(), result2, "ERROR : getNormal(Point3D) does not return an unit vector");
    }


    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Plane plane = new Plane(new Point3D(0, 1, 0), new Vector(0, 1, 0));

        // ============ Equivalence Partitions Tests ==============
        // The ray must be neither orthogonal nor parallel the plane
        // TC01 : Ray intersects the plane (1 point)
        Point3D p1 = new Point3D(2, 1, 0);
        List<Point3D> result = plane.findIntersections(
                new Ray(new Point3D(1, 0, 0), new Vector(1, 1, 0)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p1), result, "Ray intersects the plane");

        // TC02 : Ray does not intersect the plane (0 point)
        result = plane.findIntersections(
                new Ray(new Point3D(3, 2, 0), new Vector(1, 1, 0)));
        assertEquals(null, result, "Wrong number of points; Ray does not intersect the plane");


        // =============== Boundary Values Tests ==================

        // **** Group : Ray is parallel to the plane
        // TC11: Ray included in the plane (0 point, or an infinity ?)
        result = plane.findIntersections(
                new Ray(new Point3D(1, 1, 0), new Vector(1, 0, 0)));
        assertEquals(null, result, "Wrong number of points; Ray parallel and included in the plane");

        // TC12: Ray not included in the plane (0 point)
        result = plane.findIntersections(
                new Ray(new Point3D(1, 2, 0), new Vector(1, 0, 0)));
        assertEquals(null, result, "Wrong number of points; Ray parallel and not included in the plane");


        // **** Group : Ray is orthogonal to the plane
        // TC13 : Ray starts before the plane (1 point)
        p1 = new Point3D(1, 1, 0);
        result = plane.findIntersections(
                new Ray(new Point3D(1, 0, 0), new Vector(0, 1, 0)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p1), result, "Ray orthogonal and starts before the plane");

        // TC14 : Ray starts in the plane (0 point)
        result = plane.findIntersections(
                new Ray(new Point3D(1, 1, 0), new Vector(0, 1, 0)));
        assertEquals(null, result, "Wrong number of points; Ray orthogonal and starts in the plane");

        // TC15 : Ray starts after the plane (0 point)
        result = plane.findIntersections(
                new Ray(new Point3D(1, 2, 0), new Vector(0, 1, 0)));
        assertEquals(null, result, "Wrong number of points; Ray orthogonal and starts after the plane");


        // **** Group : Special cases
        // TC16 : Ray begins at the plane, but neither parallel nor orthogonal to the plane
        result = plane.findIntersections(
                new Ray(new Point3D(1, 1, 0), new Vector(1, 1, 0)));
        assertEquals(null, result,
                "Wrong number of points; Ray begins at the plane, and not parallel nor orthogonal");

        // TC17 : Ray begins at the same point which appears as reference point in the plane,
        // but neither parallel nor orthogonal to the plane
        result = plane.findIntersections(
                new Ray(new Point3D(0, 1, 0), new Vector(1, 1, 0)));
        assertEquals(null, result, "Wrong number of points; Ray starts at _q0");



    }
}