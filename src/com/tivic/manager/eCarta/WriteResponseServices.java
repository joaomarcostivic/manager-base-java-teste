package com.tivic.manager.eCarta;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.grl.ArquivoServices;
import com.tivic.manager.grl.Parametro;
import com.tivic.manager.grl.ParametroDAO;
import com.tivic.manager.grl.ParametroValor;
import com.tivic.manager.grl.ParametroValorDAO;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDAO;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.AitReportGetParamns;
import com.tivic.manager.mob.ArquivoMovimentoDAO;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.grafica.LoteImpressaoAitDAO;
import com.tivic.manager.mob.grafica.LoteImpressaoAitServices;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.grafica.LoteImpressaoDAO;
import com.tivic.manager.mob.grafica.LoteImpressaoServices;
import com.tivic.manager.mob.grafica.LoteImpressaoSituacao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

public class WriteResponseServices {
	
	public static Result WriteResponseTransit(ECartaResponse response) throws IOException {
		return  WriteResponseTransit(response, null); 
	}
	
	@SuppressWarnings("static-access")
	public static Result WriteResponseTransit(ECartaResponse response, Connection connect) throws IOException {
		//Conectar ao banco de dados
		boolean isConnectionNull = connect==null;
		if (isConnectionNull) 
			connect = Conexao.conectar();
		
		String path = ManagerConf.getInstance().get("TOMCAT_WORK_DIR", "/tivic//E-carta") + "\\E-Carta-RN";	
		File fileResponse = new File(path);	
		ManipulateFolder manipulateFolder = new ManipulateFolder();
		File pathResponse = new File(path);
		
		//Pegar o lote a ser alterado
		int numLot = Integer.parseInt(response.getNumLot());
		LoteImpressao lote = LoteImpressaoDAO.get(numLot, connect);
		
		//Identificar qual foi a descisão do usuario
		if (response.getConfirm()) 
		{
			LoteImpressaoSituacao situacaoLote = new LoteImpressaoSituacao();
			
			if (lote.getStLoteImpressao() == situacaoLote.ECARTAS_LOTE_CONSISTENTE)
			{
				movimentLotConsistent(numLot, connect);
				manipulateFolder.remove(fileResponse);
			}
			
			if (lote.getStLoteImpressao() == situacaoLote.ECARTAS_LOTE_INCONSISTENTE)
			{
				movimentLotInconsistent (numLot, connect);
				recreateLot (numLot, connect);
				manipulateFolder.remove(fileResponse);
			}
			
			if (lote.getStLoteImpressao() == situacaoLote.ECARTAS_LOTE_INVALIDO)
			{
				cancelLot (numLot, lote.getTpDocumento(), connect);
				manipulateFolder.remove(fileResponse);
			}
		}
		else
		{
			cancelLot (numLot, lote.getTpDocumento() ,  connect);
			manipulateFolder.remove (fileResponse);		
		}
		Result r = new Result(-1);
		r.setCode(1);
		r.setMessage("Resposta Enviada!");
		r.addObject("STATUS", "Respota enviada");
		
		if (isConnectionNull)
			Conexao.desconectar(connect);
		
		return r;
	}
	
	public static void cancelLot (int numLot, int tpDocument, Connection connect) 
	{
		movimentArchivesCanceled (numLot, tpDocument, connect);
		updateLotCanceled (numLot, connect);
	}
	
	public static void movimentArchivesCanceled (int numLot, int tpDocument, Connection connect)
	{	
		ResultSetMap rsm = LoteImpressaoAitDAO.find(criteria(numLot, connect));
		LoteImpressaoAit archive;
		List<LoteImpressaoAit> archiveDenied = new ArrayList<LoteImpressaoAit>();
		List<LoteImpressaoAit> archiveAuthorized = new ArrayList<LoteImpressaoAit>();
		while (rsm.next())
		{
			archive = LoteImpressaoAitDAO.get(numLot, rsm.getInt("CD_AIT"));
			
			if (archive.getStImpressao() == LoteImpressaoAitSituacao.ARQUIVO_CONSISTENTE)
			{
				archive.setStImpressao (LoteImpressaoAitSituacao.ECARTAS_ARQUIVO_AUTORIZADO);
				archiveAuthorized.add(archive);
			}
				
			if (archive.getStImpressao() == LoteImpressaoAitSituacao.ARQUIVO_INCONSISTENTE)
			{
				archive.setStImpressao (LoteImpressaoAitSituacao.ECARTAS_ARQUIVO_NEGADO);
				archiveDenied.add (archive);
			}
				
			LoteImpressaoAitServices.save (archive);
		}
		rsm.beforeFirst();
		
		handleAuthorizedArchive (archiveAuthorized, tpDocument, connect);
		handleDeniedArchive (archiveDenied, tpDocument, connect);
	}
	
	@SuppressWarnings("static-access")
	private static void movimentLotConsistent (int numLot, Connection connect)
	{
		LoteImpressao lote = LoteImpressaoDAO.get (numLot, connect);
		LoteImpressaoSituacao situacaoLote = new LoteImpressaoSituacao();
		lote.setStLoteImpressao (situacaoLote.ECARTAS_LOTE_AUTORIZADO);
		LoteImpressaoServices.save (lote, connect);
		movimentArchiveConsistent (numLot, connect);
	}
	
	private static void movimentArchiveConsistent (int numLot, Connection connect)
	{
		ResultSetMap rsm = LoteImpressaoAitDAO.find(criteria(numLot, connect));
		
		LoteImpressaoAit archive;
		while (rsm.next())
		{
			archive = LoteImpressaoAitDAO.get(numLot, rsm.getInt("CD_AIT"));
			archive.setStImpressao(LoteImpressaoAitSituacao.ECARTAS_ARQUIVO_AUTORIZADO);
			LoteImpressaoAitServices.save(archive);
		}
		rsm.beforeFirst();
	}
	
	@SuppressWarnings("static-access")
	private static void movimentLotInconsistent (int numLot, Connection connect)
	{
		LoteImpressao lote = LoteImpressaoDAO.get (numLot, connect);
		LoteImpressaoSituacao situacaoLote = new LoteImpressaoSituacao();
		lote.setStLoteImpressao (situacaoLote.ECARTAS_LOTE_AUTORIZADO);
		LoteImpressaoServices.save (lote, connect);
		movimentArchivesInconsistent (numLot, lote.getTpDocumento(), connect);
	}
	
	private static void movimentArchivesInconsistent (int numLot, int tpDocument, Connection connect)
	{
		ResultSetMap rsm = LoteImpressaoAitDAO.find(criteria(numLot, connect), connect);
		LoteImpressaoAit archive;
		List<LoteImpressaoAit> archiveDenied = new ArrayList<LoteImpressaoAit>();
		while (rsm.next())
		{
			archive = LoteImpressaoAitDAO.get(numLot, rsm.getInt("CD_AIT"), connect);
			
			if (archive.getStImpressao() == LoteImpressaoAitSituacao.ARQUIVO_CONSISTENTE)
				archive.setStImpressao (LoteImpressaoAitSituacao.ECARTAS_ARQUIVO_AUTORIZADO);
			
			if (archive.getStImpressao() == LoteImpressaoAitSituacao.ARQUIVO_INCONSISTENTE)
			{
				archive.setStImpressao (LoteImpressaoAitSituacao.ECARTAS_ARQUIVO_NEGADO);
				archiveDenied.add (archive);
			}
				
			LoteImpressaoAitServices.save (archive);
		}
		rsm.beforeFirst();
	}
	
	private static void handleDeniedArchive (List<LoteImpressaoAit> archiveDenied, int tpDocument, Connection connect)
	{	
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");	
		ResultSetMap rsmMoviment = new ResultSetMap();
		
		for (LoteImpressaoAit archive: archiveDenied)
		{
			if (lgBaseAntiga)
				rsmMoviment = com.tivic.manager.str.AitMovimentoDAO.find(criteriaMoviment(archive, tpDocument, connect), connect);
			else
				rsmMoviment = AitMovimentoDAO.find(criteriaMoviment(archive, tpDocument, connect), connect);
			
			updateDeniedArchive (archive, rsmMoviment, connect);
		}
	}
	
	private static void handleAuthorizedArchive (List<LoteImpressaoAit> archiveAuthorized, int tpDocument, Connection connect)
	{	
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");	
		ResultSetMap rsmMoviment = new ResultSetMap();
		
		for (LoteImpressaoAit archive: archiveAuthorized)
		{
			if (lgBaseAntiga)
				rsmMoviment = com.tivic.manager.str.AitMovimentoDAO.find(criteriaMoviment(archive, tpDocument, connect), connect);
			else
				rsmMoviment = AitMovimentoDAO.find(criteriaMoviment(archive, tpDocument, connect), connect);
			
			updateAuthorizedArchive (archive, rsmMoviment, connect);
		}
	}
	
	private static void updateAuthorizedArchive (LoteImpressaoAit archive, ResultSetMap rsmMoviment, Connection connect)
	{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");	
		
		if (rsmMoviment.next())
		{
			if (lgBaseAntiga)
			{
				com.tivic.manager.str.AitMovimento aitMovimento = com.tivic.manager.str.AitMovimentoDAO.get (rsmMoviment.getInt ("CD_AIT"), rsmMoviment.getInt ("NR_MOVIMENTO"), connect);
				updateAitMovimentCanceledOld (aitMovimento, connect);
			}
			else
			{
				AitMovimento aitMovimento = AitMovimentoDAO.get (rsmMoviment.getInt ("CD_MOVIMENTO"), rsmMoviment.getInt ("CD_AIT"), connect);
				updateAitMovimentCanceled (aitMovimento, connect);
			}
			LoteImpressaoAitServices.remove (archive.getCdLoteImpressao(), archive.getCdAit(), false, null, connect);
			ArquivoServices.remove (archive.getCdArquivo(), true, connect);
		}
	}
	
	private static void updateDeniedArchive (LoteImpressaoAit archive, ResultSetMap rsmMoviment, Connection connect)
	{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");	
		
		if (rsmMoviment.next())
		{	
			if (lgBaseAntiga)
			{
				com.tivic.manager.str.AitMovimento aitMovimento = com.tivic.manager.str.AitMovimentoDAO.get (rsmMoviment.getInt ("CD_AIT"), rsmMoviment.getInt ("NR_MOVIMENTO"), connect);
				updateAitMovimentOld (aitMovimento, connect);
			}
			else
			{
				AitMovimento aitMovimento = AitMovimentoDAO.get (rsmMoviment.getInt ("CD_MOVIMENTO"), rsmMoviment.getInt ("CD_AIT"), connect);
				updateAitMoviment (aitMovimento, connect);
			}
			
			LoteImpressaoAitServices.remove (archive.getCdLoteImpressao(), archive.getCdAit(), false, null, connect);
			ArquivoServices.remove (archive.getCdArquivo(), true, connect);
		}
	}
	
	private static void updateAitMoviment (AitMovimento aitMovimento, Connection connect)
	{
		String nameParam = "";
	
		switch (aitMovimento.getTpStatus())
		{
			case AitMovimentoServices.NAI_ENVIADO:
				nameParam = "OCORRENCIA_LOTE_NAI";
				break;
			case AitMovimentoServices.NIP_ENVIADA:
				nameParam = "OCORRENCIA_LOTE_NIP";
				break;
		}

		ResultSetMap cdOccurrence = ParametroDAO.find (criteriaParam(nameParam,  connect));
		if (cdOccurrence.next())
		{
			ParametroValor cdOccurrenceValue = ParametroValorDAO.get (cdOccurrence.getInt("CD_PARAMETRO"), 1, connect);
			aitMovimento.setCdOcorrencia (Integer.parseInt(cdOccurrenceValue.getVlInicial()));
			AitMovimentoServices.save (aitMovimento, null, connect);
		}
	}
	
	
	private static void updateAitMovimentOld (com.tivic.manager.str.AitMovimento aitMovimento, Connection connect)
	{
		String nameParam = "";
	
		switch (aitMovimento.getTpStatus())
		{
			case AitMovimentoServices.NAI_ENVIADO:
				nameParam = "OCORRENCIA_LOTE_NAI";
				break;
			case AitMovimentoServices.NIP_ENVIADA:
				nameParam = "OCORRENCIA_LOTE_NIP";
				break;
		}

		int cdOccurrence =  searchOccurrenceParameterOld (nameParam, connect);
		
		if (cdOccurrence > 0)
		{
			aitMovimento.setCodOcorrencia (cdOccurrence);
			com.tivic.manager.str.AitMovimentoServices.save (aitMovimento, null, connect);
		}
	}
	
	private static int searchOccurrenceParameterOld (String name, Connection connect)
	{
		int cdOcorrencia = 0;
		
		PreparedStatement pstmt;
		try 
		{
			String sql = " SELECT " + name + " FROM parametro ";
			pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if (rsm.next())
				cdOcorrencia = rsm.getInt(name);
			
			return cdOcorrencia;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitImagemServices.getFromAit: " + e);
			return 0;
		}
		
		
	}
	
	private static void updateAitMovimentCanceledOld (com.tivic.manager.str.AitMovimento aitMovimento, Connection connect)
	{
		String nameParam = "OCORRENCIA_LOTE_CANCELADO";

		int cdOccurrence = searchOccurrenceParameterOld (nameParam, connect);
		if (cdOccurrence > 0)
		{
			aitMovimento.setCodOcorrencia (cdOccurrence);
			com.tivic.manager.str.AitMovimentoServices.save (aitMovimento, null, connect);
		}
	}
	
	private static void updateAitMovimentCanceled (AitMovimento aitMovimento, Connection connect)
	{
		String nameParam = "OCORRENCIA_LOTE_CANCELADO";

		ResultSetMap cdOccurrence = ParametroDAO.find (criteriaParam(nameParam,  connect));
		if (cdOccurrence.next())
		{
			ParametroValor cdOccurrenceValue = ParametroValorDAO.get (cdOccurrence.getInt("CD_PARAMETRO"), 1, connect);
			aitMovimento.setCdOcorrencia (Integer.parseInt(cdOccurrenceValue.getVlInicial()));
			AitMovimentoServices.save (aitMovimento, null, connect);
		}
	}
	
	private static void recreateLot (int numLot, Connection connect)
	{
		ResultSetMap rsmAitLot = LoteImpressaoAitDAO.find (criteria(numLot, connect));
		Arquivo archivePdf;
		List<Arquivo> archivesPdfs = new ArrayList<Arquivo>();
		ByteArrayOutputStream blbConcat = new ByteArrayOutputStream();
		
		while (rsmAitLot.next())
		{
			archivePdf = ArquivoDAO.get (rsmAitLot.getInt("CD_ARQUIVO"), connect);
			archivesPdfs.add (archivePdf);
			blbConcat = concatArchiveBlb (archivesPdfs, connect);
		}
		
		LoteImpressao lot = LoteImpressaoDAO.get (numLot, connect);
		Arquivo archeveLot = ArquivoDAO.get (lot.getCdArquivo(), connect);
		archeveLot.setBlbArquivo (blbConcat.toByteArray());
		ArquivoServices.update (archeveLot, connect);

	}
	
	private static ByteArrayOutputStream concatArchiveBlb(List<Arquivo> archivesPdfs, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		if (isConnectionNull) 
			connect = Conexao.conectar();
		
		try
		{
			Document document = new Document();
			ByteArrayOutputStream baout = new ByteArrayOutputStream();
			PdfCopy copy = new PdfSmartCopy (document, baout);
			document.open();
			PdfReader reader = null;
			
			byte[] content = new byte[] {};
			
			for (Arquivo archivePdf: archivesPdfs)
			{
				content = archivePdf.getBlbArquivo();
				reader = new PdfReader (content);
				copy.addDocument (reader);
				reader.close();
			}
			
			reader.close();
			document.close();
			baout.close();
			
			return baout;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
		finally 
		{
			if (isConnectionNull)
				Conexao.desconectar (connect);
		}
	}
	
	private static ArrayList<ItemComparator> criteria (int numLot, Connection connect)
	{
		ArrayList<ItemComparator> criteria = new ArrayList<ItemComparator>();
		criteria.add(new ItemComparator ("cd_lote_impressao", String.valueOf (numLot), ItemComparator.EQUAL, Types.INTEGER));
		
		return criteria;
	}
	
	
	//*****HERE >>>> Preciso pensar uma forma de trazer para ca o tipo do documento se e nai ou nip
	private static ArrayList<ItemComparator> criteriaMoviment (LoteImpressaoAit archive, int tpDocument, Connection connect)
	{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		int document = 0;
		
		switch (document)
		{
			case 0:
				document = EcartaTipoDocumento.NAI;
				break;
			case 1:
				document = EcartaTipoDocumento.NIP;
				break;
			case 2:
				document = EcartaTipoDocumento.NAI;
				break;
			case 3: 
				document = EcartaTipoDocumento.NIP;
				break;
		}
		
		ArrayList<ItemComparator> criteriaMoviment = new ArrayList<ItemComparator>();
		
		if (lgBaseAntiga)
			criteriaMoviment.add (new ItemComparator ("codigo_ait", String.valueOf (archive.getCdAit()), ItemComparator.EQUAL, Types.INTEGER));
		else
			criteriaMoviment.add (new ItemComparator ("cd_ait", String.valueOf (archive.getCdAit()), ItemComparator.EQUAL, Types.INTEGER));
		
		criteriaMoviment.add (new ItemComparator ("tp_status", String.valueOf (document), ItemComparator.EQUAL, Types.INTEGER));
		
		return criteriaMoviment;
	}
	
	private static ArrayList<ItemComparator> criteriaParam (String nameParam, Connection connect)
	{
		ArrayList<ItemComparator> criteriaMoviment = new ArrayList<ItemComparator>();
		criteriaMoviment.add (new ItemComparator ("nm_parametro ", nameParam, ItemComparator.EQUAL, Types.VARCHAR));
		
		return criteriaMoviment;
	}
	
	private static void updateLotCanceled (int numLot, Connection connect)
	{
		LoteImpressao loteImpressao = LoteImpressaoDAO.get(numLot, connect);
		loteImpressao.setStLoteImpressao(LoteImpressaoSituacao.ECARTAS_LOTE_REJEITADO);
		LoteImpressaoServices.save(loteImpressao);
		
	}
	
}
