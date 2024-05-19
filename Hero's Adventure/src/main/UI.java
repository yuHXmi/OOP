package main;

import entity.Entity;
import skill.Skill;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import button.Button;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    public String currentDialogue = "";
    public Font maruMonica;
    public Font kaph;

    // IMAGE
    BufferedImage titleBackground;
    BufferedImage gameTitle;
    BufferedImage gameOver;
    BufferedImage buttonFrame;
    BufferedImage health, mana;
    BufferedImage characterWindow;
    BufferedImage plusButton;
    BufferedImage[] lvUp = new BufferedImage[15];
    String[] ending = new String[5];

    public int lvUpCounter;
    public int lvUpSprite;
    public List<DamagedInfo> damagedList = new ArrayList<DamagedInfo>();

    public UI(GamePanel gp){
        this.gp = gp;
        ending[0] = "HERO's ADVENTURE";
        ending[1] = "A game by XHUY.";
        ending[2] = "Attributing it to the original Hero's Adventure, I appreciate its uniqueness.";
        ending[3] = "Thank you for playing my game!";
        ending[4] = "Wanna play again?";

        try {

            InputStream is = getClass().getResourceAsStream("/font/Kaph-Regular.ttf");
            kaph = Font.createFont(Font.TRUETYPE_FONT, is);

            InputStream is2 = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is2);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setUpUI();
    }

    void setUpUI() {

        try {

            titleBackground = ImageIO.read(getClass().getResourceAsStream("/other/title_background.png"));
            gameTitle = ImageIO.read(getClass().getResourceAsStream("/other/game_title.png"));
            gameOver = ImageIO.read(getClass().getResourceAsStream("/other/game_over.png"));
            buttonFrame = ImageIO.read(getClass().getResourceAsStream("/other/button_frame.png"));
            health = ImageIO.read(getClass().getResourceAsStream("/other/health.png"));
            mana = ImageIO.read(getClass().getResourceAsStream("/other/mana.png"));
            characterWindow = ImageIO.read(getClass().getResourceAsStream("/other/character_window.png"));
            plusButton = ImageIO.read(getClass().getResourceAsStream("/other/plus_button.png"));

            for (int i = 0; i < 15; i++) {
                lvUp[i] = ImageIO.read(getClass().getResourceAsStream("/other/lv_up_" + i + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getScreenX(int worldX) {
        return gp.player.screenX + worldX - gp.player.worldX;
    }

    public int getScreenY(int worldY) {
        return gp.player.screenY + worldY - gp.player.worldY;
    }

    public int getWorldX(int screenX) {
        return gp.player.worldX + screenX - gp.player.screenX;
    }

    public int getWorldY(int screenY) {
        return gp.player.worldY + screenY - gp.player.screenY;
    }

    public void drawLvUpVFX(Graphics2D g2) {

        this.g2 = g2;

        if (lvUpCounter == 0) {
            lvUpSprite = 0;
            return;
        }

        g2.setFont(maruMonica);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40));
        g2.setColor(Color.BLACK);
        String text = "PLAYER LEVEL UP! CURRENT LEVEL ";
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = getXForCenteredText(text);
        int y = gp.tileSize * 3;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (lvUpCounter / 300.0)));
        g2.drawString(text, x, y);
        text = Integer.toString(gp.player.level);
        g2.setColor(Color.CYAN);
        g2.drawString(text, x + length, y);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

        lvUpCounter--;
        if (lvUpCounter < 150) {
            return;
        }
        int width = 100;
        int height = 120;
        g2.drawImage(lvUp[lvUpSprite], gp.player.screenX - (width - gp.tileSize) / 2, gp.player.screenY - (height - gp.tileSize), width, height, null);
        if (lvUpCounter % 10 == 0) {
            lvUpSprite++;
            if (lvUpSprite >= 15) {
                lvUpSprite = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {

        this.g2 = g2;
        g2.setFont(maruMonica);

        // PLAY STATE
        if (gp.gameState == gp.playState) {
            drawNpcName();
            drawPlayerStatus();
        }

        // PAUSE STATE
        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }

        // CHARACTER STATE
        if (gp.gameState == gp.characterState) {
            drawCharacterWindow();
        }

        // DIALOGUE STATE
        if (gp.gameState == gp.dialogueState) {
            drawDialogueScreen();
        }

        // BATTLE STATE
        if (gp.gameState == gp.battleState) {
            drawDamaged();
            drawPlayerStatus();
            drawMonsterHealth();
            if (gp.battleHandler.currentState == gp.battleHandler.moveState) {
                drawSkillIcon();
            }
            if (gp.battleHandler.monsterAttack) {
                gp.monsterAttack.drawEffect(g2);
            }
            if (gp.player.attacking) {
                if (gp.battleHandler.attackCounter >= 20)
                    gp.battleHandler.skillUsing.drawEffect(g2);
            }
        }

        // TITLE STATE
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }


        // GAME OVER STATE
        if (gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
        }

        // END GAME STATE
        if (gp.gameState == gp.endGameState) {
            drawEndScreen();
        }
    }

    public Color setColorAlpha(Color c, int alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }

    public void drawEndScreen() {

        gp.endingCounter++;
        if (gp.endingCounter <= 360) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, gp.endingCounter / 200));
        } else {
            gp.soundManager.backgroundMusic.loop();
        }

        g2.drawImage(titleBackground, 0, 0, gp.screenWidth, gp.screenHeight, null);
        for (int i = 0; i < 5; i++) {
            g2.setColor(Color.white);
            if (i == 0) {
                g2.setFont(maruMonica.deriveFont(Font.PLAIN, 50));
            } else {
                g2.setFont(maruMonica.deriveFont(Font.PLAIN, 35));
            }
            int x = getXForCenteredText(ending[i]);
            int y = gp.screenHeight - gp.endingCounter / 3 + i * gp.tileSize - 180;
            g2.setColor(Color.BLACK);
            g2.drawString(ending[i], x + 2, y + 2);
            g2.setColor(Color.white);
            g2.drawString(ending[i], x, y);
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

        if (gp.endingCounter >= 2000) {

            g2.setFont(maruMonica.deriveFont(Font.PLAIN, 50));
            String text = "BACK";
            int x = getXForCenteredText(text);
            int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            int y = gp.tileSize * 5;
            g2.drawString(text, x, y);
            gp.buttonManager.backToTitle = new Button(x, y - 50, length, 50);
        }
    }

    public void drawDamaged() {

        for (int i = damagedList.size() - 1; i >= 0; i--) {

            DamagedInfo damagedInfo = damagedList.get(i);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 25));
            g2.setColor(Color.red);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (damagedInfo.counter / 120.0)));

            String text = "-" + Integer.toString(damagedInfo.damaged);
            int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            int x = getScreenX(damagedInfo.worldX + gp.tileSize / 2 - length / 2);
            int y = getScreenY(damagedInfo.worldY);
            g2.drawString(text, x, y);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

            if (damagedInfo.counter % 2 == 0)
                damagedInfo.worldY--;
            damagedInfo.counter--;
            if (damagedInfo.counter == 0) {
                damagedList.remove(i);
            }
        }
    }

    public void drawGameOverScreen() {

        g2.setColor(new Color(0, 0, 0, 150 - gp.player.damagedCount));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (1 - Math.max(0, gp.player.damagedCount) / 120.0)));
        g2.drawImage(gameOver, gp.tileSize * 5, gp.tileSize * 1, gp.tileSize * 6, gp.tileSize * 4, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

        if (gp.player.damagedCount < 0) {
            String text = "BACK";
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50));
            int x = getXForCenteredText(text);
            int y = gp.tileSize * 6;
            int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            g2.drawString(text, x, y);
            gp.buttonManager.backToTitle = new Button(x, y - 50, length, 50);
        }
    }

    public void drawTitleScreen() {

        g2.drawImage(titleBackground, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2.setFont(kaph);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        String text = "HERO'S ADVENTURE";
        int x = getXForCenteredText(text);
        int y = 100;
        g2.setColor(Color.white);
        g2.drawString(text, x + 1, y + 1);
        g2.setColor(Color.gray);
        g2.drawString(text, x, y);
        g2.setColor(Color.black);
        g2.drawString(text, x - 2, y - 2);

        g2.setFont(maruMonica.deriveFont(Font.BOLD, 30));
        g2.setColor(new Color(238,232,170));

        x = gp.tileSize * 6;
        y = 150;
        g2.drawImage(buttonFrame, x, y , gp.tileSize * 4, 100, null);
        gp.buttonManager.newGame = new Button(x, y, gp.tileSize * 4, 100);
        text = "NEW GAME";
        g2.drawString(text, getXForCenteredText(text), y + 59);
        g2.drawString(text, getXForCenteredText(text), y + 60);

        y += 100;
        g2.drawImage(buttonFrame, x, y, gp.tileSize * 4, 100, null);
        gp.buttonManager.exit = new Button(x, y, gp.tileSize * 4, 100);
        text = "EXIT";
        g2.drawString(text, getXForCenteredText(text), y + 59);
        g2.drawString(text, getXForCenteredText(text), y + 60);

        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 25));
        g2.drawString("iamxhuy", gp.tileSize * 14 + 25, gp.tileSize * 9 - 10);
    }

    public void drawSkillIcon() {

        int size = gp.player.skillList.size();
        int x = (gp.screenWidth - size * 64 - 150 * (size - 1)) / 2;
        int y = gp.tileSize * 7;
        int cnt = 0;
        for (Skill skill : gp.player.skillList) {
            cnt++;
            skill.drawIcon(g2, x, y, Integer.toString(cnt));
            x += 150;
        }
    }

    public void drawSquare(Graphics2D g2, int x, int y, Color c) {
        g2.setColor(setColorAlpha(c, 100));
        g2.fillRoundRect(x + 5, y + 5, gp.tileSize - 10, gp.tileSize - 10, 10, 10);
        g2.setStroke(new BasicStroke(4));
        g2.setColor(setColorAlpha(c, 150));
        g2.drawRoundRect(x + 5, y + 5, gp.tileSize - 10, gp.tileSize - 10, 10, 10);
    }

    public void drawMonsterHealth() {

        for (Entity monster : gp.npc)
            if (monster.inBattle) {

                int x = monster.screenX + 5;
                int y = monster.screenY - 10;
                g2.setColor(Color.white);
                g2.fillRect(x, y, 50, 8);
                g2.setColor(Color.red);
                g2.fillRect(x, y, 50 * monster.HP / monster.maxHP, 8);
                g2.setColor(Color.black);
                g2.setStroke(new BasicStroke(2));
                g2.drawRect(x, y, 50, 8);
            }
    }

    public void drawCharacterWindow() {

        int x = gp.tileSize * 3;
        int y = gp.tileSize * 2;

        g2.drawImage(characterWindow, x, y, gp.tileSize * 10, gp.tileSize * 6, null);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40));
        g2.setColor(Color.gray);
        g2.drawString("CHARACTER STATUS", x + gp.tileSize + 2, y - 8);
        g2.setColor(Color.white);
        g2.drawString("CHARACTER STATUS", x + gp.tileSize, y - 10);

        x += gp.tileSize;
        y += 10;
        g2.setColor(new Color(212, 210, 155));
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25));
        g2.drawString("LEVEL", x, y + gp.tileSize);
        g2.drawString("HP", x, y + gp.tileSize * 2);
        g2.drawString("MP", x, y + gp.tileSize * 3);
        g2.drawString("EXP", x, y + gp.tileSize * 4);
        g2.drawString("GOLD", x, y + gp.tileSize * 5);
        g2.setColor(new Color(145,240,134));
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 25));
        g2.drawString(Integer.toString(gp.player.level), x + 65, y + gp.tileSize);
        drawHealthStatus(x + gp.tileSize, y + 110);
        drawManaStatus(x + gp.tileSize, y + 110 + gp.tileSize);
        drawExpStatus(x + gp.tileSize, y + 110 + gp.tileSize * 2);
        g2.setColor(Color.orange);
        g2.drawString(Integer.toString(gp.player.currentGold), x + 65, y + gp.tileSize * 5);
        g2.setColor(new Color(212, 210, 155));
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20));
        g2.drawString(Integer.toString(gp.player.HP) + "/" + Integer.toString(gp.player.maxHP), x + gp.tileSize, y + gp.tileSize * 2 + 22);
        g2.drawString(Integer.toString(gp.player.MP) + "/" + Integer.toString(gp.player.maxMP), x + gp.tileSize, y + gp.tileSize * 3 + 22);
        g2.drawString(Integer.toString(gp.player.currentExp) + "/" + Integer.toString(gp.player.nextLevelExp), x + gp.tileSize, y + gp.tileSize * 4 + 22);

        x += gp.tileSize * 5.2;
        g2.setColor(new Color(212, 210, 155));
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25));
        g2.drawString("STR", x, y + gp.tileSize);
        g2.drawString("VIT", x, y + gp.tileSize * 2);
        g2.drawString("DEX", x, y + gp.tileSize * 3);
        g2.drawString("INT", x, y + gp.tileSize * 4);
        g2.drawString("POINTS", x, y + gp.tileSize * 5);
        g2.setColor(new Color(145,240,134));
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 25));
        g2.drawString(Integer.toString(gp.player.STR), x + 90, y + gp.tileSize);
        g2.drawString(Integer.toString(gp.player.VIT), x + 90, y + gp.tileSize * 2);
        g2.drawString(Integer.toString(gp.player.DEX), x + 90, y + gp.tileSize * 3);
        g2.drawString(Integer.toString(gp.player.INT), x + 90, y + gp.tileSize * 4);
        g2.drawString(Integer.toString(gp.player.statPoint), x + 90, y + gp.tileSize * 5);

        g2.drawImage(plusButton, x + 170, y + 42, 28, 28, null);
        gp.buttonManager.strInc = new Button(x + 170, y + 42, 28, 28);

        g2.drawImage(plusButton, x + 170, y + 42 + gp.tileSize, 28, 28, null);
        gp.buttonManager.vitInc = new Button(x + 170, y + 42 + gp.tileSize, 28, 28);

        g2.drawImage(plusButton, x + 170, y + 42 + gp.tileSize * 2, 28, 28, null);
        gp.buttonManager.dexInc = new Button(x + 170, y + 42 + gp.tileSize * 2, 28, 28);

        g2.drawImage(plusButton, x + 170, y + 42 + gp.tileSize * 3, 28, 28, null);
        gp.buttonManager.intInc = new Button(x + 170, y + 42 + gp.tileSize * 3, 28, 28);
    }

    void drawHealthStatus(int x, int y) {

        g2.setColor(new Color(255,186,186));
        g2.fillRect(x, y, 200, 20);
        g2.setColor(Color.red);
        g2.fillRect(x, y, 200 * gp.player.HP / gp.player.maxHP, 20);
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(x, y, 200, 20);
    }

    void drawManaStatus(int x, int y) {

        g2.setColor(Color.cyan);
        g2.fillRect(x, y, 200, 20);
        g2.setColor(Color.blue);
        g2.fillRect(x, y, 200 * gp.player.MP / gp.player.maxMP, 20);
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(x, y, 200, 20);
    }

    void drawExpStatus(int x, int y) {

        g2.setColor(new Color(145,200,134));
        g2.fillRect(x, y, 200, 20);
        g2.setColor(Color.green);
        g2.fillRect(x, y, 200 * gp.player.currentExp / gp.player.nextLevelExp, 20);
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(x, y, 200, 20);
    }

    public void drawNpcName() {

        for (Entity NPC : gp.npc)
            if (NPC != null && NPC.alive) {

            int diffX = Math.abs(NPC.worldX - gp.player.worldX);
            int diffY = Math.abs(NPC.worldY - gp.player.worldY);
            if (Math.sqrt(diffX * diffX + diffY * diffY) <= gp.tileSize * 4) {

                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15));
                Color c = new Color(255, 255, 255, 180);
                g2.setColor(c);

                int length = (int)g2.getFontMetrics().getStringBounds(NPC.realName, g2).getWidth();
                int x = NPC.screenX + NPC.width / 2 - length / 2;
                int y = NPC.screenY - 10;
                g2.drawString(NPC.realName, x, y);
            }
        }
    }

    public void drawPlayerStatus() {

        int x = gp.tileSize;
        int y = gp.tileSize / 2;

        // Draw health bar
        g2.drawImage(health, x - 45, y - 8, 40, 40, null);
        Color fullHP = new Color(255,186,186);
        g2.setColor(fullHP);
        g2.fillRoundRect(x, y, 300, 25, 20, 20);
        Color currentHP = new Color(255,82,82);
        g2.setColor(currentHP);
        g2.fillRoundRect(x, y, 300 * gp.player.HP / gp.player.maxHP, 25, 20, 20);
        g2.setColor(Color.darkGray);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, 300, 25, 20, 20);

        // Draw mana bar
        y += 45;
        g2.drawImage(mana, x - 45, y - 8, 40, 40, null);
        g2.setColor(Color.white);
        g2.fillRoundRect(x, y, 300, 25, 20, 20);
        g2.setColor(Color.cyan);
        g2.fillRoundRect(x, y, 300 * gp.player.MP / gp.player.maxMP, 25, 20, 20);
        g2.setColor(Color.darkGray);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, 300, 25, 20, 20);
    }

    void drawPauseScreen() {

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 100));
        String text = "PAUSED";

        int x = getXForCenteredText(text);
        int y = gp.screenHeight / 2;
        g2.drawString(text, x, y);
    }

    void drawDialogueScreen() {

        // WINDOW
        int x = gp.tileSize * 2;
        int y = gp.tileSize * 5;
        int width = gp.screenWidth - gp.tileSize * 4;
        int height = gp.tileSize * 4;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32));
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    void drawSubWindow(int x, int y, int width, int height) {

        Color c = new Color(0, 0, 0, 200);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 150, 150);
        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x, y, width, height, 150, 150);
    }

    public int getXForCenteredText(String text) {

        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }
}
