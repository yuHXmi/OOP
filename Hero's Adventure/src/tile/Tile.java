package tile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Tile {

    public String name;
    public BufferedImage image;
    public boolean collision = false;

    public Tile(String fileName, String collision) throws IOException {

        name = fileName;
        image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + fileName));
        if (collision.equals("true"))
            this.collision = true;
    }
}
