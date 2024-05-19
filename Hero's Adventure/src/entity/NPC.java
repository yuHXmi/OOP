package entity;

import main.GamePanel;

public class NPC extends Entity {

    public NPC(GamePanel gp) {
        super(gp);

        type = gp.isNPC;
    }
}
