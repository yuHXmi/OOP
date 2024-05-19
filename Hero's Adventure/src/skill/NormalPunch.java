package skill;

import main.GamePanel;

public class NormalPunch extends Skill {

    public NormalPunch(GamePanel gp) {
        super(gp);
    }

    void setDefaultValue() {

        name = "normal_punch";
        realName = "Normal Punch";
        attackRange = 1;
        attackDamage = 1;
        cooldown = 1;
        manaCost = 0;
    }

}
