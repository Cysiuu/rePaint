package pl.cysiu.rePaint.Tools;

import pl.cysiu.rePaint.MainLogic.Canvas;
import pl.cysiu.rePaint.MainLogic.Workspace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;


public class Brush implements MouseMotionListener, MouseListener {
    private final Canvas canvas;
    private Graphics2D g2d;
    int lastXPositionOfCursor, lastYPositionOfCursor;

    public Brush(Canvas canvas) {
        this.canvas = canvas;
        canvas.addMouseMotionListener(this);
        canvas.addMouseListener(this);
//
    }

    @Override
    public void mousePressed(MouseEvent e) {
        drawDot(e);
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

    private void drawDot(MouseEvent e) {
        if (Workspace.getInstance().getSelectedTool() == Workspace.Tool.BRUSH) {
            g2d = canvas.getG2d();
            g2d.setPaint(Workspace.getInstance().getFirstColor());
            g2d.fillOval(
                    lastXPositionOfCursor - canvas.getStroke()/2,
                    lastYPositionOfCursor - canvas.getStroke()/2,
                    canvas.getStroke(),
                    canvas.getStroke());
            canvas.repaint();
        }
    }
}
