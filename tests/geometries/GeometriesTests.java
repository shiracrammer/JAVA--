package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit5 for geometries.Geometries class
 * @author Deborah Lellouche
 */
class GeometriesTests {

    /**
     * Test method for {@link geometries.Geometries#findIntersections(primitives.Ray)}.
     * Note : just checking the number of intersection points (and not their values)
     */
    @Test
    void testFindIntersections() {

        Plane plane = new Plane(new Point3D(0, 0, 10), new Vector(0,0, 1));
        Sphere sphere = new Sphere(1, new Point3D(3, 0, 8));
        Triangle triangle = new Triangle(
                new Point3D(1, 0, 0), new Point3D(4, 4, 0), new Point3D(5, 0, 0));

        Geometries g0 = new Geometries();
        Geometries g1 = new Geometries(plane, sphere, triangle);

        List<Point3D> result = null;

        // ============ Equivalence Partitions Tests ==============
        // TC01 : Intersection between the ray and some of the Geometries' components
        // (not just one, and not all of them)
        result = g1.findIntersections(new Ray(new Point3D(3, 2, -1), new Vector(0, 0, 1)));
        assertEquals(2, result.size(),
                "Wrong number of points ; " +
                        "Intersection between the ray and some of the Geometries' components");


        // =============== Boundary Values Tests ==================
        // TC11 : Geometries is empty
        result = g0.findIntersections(new Ray(new Point3D(3, 0.5, -1), new Vector(0, 0, 1)));
        assertEquals(null, result, "Wrong number of points ; Geometries is empty");

        // TC12 : No intersection between the ray and one of the Geometries' components
        result = g1.findIntersections(new Ray(new Point3D(3, 0.5, -1), new Vector(0, 0, -1)));
        assertEquals(null, result,
                "Wrong number of points ; " +
                        "No intersection between the ray and one of the Geometries' components");

        // TC13 : Intersection just with one of the Geometries' components
        result = g1.findIntersections(new Ray(new Point3D(0, 2, -1), new Vector(0, 0, 1)));
        assertEquals(1, result.size(),
                "Wrong number of points ; " +
                        "Intersection just with one of the Geometries' components");

        // TC14 : Intersection with all of the Geometries' components
        result = g1.findIntersections(new Ray(new Point3D(3, 0.5, -1), new Vector(0, 0, 1)));
        // note : ray from outside the sphere
        assertEquals(4, result.size(),
                "Wrong number of points ; " +
                        "Intersection with all of the Geometries' components");


    }


}