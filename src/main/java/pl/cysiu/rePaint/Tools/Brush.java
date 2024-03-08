package pl.cysiu.rePaint.Tools;

import pl.cysiu.rePaint.MainLogic.Canvas;
import pl.cysiu.rePaint.MainLogic.Workspace;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Brush implements MouseMotionListener, MouseListener {
    private final Canvas canvas;
    Graphics2D g2d;

    int lastXPositionOfCursor, lastYPositionOfCursor;

    public Brush(Canvas canvas) {
        this.canvas = canvas;
        canvas.addMouseMotionListener(this);
        canvas.addMouseListener(this);
        g2d = canvas.getG2d();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        draw(e);
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
        if (Workspace.getInstance().getSelectedTool() == Workspace.Tool.BRUSH) {
            g2d = canvas.getG2d();
            g2d.setPaint(Workspace.getInstance().getFirstColor());
            g2d.drawLine(lastXPositionOfCursor, lastYPositionOfCursor, e.getX(), e.getY());
            canvas.repaint();
            lastXPositionOfCursor = e.getX();
            lastYPositionOfCursor = e.getY();
        }
    }


}
