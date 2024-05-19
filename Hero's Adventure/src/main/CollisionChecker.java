package main;

import entity.Entity;

import java.util.List;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public boolean checkTile(Entity entity) {

        int leftWorldX = entity.worldX + entity.solidArea.x;
        int rightWorldX = leftWorldX + entity.solidArea.width;
        int topWorldY = entity.worldY + entity.solidArea.y;
        int bottomWorldY = topWorldY + entity.solidArea.height;

        int leftCol = leftWorldX / gp.tileSize;
        int rightCol = rightWorldX / gp.tileSize;
        int topRow = topWorldY / gp.tileSize;
        int bottomRow = bottomWorldY / gp.tileSize;

        int tileNum1 = 0, tileNum2 = 0;

        switch(entity.direction) {

            case "up":
                topRow = (topWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[topRow][leftCol][gp.tileM.currentMap];
                tileNum2 = gp.tileM.mapTileNum[topRow][rightCol][gp.tileM.currentMap];
                break;
            case "down":
                bottomRow = (bottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[bottomRow][leftCol][gp.tileM.currentMap];
                tileNum2 = gp.tileM.mapTileNum[bottomRow][rightCol][gp.tileM.currentMap];
                break;
            case "left":
                leftCol = (leftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[topRow][leftCol][gp.tileM.currentMap];
                tileNum2 = gp.tileM.mapTileNum[bottomRow][leftCol][gp.tileM.currentMap];
                break;
            case "right":
                rightCol = (rightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[topRow][rightCol][gp.tileM.currentMap];
                tileNum2 = gp.tileM.mapTileNum[bottomRow][rightCol][gp.tileM.currentMap];
                break;
        }

        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision){
            entity.collisionOn = true;
            return true;
        }
        return false;
    }

    public int checkEntity(Entity entity, List<Entity> target) {

        int index = 100;

        entity.solidArea.x += entity.worldX;
        entity.solidArea.y += entity.worldY;
        switch(entity.direction) {
            case "up":
                entity.solidArea.y -= entity.speed;
                break;
            case "down":
                entity.solidArea.y += entity.speed;
                break;
            case "left":
                entity.solidArea.x -= entity.speed;
                break;
            case "right":
                entity.solidArea.x += entity.speed;
                break;
        }

        for (int i = 0; i < target.size(); i++)
            if (target.get(i) != null && target.get(i) != entity) {

                target.get(i).solidArea.x += target.get(i).worldX;
                target.get(i).solidArea.y += target.get(i).worldY;

                if (entity.solidArea.intersects(target.get(i).solidArea)) {
                    entity.collisionOn = true;
                    index = i;
                }

                target.get(i).solidArea.x = target.get(i).defaultSolidAreaX;
                target.get(i).solidArea.y = target.get(i).defaultSolidAreaY;
        }
        entity.solidArea.x = entity.defaultSolidAreaX;
        entity.solidArea.y = entity.defaultSolidAreaY;

        return index;
    }

    public boolean checkPlayer(Entity entity) {

        entity.solidArea.x += entity.worldX;
        entity.solidArea.y += entity.worldY;
        switch(entity.direction) {
            case "up":
                entity.solidArea.y -= entity.speed;
                break;
            case "down":
                entity.solidArea.y += entity.speed;
                break;
            case "left":
                entity.solidArea.x -= entity.speed;
                break;
            case "right":
                entity.solidArea.x += entity.speed;
                break;
        }

        gp.player.solidArea.x += gp.player.worldX;
        gp.player.solidArea.y += gp.player.worldY;

        boolean interact = false;
        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
            interact = true;
        }

        entity.solidArea.x = entity.defaultSolidAreaX;
        entity.solidArea.y = entity.defaultSolidAreaY;
        gp.player.solidArea.x = gp.player.defaultSolidAreaX;
        gp.player.solidArea.y = gp.player.defaultSolidAreaY;

        return interact;
    }
}
