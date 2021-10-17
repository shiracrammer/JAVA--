package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit5 for renderer.ImageWriter class
 * @author Deborah Lellouche
 */
class ImageWriterTests {

    /**
     * Test method for {@link renderer.ImageWriter#writeToImage()}.
     */
    @Test
    void testWriteToImage() {
        int nX = 800;
        int nY = 500;
        int gapX = 800/16;
        int gapY = 500/10;
        ImageWriter imageWriter = new ImageWriter("Big Blue", nX, nY);

        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                // for the grid
                if (i%gapY == 0 || j%gapX == 0) {
                    imageWriter.writePixel(j, i, Color.BLACK);
                }
                else {
                    // writing blue pixel
                    imageWriter.writePixel(j, i, new Color(0, 0, 1000)); // = 255
                }
            }
        }

        imageWriter.writeToImage();
    }
}