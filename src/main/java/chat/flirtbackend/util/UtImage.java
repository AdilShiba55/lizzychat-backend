package chat.flirtbackend.util;
import lombok.SneakyThrows;
import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.*;
import java.util.Map;

public class UtImage {

    public static final Integer IMAGE_TARGET_SIZE = 512;
    public static final String IMAGE_DEFAULT_EXTENSION = "jpeg";

    @SneakyThrows
    public static byte[] getProcessedImageByteArray(MultipartFile multipartFile, Integer targetSize) {
        String extension = UtFile.getExtension(multipartFile);
        return getProcessedImageByteArray(multipartFile.getBytes(), extension, targetSize);
    }

    @SneakyThrows
    public static byte[] getProcessedImageByteArray(byte[] imageByteArray, String extension, Integer targetSize) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageByteArray);
        BufferedImage originalImage = ImageIO.read(byteArrayInputStream);

        BufferedImage processedImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, targetSize);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(processedImage, extension, baos);
        return baos.toByteArray();
    }

    @SneakyThrows
    public static byte[] getBluredImageByteArray(byte[] imageByteArray, String format) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageByteArray);
        BufferedImage originalImage = ImageIO.read(bais);

        // Gaussian blur kernel for a smoother effect
        float[] matrix = {
                1/273f,  4/273f,  7/273f,  4/273f, 1/273f,
                4/273f, 16/273f, 26/273f, 16/273f, 4/273f,
                7/273f, 26/273f, 41/273f, 26/273f, 7/273f,
                4/273f, 16/273f, 26/273f, 16/273f, 4/273f,
                1/273f,  4/273f,  7/273f,  4/273f, 1/273f
        };

        BufferedImageOp blurOp = new ConvolveOp(new Kernel(5, 5, matrix), ConvolveOp.EDGE_NO_OP, null);

        // Apply multiple passes for a smoother blur
        BufferedImage blurredImage = originalImage;
        for (int i = 0; i < 22; i++) {
            blurredImage = blurOp.filter(blurredImage, null);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(blurredImage, format, baos);
        return baos.toByteArray();
    }
}