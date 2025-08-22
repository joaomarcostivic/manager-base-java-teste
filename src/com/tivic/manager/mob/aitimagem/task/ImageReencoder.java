package com.tivic.manager.mob.aitimagem.task;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

public class ImageReencoder implements IImageReencoder {
    @Override
    public byte[] reencodeToJpg(byte[] imageBytes) throws Exception {
        try {
            ByteArrayInputStream byteArrayInput = new ByteArrayInputStream(imageBytes);
            BufferedImage image = ImageIO.read(byteArrayInput);

            if (image == null) {
                throw new Exception("Imagem não suportada ou corrompida");
            }

            BufferedImage rotatedImage = rotateImage90DegreesClockwise(image);

            ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
            ImageIO.write(rotatedImage, "jpg", byteArrayOutput);
            return byteArrayOutput.toByteArray();
        } catch (Exception ex) {
            throw new Exception("Falha ao processar imagem em JPEG", ex);
        }
    }

    private BufferedImage rotateImage90DegreesClockwise(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = new BufferedImage(height, width, image.getType());

        Graphics2D graphics = newImage.createGraphics();
        graphics.translate(height, 0);
        graphics.rotate(Math.toRadians(90));
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();

        return newImage;
    }
}
