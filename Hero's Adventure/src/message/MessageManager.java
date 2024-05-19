package message;

import main.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Queue;

public class MessageManager {

    GamePanel gp;
    Message[] messages = new Message[5];
    Message[] waitList = new Message[100];

    public MessageManager(GamePanel gp) {
        this.gp = gp;
    }

    public void add(String text) {

        for (int i = 0; i < 100; i++)
            if (waitList[i] == null) {
                waitList[i] = new Message(text);
                return;
            }
    }

    void pop() {

        for (int i = 1; i < 100; i++) {
            waitList[i - 1] = waitList[i];
        }
    }

    public void update() {

        for (int i = 0; i < 5; i++)
            if (messages[i] != null) {

                if (messages[i].counter == 0) {
                    messages[i] = null;
                } else {
                    messages[i].counter--;
                }
            }

        for (int i = 0; i < 5; i++)
            if (messages[i] == null) {
                if (waitList[0] != null) {
                    messages[i] = waitList[0];
                    pop();
                }
            }
    }

    public void drawMessage(Graphics2D g2) {

        g2.setFont(gp.ui.maruMonica);
        g2.setFont(gp.getFont().deriveFont(Font.BOLD, 21));
        for (int i = 0; i < 5; i++)
            if (messages[i] != null) {

                g2.setColor(Color.gray);
                g2.drawString(messages[i].text, gp.tileSize + 2, gp.tileSize * (i + 8) / 2 + 2);
                g2.setColor(Color.cyan);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (messages[i].counter / 240.0)));
                g2.drawString(messages[i].text, gp.tileSize, gp.tileSize * (i + 8) / 2);
            }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
    }
}
