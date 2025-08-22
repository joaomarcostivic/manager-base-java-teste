package com.tivic.manager.mob.lote.impressao.codigobarras.MG;

import java.awt.image.BufferedImage;
import org.krysalis.barcode4j.impl.int2of5.Interleaved2Of5Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import com.tivic.manager.mob.lote.impressao.DadosNotificacao;
import com.tivic.manager.mob.lote.impressao.codigobarras.CodigoBarras;
import com.tivic.manager.mob.lote.impressao.codigobarras.IGerarCodigoBarras;
import com.tivic.manager.util.Util;
import com.tivic.sol.report.ReportCriterios;

public class GeraCodigoBarras implements IGerarCodigoBarras {
	
	@Override
	public CodigoBarras gerarCodigoBarras(DadosNotificacao dadosNotificacao, ReportCriterios reportCriterios) {
		CodigoBarras codigoBarras = new CodigoBarrasMGBuilder(dadosNotificacao, reportCriterios).build();
		gerarCodigoBarrasComDV(codigoBarras);
		gerarLinhaDigitavel(codigoBarras);
		generateBarcodeImage(codigoBarras);
		return codigoBarras;
	}
	
	private void gerarCodigoBarrasComDV(CodigoBarras codigoBarras){
		String codigoBarrasSemDv = codigoBarras.getIdentificadorArrecadacao()
								 + codigoBarras.getIdentificadorSegmento()
								 + codigoBarras.getIdentificadorValorReferencia()
								 + codigoBarras.getValorInfracao()
								 + codigoBarras.getCodigoFebraban()
								 + codigoBarras.getDtVencimento()
								 + codigoBarras.getDigitoPlaca()
								 + codigoBarras.getNrProcessamento()
								 + codigoBarras.getOrgaoAutuador()
								 + codigoBarras.getCodigoInfracao();
		codigoBarras.setDigitoGeral(Integer.toString(Util.getDvMod10(codigoBarrasSemDv)));
		 String codigoBarrasComDv = codigoBarras.getIdentificadorArrecadacao()
			 	  		  		  + codigoBarras.getIdentificadorSegmento()
			 	  		  		  + codigoBarras.getIdentificadorValorReferencia()
			 	  		  		  + codigoBarras.getDigitoGeral()
			 	  		  		  + codigoBarras.getValorInfracao()
			 	  		  		  + codigoBarras.getCodigoFebraban()
			 	  		  		  + codigoBarras.getDtVencimento()
			 	  		  		  + codigoBarras.getDigitoPlaca()
			 	  		  		  + codigoBarras.getNrProcessamento()
			 	  		  		  + codigoBarras.getOrgaoAutuador()
			 	  		  		  + codigoBarras.getCodigoInfracao();
		 codigoBarras.setCodigoBarrasComDv(codigoBarrasComDv);
	}
	
	private void gerarLinhaDigitavel(CodigoBarras codigoBarras) {
		String strBarras = codigoBarras.getCodigoBarrasComDv();
		
		String bloco1 = strBarras.substring (0, 11);
		String bloco2 = strBarras.substring (11, 22);
		String bloco3 = strBarras.substring (22, 33);
		String bloco4 = strBarras.substring (33, 44);
		String bloco1DV = bloco1 + "-" + Integer.toString (Util.getDvMod10 (bloco1));
		String bloco2DV = bloco2 + "-" + Integer.toString (Util.getDvMod10 (bloco2));
		String bloco3DV = bloco3 + "-" + Integer.toString (Util.getDvMod10 (bloco3));
		String bloco4DV = bloco4 + "-" + Integer.toString (Util.getDvMod10 (bloco4));			
		codigoBarras.setLinhaDigitavel(bloco1DV + " " + bloco2DV + " " + bloco3DV + " " + bloco4DV);		
	}
	
	private void generateBarcodeImage(CodigoBarras codigoBarras) {
		Interleaved2Of5Bean barcodeGenerator = new Interleaved2Of5Bean();
	    BitmapCanvasProvider canvas = new BitmapCanvasProvider(160, BufferedImage.TYPE_BYTE_BINARY, false, 0);
	    barcodeGenerator.setFontSize(0.0);
	    barcodeGenerator.generateBarcode(canvas, codigoBarras.getCodigoBarrasComDv());
	    codigoBarras.setBarcodeImage(canvas.getBufferedImage());
	}
}
