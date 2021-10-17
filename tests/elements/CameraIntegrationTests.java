package elements;

import org.junit.jupiter.api.Test;
import primitives.*;
import geometries.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Camera class, on intersection between rays passing by the pixels of the View Plane
 * and different Intersectable objects.
 *
 * @author Deborah Lellouche
 */
class CameraIntegrationTests {

    /**
     * Function for testing purposes.
     * To check if the number of intersection points found between the rays originated from the camera
     * and the geometry is equal to the expected number of intersection points.
     * @param camera a Camera object, from which the rays are originated, passing through its View Plane.
     *               (3X3 View Plane, with width and height of one pixel = 1)
     * @param geometry an Intersectable object (Sphere, Plane, Triangle...).
     * @param expected an integer representing the expected number of intersection points between the rays
     *                 and the geometry.
     */
    private void assertCountIntersections(Camera camera, Intersectable geometry, int expected) {
        int count = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Ray ray = camera.constructRayThroughPixel(3,3, j, i);
                List<Point3D> result = geometry.findIntersections(ray);
                if (result != null) {     // else, count does not change
                    count += result.size();
                }
            }
        }

        assertEquals(expected, count, "Wrong amount of intersections");
    }


    /**
     * To check that the process of intersection points calculation between
     * the rays originated from a Camera with a 3X3 View Plane (with pixel width and height = 1)
     * and a geometry of type Sphere works properly.
     * In fact, just checking that the number of intersection points in different test cases is correct
     * (and not which points were found).
     */
    @Test
    public void testCameraSphereIntegration() {
        Camera camera = new Camera(Point3D.ZERO, new Vector(0,0,-1), new Vector(0, 1, 0))
                .setViewPlaneSize(3,3).setDistance(1);
        Sphere sphere;

        // TC01 : Sphere of small size, comparing to the View Plane (2 points)
        sphere = new Sphere(1, new Point3D(0,0,-3));
        assertCountIntersections(camera, sphere, 2);

        // TC02 : Sphere of large size, comparing to the View Plane (18 points)
        camera = new Camera(new Point3D(0, 0, 0.5), new Vector(0,0,-1), new Vector(0, 1, 0))
                .setViewPlaneSize(3,3).setDistance(1);
        sphere = new Sphere(2.5, new Point3D(0, 0, -2.5));
        assertCountIntersections(camera, sphere, 18);

        // TC03 : Sphere of medium size, comparing to the View Plane (10 points)
        sphere = new Sphere(2, new Point3D(0, 0, -2));
        assertCountIntersections(camera, sphere, 10);

        // TC04 : Sphere of very big size, including the View Plane (9 points)
        sphere = new Sphere(4, new Point3D(0,0,-1));
        assertCountIntersections(camera, sphere, 9);

        // TC05 : Sphere beyond the View Plane (0 points)
        sphere = new Sphere(0.5, new Point3D(0,0,1));
        assertCountIntersections(camera, sphere, 0);

    }

    /**
     * To check that the process of intersection points calculation between
     * the rays originated from a Camera with a 3X3 View Plane (with pixel width and height = 1)
     * and a geometry of type Plane works properly.
     * In fact, just checking that the number of intersection points in different test cases is correct
     * (and not which points were found).
     */
    @Test
    public void testCameraPlaneIntegration() {
        Camera camera = new Camera(Point3D.ZERO, new Vector(0,0,-1), new Vector(0, 1, 0))
                .setViewPlaneSize(3,3).setDistance(1);
        Plane plane;

        // TC01 : Plane parallel to the View Plane (9 points)
        plane = new Plane(new Point3D(0, 0, -3), new Vector(0,0,-1));
        assertCountIntersections(camera, plane, 9);

        // TC02 : Plane with some angle with the View Plane,
        // such that is already facing all the pixels of the View Plane (9 points)
        plane = new Plane(new Point3D(0, 0, -3), new Vector(0,0.4,-0.9));
        assertCountIntersections(camera, plane, 9);

        // TC03 : Plane with some angle with the View Plane,
        // such that not facing a full line of pixels of the View Plane (6 points)
        plane = new Plane(new Point3D(0, 0, -3), new Vector(0,0.7,-0.3));
        assertCountIntersections(camera, plane, 6);

    }

    /**
     * To check that the process of intersection points calculation between
     * the rays originated from a Camera with a 3X3 View Plane (with pixel width and height = 1)
     * and a geometry of type Triangle works properly.
     * In fact, just checking that the number of intersection points in different test cases is correct
     * (and not which points were found).
     */
    @Test
    public void testCameraTriangleIntegration() {
        Camera camera = new Camera(Point3D.ZERO, new Vector(0,0,-1), new Vector(0, 1, 0))
                .setViewPlaneSize(3,3).setDistance(1);
        Triangle triangle;

        // TC01 : Triangle with a little size, facing just one pixel (1 point)
        triangle = new Triangle
                (new Point3D(1, -1, -2), new Point3D(-1, -1, -2), new Point3D(0, 1, -2));
        assertCountIntersections(camera, triangle, 1);

        // TC02 : Triangle with a bigger size, facing two pixels (2 points)
        triangle = new Triangle
                (new Point3D(1, -1, -2), new Point3D(-1, -1, -2), new Point3D(0, 20, -2));
        assertCountIntersections(camera, triangle, 2);

    }


}