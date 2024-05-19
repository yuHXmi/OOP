package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseListener, MouseMotionListener {

    GamePanel gp;
    public int clickX, clickY;
    public int moveX, moveY;

    public MouseHandler(GamePanel gp) {
        this.gp = gp;
        clickX = -1;
        clickY = -1;
    }
    @Override
    public void mouseClicked(MouseEvent e) {

        clickX = e.getX();
        clickY = e.getY();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        clickX = -1;
        clickY = -1;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        moveX = e.getX();
        moveY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        moveX = e.getX();
        moveY = e.getY();
    }
}
