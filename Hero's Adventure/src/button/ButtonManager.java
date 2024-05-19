package button;

import main.GamePanel;

public class ButtonManager {

    GamePanel gp;
    public Button strInc, vitInc, dexInc, intInc;
    public Button newGame, exit;
    public Button backToTitle;

    public ButtonManager(GamePanel gp) {
        this.gp = gp;
    }

    public void checkButton() {

        int x = gp.mouseH.clickX;
        int y = gp.mouseH.clickY;
        if (x < 0)
            return;

        // TITLE STATE
        if (gp.gameState == gp.titleState) {

            checkNewGame(x, y);
            checkExit(x, y);
        }

        // CHARACTER STATE
        if (gp.gameState == gp.characterState) {

            checkPlusButton(x, y);
        }

        // GAME OVER STATE
        if (gp.gameState == gp.gameOverState) {

            checkBack(x, y);
        }

        // END GAME STATE
        if (gp.gameState == gp.endGameState) {

            checkBack(x, y);
        }
    }

    void checkExit(int x, int y) {

        if (exit.isClicked(x, y)) {
            System.exit(0);
        }
    }

    void checkBack(int x, int y) {

        if (backToTitle != null && backToTitle.isClicked(x, y)) {
            gp.soundManager.player_death.stop();
            gp.setUpGame();
        }
    }

    void checkNewGame(int x, int y) {

        if (newGame.isClicked(x, y)) {
            gp.soundManager.button_failed.replay();
            gp.gameState = gp.playState;
        }
    }

    void checkPlusButton(int x, int y) {

        if (strInc.isClicked(x, y)) {

            gp.mouseH.clickX = -1;
            gp.mouseH.clickY = -1;
            if (gp.player.statPoint > 0) {
                gp.soundManager.button_success.replay();
                gp.player.STR++;
                gp.player.statPoint--;
                gp.player.setStats();
            } else {
                gp.soundManager.button_failed.replay();
            }
        }

        if (vitInc.isClicked(x, y)) {

            gp.mouseH.clickX = -1;
            gp.mouseH.clickY = -1;
            if (gp.player.statPoint > 0) {
                gp.soundManager.button_success.replay();
                gp.player.VIT++;
                gp.player.HP += gp.player.level * 5;
                gp.player.statPoint--;
                gp.player.setStats();
            } else {
                gp.soundManager.button_failed.replay();
            }
        }

        if (dexInc.isClicked(x, y)) {

            gp.mouseH.clickX = -1;
            gp.mouseH.clickY = -1;
            if (gp.player.statPoint > 0) {
                gp.soundManager.button_success.replay();
                gp.player.DEX++;
                gp.player.statPoint--;
                gp.player.setStats();
            } else {
                gp.soundManager.button_failed.replay();
            }
        }

        if (intInc.isClicked(x, y)) {

            gp.mouseH.clickX = -1;
            gp.mouseH.clickY = -1;
            if (gp.player.statPoint > 0) {
                gp.soundManager.button_success.replay();
                gp.player.INT++;
                gp.player.statPoint--;
                gp.player.setStats();
            } else {
                gp.soundManager.button_failed.replay();
            }
        }
    }
}
