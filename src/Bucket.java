import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Stack;

public class Bucket implements MouseListener, MouseMotionListener {
    private final Canvas canvas;
    Graphics2D g2d;

    public Bucket(Canvas canvas) {
        this.canvas = canvas;
        canvas.addMouseListener(this);
        g2d = canvas.getG2d();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        try {
            fill(e);
        } catch (StackOverflowError error) {
            System.out.println("StackOverflowError");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

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

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    private void fill(MouseEvent e){
        if (Workspace.getInstance().getSelectedTool() == Workspace.Tool.BUCKET) {
            int x = e.getX();
            int y = e.getY();
            Color targetColor = new Color(canvas.getImage().getRGB(x, y));
            Color replacementColor = canvas.firstColor;
            floodFill(x, y, targetColor, replacementColor);
            canvas.repaint();


        }
    }
    public void floodFill(int x, int y, Color targetColor, Color replacementColor) {
           BufferedImage image = canvas.getImage();

        if (targetColor.equals(replacementColor)) {
            return;
        }
        if (image.getRGB(x, y) != targetColor.getRGB()) {
            return;
        }
        Stack<Point> stack = new Stack<>();
        stack.push(new Point(x, y));
        while (!stack.isEmpty()) {
            Point point = stack.pop();
            int px = point.x;
            int py = point.y;
            if (px < 0 || py < 0 || px >= image.getWidth() || py >= image.getHeight()) {
                continue;
            }
            if (image.getRGB(px, py) != targetColor.getRGB()) {
                continue;
            }
            image.setRGB(px, py, replacementColor.getRGB());
            stack.push(new Point(px - 1, py));
            stack.push(new Point(px + 1, py));
            stack.push(new Point(px, py - 1));
            stack.push(new Point(px, py + 1));
        }
        canvas.repaint();
    }
}
