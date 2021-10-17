package renderer;

import elements.DirectionalLight;
import elements.LightSource;
import geometries.Geometry;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Class RayTracerBasic, representing a basic ray tracer.
 * Extends RayTracerBase (and so implementing traceRay(Ray)).
 *
 * @author Deborah Lellouche
 */
public class BasicRayTracer extends RayTracerBase {
    /**
     * Stop conditions for the recursion for transparency/reflection calculation.
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double INITIAL_K = 1.0;
    private static final double MIN_CALC_COLOR_K = 0.001;

    /**
     * For Soft Shadows effect.
     * DEFAULT_NUM_OF_SAMPLES must be an odd number.
     */
    private static final int DEFAULT_NUM_OF_SAMPLES = 11;
    private int _numOfSamples = 1;
    private static final double DEFAULT_ANGLE = Math.toRadians(0.005);
    private double _angle = DEFAULT_ANGLE;
    private double _tan_angle = Math.tan(_angle);
    private boolean softShadowsFlag = false;


    /**
     * RayTracerBasic constructor, receiving 1 parameter.
     *
     * @param scene the scene of the RayTracerBase.
     */
    public BasicRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<GeoPoint> intersections = _scene._geometries.findGeoIntersections(ray);
        if (intersections == null) {
            return _scene._background;
        } else {
            GeoPoint closestPoint = ray.findClosestGeoPoint(intersections);
            return calcColor(closestPoint, ray);
        }
    }

    /**
     * To return the color of a point.
     * Helper method.
     * The method :
     * - to find the intensity of the ambientLight.
     * with calcColor(Geopoint, Ray, int level, double k) :
     * - to add to it the color of the light emitted by the object.
     * - for each light source, to add : [ kD * |l.n| + kS * (max(0,-v.r))^nShininess ] * Il
     * - to add refraction / reflection effects
     *
     * @param closestPoint the point to find the color of it
     *                     (note : using the method through traceRay(Ray), it is the closest point of
     *                     intersection with the Ray in parameter of traceRay(Ray))
     * @param ray          between the viewer and the object.
     * @return the primitives.Color of closestPoint.
     */
    private Color calcColor(GeoPoint closestPoint, Ray ray) {
        if (closestPoint == null) { // add it
            return _scene._ambientLight.getIntensity();
        }

        return calcColor(closestPoint, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K)
                .add(_scene._ambientLight.getIntensity());

    }

    /**
     * To return the color of a point.
     * Helper method.
     * The method :
     * - to find the color of the light emitted by the object.
     * - for each light source, to add : [ kD * |l.n| + kS * (max(0,-v.r))^nShininess ] * Il
     * with calcGlobalEffects(Geopoint, Ray, int level, double k) :
     * - to add refraction / reflection effects
     *
     * @param intersection the Geopoint of intersection to calculate the color of it.
     * @param ray          the ray.
     * @param level        the level of recursion for calcGlobalEffects.
     * @param k
     * @return the color of the point of intersection.
     */
    private Color calcColor(GeoPoint intersection, Ray ray, int level, double k) {
        if (intersection == null) { // add it
            return Color.BLACK;
        }
        Color color = intersection._geometry.getEmission();
        color = color.add(calcLocalEffects(intersection, ray, k));
        return 1 == level ? color : color.add(calcGlobalEffects(intersection, ray, level, k));
    }

    /**
     * Helper method, to calculate the lightSourcePart.
     *
     * @param intersection the point to calculate its light intensity
     * @param ray          between the viewer and the object
     * @param k
     * @return sum of  [ kD * |l.n| + kS * (max(0,-v.r))^nShininess ] * Il   of all lightSources
     */
    private Color calcLocalEffects(GeoPoint intersection, Ray ray, double k) {
        Geometry geometry = intersection._geometry;
        Point3D point = intersection._point;
        Material material = geometry.getMaterial();

        Vector v = ray.getDir();
        Vector n = geometry.getNormal(point);
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0) {
            return Color.BLACK;
        }

        double kD = material._kD;
        double kS = material._kS;
        int nShininess = material._nShininess;

        Color color = Color.BLACK;
        for (LightSource light : _scene._lights) {
            Vector l = light.getL(point);
            Vector towardsLight = l.scale(-1);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) {      // sign(nl) == sign(nv), to fix wrong illumination
                //if (unshaded(l, n, light, intersection)) {
                double ktr;
                if (softShadowsFlag) {
                    ktr = transparencyAndSoftShadows(towardsLight, n, light, intersection);
                } else {
                    ktr = transparency(towardsLight, n, light, intersection);
                }

                if (ktr * k > MIN_CALC_COLOR_K) {
                    Color lightIntensity = light.getIntensity(point).scale(ktr);
                    color = color.add(calcDiffusive(kD, nl, lightIntensity),
                            calcSpecular(kS, l, n, v, nShininess, lightIntensity));
                }
            }
        }
        return color;
    }

    /**
     * Helper method, to calculate the diffusive part of the effect of a lightSource at a point.
     *
     * @param kD             diffusion attenuation factor
     * @param nl             the dot product of n and l
     * @param lightIntensity of the lightSource
     * @return kD * |l.n| * Il
     */
    private Color calcDiffusive(double kD, double nl, Color lightIntensity) {
        nl = nl > 0 ? nl : -nl;
        return lightIntensity.scale(kD * nl);
    }

    /**
     * Helper method, to calculate the specular part of the effect of a lightSource at a point.
     *
     * @param kS             specular attenuation factor
     * @param l
     * @param n
     * @param v
     * @param nShininess     of the material of the geometry
     * @param lightIntensity of the lightSource
     * @return kS * (max(0,-v.r))^nShininess * Il
     */
    private Color calcSpecular(double kS, Vector l, Vector n, Vector v, int nShininess,
                               Color lightIntensity) {
        double nl = alignZero(n.dotProduct(l));
        Vector r = (l.subtract(n.scale(2 * nl))).normalized();
        double vr = alignZero((v.scale(-1)).dotProduct(r));
        if (!(vr > 0))
            vr = 0;
        double vrn = Math.pow(vr, nShininess);
        return lightIntensity.scale(kS * vrn);
    }

    /**
     * To check if a 3D-object is between the light source and the geopoint.
     *
     * @param l     the direction of the light source.
     * @param n     the normal of gp._geometry at gp._point.
     * @param light the current light source.
     * @param gp    the geopoint to check if it is unshaded or not.
     * @return true if there is no such object (the point is unshaded), false either.
     */
    private boolean unshaded(Vector l, Vector n, LightSource light, GeoPoint gp) {
        Vector lightDirection = l.scale(-1); // from point to light source
        // from point to light source, offset by delta
        Ray lightRay = new Ray(gp._point, lightDirection, n);
        List<GeoPoint> intersections = _scene._geometries.findGeoIntersections(lightRay);

        if (intersections == null) {
            return true;
        }

        double lightDistance = light.getDistance(gp._point);
        for (GeoPoint intersection : intersections) {
            if (alignZero(intersection._point.distance(gp._point) - lightDistance) <= 0
                    // part about transparency
                    && intersection._geometry.getMaterial()._kT == 0) {
                return false;
            }
        }

        return true;

    }


    /**
     *
     * @param gp
     * @param ray
     * @param level
     * @param k
     * @return
     *
     */
    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, double k) {
        Color color = Color.BLACK;
        Vector n = gp._geometry.getNormal(gp._point);
        Material material = gp._geometry.getMaterial();

        double kkr = k * material._kR;
        if (kkr > MIN_CALC_COLOR_K) {
            color = calcGlobalEffect(constructReflectedRay(gp, ray, n), level, material._kR, kkr);
        }

        double kkt = k * material._kT;
        if (kkt > MIN_CALC_COLOR_K) {
            color = color.add
                    (calcGlobalEffect(constructRefractedRay(gp, ray, n), level, material._kT, kkt));
            //(gp._point, v, n)
        }

        return color;
    }

    private Color calcGlobalEffect(Ray ray, int level, double kx, double kkx) {
        GeoPoint gp = findClosestIntersection(ray);
        return (gp == null ? _scene._background : calcColor(gp, ray, level - 1, kkx).scale(kx));
    }

    /**
     * To construct the refracted ray of the ray at the point of gp.
     * In that implementation, all geometries with the same refraction index 1.
     * So the refracted ray has the same direction as the original one, and starts from the point of gp.
     *
     * @param gp  the point and its geometry.
     * @param ray the ray to be refracted.
     * @return the refracted ray, starting at the point of gp.
     */
    private Ray constructRefractedRay(GeoPoint gp, Ray ray, Vector n) {

        return new Ray(gp._point, ray.getDir(), n);
    }

    /**
     * To construct the reflected ray of the ray at the point of gp.
     * Calculation of the reflected ray : r = v - 2 * (v . n) . n
     * The reflected ray has the direction r, and starts from the point of gp.
     *
     * @param gp  the point and its geometry.
     * @param ray the ray to be refracted.
     * @return the reflected ray, starting at the point of gp.
     */
    private Ray constructReflectedRay(GeoPoint gp, Ray ray, Vector n) {
        Vector v = ray.getDir();
        double vn = alignZero(v.dotProduct(n));
        if (isZero(vn)) {//TODO add it because failed
            return null;
        }
        Vector vnn2 = n.scale(2 * vn);
        Vector r = v.subtract(vnn2);

        return new Ray(gp._point, r, n);
    }

    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> intersections = _scene._geometries.findGeoIntersections(ray);
        if (intersections == null) {
            return null;
        } else {
            return ray.findClosestGeoPoint(intersections);
        }
    }

    /**
     * To calculate the proportion of shadowing on GeoPoint gp.
     * If a geometry is between gp and the light source,
     * take into account the transparency coefficient of that geometry,
     * in order to calculate the proportion of shadowing.
     * Refactor : l (direction from light source to point) -> towardsLight (direction from point to light source)
     * @param towardsLight     the direction from point to the light source.
     * @param n     the normal of gp._geometry at gp._point.
     * @param light the current light source.
     * @param gp    the geopoint to check if it is unshaded or not.
     * @return 1.0 if there is no such object (no geometry between gp and the light source),
     * the proportion of shadowing either.
     */
    private double transparency(Vector towardsLight, Vector n, LightSource light, GeoPoint gp) {
        // from point to light source, offset by delta
        Ray lightRay = new Ray(gp._point, towardsLight, n);
        List<GeoPoint> intersections = _scene._geometries.findGeoIntersections(lightRay);

        if (intersections == null) {
            return 1.0;
        }

        double lightDistance = light.getDistance(gp._point);
        double ktr = 1.0;
        for (GeoPoint intersection : intersections) {
            if (alignZero(intersection._point.distance(gp._point) - lightDistance) <= 0) {
                ktr *= intersection._geometry.getMaterial()._kT;
                if (ktr < MIN_CALC_COLOR_K) {
                    return 0.0;
                }
            }
        }

        return ktr;
    }


    /**
     * To calculate soft shadows - deterministic.
     * The method : averaging the coefficient ktr returned by transparency() of sample rays
     * forming a pyramid around towardsLight, with square placed at light source as base of the pyramid.
     * More in details :
     * - finding the plane containing that square :
     * supposing a square orthogonal to towardsLight, and the position of light (P0) is its central point
     * - finding two orthogonal vectors contained in that plane, in order to move from "pixel" to "pixel"
     * in the square :
     * supposing another point P contained in that plane
     * => towardsLight . (P - P0) = 0
     * => <a, b, c> . <x - x0, y - y0, z - z0> = 0
     * => a * (x - x0) + b * (y - y0) + c * (z - z0) = 0
     * supposing x - x0 = 1, y - y0 = 0 => x = 1 + x0, y = y0, z = z0 - a / c
     * => the vector from P0 to P (v1) is contained in the plane of the square
     * crossProduct with v1 and towardsLight to find another vector (v2),
     * contained in that plane and orthogonal to v1
     * - the edge size of the square is calculated using _angle : 2 * distance * tan(_angle)
     * - finding the limits of the square and the step between two sample rays
     * - finding the ktr value (proportion of shadowing, return value of transparency()) for each ray
     * - summing up all the ktr values, then making average
     * Adaptive (first approximation based on 5 rays)
     * @param towardsLight     the direction from point to light source
     * @param n     the normal of gp._geometry at gp._point.
     * @param light the current light source.
     * @param gp    the geopoint to check if it is unshaded or not.
     * @return the average of the proportion of shadowing of the sample rays.
     */
    private double transparencyAndSoftShadows(Vector towardsLight, Vector n, LightSource light, GeoPoint gp) {

        Geometry geometry = gp._geometry;
        Point3D point = gp._point;

        // distance from point to light source
        double distanceFull = (light instanceof DirectionalLight) ?
                ((DirectionalLight) light).getFakeDistance(point) : light.getDistance(point);
        // P0 of the plane containing the square for the sample rays
        Point3D squarePosition = point.add(towardsLight.scale(distanceFull));

        // towardsLight is the normal to plane containing the square for the sample rays
        Point3D head = towardsLight.getHead();
        // getting one vector contained in the plane of the square
        Point3D P1 = new Point3D(1 + squarePosition.getX(),
                squarePosition.getY(),
                squarePosition.getZ() - head.getX() / head.getZ());
        Vector v1 = new Vector(P1);

        // getting another vector contained in the plane of the square, perpendicular to the first one
        Vector v2 = v1.crossProduct(towardsLight);

        // to get the limits of the square and the width of one of its "pixels"
        int halfStepsNumber = (_numOfSamples - 1) / 2; // odd number of samples
        // 0.005 degrees ~ 0.00087 radians => small angle approximation tan(_angle) ~ _angle
        double edgeSize = 2 * distanceFull * _tan_angle;
        double stepSize = edgeSize / _numOfSamples;
        int lowerBound = -halfStepsNumber;
        int upperBound = halfStepsNumber;

        Point3D currentPoint;
        Vector currentDir;

        // first approximation
        double fsKtr = 0.0;
        currentPoint =
                squarePosition.add(v1.scale(-edgeSize/2)).add(v2.scale(-edgeSize/2));
        currentDir = currentPoint.subtract(point);
        fsKtr += transparency(currentDir, n, light, gp);
        currentPoint =
                squarePosition.add(v1.scale(-edgeSize/2)).add(v2.scale(edgeSize/2));
        currentDir = currentPoint.subtract(point);
        fsKtr += transparency(currentDir, n, light, gp);
        currentPoint =
                squarePosition.add(v1.scale(edgeSize/2)).add(v2.scale(-edgeSize/2));
        currentDir = currentPoint.subtract(point);
        fsKtr += transparency(currentDir, n, light, gp);
        currentPoint =
                squarePosition.add(v1.scale(edgeSize/2)).add(v2.scale(edgeSize/2));
        currentDir = currentPoint.subtract(point);
        fsKtr += transparency(currentDir, n, light, gp);
        double basicKtr = transparency(towardsLight, n, light, gp);
        fsKtr += basicKtr;
        double fsAvgKtr = fsKtr / 5;
        double difference = fsAvgKtr - basicKtr;
        if (isZero(alignZero(difference))) {
            //System.out.print("basic : ");
            //System.out.println(difference);
            return fsAvgKtr;
        }

        //System.out.print("fullSamples : ");
        //System.out.println(difference);
        // to sum up the ktr of the sample rays
        double totalKtr = 0.0;

        // travelling through the "pixels" of the square of sample rays
        for (int i = lowerBound; i <= upperBound; i++) {
            for (int j = lowerBound; j <= upperBound; j++) {
                // getting the direction from point to the current "pixel" (currentPoint)
                currentPoint =
                        squarePosition.add(v1.scale(i * stepSize)).add(v2.scale(j * stepSize));
                currentDir = currentPoint.subtract(point);

                totalKtr += transparency(currentDir, n, light, gp);

            }
        }

        // averaging
        double resultKtr = totalKtr / (_numOfSamples * _numOfSamples);

        return resultKtr;

    }


    /**
     * To set on the feature soft shadows.
     * @param numOfSamples num of sample rays for anti aliasing effect.
     * @return this.
     */
    public BasicRayTracer setSoftShadows(int numOfSamples) {
        softShadowsFlag = true;

        // so that numOfSamples is an odd number
        if (numOfSamples % 2 == 0) {
            numOfSamples += 1;
        }

        if (numOfSamples < DEFAULT_NUM_OF_SAMPLES) {
            _numOfSamples = DEFAULT_NUM_OF_SAMPLES;
        } else {
            _numOfSamples = numOfSamples;
        }

        return this;
    }

    /**
     * To set on the feature soft shadows.
     * Wrapper method.
     * Chaining method.
     * @return this.
     */
    public BasicRayTracer setSoftShadows() {
       return setSoftShadows(DEFAULT_NUM_OF_SAMPLES);
    }

}