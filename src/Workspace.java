import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Workspace extends JFrame {
    private final JMenuBar menuBar = new JMenuBar();
    private final JToolBar toolBar = new JToolBar();
    private final Color highlightedColor = new Color(173, 216, 230);
    private final Color defaultColor = UIManager.getColor("Panel.background");
    private Canvas canvas;
    private JButton selectedToolButton = null;

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
        toolBar.add(createTool("Brush", "src/brush1.png", e -> activateBrush()));

        add(toolBar, BorderLayout.NORTH);
    }

    private JPanel createTool(String toolName, String iconPath, ActionListener actionListener) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(toolName);
        ImageIcon icon = new ImageIcon(iconPath);
        JButton button = new JButton(icon);

        configureButton(button, action -> {
            if (selectedToolButton != null) {
                selectedToolButton.setBackground(defaultColor);
            }
            button.setBackground(highlightedColor);
            actionListener.actionPerformed(action);
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

    private void deselectTool() {
        if (selectedToolButton != null) {
            selectedToolButton.setBackground(defaultColor);
            selectedToolButton = null;
        }
    }

    private void activateBrush() {
        new Brush(canvas);
    }

}