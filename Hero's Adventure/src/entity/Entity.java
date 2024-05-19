package entity;

import main.DamagedInfo;
import main.GamePanel;
import skill.Skill;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Entity {

    GamePanel gp;

    public String name, realName;
    public int type;
    public int sight = 4;

    public int worldX, worldY;
    public int screenX, screenY;
    public int actionLockCounter;
    public int width;
    public int height;

    // COLLISION
    public int defaultSolidAreaX = 0;
    public int defaultSolidAreaY = 0;
    public Rectangle solidArea = new Rectangle(defaultSolidAreaX, defaultSolidAreaY, 64, 64);
    public boolean collisionOn = false;

    public String[] dialogue = new String[20];
    public int dialogueIndex;
    public int dialogueEndIndex;
    public int dialogueDelay;
    public boolean continueDialogue = false;
    public boolean inBattle = false;
    public boolean alive = true;
    public boolean cutScene;
    public int damagedCount;
    public boolean doneEvent = false;

    // STATUS
    public int level = 1;
    public int expReward; // 100 * LV
    public int goldDrop; // 50 * (1.5)^(LV - 1)
    public int currentExp = 0;
    public int currentGold = 0;
    public int nextLevelExp = 100;
    public int speed;
    public int maxHP; // (LV + 1) * 25 + VIT * LV * 5
    public int HP;
    public int maxMP; // LV * 10
    public int MP;
    public int attack;  // ((LV + 1) * 5 + STR * LV * 2 + INT * 1) * (1 + DEX * 0.05)
    public int defence; // ((LV + 1) * 5 + VIT * LV * 1 + INT * 1) * (1 + DEX * 0.05)
    public int STR = 1;
    public int VIT = 1;
    public int DEX = 1;
    public int INT = 1;
    public int statPoint = 0;
    public List<Skill> skillList = new ArrayList<Skill>();

    // SPRITE
    int maxSprite = 3;
    BufferedImage[] up = new BufferedImage[maxSprite];
    BufferedImage[] down = new BufferedImage[maxSprite];
    BufferedImage[] left = new BufferedImage[maxSprite];
    BufferedImage[] right = new BufferedImage[maxSprite];
    BufferedImage[] upAttack = new BufferedImage[maxSprite];
    BufferedImage[] downAttack = new BufferedImage[maxSprite];
    BufferedImage[] leftAttack = new BufferedImage[maxSprite];
    BufferedImage[] rightAttack = new BufferedImage[maxSprite];

    public int spriteNum;
    int spriteCounter;
    public String direction = "down";
    public boolean attacking = false;
    public int respawnCounter;

    public Entity(GamePanel gp) {

        this.gp = gp;
        width = gp.tileSize;
        height = gp.tileSize;
        setStats();
        HP = maxHP;
        MP = maxMP;
    }

    public void setStats() {

        setMaxHP();
        setMaxMP();
        setAttack();
        setDefence();
        setExp();
        setGold();
    }

    void gainExp(int exp) {
        gp.messageManager.add("You gain " + exp + " exp!");
        currentExp += exp;
        while (currentExp >= nextLevelExp) {
            levelUp();
        }
    }

    void gainGold(int gold) {
        gp.messageManager.add("You gain " + gold + " gold!");
        currentGold += gold;
    }

    void levelUp() {
        gp.soundManager.level_up.replay();
        gp.ui.lvUpCounter = 300;
        currentExp -= nextLevelExp;
        level++;
        statPoint++;
        nextLevelExp = (int) (nextLevelExp * 1.45);
        setStats();
        HP = maxHP;
        MP = maxMP;
    }

    void setGold() {
        goldDrop = (int) (50 * Math.pow(1.5, level - 1));
    }

    void setMaxHP() {
        maxHP = (level + 1) * 25 + VIT * level * 5;
    }

    void setMaxMP() {
        maxMP = level * 10;
    }

    void setAttack() {
        attack = (int) (((level + 1) * 5 + STR * level * 2 + INT * 1) * (1 + DEX * 0.05));
    }

    void setDefence() {
        defence = (int) (((level + 1) * 5 + VIT * level * 1 + INT * 1) * (1 + DEX * 0.05));
    }

    void setExp() {
        expReward = 100 * level;
    }

    void setDefaultValue() {}

    public int getCenterX() {
        return worldX + solidArea.x + solidArea.width / 2;
    }

    public int getCenterY() {
        return worldY + defaultSolidAreaY + solidArea.width / 2;
    }

    public int getCenterRow() {
        return getCenterY() / gp.tileSize;
    }

    public int getCenterCol() {
        return getCenterX() / gp.tileSize;
    }

    public void getImage() {

        try {
            for (int i = 0; i < maxSprite; i++) {

                up[i] = ImageIO.read(getClass().getResourceAsStream("/" + name + "/" + name + "_up_" + i + ".png"));
                down[i] = ImageIO.read(getClass().getResourceAsStream("/" + name + "/" + name + "_down_" + i + ".png"));
                left[i] = ImageIO.read(getClass().getResourceAsStream("/" + name + "/" + name + "_left_" + i + ".png"));
                right[i] = ImageIO.read(getClass().getResourceAsStream("/" + name + "/" + name + "_right_" + i + ".png"));
                if (type == 1) {
                    upAttack[i] = ImageIO.read(getClass().getResourceAsStream("/" + name + "/" + name + "_up_attack_" + i + ".png"));
                    downAttack[i] = ImageIO.read(getClass().getResourceAsStream("/" + name + "/" + name + "_down_attack_" + i + ".png"));
                    leftAttack[i] = ImageIO.read(getClass().getResourceAsStream("/" + name + "/" + name + "_left_attack_" + i + ".png"));
                    rightAttack[i] = ImageIO.read(getClass().getResourceAsStream("/" + name + "/" + name + "_right_attack_" + i + ".png"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void speak() {

        if (dialogue[dialogueIndex] == null) {
            dialogueIndex = dialogueEndIndex = dialogueDelay = 0;
            gp.gameState = gp.playState;
        }
        dialogueDelay++;
        if (dialogueDelay >= gp.FPS / 30 && dialogueEndIndex < dialogue[dialogueIndex].length()) {

            dialogueDelay = 0;
            dialogueEndIndex++;
        }
        if (dialogueEndIndex == dialogue[dialogueIndex].length()) {
            continueDialogue = false;
        }
        gp.ui.currentDialogue = dialogue[dialogueIndex].substring(0, dialogueEndIndex);
        faceToPlayer();
    };

    public void moving() {
        this.nextPosition();
        countSprite();
    }

    public void healthHeal(int health) {
        HP += health;
        if (HP > maxHP)
            HP = maxHP;
    }

    void die() {

        if (this == gp.player) {
            gp.soundManager.battleMusic.stop();
            gp.soundManager.player_death.replay();
            gp.gameState = gp.gameOverState;
        }
        inBattle = false;
        alive = false;
        respawnCounter = 0;
        gp.respawnMonster.add(this);
        gp.npc.remove(this);
    }

    public void respawn() {
        alive = true;
        gp.npc.add(this);
        gp.respawnMonster.remove(this);
        HP = maxHP;
    }

    public void damaged(Entity entity, int damage) {

        damage = (int) (damage * (1 + (entity.level - level) * 0.02));
        damage = (int) (damage * (1 + Math.random() / 10));
        damage -= defence;
        if (damage <= 0)
            damage = 1;
        HP -= damage;
        damagedCount = 120;
        if (HP <= 0) {
            HP = 0;
            if (entity == gp.player) {
                entity.gainGold((int) (goldDrop * (1 + Math.random() / 5)));
                entity.gainExp((int) (expReward * Math.max(0, (1 + (level - entity.level) * 0.08))));
            }
            die();
        }
        gp.ui.damagedList.add(new DamagedInfo(damage, this.worldX, this.worldY - 10));
    }

    public void setAction() {

        actionLockCounter++;
        if (actionLockCounter > gp.FPS * 2) {

            actionLockCounter = 0;
            Random random = new Random();
            int i = random.nextInt(4) + 1;
            if (i == 1) {
                direction = "up";
            }
            if (i == 2) {
                direction = "down";
            }
            if (i == 3) {
                direction = "left";
            }
            if (i == 4) {
                direction = "right";
            }
        }
    }

    public void interactPlayer() {}

    public void update() {
        setAction();

        collisionOn = false;
        gp.collisionChecker.checkTile(this);
        if (gp.collisionChecker.checkPlayer(this)) {
            interactPlayer();
        }
        gp.collisionChecker.checkEntity(this, gp.npc);

        if (!collisionOn) {
            this.nextPosition();
        }

        countSprite();
        setScreenPosition();
    }

    public void countSprite() {

        spriteCounter++;
        if (spriteCounter >= gp.FPS / 12) {
            spriteNum++;
            if (spriteNum >= maxSprite)
                spriteNum = 0;
            spriteCounter = 0;
        }
    }

    public void setScreenPosition() {

        int diffX = worldX - gp.player.worldX;
        int diffY = worldY - gp.player.worldY;
        screenX = gp.player.screenX + diffX;
        screenY = gp.player.screenY + diffY;
    }

    public void faceToPlayer() {

        switch(gp.player.direction) {
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }
    }

    public void nextPosition() {

        switch(direction) {
            case "up": worldY -= speed; break;
            case "down": worldY += speed; break;
            case "left": worldX -= speed; break;
            case "right": worldX += speed; break;
        }
    }

    public void previousPosition() {

        switch (direction) {
            case "up":
                worldY += speed;
                break;
            case "down":
                worldY -= speed;
                break;
            case "left":
                worldX += speed;
                break;
            case "right":
                worldX -= speed;
                break;
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        if (attacking) {
            switch (direction) {
                case "up":
                    image = upAttack[spriteNum];
                    break;
                case "down":
                    image = downAttack[spriteNum];
                    break;
                case "left":
                    image = leftAttack[spriteNum];
                    break;
                case "right":
                    image = rightAttack[spriteNum];
                    break;
            }
        } else {
            switch (direction) {
                case "up":
                    image = up[spriteNum];
                    break;
                case "down":
                    image = down[spriteNum];
                    break;
                case "left":
                    image = left[spriteNum];
                    break;
                case "right":
                    image = right[spriteNum];
                    break;
            }
        }
        if (damagedCount >= 0) {
            damagedCount--;
            if (damagedCount % 40 <= 20) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            } else {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            }
        }
        g2.drawImage(image, screenX, screenY, width, height, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
