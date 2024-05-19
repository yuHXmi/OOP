package entity;

import main.CollisionChecker;
import main.GamePanel;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Monster extends Entity {


    public Monster(GamePanel gp) {

        super(gp);
        type = gp.isMonster;
    }

    public void faceToPlayer() {
        int diffRow = getCenterRow() - gp.player.getCenterRow();
        int diffCol = getCenterCol() - gp.player.getCenterCol();
        if (Math.abs(diffRow) > Math.abs(diffCol)) {
            direction = (diffRow > 0 ? "up" : "down");
        } else {
            direction = (diffCol > 0 ? "left" : "right");
        }
    }
}
