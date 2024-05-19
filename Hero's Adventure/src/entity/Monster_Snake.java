package entity;

import main.GamePanel;

import java.util.Random;

public class Monster_Snake extends Monster {

    public Monster_Snake(GamePanel gp) {
        super(gp);

        setDefaultValue();
        getImage();
    }

    void setDefaultValue() {

        name = "snake";
        realName = "Snake";
        level = (int) (2 + Math.random() * 3);
        for (int i = 1; i < level; i++) {

            double random = Math.random();
            if (random <= 0.25) {
                STR++;
            } else
            if (random <= 0.7) {
                VIT++;
            } else
            if (random <= 0.8) {
                DEX++;
            } else {
                INT++;
            }
        }
        width *= 0.9;
        height *= 0.9;
        speed = 2;
        setStats();
        HP = maxHP;
        MP = maxMP;
    }
}
