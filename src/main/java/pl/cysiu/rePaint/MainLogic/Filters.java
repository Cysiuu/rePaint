package pl.cysiu.rePaint.MainLogic;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Filters {

    public void grayScaleFilter(String filter) {
        BufferedImage image = new BufferedImage(Canvas.getInstance().getImage().getWidth(), Canvas.getInstance().getImage().getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.drawImage(Canvas.getInstance().getImage(), 0, 0, null);
        g2d.dispose();

        if (filter.equals("grayscale")) {
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    int p = image.getRGB(x, y);
                    int a = (p >> 24) & 0xff;
                    int r = (p >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    int b = p & 0xff;
                    int avg = (r + g + b) / 3;
                    p = (a << 24) | (avg << 16) | (avg << 8) | avg;
                    image.setRGB(x, y, p);
                }
            }
            Canvas.getInstance().captureCanvasState();
            Canvas.getInstance().setImage(image);
        }
    }

    public void sepiaFilter(String filter) {

        BufferedImage image = new BufferedImage(Canvas.getInstance().getImage().getWidth(), Canvas.getInstance().getImage().getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.drawImage(Canvas.getInstance().getImage(), 0, 0, null);
        g2d.dispose();

        if (filter.equals("sepia")) {
            int width = image.getWidth();
            int height = image.getHeight();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int p = image.getRGB(x, y);
                    int a = (p >> 24) & 0xff;
                    int r = (p >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    int b = p & 0xff;
                    int newRed = (int) (0.393 * r + 0.769 * g + 0.189 * b);
                    int newGreen = (int) (0.349 * r + 0.686 * g + 0.168 * b);
                    int newBlue = (int) (0.272 * r + 0.534 * g + 0.131 * b);
                    r = Math.min(newRed, 255);
                    g = Math.min(newGreen, 255);
                    b = Math.min(newBlue, 255);
                    p = (a << 24) | (r << 16) | (g << 8) | b;
                    image.setRGB(x, y, p);
                }
            }
            Canvas.getInstance().captureCanvasState();
            Canvas.getInstance().setImage(image);
        }

    }

}
