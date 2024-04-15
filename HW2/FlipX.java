import java.awt.Color;
import java.awt.Image;

public class FlipX {
    public static void main (String[] args) {
       Picture pic = new Picture(args[0]);
       int width = pic.width();
       int height = pic.height();
       pic.show();
       for (int x = 0; x < height; x++)
           for (int y = 0; y < width / 2; y++) {
               Color temp = pic.get(x, y);
               pic.set(x, y, pic.get(x, width - y - 1));
               pic.set(x, width - y - 1, temp);
           }
       pic.show();
    }
}
