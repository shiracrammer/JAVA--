package primitives;

import java.util.Objects;

import static primitives.Point3D.ZERO;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Class Vector to represent an Euclidian vector in 3D.
 * @author Deborah Lellouche
 */
public class Vector {
    /**
     * Point3D object to indicate the direction of the vector from the origin point.
     */
    private Point3D _head; // final, but normalize() ?

    /**
     * Vector constructor receiving 1 Point3D object to represent the direction of the vector from the origin.
     * @param head the Point3D object which represent the direction of the vector.
     * @throws IllegalArgumentException if head's coordinates are 0,0,0.
     */
    public Vector(Point3D head) {
        if (head.equals(ZERO)) {
            throw new IllegalArgumentException("Vector _head cannot be Point3D(0,0,0)");
        }
        _head = head;
    }

    /**
     * Vector constructor receiving 3 Coordinate objects to describe the point representing its direction.
     * @param x Coordinate object for the X axis.
     * @param y Coordinate object for the Y axis.
     * @param z Coordinate object for the Z axis.
     */
    public Vector(Coordinate x, Coordinate y, Coordinate z) {
        this(new Point3D(x.coord, y.coord, z.coord));
    }

    /**
     * Vector constructor receiving 3 double values as coordinates for the point representing its direction.
     * @param x double value for the coordinate on the X axis.
     * @param y double value for the coordinate on the Y axis.
     * @param z double value for the coordinate on the Z axis.
     */
    public Vector(double x, double y, double z) {
        this(new Point3D(x, y, z));
    }

    /**
     * Getter for _head.
     * @return a new Point3D, because _head is not final (because normalize()).
     */
    public Point3D getHead() {
        return new Point3D(_head._x, _head._y, _head._z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return _head.equals(vector._head);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "_head=" + _head +
                '}';
    }

    /**
     * To add two Vector objects.
     * @param other the other vector to add to this.
     * @return a new Vector, resulting of the addition of this and other: this + other.
     */
    public Vector add(Vector other) {
        double u1 = _head._x.coord;
        double u2 = _head._y.coord;
        double u3 = _head._z.coord;
        double v1 = other._head._x.coord;
        double v2 = other._head._y.coord;
        double v3 = other._head._z.coord;

        return new Vector(u1 + v1, u2 + v2, u3 + v3);
    }

    /**
     * To substract 2 Vector objects.
     * @param other the vector to subtract to this.
     * @return a new Vector, resulting from the subtraction of other from this : this - other.
     */
    public Vector subtract(Vector other) {
        double u1 = _head._x.coord;
        double u2 = _head._y.coord;
        double u3 = _head._z.coord;
        double v1 = other._head._x.coord;
        double v2 = other._head._y.coord;
        double v3 = other._head._z.coord;

        return new Vector(u1 - v1, u2 - v2, u3 - v3);
    }

    /**
     * For scalar multiplication.
     * @param scalar a double.
     * @return a new Vector, resulting of the scalar multiplication of scalar and this : scalar * this.
     */
    public Vector scale(double scalar) {
        double u1 = _head._x.coord;
        double u2 = _head._y.coord;
        double u3 = _head._z.coord;

        if (isZero((alignZero(scalar * u1)))    // TODO add it because failed
            && isZero((alignZero(scalar * u2)))
            && isZero((alignZero(scalar * u3)))) {
            return new Vector(u1, u2, u3);
        }

        return new Vector(scalar * u1, scalar * u2, scalar * u3);
    }

    /**
     * For dot product between two Vector objects.
     * @param other the other vector.
     * @return a double, resulting of : this . other = (u1 * v1 + u2 * v2 + u3 * v3).
     */
    public double dotProduct(Vector other) {
        double u1 = _head._x.coord;
        double u2 = _head._y.coord;
        double u3 = _head._z.coord;
        double v1 = other._head._x.coord;
        double v2 = other._head._y.coord;
        double v3 = other._head._z.coord;

        return (u1 * v1 + u2 * v2 + u3 * v3);
    }

    /**
     * For cross product between two vectors.
     * @param other the other vector.
     * @return a new Vector, resulting of :
     * this x other = (u2 * v3 - u3 * v2, u3 * v1 - u1 * v3, u1 * v2 - u2 * v1).
     */
    public Vector crossProduct(Vector other) {
        double u1 = _head._x.coord;
        double u2 = _head._y.coord;
        double u3 = _head._z.coord;
        double v1 = other._head._x.coord;
        double v2 = other._head._y.coord;
        double v3 = other._head._z.coord;

        return new Vector(new Point3D(u2 * v3 - u3 * v2, u3 * v1 - u1 * v3, u1 * v2 - u2 * v1));
    }

    /**
     * To partly calculate the length of a vector.
     * Not meant to be used as itself, but used by the method length().
     * @return the square of the length of this : (x * x + y * y + z * z).
     */
    public double lengthSquared() {
        double x = _head._x.coord;
        double y = _head._y.coord;
        double z = _head._z.coord;

        return (x * x + y * y + z * z);
    }

    /**
     * To calculate the length of a vector.
     * @return the length of the vector : Math.sqrt[(x * x + y * y + z * z)].
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * To normalize this, dividing all the coordinate of its destination point by its length.
     * Modifier.
     * @return this, after modification.
     */
    public Vector normalize() {     // modifier
        double x = _head._x.coord;
        double y = _head._y.coord;
        double z = _head._z.coord;
        double length = length();

        Point3D head = new Point3D(x / length, y / length, z / length);
        if (head.equals(ZERO)) {
            throw new IllegalArgumentException("Vector _head cannot be Point3D(0,0,0)");
        }

        _head = head;
        return this;
    }

    /**
     * To return the normalized vector corresponding of this, without modifying it.
     * @return a new Vector, which is the normalized vector corresponding to this.
     */
    public Vector normalized() {
        double x = _head._x.coord;
        double y = _head._y.coord;
        double z = _head._z.coord;
        double length = length();

        return new Vector(x / length, y / length, z / length);
    }



}
