package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import primitives.*;

import java.util.List;

/**
 * JUnit5 for geometries.Sphere class
 * @author Deborah Lellouche
 */
class SphereTests {

    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point3D)}.
     */
    @Test
    void testGetNormal() {
        // Test if getNormal(Point3D) returns the correct normal
        // WHILE presumption that the Point3D in parameter is at the surface of the sphere
        Sphere s = new Sphere(1, new Point3D(0, 0, 0));
        Vector result = s.getNormal(new Point3D(1, 0, 0));
        assertEquals(new Vector(1, 0, 0), result, "ERROR : getNormal(Point3D) does not return the correct normal");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(1d, new Point3D(1, 0, 0));

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray's line is outside the sphere (0 points)
        assertEquals(null, sphere.findIntersections(
                new Ray(new Point3D(-1, 0, 0), new Vector(1, 1, 0))),
                "Ray's line out of sphere");

        // TC02: Ray starts before and crosses the sphere (2 points)
        Point3D p1 = new Point3D(0.0651530771650466, 0.355051025721682, 0);
        Point3D p2 = new Point3D(1.53484692283495, 0.844948974278318, 0);
        List<Point3D> result = sphere.findIntersections(
                new Ray(new Point3D(-1, 0, 0), new Vector(3, 1, 0)));
        assertEquals(2, result.size(), "Wrong number of points");
        if (result.get(0).getX() > result.get(1).getX())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(p1, p2), result, "Ray crosses sphere");

        // TC03: Ray starts inside the sphere (1 point)
        p1 = new Point3D(1.4114378277661, 0.9114378277661, 0);
        result = sphere.findIntersections(
                new Ray(new Point3D(1, 0.5, 0), new Vector(1, 1, 0)));
        assertEquals(1, result.size(), "Wrong number of points");
        result = List.of(result.get(0));
        assertEquals(List.of(p1), result, "Ray from inside the sphere");


        // TC04: Ray starts after the sphere (0 points)
        result = sphere.findIntersections(
                new Ray(new Point3D(2, 2, 0), new Vector(1, 1, 0)));
        assertEquals(null, result, "Wrong number of points ; Ray after the sphere");


        // =============== Boundary Values Tests ==================

        // **** Group: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 points)
        p1 = new Point3D(2, 0, 0);
        result = sphere.findIntersections(
                new Ray(new Point3D(1, -1, 0), new Vector(1, 1, 0)));
        assertEquals(1, result.size(), "Wrong number of points");
        result = List.of(result.get(0));
        assertEquals(List.of(p1), result, "Ray starts at sphere and goes inside");

        // TC12: Ray starts at sphere and goes outside (0 points)
        result = sphere.findIntersections(
                new Ray(new Point3D(2, 0, 0), new Vector(1, 1, 0)));
        assertEquals(null, result,
                "Wrong number of points ; Ray starts a sphere and goes outside");


        // **** Group: Ray's line goes through the center
        // TC13: Ray starts before the sphere (2 points)
        p1 = new Point3D(1.3021694792519625,  0.9532542188779438,  0.0); //new Point3D(1.302169479252, 0.953254218879, 0);
        p2 = new Point3D(0.2362920592095763,  -0.6455619111856356,  0.0); //new Point3D(0.2362920592096, -0.6455619111856, 0);
        result = sphere.findIntersections(
                new Ray(new Point3D(0, -1, 0), new Vector(1, 1.5, 0)));
        assertEquals(2, result.size(), "Wrong number of points");
        result = List.of(result.get(0), result.get(1));
        assertEquals(List.of(p1, p2), result, "Ray starts before the sphere and goes through");

        // TC14: Ray starts at sphere and goes inside (1 points)
        p1 = new Point3D(1.3021694792519625,  0.9532542188779438,  0.0);
        result = sphere.findIntersections(
                new Ray(new Point3D(0.2362920592095763,  -0.6455619111856356,  0.0),
                        new Vector(1, 1.5, 0)));
        assertEquals(1, result.size(), "Wrong number of points");
        result = List.of(result.get(0));
        assertEquals(List.of(p1), result, "Ray starts at sphere and goes through");

        // TC15: Ray starts inside (1 points)
        p1 = new Point3D(1.3021694792519625,  0.9532542188779438,  0.0);
        result = sphere.findIntersections(
                new Ray(new Point3D(0.666666666666667,  0,  0.0),
                        new Vector(1, 1.5, 0)));
        assertEquals(1, result.size(), "Wrong number of points");
        result = List.of(result.get(0));
        assertEquals(List.of(p1), result, "Ray starts inside and goes through");

        // TC16: Ray starts at the center (1 points)
        p1 = new Point3D(1.4472135955,  0.8944271909999,  0.0);
        result = sphere.findIntersections(
                new Ray(new Point3D(1,  0,  0), new Vector(1, 2, 0)));
        assertEquals(1, result.size(), "Wrong number of points");
        result = List.of(result.get(0));
        assertEquals(List.of(p1), result, "Ray starts at the center of the sphere");

        // TC17: Ray starts at sphere and goes outside (0 points)
        result = sphere.findIntersections(
                new Ray(new Point3D(1.3021694792519625,  0.9532542188779438,  0.0),
                        new Vector(1, 1.5, 0)));
        assertEquals(null, result, "Wrong number of points; Ray starts at sphere and goes outside");

        // TC18: Ray starts after sphere (0 points)
        result = sphere.findIntersections(
                new Ray(new Point3D(2, 2,  0.0),
                        new Vector(1, 1.5, 0)));
        assertEquals(null, result, "Wrong number of points; Ray starts after sphere");


        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC19: Ray starts before the tangent point
        result = sphere.findIntersections(
                new Ray(new Point3D(0, 1,  0.0),
                        new Vector(1, 0, 0)));
        assertEquals(null, result, "Wrong number of points; Ray starts before the tangent point");

        // TC20: Ray starts at the tangent point
        result = sphere.findIntersections(
                new Ray(new Point3D(1, 1,  0.0),
                        new Vector(1, 0, 0)));
        assertEquals(null, result, "Wrong number of points; Ray starts at the tangent point");

        // TC21: Ray starts after the tangent point
        result = sphere.findIntersections(
                new Ray(new Point3D(2, 1,  0.0),
                        new Vector(1, 0, 0)));
        assertEquals(null, result, "Wrong number of points; Ray starts after the tangent point");


        // **** Group: Special cases
        // TC19: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
        result = sphere.findIntersections(
                new Ray(new Point3D(0, 2,  0.0),
                        new Vector(1, 0, 0)));
        assertEquals(null, result, "Wrong number of points; Ray is orthogonal");


    }

}