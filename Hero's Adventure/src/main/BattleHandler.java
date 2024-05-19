package main;

import entity.Entity;
import skill.MonsterAttack;
import skill.Skill;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BattleHandler {

    GamePanel gp;
    List<Entity> battleTurn = new ArrayList<Entity>();
    int currentTurn;
    int[][] dist;
    int goalRow, goalCol, nextGoalRow = -1, nextGoalCol = -1;
    public final int moveState = 1;
    public final int useSkillState = 2;
    public final int attackState = 3;
    public final int monsterState = 4;
    public final int delayState = 5;
    int currentState = moveState;
    boolean playerAttack;
    boolean monsterAttack;
    int attackCounter;
    Skill skillUsing;
    int monsterMoveCount;
    int newPhaseCount;

    public BattleHandler(GamePanel gp) {
        this.gp = gp;
        dist = new int[gp.worldSize][gp.worldSize];
    }

    void joinBattle(Entity entity) {
        entity.inBattle = true;
        battleTurn.add(entity);
    }

    void startBattle() {

        gp.soundManager.backgroundMusic.stop();
        gp.soundManager.dungeonMusic.stop();
        gp.soundManager.battleMusic.playMusic();
        adjustPosition();
        joinBattle(gp.player);
        newPhase();
        currentTurn = 0;
    }

    void endBattle() {

        gp.soundManager.battleMusic.stop();
        if (gp.tileM.currentMap == gp.tileM.mapIsland) {
            gp.soundManager.backgroundMusic.playMusic();
        } else {
            gp.soundManager.dungeonMusic.playMusic();
        }
        gp.ui.damagedList.clear();
        battleTurn.clear();
        gp.gameState = gp.playState;
    }

    void newPhase() {

        currentState = delayState;
        newPhaseCount = 0;
        attackCounter = 0;
        monsterMoveCount = 0;
        monsterAttack = false;
        playerAttack = false;
        for (Entity entity : gp.npc)
            if (entity != null && entity.type == gp.isMonster && !entity.inBattle) {
                gp.pathFinder.trackAllPath(entity);
                if (gp.pathFinder.dist[gp.player.getCenterRow()][gp.player.getCenterCol()] <= 5) {
                    joinBattle(entity);
                }
            }

        if (battleTurn.size() == 1) {
            endBattle();
            return;
        }

        currentTurn++;
        if (currentTurn == battleTurn.size()) {
            currentTurn = 0;
        }
        if (battleTurn.get(currentTurn) == gp.player) {
            for (Skill skill : gp.player.skillList) {
                if (skill.currentCD > 0) {
                    skill.currentCD--;
                }
            }
        }

        goalRow = gp.player.getCenterRow();
        goalCol = gp.player.getCenterCol();
        gp.pathFinder.trackAllPath(gp.player);

        for (int i = 0; i < gp.worldSize; i++)
            for (int j = 0; j < gp.worldSize; j++)
                dist[i][j] = gp.pathFinder.dist[i][j];
    }

    void move() {

        if (gp.mouseH.clickX >= 0) {
            int row = (gp.player.worldY + gp.mouseH.clickY - gp.player.screenY) / gp.tileSize;
            int col = (gp.player.worldX + gp.mouseH.clickX - gp.player.screenX) / gp.tileSize;
            if (dist[row][col] <= 5) {
                nextGoalRow = row;
                nextGoalCol = col;
            }
        }
        gp.mouseH.clickX = -1;
        gp.mouseH.clickY = -1;
        if (gp.player.worldX == gp.player.getCenterCol() * gp.tileSize && gp.player.worldY == gp.player.getCenterRow() * gp.tileSize) {

            if (nextGoalRow >= 0) {
                goalRow = nextGoalRow;
                goalCol = nextGoalCol;
                nextGoalRow = -1;
                nextGoalCol = -1;
            }
            gp.pathFinder.findPath(gp.player, goalRow, goalCol);
        }
        if (gp.player.worldX == goalCol * gp.tileSize && gp.player.worldY == goalRow * gp.tileSize) {
            gp.player.spriteNum = 0;
            if (gp.keyH.onePressed) {
                currentState = useSkillState;
                skillUsing = gp.player.skillList.get(0);
                gp.keyH.onePressed = false;
            }
            else if (gp.keyH.twoPressed && gp.player.skillList.size() >= 2) {
                Skill skill = gp.player.skillList.get(1);
                if (gp.player.MP >= skill.manaCost && skill.currentCD == 0) {
                    gp.player.MP -= skill.manaCost;
                    skill.currentCD = skill.cooldown;
                    currentState = useSkillState;
                    skillUsing = skill;
                }
                gp.keyH.twoPressed = false;
            }
        } else {
            gp.player.moving();
        }
    }

    void checkDamagedEntity() {

        int diffRow = 0;
        int diffCol = 0;
        switch (gp.player.direction) {
            case "up":
                diffRow = -1;
                break;
            case "down":
                diffRow = 1;
                break;
            case "left":
                diffCol = -1;
                break;
            case "right":
                diffCol = 1;
                break;
        }

        for (int i = 1; i <= skillUsing.attackRange; i++) {

            int row = gp.player.getCenterRow() + diffRow * i;
            int col = gp.player.getCenterCol() + diffCol * i;
            for (Entity entity : battleTurn)
                if (entity != gp.player && entity.getCenterRow() == row && entity.getCenterCol() == col) {
                    entity.damaged(gp.player, (int) (gp.player.attack * skillUsing.attackDamage));
                }

            for (int j = battleTurn.size() - 1; j > 0; j--) {
                if (!battleTurn.get(j).alive) {
                    battleTurn.remove(j);
                    if (j <= currentTurn)
                        currentTurn--;
                }
            }
        }
    }

    void playerAttack() {
        gp.player.attacking = true;
        attackCounter++;
        if (attackCounter % 3 == 0)
            gp.player.countSprite();
        if (attackCounter == 40) {
            gp.soundManager.attack.stop();
            gp.soundManager.attack.replay();
            checkDamagedEntity();
        }
        if (attackCounter > 90) {
            skillUsing.spriteCounter = 0;
            skillUsing.spriteNum = 0;
            newPhase();
            currentState = moveState;
            gp.player.attacking = false;
        }
    }

    void monsterAttack() {
        attackCounter++;
        monsterAttack = true;
        Entity entity = battleTurn.get(currentTurn);
        if (attackCounter % 2 == 0) {
            entity.countSprite();
        }
        if (attackCounter == 40) {
            gp.soundManager.attack.replay();
            gp.player.damaged(entity, entity.attack);
        }
        if (attackCounter > 60) {
            entity.spriteNum = 0;
            newPhase();
        }
    }

    public void update() {

        if (currentState == delayState) {
            newPhaseCount++;
            if (newPhaseCount <= 40)
                return;
            currentState = moveState;
        }
        if (battleTurn.get(currentTurn) == gp.player) {
            playerTurn();
        } else {
            monsterTurn();
        }
    }

    void monsterTurn() {

        currentState = monsterState;
        Entity entity = battleTurn.get(currentTurn);
        if (entity.getCenterCol() * gp.tileSize == entity.worldX && entity.getCenterRow() * gp.tileSize == entity.worldY) {
            gp.pathFinder.findPath(entity, gp.player.getCenterRow(), gp.player.getCenterCol());
        }
        if (dist[gp.player.getCenterRow()][gp.player.getCenterCol()] >= gp.worldSize * 2) {
            newPhase();
            return;
        }
        if (gp.collisionChecker.checkPlayer(entity)) {
            monsterAttack();
            return;
        }
        if (monsterMoveCount == gp.tileSize * 4) {
            newPhase();
            return;
        }
        monsterMoveCount += entity.speed;
        entity.moving();
    }

    void playerTurn() {

        for (Entity entity : battleTurn)
            if (entity != gp.player) {
                entity.faceToPlayer();
            }
        if (currentState == monsterState) {
            currentState = moveState;
        }

        if (currentState == moveState) {
            move();
        } else if (currentState == useSkillState) {

            if (gp.keyH.escPressed) {
                currentState = moveState;
                return;
            }
            if (gp.mouseH.clickX >= 0) {

                int clickRow = gp.ui.getWorldY(gp.mouseH.clickY) / gp.tileSize;
                int clickCol = gp.ui.getWorldX(gp.mouseH.clickX) / gp.tileSize;
                int diffRow = clickRow - gp.player.getCenterRow();
                int diffCol = clickCol - gp.player.getCenterCol();
                if (Math.abs(diffRow) + Math.abs(diffCol) > 1) {
                    currentState = moveState;
                } else {
                    currentState = attackState;
                    if (diffRow == 1) {
                        gp.player.direction = "down";
                    }
                    if (diffRow == -1) {
                        gp.player.direction = "up";
                    }
                    if (diffCol == 1) {
                        gp.player.direction = "right";
                    }
                    if (diffCol == -1) {
                        gp.player.direction = "left";
                    }
                    playerAttack();
                }
                gp.mouseH.clickX = -1;
                gp.mouseH.clickY = -1;
            }
        } else if (gp.player.attacking) {
            playerAttack();
        }
    }

    void changePosition(Entity entity) {

        int row = entity.getCenterRow();
        int col = entity.getCenterCol();
        entity.worldX = col * gp.tileSize;
        entity.worldY = row * gp.tileSize;
    }

    void adjustPosition() {

        for (Entity entity : gp.npc)
            if (entity != null) {
                changePosition(entity);
            }
        changePosition(gp.player);
    }

    void drawMoveTile(Graphics2D g2) {

        for (int i = 0; i < gp.worldSize; i++) {
            for (int j = 0; j < gp.worldSize; j++) {
                if (dist[i][j] <= 5) {
                    Color c;
                    if (i == gp.player.getCenterRow() && j == gp.player.getCenterCol()) {
                        c = new Color(8, 160, 69);
                    } else if (i == gp.ui.getWorldY(gp.mouseH.moveY) / gp.tileSize && j == gp.ui.getWorldX(gp.mouseH.moveX) / gp.tileSize) {
                        c = Color.yellow;
                    } else {
                        c = new Color(63, 63, 63);
                    }
                    gp.ui.drawSquare(g2, gp.ui.getScreenX(gp.tileSize * j), gp.ui.getScreenY(gp.tileSize * i), c);
                }
            }
        }
    }

    void drawSkillTile(Graphics2D g2) {

        int[] nxt = {0, -1, 0, 1, 0};
        int currentRow = gp.player.getCenterRow();
        int currentCol = gp.player.getCenterCol();
        Color c;
        for (int i = 0; i < 4; i++) {

            int nextRow = currentRow + nxt[i];
            int nextCol = currentCol + nxt[i + 1];
            if (nextRow == gp.ui.getWorldY(gp.mouseH.moveY) / gp.tileSize && nextCol == gp.ui.getWorldX(gp.mouseH.moveX) / gp.tileSize) {
                c = Color.yellow;
            } else {
                c = Color.red;
            }
            gp.ui.drawSquare(g2, gp.ui.getScreenX(gp.tileSize * nextCol), gp.ui.getScreenY(gp.tileSize * nextRow), c);
            if (c.equals(Color.yellow)) {
                for (int j = 2; j <= skillUsing.attackRange; j++) {
                    nextRow += nxt[i];
                    nextCol += nxt[i + 1];
                    gp.ui.drawSquare(g2, gp.ui.getScreenX(gp.tileSize * nextCol), gp.ui.getScreenY(gp.tileSize * nextRow), c);
                }
            }
        }
    }

    void draw(Graphics2D g2) {

        if (currentState == moveState) {
            drawMoveTile(g2);
        } else if (currentState == useSkillState) {
            drawSkillTile(g2);
        }
    }
}
