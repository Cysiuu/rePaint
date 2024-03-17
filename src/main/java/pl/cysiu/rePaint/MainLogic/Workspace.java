package pl.cysiu.rePaint.MainLogic;

import pl.cysiu.rePaint.Tools.Brush;
import pl.cysiu.rePaint.Tools.Bucket;
import pl.cysiu.rePaint.Tools.Eraser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;


public class Workspace extends JFrame {
    private static Workspace instance;
    private final JMenuBar menuBar = new JMenuBar();
    private final JToolBar toolBar = new JToolBar();
    private final Color highlightedColor = new Color(173, 216, 230); // Light blue
    private final Color defaultColor = UIManager.getColor("Panel.background");
    private final FileManagement fileManagement = new FileManagement();
    private final Font font = new Font("Roboto", Font.BOLD, 12);
    Filters filter = new Filters();
    private Tool selectedTool;
    private Canvas canvas;
    private Color firstColor = Color.BLACK;
    private Color secondColor = Color.WHITE;
    private HashMap<JButton, Tool> buttonToolMap;
    public Workspace() {
        instance = this;
        setTitle(fileManagement.updateTitle());
        setSize(1366, 768);
        setupCanvas();
        setupOptionBar();
        setupToolBar();
        setupTools();
        setupBottomParametersBar();
        setupShorcuts();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    public static Workspace getInstance() {
        return instance;
    }

    private void setupTools() {
        new Brush(canvas);
        new Eraser(canvas);
        new Bucket(canvas);
    }

    private void setupCanvas() {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        canvas = new Canvas();
        panel.add(canvas, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);

    }


    private void setupOptionBar() {
        setJMenuBar(menuBar);
        JMenu fileMenu = getFileMenu();

        JMenu Filters = new JMenu("Filter");
        JMenuItem grayscale = new JMenuItem("Grayscale");
        JMenuItem sepia = new JMenuItem("Sepia");

        Filters.add(grayscale);
        Filters.add(sepia);

        grayscale.addActionListener(e -> filter.grayScaleFilter("grayscale"));

        sepia.addActionListener(e -> filter.sepiaFilter("sepia"));

        JMenu Images = new JMenu("Image");
        JMenuItem generateAiImageItem = new JMenuItem("Generate AI Image");

        Images.add(generateAiImageItem);

        generateAiImageItem.addActionListener(e -> {


            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setPreferredSize(new Dimension(300, 200));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 50));

            JLabel descriptionLabel = new JLabel("Description of image");
            descriptionLabel.setFont(font);
            descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            descriptionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.add(descriptionLabel);


            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setPreferredSize(new Dimension(300, 100));

            JTextArea descriptionField = new JTextArea();
            descriptionField.setLineWrap(true);
            descriptionField.setFont(font);
            descriptionField.setAlignmentX(Component.CENTER_ALIGNMENT);

            scrollPane.setViewportView(descriptionField);


            panel.add(scrollPane);


            int result = JOptionPane.showConfirmDialog(
                    this,
                    panel,
                    "Enter Description",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                String description = descriptionField.getText();
                if (description != null && !description.isEmpty()) generateAiImage(description);
            }
        });


        menuBar.add(fileMenu);
        menuBar.add(Filters);
        menuBar.add(Images);
    }

    private JMenu getFileMenu() {
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
        return fileMenu;
    }

    private void setupToolBar() {
        add(toolBar, BorderLayout.NORTH);
        setupToolsPanel();
    }

    private void setupToolsPanel() {
        JPanel toolsPanelWrapper = new JPanel();
        toolsPanelWrapper.setLayout(new BorderLayout());
        toolsPanelWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        buttonToolMap = new HashMap<>();
        JPanel toolsPanel = new JPanel();

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


        setGbc(3, 0, gbc);
        toolsPanel.add(setupColorPanel("Color 1", firstColor), gbc);

        setGbc(4, 0, gbc);
        toolsPanel.add(setupColorPanel("Color 2", secondColor), gbc);


        setGbc(5, 0, gbc);
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));

        JSlider slider = new JSlider(1, 50, canvas.getStroke());
        slider.setPreferredSize(new Dimension(150, 30));
        JLabel sliderLabel = new JLabel("Size: " + slider.getValue());

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

        toolsPanelWrapper.add(toolsPanel, BorderLayout.WEST);
        toolBar.add(toolsPanelWrapper, BorderLayout.NORTH);

    }

    private JPanel createTool(String toolName, String iconPath, Tool toolType) {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setMaximumSize(new Dimension(50, 50));

        JLabel label = new JLabel(toolName);
        label.setFont(font);
        label.setVerticalAlignment(JLabel.CENTER);
        URL resource = getClass().getClassLoader().getResource(iconPath);

        assert resource != null;
        ImageIcon icon = new ImageIcon(resource);

        JButton button = new JButton(icon);
        buttonToolMap.put(button, toolType);
        button.setFocusable(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.addActionListener(this::actionPerformed);
        panel.add(button);
        panel.add(label);

        return panel;
    }

    private JPanel setupColorPanel(String colorName, Color colorToChange) {

        JPanel colorPanel = new JPanel();
        Button button = new Button();
        colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.Y_AXIS));
        button.setBackground(colorToChange);
        button.setPreferredSize(new Dimension(50, 30));
        button.setMaximumSize(button.getPreferredSize());
        button.setFocusable(false);

        button.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", colorToChange);
            if (newColor != null) {
                button.setBackground(newColor);
                updateColor(colorName.equals("Color 1"), newColor);
            }
        });

        JLabel label = new JLabel(colorName);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        label.setFont(font);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setHorizontalAlignment(JLabel.CENTER);

        colorPanel.add(button);
        colorPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        colorPanel.add(label);

        return colorPanel;
    }

    private void setupBottomParametersBar() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel coordinates = new JLabel("X: " + "Y: ");
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                coordinates.setText(mouseCoordinates(e));
            }

            public void mouseDragged(MouseEvent e) {
                coordinates.setText(mouseCoordinates(e));
            }
        });

        coordinates.setPreferredSize(new Dimension(100, 15));
        coordinates.setFont(font);

        bottomPanel.add(coordinates);
        bottomPanel.add(Box.createHorizontalGlue());
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupShorcuts(){

        // Ctrl + z - Undo
        canvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "undo");
        canvas.getActionMap().put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.undo();
            }
        });

        // Ctrl + y - Redo
        canvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), "redo");
        canvas.getActionMap().put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.redo();
            }
        });

    }


    private String mouseCoordinates(MouseEvent e) {
        if (e.getX() < canvas.getImage().getWidth() && e.getY() < canvas.getImage().getHeight()) {
            return "X: " + e.getX() + " Y: " + e.getY();
        } else {
            return ("X:  Y: ");
        }
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

    public void updateColor(boolean isFirstColor, Color newColor) {
        if (isFirstColor) {
            this.firstColor = newColor;
        } else {
            this.secondColor = newColor;
        }
        canvas.updateCanvasColors(firstColor, secondColor);
    }

    public void setGbc(int col, int row, GridBagConstraints gbc) {
        gbc.gridx = col;
        gbc.gridy = row;
    }

    public void actionPerformed(ActionEvent e) {
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

    private void generateAiImage(String description) {
        JDialog dialog = new JDialog(this, "Generating AI Image", false);
        dialog.setLayout(new FlowLayout());
        dialog.setSize(300, 100);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setLocationRelativeTo(this);

        JLabel label = new JLabel("Generating the image...");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        dialog.add(label);


        dialog.setVisible(true);
        new Thread(() -> {
            try {
                BufferedImage image = ImageGeneratorAPI.getInstance().generateImage(description);
                SwingUtilities.invokeLater(() -> {
                    Canvas.getInstance().setImage(image);
                    Canvas.getInstance().clearStacksForRedoAndUndo();
                    dialog.dispose();
                });
            } catch (IOException | URISyntaxException | InterruptedException e) {
                System.out.println("Error generating the image");
                dialog.dispose();
            }
        }).start();

    }

    public Component getFrame() {
        return this;
    }

    public enum Tool {
        BRUSH, ERASER, BUCKET
    }

}