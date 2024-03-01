import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;


public class Workspace extends JFrame {
    public enum Tool {
        BRUSH, ERASER;
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
    private HashMap<JButton, Tool> buttonToolMap;





    public Workspace() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1366, 768);
        setupOptionBar();
        setupToolBar();
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

    private void setupToolBar() {
        add(toolBar, BorderLayout.NORTH);
        setupToolsPanel();
    }

    private void setupToolsPanel() {
        buttonToolMap = new HashMap<JButton, Tool>();
        JPanel toolsPanel = new JPanel();
        toolsPanel.setBackground(new Color(227,227,227));
        toolsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
        toolsPanel.add(createTool("Brush", "src/brush.png", Tool.BRUSH));
        toolsPanel.add(createTool("Eraser", "src/eraser.png", Tool.ERASER));


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




    private JPanel createTool(String toolName, String iconPath, Tool toolType) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JLabel label = new JLabel(toolName);
        label.setHorizontalAlignment(JLabel.CENTER);
        ImageIcon icon = new ImageIcon(iconPath);
        JButton button = new JButton(icon);
        buttonToolMap.put(button,toolType);
        button.setFocusable(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.addActionListener(this::actionPerfomed);
        panel.add(button);
        panel.add(label);
        return panel;
    }


    public Tool getSelectedTool() {
        return selectedTool;
    }

    public Tool setSelectedTool(Tool tool) {
        return selectedTool = tool;
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

    public void actionPerfomed(ActionEvent e){
        selectedTool = buttonToolMap.get(e.getSource());
        selectedToolButton = (JButton) e.getSource();
        for (JButton button : buttonToolMap.keySet()) {
            if (button == selectedToolButton) {
                button.setBackground(highlightedColor);
            } else {
                button.setBackground(defaultColor);
            }
        }
    }



}