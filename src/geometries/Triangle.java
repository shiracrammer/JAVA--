package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Class Triangle, representing a 2D triangle in a 3D cartesian coordinate system.
 * Extends Polygon, and so :
 * has 2 instance members (_vertices and _plane)
 * and implements Geometry (and getNormal(Point3D) method).
 * @author Deborah Lellouche
 */
public class Triangle extends Polygon {

    /**
     * Triangle constructor, receiving 3 parameters.
     * @param p1 a Point3D object representing one of the 3 points of the triangle.
     * @param p2 a Point3D object representing another of the 3 points of the triangle.
     * @param p3 a Point3D object representing a third of the 3 points of the triangle.
     * @throws IllegalArgumentException from super if at least 2 of the points are the same
     * and more cases (see Polygon doc).
     */
    public Triangle(Point3D p1, Point3D p2, Point3D p3) {
        super(p1, p2, p3);
    }

    /**
     * To calculate the intersection point of a ray with a triangle. The method :
     * 1) checking if there is an intersection between the ray and the plane of the triangle
     * 2) calculating the normals of the sides of the pyramid with basis the triangle and top the origin of the ray :
     * finding the two corresponding vectors from the origin of the ray and the triangle's vertices
     * and then cross-product of them and normalization of the result
     * 3) calculating the dot-product of the vector direction of the ray and each of the three normals
     * 4) checking if the three dot-product results have the same sign :
     * if yes, the ray intersects the triangle, either not = return null
     * for optimization purposes, if one is 0.0 = return null
     * 5) for optimization purposes : checking if the intersection point is on an edge or at a vertex,
     * and if yes, not to return it = return null
     * 6) return the intersection point between the ray and the plane of triangle (from 1) )
     * @param ray the ray to find the intersection point between it and the triangle.
     * @param maxDistance
     * @return a list of the intersection point between the ray and the triangle
     * (or null if there is not, or if it is on an edge or at a vertex of the triangle).
     */
    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        List<GeoPoint> planeGeoResult = _plane.findGeoIntersections(ray);

        if (planeGeoResult == null) {  // ray does not intersect the plane of the triangle
            return null;
        }

        Point3D p0 = ray.getP0();
        Vector v = ray.getDir();

        Point3D vertex1 = _vertices.get(0);
        Point3D vertex2 = _vertices.get(1);
        Point3D vertex3 = _vertices.get(2);

        Vector v1 = vertex1.subtract(p0);
        Vector v2 = vertex2.subtract(p0);
        Vector v3 = vertex3.subtract(p0);

        Vector N1 = (v1.crossProduct(v2)).normalize();
        Vector N2 = (v2.crossProduct(v3)).normalize();
        Vector N3 = (v3.crossProduct(v1)).normalize();

        double t1 = alignZero(v.dotProduct(N1));
        double t2 = alignZero(v.dotProduct(N2));
        double t3 = alignZero(v.dotProduct(N3));

        // for optimization purpose, if one is 0.0, no intersection
        if (isZero(t1) || isZero(t2) || isZero(t3)) {
            return null;
        }

        // if ray intersects the plane inside the triangle
        if ((t1 > 0 && t2 > 0 && t3 > 0) || (t1 < 0 && t2 < 0 && t3 < 0)) {
            GeoPoint gp1 = planeGeoResult.get(0);
            Point3D p1 = gp1._point;

            // for optimization purpose, if the intersection point is a vertex of the triangle, no intersection
            if ((p1.equals(vertex1)) || (p1.equals(vertex2)) || (p1.equals(vertex3))) {
                return null;
            }

            // for optimization purpose, if the intersection point is on an edge of the triangle, no intersection;
            // check : trying to make cross-product between the vector from one vertex to the intersection point
            // and the vector from this first vertex and its opposite on the edge
            // and so on for the other edges;
            // the intersection point is on the triangle and if one this cross-products return Vector(0,0,0)
            // it means that the intersection point is on an edge;
            // trying to make Vector(0,0,0) throws an exception;
            // and supposing that the exception would be thrown only for trying to make Vector(0,0,0)
            try {
                (p1.subtract(vertex1)).crossProduct(vertex2.subtract(vertex1));
                (p1.subtract(vertex2)).crossProduct(vertex3.subtract(vertex2));
                (p1.subtract(vertex3)).crossProduct(vertex1.subtract(vertex3));
                // the intersection point is in the triangle, not in a vertex and not on an edge
                // updating _geometry of the GeoPoints of PlaneGeoResult to this
                return List.of(new GeoPoint(this, p1));

            } catch (Exception e) {  // intersection point on an edge
                return null;
            }

        }

        // the intersection point is outside the triangle
        return null;
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "_vertices=" + _vertices +
                ", _plane=" + _plane +
                '}';
    }
}
