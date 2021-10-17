package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import primitives.*;

import java.util.List;

/**
 * JUnit5 for geometries.Triangle class
 * @author Deborah Lellouche
 */
class TriangleTests {

    /**
     * Test method for {@link geometries.Triangle#getNormal(primitives.Point3D)}.
     */
    @Test
    void testGetNormal() {
        // WHILE presumption that the Point3D in parameter is on the triangle
        Triangle t = new Triangle(new Point3D(1, 0, 0), new Point3D(0, 0, 0), new Point3D(0, -1, 0));
        Vector result = t.getNormal(new Point3D(0, 0.5, 0));

        // Test if getNormal(Point3D) returns a normal with the good direction
        try {
            result.crossProduct(new Vector(0, 0, 1));
            fail("ERROR : getNormal(Point3D) does not return a normal with the good direction");
            // Vector constructor already tested for Point3D (0, 0, 0)
        } catch (IllegalArgumentException e) {}

        // Test if getNormal(Point3D) returns an unit vector
        assertEquals(result.normalized(), result, "ERROR : getNormal(Point3D) does not return an unit vector");

    }

    /**
     * Test method for {@link geometries.Triangle#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        Triangle triangle = new Triangle(
                new Point3D(1, 0, 0), new Point3D(4, 4, 0), new Point3D(5, 0, 0));

        // ============ Equivalence Partitions Tests ==============
        // Ray intersects the plane of triangle
        // TC01 : Inside the triangle (1 point)
        Point3D p1 = new Point3D(3, 1, 0);
        List<Point3D> result = triangle.findIntersections(
                new Ray(new Point3D(3, 1, -1), new Vector(0, 0, 1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p1), result, "Ray intersects the plane, inside the triangle");

        // TC02 : Outside against edge (0 point)
        result = triangle.findIntersections(
                new Ray(new Point3D(2, 2, -1), new Vector(0, 0, 1)));
        assertEquals(null, result,
                "Wrong number of points; Ray intersects the plane, outside of the triangle against edge");

        // TC03 : Outside against vertex (0 point)
        result = triangle.findIntersections(
                new Ray(new Point3D(0, -1, -1), new Vector(0, 0, 1)));
        assertEquals(null, result,
                "Wrong number of points; Ray intersects the plane, outside of the triangle against vertex");


        // =============== Boundary Values Tests ==================
        // Ray begins before (intersects) the plane of triangle
        // TC11 : On edge (0 point)
        result = triangle.findIntersections(
                new Ray(new Point3D(3, 0, -1), new Vector(0, 0, 1)));
        assertEquals(null, result,
                "Wrong number of points; Ray intersects the plane, on an edge of the triangle");

        // TC12 : In vertex
        result = triangle.findIntersections(
                new Ray(new Point3D(1, 0, -1), new Vector(0, 0, 1)));
        assertEquals(null, result,
                "Wrong number of points; Ray intersects the plane, in vertex of the triangle");

        // TC13 : On edge's continuation
        result = triangle.findIntersections(
                new Ray(new Point3D(6, 0, -1), new Vector(0, 0, 1)));
        assertEquals(null, result,
                "Wrong number of points; " +
                        "Ray intersects the plane, on the continuation of an edge of the triangle");



    }

}