import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class FileManagement {

    private Path pathToActualFile; // Zmienna dla ścieżki aktualnie otwartego pliku
    private final JFileChooser fileChooser = new JFileChooser();

    public FileManagement() {
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        pathToActualFile = null;
    }

    public void openFile(JFrame frame) {
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            pathToActualFile = file.toPath();
            try {
                BufferedImage image = ImageIO.read(file);
                Canvas.getInstance().setImage(image); // Załóżmy, że Canvas ma metodę setImage
                Canvas.getInstance().repaint(); // Odświeżanie canvas
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error opening the file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void saveFile(JFrame frame) {
        if (pathToActualFile != null) {
            saveImageToFile(frame, pathToActualFile.toFile());
        } else {
            fileChooser.setSelectedFile(new File("untitled.png"));
            int result = fileChooser.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (fileToSave.exists()) {
                    int overwrite = JOptionPane.showConfirmDialog(frame, "File already exists, do you want to overwrite it?", "File already exists", JOptionPane.YES_NO_OPTION);
                    if (overwrite == JOptionPane.YES_OPTION) {
                        saveImageToFile(frame, fileToSave);
                    }
                } else {
                    saveImageToFile(frame, fileToSave);
                }
            }
        }
    }

    private void saveImageToFile(JFrame frame, File file) {
        try {
            ImageIO.write(Canvas.getInstance().getImage(), "PNG", file);
            pathToActualFile = file.toPath();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error saving the file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Path getPathToActualFile() {
        return pathToActualFile;
    }

    public void setPathToActualFile(Path pathToActualFile) {
        this.pathToActualFile = pathToActualFile;
    }

    public String updateTitle() {
        String appName = "rePaint";
        String fileName = pathToActualFile != null ? pathToActualFile.getFileName().toString() : "untitled.png";
        return appName + " - " + fileName;
    }
}
