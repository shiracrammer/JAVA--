package elements;

import primitives.*;

import static primitives.Util.alignZero;

/**
 * Class SpotLight, representing point light source with a direction (as a luxo lamp).
 * Intensity (Io), Position (Pl), Direction (direction) normalized, Attenuation factors (kC, kL, kQ).
 * Intensity at a point d with a unity vector l of the vector from the spot to the point :
 * Il = (Io * max (0, direction . l)) / (kC + kL * d + kQ * d^2)
 * Extends PointLight.
 * @author Deborah Lellouche
 */
public class SpotLight extends PointLight {
    /**
     * The direction of the SpotLight.
     */
    private Vector _direction;

    /**
     * SpotLight constructor, receiving 6 parameters.
     * @param intensity for _intensity.
     * @param position  for _position.
     * @param direction for _direction.
     * @throw IllegalArgumentException if direction is null.
     */
    public SpotLight(Color intensity, Point3D position, Vector direction) {
        super(intensity, position);
        if (direction == null) {
            throw new IllegalArgumentException("direction must be not null");
        }
        _direction = direction.normalized();
    }

    @Override
    public Color getIntensity(Point3D p) {
        double d = _position.distance(p);
        Vector l = getL(p);
        double dl = alignZero(_direction.dotProduct(l));
        //double max = dl > 0 ? dl : 0;
        double max = 0;
        if (dl > 0)
            max = dl;
        double attenuation = alignZero(1d / (_kC + _kL * d + _kQ * d * d));
        return (_intensity.scale(max * attenuation));

    }

}
