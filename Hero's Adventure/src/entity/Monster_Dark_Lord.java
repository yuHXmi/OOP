package entity;

import main.GamePanel;

public class Monster_Dark_Lord extends Monster {

    public Monster_Dark_Lord(GamePanel gp) {
        super(gp);

        setDefaultValue();
        getImage();
        setDialogue();
    }

    void setDialogue() {

        dialogue[0] = "Ah, so the fabled hero finally arrives. I must admit, I've \nbeen eagerly anticipating our confrontation.";
        dialogue[1] = "Your reign of terror ends now, DARK LORD. I will not let \nyou plunge this world into darkness!";
        dialogue[2] = "Brave words for someone who stands at the precipice of \noblivion. Do you truly believe you can defeat me?";
        dialogue[3] = "It's not just belief; it's a promise. The light will always \ntriumph over the shadows.";
        dialogue[4] = "Then come, hero. Show me the strength of your conviction. \nBut know this: when you fail, this world will be mine to \nshape as I see fit.";
    }

    public void setAction() {
        // nothing
    }

    public void update() {
        setScreenPosition();
    }

    void die() {
        gp.soundManager.battleMusic.stop();
        gp.soundManager.victory.replay();
        gp.gameState = gp.endGameState;
    }

    void setDefaultValue() {

        cutScene = true;
        name = "dark_lord";
        realName = "Dark Lord";
        level = 15;
        STR = 10;
        DEX = 4;
        VIT = 4;
        INT = 4;
        speed = 2;
        setStats();
        HP = maxHP;
        MP = maxMP;
    }
}
