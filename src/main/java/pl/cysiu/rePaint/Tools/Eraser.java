package pl.cysiu.rePaint.Tools;

import pl.cysiu.rePaint.MainLogic.Canvas;
import pl.cysiu.rePaint.MainLogic.Workspace;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Eraser implements MouseMotionListener, MouseListener {
    private final pl.cysiu.rePaint.MainLogic.Canvas canvas;
    Graphics2D g2d;
    int lastXPositionOfCursor, lastYPositionOfCursor;

    public Eraser(Canvas canvas) {
        this.canvas = canvas;
        canvas.addMouseMotionListener(this);
        canvas.addMouseListener(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        draw(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        drawDot(e);
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

    private void drawDot(MouseEvent e) {
        if (Workspace.getInstance().getSelectedTool() == Workspace.Tool.ERASER) {
            g2d = canvas.getG2d();
            g2d.setPaint(Workspace.getInstance().getSecondColor());
            g2d.fillOval(
                    lastXPositionOfCursor - canvas.getStroke()/2,
                    lastYPositionOfCursor - canvas.getStroke()/2,
                    canvas.getStroke(),
                    canvas.getStroke());
            canvas.repaint();
        }
    }
}
