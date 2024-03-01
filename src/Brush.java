import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Brush implements MouseMotionListener, MouseListener {
    private final Canvas canvas;
    Graphics2D g2d;

    int lastX,lastY;
    public Brush(Canvas canvas) {
        this.canvas = canvas;
        canvas.addMouseMotionListener(this);
        g2d = canvas.getG2d();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        draw(e);


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
        lastX = e.getX();
        lastY = e.getY();
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

    private void draw(MouseEvent e){
        if (canvas.getWorkspace().getSelectedTool() == Workspace.Tool.BRUSH) {
            g2d = canvas.getG2d();
            g2d.setPaint(canvas.getWorkspace().getFirstColor());
            g2d.drawLine(lastX, lastY, e.getX(), e.getY());
            g2d.fillOval(e.getX() - 5, e.getY() - 5, 10, 10);
            canvas.repaint();
            lastX = e.getX();
            lastY = e.getY();
        }
    }


}
