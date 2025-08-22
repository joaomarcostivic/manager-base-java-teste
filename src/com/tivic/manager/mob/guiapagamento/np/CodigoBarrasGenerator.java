package com.tivic.manager.mob.guiapagamento.np;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.apache.xmlgraphics.util.UnitConv;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

public class CodigoBarrasGenerator {

	public BufferedImage gerar(String code) throws IOException {
        Code128Bean barcodeGenerator = new Code128Bean();
        final int dpi = 160;

        barcodeGenerator.setModuleWidth(UnitConv.in2mm(1.0f / dpi));
        barcodeGenerator.doQuietZone(false);

        int largura = mmToPixel(60, dpi);
        int altura = mmToPixel(15, dpi);

        BitmapCanvasProvider canvasProvider = new BitmapCanvasProvider(
            largura, BufferedImage.TYPE_BYTE_BINARY, false, 0
        );
        barcodeGenerator.generateBarcode(canvasProvider, code);
        BufferedImage imagemOriginal = canvasProvider.getBufferedImage();
        canvasProvider.finish();

        BufferedImage imagemRedimensionada = new BufferedImage(largura, altura, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g = imagemRedimensionada.createGraphics();
        Image imagemEscalada = imagemOriginal.getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
        g.drawImage(imagemEscalada, 0, 0, null);
        g.dispose();

        int alturaDigitos = mmToPixel(3.0f, dpi);
        int alturaImagem = imagemRedimensionada.getHeight();
        return imagemRedimensionada.getSubimage(0, 0, largura, alturaImagem - alturaDigitos);
    }

    private int mmToPixel(float mm, int dpi) {
        return Math.round(mm * dpi / 25.4f);
    }
}