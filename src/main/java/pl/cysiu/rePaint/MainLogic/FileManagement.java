package pl.cysiu.rePaint.MainLogic;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class FileManagement {

    String[] fileTypes = {"jpg", "jpeg", "png", "gif", "bmp"};

    private final JFileChooser fileChooser = new JFileChooser();
    private Path pathToActualFile;

    public FileManagement() {
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        pathToActualFile = null;
    }

    public void NewFile(JFrame frame) {
        Canvas.getInstance().clearCanvas();
        frame.setTitle(updateTitle());
        pathToActualFile = null;
    }

    public void openFile(JFrame frame) {
        JFileChooser frameToChooseFile = new JFileChooser();

        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String extension = f.getName().substring(f.getName().lastIndexOf(".") + 1);
                for (String fileType : fileTypes) {
                    if (extension.equalsIgnoreCase(fileType)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "Image files";
            }
        };

        frameToChooseFile.setFileFilter(filter);
        int result = frameToChooseFile.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = frameToChooseFile.getSelectedFile();
            pathToActualFile = file.toPath();

            try {
                BufferedImage image = ImageIO.read(file);
                Canvas.getInstance().captureCanvasState();
                Canvas.getInstance().setImage(image);
                Canvas.getInstance().clearStacksForRedoAndUndo();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error opening the file", "Error", JOptionPane.ERROR_MESSAGE);
                NewFile(frame);
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
            JOptionPane.showMessageDialog(frame, "Error saving the file", "Error", JOptionPane.ERROR_MESSAGE);
        }
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
