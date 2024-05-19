package main;

import entity.*;

import java.util.ArrayList;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {

        this.gp = gp;
    }

    public void setObject() {

    }

    public void setNPC() {

        gp.npc1 = new ArrayList<Entity>();
        gp.respawnMonster1 = new ArrayList<Entity>();
        gp.npc2 = new ArrayList<Entity>();
        gp.respawnMonster2 = new ArrayList<Entity>();

        gp.npc = gp.npc1;
        gp.respawnMonster = gp.respawnMonster1;

        gp.npc1.add(new NPC_Shadowcat_Merchant(gp));
        gp.npc1.get(0).worldX = gp.tileSize * 12;
        gp.npc1.get(0).worldY = gp.tileSize * 12;

        gp.npc1.add(new Monster_Snake(gp));
        gp.npc1.get(1).worldX = gp.tileSize * 35;
        gp.npc1.get(1).worldY = gp.tileSize * 35;

        gp.npc1.add(new Monster_Snake(gp));
        gp.npc1.get(2).worldX = gp.tileSize * 34;
        gp.npc1.get(2).worldY = gp.tileSize * 34;

        gp.npc1.add(new Monster_Snake(gp));
        gp.npc1.get(3).worldX = gp.tileSize * 27;
        gp.npc1.get(3).worldY = gp.tileSize * 38;

        gp.npc1.add(new Monster_Wolf(gp));
        gp.npc1.get(4).worldX = gp.tileSize * 13;
        gp.npc1.get(4).worldY = gp.tileSize * 27;

        gp.npc1.add(new Monster_Wolf(gp));
        gp.npc1.get(5).worldX = gp.tileSize * 15;
        gp.npc1.get(5).worldY = gp.tileSize * 11;

        gp.npc1.add(new Monster_Wolf(gp));
        gp.npc1.get(6).worldX = gp.tileSize * 25;
        gp.npc1.get(6).worldY = gp.tileSize * 12;

        gp.npc2.add(new Monster_Mutant_Mouse(gp));
        gp.npc2.get(0).worldX = gp.tileSize * 37;
        gp.npc2.get(0).worldY = gp.tileSize * 37;

        gp.npc2.add(new Monster_Mutant_Mouse(gp));
        gp.npc2.get(1).worldX = gp.tileSize * 30;
        gp.npc2.get(1).worldY = gp.tileSize * 30;

        gp.npc2.add(new Monster_Mutant_Mouse(gp));
        gp.npc2.get(2).worldX = gp.tileSize * 23;
        gp.npc2.get(2).worldY = gp.tileSize * 23;

        gp.npc2.add(new Monster_Dark_Lord(gp));
        gp.npc2.get(3).worldX = gp.tileSize * 30;
        gp.npc2.get(3).worldY = gp.tileSize * 13;
    }
}
