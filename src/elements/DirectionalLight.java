package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * Class DirectionalLight, representing directional light (like the sun).
 * A far away light source, with an intensity and a direction, and no attenuation with distance.
 * Io, at all points.
 * Extends Light.
 * Implements LightSource.
 * @author Deborah Lellouche
 */
public class DirectionalLight extends Light implements LightSource {
    /**
     * For Soft Shadows effect.
     */
    private static final double DEFAULT_SQUARE_EDGE_SIZE = 110.0;
    private double _squareEdgeSize = DEFAULT_SQUARE_EDGE_SIZE;
    private static final double DEFAULT_DISTANCE = 10000; //10000
    private double _distance = DEFAULT_DISTANCE;

    /**
     * Direction of the DirectionalLight.
     */
    private Vector _direction;

    /**
     * DirectionalLight constructor, receiving 2 parameters.
     * @param intensity the value for _intensity.
     * @param direction the value for _direction.
     * @throw IllegalArgumentException if direction is null.
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        if (direction == null) {
            throw new IllegalArgumentException("intensity and direction must be not null");
        }
        _direction = direction.normalized();
    }

    public DirectionalLight(Color intensity, Vector direction, double squareEdgeSize) {
        this(intensity, direction);
        if (squareEdgeSize > 0) {
            _squareEdgeSize = squareEdgeSize;
        }
    }

    @Override
    public Color getIntensity(Point3D p) {
        return _intensity;
    }

    @Override
    public Vector getL(Point3D p) {
        return _direction;
    }

    @Override
    public double getDistance(Point3D point) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double getSquareEdgeSize() {
        return _squareEdgeSize;
    }

    /**
     * For soft shadows effect.
     * @param point the point for which that distance is needed.
     * @return the distance where to place the square for sample rays of soft shadows effect.
     */
    public double getFakeDistance(Point3D point) {
        return _distance;
    }
}
