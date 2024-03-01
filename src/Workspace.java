import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;


public class Workspace extends JFrame {
    public enum Tool {
        BRUSH, ERASER
    }
    private Tool selectedTool;
    private final JMenuBar menuBar = new JMenuBar();
    private final JToolBar toolBar = new JToolBar();
    private final Color highlightedColor = new Color(173, 216, 230); // Light blue
    private final Color defaultColor = UIManager.getColor("Panel.background");
    private Canvas canvas;
    private JButton selectedToolButton = null;
    private Color firstColor = Color.BLACK;
    private Color secondColor = Color.WHITE;




    public Workspace() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1366, 768);
        setupOptionBar();
        setupMenu();
        setupCanvas();
        setupTools();
        setVisible(true);

    }

    private void setupTools() {
        Brush brush = new Brush(canvas);
        Eraser eraser = new Eraser(canvas);
    }

    private void setupOptionBar() {
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);
    }

    private void setupCanvas() {
        canvas = new Canvas();
        add(canvas);

    }

    private void setupMenu() {
        add(toolBar, BorderLayout.NORTH);
        setupToolsPanel();
    }

    private void setupToolsPanel() {

        JPanel toolsPanel = new JPanel();
        toolsPanel.setBackground(new Color(227,227,227));
        toolsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
        toolsPanel.add(createTool("Brush", "src/brush.png", e -> activateBrush()));
        toolsPanel.add(createTool("Eraser", "src/eraser.png", e -> activateEraser()));


        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));

        JButton firstColorButton = new JButton("");
        JButton secondColorButton = new JButton("");

        firstColorButton.setBackground(firstColor);
        secondColorButton.setBackground(secondColor);

        firstColorButton.addActionListener(e -> {
            firstColor = JColorChooser.showDialog(null, "Choose a first color", firstColor);
            firstColorButton.setBackground(firstColor);
            canvas.updateCanvasColors();
        });

        secondColorButton.addActionListener(e -> {
            secondColor = JColorChooser.showDialog(null, "Choose a second color", secondColor);
            secondColorButton.setBackground(secondColor);
            canvas.updateCanvasColors();
        });

        colorPanel.add(firstColorButton);
        colorPanel.add(secondColorButton);
        toolsPanel.add(colorPanel);
        this.add(toolsPanel, BorderLayout.NORTH);

    }




    private JPanel createTool(String toolName, String iconPath, ActionListener actionListener) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JLabel label = new JLabel(toolName);
        label.setHorizontalAlignment(JLabel.CENTER);
        ImageIcon icon = new ImageIcon(iconPath);
        JButton button = new JButton(icon);


        configureButton(button, action -> {
            if (selectedToolButton != null) {
                selectedToolButton.setBackground(defaultColor);
            }
            button.setBackground(highlightedColor);
            actionListener.actionPerformed(action);
            selectedToolButton = button;
        });

        panel.add(button);
        panel.add(label);

        return panel;
    }

    private void configureButton(JButton button, ActionListener actionListener) {
        button.setFocusable(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.addActionListener(actionListener);
    }
    public Tool getSelectedTool() {
        return selectedTool;
    }
    public Tool setSelectedTool(Tool tool) {
        return selectedTool = tool;
    }
    private void activateBrush() {
        selectedTool = (Tool.BRUSH);
    }
    private void activateEraser() {
        selectedTool = (Tool.ERASER);

    }

    public Color getFirstColor() {
        return firstColor;
    }

    public Color getSecondColor() {
        return secondColor;
    }

    public void setFirstColor(Color firstColor) {
        this.firstColor = firstColor;
    }

    public void setSecondColor(Color secondColor) {
        this.secondColor = secondColor;
    }


}