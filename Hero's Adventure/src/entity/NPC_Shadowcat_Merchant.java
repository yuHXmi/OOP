package entity;

import main.GamePanel;
import skill.SolarFist;

import java.util.Random;

public class NPC_Shadowcat_Merchant extends NPC {

    public NPC_Shadowcat_Merchant(GamePanel gp) {
        super(gp);

        name = "shadowcat_merchant";
        realName = "Shadowcat Merchant";
        speed = 1;
        type = 2;
        getImage();
        setDialogue();
    }

    public void speak() {

        if (!doneEvent && gp.player.currentGold >= 10000) {
            gp.player.skillList.add(new SolarFist(gp));
            gp.player.currentGold -= 10000;
            gp.messageManager.add("You lost 10000 gold!");
            gp.messageManager.add("But received SOLAR FIST!!!");
            dialogueIndex = 6;
            doneEvent = true;
        }

        if (dialogue[dialogueIndex] == null) {
            dialogueIndex = dialogueEndIndex = dialogueDelay = 0;
            gp.gameState = gp.playState;
        }
        dialogueDelay++;
        if (dialogueDelay >= gp.FPS / 30 && dialogueEndIndex < dialogue[dialogueIndex].length()) {

            dialogueDelay = 0;
            dialogueEndIndex++;
        }
        if (dialogueEndIndex == dialogue[dialogueIndex].length()) {
            continueDialogue = false;
        }
        gp.ui.currentDialogue = dialogue[dialogueIndex].substring(0, dialogueEndIndex);
        faceToPlayer();
    }

    void setDialogue() {

        dialogue[0] = "Ah, a curious soul has found their way to the \nShadowcat's lair.";
        dialogue[1] = "Welcome, traveler, to a trove where the rarest \ntreasures glimmer in the moon's whisper.";
        dialogue[2] = "I am the keeper of secrets and wares that are as \nelusive as the night's breeze.";
        dialogue[3] = "Peruse my collection, but know that each artifact \ncomes with a tale... and a price.";
        dialogue[4] = "Now, what hidden desire brings you to my silent \ndoorstep?";

        dialogue[6] = "It's seem like you have enough gold for this. \nTake this skill and give me your gold.";
    }
}
