import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
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
    private static Workspace instance;
    private Color firstColor = Color.BLACK;
    private Color secondColor = Color.WHITE;
    private HashMap<JButton, Tool> buttonToolMap;
    private FileManagement fileManagment = new FileManagement();

    public Workspace() {
        instance = this;
        setTitle(fileManagment.updateTitle());
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
        newMenuItem.addActionListener(e -> {
            Canvas.getInstance().clearCanvas();
            setTitle(fileManagment.updateTitle());
            fileManagment.setPathToActualFile(null);
        });


        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(e -> {
            fileManagment.openFile(this);
            setTitle(fileManagment.updateTitle());
        });

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(e -> {
            fileManagment.saveFile(this);
            setTitle(fileManagment.updateTitle());
        });

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
        toolsPanel.setBackground(UIManager.getColor("Panel.background"));

        toolsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5); // marginesy

        gbc.gridx = 0; // Kolumna 0
        gbc.gridy = 0; // Rząd 0
        toolsPanel.add(createTool("Brush", "src/brush.png", Tool.BRUSH), gbc);

        gbc.gridx = 1; // Kolumna 1
        gbc.gridy = 0; // Rząd 0
        toolsPanel.add(createTool("Eraser", "src/eraser.png", Tool.ERASER), gbc);



        JPanel firstColorPanel = new JPanel();
        firstColorPanel.setLayout(new BoxLayout(firstColorPanel, BoxLayout.Y_AXIS));
        JButton firstColorButton = new JButton();
        firstColorButton.setBackground(firstColor);
        firstColorButton.setPreferredSize(new Dimension(30,30));
        firstColorButton.setMaximumSize(firstColorButton.getPreferredSize());
        firstColorButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel firstColorLabel = new JLabel("Color 1");
        firstColorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        firstColorPanel.add(firstColorButton);
        firstColorPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        firstColorPanel.add(firstColorLabel);


        firstColorButton.addActionListener(e -> {
            firstColor = JColorChooser.showDialog(null, "Choose a first color", firstColor);
            firstColorButton.setBackground(firstColor);
            canvas.updateCanvasColors();
        });

        gbc.gridx = 2; // Col 2
        gbc.gridy = 0; // Row 0
        toolsPanel.add(firstColorPanel, gbc);

        JPanel secondColorPanel = new JPanel();
        secondColorPanel.setLayout(new BoxLayout(secondColorPanel, BoxLayout.Y_AXIS));

        JButton secondColorButton = new JButton();
        secondColorButton.setBackground(secondColor);
        secondColorButton.setPreferredSize(new Dimension(30,30));
        secondColorButton.setMaximumSize(secondColorButton.getPreferredSize());
        secondColorButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel secondColorLabel = new JLabel("Color 2");
        secondColorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        secondColorPanel.add(secondColorButton);
        secondColorPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        secondColorPanel.add(secondColorLabel);

        secondColorButton.addActionListener(e -> {
            secondColor = JColorChooser.showDialog(null, "Choose a second color", secondColor);
            secondColorButton.setBackground(secondColor);
            canvas.updateCanvasColors();
        });

        gbc.gridx = 3; // Col 3
        gbc.gridy = 0; // Row 0
        toolsPanel.add(secondColorPanel, gbc);

        //Invisible element that takes all the space
        gbc.gridx = 4;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        toolsPanel.add(Box.createHorizontalGlue(), gbc);

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
        JButton selectedToolButton = (JButton) e.getSource();
        for (JButton button : buttonToolMap.keySet()) {
            if (button == selectedToolButton) {
                button.setBackground(highlightedColor);
            } else {
                button.setBackground(defaultColor);
            }
        }
    }

    public boolean checkIfFileExists(Path path) {
        return Files.exists(path);
    }
    public static Workspace getInstance() {
        return instance;
    }

}