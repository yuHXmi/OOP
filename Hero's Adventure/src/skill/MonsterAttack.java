package skill;

import entity.Monster;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MonsterAttack {

    GamePanel gp;
    public final int maxSprite = 4;
    int spriteNum;
    int spriteCounter;
    BufferedImage[] effect = new BufferedImage[maxSprite];

    public MonsterAttack(GamePanel gp) {

        this.gp = gp;
        loadImage();
    }

    void loadImage() {

        for (int i = 0; i < maxSprite; i++) {
            try {
                effect[i] = ImageIO.read(getClass().getResourceAsStream("/skill/monster_attack/effect_" + i + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void drawEffect(Graphics2D g2) {

        spriteCounter++;
        if (spriteCounter == 15) {
            spriteCounter = 0;
            spriteNum++;
            if (spriteNum == maxSprite) {
                spriteNum = 0;
            }
        }

        BufferedImage image = effect[spriteNum];
        g2.drawImage(image, gp.player.screenX, gp.player.screenY, gp.tileSize, gp.tileSize, null);
    }
}
