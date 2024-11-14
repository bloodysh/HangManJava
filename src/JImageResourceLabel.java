import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class JImageResourceLabel extends JLabel {
    public JImageResourceLabel(String resourcePath) {
        super();
        setHorizontalAlignment(CENTER);
        setImageResourcePath(resourcePath);
    }

    public void setImageResourcePath(String resourcePath) {
        BufferedImage image;
        try{
            InputStream inputStream = getClass().getResourceAsStream(resourcePath);
            image = ImageIO.read(inputStream);
            setIcon(new ImageIcon(image));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
