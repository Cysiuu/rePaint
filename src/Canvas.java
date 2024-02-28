import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener {
    private BufferedImage image;
    private Graphics2D g2d;
    private boolean resizing = false;
    private int mouseX, mouseY;
    private final JButton button = new JButton();

    public Canvas() {
        initializeCanvas(640, 480);
        resizeButton();
    }

    private void initializeCanvas(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
        fillCanvas(Color.WHITE);
        setLayout(null); // Pozwala na ręczne umieszczanie komponentów
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    private void resizeButton() {
        button.setBounds(image.getWidth() - 10, image.getHeight() - 10, 10, 10);
        button.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (resizing) {
                    // Oblicz nowe wymiary na podstawie przeciągania myszy
                    int dx = e.getXOnScreen() - mouseX;
                    int dy = e.getYOnScreen() - mouseY;
                    int newWidth = Math.max(image.getWidth() + dx, 10); // Ograniczenie minimalnego rozmiaru
                    int newHeight = Math.max(image.getHeight() + dy, 10);
                    resizeCanvas(newWidth, newHeight);
                    mouseX += dx;
                    mouseY += dy;
                }
            }
        });

        button.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                resizing = true;
                mouseX = e.getXOnScreen();
                mouseY = e.getYOnScreen();
            }

            public void mouseReleased(MouseEvent e) {
                resizing = false;
            }
        });
        add(button);
    }

    private void resizeCanvas(int newWidth, int newHeight) {
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, newWidth, newHeight);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        image = newImage;
        g2d = image.createGraphics();
        button.setBounds(newWidth - 10, newHeight - 10, 10, 10);
        repaint();
    }

    private void fillCanvas(Color color) {
        g2d.setPaint(color);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }


    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public Graphics2D getG2d() {
        return g2d;
    }
}
