package tile;

import main.GamePanel;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int[][][] mapTileNum;
    public final int mapIsland = 0;
    public final int mapDungeon = 1;
    public int currentMap = mapIsland;

    public TileManager(GamePanel gp) {

        this.gp = gp;

        tile = new Tile[100];
        mapTileNum = new int[gp.worldSize][gp.worldSize][2];

        getTileData();
        loadMap();
    }

    void getTileData() {

        InputStream is = getClass().getResourceAsStream("/maps/tiledata.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line;
        try {
            int index = 0;
            while ((line = reader.readLine()) != null) {
                tile[index] = new Tile(line, reader.readLine());
                index++;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    void loadMap() {

        try {

            InputStream is = getClass().getResourceAsStream("/maps/island.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            for (int row = 0; row < gp.worldSize; row++) {

                String line = reader.readLine();
                String numbers[] = line.split(" ");

                for (int col = 0; col < gp.worldSize; col++) {

                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[row][col][0] = num;
                }
            }

            is = getClass().getResourceAsStream("/maps/dungeon.txt");
            reader = new BufferedReader(new InputStreamReader(is));

            for (int row = 0; row < gp.worldSize; row++) {

                String line = reader.readLine();
                String numbers[] = line.split(" ");

                for (int col = 0; col < gp.worldSize; col++) {

                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[row][col][1] = num;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {

        int playerWorldCol = gp.player.worldX / gp.tileSize;
        int playerWorldRow = gp.player.worldY / gp.tileSize;

        int startCol = playerWorldCol - gp.maxScreenCol / 2;
        int endCol = startCol + gp.maxScreenCol + 2;
        int startRow = playerWorldRow - gp.maxScreenRow / 2;
        int endRow = startRow + gp.maxScreenRow + 2;

        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {

                int worldX = col * gp.tileSize;
                int worldY = row * gp.tileSize;
                int diffX = worldX - gp.player.worldX;
                int diffY = worldY - gp.player.worldY;
                int screenX = gp.player.screenX + diffX;
                int screenY = gp.player.screenY + diffY;

                int num = mapTileNum[row][col][currentMap];
                g2.drawImage(tile[num].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
        }
    }
}
