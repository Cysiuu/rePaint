package org.MainLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener {

    private static Canvas instance;
    private final JButton button = new JButton();
    private final int width = 640;
    private final int height = 480;
    private final int strokeSize = 5;
    public Color firstColor = Color.BLACK;
    public Color secondColor = Color.WHITE;
    private BufferedImage image;
    private Graphics2D g2d;
    private boolean resizing = false;
    private int mouseX, mouseY;
    private Stroke stroke = new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);


    public Canvas() {
        initializeCanvas(width, height);
        resizeButton();
    }

    public static Canvas getInstance() {
        return instance;
    }

    private void initializeCanvas(int width, int height) {
        instance = this;
        setPreferredSize(new Dimension(width+50, height+50));
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        updateCanvasColors(firstColor, secondColor);
        setLayout(null);
        addMouseListener(this);
        addMouseMotionListener(this);
        g2d.setStroke(stroke);
    }

    public void updateCanvasColors(Color firstColor, Color secondColor) {
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        g2d.setPaint(firstColor);
    }

    private void resizeButton() {
        button.setBounds(image.getWidth(), image.getHeight(), 10, 10);
        button.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (resizing) {
                    int dx = e.getXOnScreen() - mouseX;
                    int dy = e.getYOnScreen() - mouseY;
                    int newWidth = Math.max(image.getWidth() + dx, 10);
                    int newHeight = Math.max(image.getHeight() + dy, 10);
                    resizeCanvas(newWidth, newHeight);
                    mouseX += dx;
                    mouseY += dy;
                    button.setBounds(newWidth, newHeight, 10, 10);
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
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        updateCanvasProperties();
    }

    public void clearCanvas() {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2d.setPaint(firstColor);
        g2d.setStroke(stroke);
        updateCanvasProperties();
    }

    private void updateCanvasProperties() {
        g2d.setColor(firstColor);
        g2d.setStroke(stroke);
        button.setBounds(image.getWidth(), image.getHeight(), 10, 10);
        setPreferredSize(new Dimension(image.getWidth()+20, image.getHeight()+20));
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }

    //Methods not used for implementation but required by the interface
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

    public void updateStroke(Stroke stroke) {
        this.stroke = stroke;
        g2d.setStroke(stroke);
    }

    public int getStroke() {
        return strokeSize;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        updateCanvasProperties();
        repaint();
    }


}
