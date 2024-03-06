package org.Tools;

import org.MainLogic.Canvas;
import org.MainLogic.Workspace;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Eraser implements MouseMotionListener, MouseListener {
    private final Canvas canvas;
    Graphics2D g2d;
    int lastXPositionOfCursor, lastYPositionOfCursor;

    public Eraser(Canvas canvas) {
        this.canvas = canvas;
        canvas.addMouseMotionListener(this);
        canvas.addMouseListener(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        draw(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        draw(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        lastXPositionOfCursor = e.getX();
        lastYPositionOfCursor = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private void draw(MouseEvent e) {
        if (Workspace.getInstance().getSelectedTool() == Workspace.Tool.ERASER) {
            g2d = canvas.getG2d();
            g2d.setPaint(Workspace.getInstance().getSecondColor());
            g2d.drawLine(lastXPositionOfCursor, lastYPositionOfCursor, e.getX(), e.getY());
            canvas.repaint();
            lastXPositionOfCursor = e.getX();
            lastYPositionOfCursor = e.getY();
        }
    }
}
