import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * The JImageResourceLabel class is a custom JLabel that loads and displays an image from a resource path.
 */
public class JImageResourceLabel extends JLabel {

    /**
     * Constructs a JImageResourceLabel with the specified resource path.
     *
     * @param resourcePath the path to the image resource
     */
    public JImageResourceLabel(String resourcePath) {
        super();
        setHorizontalAlignment(CENTER);
        setImageResourcePath(resourcePath);
    }

    /**
     * Sets the image resource path and updates the label icon.
     *
     * @param resourcePath the path to the image resource
     */
    public void setImageResourcePath(String resourcePath) {
        BufferedImage image;
        try {
            InputStream inputStream = getClass().getResourceAsStream(resourcePath);
            image = ImageIO.read(inputStream);
            setIcon(new ImageIcon(image));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}