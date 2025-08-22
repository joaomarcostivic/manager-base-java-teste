package com.tivic.manager.mob.mobile.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QrCodeImagemUtils {

    public static BufferedImage renderizarQrCode(BitMatrix bitMatrix) {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage qrCodeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                qrCodeImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return qrCodeImage;
    }

    public static void adicionarLogoNoQrCode(BufferedImage qrCodeImage, BufferedImage logo) {
        int width = qrCodeImage.getWidth();
        int height = qrCodeImage.getHeight();
        int logoSize = width / 5;
        int squareSize = logoSize;
        int centerX = (width - logoSize) / 2;
        int centerY = (height - logoSize) / 2;

        Graphics2D g = qrCodeImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g.setColor(Color.WHITE);
        g.fillRect(centerX, centerY, squareSize, squareSize);

        g.drawImage(logo, centerX, centerY, logoSize, logoSize, null);
        g.dispose();
    }
    
    public static BitMatrix gerarQrCode(String jsonParametros) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int QR_CODE_SIZE = 300;
        Map<EncodeHintType, Object> dimensionamento = new HashMap<>();
        dimensionamento.put(EncodeHintType.MARGIN, 1);
        return qrCodeWriter.encode(jsonParametros, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE, dimensionamento);
    }
    
    public static byte[] converterImagemParaByteArray(BufferedImage imagem) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(imagem, "PNG", baos);
        baos.flush();
        byte[] imagemBytes = baos.toByteArray();
        baos.close();
        return imagemBytes;
    }
    
}
