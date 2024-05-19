package entity;

import main.GamePanel;
import main.KeyHandler;
import skill.NormalPunch;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {

    KeyHandler keyH;
    public int npcIndex;

    public Player(GamePanel gp, KeyHandler keyH){

        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - gp.tileSize / 2;
        screenY = gp.screenHeight / 2 - gp.tileSize / 2;
        type = 1;

        setDefaultValue();
        getImage();
    }

    public void setDefaultValue() {

        level = 1;
        STR = 3;
        VIT = 3;
        DEX = 2;
        INT = 2;
        setStats();
        HP = maxHP;
        MP = maxMP;
        name = "player";
        worldX = gp.tileSize * 26;
        worldY = gp.tileSize * 26;
        speed = 4;
        skillList.add(new NormalPunch(gp));
    }

    public void interactEntity(int index) {

        if (index < 100) {

            if (gp.npc.get(index).type == gp.isNPC) {
                if (gp.keyH.enterPressed) {
                    gp.gameState = gp.dialogueState;
                    gp.npc.get(index).continueDialogue = true;
                    gp.npc.get(index).speak();
                    gp.keyH.enterPressed = false;
                }
                healthHeal(1);
            }
        }
    }

    public void update() {

        boolean movingKey = keyH.upPressed | keyH.downPressed | keyH.leftPressed | keyH.rightPressed;

        if (movingKey || keyH.enterPressed) {

            if (keyH.upPressed) {
                direction = "up";
            }
            if (keyH.downPressed) {
                direction = "down";
            }
            if (keyH.leftPressed) {
                direction = "left";
            }
            if (keyH.rightPressed) {
                direction = "right";
            }

            collisionOn = false;
            gp.collisionChecker.checkTile(this);
            npcIndex = gp.collisionChecker.checkEntity(this, gp.npc);
            interactEntity(npcIndex);

            if (!collisionOn && movingKey) {
                moving();
            }
        } else {
            spriteNum = 0;
        }
    }

    public void moving() {
        this.nextPosition();
        System.out.println("PlayerRow: " + gp.player.getCenterRow());
        System.out.println("PlayerCol: " + gp.player.getCenterCol());
        System.out.println("--------");
        countSprite();
    }
}
