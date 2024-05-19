package button;

import java.awt.*;

public class Button {

    public Rectangle area;

    public Button(int x, int y, int width, int height) {

        area = new Rectangle(x, y, width, height);
    }

    boolean isClicked(int x, int y) {

        return x >= area.x && x < area.x + area.width && y >= area.y && y < area.y + area.height;
    }
}
