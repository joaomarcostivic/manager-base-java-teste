package com.tivic.manager.eCarta;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoDAO;

public class TextService {
	private static String numMatriz;
	private static String numLot;
	static GetParamns parameters = new GetParamns();
	
	public void arquivoServico(List<ECartaItem> itens, ConfigTextService configText, String path) {
		getLotMatriz(itens);
		
		FileWriter arquivo;
		String pathFile = path + "/e-Carta_" + numMatriz + "_" + numLot + "_servico.txt";
			 
		try 
		{
			arquivo = new FileWriter(new File(pathFile));
			escritor(itens, configText, path, pathFile);
			arquivo.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public String nameZip(String path) {
		String fileZip = path + "/e-Carta_" + numMatriz + "_" + numLot + "_servico.zip";
		
		return fileZip;
	}
	
	@SuppressWarnings("unused")
	public static void escritor(List<ECartaItem> itens, ConfigTextService configText, String path, String pathFile) throws IOException {
		Connection connect = Conexao.conectar();
		
		BufferedWriter bufferedWrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathFile),"UTF-8"));
		String linha = "";
		int interador = 0;
		
		HashMap<String, Object> paramns = parameters.getParamns(connect);
		
		String contrato = (String) paramns.get("MOB_CORREIOS_NR_CONTRATO");
		String numContrato = contrato.replaceAll("[\\D]", "");
		String tpRequisto = "1";
		String numeroLote = "";
		String cartaoPostagem = (String) paramns.get("MOB_CORREIOS_NR_CARTAO_POSTAGEM");
		String numeroContrato = numContrato;
		String servicoAdicional = configText.getServicoAdicional();
		String arquivoSpool = configText.getArquivoSpool();
		String nomeSpool = configText.getNomeSpool();
		String arquivoComplementar = "S";
		String nomeDestinatario = "";
		String enderecoDestinatario = "";
		String numeroEtiqueta = "";
		String codObjeto = "";
		//String numeroLogradouro = "0000000"; //Omitida pois o preechimento não e obrigatorio e segundo o tecnico a montagem de endereço já vai esse dado
		
		for (ECartaItem item : itens) 
		{
			
			interador++;
			numeroLote = item.getNumeroLote();
			nomeDestinatario = item.getNomeDestinatario();
			numeroEtiqueta = item.getNumeroEtiqueta();
			codObjeto = item.getCdArquivo();
			
			GeneratePDF.generatePdf(item, codObjeto, path, connect);
			
			enderecoDestinatario = 
					item.getNomeLogradouro() + "|"+
					item.getNumEndereco() + "|" +
					item.getComplementEndereco() + "|" +
					item.getNmBairro() + "|" +
					item.getNmMunicipio() + "|" +
					item.getSgEstado() + "|" +
					item.getCep() + "|";	
						
			String nomeArquivo = "e-Carta_" + numMatriz + "_" + numLot + "_1_" + codObjeto + "_complementar.pdf";
			linha = 
				tpRequisto + "|" + 
				codObjeto + "|" + 
				numeroLote + "|" + 
				cartaoPostagem + "|" + 
				numeroContrato + "|" +
				servicoAdicional + "|" + 
				arquivoSpool + "|" + 
				nomeSpool + "|" + 
				arquivoComplementar + "|" + 
				nomeArquivo + "|" + 
				nomeDestinatario + "|" + 
				enderecoDestinatario;
				//numeroEtiqueta + "|"; <- Comentando pois esta indo vazio pois não usa por enquanto e o tecnico dos correios falou pra remover
				
				bufferedWrite.append(linha + "\n");
				
		}
		
		bufferedWrite.close();	
		
		Conexao.desconectar(connect);

	}

	public static void getLotMatriz (List<ECartaItem> itens) 
	{
		
		ECartaItem item  = itens.get(0);
		numLot = item.getNumeroLote();
		LoteImpressao lote = LoteImpressaoDAO.get(Integer.parseInt(numLot));
		numMatriz = searchMatriz (lote);
	}
	
	public static void cancellationFile (String nLote, String path)
	{	
		numLot = nLote;
		LoteImpressao lote = LoteImpressaoDAO.get(Integer.parseInt(numLot));
		numMatriz = searchMatriz (lote);
		
		FileWriter file;
		String fileText = path + "/e-Carta_" + numMatriz + "_" + numLot + "_Cancelamento" + ".txt";
		
		try 
		{
			file = new FileWriter(new File(fileText));
			writerCancellation(numMatriz, numLot, fileText);
			file.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private static void writerCancellation (String numMatriz, String numLot, String fileText) throws IOException 
	{
		BufferedWriter bufferedWrite = new BufferedWriter(new FileWriter(fileText));
		String line = "";
		
		line = numMatriz + ";" + numLot + ";";
		
		bufferedWrite.append(line + "\n");
		bufferedWrite.close();
	}
	
	public static String nameZipCancellation (String path) 
	{
		String fileZip = path + "/e-Carta_" + numMatriz + "_" + numLot + "_Cancelamento.zip";	
		return fileZip;
	}
	
	public static String searchMatriz (LoteImpressao lote)
	{
		final int tpNai = 0;
		final int tpNip = 1;
		final int tpNaiCorrecao = 2;
		final int tpNipCorrecao = 3;
		String matriz = null;
		
		HashMap<String, Object> paramns = parameters.getParamns(null);
		
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
				break;
		}
	
		matriz = numMatriz;
		return matriz;
	}
}
