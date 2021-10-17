package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Class Sphere, representing a sphere in 3D.
 * Extends Geometry.
 * @author Deborah Lellouche
 */
public class Sphere extends Geometry {
    /**
     * Point3D object representing the center of the sphere.
     */
    final Point3D _center;

    /**
     * Double value representing the radius of the sphere.
     */
    final double _radius;

    /**
     * Sphere constructor receiving 2 parameters.
     * @param radius a double value representing the radius of the sphere.
     * @param center a Point3D object representing the center of the sphere.
     * @throws IllegalArgumentException if radius <= 0.
     */
    public Sphere(double radius, Point3D center) {
        if (radius <= 0) {
            throw new IllegalArgumentException("radius of a Sphere object must be > 0");
        }
        _radius = radius;
        _center = center;
    }

    /**
     * Getter of _center.
     * @return _center itself.
     */
    public Point3D getCenter() {
        return _center;
    }

    /**
     * Getter of _radius.
     * @return the value of _radius.
     */
    public double getRadius() {
        return _radius;
    }

    @Override
    public String toString() {
        return "Sphere{" +
                "_center=" + _center +
                ", _radius=" + _radius +
                '}';
    }

    @Override
    public Vector getNormal(Point3D point) {
        if (point.equals(_center)) {
            throw new IllegalArgumentException("point cannot be the center of the sphere");
        }
        Vector p_o = point.subtract(_center);
        Vector normal = p_o.normalize();
        return normal;
    }

    /**
     * To calculate the intersection point between a ray and a sphere. The method :
     * Ray points : P = P0 + t.v (t is scalar > 0)
     * Sphere points : |P - _center|^2 - r^2 = 0
     * 1) building an right angle triangle with segment from P0 to _center as hypotenuse,
     * and other edges tm and d
     * u = _center - P0
     * tm = v.u
     * d = sqrt(u^2 - tm^2)
     * 2) if d >= _radius, there is no intersection = return null
     * 3) calculating th, the distance between the right angle of the triangle and the intersection points
     * on the both sides continuation of the ray
     * 4) calculating t1,t2 = tm +- th   (two scalars)
     * 5) if one of them or both > 0 : calculating the corresponding intersection points : Pi = P0 + ti.v (i=1,2),
     * either return null
     * @param ray the Ray object to find the intersections between it and the sphere.
     * @param maxDistance
     * @return a list of the intersection(s) point(s) between the ray and the plane (or null if there is not).
     */
    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        Point3D P0 = ray.getP0();
        Vector v = ray.getDir();

        if (_center.equals(P0)) {
            return List.of(new GeoPoint(this, _center.add(v.scale(_radius))));
            //throw new IllegalArgumentException("origin of the ray cannot be the center of the sphere");
        }

        Vector u = _center.subtract(P0);
        double tm = alignZero(v.dotProduct(u));

        double d = alignZero(Math.sqrt(u.length()*u.length() - (tm*tm)));

        if (d > _radius || isZero(d - _radius)) {       // d >= _radius, no intersection
            return null;
        }

        double th = alignZero(Math.sqrt(_radius*_radius - d*d));

        double t1 = alignZero(tm + th);
        double t2 = alignZero(tm - th);
        if ((t1 > 0) && (t2 > 0)) {
            return List.of(new GeoPoint(this, ray.getPoint(t1)),
                            new GeoPoint(this, ray.getPoint(t2)));
        }
        else if (t1 > 0) {
            return List.of(new GeoPoint(this, ray.getPoint(t1)));
        }
        else if (t2 > 0) {
            return List.of(new GeoPoint(this, ray.getPoint(t2)));
        }

        return null;
    }
}
