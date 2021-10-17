package primitives;

import geometries.Sphere;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import geometries.Intersectable.GeoPoint;

/**
 * JUnit5 for primitives.Ray class
 * @author Deborah Lellouche
 */
class RayTests {

    /**
     * Test method for {@link primitives.Ray#getPoint(double)}.
     */
    @Test
    void testGetPoint() {
        Ray ray = new Ray(new Point3D(1, 0, 0), new Vector(0, 0, 1));
        Point3D result = null;

        // ============ Equivalence Partitions Tests ==============
        // TC01 : Scalar is positive
        result = ray.getPoint(5);
        assertEquals(new Point3D(1, 0, 5), result, "Wrong result; Scalar is positive");

        // TC02 : Scalar is negative
        result = ray.getPoint(-5);
        assertEquals(new Point3D(1, 0, -5), result, "Wrong result; Scalar is negative");

        // =============== Boundary Values Tests ==================
        // TC11 : Scalar is zero (because Vector(0,0,0) not allowed here)
        try {
            result = ray.getPoint(0);
            fail("Wrong result; Scalar is zero");
        } catch (Exception e) {}

    }

    /**
     * Test method for {@link primitives.Ray#findClosestPoint(List)}.
     */
    @Test
    void testFindClosestPoint() {
        Ray ray = new Ray(Point3D.ZERO, new Vector(1,0,0));
        List<Point3D> pointsList;

        // ============ Equivalence Partitions Tests ==============
        // TC01 : The closest point is in the middle of the list
        pointsList = new LinkedList<>();
        pointsList.add(new Point3D(-2, 0, 0));
        pointsList.add(new Point3D(-1, 0, 0));
        pointsList.add(new Point3D(2, 0, 0));

        assertEquals(new Point3D(-1, 0, 0), ray.findClosestPoint(pointsList),
                "Wrong closest point");

        // =============== Boundary Values Tests ==================
        // TC11 : The list is null
        assertNull(ray.findClosestPoint(null),
                "Wrong closest point, the answer should be null");

        // TC12 : The closest point is the first point of the list
        pointsList = new LinkedList<>();
        pointsList.add(new Point3D(-1, 0, 0));
        pointsList.add(new Point3D(-2, 0, 0));
        pointsList.add(new Point3D(2, 0, 0));

        assertEquals(new Point3D(-1, 0, 0), ray.findClosestPoint(pointsList),
                "Wrong closest point");

        // TC13 : The closest point is the last point of the list
        pointsList = new LinkedList<>();
        pointsList.add(new Point3D(-2, 0, 0));
        pointsList.add(new Point3D(2, 0, 0));
        pointsList.add(new Point3D(-1, 0, 0));

        assertEquals(new Point3D(-1, 0, 0), ray.findClosestPoint(pointsList),
                "Wrong closest point");

    }

    /**
     * Test method for {@link primitives.Ray#findClosestGeoPoint(List)}.
     *
     * CLOSEST IN WHICH DIRECTION ?
     */
    @Test
    void testFindClosestGeoPoint() {
        Ray ray = new Ray(Point3D.ZERO, new Vector(1,0,0));
        List<GeoPoint> gpList;
        Sphere sphere;

        // ============ Equivalence Partitions Tests ==============
        // TC01 : The closest point is in the middle of the list
        gpList = new LinkedList<>();
        sphere = new Sphere(1, new Point3D(3, 0, 0));
        GeoPoint gp1 = new GeoPoint(sphere, new Point3D(2, 0, 0));
        GeoPoint gp2 = new GeoPoint(sphere, new Point3D(3, 0, 0));
        GeoPoint gp3 = new GeoPoint(sphere, new Point3D(4, 0, 0));
        gpList.add(gp2);
        gpList.add(gp1);
        gpList.add(gp3);

        assertEquals(gp1, ray.findClosestGeoPoint(gpList),
                "Wrong closest point");

        // =============== Boundary Values Tests ==================
        // TC11 : The list is null
        assertNull(ray.findClosestGeoPoint(null),
                "Wrong closest point, the answer should be null");

        // TC12 : The closest point is the first point of the list
        gpList = new LinkedList<>();
        gpList.add(gp1);
        gpList.add(gp2);
        gpList.add(gp3);

        assertEquals(gp1, ray.findClosestGeoPoint(gpList),
                "Wrong closest point");

        // TC13 : The closest point is the last point of the list
        gpList = new LinkedList<>();
        gpList.add(gp2);
        gpList.add(gp3);
        gpList.add(gp1);

        assertEquals(gp1, ray.findClosestGeoPoint(gpList),
                "Wrong closest point");

    }


}