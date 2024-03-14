package pl.cysiu.rePaint.MainLogic;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageGeneratorAPI {

    private static ImageGeneratorAPI instance;

    public static ImageGeneratorAPI getInstance() {
        if (instance == null) {
            instance = new ImageGeneratorAPI();
        }
        return instance;
    }

    public BufferedImage generateImage(String prompt) throws IOException, InterruptedException, URISyntaxException {

        String apiKey = "ab1c63f26eb482c4b896277c57f607d5cb7cb1cb8a605bf094fb1536d6a9979dddf628a5ba91831a7768effafd8e11c9"; // YOUR OWN API KEY FOR CLIPDROP.CO

        String boundary = "--------------------------" + Long.toHexString(System.currentTimeMillis());

        String requestBodyBuilder = "--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"prompt\"\r\n\r\n" +
                prompt + "\r\n" +
                "--" + boundary + "--\r\n";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI("https://clipdrop-api.co/text-to-image/v1"))
                .header("x-api-key", apiKey)
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(BodyPublishers.ofString(requestBodyBuilder))
                .build();


        Path tempFile = Files.createTempFile("image", ".png");
        HttpResponse<Path> response = client.send(postRequest, HttpResponse.BodyHandlers.ofFile(tempFile));

        if (response.statusCode() != 200) {
            JOptionPane.showMessageDialog(
                    Workspace.getInstance().getFrame(),
                    "Error generating the image",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            throw new IOException("Error generating the image");
        }

            BufferedImage image = ImageIO.read(tempFile.toFile());

            Files.delete(tempFile);

            return image;



    }
}
