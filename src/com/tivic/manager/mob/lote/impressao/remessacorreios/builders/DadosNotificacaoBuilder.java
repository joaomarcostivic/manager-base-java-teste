package com.tivic.manager.mob.lote.impressao.remessacorreios.builders;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.GregorianCalendar;

import org.apache.xmlgraphics.util.UnitConv;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.lote.impressao.DadosNotificacao;
import com.tivic.manager.util.Util;
import com.tivic.sol.connection.CustomConnection;

public class DadosNotificacaoBuilder {
	private DadosNotificacao dadosNotificacao;
	
	public DadosNotificacaoBuilder(DadosNotificacao dados, CorreiosEtiqueta correiosEtiqueta) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		dadosNotificacao = dados;
		String nrEtiqueta = formarNrEtiqueta(correiosEtiqueta);
		correiosEtiqueta.setCdAit(dados.getCdAit());
		correiosEtiqueta.setDtEnvio(new GregorianCalendar());
		addCorreiosEtiqueta(correiosEtiqueta);
		addSgServico(correiosEtiqueta);
		addBarCode(nrEtiqueta, customConnection);
		addCodeSG(dados, nrEtiqueta);
		addBarCode2D(dados, nrEtiqueta);
		addQrCode(dados, nrEtiqueta, customConnection);
	}
	
	public DadosNotificacaoBuilder addCorreiosEtiqueta(CorreiosEtiqueta correiosEtiqueta) {
		dadosNotificacao.setCorreiosEtiqueta(correiosEtiqueta);
		return this;
	}
	
	public DadosNotificacaoBuilder addSgServico(CorreiosEtiqueta correiosEtiqueta) {
		dadosNotificacao.setSgServico(correiosEtiqueta.getSgServico());
		return this;
	}
	
	public DadosNotificacaoBuilder addBarCode(String nrEtiqueta, CustomConnection customConnection) throws IOException {
	    String barCode = "AR" + nrEtiqueta + ParametroServices.getValorOfParametro("MOB_CORREIOS_SG_CLIENTE", customConnection.getConnection());
	    dadosNotificacao.setBarCodeImage(montaImage(barCode));
	    dadosNotificacao.setBarCode(barCode);
	    return this;
	}
	
	public DadosNotificacaoBuilder addCodeSG(DadosNotificacao dados, String nrEtiqueta) throws IOException {
		String codeSG = dados.getSgServico() + nrEtiqueta + "BR";
	    dadosNotificacao.setCodeSgImage(montaImage(codeSG));
	    dadosNotificacao.setCodeSG(codeSG);
	    return this;
	}
	
	public DadosNotificacaoBuilder addBarCode2D(DadosNotificacao dados, String nrEtiqueta) throws IOException {
		String barCode2D = dados.getSgServico() + nrEtiqueta + "AA";
	    dadosNotificacao.setBarCode2DImage(montaImage(barCode2D));
		dadosNotificacao.setBarCode2D(barCode2D);
		return this;
	}
	
	private BufferedImage montaImage(String code) throws IOException {
		Code128Bean barcodeGenerator = new Code128Bean();
	    final int dpi = 160;
	    barcodeGenerator.setModuleWidth(UnitConv.in2mm(1.0f / dpi));
	    barcodeGenerator.doQuietZone(false);

	    int desiredWidth = mmToPixel(60, dpi);
	    int desiredHeight = mmToPixel(15, dpi);

	    BitmapCanvasProvider canvasProvider = new BitmapCanvasProvider(desiredWidth, BufferedImage.TYPE_BYTE_BINARY, false, 0);
	    barcodeGenerator.generateBarcode(canvasProvider, code);
	    BufferedImage barcodeImage = canvasProvider.getBufferedImage();
	    canvasProvider.finish();

	    BufferedImage resizedBarcodeImage = new BufferedImage(desiredWidth, desiredHeight, BufferedImage.TYPE_BYTE_BINARY);
	    Graphics2D graphics = resizedBarcodeImage.createGraphics();
	    Image scaledBarcodeImage = barcodeImage.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
	    graphics.drawImage(scaledBarcodeImage, 0, 0, null);
	    graphics.dispose();

	    int lineHeight = mmToPixel(3.0f, dpi);
	    int barcodeHeight = resizedBarcodeImage.getHeight();
	    BufferedImage barcodeImageWithoutDigits = resizedBarcodeImage.getSubimage(0, 0, desiredWidth, barcodeHeight - lineHeight);
	    return barcodeImageWithoutDigits;
	}
	
	private int mmToPixel(float mm, int dpi) {
	    return Math.round(mm * dpi / 25.4f);
	}
		
	public DadosNotificacaoBuilder addQrCode(DadosNotificacao dados, String nrEtiqueta, CustomConnection customConnection) throws WriterException {
	    String qrCodeContent = dados.getSgServico() + nrEtiqueta + "AA" + ParametroServices.getValorOfParametro("MOB_CORREIOS_SG_CLIENTE", customConnection.getConnection());
	    int datamatrixSize = 18;

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
	    dadosNotificacao.setQrCodeImage(qrCodeImage);
	    return this;
	}

	private String formarNrEtiqueta(CorreiosEtiqueta correiosEtiqueta) throws Exception {
		int qtdEtiqueta = 8;
		return Util.fillNum(correiosEtiqueta.getNrEtiqueta(), qtdEtiqueta) 
				+ correiosEtiqueta.getNrDigitoVerificador();		
	}
	
	public DadosNotificacao build() {
		return dadosNotificacao;
	}
}
