package com.tivic.manager.eCarta;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoDAO;

public class GeneratePDF {
	//Numero do lote
	static String lot = "XX";
	// Matriz de Relacionamento 
	static String matriz = "XXXXX";
	
	public static void generatePdf(ECartaItem item, String codObjeto, String path, Connection connect) throws IOException {
		LoteImpressao lote = LoteImpressaoDAO.get(Integer.parseInt(item.getNumeroLote()), connect);
		matriz = TextService.searchMatriz(lote);
		lot = item.getNumeroLote();
		
		String nameComplementary = null;
				
		nameComplementary = "e-Carta_" + matriz + "_" + lot + "_1_" + codObjeto + "_complementar.pdf";
				
		File someFile = new File(path + "/" + nameComplementary);
		FileOutputStream fos = new FileOutputStream(someFile);
		fos.write(item.getBlbArquivo());
		fos.flush();
		fos.close();
	}
}
