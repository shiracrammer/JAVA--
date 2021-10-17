package primitives;

import elements.AmbientLight;
import scene.Scene;

/**
 * Class Material, representing the material composing a 3D-object.
 *
 * Passive Data Structure (Plain Old Data) :
 * only public data members and no methods (or just simple getters)
 *
 * Element of Builder Design Pattern
 *
 * @author Deborah Lellouche
 */
public class Material {
    /**
     * Diffusion constant.
     */
    public double _kD;

    /**
     * Specular constant.
     */
    public double _kS;

    /**
     * Shininess constant.
     */
    public int _nShininess;

    /**
     * Transparency constant.
     */
    public double _kT;

    /**
     * Reflection constant.
     */
    public double _kR;

    /**
     * Material default constructor, initializing all the member attributes to 0.
     */
    public Material() {
        _kD = 0.0;
        _kS = 0.0;
        _nShininess = 0;
        _kT = 0.0;
        _kR = 0.0;
    }

    /**
     * Setter for _kD.
     * Chaining method.
     * @param kD the new value of _kD.
     * @return this.
     */
    public Material setKd(double kD) {
        _kD = kD;
        return this;
    }

    /**
     * Setter for _kS.
     * Chaining method.
     * @param kS the new value of _kS.
     * @return this.
     */
    public Material setKs(double kS) {
        _kS = kS;
        return this;
    }

    /**
     * Setter for _nShininess.
     * Chaining method.
     * @param nShininess the new value of _nShininess.
     * @return this.
     */
    public Material setShininess(int nShininess) {
        _nShininess = nShininess;
        return this;
    }

    /**
     * Setter for _kT.
     * Chaining method.
     * @param kT the new value of _kT.
     * @return this.
     */
    public Material setKt(double kT) {
        _kT = kT;
        return this;
    }

    /**
     * Setter for _kR.
     * Chaining method.
     * @param kR the new value of _kR.
     * @return this.
     */
    public Material setKr(double kR) {
        _kR = kR;
        return this;
    }
}
