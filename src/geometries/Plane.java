package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Class Plane, representing an infinite plane in 3D.
 * Extends Geometry.
 * @author Deborah Lellouche
 */
public class Plane extends Geometry {
    /**
     * Point3D object representing a point on the plane.
     */
    final Point3D _q0;

    /**
     * Vector object representing the normal to the plane.
     */
    final Vector _normal;

    /**
     * Plane constructor, receiving 2 parameters.
     * @param q0 a Point3D object representing a point on the plane.
     * @param normal a Vector object representing the normal to the plane.
     */
    public Plane(Point3D q0, Vector normal) {
        _q0 = q0;
        _normal = normal.normalize();
    }

    /**
     * Plane constructor, receiving 3 Point3D as parameters.
     * One of them is saved as it is, and from them all the normal to the plane is made.
     * @param p1 a Point3D object representing a point on the plane.
     * @param p2 a Point3D object representing another point on the plane.
     * @param p3 a Point3D object representing a third point on the plane.
     * @throws IllegalArgumentException if at least two of the three points are the same.
     */
    public Plane(Point3D p1, Point3D p2, Point3D p3) {
        if (p1.equals(p2) || (p1.equals(p3)) || p2.equals(p3)) {
            throw new IllegalArgumentException("This constructor must received 3 different points.");
        }
        _q0 = p1;
        Vector u = p1.subtract(p2);    // vector with direction from p2 to p1
        Vector v = p1.subtract(p3);    // vector with direction from p3 to p1
        Vector n = u.crossProduct(v);
        _normal = n.normalized();
    }

    /**
     * Getter of _q0.
     * @return _q0 itself.
     */
    public Point3D getQ0() {
        return _q0;
    }

    /**
     * Getter of _normal.
     * @deprecated use {@link Plane#getNormal(Point3D)} with null value as parameter.
     * @return _normal itself.
     */
    @Deprecated
    public Vector getNormal() {
        return _normal;
    }

    @Override
    public String toString() {
        return "Plane{" +
                "_q0=" + _q0 +
                ", _normal=" + _normal +
                '}';
    }

    @Override
    public Vector getNormal(Point3D point) {
        return _normal;
    }


    /**
     * To calculate the intersection point between a ray and a plane. The method :
     * Ray points : P = P0 + t.v (t is scalar > 0)
     * Plane points : the P such as N.(q0 - p0) = 0
     * The intersection point must be such as : N.(q0 - t.v - p0) = 0
     * => t = N.(q0 - p0) / N.v
     * => P = P0 + t.v
     * Notes :
     * 1) for optimization purpose : if p0.equals(q0), return null
     * 2) if numerator or denominator is zero, return null (parallel, included or not)
     * 3) if t < 0 : return null
     * @param ray the ray to find the intersection point between it and the plane.
     * @return a list of the intersection point between the ray and the plane (or null if there is not).
     */
    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        Point3D p0 = ray.getP0();
        Vector v = ray.getDir();

        double denominator = alignZero(_normal.dotProduct(v));

        if (_q0.equals(p0)) {   // origin of the ray onto the plane (_q0)
            if (isZero(denominator)) { // and ray parallel to the plane => included in
                return null;    // an infinity ?
            }
            else {                      // and ray crosses the plane
                return null;
            }
        }

        Vector q0_p0 = _q0.subtract(p0);
        double numerator = alignZero(_normal.dotProduct(q0_p0));

        if (isZero(numerator)) {    // origin of the ray on the plane (q0_p0 perpendicular to _normal)
            return null;
        }
        else if (isZero(denominator)) {  // ray parallel to the plane but not included in
            return null;
        }
        else {
            double t = alignZero(numerator / denominator);
            if (t > 0) {
                return List.of(new GeoPoint(this, ray.getPoint(t)));
            }
            else {
                return null;
            }
        }

    }
}
