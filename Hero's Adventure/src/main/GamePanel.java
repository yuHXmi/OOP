package main;

import button.ButtonManager;
import entity.Entity;
import entity.Player;
import message.MessageManager;
import skill.MonsterAttack;
import sound.SoundManager;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 32; // 32x32 tile
    final int scale = 2;

    public final int tileSize = originalTileSize * scale; // 64x64 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 9;
    public final int worldSize = 50; // 50x50 world
    public final int screenWidth = maxScreenCol * tileSize;
    public final int screenHeight = maxScreenRow * tileSize;

    // FPS
    public final int FPS = 120;
    final double drawInterval = 1e9 / FPS;
    double delta = 0;

    // SYSTEM
    Thread gameThread;
    public KeyHandler keyH = new KeyHandler();
    public MouseHandler mouseH = new MouseHandler(this);
    public TileManager tileM;
    public AssetSetter assetSetter;
    public CollisionChecker collisionChecker;
    public UI ui;
    public PathFinder pathFinder;
    public BattleHandler battleHandler;
    public MonsterAttack monsterAttack;
    public ButtonManager buttonManager;
    public MessageManager messageManager;
    public SoundManager soundManager;
    public int endingCounter;

    // ENTITY
    public final int isPlayer = 1;
    public final int isNPC = 2;
    public final int isMonster = 3;
    public Player player;
    public List<Entity> npc1;
    public List<Entity> respawnMonster1;
    public List<Entity> npc2;
    public List<Entity> respawnMonster2;
    public List<Entity> npc;
    public List<Entity> respawnMonster;

    // GAME STATE
    public int gameState;
    public final int titleState = 1;
    public final int playState = 2;
    public final int pauseState = 3;
    public final int dialogueState = 4;
    public final int battleState = 5;
    public final int characterState = 6;
    public final int gameOverState = 7;
    public final int endGameState = 8;

    public GamePanel() {

        setUpGame();
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseListener(mouseH);
        this.addMouseMotionListener(mouseH);
        this.setFocusable(true);
    }

    public void setUpGame() {

        gameState = titleState;
        endingCounter = 0;
        tileM = new TileManager(this);
        assetSetter = new AssetSetter(this);
        collisionChecker = new CollisionChecker(this);
        ui = new UI(this);
        pathFinder = new PathFinder(this);
        battleHandler = new BattleHandler(this);
        monsterAttack = new MonsterAttack(this);
        buttonManager = new ButtonManager(this);
        messageManager = new MessageManager(this);
        soundManager = new SoundManager();
        player = new Player(this, keyH);
        assetSetter.setObject();
        assetSetter.setNPC();
        soundManager.backgroundMusic.changeVolume(-7f);
        soundManager.battleMusic.changeVolume(-4f);
        soundManager.backgroundMusic.playMusic();
    }

    public void launchGame() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        long lastPrint = System.nanoTime();
        long drawCount = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        while (gameThread != null) {

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                drawCount++;
                delta--;
            }
            if (currentTime - lastPrint >= 1000000000) {
                lastPrint = currentTime;
                System.out.println(drawCount);
                drawCount = 0;
            }
        }
    }

    public void update() {

        buttonManager.checkButton();
        messageManager.update();

        // PLAY STATE
        if (gameState == playState) {

            if (tileM.currentMap == tileM.mapIsland && player.getCenterRow() == 12 && player.getCenterCol() == 33 && keyH.enterPressed) {

                npc = npc2;
                respawnMonster = respawnMonster2;
                soundManager.backgroundMusic.stop();
                soundManager.dungeonMusic.playMusic();
                soundManager.ladder_slide.replay();
                tileM.currentMap = tileM.mapDungeon;
                player.worldX = 10 * tileSize;
                player.worldY = 39 * tileSize;
                keyH.enterPressed = false;
                player.direction = "down";
            }

            if (tileM.currentMap == tileM.mapDungeon && player.getCenterRow() == 39 && player.getCenterCol() == 10 && keyH.enterPressed) {

                npc = npc1;
                respawnMonster = respawnMonster1;
                soundManager.dungeonMusic.stop();
                soundManager.backgroundMusic.playMusic();
                soundManager.ladder_slide.replay();
                tileM.currentMap = tileM.mapIsland;
                player.worldX = 33 * tileSize;
                player.worldY = 12 * tileSize;
                keyH.enterPressed = false;
                player.direction = "down";
            }

            for (Entity entity : respawnMonster) {

                entity.respawnCounter++;
                if (entity.respawnCounter > 1200) {

                    entity.previousPosition();
                    entity.collisionOn = false;
                    collisionChecker.checkPlayer(entity);
                    collisionChecker.checkTile(entity);
                    collisionChecker.checkEntity(entity, npc);
                    entity.nextPosition();
                    if (!entity.collisionOn) {
                        entity.respawn();
                        break;
                    }
                }
            }

            for (Entity entity : respawnMonster) {
                entity.setScreenPosition();
            }

            player.update();
            for (Entity entity : npc) {
                entity.update();
            }

            for (int i = 0; i < npc.size(); i++) {

                Entity entity = npc.get(i);
                if (entity.type == isMonster) {

                    pathFinder.trackAllPath(entity);
                    int row = player.getCenterRow();
                    int col = player.getCenterCol();
                    if (pathFinder.dist[row][col] <= entity.sight) {
                        if (entity.cutScene) {
                            entity.cutScene = false;
                            gameState = dialogueState;
                            entity.continueDialogue = true;
                            player.npcIndex = i;
                            return;
                        }
                        player.spriteNum = 0;
                        gameState = battleState;
                        battleHandler.startBattle();
                        return;
                    }
                }
            }

            if (keyH.pPressed) {

                gameState = pauseState;
                keyH.pPressed = false;
            }

            if (keyH.cPressed) {

                gameState = characterState;
                keyH.cPressed = false;
            }

        }

        // PAUSE STATE
        if (gameState == pauseState) {

            if (keyH.pPressed) {

                gameState = playState;
                keyH.pPressed = false;
            }
        }

        // CHARACTER STATE
        if (gameState == characterState) {

            if (keyH.cPressed) {
                gameState = playState;
                keyH.cPressed = false;
            }
        }

        // DIALOGUE STATE
        if (gameState == dialogueState) {

            Entity NPC = npc.get(player.npcIndex);

            if (NPC.continueDialogue) {
                soundManager.dialogue.playMusic();
            } else {
                soundManager.dialogue.stop();
            }

            if (keyH.escPressed) {

                NPC.dialogueIndex = 0;
                NPC.dialogueEndIndex = 0;
                NPC.dialogueDelay = 0;
                NPC.continueDialogue = false;
                soundManager.dialogue.stop();
                gameState = playState;
            }
            if (keyH.enterPressed) {

                if (NPC.continueDialogue) {
                    NPC.dialogueEndIndex = NPC.dialogue[NPC.dialogueIndex].length();
                }
                else {
                    NPC.continueDialogue = true;
                    NPC.dialogueIndex++;
                    NPC.dialogueEndIndex = 0;
                    NPC.dialogueDelay = 0;
                }
                keyH.enterPressed = false;
            }

            NPC.speak();
        }

        // BATTLE STATE
        if (gameState == battleState) {

            battleHandler.update();
            for (Entity entity : npc)
                if (entity != null) {
                    if (!entity.inBattle) {
                        entity.spriteNum = 0;
                    }
                    entity.setScreenPosition();
                }
            for (Entity entity : respawnMonster) {
                entity.setScreenPosition();
            }
        }

        mouseH.clickX = -1;
        mouseH.clickY = -1;
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        // TILE
        tileM.draw(g2);

        // COMBAT
        if (gameState == battleState) {
            battleHandler.draw(g2);
        }

        // LV UP
        ui.drawLvUpVFX(g2);

        // NPC
        List<Entity> entityList = new ArrayList<Entity>();
        for (Entity entity : npc)
            if (entity != null && entity.alive) {
                entityList.add(entity);
            }
        for (Entity entity : respawnMonster)
            if (entity.damagedCount > 0) {
                entityList.add(entity);
            }
        entityList.add(player);
        entityList.sort(new EntityComparator());
        for (Entity entity : entityList)
            entity.draw(g2);

        // UI
        ui.draw(g2);

        // MESSAGES
        messageManager.drawMessage(g2);

        g2.dispose();
    }
}
