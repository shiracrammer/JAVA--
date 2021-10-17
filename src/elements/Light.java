package elements;

import primitives.Color;

/**
 * Abstract class Light, implemented by classes representing light types :
 * ambient light and light sources.
 * @author Deborah Lellouche
 */
abstract class Light {
    /**
     * The color of the light.
     */
    protected Color _intensity; // protected instead of private

    /**
     * Light constructor.
     * @param intensity the value for _intensity.
     * @throw IllegalArgumentException if intensity is null.
     */
    protected Light(Color intensity) {
        if (intensity == null) {
            throw new IllegalArgumentException("intensity must be not null");
        }
        _intensity = intensity;
    }

    /**
     * Getter of _intensity.
     * @return the value of _intensity.
     */
    public Color getIntensity() {
        return _intensity;
    }
}
