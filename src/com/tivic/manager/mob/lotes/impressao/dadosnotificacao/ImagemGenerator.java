package com.tivic.manager.mob.lotes.impressao.dadosnotificacao;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Image;
import org.apache.xmlgraphics.util.UnitConv;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.DataMatrixWriter;

public class ImagemGenerator {
	
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
    
    public BufferedImage gerarQrCodeDataMatrix(String qrCodeContent, int datamatrixSize) throws WriterException {
        DataMatrixWriter datamatrixWriter = new DataMatrixWriter();
	    BitMatrix bitMatrix = datamatrixWriter.encode(qrCodeContent, BarcodeFormat.DATA_MATRIX, datamatrixSize, datamatrixSize);

	    int datamatrixImageWidth = bitMatrix.getWidth();
	    int datamatrixImageHeight = bitMatrix.getHeight();
	    BufferedImage qrCodeImage = new BufferedImage(datamatrixImageWidth, datamatrixImageHeight, BufferedImage.TYPE_INT_RGB);

	    for (int x = 0; x < datamatrixImageWidth; x++) {
	        for (int y = 0; y < datamatrixImageHeight; y++) {
	            qrCodeImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
	        }
	    }
        return qrCodeImage;
    }

}
