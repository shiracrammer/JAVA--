package primitives;

import geometries.Geometry;
import geometries.Intersectable.GeoPoint;

import java.util.List;
import java.util.Objects;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Class Ray to represent rays in 3D.
 * Meaning the group of points on a line that are on one specific side
 * relatively to a specific point called the head of the ray.
 * @author Deborah Lellouche
 */
public class Ray {
    /**
     * Point3D object representing the origin of the ray.
     */
    final Point3D _p0; // private

    /**
     * Vector object representing the direction of the ray, the line on which are the group of points of the ray.
     * By construction, it is a normalized vector.
     */
    final Vector _dir;

    /**
     * Constant for value of deplacement of head of rays for shadow rays.
     */
    private static final double DELTA = 0.1;

    /**
     * Ray constructor, receiving two parameters.
     * @param p0 a Point3D object that represents the head of the ray.
     * @param dir a Vector object representing the direction of the ray.
     *            This vector not has to be normalized.
     *            The normalized form of this vector is calculated to be used as the direction of the ray.
     */
    public Ray(Point3D p0, Vector dir) {
        _p0 = p0;
        _dir = dir.normalized();
    }

    /**
     * Ray constructor for secondary rays for refraction / reflection, receivin 3 parameters.
     * @param p0 a Point3D object that represents the head of the ray.
     * @param dir a Vector object representing the direction of the ray.
     * @param normal the normal to the geometry of p0 at p0.
     */
    public Ray(Point3D p0, Vector dir, Vector normal) {
        double sign = alignZero(normal.dotProduct(dir));

        if (sign > 0) { // TODO what if is 0
            _p0 = p0.add(normal.scale(DELTA));
        }
        else {
            _p0 = p0.add(normal.scale(-DELTA));
        }

        _dir = dir.normalized();

    }

    /**
     * Getter of _p0, the head of the ray.
     * @return _p0 itself.
     */
    public Point3D getP0() {
        return _p0;
    }

    /**
     * Getter of _dir, the direction of the ray.
     * @return _dir itself.
     */
    public Vector getDir() {
        return _dir;
    }

    /**
     * To return the point P such as : P = P0 + t.v
     * @throws IllegalArgumentException if t is zero
     * (because Vector(0,0,0) not allowed here, and would throw another one).
     * @param t double != 0 (> 0 or < 0).
     * @return the point P such as : P = P0 + t.v
     */
    public Point3D getPoint(double t) {
        if (isZero(t)) {
            throw new IllegalArgumentException("the scalar t cannot be zero here");
        }

        Point3D p = _p0.add(_dir.scale(t));
        return p;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return _p0.equals(ray._p0) && _dir.equals(ray._dir);
    }

    @Override
    public String toString() {
        return "Ray{" +
                "_p0=" + _p0 +
                ", _dir=" + _dir +
                '}';
    }

    /**
     * To return the point of pointsList that is the closest to the origin point of this (_p0).
     * @param pointsList a List<Point3D> containing the points.
     * @return the Point3D in pointsList that is the closest to _p0.
     */
    public Point3D findClosestPoint(List<Point3D> pointsList) {
        // searching for the minimal distance
        double distance = Double.POSITIVE_INFINITY;
        Point3D nearPoint = null;

        if (pointsList != null) {
            for (Point3D point : pointsList) {
                double currentDistance = point.distance(_p0);
                if (currentDistance < distance) {
                    distance = currentDistance;
                    nearPoint = point;
                }
            }
        }

        return nearPoint;
    }

    /**
     * As for findClosestPoint, but using Intersectable.GeoPoint instead of Point3D.
     * @return a GeoPoint, composed of the Point3D that is the closest to this,
     * and of the corresponding Geometry.
     *
     * CLOSEST IN WHICH DIRECTION ??
     */
    public GeoPoint findClosestGeoPoint(List<GeoPoint> gpList) {
        // searching for the minimal distance
        double distance = Double.POSITIVE_INFINITY;
        Geometry nearGeometry = null;
        Point3D nearPoint = null;

        if (gpList != null) {
            for (GeoPoint gp : gpList) {
                Point3D point = gp._point;
                double currentDistance = point.distance(_p0);
                if (currentDistance < distance) {
                    distance = currentDistance;
                    nearGeometry = gp._geometry;
                    nearPoint = point;
                }
            }
        }

        if (nearGeometry == null) {
            return null;
        }
        return new GeoPoint(nearGeometry, nearPoint);

    }

}
