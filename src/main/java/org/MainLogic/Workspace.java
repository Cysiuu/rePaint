package org.MainLogic;

import org.Tools.Brush;
import org.Tools.Bucket;
import org.Tools.Eraser;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;


public class Workspace extends JFrame {
    public enum Tool {
        BRUSH, ERASER, BUCKET
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
    private final FileManagement fileManagement = new FileManagement();
    Filters filter = new Filters();

    public Workspace() {
        instance = this;
        setTitle(fileManagement.updateTitle());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1366, 768);
        setupCanvas();
        setupOptionBar();
        setupToolBar();
        setupTools();
        setVisible(true);
    }


    private void setupTools() {
          new Brush(canvas);
          new Eraser(canvas);
          new Bucket(canvas);
    }

    private void setupOptionBar() {
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");

        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(e -> {
            fileManagement.NewFile(this);
            setTitle(fileManagement.updateTitle());
            fileManagement.setPathToActualFile(null);
        });


        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(e -> {
            fileManagement.openFile(this);
            setTitle(fileManagement.updateTitle());
        });

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(e -> {
            fileManagement.saveFile(this);
            setTitle(fileManagement.updateTitle());
        });

        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);

        JMenu Filters = new JMenu("Filter");
        JMenuItem grayscale = new JMenuItem("Grayscale");
        JMenuItem sepia = new JMenuItem("Sepia");

        Filters.add(grayscale);
        Filters.add(sepia);

        grayscale.addActionListener(e -> filter.grayScaleFilter("grayscale"));

        sepia.addActionListener(e -> filter.sepiaFilter("sepia"));

        menuBar.add(fileMenu);
        menuBar.add(Filters);
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

        buttonToolMap = new HashMap<>();
        JPanel toolsPanel = new JPanel();
        toolsPanel.setBackground(UIManager.getColor("Panel.background"));

        toolsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        setGbc(0, 0, gbc);


        toolsPanel.add(createTool("Brush", "brush.png", Tool.BRUSH), gbc);
        setGbc(1, 0, gbc);
        toolsPanel.add(createTool("Eraser", "eraser.png", Tool.ERASER), gbc);
        setGbc(2, 0, gbc);
        toolsPanel.add(createTool("Bucket", "bucket.png", Tool.BUCKET), gbc);



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

        setGbc(3, 0, gbc);
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

        setGbc(4, 0, gbc);
        toolsPanel.add(secondColorPanel, gbc);


        setGbc(5, 0, gbc);
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));

        JSlider slider = new JSlider(1, 50, canvas.getStroke());
        slider.setPreferredSize(new Dimension(100, 30));
        JLabel sliderLabel = new JLabel("Size: "+slider.getValue());

        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sliderLabel.setHorizontalAlignment(JLabel.CENTER);
        slider.addChangeListener(e -> {
            canvas.updateStroke(new BasicStroke(slider.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            sliderLabel.setText("Size: " + slider.getValue());
        });

        sliderPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        sliderPanel.add(sliderLabel);
        sliderPanel.add(slider);

        toolsPanel.add(sliderPanel, gbc);

        //Invisible element that takes all the space
        setGbc(6, 0, gbc);
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
        URL resource = getClass().getClassLoader().getResource(iconPath);
        assert resource != null;
        ImageIcon icon = new ImageIcon(resource);
        JButton button = new JButton(icon);
        buttonToolMap.put(button,toolType);
        button.setFocusable(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.addActionListener(this::actionPerformed);
        panel.add(button);
        panel.add(label);
        return panel;
    }


    public Tool getSelectedTool() {
        return selectedTool;
    }

    public Color getFirstColor() {
        return firstColor;
    }

    public Color getSecondColor() {
        return secondColor;
    }


    public void setGbc(int col, int row, GridBagConstraints gbc){
        gbc.gridx = col;
        gbc.gridy = row;
    }

    public void actionPerformed(ActionEvent e){
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

    public static Workspace getInstance() {
        return instance;
    }

}