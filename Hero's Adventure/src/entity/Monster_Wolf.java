package entity;

import main.GamePanel;

public class Monster_Wolf extends Monster {

    public Monster_Wolf(GamePanel gp) {
        super(gp);

        setDefaultValue();
        getImage();
    }

    void setDefaultValue() {

        name = "wolf";
        realName = "Wolf";
        level = 1;
        width *= 0.9;
        height *= 0.9;
        speed = 2;
        setStats();
        HP = maxHP;
        MP = maxMP;
    }
}
