package pl.cysiu.rePaint.MainLogic;

import pl.cysiu.rePaint.Tools.Brush;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Stack;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener {

    private static Canvas instance;
    private final JButton button = new JButton();
    private final int width = 640;
    private final int height = 480;
    private int strokeSize = 5;
    public Color firstColor = Color.BLACK;
    public Color secondColor = Color.WHITE;
    private BufferedImage image;
    private BufferedImage backgroundRememberImage;
    private Graphics2D g2d;
    private boolean resizing = false;
    private int mouseX, mouseY;
    private Stroke stroke = new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    private Stack<BufferedImage> undoStack = new Stack<>();
    private Stack<BufferedImage> redoStack = new Stack<>();


    public Canvas() {
        initializeCanvas(width, height);
        resizeButton();
    }

    public static Canvas getInstance() {
        return instance;
    }

    private void initializeCanvas(int width, int height) {
        instance = this;
        setLayout(null);
        setBackground(new Color(205,215,230));
        setPreferredSize(new Dimension(width+50, height+50));
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        backgroundRememberImage = image;
        g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        g2d.setStroke(stroke);
        updateCanvasColors(firstColor, secondColor);
        addMouseListener(this);
        addMouseMotionListener(this);
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
                captureCanvasState();
            }

            public void mouseReleased(MouseEvent e) {
                resizing = false;
            }
        });
        add(button);
    }

    private void resizeCanvas(int newWidth, int newHeight) {
        if (newWidth > backgroundRememberImage.getWidth() || newHeight > backgroundRememberImage.getHeight()) {
            BufferedImage newImage = new BufferedImage(Math.max(newWidth, backgroundRememberImage.getWidth()),
                    Math.max(newHeight, backgroundRememberImage.getHeight()),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = newImage.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, newImage.getWidth(), newImage.getHeight());
            g.drawImage(backgroundRememberImage, 0, 0, null);
            g.dispose();
            backgroundRememberImage = newImage;
        }

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImage.createGraphics();
        g2.drawImage(backgroundRememberImage, 0, 0, null);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        image = resizedImage;
        g2d = image.createGraphics();
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
        this.backgroundRememberImage = image;
        clearStacksForRedoAndUndo();
        updateCanvasProperties();
        new Brush(this);

    }

    private void updateCanvasProperties() {
        setBackground(new Color(205,215,230));
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

        BufferedImage tempImage = new BufferedImage(backgroundRememberImage.getWidth(), backgroundRememberImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = tempImage.createGraphics();
        g2.drawImage(backgroundRememberImage, 0, 0, null);
        g2.drawImage(image.getSubimage(0,0,image.getWidth(),image.getHeight()), 0, 0, null);
        g2.dispose();
        backgroundRememberImage = tempImage;


    }
    @Override
    public void mousePressed(MouseEvent e) {

        //If the mouse is pressed inside the canvas, capture the state of the canvas
        if (e.getX() < getImage().getWidth() && e.getY() < getImage().getHeight()) {

            captureCanvasState();
        }
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
        this.strokeSize = (int) ((BasicStroke) stroke).getLineWidth();
        this.stroke = stroke;
        g2d.setStroke(stroke);
    }

    public int getStroke() {
        return strokeSize;
    }

    public BufferedImage getImage() {
        return image;
    }
    public void setBackgroundRememberImage(BufferedImage backgroundRememberImage) {
        this.backgroundRememberImage = backgroundRememberImage;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        this.backgroundRememberImage = image;
        g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        updateCanvasProperties();
        repaint();
    }

    public void captureCanvasState() {
        BufferedImage copyOfImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = copyOfImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        undoStack.push(copyOfImage);

    }

    //Shortcuts methods

    public void clearStacksForRedoAndUndo() {
        undoStack.clear();
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(image);
            image = undoStack.pop();
            setImage(image);
        }
    }
    public void redo(){
        if (!redoStack.isEmpty()) {
            undoStack.push(image);
            image = redoStack.pop();
            setImage(image);
        }
    }

}
