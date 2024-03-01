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
    Color firstColor = Color.BLACK;
    Color secondColor = Color.WHITE;


    public Canvas() {
        initializeCanvas(1366, 768);
        resizeButton();
    }

    private void initializeCanvas(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0,0, width, height);
        updateCanvasColors();
        setLayout(null);
        addMouseListener(this);
        addMouseMotionListener(this);
        g2d.setStroke(new BasicStroke(10));
    }

    public void updateCanvasColors() {
        Workspace workspace = getWorkspace();
        if (workspace != null) {
            Color firstColor = workspace.getFirstColor();
            Color secondColor = workspace.getSecondColor();
            g2d.setPaint(firstColor);
        }
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
        g.setColor(Color.WHITE); // Ustawienie domyślnego koloru tła dla nowego obszaru
        g.fillRect(0, 0, newWidth, newHeight);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        image = newImage;
        g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Ponowne ustawienie właściwości pędzla
        updateCanvasProperties();

        button.setBounds(newWidth - 10, newHeight - 10, 10, 10);
        repaint();
    }

    private void updateCanvasProperties() {
        // Przywrócenie koloru, grubości linii itp.
        g2d.setColor(firstColor); // Ustaw kolor, który chcesz używać do rysowania
        g2d.setStroke(new BasicStroke(10)); // Przykładowa grubość linii
        // Możesz dodać więcej właściwości zgodnie z potrzebami
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

    public Workspace getWorkspace() {
        return (Workspace) SwingUtilities.getWindowAncestor(this);
    }

}
