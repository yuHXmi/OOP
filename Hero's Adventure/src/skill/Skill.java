package skill;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Skill {

    GamePanel gp;

    String name;
    String realName;
    public int attackRange;
    public double attackDamage;
    public int cooldown;
    public int spriteCounter;
    public int spriteNum;
    public final int maxSprite = 4;
    public int currentCD = 0;
    public int manaCost;
    String description;
    BufferedImage icon;
    BufferedImage[] upEffect = new BufferedImage[maxSprite];
    BufferedImage[] downEffect = new BufferedImage[maxSprite];
    BufferedImage[] leftEffect = new BufferedImage[maxSprite];
    BufferedImage[] rightEffect = new BufferedImage[maxSprite];

    public Skill(GamePanel gp) {

        this.gp = gp;
        setDefaultValue();
        loadImage();
    }

    void loadImage() {
        try {

            icon = ImageIO.read(getClass().getResourceAsStream("/skill/" + name + "/" + name + ".png"));
            for (int i = 0; i < 4; i++) {

                upEffect[i] = ImageIO.read(getClass().getResourceAsStream("/skill/" + name + "/up_effect_" + i + ".png"));
                downEffect[i] = ImageIO.read(getClass().getResourceAsStream("/skill/" + name + "/down_effect_" + i + ".png"));
                leftEffect[i] = ImageIO.read(getClass().getResourceAsStream("/skill/" + name + "/left_effect_" + i + ".png"));
                rightEffect[i] = ImageIO.read(getClass().getResourceAsStream("/skill/" + name + "/right_effect_" + i + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setDefaultValue() {}

    public void drawIcon(Graphics2D g2, int x, int y, String number) {

        g2.drawImage(icon, x, y, 64, 64, null);
        if (this.currentCD > 0 || gp.player.MP < manaCost) {
        g2.setColor(new Color(65, 65, 65, 200));
            g2.fillOval(x, y, 64, 64);
        }
    }

    public void drawEffect(Graphics2D g2) {

        spriteCounter++;
        if (spriteCounter == 25) {
            spriteCounter = 0;
            spriteNum++;
            if (spriteNum == maxSprite) {
                spriteNum = 0;
            }
        }

        BufferedImage image = null;
        int diffRow = 0;
        int diffCol = 0;
        switch (gp.player.direction) {
            case "up":
                diffRow = -1;
                image = upEffect[spriteNum];
                break;
            case "down":
                diffRow = 1;
                image = downEffect[spriteNum];
                break;
            case "left":
                diffCol = -1;
                image = leftEffect[spriteNum];
                break;
            case "right":
                diffCol = 1;
                image = rightEffect[spriteNum];
                break;
        }
        for (int i = 1; i <= attackRange; i++) {
            g2.drawImage(image, gp.player.screenX + diffCol * i * gp.tileSize, gp.player.screenY + diffRow * i * gp.tileSize, gp.tileSize, gp.tileSize, null);
        }
    }
}
