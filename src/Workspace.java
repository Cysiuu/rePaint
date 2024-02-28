import javax.swing.*;
import java.awt.*;

public class Workspace extends JFrame {
    private final JMenuBar menuBar = new JMenuBar();
    private Canvas canvas;

    public Workspace() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1366, 768);
        setupMenu();
        setupCanvas();
        setVisible(true);
    }

    private void setupCanvas() {
        canvas = new Canvas();
        add(canvas);

    }

    private void setupMenu() {
        JMenu toolsMenu = new JMenu("Tools");

        JMenuItem brushItem = new JMenuItem("Brush");
        brushItem.addActionListener(e -> activateBrush());
        toolsMenu.add(brushItem);


        menuBar.add(toolsMenu);
        setJMenuBar(menuBar);
    }

    private void activateBrush() {
        new Brush(canvas);
    }
}