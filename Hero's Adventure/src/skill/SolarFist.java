package skill;

import main.GamePanel;

public class SolarFist extends Skill {

    public SolarFist(GamePanel gp) {
        super(gp);
    }

    void setDefaultValue() {

        name = "solar_fist";
        realName = "Solar_fist";
        attackRange = 2;
        attackDamage = 1.8;
        cooldown = 2;
        manaCost = 20;
    }
}
