package com.tivic.manager.mob.grafica;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.eCarta.ECartaItem;
import com.tivic.manager.eCarta.ECartaServices;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.grl.ArquivoServices;
import com.tivic.manager.grl.ParametroDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ParametroValor;
import com.tivic.manager.grl.ParametroValorDAO;
import com.tivic.manager.grl.parametro.repository.ParametroRepositoryDAOFactory;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitDAO;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDAO;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.AitReportGetParamns;
import com.tivic.manager.mob.AitReportServices;
import com.tivic.manager.mob.AitReportValidatorsNAI;
import com.tivic.manager.mob.AitReportValidatorsNIP;
import com.tivic.manager.mob.AitServices;
import com.tivic.manager.mob.ArquivoMovimentoDAO;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.EstrategiaVerificarPrazoDefesa;
import com.tivic.manager.mob.IVerificarPrazoDefesa;
import com.tivic.manager.mob.InfracaoServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.correios.CorreiosEtiquetaRepositoryDAO;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoAitDTO;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoAitDTOListBuilder;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.Conexao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class LoteImpressaoServices {
	static AitReportValidatorsNAI validatorsNai = new AitReportValidatorsNAI();
	static AitReportGetParamns getParamnsNai = new AitReportGetParamns();
	static AitReportValidatorsNIP validatorsNip = new AitReportValidatorsNIP();
	static AitReportGetParamns getParamnsNip = new AitReportGetParamns();
	
	private class LoteImpressaoTipoDocumento {
		   private static final short LOTE_NAI = 0,
									  LOTE_NIP = 1,
									  LOTE_NAI_CORRECAO = 2,
									  LOTE_NIP_CORRECAO = 3;
		}

	public static Result save(LoteImpressao loteImpressao){
		return save(loteImpressao, null);
	}

	public static Result save(LoteImpressao loteImpressao, Connection connect){
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		boolean isConnectionNull = connect==null;
		GregorianCalendar dtPublicaco = loteImpressao.getDtAtualizacao();
		loteImpressao.setDtAtualizacao(null);
		try 
		{
			if (isConnectionNull) 
			{
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(loteImpressao==null)
				return new Result(-1, "Erro ao salvar. LoteImpressao é nulo");

			int retorno;
			if (loteImpressao.getCdLoteImpressao()==0)
			{
				
				loteImpressao.setDtCriacao(new GregorianCalendar());
				if(loteImpressao.getStLoteImpressao() != LoteImpressaoSituacao.ECARTAS_LOTE_COM_ERRO) {
					loteImpressao.setStLoteImpressao(LoteImpressaoSituacao.AGUARDANDO_MOVIMENTOS);
				}
				if(loteImpressao.getStLoteImpressao() == LoteImpressaoSituacao.ECARTAS_LOTE_COM_ERRO) {
					loteImpressao.setStLoteImpressao(LoteImpressaoSituacao.AGUARDANDO_ENVIO);
				}
				retorno = LoteImpressaoDAO.insert(loteImpressao, connect);
				loteImpressao.setCdLoteImpressao(retorno);
			}
			else 
			{
				retorno = LoteImpressaoDAO.update(loteImpressao, connect);
			}
			
			boolean isCorrect = false;
			ResultSetMap ait = new ResultSetMap();
			ParametroValor cdOccurrenceValue = new ParametroValor();
			int cdOccurrenceBaseAntiga = 0;
			
			if (lgBaseAntiga)
			{
				String sql =  "SELECT OCORRENCIA_ERRO_DETRAN FROM parametro";
				PreparedStatement ps = connect.prepareStatement(sql);	
				ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
				
				if (rsm.next())
					cdOccurrenceBaseAntiga = rsm.getInt("OCORRENCIA_ERRO_DETRAN");
				
			}
			else
			{
				ResultSetMap cdOccurrence = ParametroDAO.find (criteriaParamOcorrencia());
				
				if (cdOccurrence.next())
					cdOccurrenceValue = ParametroValorDAO.get (cdOccurrence.getInt("CD_PARAMETRO"), 1, connect);
				
			}

			if (loteImpressao.getAits()!=null && loteImpressao.getAits().size()>0) 
			{
				final int tpNai = 2;
				final int tpNip = 3;
				
				for (LoteImpressaoAit loteImpressaoAit : loteImpressao.getAits()) {
					loteImpressaoAit.setCdLoteImpressao(loteImpressao.getCdLoteImpressao());
					loteImpressaoAit.setDtInclusao(new GregorianCalendar());
					loteImpressaoAit.setIdLoteImpressaoAit(Util.generateRandomString(5));
					loteImpressaoAit.setStImpressao(LoteImpressaoAitSituacao.AGUARDANDO_GERACAO);
					loteImpressaoAit.setDtEnvio(dtPublicaco);
					
					if (lgBaseAntiga)
					{
						switch (loteImpressao.getTpDocumento())
						{
							case tpNai:
								ait = com.tivic.manager.str.AitMovimentoDAO.find(getCriteriosMovimentosNAICorrecoes(loteImpressaoAit.getCdAit()));
								break;
							case tpNip:
								ait = com.tivic.manager.str.AitMovimentoDAO.find(getCriteriosMovimentosNIPCorrecoes(loteImpressaoAit.getCdAit()));
								break;
						}
					}
					else
					{
						switch (loteImpressao.getTpDocumento())
						{
							case tpNai:
								ait = AitMovimentoDAO.find(getCriteriosMovimentosNAICorrecoes(loteImpressaoAit.getCdAit()));
								break;
							case tpNip:
								ait = AitMovimentoDAO.find(getCriteriosMovimentosNIPCorrecoes(loteImpressaoAit.getCdAit()));
								break;
						}
					}

					if (lgBaseAntiga)
					{
						if (ait.next() && ait.getInt("COD_OCORRENCIA") == cdOccurrenceBaseAntiga)
						{
							updateLoteCorrecao (loteImpressao, ait, connect);
							isCorrect = true;
						}
					}
					else
					{
						if (ait.next() && ait.getInt("CD_OCORRENCIA") == Integer.parseInt(cdOccurrenceValue.getVlInicial()))
						{
							updateLoteCorrecao (loteImpressao, ait, connect);
							isCorrect = true;
						}
					}
				}
			
				retorno = vincularAits(loteImpressao.getAits(), connect).getCode();				
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			if (isCorrect)
			{
				loteImpressao.setStLoteImpressao(LoteImpressaoSituacao.AGUARDANDO_DOCUMENTOS);
				LoteImpressaoServices.save(loteImpressao);
			}

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "LOTEIMPRESSAO", loteImpressao);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static ArrayList<ItemComparator> criteriaParamOcorrencia ()
	{
		ArrayList<ItemComparator> criteriaParamOcorrencia = new ArrayList<ItemComparator>();
		criteriaParamOcorrencia.add (new ItemComparator ("nm_parametro ", "OCORRENCIA_ERRO_DETRAN", ItemComparator.EQUAL, Types.VARCHAR));
		
		return criteriaParamOcorrencia;
	}
	
	private static int buscarocorrenciaParametroOld (Connection connect)
	{
		int cdOcorrencia = 0;
		
		PreparedStatement pstmt;
		try 
		{
			String sql = " SELECT OCORRENCIA_ERRO_DETRAN FROM parametro ";
			pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if (rsm.next())
				cdOcorrencia = rsm.getInt("OCORRENCIA_ERRO_DETRAN");
			
			return cdOcorrencia;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitImagemServices.getFromAit: " + e);
			return 0;
		}
		
		
	}
		
	public static Result remove(LoteImpressao loteImpressao) {
		return remove(loteImpressao.getCdLoteImpressao());
	}
	public static Result remove(int cdLoteImpressao){
		return remove(cdLoteImpressao, false, null, null);
	}
	public static Result remove(int cdLoteImpressao, boolean cascade){
		return remove(cdLoteImpressao, cascade, null, null);
	}
	public static Result remove(int cdLoteImpressao, boolean cascade, AuthData authData){
		return remove(cdLoteImpressao, cascade, authData, null);
	}
	public static Result remove(int cdLoteImpressao, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = LoteImpressaoDAO.delete(cdLoteImpressao, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_lote_impressao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllAits(int cdLoteImpressao) {
		return getAllAits(cdLoteImpressao, null);
	}
	
	public static ResultSetMap getAllAits(int cdLoteImpressao, Connection connect) {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, C.* FROM mob_lote_impressao_ait A "+
											 "  JOIN mob_lote_impressao B ON (A.cd_lote_impressao = B.cd_lote_impressao) "+ 
											 "  JOIN mob_ait C ON (A.cd_ait = C.cd_ait) " +
											 " WHERE A.cd_lote_impressao = ?");
			if (lgBaseAntiga)
			{
				pstmt = connect.prepareStatement("SELECT A.*, C.*, C.codigo_ait as cd_ait FROM mob_lote_impressao_ait A "+
						 "  JOIN mob_lote_impressao B ON (A.cd_lote_impressao = B.cd_lote_impressao) "+ 
						 "  JOIN ait C ON (A.cd_ait = C.codigo_ait) " +
						 " WHERE A.cd_lote_impressao = ?");
			}
			
			pstmt.setInt(1, cdLoteImpressao);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoServices.getAllAits: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoServices.getAllAits: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result vincularAits(LoteImpressao loteImpressao, List<LoteImpressaoAit> aits) {
		return vincularAits(aits, null);
	}

	public static Result vincularAits(List<LoteImpressaoAit> aits, Connection connect) {
			boolean isConnectionNull = connect==null;
			try {
			
				if (isConnectionNull) {
					connect = Conexao.conectar();
					connect.setAutoCommit(false);
				}

				if(aits==null)
					return new Result(-1, "Erro ao vincular. Lista de aits está nula");

				Result r = new Result(-1);
				for (LoteImpressaoAit loteImpressaoAit : aits) {
					r = LoteImpressaoAitServices.save(loteImpressaoAit, null, connect);
					
					if(r.getCode()<=0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return r;						
					}	
				}
				
				if(r.getCode()<=0)
					Conexao.rollback(connect);
				else if (isConnectionNull)
					connect.commit();
					
				return r;
			}
			catch(Exception e){
				e.printStackTrace();
				if (isConnectionNull)
					Conexao.rollback(connect);
				System.err.println("Erro! LoteImpressaoServices.vincularAits: " + e);
				return new Result(-1, e.getMessage());
			}
			finally {
				if (isConnectionNull)
					Conexao.desconectar(connect);
			}
	}
	
	private static void updateLoteCorrecao (LoteImpressao loteImpressao, ResultSetMap ait, Connection connect) throws SQLException
	{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		
		if (lgBaseAntiga)
		{
			com.tivic.manager.str.AitMovimento aitMovimentoBaseAntiga = com.tivic.manager.str.AitMovimentoDAO.get(ait.getInt("CD_AIT"),  ait.getInt("nr_movimento"), connect);

			aitMovimentoBaseAntiga.setCodOcorrencia(0);
			com.tivic.manager.str.AitMovimentoServices.save(aitMovimentoBaseAntiga);
		}
		else
		{
			AitMovimento aitMovimento = AitMovimentoDAO.get(ait.getInt("CD_MOVIMENTO"), ait.getInt("CD_AIT"), connect);
		
			aitMovimento.setCdOcorrencia(0);
			AitMovimentoServices.save(aitMovimento);
		}
	}
	
	private static ArrayList<ItemComparator> getCriteriosMovimentosNAICorrecoes(int cdAit) 
	{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		if (lgBaseAntiga)
			criterios.add(new ItemComparator("codigo_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		else
			criterios.add(new ItemComparator("cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		
		
		criterios.add(new ItemComparator("tp_status", String.valueOf(AitMovimentoServices.NAI_ENVIADO), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("lg_enviado_detran", String.valueOf(1), ItemComparator.EQUAL, Types.INTEGER));
				
		return criterios;
	}
	
	private static ArrayList<ItemComparator> getCriteriosMovimentosNIPCorrecoes(int cdAit) 
	{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		if (lgBaseAntiga)
			criterios.add(new ItemComparator("codigo_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		else
			criterios.add(new ItemComparator("cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		
		criterios.add(new ItemComparator("tp_status", String.valueOf(AitMovimentoServices.NIP_ENVIADA), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("lg_enviado_detran", String.valueOf(1), ItemComparator.EQUAL, Types.INTEGER));
				
		return criterios;
	}
	
	public static ResultSetMap getAllAitsEmitirNAI() throws ValidacaoException {
		return getAllAitsEmitirNAI (1000);
	}

	public static ResultSetMap getAllAitsEmitirNAI(int qtdLote) throws ValidacaoException {
		return getAllAitsEmitirNAI (qtdLote, null);
	}

	public static ResultSetMap getAllAitsEmitirNAI (int qtdLote, Connection connect) throws ValidacaoException {	
		
		AitReportServices aitReportServices = new AitReportServices();
		int limitLote = ManagerConf.getInstance().getAsInteger ("QT_LOTE", qtdLote);
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
		
		HashMap<String, Object> paramns = getParamnsNai.getParamnsNAI (connect);
		
		validatorsNai.verificarParamns (paramns);
			
		try 
		{
			ResultSetMap rsm = findAitsNai (getCriteriosMovimentosEmitirNAI(), connect);
			
			ResultSetMap rsmAits = new ResultSetMap();
			
			int contNai = 0;
			int cd_movimento = 0;
			int PERIODO_CINQUENTA_DIAS_EMISSAO_AIT = 50;
			String periodoDiasEmissaoAitParam = (String) paramns.get("MOB_PERIODO_DIAS_EMISSAO_AIT");
			int periodoDiasEmissaoAit = (periodoDiasEmissaoAitParam == null || periodoDiasEmissaoAitParam.equals("0"))
					? PERIODO_CINQUENTA_DIAS_EMISSAO_AIT
					: Integer.parseInt(periodoDiasEmissaoAitParam);			
			while (rsm.next())
			{
				if (cd_movimento == 0 || cd_movimento != rsm.getInt ("CD_AIT"))
				{
					cd_movimento = rsm.getInt ("CD_AIT");
					
					try 
					{
						Ait ait = AitDAO.get (rsm.getInt ("CD_AIT"), connect);
						
						if (aitReportServices.verificarPrazoPandemia(ait.getCdAit(), connect))
						{
							rsmAits.addRegister (rsm.getRegister());
							contNai++;	
						}
						else if (periodoEmissao (ait, periodoDiasEmissaoAit))
						{
							rsmAits.addRegister (rsm.getRegister());
							contNai++;
						}

					}
					catch (ValidacaoException e)
					{
						continue;
					}

				}
				
				if (contNai >= limitLote)
					break;
			}
			rsm.beforeFirst();
			return rsmAits;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback (connect);
			return null;
		}
		finally
		{
			if (isConnectionNull)
				Conexao.desconectar (connect);
		}
	}
	
	//***Emissão de NAIs correções
	
	public static ResultSetMap getAllAitsEmitirNAICorrecoes() throws ValidacaoException 
	{
		return getAllAitsEmitirNAICorrecoes (null);
	}

	public static ResultSetMap getAllAitsEmitirNAICorrecoes (Connection connect) throws ValidacaoException 
	{
		int limitLote = ManagerConf.getInstance().getAsInteger ("QT_LOTE", 500);
		
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		HashMap<String, Object> paramns = getParamnsNai.getParamnsNAI (connect);
		
		validatorsNai.verificarParamns (paramns);
		
		try 
		{
			ResultSetMap rsm = findAitsNaiCorrecoes (getCriteriosMovimentosEmitirNAICorrecoes(connect), connect);
			ResultSetMap rsmAits = new ResultSetMap();
			
			int contNai = 0;
			int cd_movimento = 0;
			
			while (rsm.next())
			{
				if (cd_movimento == 0 || cd_movimento != rsm.getInt ("CD_AIT"))
				{
					cd_movimento = rsm.getInt ("CD_AIT");
					
					try 
					{
						rsmAits.addRegister (rsm.getRegister());
						contNai++;
					}
					catch (Exception e)
					{
						continue;
					}

				}
				
				if (contNai >= limitLote)
					break;
			}
			rsm.beforeFirst();
			
			return rsmAits;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback (connect);
			return null;
		}
		finally
		{
			if (isConnectionNull)
				Conexao.desconectar (connect);
		}
	}
	
	//***Emissão de NIPs correções
	
	public static ResultSetMap getAllAitsEmitirNIPCorrecoes() throws ValidacaoException 
	{
		return getAllAitsEmitirNIPCorrecoes (null);
	}

	public static ResultSetMap getAllAitsEmitirNIPCorrecoes (Connection connect) throws ValidacaoException 
	{
		int limitLote = ManagerConf.getInstance().getAsInteger ("QT_LOTE", 500);
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
		
		try 
		{
			ResultSetMap rsm = findAitsNipCorrecoes (getCriteriosMovimentosEmitirNIPCorrecoes(connect), connect);
			ResultSetMap rsmAits = new ResultSetMap();
			
			int contNai = 0;
			int cd_movimento = 0;
			
			while (rsm.next())
			{
				if (cd_movimento == 0 || cd_movimento != rsm.getInt ("CD_AIT"))
				{
					cd_movimento = rsm.getInt ("CD_AIT");
					
					try 
					{
						rsmAits.addRegister (rsm.getRegister());
						contNai++;
					}
					catch (Exception e)
					{
						continue;
					}
				}
				
				if (contNai >= limitLote)
					break;
			}
			rsm.beforeFirst();
			
			return rsmAits;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback (connect);
			return null;
		}
		finally
		{
			if (isConnectionNull)
				Conexao.desconectar (connect);
		}
	}
	
	private static ArrayList<ItemComparator> getCriteriosMovimentosEmitirNAICorrecoes(Connection connect) 
	{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");	
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		if(lgBaseAntiga)
		{
			int cdOccurrenceOld = buscarocorrenciaParametroOld (connect);
			criterios.add(new ItemComparator("K.lg_enviado_detran", String.valueOf(1), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("K.cod_ocorrencia", String.valueOf(cdOccurrenceOld), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("K.tp_status", String.valueOf(AitMovimentoServices.NAI_ENVIADO), ItemComparator.EQUAL, Types.INTEGER));
					
			return criterios;
		}
		else
		{
			ResultSetMap cdOccurrence = ParametroDAO.find (criteriaParamOcorrencia());
			ParametroValor cdOccurrenceValue = new ParametroValor();
		
			if (cdOccurrence.next())
				cdOccurrenceValue = ParametroValorDAO.get (cdOccurrence.getInt("CD_PARAMETRO"), 1, connect);
		
			criterios.add(new ItemComparator("K.lg_enviado_detran", String.valueOf(1), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("K.cd_ocorrencia", String.valueOf(cdOccurrenceValue.getVlInicial()), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("K.tp_status", String.valueOf(AitMovimentoServices.NAI_ENVIADO), ItemComparator.EQUAL, Types.INTEGER));
				
			return criterios;
		}
	}
	
	//Metodo que busca o id da ocirrencia
	
	private static ArrayList<ItemComparator> getCriteriosMovimentosEmitirNIPCorrecoes(Connection connect) 
	{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
	
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		if(lgBaseAntiga)
		{
			int cdOccurrenceOld = buscarocorrenciaParametroOld (connect);
			criterios.add(new ItemComparator("B.lg_enviado_detran", String.valueOf(1), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("B.cod_ocorrencia", String.valueOf(cdOccurrenceOld), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("B.tp_status", String.valueOf(AitMovimentoServices.NIP_ENVIADA), ItemComparator.EQUAL, Types.INTEGER));
					
			return criterios;
		}
		else
		{
			ResultSetMap cdOccurrence = ParametroDAO.find (criteriaParamOcorrencia());
			ParametroValor cdOccurrenceValue = new ParametroValor();
		
			if (cdOccurrence.next())
				cdOccurrenceValue = ParametroValorDAO.get (cdOccurrence.getInt("CD_PARAMETRO"), 1, connect);
		
			criterios.add(new ItemComparator("B.lg_enviado_detran", String.valueOf(1), ItemComparator.EQUAL, Types.INTEGER));	
			criterios.add(new ItemComparator("B.cd_ocorrencia", String.valueOf(cdOccurrenceValue.getVlInicial()), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("B.tp_status", String.valueOf(AitMovimentoServices.NIP_ENVIADA), ItemComparator.EQUAL, Types.INTEGER));
				
			return criterios;
		}
	}
	
	public static ResultSetMap findAitsNaiCorrecoes (ArrayList<ItemComparator> criterios, Connection connect) 
	{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		String sql = "";
		
		if (lgBaseAntiga)
		{
			sql = "SELECT "
					+ " A.*, A.CODIGO_AIT as CD_AIT, A.COD_AGENTE as CD_AGENTE, A.cd_renavan as nr_renavan, "
					+ " B.nr_cod_detran, B.DS_INFRACAO, B.DS_INFRACAO2, B.nr_artigo, B.nr_inciso, "
					+ "		B.nr_paragrafo, B.nr_alinea, B.nm_natureza, B.nr_pontuacao, "
					+ " C.nm_municipio, C.nm_uf, "
					+ " D.nm_bairro, "
					+ " E.nm_categoria, "
					+ " F.nm_cor, "
					+ " G.ds_especie, "
					+ " H.nm_marca, H.nm_modelo,"
					+ " I.nm_equipamento, I.id_equipamento, I.nm_marca AS marca_equipamento, "
					+ " I.nm_modelo AS modelo_equipamento, I.tp_equipamento, " 
					+ " J.nm_agente, J.nr_matricula, "
					+ " K.dt_movimento, K.tp_status, K.nr_processo, "
					+ " M.dt_afericao, M.id_identificacao_inmetro "
					+ " "
					+ " FROM ait A "
					+ " LEFT OUTER JOIN infracao 		 	B ON (A.cod_infracao = B.cod_infracao) "
					+ " LEFT OUTER JOIN municipio 		 	C ON (A.cod_municipio = C.cod_municipio) "
					+ " LEFT OUTER JOIN bairro 			 	D ON (A.cod_bairro = D.cod_bairro) "
					+ " LEFT OUTER JOIN categoria_veiculo	E ON (A.cod_categoria = E.cod_categoria) "
					+ " LEFT OUTER JOIN cor 				F ON (A.cod_cor = F.cod_cor) "
					+ " LEFT OUTER JOIN especie_veiculo 	G ON (A.cod_especie = G.cod_especie) "
					+ " LEFT OUTER JOIN marca_modelo 	 	H ON (A.cod_marca = H.cod_marca) "
					+ " LEFT OUTER JOIN grl_equipamento 	I ON (A.cd_equipamento = I.cd_equipamento) "
					+ " LEFT OUTER JOIN agente 			 	J ON (A.cod_agente = J.cod_agente) "
					+ " LEFT OUTER JOIN ait_movimento	K ON (A.codigo_ait = K.codigo_ait)"
					+ " LEFT OUTER JOIN mob_ait_evento      L  ON (A.codigo_ait = L.cd_ait) "
					+ " LEFT OUTER JOIN mob_evento_equipamento M  ON (L.cd_evento = M.cd_evento) ";
			
			return Search.find (sql, " ORDER BY K.CODIGO_AIT ASC, K.dt_movimento DESC" , criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		}
		else
		{
			sql = "SELECT "
				+ " K.dt_movimento, K.tp_status, K.nr_processo, A.*,"
				+ " B.nr_cod_detran, B.ds_infracao, B.nr_artigo, nr_inciso, nr_paragrafo, nr_alinea, nm_natureza, nr_pontuacao, "
				+ " C.nm_cidade, C.nm_cidade AS nm_municipio,"
				+ " 	C1.sg_estado, C1.sg_estado AS nm_uf, C1.nm_estado,"
				+ " E.nm_categoria,"
				+ " F.nm_cor,"
				+ " G.ds_especie,"
				+ " H.nm_marca, H.nm_modelo,"
				+ " I.nm_equipamento, I.id_equipamento, I.nm_marca AS marca_equipamento, I.nm_modelo AS modelo_equipamento, I.tp_equipamento, " 
				+ " J.nm_agente, J.nr_matricula, "
				+ " M.dt_afericao, M.id_identificacao_inmetro, "
				+ " N.ds_logradouro, "
				+ " P.nm_bairro "
				+ " "
				+ " FROM 			mob_ait 			   A"
				+ " LEFT OUTER JOIN mob_infracao 		   B  ON (A.cd_infracao = B.cd_infracao)"
				+ " LEFT OUTER JOIN grl_cidade 			   C  ON (A.cd_cidade = C.cd_cidade)"
				+ " LEFT OUTER JOIN grl_estado 			   C1 ON (C.cd_estado = C1.cd_estado)"
				+ " LEFT OUTER JOIN fta_categoria_veiculo  E  ON (A.cd_categoria_veiculo = E.cd_categoria)"
				+ " LEFT OUTER JOIN fta_cor 			   F  ON (A.cd_cor_veiculo = F.cd_cor)"
				+ " LEFT OUTER JOIN fta_especie_veiculo    G  ON (A.cd_especie_veiculo = G.cd_especie)"
				+ " LEFT OUTER JOIN fta_marca_modelo	   H  ON (A.cd_marca_veiculo = H.cd_marca)"
				+ " "
				+ " LEFT OUTER JOIN grl_equipamento		   I  ON (A.cd_equipamento = I.cd_equipamento)"
 			    + " LEFT OUTER JOIN mob_agente			   J  ON (A.cd_agente = J.cd_agente)"
 			    + " LEFT OUTER JOIN mob_ait_movimento	   K  ON (A.cd_ait = K.cd_ait AND A.cd_movimento_atual = K.cd_movimento)"
 			    + " "
 			    + " LEFT OUTER JOIN mob_ait_evento         L  ON (A.cd_ait = L.cd_ait) "
 			    + " LEFT OUTER JOIN mob_evento_equipamento M  ON (L.cd_evento = M.cd_evento) "
 			    + " LEFT OUTER JOIN mob_ait_proprietario   N  ON (A.cd_ait = N.cd_ait) "
 			    + " LEFT OUTER JOIN grl_bairro             P  ON (A.cd_bairro = P.cd_bairro) ";
				
			return Search.find (sql, " ORDER BY cd_ait ASC " , criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	
		}
	}
	
	//**********
	
	public static ResultSetMap findAitsNipCorrecoes (ArrayList<ItemComparator> criterios, Connection connect) 
	{	
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		
		if (lgBaseAntiga)
		{
			String sql = " SELECT A.*, A.CODIGO_AIT as CD_AIT, A.COD_AGENTE as CD_AGENTE, A.cd_renavan as nr_renavan, B.*, C.* "
					   + " FROM ait A "
					   + " JOIN ait_movimento B ON (A.codigo_ait = B.codigo_ait) "
					   + " JOIN infracao C ON (A.cod_infracao = C.cod_infracao) ";

			return Search.find (sql, " ORDER BY B.CODIGO_AIT DESC, B.dt_movimento DESC "  , criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		
		}	
		else
		{
			String sql = " SELECT A.*, B.*, C.* "
					   + " FROM mob_ait A "
					   + " JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait) "
					   + " JOIN mob_infracao C ON (A.cd_infracao = C.cd_infracao) ";

			return Search.find (sql, " ORDER BY B.CD_AIT DESC, B.dt_movimento DESC, B.cd_movimento DESC "  , criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		}
	}
	
	public static ResultSetMap getAllAitsEmitirNIP() throws ValidacaoException {
		return getAllAitsEmitirNIP (1000);
	}
	
	public static ResultSetMap getAllAitsEmitirNIP(int qtdLote) throws ValidacaoException {
		return getAllAitsEmitirNIP (qtdLote, null);
	}

	@SuppressWarnings("static-access")
	public static ResultSetMap getAllAitsEmitirNIP ( int qtdLote, Connection connect) throws ValidacaoException {

		int limitLote = ManagerConf.getInstance().getAsInteger("QT_LOTE", qtdLote);
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
		
		HashMap<String, Object> paramns = getParamnsNip.getParamnsNIP (connect);
		validatorsNip.verificarParamns (paramns);
		
		try 
		{
			ResultSetMap rsm = findAitsNip (getCriteriosMovimentosEmitirNIP(), connect);
			ResultSetMap rsmAitMovimento = new ResultSetMap();
			ResultSetMap rsmAits = new ResultSetMap();
			
			int contNip = 0;
			int cd_movimento = 0;
			
			while(rsm.next())
			{	
			
				if(cd_movimento == 0 || cd_movimento != rsm.getInt("CD_AIT"))
				{
					cd_movimento = rsm.getInt("CD_AIT");
					
					try 
					{
						rsmAitMovimento.addRegister(rsm.getRegister());
						verificarDefesas(rsmAitMovimento, connect);
						rsmAits.addRegister(rsm.getRegister());
						rsmAitMovimento = new ResultSetMap();
						contNip++;
					}
					catch (Exception e)
					{
						rsmAitMovimento = new ResultSetMap();
						continue;
					}
				}
				
				if (contNip >= limitLote)
					break;
			}
			
			return rsmAits;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback (connect);
			return null;
		}
		finally
		{
			if (isConnectionNull)
				Conexao.desconectar (connect);
		}
	}
	
	public static ResultSetMap findAitsNai (ArrayList<ItemComparator> criterios, Connection connect) 
	{	
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		String sql = "";
		
		if (lgBaseAntiga)
		{
			sql = "SELECT "
					+ " A.*, A.CODIGO_AIT as CD_AIT, A.COD_AGENTE as CD_AGENTE, A.cd_renavan as nr_renavan, "
					+ " B.dt_afericao, B.id_identificacao_inmetro, "
					+ " C.nm_bairro, "
					+ " D.nm_usuario, D.nm_nick, "
					+ " E.nr_cod_detran, E.DS_INFRACAO, E.DS_INFRACAO2, E.nr_artigo, E.nr_inciso, "
					+ " E.nr_paragrafo, E.nr_alinea, E.nm_natureza, E.nr_pontuacao, "
					+ " F.ds_especie, "
					+ " G.nm_marca, G.nm_modelo, "
					+ " H.nm_agente, H.nr_matricula, "
					+ " I.nm_cor, "
					+ " J.nm_tipo, "
					+ " L.nm_categoria, "
					+ " M.nm_municipio, M.nm_uf, "
					+ " N.ds_erro, "
					+ " O.nm_equipamento, O.id_equipamento, O.nm_marca AS marca_equipamento, O.nm_modelo AS modelo_equipamento, O.tp_equipamento, "
					+ " P.cd_etiqueta, P.nr_etiqueta, P.sg_servico, "
					+ " Q.dt_vencimento AS dt_vencimento_Etiqueta, Q.st_lote AS st_lote_etiquetas, "
					+ " R.dt_movimento, R.tp_status, R.nr_processo "
			        + " FROM ait A "
			        + " LEFT OUTER JOIN mob_evento_equipamento       B ON (B.cd_evento = A.cd_evento_equipamento) "
			        + " LEFT OUTER JOIN bairro                       C ON (C.cod_bairro = A.cod_bairro) "
			        + " LEFT OUTER JOIN usuario                      D ON (D.cod_usuario = A.cod_usuario) "
			        + " LEFT OUTER JOIN infracao                     E ON (E.cod_infracao = A.cod_infracao) "
			        + " LEFT OUTER JOIN especie_veiculo              F ON (F.cod_especie = A.cod_especie) "
			        + " LEFT OUTER JOIN marca_modelo                 G ON (G.cod_marca = A.cod_marca) "
			        + " LEFT OUTER JOIN agente                       H ON (H.cod_agente = A.cod_agente) "
			        + " LEFT OUTER JOIN cor                          I ON (I.cod_cor = A.cod_cor) "
			        + " LEFT OUTER JOIN tipo_veiculo                 J ON (J.cod_tipo = A.cod_tipo) "
			        + " LEFT OUTER JOIN categoria_veiculo            L ON (L.cod_categoria = A.cod_categoria) "
			        + " LEFT OUTER JOIN municipio                    M ON (M.cod_municipio = A.cod_municipio) "
			        + " LEFT OUTER JOIN erro_retorno                 N ON (N.nr_erro = A.nr_erro) "
			        + " LEFT OUTER JOIN grl_equipamento              O ON (O.cd_equipamento = A.cd_equipamento) "
			        + " LEFT OUTER JOIN stt_correios_etiqueta        P ON (P.cd_ait = A.codigo_ait AND P.tp_status = 3) "
			        + " LEFT OUTER JOIN stt_correios_lote            Q ON (Q.cd_lote = P.cd_lote) "
			        + " LEFT OUTER JOIN ait_movimento	             R ON (A.codigo_ait = R.codigo_ait)"
			        + " WHERE 1=1 AND "
			        + " ( "
			        + "     E.tp_competencia = " + InfracaoServices.MULTA_COMPETENCIA_MUNICIPAL
			        + "     AND CAST(A.tp_status AS INTEGER) = " + AitMovimentoServices.REGISTRO_INFRACAO
			        + "     AND A.nr_controle IS NOT NULL "
			        + " ) ";
			return Search.find (sql, " ORDER BY R.CODIGO_AIT DESC, R.dt_movimento DESC" , criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		}
		else
		{
			sql = "SELECT "
				+ " K.dt_movimento, K.tp_status, K.nr_processo, A.*,"
				+ " B.nr_cod_detran, B.ds_infracao, B.nr_artigo, nr_inciso, nr_paragrafo, nr_alinea, nm_natureza, nr_pontuacao, "
				+ " C.nm_cidade, C.nm_cidade AS nm_municipio,"
				+ " 	C1.sg_estado, C1.sg_estado AS nm_uf, C1.nm_estado,"
				+ " E.nm_categoria,"
				+ " F.nm_cor,"
				+ " G.ds_especie,"
				+ " H.nm_marca, H.nm_modelo,"
				+ " I.nm_equipamento, I.id_equipamento, I.nm_marca AS marca_equipamento, I.nm_modelo AS modelo_equipamento, I.tp_equipamento, " 
				+ " J.nm_agente, J.nr_matricula, "
				+ " M.dt_afericao, M.id_identificacao_inmetro, "
				+ " N.ds_logradouro, "
				+ " P.nm_bairro "
				+ " "
				+ " FROM 			mob_ait 			   A"
				+ " LEFT OUTER JOIN mob_infracao 		   B  ON (A.cd_infracao = B.cd_infracao)"
				+ " LEFT OUTER JOIN grl_cidade 			   C  ON (A.cd_cidade = C.cd_cidade)"
				+ " LEFT OUTER JOIN grl_estado 			   C1 ON (C.cd_estado = C1.cd_estado)"
				+ " LEFT OUTER JOIN fta_categoria_veiculo  E  ON (A.cd_categoria_veiculo = E.cd_categoria)"
				+ " LEFT OUTER JOIN fta_cor 			   F  ON (A.cd_cor_veiculo = F.cd_cor)"
				+ " LEFT OUTER JOIN fta_especie_veiculo    G  ON (A.cd_especie_veiculo = G.cd_especie)"
				+ " LEFT OUTER JOIN fta_marca_modelo	   H  ON (A.cd_marca_veiculo = H.cd_marca)"
				+ " "
				+ " LEFT OUTER JOIN grl_equipamento		   I  ON (A.cd_equipamento = I.cd_equipamento)"
 			    + " LEFT OUTER JOIN mob_agente			   J  ON (A.cd_agente = J.cd_agente)"
 			    + " LEFT OUTER JOIN mob_ait_movimento	   K  ON (A.cd_ait = K.cd_ait AND A.cd_movimento_atual = K.cd_movimento)"
 			    + " "
 			    + " LEFT OUTER JOIN mob_ait_evento         L  ON (A.cd_ait = L.cd_ait) "
 			    + " LEFT OUTER JOIN mob_evento_equipamento M  ON (L.cd_evento = M.cd_evento) "
 			    + " LEFT OUTER JOIN mob_ait_proprietario   N  ON (A.cd_ait = N.cd_ait) "
 			    + " LEFT OUTER JOIN grl_bairro             P  ON (A.cd_bairro = P.cd_bairro) "
				+ " WHERE "
				+ " NOT EXISTS "
				+ " ( "
				+ " 	SELECT tp_status FROM mob_ait_movimento K "
				+ "		WHERE "
				+ "			( "
				+ "			tp_status = " + AitMovimentoServices.NIP_ENVIADA
				+ "			OR tp_status = " + AitMovimentoServices.CADASTRO_CANCELADO
				+ "			OR tp_status = " + AitMovimentoServices.CANCELA_REGISTRO_MULTA
				+ "			OR tp_status = " + AitMovimentoServices.CANCELAMENTO_AUTUACAO
				+ " 		OR tp_status = " + AitMovimentoServices.CANCELAMENTO_MULTA
				+ " 		OR tp_status = " + AitMovimentoServices.CANCELAMENTO_PONTUACAO
				+ "			OR tp_status = " + AitMovimentoServices.DEVOLUCAO_PAGAMENTO
				+ "			OR tp_status = " + AitMovimentoServices.NAI_ENVIADO
				+ "			)"
				+ " AND K.cd_ait = A.cd_ait "
				+ " )"
				+ "	AND NOT EXISTS "
				+ " ( "
				+ "		SELECT B.nr_cod_detran FROM mob_infracao B "
				+ " 	WHERE "
				+ "			( "
				+ " 		B.nr_cod_detran = 50002 "
				+ "			OR B.nr_cod_detran = 50020 "
				+ " 		)"
				+ " AND A.cd_infracao = B.cd_infracao "
				+ " )";
			return Search.find (sql, " ORDER BY K.CD_AIT DESC, K.dt_movimento DESC" , criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		
		}
				
		//return Search.find (sql, " ORDER BY K.CODIGO_AIT DESC, K.dt_movimento DESC" , criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	@SuppressWarnings("static-access")
	public static ResultSetMap findAitsNip (ArrayList<ItemComparator> criterios, Connection connect)
	{
		
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		AitReportGetParamns getParamns = new AitReportGetParamns();
		HashMap<String, Object> paramns = getParamns.getParamnsNIP (connect);
		
		int tpPrazoRecurso = 0;
		int PRAZO_PENAL_MINIMO_DEFINIDO_POR_LEI = 30;
		tpPrazoRecurso = Integer.parseInt ((String) paramns.get ("MOB_PRAZOS_NR_DEFESA_PREVIA")) + 15;
		String prazoPenalSemEntregaParam = (String) paramns.get("MOB_NR_PRAZO_PENAL_SEM_ENTREGA");
		int prazoPenalSemEntrega = (prazoPenalSemEntregaParam == null || prazoPenalSemEntregaParam.equals("0"))
				? PRAZO_PENAL_MINIMO_DEFINIDO_POR_LEI
				: Integer.parseInt(prazoPenalSemEntregaParam);
		
		//Este iniciio de vigencia diz respeito a resolução de prazo para emissão de NIP
		String inicioVigenciaPrazo = "2021-04-01";
		int seisMeses = 180;
		int dozeMeses = 360;
		
		LocalDate dataAtual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate dataAnterior = dataAtual.minusDays(prazoPenalSemEntrega);
        String dtPrazoPenalSemEntrega = dataAnterior.format(formatter);
		
		if (lgBaseAntiga)
		{
			String sql = " SELECT A.*, A.CODIGO_AIT as CD_AIT, A.NR_AIT, A.dt_infracao, A.ds_local_infracao, A.nr_placa, A.COD_AGENTE as CD_AGENTE, "
					   + " A.nm_proprietario, A.cd_renavan as nr_renavan, C.nm_bairro, D.nm_usuario, D.nm_nick, E.nr_cod_detran, E.ds_infracao, E.ds_infracao2, E.nr_pontuacao, E.nr_artigo, E.nr_inciso, E.lg_suspensao_cnh, "
					   + " E.nr_paragrafo, E.tp_competencia, E.nr_alinea, E.nm_natureza, F.ds_especie, G.nm_modelo AS nm_marca, H.nm_agente, H.nr_matricula, I.nm_cor, J.nm_tipo, L.nm_categoria, "
					   + " M.nm_municipio, M.nm_uf, N.ds_erro, G.nm_modelo, O.nm_equipamento, O.id_equipamento,  O.nm_marca as nm_marca_equip,  O.nm_modelo as nm_modelo_equip, O.tp_equipamento, "
					   + " P.cd_etiqueta, P.nr_etiqueta, P.sg_servico, Q.dt_vencimento as dt_vencimento_Etiqueta, Q.st_lote as st_lote_etiquetas "
					   + " FROM ait A " 
					   + " LEFT OUTER JOIN bairro  C ON (C.cod_bairro = A.cod_bairro) " 
					   + " LEFT OUTER JOIN usuario D ON (D.cod_usuario   = A.cod_usuario) " 
					   + " LEFT OUTER JOIN infracao E ON (E.cod_infracao  = A.cod_infracao) " 
					   + " LEFT OUTER JOIN especie_veiculo F ON (F.cod_especie = A.cod_especie) " 
					   + " LEFT OUTER JOIN marca_modelo G ON (G.cod_marca = A.cod_marca) " 
					   + " LEFT OUTER JOIN agente H ON (H.cod_agente = A.cod_agente) " 
					   + " LEFT OUTER JOIN cor I ON (I.cod_cor = A.cod_cor) " 
					   + " LEFT OUTER JOIN tipo_veiculo J ON (J.cod_tipo = A.cod_tipo) " 
					   + " LEFT OUTER JOIN categoria_veiculo L ON (L.cod_categoria = A.cod_categoria) " 
					   + " LEFT OUTER JOIN municipio M ON (M.cod_municipio = A.cod_municipio) " 
					   + " LEFT OUTER JOIN erro_retorno N ON (N.nr_erro = A.nr_erro) " 
					   + " LEFT OUTER JOIN grl_equipamento O ON (O.cd_equipamento = A.cd_equipamento) " 
					   + " LEFT OUTER JOIN stt_correios_etiqueta P ON (P.cd_ait = A.codigo_ait AND P.tp_status = 5) "
					   + " LEFT OUTER JOIN stt_correios_lote Q ON (Q.cd_lote = P.cd_lote)"
					   + " WHERE 1=1 AND "
					   + " ( "
					   + " 		    ( A.tp_status = " + TipoStatusEnum.NAI_ENVIADO.getKey() + " AND GREATEST(A.DT_PRAZO_DEFESA,  A.dt_ar_nai + interval " + " '"+ prazoPenalSemEntrega + " days' " + " ) " + " < '" + dtPrazoPenalSemEntrega + "' "
					   + "                AND EXISTS ("
					   + "                            SELECT * FROM ait_movimento W  WHERE A.codigo_ait = W.codigo_ait "
					   + "                                 AND W.tp_status  = " + TipoStatusEnum.NAI_ENVIADO.getKey() + " "
					   + "                                 AND W.lg_enviado_detran = " + TipoStatusEnum.ENVIADO_AO_DETRAN.getKey() + " "
					   + "                            )"
					   + "          )"
					   + "			OR ( A.tp_status = " + TipoStatusEnum.DEFESA_INDEFERIDA.getKey() + " AND NOT EXISTS (SELECT * FROM ait_movimento X WHERE A.codigo_ait = X.codigo_ait AND X.tp_status = " + TipoStatusEnum.NIP_ENVIADA.getKey() + " )) "
					   + "			OR ( A.tp_status = "+ TipoStatusEnum.NIP_ESTORNADA.getKey() + " AND NOT EXISTS (SELECT * FROM ait_movimento X WHERE A.codigo_ait = X.codigo_ait AND X.tp_status = " + TipoStatusEnum.NIP_ENVIADA.getKey() + " )"
					   + "               AND GREATEST(A.DT_PRAZO_DEFESA,  A.dt_ar_nai + interval " + " '"+ prazoPenalSemEntrega + " days' " + " ) " + " < '" + dtPrazoPenalSemEntrega + "' )"
					   + "	    	OR ( A.tp_status = " + TipoStatusEnum.RECURSO_JARI.getKey() + " AND NOT EXISTS ( SELECT * FROM ait_movimento X WHERE A.codigo_ait = X.codigo_ait AND X.tp_status = " + TipoStatusEnum.NIP_ENVIADA.getKey() + " ))"
					   + " ) ";
			return Search.find(sql, " order by A.dt_infracao asc, nr_ait  " , criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		}
		else
		{
			String sql = " SELECT A.cd_ait, A.id_ait, A.dt_infracao, A.ds_local_infracao, A.nr_placa, A.nm_proprietario, A.nr_renavan, B.*, C.*, "
					   + " G.ds_especie,"
					   + " H.nm_marca, H.nm_modelo "
					   + " FROM mob_ait A "
					   + " JOIN mob_ait_movimento B ON (A.cd_ait = 	B.cd_ait) "
					   + " JOIN mob_infracao C ON (A.cd_infracao = 	C.cd_infracao) "
					   + " LEFT OUTER JOIN fta_especie_veiculo    	G  ON (A.cd_especie_veiculo = G.cd_especie)"
					   + " LEFT OUTER JOIN fta_marca_modelo	   		H  ON (A.cd_marca_veiculo = H.cd_marca)"
					   + " WHERE B.tp_status = " + AitMovimentoServices.NAI_ENVIADO
					   + " 		AND NOT EXISTS "
					   + "  	( "
					   + " 		SELECT tp_status FROM mob_ait_movimento B "
					   + "		WHERE "
					   + " 			( "	
					   + "			tp_status =    " + AitMovimentoServices.NIP_ENVIADA
					   + "			OR tp_status = " + AitMovimentoServices.CADASTRO_CANCELADO
					   + "			OR tp_status = " + AitMovimentoServices.CANCELA_REGISTRO_MULTA
					   + "			OR tp_status = " + AitMovimentoServices.CANCELAMENTO_AUTUACAO
					   + "			OR tp_status = " + AitMovimentoServices.CANCELAMENTO_MULTA
					   + "	 		OR tp_status = " + AitMovimentoServices.CANCELAMENTO_PONTUACAO
					   + "			OR tp_status = " + AitMovimentoServices.DEVOLUCAO_PAGAMENTO
					   + " 			) "
					   + "			AND B.cd_ait = A.cd_ait"	
					   + "  	) "
					   + "		AND EXISTS "
					   + "		( "
					   + "			SELECT B.cd_ait, B.dt_movimento "	
					   + " 			FROM mob_ait_movimento B "	
					   + "				WHERE CAST(A.dt_prazo_defesa as date) < CURRENT_DATE "
					   + " 				AND B.cd_ait = A.cd_ait "	
					   + "		)"
					   + "		AND EXISTS "
					   + "		( "
					   + "			SELECT I.cd_ait, I.dt_infracao, J.tp_status, J.dt_movimento FROM mob_ait I "
					   + "				LEFT JOIN mob_ait_movimento J on (J.cd_ait = I.cd_ait) "
					   + "			WHERE "	
					   + "			( "
					   + "				J.tp_status = " + AitMovimentoServices.NAI_ENVIADO
					   + "				AND CASE WHEN I.dt_infracao > ' " + inicioVigenciaPrazo + " '"
					   + "				AND CAST(I.dt_infracao as date) + " + seisMeses + " >= CURRENT_DATE "
					   + "				THEN true "
					   + "				ELSE false END  "
					   + "				OR J.tp_status = " + AitMovimentoServices.DEFESA_INDEFERIDA + " AND I.dt_infracao > '"+ inicioVigenciaPrazo  +"' AND CAST(I.dt_infracao as date) + " + dozeMeses + " >= CURRENT_DATE "				
					   + "				OR J.tp_status = " + AitMovimentoServices.NAI_ENVIADO  + " AND I.dt_infracao < '"+ inicioVigenciaPrazo  +"' "
					   + "			) "
					   + "			AND I.cd_ait = A.cd_ait "
					   + "		)";
			
			return Search.find (sql, " ORDER BY B.CD_AIT DESC, B.dt_movimento DESC, B.cd_movimento DESC "  , criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		}
}
	
	private static ArrayList<ItemComparator> getCriteriosMovimentosEmitirNAI() throws ValidacaoException 
	{
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		
		if (lgBaseAntiga)
		{
			criterios.add(new ItemComparator("A.nr_controle", "", ItemComparator.NOTISNULL, Types.CHAR));
		}
		else
		{
			criterios.add(new ItemComparator("A.st_ait", String.valueOf(AitServices.ST_CONFIRMADO), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("K.tp_status", String.valueOf(AitMovimentoServices.REGISTRO_INFRACAO), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.nr_controle", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("A.nr_controle", "0", ItemComparator.GREATER, Types.CHAR));
			criterios.add(new ItemComparator("A.NM_PROPRIETARIO", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator(("A.NR_CPF_CNPJ_PROPRIETARIO").trim(), "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("A.NR_CPF_CNPJ_PROPRIETARIO", "0", ItemComparator.GREATER, Types.CHAR));
			criterios.add(new ItemComparator("H.NM_MODELO", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("G.DS_ESPECIE", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("A.NR_RENAVAN", "", ItemComparator.NOTISNULL, Types.CHAR));
		}
		return criterios;
	}
	
	private static ArrayList<ItemComparator> getCriteriosMovimentosEmitirNIP() {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA"); 
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		if (lgBaseAntiga){
			return criterios;
		}
		else {
			criterios.add(new ItemComparator("A.st_ait", String.valueOf(AitServices.ST_CONFIRMADO), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.nr_controle", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("A.nr_controle", "0", ItemComparator.GREATER, Types.CHAR));
			criterios.add(new ItemComparator("A.NM_PROPRIETARIO", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator(("A.NR_CPF_CNPJ_PROPRIETARIO").trim(), "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("A.NR_CPF_CNPJ_PROPRIETARIO", "0", ItemComparator.GREATER, Types.CHAR));
			criterios.add(new ItemComparator("H.NM_MODELO", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("G.DS_ESPECIE", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("A.NR_RENAVAN", "", ItemComparator.NOTISNULL, Types.CHAR));
			return criterios;
		}
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		
		String sql = "SELECT A.*, "+ 
				" (select count(*) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao) as total_documentos, "+
				" (select count(*) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao AND B.st_impressao > "+LoteImpressaoAitSituacao.AGUARDANDO_GERACAO+") as total_gerados "+
				" FROM mob_lote_impressao A "+
				" 		WHERE NOT EXISTS " +
				"("
				+ " SELECT st_lote_impressao FROM mob_lote_impressao C "
					+ " WHERE "
					+ "("
						+ " st_lote_impressao = " + LoteImpressaoSituacao.ENVIADO_eCARTAS_AGUARDANDO_RESPOSTA
						+ " OR st_lote_impressao = " + LoteImpressaoSituacao.ECARTAS_LOTE_CONSISTENTE
						+ " OR st_lote_impressao = " + LoteImpressaoSituacao.ECARTAS_LOTE_INCONSISTENTE
						+ " OR st_lote_impressao = " + LoteImpressaoSituacao.ECARTAS_LOTE_INVALIDO
						+ " OR st_lote_impressao = " + LoteImpressaoSituacao.ECARTAS_LOTE_AUTORIZADO
						+ " OR st_lote_impressao = " + LoteImpressaoSituacao.ECARTAS_LOTE_REJEITADO
						+ " OR st_lote_impressao = " + LoteImpressaoSituacao.ECARTAS_SOLICITAR_CANCELAMENTO
						+ " OR st_lote_impressao = " + LoteImpressaoSituacao.ECARTAS_LOTE_COM_ERRO
						+ " OR tp_documento = " + LoteImpressaoTipoDocumento.LOTE_NAI_CORRECAO
						+ " OR tp_documento = " + LoteImpressaoTipoDocumento.LOTE_NIP_CORRECAO
					+ ")"
					+ " AND C.cd_lote_impressao = A.cd_lote_impressao"
				+ ")"
		;
		
		return Search.find(sql, "ORDER BY A.dt_criacao DESC LIMIT 15", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap findLoteErro(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			ResultSetMap list = findLoteErro(searchCriterios, customConnection);
			customConnection.finishConnection();
			return list;
		} finally {
			customConnection.closeConnection();
		} 
		
	}

	public static ResultSetMap findLoteErro(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		com.tivic.sol.search.Search<LoteImpressaoAitDTO> search = new SearchBuilder<LoteImpressaoAitDTO>("mob_lote_impressao A")
				.fields("A.*, (select count(*) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao) as total_documentos, "
						+ "(select count(*) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao AND B.st_impressao > " + LoteImpressaoAitSituacao.AGUARDANDO_GERACAO + ") as total_gerados")
				.searchCriterios(searchCriterios)
				.additionalCriterias("EXISTS (SELECT st_lote_impressao FROM mob_lote_impressao C " +
						"WHERE (tp_documento = " + LoteImpressaoTipoDocumento.LOTE_NAI_CORRECAO + " OR tp_documento = " + LoteImpressaoTipoDocumento.LOTE_NIP_CORRECAO + ")" + 
						"AND C.cd_lote_impressao = A.cd_lote_impressao)" +
						"AND A.st_lote_impressao <> " + LoteImpressaoSituacao.ENVIADO_eCARTAS_AGUARDANDO_RESPOSTA)
				.orderBy(" A.dt_criacao DESC ")
				.customConnection(customConnection)
				.build();
		return search.getRsm();
	}
	
	public static PagedResponse<LoteImpressaoAitDTO> buscarLotesAitsErro(SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		List<LoteImpressaoAitDTO> list = searchLotesAits(searchCriterios);
		if(list.isEmpty()) {
			throw new ValidacaoException ("Não há AIT nesse Lote com esse filtro.");
		} 
		return new LoteImpressaoAitDTOListBuilder(list, list.size()).build();
	}
	
	private static List<LoteImpressaoAitDTO> searchLotesAits(SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		
		com.tivic.sol.search.Search<LoteImpressaoAitDTO> search = new SearchBuilder<LoteImpressaoAitDTO>("mob_lote_impressao_ait A")
				.fields("B.codigo_ait as cd_ait, B.nr_ait as id_ait, A.cd_lote_impressao, B.dt_infracao, B.nr_placa, B.cd_renavan as nr_renavan, "
						+ "B.nm_proprietario, E.dt_criacao, A.id_lote_impressao_ait ")
				.addJoinTable("LEFT OUTER JOIN ait B ON (A.cd_ait = B.codigo_ait)")
				.addJoinTable("LEFT OUTER JOIN mob_lote_impressao E ON (A.cd_lote_impressao = E.cd_lote_impressao)")
        		.searchCriterios(searchCriterios)
				.orderBy(" B.dt_infracao DESC ")
				.count()
				.build();
		return search.getList(LoteImpressaoAitDTO.class);	
	}
	
	public static LoteImpressaoAitDTO buscarLoteErro(SearchCriterios searchCriterios) throws Exception, NoContentException{ 
		List<LoteImpressaoAitDTO> lote = searchLoteErro(searchCriterios);
		if(lote.isEmpty()) {
			throw new NoContentException("Nenhum Lote encontrado.");
		}
		return lote.get(0);
	}
	
	private static List<LoteImpressaoAitDTO> searchLoteErro(SearchCriterios searchCriterios) throws Exception {
		com.tivic.sol.search.Search<LoteImpressaoAitDTO> search = new SearchBuilder<LoteImpressaoAitDTO>("mob_lote_impressao A")
				.fields("A.*,"
					  + "(select count(B.cd_lote_impressao) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao) as qtd_Documentos, "
					  + "(select count(*) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao AND B.st_impressao > " +LoteImpressaoAitSituacao.AGUARDANDO_GERACAO+ ") as total_gerados ")
				.addJoinTable("LEFT OUTER JOIN mob_lote_impressao_ait B on (B.cd_lote_impressao = A.cd_lote_impressao)")
                .addJoinTable(" join ait E on (B.cd_ait = E.codigo_ait)")
                .searchCriterios(searchCriterios)
				.groupBy(" A.cd_lote_impressao ")
				.orderBy(" A.dt_criacao DESC ")
				.build();
		return search.getList(LoteImpressaoAitDTO.class);
	}

	public static Result gerarDocumentos(int cdLoteImpressao, int cdUsuario) throws Exception {
		return gerarDocumentos(cdLoteImpressao, cdUsuario, null);
	}
	
	public static Result gerarDocumentos(int cdLoteImpressao, int cdUsuario, Connection connect) throws Exception {
		boolean isConnectionNull = connect==null;
		boolean loteExigeRegistroDetran = getParametroLoteExigeRegistroDetran();
		
		try {
			if (isConnectionNull)
			{
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			boolean isLote = true;
			int tpNai = 0;
			int tpNip = 1;
			
			LoteImpressao lote = LoteImpressaoDAO.get(cdLoteImpressao, connect);
			ResultSetMap rsmAits = getAllAits(cdLoteImpressao, connect);
			
			while (rsmAits.next()) {

				LoteImpressaoAit loteAit = LoteImpressaoAitDAO.get(cdLoteImpressao, rsmAits.getInt("cd_ait"), connect);	
	
				//Gerar NAI
				if(loteAit.getStImpressao() == LoteImpressaoAitSituacao.AGUARDANDO_GERACAO && lote.getTpDocumento() == tpNai ) {
					
					byte[] content = AitReportServices.getNAIPrimeiraVia(loteAit.getCdAit(), connect, cdUsuario, isLote);
					
					Arquivo arquivo = new Arquivo(0, "NAI AIT "+loteAit.getIdLoteImpressaoAit(), 
												  new GregorianCalendar(), 
												  "nai_"+loteAit.getIdLoteImpressaoAit()+".pdf", 
												  content, 0, new GregorianCalendar(), lote.getCdUsuario(), 
												  1, null, null, 0, null, 0, null, null);
					Result r = ArquivoServices.save(arquivo, null, connect);

					System.out.println("\tarquivo gerado: "+content.length+" bytes");
					if(r.getCode()>0) {
						arquivo.setCdArquivo(r.getCode());
						
						loteAit.setCdArquivo(arquivo.getCdArquivo());
						loteAit.setStImpressao(LoteImpressaoAitSituacao.AGUARDANDO_IMPRESSAO);
						
						r = LoteImpressaoAitServices.save(loteAit, null, connect);
						
						if(r.getCode()>0)
							System.out.println("\tdocumento salvo");
						else
							System.out.println("\terro ao gerar: "+r.getMessage());
					}
				}
				
				//GERAR Nip
				if(loteAit.getStImpressao() == LoteImpressaoAitSituacao.AGUARDANDO_GERACAO && lote.getTpDocumento() == tpNip ) {
					byte[] content = AitReportServices.getNIPPrimeiraVia(loteAit.getCdAit(), isLote, connect, cdUsuario);
					
					Arquivo arquivo = new Arquivo(0, "NIP AIT "+loteAit.getIdLoteImpressaoAit(), 
												  new GregorianCalendar(), 
												  "nip_"+loteAit.getIdLoteImpressaoAit()+".pdf", 
												  content, 0, new GregorianCalendar(), lote.getCdUsuario(), 
												  1, null, null, 0, null, 0, null, null);
					Result r = ArquivoServices.save(arquivo, null, connect);

					if(r.getCode()>0) {
						arquivo.setCdArquivo(r.getCode());
						
						loteAit.setCdArquivo(arquivo.getCdArquivo());
						loteAit.setStImpressao(LoteImpressaoAitSituacao.AGUARDANDO_IMPRESSAO);
						
						r = LoteImpressaoAitServices.save(loteAit, null, connect);
						
						if(r.getCode()>0)
							System.out.println("\tdocumento salvo");
						else
							System.out.println("\terro ao gerar: "+r.getMessage());
					}
				}

				System.out.println("------------------------------------------------------");
				
				if(isConnectionNull)
				{
					connect.setAutoCommit(false);
					connect.commit();
				}
			}
			int stLoteImpressao = loteExigeRegistroDetran ? LoteImpressaoSituacao.AGUARDANDO_ENVIO : LoteImpressaoSituacao.AGUARDANDO_DOCUMENTOS;
			lote.setStLoteImpressao(stLoteImpressao);
			save(lote, connect);

			Result r = new Result(-1);
			r.setCode(1);
			r.setMessage("Geração de movimentos iniciada");
			
			connect.commit();
			return r;
		}
		catch(Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	//***Processo para gerar o lote verificando quais foram enviados ao detran
	public static Result iniciarGeracaoLote(int cdLoteImpressao, int cdUsuario) {
		return iniciarGeracaoLote(cdLoteImpressao, cdUsuario, null);
	}
	
	public static Result iniciarGeracaoLote (int cdLoteImpressao, int cdUsuario, Connection connect) 
	{
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try 
		{
			Result r = new Result(-1);
			
			LoteImpressaoGeracaoLoteTask task = new LoteImpressaoGeracaoLoteTask(cdLoteImpressao, cdUsuario);
			Thread threadGeracaoLote = new Thread(task);
			threadGeracaoLote.start();
			
			r.setCode(1);
			r.setMessage("Geração de documentos iniciada");
			r.addObject("STATUS", getStatusGeracaoDocumentos(cdLoteImpressao, connect));

			return r;
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally
		{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result gerarLote (int cdLoteImpressao, int cdUsuario) 
	{
		return gerarLote (cdLoteImpressao, cdUsuario, null);
	}
	
	public static Result gerarLote (int cdLoteImpressao, int cdUsuario, Connection connect) 
	{
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try 
		{
			LoteImpressao lote = LoteImpressaoDAO.get(cdLoteImpressao, connect);
			ResultSetMap listaAitsLote = LoteImpressaoAitDAO.find(getCriteriosLoteImpressao(cdLoteImpressao), connect);
			
			tratarAitsLote (lote, listaAitsLote, connect);
		
			Result r = new Result(-1);
			r.setCode(1);
			r.setMessage(".......");
			return r;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally
		{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static void tratarAitsLote (LoteImpressao lote, ResultSetMap listaAitsLote, Connection connect) throws Exception {	
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		boolean loteExigeRegistroDetran = getParametroLoteExigeRegistroDetran();
		ResultSetMap aitLote = new ResultSetMap();
		ResultSetMap rsmAits = getAllAits(lote.getCdLoteImpressao(), connect);
		ArrayList<LoteImpressaoAit> loteImpressaoAIT = new ArrayList<LoteImpressaoAit>();
		ArrayList<LoteImpressaoAit> listaErros = new ArrayList<LoteImpressaoAit>();
		LoteImpressao loteComErro = new LoteImpressao();
	    List<LoteImpressaoAit> listaAitsInconsistentes = new ArrayList<LoteImpressaoAit>();
	    int tpNai = 0;
		while (listaAitsLote.next())
		{		
			if (lgBaseAntiga) {
				aitLote = com.tivic.manager.str.AitMovimentoDAO.find(getCriteriosMovimentoImpressao (listaAitsLote.getInt("CD_AIT")), connect);
			}
			else { 
				aitLote = AitMovimentoDAO.find(getCriteriosMovimentoImpressao (listaAitsLote.getInt("CD_AIT")), connect);
			}
			
			if(lote.getTpDocumento() == tpNai) {
				if (loteExigeRegistroDetran) {
					verificaEnviadoDetran(loteImpressaoAIT, lote, aitLote, connect);
				} else {
					buscarAitsGeracaoLote(loteImpressaoAIT, lote, aitLote);
				}
			} else {
				verificaEnviadoDetran(loteImpressaoAIT, lote, aitLote, connect);
			}
			verificaErroDetran (listaErros, lote, aitLote, connect);
		}
		if(lote.getStLoteImpressao() == LoteImpressaoSituacao.NAO_ENVIADO_DETRAN) {
			lote.setStLoteImpressao(LoteImpressaoSituacao.AGUARDANDO_ENVIO);
		}
		if ((lote.getTpDocumento() == tpNai && loteExigeRegistroDetran) || lote.getTpDocumento() != tpNai) {
			if ((loteImpressaoAIT.isEmpty() && listaErros.isEmpty())) {
				lote.setStLoteImpressao(LoteImpressaoSituacao.NAO_ENVIADO_DETRAN);
				save(lote, connect);
				return;
			}
			if (( loteImpressaoAIT.size() > 0 && listaErros.size() <= 0) && loteImpressaoAIT.size() < rsmAits.getLines().size()) {
				lote.setStLoteImpressao(LoteImpressaoSituacao.NAO_ENVIADO_DETRAN);
				save(lote, connect);
				return;
			}
			
			if (( listaErros.size() > 0 && loteImpressaoAIT.size() <= 0) && listaErros.size() < rsmAits.getLines().size()) {
				lote.setStLoteImpressao(LoteImpressaoSituacao.NAO_ENVIADO_DETRAN);
				save(lote, connect);
				return;
			}
			if (loteImpressaoAIT.isEmpty() && !listaErros.isEmpty()) {
				LoteImpressao loteAits = LoteImpressaoDAO.get(lote.getCdLoteImpressao(), connect);
				loteAits.setStLoteImpressao(LoteImpressaoSituacao.AGUARDANDO_ENVIO);
				for (LoteImpressaoAit ait: listaErros)
				{
					int cdAit = ait.getCdAit();
			        LoteImpressaoAit aitInconsistente = new LoteImpressaoAit();
			        aitInconsistente.setStImpressao(LoteImpressaoAitSituacao.AGUARDANDO_GERACAO);
			        aitInconsistente.setCdAit(cdAit);
			        listaAitsInconsistentes.add(aitInconsistente);
					removePdf (ait, connect);
				}
			    boolean tipoDocumentoNai = lote.getTpDocumento() == LoteImpressaoTipoDocumento.LOTE_NAI || 
			    													lote.getTpDocumento() == LoteImpressaoTipoDocumento.LOTE_NAI_CORRECAO;
			    boolean tipoDocumentoNip = lote.getTpDocumento() == LoteImpressaoTipoDocumento.LOTE_NIP || 
			    													lote.getTpDocumento() == LoteImpressaoTipoDocumento.LOTE_NIP_CORRECAO;
			    if (tipoDocumentoNai) {
			        loteAits.setTpDocumento(LoteImpressaoTipoDocumento.LOTE_NAI_CORRECAO);
			    } else if (tipoDocumentoNip) {
			        loteAits.setTpDocumento(LoteImpressaoTipoDocumento.LOTE_NIP_CORRECAO);
			    }
			    if (lote.getTpDocumento() == LoteImpressaoTipoDocumento.LOTE_NAI_CORRECAO || 
			    		lote.getTpDocumento() == LoteImpressaoTipoDocumento.LOTE_NIP_CORRECAO) {
			        loteAits.setStLoteImpressao(LoteImpressaoSituacao.NAO_ENVIADO_DETRAN);
			    }
				loteAits.setAits(listaAitsInconsistentes);
				save(loteAits, connect);
				return;
			}
		}
		if (!loteImpressaoAIT.isEmpty()) {
			try {
				gerarLote (loteImpressaoAIT, lote, connect);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!listaErros.isEmpty()) {
			loteComErro.setIdLoteImpressao(Util.generateRandomString(5));
			for (LoteImpressaoAit ait: listaErros)
			{
				int cdAit = ait.getCdAit();
		        int stImpressao = ait.getStImpressao();
		        LoteImpressaoAit aitInconsistente = new LoteImpressaoAit();
		        aitInconsistente.setCdAit(cdAit);
		        aitInconsistente.setStImpressao(stImpressao);
		        listaAitsInconsistentes.add(aitInconsistente);
				removePdf (ait, connect);
			}
			loteComErro.setQtdDocumentos(listaAitsInconsistentes.size());
			loteComErro.setAits(listaAitsInconsistentes);
			loteComErro.setTpLoteImpressao(lote.getTpLoteImpressao());
			loteComErro.setCdUsuario(lote.getCdUsuario());
			loteComErro.setTpDestino(lote.getTpDestino());
			if(lote.getTpDocumento() == LoteImpressaoTipoDocumento.LOTE_NAI ||
					lote.getTpDocumento() == LoteImpressaoTipoDocumento.LOTE_NAI_CORRECAO) {
				loteComErro.setTpDocumento(LoteImpressaoTipoDocumento.LOTE_NAI_CORRECAO);
			}
			if(lote.getTpDocumento() == LoteImpressaoTipoDocumento.LOTE_NIP ||
					lote.getTpDocumento() == LoteImpressaoTipoDocumento.LOTE_NIP_CORRECAO) {
				loteComErro.setTpDocumento(LoteImpressaoTipoDocumento.LOTE_NIP_CORRECAO);
			}
			loteComErro.setCdLoteImpressao(0);
			loteComErro.setStLoteImpressao(LoteImpressaoSituacao.ECARTAS_LOTE_COM_ERRO);
			save(loteComErro, connect);
		}
	}
	
	public static boolean getParametroLoteExigeRegistroDetran() throws Exception {
		try {
			ParametroRepositoryDAOFactory parametro = new ParametroRepositoryDAOFactory();
			return parametro.getStrategy().getValorOfParametroAsBoolean("lote_exige_registro_detran");
		} catch (Exception e) {
			throw new Exception("O parâmetro lote_exige_registro_detran não foi configurado.");
		}
	}
	
	private static void removePdf (LoteImpressaoAit ait, Connection connect)
	{
		LoteImpressaoAitServices.remove (ait.getCdLoteImpressao(), ait.getCdAit(), false, null, connect);
		ArquivoServices.remove (ait.getCdArquivo(), true, connect);
	}

	private static void addOcorrenciaCorrecao (LoteImpressao lote, LoteImpressaoAit ait, Connection connect)
	{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA"); //Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
		
		int tpNai = 0;
		int tpNip = 1;
		
		ResultSetMap aitLote = new ResultSetMap();
		AitMovimento aitMovimento = new AitMovimento();
		ResultSetMap cdOccurrence = new ResultSetMap();
		
		if (lgBaseAntiga)
		{
			com.tivic.manager.str.AitMovimento aitMovimentoBaseAntiga = new com.tivic.manager.str.AitMovimento();
			
			if (lote.getTpDocumento() == tpNai)
			{
				aitLote = com.tivic.manager.str.AitMovimentoDAO.find(getCriteriosMovimentoImpressao (ait.getCdAit()), connect);
				while (aitLote.next())
				{
					if (aitLote.getInt("TP_STATUS") == AitMovimentoServices.NAI_ENVIADO)
					{
						int cdOccurrenceValue =  cdOccurrenceBaseAntiga (connect);
						aitMovimentoBaseAntiga =  com.tivic.manager.str.AitMovimentoDAO.get(ait.getCdAit(), aitLote.getInt("NR_MOVIMENTO"));
						aitMovimentoBaseAntiga.setCodOcorrencia(cdOccurrenceValue);
						com.tivic.manager.str.AitMovimentoDAO.update(aitMovimentoBaseAntiga, 0, 0, connect);
					}
				}
				
				return;
			}
			
			if (lote.getTpDocumento() == tpNip)
			{
				aitLote = com.tivic.manager.str.AitMovimentoDAO.find(getCriteriosMovimentoImpressao (ait.getCdAit()), connect);
				while (aitLote.next())
				{
					if (aitLote.getInt("TP_STATUS") == AitMovimentoServices.NIP_ENVIADA)
					{
						int cdOccurrenceValue =  cdOccurrenceBaseAntiga (connect);
						aitMovimentoBaseAntiga = com.tivic.manager.str.AitMovimentoDAO.get(ait.getCdAit(), aitLote.getInt("NR_MOVIMENTO"));
						aitMovimentoBaseAntiga.setCodOcorrencia(cdOccurrenceValue);
						com.tivic.manager.str.AitMovimentoDAO.update(aitMovimentoBaseAntiga, 0, 0, connect);
					}
				}
				
				return;
			}
		}
		else
		{
			cdOccurrence = ParametroDAO.find (criteriaParamOcorrencia());
			
			ParametroValor cdOccurrenceValue = new ParametroValor();
			
			if (cdOccurrence.next())
				cdOccurrenceValue = ParametroValorDAO.get (cdOccurrence.getInt("CD_PARAMETRO"), 1, connect);
			
			if (lote.getTpDocumento() == tpNai)
			{
				aitLote = AitMovimentoDAO.find(getCriteriosMovimentoImpressao (ait.getCdAit()), connect);
				while (aitLote.next())
				{
					if (aitLote.getInt("TP_STATUS") == AitMovimentoServices.NAI_ENVIADO)
					{
						aitMovimento = AitMovimentoDAO.get(aitLote.getInt("CD_MOVIMENTO"), ait.getCdAit());
						aitMovimento.setCdOcorrencia(Integer.parseInt(cdOccurrenceValue.getVlInicial()));
						AitMovimentoDAO.update(aitMovimento, 0, 0, connect);
					}
				}
				
				return;
			}
			
			if (lote.getTpDocumento() == tpNip)
			{
				aitLote = AitMovimentoDAO.find(getCriteriosMovimentoImpressao (ait.getCdAit()), connect);
				while (aitLote.next())
				{
					if (aitLote.getInt("TP_STATUS") == AitMovimentoServices.NIP_ENVIADA)
					{
						aitMovimento = AitMovimentoDAO.get(aitLote.getInt("CD_MOVIMENTO"), ait.getCdAit());
						aitMovimento.setCdOcorrencia(Integer.parseInt(cdOccurrenceValue.getVlInicial()));
						AitMovimentoDAO.update(aitMovimento, 0, 0, connect);
					}
				}
				
				return;
			}
		}
	}
	
	private static int cdOccurrenceBaseAntiga (Connection connect)
	{
		int cdOccurrence = 0;
		
		try
		{
			String sql =  "SELECT OCORRENCIA_ERRO_DETRAN FROM parametro" ;
			PreparedStatement ps = connect.prepareStatement(sql);	
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
		
			if (rsm.next())
				cdOccurrence = rsm.getInt("OCORRENCIA_ERRO_DETRAN");
		
			return cdOccurrence;
		}
		catch(Exception e) 
		{
			e.printStackTrace(System.out);
			return 0;
		}	
	}
	
	private static void gerarLote(ArrayList<LoteImpressaoAit> listaEnviados, LoteImpressao lote,  Connection connect) throws DocumentException, IOException
	{
		Arquivo arquivoPdf = new Arquivo();
		
		//Variaveis para gerar lote concatenado
		Document document = new Document();
		ByteArrayOutputStream baout = new ByteArrayOutputStream();
		PdfCopy copy = new PdfSmartCopy(document, baout);
		document.open();
		PdfReader reader = null;
		
		for (LoteImpressaoAit ait: listaEnviados)
		{
			arquivoPdf = ArquivoDAO.get(ait.getCdArquivo(), connect);
			byte[] content = arquivoPdf.getBlbArquivo();
			
			//Inicializando variaveis para lote concatenado
			reader = new PdfReader(content);
			copy.addDocument(reader);
			reader.close();
			
			ait.setStImpressao(LoteImpressaoAitSituacao.EM_IMPRESSAO);
			LoteImpressaoAitDAO.update(ait, connect);
		}
		reader.close();
		document.close();
		baout.close();
		
		//Inserindo arquivo concatenado
		Arquivo arquivoLote = new Arquivo(0, "Ait_Lote_" + Integer.toString(lote.getCdLoteImpressao()), 
				  new GregorianCalendar(), 
				  "lote_"+ Integer.toString(lote.getCdLoteImpressao()) + ".pdf", 
				  baout.toByteArray(), 0, new GregorianCalendar(), lote.getCdUsuario(), 
				  1, null, null, 0, null, 0, null, null);
		ArquivoServices.save(arquivoLote, null, connect);
		
		LoteImpressao loteAits = LoteImpressaoDAO.get(lote.getCdLoteImpressao(), connect);
		loteAits.setCdArquivo(arquivoLote.getCdArquivo());
		loteAits.setStLoteImpressao(LoteImpressaoSituacao.AGUARDANDO_IMPRESSAO);
		save(loteAits, connect);
	}
	
	private static void verificaEnviadoDetran (ArrayList<LoteImpressaoAit> loteImpressaoAIT, LoteImpressao lote, ResultSetMap aitLote, Connection connect)
	{	
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		
		int tpNai = 0;
		int tpNip = 1;
		int tpNaiCorrecao = 2;
		int tpNipCorrecao = 3;
		LoteImpressaoAit obj = new LoteImpressaoAit();
		
		if (lote.getTpDocumento() == tpNai) 
		{
			while (aitLote.next())
			{
				if (aitLote.getInt("TP_STATUS") == AitMovimentoServices.NAI_ENVIADO && 
						aitLote.getInt("LG_ENVIADO_DETRAN") == 1)
				{
					if (lgBaseAntiga)
						obj = LoteImpressaoAitDAO.get(lote.getCdLoteImpressao(), aitLote.getInt("CD_AIT"), connect);
					
					else
						obj = LoteImpressaoAitDAO.get(lote.getCdLoteImpressao(), aitLote.getInt("CD_AIT"), connect);
					
					loteImpressaoAIT.add(obj);
				}
			}
			aitLote.beforeFirst();
		}
		
		if (lote.getTpDocumento() == tpNip ) 
		{
			while (aitLote.next())
			{
				if (aitLote.getInt("TP_STATUS") == AitMovimentoServices.NIP_ENVIADA && 
						aitLote.getInt("LG_ENVIADO_DETRAN") == 1)
				{
					if (lgBaseAntiga)
						obj = LoteImpressaoAitDAO.get(lote.getCdLoteImpressao(), aitLote.getInt("CD_AIT"), connect);
					else
						obj = LoteImpressaoAitDAO.get(lote.getCdLoteImpressao(), aitLote.getInt("CD_AIT"), connect);
					
					loteImpressaoAIT.add(obj);
				}
			}
			aitLote.beforeFirst();
		}
		
		if (lote.getTpDocumento() == tpNaiCorrecao) {
		    while (aitLote.next()) {
		        if (aitLote.getInt("TP_STATUS") == AitMovimentoServices.NAI_ENVIADO && aitLote.getInt("LG_ENVIADO_DETRAN") == 1) {
		            if (lgBaseAntiga) {
		                regerarDocumentos(lote.getCdLoteImpressao(), aitLote.getInt("CD_AIT"), connect);
		                obj = LoteImpressaoAitDAO.get(lote.getCdLoteImpressao(), aitLote.getInt("CD_AIT"), connect);
		            } else {
		                regerarDocumentos(lote.getCdLoteImpressao(), aitLote.getInt("CD_AIT"), connect);
		                obj = LoteImpressaoAitDAO.get(lote.getCdLoteImpressao(), aitLote.getInt("CD_AIT"), connect);
		            }

		            loteImpressaoAIT.add(obj);
		        }
		    }
		    aitLote.beforeFirst();
		}
		
		if (lote.getTpDocumento() == tpNipCorrecao) {
		    while (aitLote.next()) {
		        if (aitLote.getInt("TP_STATUS") == AitMovimentoServices.NIP_ENVIADA && aitLote.getInt("LG_ENVIADO_DETRAN") == 1) {
		            if (lgBaseAntiga) {
		                regerarDocumentos(lote.getCdLoteImpressao(), aitLote.getInt("CD_AIT"), connect);
		                obj = LoteImpressaoAitDAO.get(lote.getCdLoteImpressao(), aitLote.getInt("CD_AIT"), connect);
		            } else {
		                regerarDocumentos(lote.getCdLoteImpressao(), aitLote.getInt("CD_AIT"), connect);
		                obj = LoteImpressaoAitDAO.get(lote.getCdLoteImpressao(), aitLote.getInt("CD_AIT"), connect);
		            }

		            loteImpressaoAIT.add(obj);
		        }
		    }
		    aitLote.beforeFirst();
		}
	}
	
	private static void buscarAitsGeracaoLote(ArrayList<LoteImpressaoAit> loteImpressaoAIT, LoteImpressao lote, ResultSetMap aitLote) {
		int tpNai = 0;
	    LoteImpressaoAit ait = new LoteImpressaoAit();
		if (lote.getTpDocumento() == tpNai) {
			while (aitLote.next())
			{
				ait = LoteImpressaoAitDAO.get(lote.getCdLoteImpressao(), aitLote.getInt("CD_AIT"));
				loteImpressaoAIT.add(ait);
			}
			aitLote.beforeFirst();
		}
	}
	
	private static void verificaErroDetran (ArrayList<LoteImpressaoAit> listaErros, LoteImpressao lote, ResultSetMap aitLote, Connection connect)
	{
		int tpNai = 0;
		int tpNip = 1;
		int tpNaiCorrecao = 2;
		int tpNipCorrecao = 3;
		LoteImpressaoAit obj = new LoteImpressaoAit();
		
		int[] lgDetranSemErro = {AitMovimentoServices.ENVIADO_AGUARDANDO, AitMovimentoServices.ENVIADO_AO_DETRAN};		
		
		if (lote.getTpDocumento() == tpNai || lote.getTpDocumento() == tpNaiCorrecao) 
		{
			while (aitLote.next())
			{
				if (!aitLote.isNull("NR_ERRO") && aitLote.getInt("TP_STATUS") == AitMovimentoServices.NAI_ENVIADO && !contains(lgDetranSemErro, aitLote.getInt("LG_ENVIADO_DETRAN")))
				{
					String nrErro = aitLote.getString("NR_ERRO");
				    if (nrErro != null) {
				        obj = LoteImpressaoAitDAO.get(lote.getCdLoteImpressao(), aitLote.getInt("CD_AIT"), connect);
				        listaErros.add(obj);
				    }
				}
			}
			aitLote.beforeFirst();
		}
		
		if (lote.getTpDocumento() == tpNip || lote.getTpDocumento() == tpNipCorrecao) 
		{
			while (aitLote.next())
			{
				if (!aitLote.isNull("NR_ERRO") && aitLote.getInt("TP_STATUS") == AitMovimentoServices.NIP_ENVIADA && !contains(lgDetranSemErro, aitLote.getInt("LG_ENVIADO_DETRAN")))
				{
					String nrErro = aitLote.getString("NR_ERRO");
				    if (nrErro != null) {
				        obj = LoteImpressaoAitDAO.get(lote.getCdLoteImpressao(), aitLote.getInt("CD_AIT"), connect);
				        listaErros.add(obj);
				    }
				}
			}
			aitLote.beforeFirst();
		}
	}
	
	private static boolean contains(int[] array, int num) {
		boolean resut = false;
		for(int n : array)
			if(n == num)
				resut = true;	
		
		return resut;
	}
	
	private static boolean verificaRetornoDetran (int cdAit, int tpStatus, Connection connect)
	{
		boolean isError = false;
		ResultSetMap arquivoMovimento = ArquivoMovimentoDAO.find (getCriteriosArquivoMovimento(cdAit, tpStatus));
		
		while (arquivoMovimento.next())
		{
			if (arquivoMovimento.getString("nr_erro") != null)
				isError = true;
		}
		
		return isError;
	}
	
	private static ArrayList<ItemComparator> getCriteriosLoteImpressao(int cdLoteImpressao) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		criterios.add(new ItemComparator("cd_lote_impressao", String.valueOf(cdLoteImpressao), ItemComparator.EQUAL, Types.INTEGER));
		
		return criterios;
	}
	
	private static ArrayList<ItemComparator> getCriteriosMovimentoImpressao(int cdAit) {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		if (lgBaseAntiga)
			criterios.add(new ItemComparator("codigo_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		else
			criterios.add(new ItemComparator("cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		
		return criterios;
	}
	
	private static ArrayList<ItemComparator> getCriteriosArquivoMovimento(int cdAit, int tpStatus) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		criterios.add(new ItemComparator("cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("tp_status", String.valueOf(tpStatus), ItemComparator.EQUAL, Types.INTEGER));
		
		return criterios;
	}
	//**********

	public static Result iniciarGeracaoDocumentos(int cdLoteImpressao, int cdUsuario) {
		return iniciarGeracaoDocumentos(cdLoteImpressao, cdUsuario, null);
	}
	
	public static Result iniciarGeracaoDocumentos(int cdLoteImpressao, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			Result r = new Result(-1);
			
			LoteImpressaoGeracaoDocumentosTask task = new LoteImpressaoGeracaoDocumentosTask(cdLoteImpressao, cdUsuario);
			Thread threadGeracaoDocumento = new Thread(task);
			threadGeracaoDocumento.start();
			
			r.setCode(1);
			r.setMessage("Geração de movimentos iniciada");
			r.addObject("STATUS", getStatusGeracaoDocumentos(cdLoteImpressao, connect));
			return r;
		}
		catch(Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	//Processo de impressão do lote
	public static byte[] iniciarImpressaoDocumentos(int cdLoteImpressao) {
		return iniciarImpressaoDocumentos(cdLoteImpressao, null);
	}
	
	public static byte[] iniciarImpressaoDocumentos(int cdLoteImpressao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try 
		{
			LoteImpressao lote = LoteImpressaoDAO.get (cdLoteImpressao, connect);
			Arquivo arquivoLote = ArquivoDAO.get (lote.getCdArquivo(), connect);
			byte[] print = new byte[] {};
			print = arquivoLote.getBlbArquivo();
			lote.setStLoteImpressao (LoteImpressaoAitSituacao.IMPRESSO);
			save(lote);
			
			return print;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback (connect);
			return null;
		}
		finally
		{
			if (isConnectionNull)
				Conexao.desconectar (connect);
		}
	}
	
	
	
	//=============================
	
	public static LoteImpressaoStatus getStatusGeracaoDocumentos(int cdLoteImpressao) {
		return getStatusGeracaoDocumentos(cdLoteImpressao, null);
	}
	
	public static LoteImpressaoStatus getStatusGeracaoDocumentos(int cdLoteImpressao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {

			LoteImpressaoStatus status = new LoteImpressaoStatus();
			
			LoteImpressao lote = LoteImpressaoDAO.get(cdLoteImpressao, connect);
			ResultSetMap rsmAits = getAllAits(cdLoteImpressao, connect);
			
			int qtDocumentosGerados = 0;
			while (rsmAits.next()) {
				if (rsmAits.getInt("st_impressao")>LoteImpressaoAitSituacao.AGUARDANDO_GERACAO) {
					qtDocumentosGerados++;
				}				
			}

			status.setCdLoteImpressao(cdLoteImpressao);
			status.setIdLoteImpressaoAit(lote.getIdLoteImpressao());
			status.setDtInicio(lote.getDtCriacao());
			status.setTotalDocumentos(rsmAits.size());
			status.setQtDocumentosGerados(qtDocumentosGerados);
			status.setStDocumento(lote.getStLoteImpressao());
			
			return status;
		}
		catch(Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static LoteImpressaoStatus getStatusGeracaoLote(int cdLoteImpressao) {
		return getStatusGeracaoLote(cdLoteImpressao, null);
	}
	
	public static LoteImpressaoStatus getStatusGeracaoLote(int cdLoteImpressao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			LoteImpressaoStatus status = new LoteImpressaoStatus();
			
			LoteImpressao lote = LoteImpressaoDAO.get(cdLoteImpressao, connect);
			ResultSetMap rsmAits = getAllAits(cdLoteImpressao, connect);
			
			int qtDocumentosGerados = 0;
			while (rsmAits.next()) {
			 if (rsmAits.getInt("st_impressao")>LoteImpressaoAitSituacao.AGUARDANDO_IMPRESSAO)
					qtDocumentosGerados++;
			}

			status.setCdLoteImpressao(cdLoteImpressao);
			status.setIdLoteImpressaoAit(lote.getIdLoteImpressao());
			status.setDtInicio(lote.getDtCriacao());
			status.setTotalDocumentos(rsmAits.size());
			status.setQtDocumentosGerados(qtDocumentosGerados);
			status.setStDocumento(lote.getStLoteImpressao());
			
			return status;
		}
		catch(Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	//********

	public static Result enviarECarta(int cdLoteImpressao) {
		return enviarECarta(cdLoteImpressao, null);
	}
	
	@SuppressWarnings("static-access")
	public static Result enviarECarta (int cdLoteImpressao, Connection connect) 
	{
		List<ECartaItem> listAits = listarArquivosLote(cdLoteImpressao, connect);
		Result r = new Result(-1);
		ECartaServices eCartaServices = new ECartaServices();
		
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
		
		try 
		{
			eCartaServices.generateLot(listAits);
			r.setCode(1);
			r.setMessage("Lote de Impressão enviado para os correios");
			return r;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally
		{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static List<ECartaItem> listarArquivosLote(int cdLoteImpressao)
	{
		return listarArquivosLote(cdLoteImpressao, null);
	}
	
	protected static List<ECartaItem> listarArquivosLote(int cdLoteImpressao, Connection connect)
	{
		List<ECartaItem> listAits = new ArrayList<ECartaItem>();
		ECartaItem itemAit = new ECartaItem();
		
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try 
		{
			
			ResultSetMap rsmAits = getAllAits(cdLoteImpressao, connect);
			ResultSetMap rsmAit = new ResultSetMap();
			
			while (rsmAits.next())
			{
				rsmAit.addRegister(rsmAits.getRegister());
				insertAit(itemAit, rsmAit, rsmAits.getInt("CD_AIT"), connect);
				listAits.add(itemAit);
				itemAit = new ECartaItem();
			}
			rsmAits.beforeFirst();
			
			return listAits;
		}
		catch (Exception e)
		{
			System.out.println("Error in listarArquivosLote: " + e.getMessage());
			return null;
		}
		finally
		{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static void insertAit (ECartaItem itemAit, ResultSetMap rsmAit, int cdAit, Connection connect)
	{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");	
		
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try 
		{
			if (rsmAit.next())
			{
				if (lgBaseAntiga)
				{
						String sql = " SELECT A.codigo_ait as cd_ait, A.cod_bairro, A.cod_municipio, "
							   + " B.nm_bairro, "
							   + " C.nm_municipio, C.nm_uf "
							   + " FROM ait A "
							   + " LEFT OUTER JOIN bairro B on (A.cod_bairro = B.cod_bairro) "
							   + " LEFT OUTER JOIN municipio C on (A.cod_municipio = C.cod_municipio) "
							   + " WHERE A.codigo_ait = ?";
						PreparedStatement ps = connect.prepareStatement(sql);	
						ps.setInt (1, cdAit);
						ResultSetMap rsmDadosAit = new ResultSetMap (ps.executeQuery());
				
						//Inserindo dados vindos do RSM
						itemAit.setNumeroLote (Integer.toString(rsmAit.getInt("CD_LOTE_IMPRESSAO")));
						//Numero da etique: Ver como sera montada a etiquera com Anderson
						itemAit.setNumeroEtiqueta(null);
						itemAit.setNomeDestinatario (rsmAit.getString("NM_PROPRIETARIO"));
						itemAit.setNomeLogradouro (rsmAit.getString("DS_LOGRADOURO"));
						itemAit.setNumEndereco (rsmAit.getString("DS_NR_IMOVEL"));
						itemAit.setComplementEndereco (rsmAit.getString("NM_COMPLEMENTO"));
						itemAit.setCep (rsmAit.getString("NR_CEP"));
						itemAit.setCdArquivo(rsmAit.getString("CD_AIT"));
				
						if (rsmDadosAit.next())
						{
							itemAit.setNmBairro (rsmDadosAit.getString("NM_BAIRRO"));
							itemAit.setNmMunicipio (rsmDadosAit.getString("NM_MUNICIPIO"));
							itemAit.setSgEstado(rsmDadosAit.getString("NM_UF"));
						}
				
						Arquivo blbArquivo = ArquivoDAO.get (rsmAit.getInt("CD_ARQUIVO"), connect);
						itemAit.setBlbArquivo (blbArquivo.getBlbArquivo());

					}
					else
					{
						String sql = " SELECT A.cd_ait, A.cd_bairro, A.cd_cidade, "
						   + " B.nm_bairro, "
						   + " C.nm_cidade, "
						   + " D.sg_estado "
						   + " FROM mob_ait A "
						   + " LEFT OUTER JOIN grl_bairro B on (A.cd_bairro = B.cd_bairro) "
						   + " LEFT OUTER JOIN grl_cidade C on (A.cd_cidade = C.cd_cidade) "
						   + " LEFT OUTER JOIN grl_estado D on (C.cd_estado = D.cd_estado) "
						   + " WHERE A.cd_ait = ?";
					PreparedStatement ps = connect.prepareStatement(sql);	
					ps.setInt (1, cdAit);
					ResultSetMap rsmDadosAit = new ResultSetMap (ps.executeQuery());
			
					//Inserindo dados vindos do RSM
					itemAit.setNumeroLote (Integer.toString(rsmAit.getInt("CD_LOTE_IMPRESSAO")));
					//Numero da etique: Ver como sera montada a etiquera com Anderson
					itemAit.setNumeroEtiqueta(null);
					itemAit.setNomeDestinatario (rsmAit.getString("NM_PROPRIETARIO"));
					itemAit.setNomeLogradouro (rsmAit.getString("DS_LOGRADOURO"));
					itemAit.setNumEndereco (rsmAit.getString("DS_NR_IMOVEL"));
					itemAit.setComplementEndereco (rsmAit.getString("NM_COMPLEMENTO"));
					itemAit.setCep (rsmAit.getString("NR_CEP"));
					itemAit.setCdArquivo(rsmAit.getString("CD_AIT"));
			
					if (rsmDadosAit.next())
					{
						itemAit.setNmBairro (rsmDadosAit.getString("NM_BAIRRO"));
						itemAit.setNmMunicipio (rsmDadosAit.getString("NM_CIDADE"));
						itemAit.setSgEstado(rsmDadosAit.getString("SG_ESTADO"));
					}
			
					Arquivo blbArquivo = ArquivoDAO.get (rsmAit.getInt("CD_ARQUIVO"), connect);
					itemAit.setBlbArquivo (blbArquivo.getBlbArquivo());
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace (System.out);
		}	
		finally 
		{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static boolean periodoEmissao (Ait ait, int periodoDiasEmissaoAit) throws ValidacaoException {	
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		if (lgBaseAntiga)
			return periodoEmissaoBaseAntiga(ait, periodoDiasEmissaoAit);
		GregorianCalendar dtInicial = new GregorianCalendar();
		dtInicial.set(Calendar.HOUR, 0);
		dtInicial.set(Calendar.MINUTE, 0);
		dtInicial.set(Calendar.SECOND, 0);
		dtInicial.add(Calendar.DATE, -50);
		GregorianCalendar dtEmissao = new GregorianCalendar();
		dtEmissao = ait.getDtInfracao();
		return dtEmissao.after(dtInicial);
	}
	
	public static boolean periodoEmissaoBaseAntiga (Ait ait, int periodoDiasEmissaoAit) throws ValidacaoException {
		
		GregorianCalendar dtInicial = new GregorianCalendar();
		dtInicial.set(Calendar.HOUR, 0);
		dtInicial.set(Calendar.MINUTE, 0);
		dtInicial.set(Calendar.SECOND, 0);
		dtInicial.add(Calendar.DATE, - periodoDiasEmissaoAit);
		GregorianCalendar dtEmissao = new GregorianCalendar();
		dtEmissao = ait.getDtInfracao();
		return dtEmissao.after(dtInicial);
	}
	
	private static ArrayList<ItemComparator> getCriteriosMovimentosNIP(int cdAit) {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		if (lgBaseAntiga)
			criterios.add(new ItemComparator("codigo_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		else
			criterios.add(new ItemComparator("cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		
		return criterios;
	}
	
	public static Result regerarDocumentos (int cdLoteImpressao, int cdAit) 
	{
		return regerarDocumentos(cdLoteImpressao,cdAit, null);
	}
	
	public static Result regerarDocumentos (int cdLoteImpressao, int cdAit, Connection connect) 
	{
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try 
		{
			boolean isLote = true;
			int tpNai = 0;
			int tpNip = 1;
			int tpNaiCorrecao = 2;
			int tpNipCoorecao = 3;
			
			LoteImpressao lote = LoteImpressaoDAO.get(cdLoteImpressao, connect);
			LoteImpressaoAit aitLote = LoteImpressaoAitDAO.get(cdLoteImpressao, cdAit);
			
			System.out.println("Iniciando processo de geração de lote de documentos...");
			System.out.println("\tID:"+lote.getIdLoteImpressao());
			System.out.println("\tCD:"+lote.getCdLoteImpressao());
			System.out.println("------------------------------------------------------");

			LoteImpressaoAit loteAit = LoteImpressaoAitDAO.get(cdLoteImpressao, cdAit, connect);	
			System.out.println("\tverificando... ["+loteAit.getCdAit()+":"+loteAit.getIdLoteImpressaoAit()+":"+loteAit.getStImpressao()+"]");

			//Gerar NAI
			if (loteAit.getStImpressao() == LoteImpressaoAitSituacao.AGUARDANDO_GERACAO && 
					lote.getTpDocumento() == tpNai || lote.getTpDocumento() == tpNaiCorrecao ) 
			{
				byte[] content = LoteImpressaoReportServices.getNAIPrimeiraVia(loteAit.getCdAit(), lote.getCdUsuario());
					
				Arquivo arquivo = new Arquivo(0, "NAI AIT "+loteAit.getIdLoteImpressaoAit(), 
											  new GregorianCalendar(), 
											  "nai_"+loteAit.getIdLoteImpressaoAit()+".pdf", 
											  content, 0, new GregorianCalendar(), lote.getCdUsuario(), 
											  1, null, null, 0, null, 0, null, null);
				Result r = ArquivoServices.save(arquivo, null, connect);

				System.out.println("\tarquivo gerado: "+content.length+" bytes");
				if(r.getCode()>0) {
					arquivo.setCdArquivo(r.getCode());
					
					loteAit.setCdArquivo(arquivo.getCdArquivo());
					loteAit.setStImpressao(LoteImpressaoAitSituacao.AGUARDANDO_IMPRESSAO);
						
					r = LoteImpressaoAitServices.save(loteAit, null, connect);
					
					if(r.getCode()>0)
						System.out.println("\tdocumento salvo");
					else
						System.out.println("\terro ao gerar: "+r.getMessage());
				}
			}
				
			//GERAR Nip
			if (loteAit.getStImpressao() == LoteImpressaoAitSituacao.AGUARDANDO_GERACAO && 
					lote.getTpDocumento() == tpNip || lote.getTpDocumento() == tpNipCoorecao) 
			{
				byte[] content = LoteImpressaoReportServices.getNIPPrimeiraVia(loteAit.getCdAit(), isLote, connect);
					
				Arquivo arquivo = new Arquivo(0, "NIP AIT "+loteAit.getIdLoteImpressaoAit(), 
											  new GregorianCalendar(), 
											  "nip_"+loteAit.getIdLoteImpressaoAit()+".pdf", 
											  content, 0, new GregorianCalendar(), lote.getCdUsuario(), 
											  1, null, null, 0, null, 0, null, null);
				Result r = ArquivoServices.save(arquivo, null, connect);

				System.out.println("\tarquivo gerado: "+content.length+" bytes");
				if(r.getCode()>0) {
					arquivo.setCdArquivo(r.getCode());
						
					loteAit.setCdArquivo(arquivo.getCdArquivo());
					loteAit.setStImpressao(LoteImpressaoAitSituacao.AGUARDANDO_IMPRESSAO);
						
					r = LoteImpressaoAitServices.save(loteAit, null, connect);
						
					if(r.getCode()>0)
						System.out.println("\tdocumento salvo");
					else
						System.out.println("\terro ao gerar: "+r.getMessage());
				}
			}

			System.out.println("------------------------------------------------------");
			
			lote.setStLoteImpressao(LoteImpressaoSituacao.AGUARDANDO_ENVIO);
			save(lote, connect);

			Result r = new Result(-1);
			r.setCode(1);
			r.setMessage("Geração de movimentos iniciada");
			return r;
		}
		catch(Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	@SuppressWarnings("deprecation")
	private static void verificarDefesas(ResultSetMap rsmAitMovimento, Connection connect) throws ValidacaoException, Exception
	{

		
		if (rsmAitMovimento.next())
		{
		
			boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
			
			if (lgBaseAntiga)
			{
				List<com.tivic.manager.str.AitMovimento> aitsDefesas = com.tivic.manager.str.AitMovimentoServices.getAllDefesas(rsmAitMovimento.getInt("CD_AIT"), connect);
				
				if(aitsDefesas.isEmpty())
				{
					validarSemDefesaOld(rsmAitMovimento, connect);
				}
				else
				{
					validarDefesaProtocoladaOld(aitsDefesas);
				}
				
			}
			else
			{
				
				List<AitMovimento> aitsDefesas = AitMovimentoServices.getAllDefesas(rsmAitMovimento.getInt("CD_AIT"), connect);
				
				if(aitsDefesas.isEmpty())
				{
					validarSemDefesa(rsmAitMovimento, connect);
				}
				else
				{
					validarDefesaProtocolada(aitsDefesas, rsmAitMovimento.getInt("CD_AIT"), connect);
				}
				
			}
		}
		
	}
	
	private static void validarDefesaProtocolada(List<AitMovimento> aitsDefesas, int cdAit, Connection connect) throws ValidacaoException {	
		for(AitMovimento aitDefesa : aitsDefesas) {
			if(aitDefesa.getTpStatus() == AitMovimentoServices.DEFESA_INDEFERIDA || 
					aitDefesa.getTpStatus() == AitMovimentoServices.ADVERTENCIA_DEFESA_INDEFERIDA){
				verificarCacelamentoDeIndeferimento(cdAit, connect);
				return;
			}
		}
		throw new ValidacaoException("AIT possui uma defesa deferida");
	}
	
	private static void verificarCacelamentoDeIndeferimento(int cdAit, Connection connect) throws ValidacaoException {
		List<AitMovimento> aitsDefesasCanceladas = AitMovimentoServices.getAllDefesasCanceladas(cdAit, connect);
		for(AitMovimento aitDefesa : aitsDefesasCanceladas) {
			if(aitDefesa.getTpStatus() == AitMovimentoServices.CANCELAMENTO_ADVERTENCIA_DEFESA_INDEFERIDA ||
							aitDefesa.getTpStatus() == AitMovimentoServices.CANCELAMENTO_DEFESA_INDEFERIDA){
				throw new ValidacaoException("AIT possui uma defesa");
			}
		}
		return;
	}

	@SuppressWarnings({"deprecation" })
	private static void validarDefesaProtocoladaOld(List<com.tivic.manager.str.AitMovimento> aitsDefesas) throws ValidacaoException 
	{
		
		for(com.tivic.manager.str.AitMovimento aitDefesa : aitsDefesas) 
		{
			if(aitDefesa.getTpStatus() == AitMovimentoServices.DEFESA_INDEFERIDA)
			{
				return;
			}
		}
		
		throw new ValidacaoException("AIT possui uma defesa deferida");
		
	}
	
	private static void validarSemDefesaOld( ResultSetMap rsmAitMovimento, Connection connect ) throws ValidacaoException, Exception {
		rsmAitMovimento.beforeFirst();
		if (rsmAitMovimento.next()){
			if (!naiEntregue(rsmAitMovimento.getInt("ST_AVISO_RECEBIMENTO"))){
				semDadosEntregaPublicacao(rsmAitMovimento.getInt("CD_AIT"));
			}
			else{
				comDadosEntregaPublicacao(rsmAitMovimento.getInt("CD_AIT"), rsmAitMovimento.getGregorianCalendar("DT_AVISO_RECEBIMENTO"), connect);
			}
		}
	}
	
	private static void validarSemDefesa(ResultSetMap rsmAitMovimento, Connection connect ) throws ValidacaoException, Exception {
		rsmAitMovimento.beforeFirst();
		CorreiosEtiqueta etiqueta = getEtiqueta(rsmAitMovimento.getInt("CD_AIT"), rsmAitMovimento.getInt("TP_STATUS"));
		if (rsmAitMovimento.next()){	
			if (naiEntregue(etiqueta.getStAvisoRecebimento())){
				comDadosEntregaPublicacao(rsmAitMovimento.getInt("CD_AIT"), etiqueta.getDtAvisoRecebimento(), connect);
			}
			else if (naiPublicada(rsmAitMovimento.getInt("ST_PUBLICACAO_DO"))){
				comDadosEntregaPublicacao(rsmAitMovimento.getInt("CD_AIT"), rsmAitMovimento.getGregorianCalendar("DT_PUBLICACAO_DO"), connect);
			}
			else{
				semDadosEntregaPublicacao(rsmAitMovimento.getInt("CD_AIT"));
			}
		}
	}
	
	private static CorreiosEtiqueta getEtiqueta(int cdAit, int tpStatus) throws Exception {
		CorreiosEtiqueta etiqueta = getEtiqueta(cdAit, tpStatus, new CustomConnection());
		if(etiqueta == null) {
			throw new ValidacaoException("Não foi encontrado nenhuma etiqueta para o cdAit: " + cdAit);
		}
		return etiqueta;
	}
	
	private static CorreiosEtiqueta getEtiqueta(int cdAit, int tpStatus, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		if (lgBaseAntiga) {
			searchCriterios.addCriteriosEqualInteger("tp_status", tpStatus);
		}
		else {
			searchCriterios.addCriteriosEqualInteger("B.cd_ait", cdAit);
			searchCriterios.addCriteriosEqualInteger("A.tp_status", tpStatus);
		}
		CorreiosEtiqueta etiqueta = new CorreiosEtiquetaRepositoryDAO().find(searchCriterios, new CustomConnection()).get(0);
		return etiqueta;
	}

	public static boolean naiEntregue(int stAvisoRecebimento) 
	{
		return stAvisoRecebimento == 1 || stAvisoRecebimento == 100;
	}
	
	public static boolean naiPublicada(int stPublicacaoDo) 
	{
		return stPublicacaoDo == 1 || stPublicacaoDo == 100;
	}
	
	private static void semDadosEntregaPublicacao(int cdAit)  throws ValidacaoException
	{
		com.tivic.manager.str.AitMovimento movimentoNAI = com.tivic.manager.str.AitMovimentoServices.getMovimentoTpStatus(cdAit, AitMovimentoServices.NAI_ENVIADO);
		GregorianCalendar dtExpiracao = movimentoNAI.getDtMovimento();
		dtExpiracao.add(Calendar.DAY_OF_MONTH, ParametroServices.getValorOfParametroAsInteger("MOB_CALCULAR_JUROS_NIP", 60));
		if (Util.getDataAtual().before(dtExpiracao)){
			throw new ValidacaoException("NAI não expirou prazo de 60 dias e não possui dados de entrega");
		}
	}
	
	private static void comDadosEntregaPublicacao(int cdAit, GregorianCalendar dtEntreguePublicado, Connection connect)  throws ValidacaoException{
		IVerificarPrazoDefesa estrategiaVerificarPrazoDefesa = new EstrategiaVerificarPrazoDefesa().getEstrategia(AitReportServices.verificarEstado(connect));
		estrategiaVerificarPrazoDefesa.verificarPrazoDefesa(cdAit, dtEntreguePublicado, connect);
	}
	
}
