package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

import static primitives.Util.alignZero;

/**
 * Class PointLight, representing omni-directional point light sources (as a bulb).
 * Intensity (Io), Position (Pl), Factors (kC, kL, kQ) for attenuation with distance (d).
 * At a point at a distance d :
 * Il = Io / (kC + kL * d + kQ * d^2)
 * Extends Light.
 * Implements LightSource.
 * @author Deborah Lellouche
 */
public class PointLight extends Light implements LightSource {
    /**
     * The position of the PointLight.
     */
    protected Point3D _position;

    /**
     * The coefficient to calculate the attenuation of the light with the distance.
     */
    protected double _kC = 1.0;
    protected double _kL = 0.00005;
    protected double _kQ = 0.0000025;

    /**
     * For Soft Shadows effect.
     */
    private static final double DEFAULT_SQUARE_EDGE_SIZE = 110.0;
    private double _squareEdgeSize = DEFAULT_SQUARE_EDGE_SIZE;

    /**
     * PointLight constructor, receiving 5 parameters.
     * @param intensity for _intensity.
     * @param position for _position.
     * @throw IllegalArgumentException if position is null.
     */
    public PointLight(Color intensity, Point3D position) {
        super(intensity);
        if (position == null) {
            throw new IllegalArgumentException("position must be not null");
        }
        _position = position;
    }

    public PointLight(Color intensity, Point3D position, double squareEdgeSize) {
        this(intensity, position);
        if (squareEdgeSize > 0) {
            _squareEdgeSize = squareEdgeSize;
        }
    }

    @Override
    public Color getIntensity(Point3D p) {
        double d = _position.distance(p);
        double attenuation = alignZero(1d / (_kC + _kL * d + _kQ * d * d));
        return (_intensity.scale(attenuation));

    }

    @Override
    public Vector getL(Point3D p) {
        return (p.subtract(_position)).normalized();
    }

    @Override
    public double getDistance(Point3D point) {
        return _position.distance(point);
    }

    @Override
    public double getSquareEdgeSize() {
        return _squareEdgeSize;
    }

    /**
     * Setter of _kC.
     * Chaining method.
     * @param kC the new value for _kC.
     * @return this.
     */
    public PointLight setKc(double kC) {
        _kC = kC;
        return this;
    }

    /**
     * Setter of _kL.
     * Chaining method.
     * @param kL the new value for _kL.
     * @return this.
     */
    public PointLight setKl(double kL) {
        _kL = kL;
        return this;
    }

    /**
     * Setter of _kQ.
     * Chaining method.
     * @param kQ the new value for _kQ.
     * @return this.
     */
    public PointLight setKq(double kQ) {
        _kQ = kQ;
        return this;
    }
}
