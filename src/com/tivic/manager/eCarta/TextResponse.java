package com.tivic.manager.eCarta;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoDAO;

public class TextResponse {
	private static String numMatriz;
	private static String numLot;
	static GetParamns parameters = new GetParamns();
	
	public void fileResponse(ECartaResponse response, String path) {
		getLotMatriz(response);
		
		FileWriter file;
		
		SimpleDateFormat format = new SimpleDateFormat("ddMMYYYYHHmmss"); 	
		String dateResponse = format.format(new Date());	
		
		String fileText = path + "/e-Carta_" + numMatriz + "_" + numLot + "_Resposta" + dateResponse + ".txt";
			 
		try 
		{
			file = new FileWriter(new File(fileText));
			writer(response, fileText);
			file.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void writer(ECartaResponse response, String fileText) throws IOException {
		BufferedWriter bufferedWrite = new BufferedWriter(new FileWriter(fileText));
		String line = "";
		String responseProduction = response.getConfirm() ? "A" : "R";
		
		line = "1" + "|" + response.getNumLot() + "|" + responseProduction;
		
		bufferedWrite.append(line + "\n");
		bufferedWrite.close();
	}
	
	public String nameZip(String path) {
		SimpleDateFormat format = new SimpleDateFormat("ddMMYYYYHHmmss"); 	
		String dateResponse = format.format(new Date());
		String fileZip = path + "/e-Carta_" + numMatriz + "_" + numLot + "_Resposta" + dateResponse + ".zip";
		
		return fileZip;
	}
	
	public static void getLotMatriz(ECartaResponse response) {
		final int tpNai = 0;
		final int tpNip = 1;
		final int tpNaiCorrecao = 2;
		final int tpNipCorrecao = 3;
		
		HashMap<String, Object> paramns = parameters.getParamns(null);
		numMatriz = (String) paramns.get("MOB_CORREIOS_SG_CLIENTE");
		numLot = response.getNumLot();
		
		LoteImpressao lote = LoteImpressaoDAO.get(Integer.parseInt(numLot));
		
		switch(lote.getTpDocumento())
		{
		case tpNai:
			numMatriz = (String) paramns.get("mob_correios_matriz_nai");
			break;
		case tpNip:
			numMatriz =  (String) paramns.get("mob_correios_matriz_nip");
			break;
		case tpNaiCorrecao:
			numMatriz = (String) paramns.get("mob_correios_matriz_nai");
			break;
		case tpNipCorrecao:
			numMatriz =  (String) paramns.get("mob_correios_matriz_nip");
		}

	}
}
