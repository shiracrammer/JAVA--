package renderer;

import elements.Camera;
import primitives.Color;
import primitives.Ray;

import java.util.MissingResourceException;

import static primitives.Util.alignZero;

/**
 * Class Render, to make the matrix of the colors of the image.
 * Partly build on the principe of Builder Pattern.
 *
 * @author Deborah Lellouche
 * with adding of Multithreading features from file of Dan Zilberstein
 */
public class Render {

     // For Anti-Aliasing effect.
    /**
     * Is anti-aliasing feature set on or set off.
     */
    private boolean antiAliasingFlag = false;
    /**
     * Default number of samples per side.
     * Must be a positive odd number.
     */
    private static final int DEFAULT_NUM_OF_SAMPLES = 9;
    /**
     * Number of samples per side of the pixel.
     * Total number of samples : _numOfSamples * _numOfSamples;
     */
    private int _numOfSamples = DEFAULT_NUM_OF_SAMPLES;
    /**
     * Default threshold value, to determine if the difference between rays' colors is significant.
     */
    private static final double DEFAULT_THRESHOLD = 1.0; // 0.001 = 3, 0.01
    /**
     * Threshold to determine if the difference between rays' colors is significant.
     */
    private double _threshold = DEFAULT_THRESHOLD;
    /**
     * Which quadrant of the previous (sub)pixel is the current (sub)pixel.
     */
    private enum Quadrant {INITIAL, LEFT_UP, RIGHT_UP, LEFT_DOWN, RIGHT_DOWN}

    /**
     * ImageWriter of the render.
     */
    ImageWriter _imageWriter;

    /**
     * Camera of the render.
     */
    Camera _camera;

    /**
     * RayTracer of the render.
     */
    RayTracerBase _rayTracer;


    /**
     * Setter of _imageWriter.
     * Chaining method.
     *
     * @param imageWriter the new value for _imageWriter.
     * @return this.
     */
    public Render setImageWriter(ImageWriter imageWriter) {
        _imageWriter = imageWriter;
        return this;
    }

    /**
     * Setter of _camera.
     * Chaining method.
     *
     * @param camera the new value for _camera.
     * @return this.
     */
    public Render setCamera(Camera camera) {
        _camera = camera;
        return this;
    }

    /**
     * Setter of _rayTracer.
     * Chaining method.
     *
     * @param rayTracer the new value for _rayTracer.
     * @return this.
     */
    public Render setRayTracer(RayTracerBase rayTracer) {
        _rayTracer = rayTracer;
        return this;
    }

    /**
     * To render the image, read to color each pixel of the image according to the intersection
     * between the rays of _camera with _scene,
     * with the use of _rayTrace to find this color and _imageWriter to color the pixel with that color.
     * (note : building of that function is according to the given RenderTests, and not to Recitation 5)
     *
     * @throws MissingResourceException if one among _imageWriter, _camera or _rayTracer is null.
     * @throws IllegalArgumentException if in _imageWriter, nX or nY <= 0.
     */
    public void renderImage()
            throws MissingResourceException, IllegalArgumentException {
        if (_imageWriter == null || _camera == null || _rayTracer == null) {
            throw new MissingResourceException
                    ("at least one of the fields of the Render is null", "Render", "");
        }

        int nX = _imageWriter.getNx();
        int nY = _imageWriter.getNy();
        if (nX <= 0 || nY <= 0) {
            throw new IllegalArgumentException(
                    "imageName must be a non null string and the number of pixels must be > 0");
        }

        if (threadsCount == 0) {
            for (int i = 0; i < nY; i++) {
                for (int j = 0; j < nX; j++) {
                    System.out.println("for the pixel : " + i + ", " + j);
                    // for the pixel (j,i)
                    // casting a ray from _camera to the pixel (j,i)
                    // tracing the ray to find the color of its closest point of intersection
                    // coloring the pixel (j,i)
                    if (antiAliasingFlag) {
                        //castRayForAntiAliasing1(nX, nY, j, i);
                        //castRayForAntiAliasing2(nX, nY, j, i);
                        //castRayForAntiAliasing3(nX, nY, j, i);
                        castRayForAntiAliasing4(nX, nY, j, i);
                    } else {
                        castRay(nX, nY, j, i);
                    }
                }
            }
        } else {
            renderImageThreaded();
        }
    }


    /**
     * To add a grid on the image of _imageWriter.
     *
     * @param interval the interval between the bars of the grid
     *                 (on the X axis and on the Y axis).
     * @param color    the Color of the grid.
     * @throws MissingResourceException if _imageWriter is null.
     */
    public void printGrid(int interval, Color color) throws MissingResourceException {
        // checking the resources
        if (_imageWriter == null) {
            throw new MissingResourceException(
                    "_imageWriter of the Render object is null", "Render", "");
        }

        int nX = _imageWriter.getNx();
        int nY = _imageWriter.getNy();

        // printing the grid
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                if (i % interval == 0 || j % interval == 0) {
                    _imageWriter.writePixel(j, i, color);
                }
            }
        }
    }

    /**
     * To check that _imageWriter is not null before calling to its method writeToImage().
     *
     * @throws MissingResourceException if _imageWriter is null.
     */
    public void writeToImage() throws MissingResourceException {
        // checking the resources
        if (_imageWriter == null) {
            throw new MissingResourceException(
                    "_imageWriter of the Render object is null", "Render", "");
        }

        _imageWriter.writeToImage();
    }


// Multithreading part

    private int threadsCount = 0;
    private static final int SPARE_THREADS = 2; // Spare threads if trying to use all the cores
    private boolean print = false; // printing progress percentage

    /**
     * Set multi-threading <br>
     * - if the parameter is 0 - number of cores less 2 is taken
     *
     * @param threads number of threads
     * @return the Render object itself
     */
    public Render setMultithreading(int threads) {
        if (threads < 0)
            throw new IllegalArgumentException("Multithreading parameter must be 0 or higher");
        if (threads != 0)
            this.threadsCount = threads;
        else {
            int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
            this.threadsCount = cores <= 2 ? 1 : cores;
        }
        return this;
    }

    /**
     * Set debug printing on
     *
     * @return the Render object itself
     */
    public Render setDebugPrint() {
        print = true;
        return this;
    }

    /**
     * Pixel is an internal helper class whose objects are associated with a Render
     * object that they are generated in scope of. It is used for multithreading in
     * the Renderer and for follow up its progress.<br/>
     * There is a main follow up object and several secondary objects - one in each
     * thread.
     *
     * @author Dan
     */
    private class Pixel {
        private long maxRows = 0;
        private long maxCols = 0;
        private long pixels = 0;
        public volatile int row = 0;
        public volatile int col = -1;
        private long counter = 0;
        private int percents = 0;
        private long nextCounter = 0;

        /**
         * The constructor for initializing the main follow up Pixel object
         *
         * @param maxRows the amount of pixel rows
         * @param maxCols the amount of pixel columns
         */
        public Pixel(int maxRows, int maxCols) {
            this.maxRows = maxRows;
            this.maxCols = maxCols;
            this.pixels = (long) maxRows * maxCols;
            this.nextCounter = this.pixels / 100;
            if (Render.this.print)
                System.out.printf("\r %02d%%", this.percents);
        }

        /**
         * Default constructor for secondary Pixel objects
         */
        public Pixel() {
        }

        /**
         * Internal function for thread-safe manipulating of main follow up Pixel object
         * - this function is critical section for all the threads, and main Pixel
         * object data is the shared data of this critical section.<br/>
         * The function provides next pixel number each call.
         *
         * @param target target secondary Pixel object to copy the row/column of the
         *               next pixel
         * @return the progress percentage for follow up: if it is 0 - nothing to print,
         * if it is -1 - the task is finished, any other value - the progress
         * percentage (only when it changes)
         */
        private synchronized int nextP(Pixel target) {
            ++col;
            ++this.counter;
            if (col < this.maxCols) {
                target.row = this.row;
                target.col = this.col;
                if (Render.this.print && this.counter == this.nextCounter) {
                    ++this.percents;
                    this.nextCounter = this.pixels * (this.percents + 1) / 100;
                    return this.percents;
                }
                return 0;
            }
            ++row;
            if (row < this.maxRows) {
                col = 0;
                target.row = this.row;
                target.col = this.col;
                if (Render.this.print && this.counter == this.nextCounter) {
                    ++this.percents;
                    this.nextCounter = this.pixels * (this.percents + 1) / 100;
                    return this.percents;
                }
                return 0;
            }
            return -1;
        }

        /**
         * Public function for getting next pixel number into secondary Pixel object.
         * The function prints also progress percentage in the console window.
         *
         * @param target target secondary Pixel object to copy the row/column of the
         *               next pixel
         * @return true if the work still in progress, -1 if it's done
         */
        public boolean nextPixel(Pixel target) {
            int percent = nextP(target);
            if (Render.this.print && percent > 0)
                synchronized (this) {
                    notifyAll();
                }
            if (percent >= 0)
                return true;
            if (Render.this.print)
                synchronized (this) {
                    notifyAll();
                }
            return false;
        }

        /**
         * Debug print of progress percentage - must be run from the main thread
         */
        public void print() {
            if (Render.this.print)
                while (this.percents < 100)
                    try {
                        synchronized (this) {
                            wait();
                        }
                        System.out.printf("\r %02d%%", this.percents);
                        System.out.flush();
                    } catch (Exception e) {
                    }
        }

    }

    /**
     * Cast ray from camera in order to color a pixel
     *
     * @param nX  resolution on X axis (number of pixels in row)
     * @param nY  resolution on Y axis (number of pixels in column)
     * @param col pixel's column number (pixel index in row)
     * @param row pixel's row number (pixel index in column)
     */
    private void castRay(int nX, int nY, int col, int row) {
        Ray ray = _camera.constructRayThroughPixel(nX, nY, col, row);
        Color color = _rayTracer.traceRay(ray);
        _imageWriter.writePixel(col, row, color);
    }

    /**
     * This function renders image's pixel color map from the scene included with
     * the Renderer object - with multi-threading
     */
    private void renderImageThreaded() {
        final int nX = _imageWriter.getNx();
        final int nY = _imageWriter.getNy();
        final Pixel thePixel = new Pixel(nY, nX);
        // Generate threads
        Thread[] threads = new Thread[threadsCount];
        for (int i = threadsCount - 1; i >= 0; --i) {
            threads[i] = new Thread(() -> {
                Pixel pixel = new Pixel();
                while (thePixel.nextPixel(pixel)) {
                    System.out.println("for the pixel : " + pixel.row + ", " + pixel.col);
                    if (antiAliasingFlag) {
                        //castRayForAntiAliasing1(nX, nY, pixel.col, pixel.row);
                        //castRayForAntiAliasing2(nX, nY, pixel.col, pixel.row);
                        //castRayForAntiAliasing3(nX, nY, pixel.col, pixel.row);
                        castRayForAntiAliasing4(nX, nY, pixel.col, pixel.row);
                    } else {
                        castRay(nX, nY, pixel.col, pixel.row);
                    }
                }
            });
        }
        // Start threads
        for (Thread thread : threads)
            thread.start();

        // Print percents on the console
        thePixel.print();

        // Ensure all threads have finished
        for (Thread thread : threads)
            try {
                thread.join();
            } catch (Exception e) { }

        if (print)
            System.out.print("\r100%");
    }


    // Mini-Project 1 part

    /**
     * Supersampling for anti-aliasing - deterministic.
     * Casting sample rays in the form of a pyramid with the pixel at [row][col] as the base of the pyramid.
     * The method : deterministic, one sample ray by subpixel, placed at the center of the subpixel.
     * @param nX  resolution on X axis (number of pixels in row)
     * @param nY  resolution on Y axis (number of pixels in column)
     * @param col pixel's column number (pixel index in row)
     * @param row pixel's row number (pixel index in column)
     */
    private void castRayForAntiAliasing1(int nX, int nY, int col, int row) {
        Color color = new Color(Color.BLACK);

        for (int ii = 0; ii < _numOfSamples; ii++) {
            double i1 = row + ((double) ii + 0.5) / _numOfSamples;
            for (int jj = 0; jj < _numOfSamples; jj++) {
                double j1 = col + ((double) jj + 0.5) / _numOfSamples;
                Ray ray = _camera.constructRayThroughPixel(nX, nY, j1, i1);
                Color color2 = _rayTracer.traceRay(ray);
                color = color.add(color2);
            }
        }
        color = color.scale(1d / (Math.pow(_numOfSamples, 2)));
        _imageWriter.writePixel(col, row, color);
    }


    /**
     * Supersampling for anti-aliasing - random.
     * Casting sample rays in the form of a pyramid with the pixel at [row][col] as the base of the pyramid.
     * The method : random, one sample ray by subpixel, placed at a random part of the subpixel.
     * @param nX  resolution on X axis (number of pixels in row)
     * @param nY  resolution on Y axis (number of pixels in column)
     * @param col pixel's column number (pixel index in row)
     * @param row pixel's row number (pixel index in column)
     */
    private void castRayForAntiAliasing2(int nX, int nY, int col, int row) {
        Color color = new Color(Color.BLACK);
        for (int ii = 0; ii < _numOfSamples; ii++) {
            for (int jj = 0; jj < _numOfSamples; jj++) {
                double i1 = row + (((double) ii) + Math.random()) / _numOfSamples;
                double j1 = col + (((double) jj) + Math.random()) / _numOfSamples;
                Ray ray = _camera.constructRayThroughPixel(nX, nY, j1, i1);
                Color color2 = _rayTracer.traceRay(ray);
                color = color.add(color2);
            }
        }
        color = color.scale(1d / (Math.pow(_numOfSamples, 2)));
        _imageWriter.writePixel(col, row, color);
    }

    /**
     * Supersampling for anti-aliasing - adaptive.
     * Casting sample rays in the form of a pyramid with the pixel at [row][col] as the base of the pyramid.
     * The method : deterministic, one sample ray by subpixel, placed at the center of the subpixel.
     * Adaptive (first approximation based on 5 rays)
     * @param nX  resolution on X axis (number of pixels in row)
     * @param nY  resolution on Y axis (number of pixels in column)
     * @param col pixel's column number (pixel index in row)
     * @param row pixel's row number (pixel index in column)
     */
    private void castRayForAntiAliasing3(int nX, int nY, int col, int row) {
        double Rx = _camera.getWidth() / nX;    // width of one pixel
        double Ry = _camera.getHeight() / nY;   // height of one pixel

        Color color = new Color(Color.BLACK);

        // first approximation
        Color basicColor =
                _rayTracer.traceRay(_camera.constructRayThroughPixel(nX, nY, col + 0.5*Rx, row + 0.5*Ry));
        Color fsColor = new Color(Color.BLACK);
        Color color1 = _rayTracer.traceRay(_camera.constructRayThroughPixel(nX, nY, col, row));
        Color color2 = _rayTracer.traceRay(_camera.constructRayThroughPixel(nX, nY, col + 0.99*Rx, row));
        Color color3 = _rayTracer.traceRay(_camera.constructRayThroughPixel(nX, nY, col, row + 0.99*Ry));
        Color color4 = _rayTracer.traceRay(_camera.constructRayThroughPixel(nX, nY, col + 0.99*Rx, row + 0.99*Ry));
        fsColor = fsColor.add(color1, color2, color3, color4, basicColor, basicColor, basicColor, basicColor)
                .scale(1d/8);

        double difference =
                alignZero(basicColor.r - fsColor.r
                        + basicColor.g - fsColor.g
                        + basicColor.b - fsColor.b);
        if (Math.abs(difference) < _threshold) {
            _imageWriter.writePixel(col, row, fsColor);
            return;
        }

        for (int ii = 0; ii < _numOfSamples; ii++) {
            double i1 = row + ((double) ii + 0.5) / _numOfSamples;
            for (int jj = 0; jj < _numOfSamples; jj++) {
                double j1 = col + ((double) jj + 0.5) / _numOfSamples;
                Ray ray = _camera.constructRayThroughPixel(nX, nY, j1, i1);
                Color currentColor = _rayTracer.traceRay(ray);
                color = color.add(currentColor);
            }
        }
        color = color.scale(1d / (Math.pow(_numOfSamples, 2)));
        _imageWriter.writePixel(col, row, color);
    }


    /**
     * Supersampling for anti-aliasing - adaptive and recursive.
     * Casting sample rays in the form of a pyramid with the pixel at [row][col]
     * as the base of the pyramid.
     * Adaptive : approximations based on 5 sample rays (one for each corner + center),
     * recursion according to the need, that is to say if there is a significant difference
     * between the rays' colors.
     * The method : fractioning the pixel row,col into quadrants,
     * and according to the need, fractioning those quadrants into subquadrants, and so on,
     * until reaching total number of samples (according to _numOfSamples)
     * or until there is no significant difference between the sample rays' colors.
     * @param nX  resolution on X axis (number of pixels in row)
     * @param nY  resolution on Y axis (number of pixels in column)
     * @param col pixel's column number (pixel index in row)
     * @param row pixel's row number (pixel index in column)
     */
    private void castRayForAntiAliasing4(int nX, int nY, int col, int row) {
        Color fsColor = checkFirstSamples(nX, nY, col, row);
        _imageWriter.writePixel(col, row, fsColor);
    }

    /**
     * Wrapper function for checkFirstSamples.
     * @param nX  resolution on X axis (number of pixels in row)
     * @param nY  resolution on Y axis (number of pixels in column)
     * @param col pixel's column number (pixel index in row)
     * @param row pixel's row number (pixel index in column)
     * @return the color resulting of the average of the color of the sample rays for the pixel i,j.
     */
    public Color checkFirstSamples(int nX, int nY, double col, double row) {
        double Rx = _camera.getWidth() / nX;    // width of one pixel
        double Ry = _camera.getHeight() / nY;   // height of one pixel
        Color centerColor = _rayTracer.traceRay
                (_camera.constructRayThroughPixel(nX, nY, col + 0.5 * Rx, row + 0.5 * Ry));

        return checkFirstSamples(nX, nY, col, row,
                1, Rx, Ry,
                Quadrant.INITIAL, centerColor, centerColor);
    }

    /**
     * Helper function for castRayForAA4.
     * Recursive function.
     * At the stop condition, return average of the two colors in parameters.
     * Else, fractionate the (sub)pixel at row,col in four quadrants,
     * and for each of them, find its supersampling color,
     * and eventually, return the average colors of the four quadrants.
     * In order to find the supersampling color of each quadrant :
     * calculate the difference between its identifying corner's color
     * and the average of the other colors (center + 3 other corners) of the current (sub)pixel;
     * if the difference > _treshold, quadrant color is result of a recursive call;
     * else, quadrant color is the average of its identifying corner's color and current (sub)pixel center's color.
     * @param nX  resolution on X axis (number of pixels in row)
     * @param nY  resolution on Y axis (number of pixels in column)
     * @param col pixel's column number (pixel index in row)
     * @param row pixel's row number (pixel index in column.
     * @param levelDivisor coefficient to get the number of samples for the current (sub)pixel.
     * @param Rx width of one (sub)pixel.
     * @param Ry height of one (sub)pixel.
     * @param quadrant which quadrant of the previous (sub)pixel, in order not to calculate two times same rays :
     *                 0 - initial, for complete pixel
     *                 1 - left up quadrant, 2 - right up quadrant, 3 - left down quadrant, 4 - right down quadrant
     * @param cornerColor already calculated color of specific corner.
     * @param centerColor already calculated color of center of the subpixel.
     * @return the color resulting of the average of the color of the sample rays for the pixel i,j.
     */
    public Color checkFirstSamples(int nX, int nY, double col, double row, int levelDivisor, double Rx, double Ry,
                                   Quadrant quadrant, Color cornerColor, Color centerColor) {

        // number of samples of the current (sub)pixel, according to the level of the recursion
        double currentNumSamples = (int) (_numOfSamples / levelDivisor);

        // check stop condition
        if (currentNumSamples == 1) {
            System.out.println("stop condition : " + levelDivisor);
            return centerColor.add(cornerColor).scale(1/2d);
        }

        // odd number for number of samples per edge
        if (currentNumSamples % 2 == 0)
            currentNumSamples++;

        // color at the center of the (sub)pixel
        Color basicColor = _rayTracer.traceRay
                    (_camera.constructRayThroughPixel(nX, nY, col + 0.5 * Rx, row + 0.5 * Ry));

        // four samples, one per quadrant
        Color fsColor = new Color(Color.BLACK); // for sum up the samples and average
        Color color1 = new Color(Color.BLACK); // left up
        Color color2 = new Color(Color.BLACK); // right up
        Color color3 = new Color(Color.BLACK); // left down
        Color color4 = new Color(Color.BLACK); // right down

        // part to calculate the colors at the corners, according to the current quadrant (of the previous (sub)pixel);
        // in order to avoid calculating two times two of the four corners
        // (one time in the previous level, and one more time in the current level of recursion)
        switch (quadrant) {
            case INITIAL: // initial, for complete pixel
                color1 = _rayTracer.traceRay(_camera.constructRayThroughPixel
                        //(nX, nY, col + (0.5) * xStep, row + (0.5) * yStep));
                                (nX, nY, col, row));
                color2 = _rayTracer.traceRay(_camera.constructRayThroughPixel
                        //(nX, nY, col + (currentNumSamples - 0.5) * xStep, row + (0.5) * yStep));
                        //(nX, nY, col + (currentNumSamples) * xStep, row));
                                (nX, nY, col + 0.99 * Rx, row));
                color3 = _rayTracer.traceRay(_camera.constructRayThroughPixel
                        //(nX, nY, col + (0.5) * xStep, row + (currentNumSamples - 0.5) * yStep));
                        //(nX, nY, col, row + (currentNumSamples) * yStep));
                                (nX, nY, col, row + 0.99 * Ry));
                color4 = _rayTracer.traceRay(_camera.constructRayThroughPixel
                        //(nX, nY, col + (currentNumSamples - 0.5) * xStep, row + (currentNumSamples - 0.5) * yStep));
                        //(nX, nY, col + (currentNumSamples) * xStep, row + (currentNumSamples) * yStep));
                                (nX, nY, col + 0.99 * Rx, row + 0.99 * Ry));
                break;

            case LEFT_UP: // left up quadrant of previous (sub)pixel
                color1 = cornerColor;
                color4 = centerColor;

                color2 = _rayTracer.traceRay(_camera.constructRayThroughPixel
                        (nX, nY, col + 0.99 * Rx, row));
                color3 = _rayTracer.traceRay(_camera.constructRayThroughPixel
                        (nX, nY, col, row + 0.99 * Ry));
                break;

            case RIGHT_UP: // right up quadrant of previous (sub)pixel
                color2 = cornerColor;
                color3 = centerColor;

                color1 = _rayTracer.traceRay(_camera.constructRayThroughPixel
                        (nX, nY, col, row));
                color4 = _rayTracer.traceRay(_camera.constructRayThroughPixel
                        (nX, nY, col + 0.99 * Rx, row + 0.99 * Ry));
                break;

            case LEFT_DOWN: // left down quadrant of previous (sub)pixel
                color3 = cornerColor;
                color2 = centerColor;

                color1 = _rayTracer.traceRay(_camera.constructRayThroughPixel
                        (nX, nY, col, row));
                color4 = _rayTracer.traceRay(_camera.constructRayThroughPixel
                        (nX, nY, col + 0.99 * Rx, row + 0.99 * Ry));
                break;

            case RIGHT_DOWN: // right down quadrant of previous (sub)pixel
                color4 = cornerColor;
                color1 = centerColor;

                color2 = _rayTracer.traceRay(_camera.constructRayThroughPixel
                        (nX, nY, col + 0.99 * Rx, row));
                color3 = _rayTracer.traceRay(_camera.constructRayThroughPixel
                        (nX, nY, col, row + 0.99 * Ry));
                break;
        }

        // each quadrant of the current (sub)pixel is identified by one corner of the (sub)pixel;
        // for each corner, comparing with the average of the colors of the center and the three other corners;
        // if the difference > _treshold, recursive call;
        // else, averaging between corner's color and center's color;
        // the resulting color (for if and else) is attributed to the corresponding quadrant
        Color avgColorFor1 = fsColor.add(color2, color3, color4, basicColor).scale(1d/4);
        Color avgColorFor2 = fsColor.add(color1, color3, color4, basicColor).scale(1d/4);
        Color avgColorFor3 = fsColor.add(color1, color2, color4, basicColor).scale(1d/4);
        Color avgColorFor4 = fsColor.add(color1, color2, color3, basicColor).scale(1d/4);

        Color quadrantColor;    // averaged color for the quadrant
        double difference;      // difference between corner's color and the average of the colors of the other rays

        // for left up quadrant of current (sub)pixel
        // comparing with the average of the colors of the center and the three other corners;
        difference = alignZero(avgColorFor1.r - color1.r
                                + avgColorFor1.g - color1.g
                                + avgColorFor1.b - color1.b);
        // if the difference > _treshold, recursive call;
        if (Math.abs(difference) > _threshold) {
            quadrantColor = checkFirstSamples
                    (nX, nY, col, row,
                            levelDivisor * 2, Rx / 2.0, Ry / 2.0,
                            Quadrant.LEFT_UP, color1, basicColor);
            // else, averaging between corner's color and center's color;
        } else {
            quadrantColor = color1.add(basicColor).scale(1d/2);
        }
        // the resulting color (for if and else) is attributed to the corresponding quadrant
        fsColor = fsColor.add(quadrantColor);

        // for right up quadrant of current (sub)pixel
        difference = alignZero(avgColorFor2.r - color2.r
                        + avgColorFor2.g - color2.g
                        + avgColorFor2.b - color2.b);
        if (Math.abs(difference) > _threshold) {
            quadrantColor = checkFirstSamples
                    (nX, nY, col + 0.5 * Rx, row,
                            levelDivisor * 2,Rx / 2.0, Ry / 2.0,
                            Quadrant.RIGHT_UP, color2, basicColor);
        } else {
            quadrantColor = color2.add(basicColor).scale(1d/2);
        }
        fsColor = fsColor.add(quadrantColor);

        // for left down quadrant of current (sub)pixel
        difference = alignZero(avgColorFor3.r - color3.r
                + avgColorFor3.g - color3.g
                + avgColorFor3.b - color3.b);
        if (Math.abs(difference) > _threshold) {
            quadrantColor = checkFirstSamples
                    (nX, nY, col, row + 0.5 * Ry,
                            levelDivisor * 2, Rx / 2.0, Ry / 2.0,
                            Quadrant.LEFT_DOWN, color3, basicColor);
        } else {
            quadrantColor = color3.add(basicColor).scale(1d/2);
        }
        fsColor = fsColor.add(quadrantColor);

        // for right down quadrant of current (sub)pixel
        difference = alignZero(avgColorFor4.r - color4.r
                + avgColorFor4.g - color4.g
                + avgColorFor4.b - color4.b);
        if (Math.abs(difference) > _threshold) {
            quadrantColor = checkFirstSamples
                    (nX, nY, col + 0.5 * Rx, row + 0.5 * Ry,
                            levelDivisor * 2, Rx / 2.0, Ry / 2.0,
                            Quadrant.RIGHT_DOWN, color4, basicColor);
        } else {
            quadrantColor = color4.add(basicColor).scale(1d/2);
        }
        fsColor = fsColor.add(quadrantColor);

        // averaging the colors of the four quadrants of the (sub)pixel
        fsColor = fsColor.scale(1d/4);
        //System.out.println(counter + "  " + fsColor.r + ", " + fsColor.g + ", " + fsColor.b);
        //if (levelDivisor > 1)
          //  System.out.println("level : " + levelDivisor);
        return fsColor;
    }



    /**
     * To set on the feature anti-aliasing.
     * Chaining method.
     * @param numOfSamples num of sample rays for anti aliasing effect.
     * @return this.
     */
    public Render setAntiAliasing(int numOfSamples) {
        antiAliasingFlag = true;

        if (numOfSamples < DEFAULT_NUM_OF_SAMPLES) {
            _numOfSamples = DEFAULT_NUM_OF_SAMPLES;
        } else {
            _numOfSamples = numOfSamples;
        }

        return this;
    }

    /**
     * To set on the feature anti-aliasing.
     * Wrapper method.
     * Chaining method.
     * @return this.
     */
    public Render setAntiAliasing() {
        return setAntiAliasing(DEFAULT_NUM_OF_SAMPLES);
    }


}
