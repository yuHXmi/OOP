package entity;

import main.GamePanel;
import java.util.Random;

public class Monster_Mutant_Mouse extends Monster {

    public Monster_Mutant_Mouse(GamePanel gp) {
        super(gp);

        setDefaultValue();
        getImage();
    }

    void setDefaultValue() {

        name = "mutant_mouse";
        realName = "Mutant Mouse";
        level = (int) (6 + Math.random() * 5);
        for (int i = 1; i < level; i++) {

            double random = Math.random();
            if (random <= 0.5) {
                STR++;
            } else
            if (random <= 0.6) {
                VIT++;
            } else
            if (random <= 0.7) {
                DEX++;
            } else {
                INT++;
            }
        }
        speed = 2;
        setStats();
        HP = maxHP;
        MP = maxMP;
    }
}
