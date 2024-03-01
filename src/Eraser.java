import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Eraser implements MouseMotionListener, MouseListener {
    private final Canvas canvas;

    int lastXPositionOfCursor, lastYPositionOfCursor;
    public Eraser(Canvas canvas) {
        this.canvas = canvas;
        canvas.addMouseMotionListener(this);
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
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        lastXPositionOfCursor = e.getX();
        lastYPositionOfCursor = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    private void draw(MouseEvent e){
        Graphics2D g2d;
        if (canvas.getWorkspace().getSelectedTool() == Workspace.Tool.ERASER) {
            g2d = canvas.getG2d();
            g2d.setPaint(canvas.getWorkspace().getSecondColor());
            g2d.drawLine(lastXPositionOfCursor, lastYPositionOfCursor, e.getX(), e.getY());
            g2d.fillOval(e.getX() - 5, e.getY() - 5, 10, 10);
            canvas.repaint();
            lastXPositionOfCursor = e.getX();
            lastYPositionOfCursor = e.getY();
        }
    }
}
