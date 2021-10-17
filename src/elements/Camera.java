package elements;

import primitives.*;

import static primitives.Util.isZero;


/**
 * Class Camera, representing a camera in 3D.
 * Its position is represented by a Point3D (_p0), and its direction by 3 orthogonal Vectors (_vUp, _vTo, _vRight).
 * Its distance from the View Plane is represented by _distance.
 * Also include information about the View Plane : size (_width, _height) and resolution (Nx, Ny pixels).
 *
 * @author Deborah Lellouche
 */
public class Camera {
    /**
     * Point3D object representing the position of the Camera in 3D.
     */
    final Point3D _p0;

    /**
     * Vector object representing the vector from the Camera to the up.
     */
    final Vector _vUp;

    /**
     * Vector object representing the vector from the Camera to the scene.
     */
    final Vector _vTo;

    /**
     * Vector object representing the vector from the Camera to its right.
     */
    final Vector _vRight;

    /**
     * Double representing the width of the View Plane of the Camera.
     * Initial value = 0.
     */
    private double _width = 0;

    /**
     * Double representing the height of the View Plane of the Camera.
     * Initial value = 0.
     */
    private double _height = 0;

    /**
     * Double representing the distance from _p0 to the View Plane of the Camera.
     * Initial value = 0.
     */
    private double _distance = 0;


    /**
     * Camera constructor, receiving 3 parameters : p0, vUp, vTo.
     * Checking of the orthogonality of vUp and vTo.
     * vRight = vTo X vUp
     * The final vectors are in normalized form.
     * @param p0 a Point3D object representing the position of the Camera.
     * @param vTo a Vector object representing the vector from the Camera to the scene.
     * @param vUp a Vector object representing the vector from the Camera to the up.
     * @throws IllegalArgumentException if vUp and vTo are not orthogonal.
     */
    public Camera(Point3D p0, Vector vTo, Vector vUp) {

        if (!isZero(vUp.dotProduct(vTo))) {
            throw new IllegalArgumentException("vUp and vTo must be orthogonal");
        }

        _p0 = p0;
        _vUp = vUp.normalized();
        _vTo = vTo.normalized();
        _vRight = _vTo.crossProduct(_vUp).normalized();
    }

    /**
     * To set the size of the View Plane of the Camera.
     * From Design Pattern "Builder".
     * @param width a double representing the width of the View Plane. Must be > 0.
     * @param height a double representing the height of the View Plane. Must be > 0.
     * @return this, the Camera object itself.
     * @throws IllegalArgumentException if width or height <= 0.
     */
    public Camera setViewPlaneSize(double width, double height) {
        if ((width <= 0) || (height <= 0)) {
            throw new IllegalArgumentException("width and height must be > 0");
        }

        _width = width;
        _height = height;
        return this;
    }

    /**
     * To set the distance between _p0 and the View Plane of the Camera.
     * @param distance a double representing the distance between _p0 and the View Plane. Must be >= 0.
     * @return this, the Camera object itself.
     * @throws IllegalArgumentException if distance < 0.
     */
    public Camera setDistance(double distance) {
        if (distance < 0) {
            throw new IllegalArgumentException("distance must be >= 0");
        }

        _distance = distance;
        return this;
    }

    /**
     * To construct a ray through a pixel. The method :
     * Image center : pc = _p0 + d . vTo
     * Ratio (pixel's width and height) : Rx = w / nX   ; Ry = h / nY
     * Pixel[i,j]'s center : Pij = pc + [(j - (nX - 1) / 2) . Rx . _vRight - (i - (nY - 1) / 2) . Ry . _vUp]
     * Vector Vij = Pij - _p0
     * @param nX an integer representing the number of columns.
     * @param nY an integer representing the number of lines.
     * @param j an integer representing the index of the column.
     * @param i an integer representing the index of the line.
     * @return a new Ray, with _p0 = _p0 of the Camera, and direction = Vij.
     * @throws IllegalArgumentException if nX or nY <= 0.
     * @throws IllegalStateException if width and height have not been initiated.
     */
    public Ray constructRayThroughPixel(int nX, int nY, double j, double i) {
        if ((nX <= 0) || (nY <= 0)) {
            throw new IllegalArgumentException("nX and nY must be > 0");
        }

        Point3D Pc = _p0.add(_vTo.scale(_distance));

        if ((_width == 0) || (_height == 0)) {
            throw new IllegalStateException(
                    "width and height of the View Plane cannot be 0" +
                    "(use setViewPlaneSize to initiate them");
        }
        double Rx = _width / nX;    // width of one pixel
        double Ry = _height / nY;   // height of one pixel

        double Xj = (j - (nX - 1) / 2d) * Rx;
        double Yi = - (i - (nY - 1) / 2d) * Ry;

        // to avoid Vector (0,0,0) with scale, checking of the value of Xj and Yi
        Point3D Pij;
        if (isZero(Xj) && isZero(Yi)) {
            Pij = Pc;
        }
        else if (isZero(Xj)) {
            Pij = Pc.add(_vUp.scale(Yi));
        }
        else if (isZero(Yi)) {
            Pij = Pc.add(_vRight.scale(Xj));
        }
        else {
            Pij = Pc.add(_vRight.scale(Xj)).add(_vUp.scale(Yi));
        }

        Vector Vij = Pij.subtract(_p0);                             // a Vector from _p0 to Pij

        return new Ray(_p0, Vij);

    }

    /**
     * Getter of _p0.
     * @return _p0.
     */
    public Point3D getP0() {
        return _p0;
    }

    /**
     * Getter of _vUp.
     * @return _vUp.
     */
    public Vector getvUp() {
        return _vUp;
    }

    /**
     * Getter of _vTo.
     * @return _vTo.
     */
    public Vector getvTo() {
        return _vTo;
    }

    /**
     * Getter of vRight.
     * @return _vRight.
     */
    public Vector getvRight() {
        return _vRight;
    }

    /**
     * Getter of _width.
     * @return the value of _width.
     */
    public double getWidth() {
        return _width;
    }

    /**
     * Getter of _height.
     * @return the value of _height.
     */
    public double getHeight() {
        return _height;
    }
}
