import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Brush implements MouseMotionListener {
    private final Canvas canvas;

    public Brush(Canvas canvas) {
        this.canvas = canvas;
        canvas.addMouseMotionListener(this);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
            Graphics2D g2d = canvas.getG2d();
            g2d.setPaint(Color.BLACK);
            int x = e.getX();
            int y = e.getY();
            g2d.fillOval(x - 5, y - 5, 10, 10);
            canvas.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

}
