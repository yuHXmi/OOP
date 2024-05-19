package main;

import entity.Entity;

import java.util.LinkedList;
import java.util.Queue;

public class PathFinder {

    GamePanel gp;
    int[][] dist;
    Queue<Integer> queueRow = new LinkedList<Integer>();
    Queue<Integer> queueCol = new LinkedList<Integer>();
    final int[] nxt = {0, -1, 0, 1, 0};

    public PathFinder(GamePanel gp) {
        this.gp = gp;

        dist = new int[gp.worldSize][gp.worldSize];
    }

    void reset() {
        for (int i = 0; i < gp.worldSize; i++)
            for (int j = 0; j < gp.worldSize; j++)
                dist[i][j] = gp.worldSize * 2;
        queueRow.clear();
        queueCol.clear();
    }

    public void trackAllPath(Entity entity) {

        reset();
        int startRow = entity.getCenterRow();
        int startCol = entity.getCenterCol();

        dist[startRow][startCol] = 0;
        queueRow.add(startRow);
        queueCol.add(startCol);

        while (!queueRow.isEmpty()) {

            int row = queueRow.poll();
            int col = queueCol.poll();
            if (dist[row][col] > 5) {
                break;
            }

            for (int i = 0; i < 4; i++) {

                int nextRow = row + nxt[i];
                int nextCol = col + nxt[i + 1];
                int tileNum = gp.tileM.mapTileNum[nextRow][nextCol][gp.tileM.currentMap];

                entity.collisionOn = false;
                if (gp.tileM.tile[tileNum].collision) {
                    entity.collisionOn = true;
                }
                for (Entity other : gp.npc)
                    if (other != null && other != entity && other.getCenterRow() == nextRow && other.getCenterCol() == nextCol) {
                        entity.collisionOn = true;
                    }
                if (!entity.collisionOn && dist[nextRow][nextCol] > dist[row][col] + 1) {

                    dist[nextRow][nextCol] = dist[row][col] + 1;
                    queueRow.add(nextRow);
                    queueCol.add(nextCol);
                }
            }
        }
    }

    public void findPath(Entity entity, int goalRow, int goalCol) {

        reset();
        int startRow = entity.getCenterRow();
        int startCol = entity.getCenterCol();

        dist[goalRow][goalCol] = 0;
        queueRow.add(goalRow);
        queueCol.add(goalCol);
        while (!queueRow.isEmpty()) {
            int row = queueRow.poll();
            int col = queueCol.poll();

            if (row == startRow && col == startCol) {
                break;
            }

            for (int i = 0; i < 4; i++) {

                int nextRow = row + nxt[i];
                int nextCol = col + nxt[i + 1];
                int tileNum = gp.tileM.mapTileNum[nextRow][nextCol][gp.tileM.currentMap];

                entity.collisionOn = false;
                if (gp.tileM.tile[tileNum].collision) {
                    entity.collisionOn = true;
                }
                for (Entity other : gp.npc)
                    if (other != null && other != entity && other.getCenterRow() == nextRow && other.getCenterCol() == nextCol) {
                        entity.collisionOn = true;
                    }
                if (!entity.collisionOn && dist[nextRow][nextCol] > dist[row][col] + 1 && (entity != gp.player || gp.battleHandler.dist[nextRow][nextCol] <= 5)) {

                    dist[nextRow][nextCol] = dist[row][col] + 1;
                    queueRow.add(nextRow);
                    queueCol.add(nextCol);
                }
            }
        }

        if (dist[startRow][startCol] < gp.worldSize * 2) {
            int nextRow, nextCol;
            String nextDirection = "";
            for (int i = 0; i < 4; i++) {

                nextRow = startRow + nxt[i];
                nextCol = startCol + nxt[i + 1];
                if (dist[nextRow][nextCol] + 1 == dist[startRow][startCol]) {
                    // 0, -1, 0, 1, 0
                    // left
                    // up
                    // right
                    // down
                    switch (i) {
                        case 0:
                            nextDirection = "left";
                            break;
                        case 1:
                            nextDirection = "up";
                            break;
                        case 2:
                            nextDirection = "right";
                            break;
                        case 3:
                            nextDirection = "down";
                            break;
                    }
                }
                if (nextDirection.equals(entity.direction)) {
                    break;
                }
            }

            if (!nextDirection.isEmpty()) {
                entity.direction = nextDirection;
            }
        }
    }
}
