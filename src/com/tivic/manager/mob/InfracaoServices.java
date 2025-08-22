package com.tivic.manager.mob;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class InfracaoServices {	
	public static final int MULTA_RESPONSABILIDADE_CONDUTOR = 0;
	public static final int MULTA_RESPONSABILIDADE_PROPRIETARIO = 1;
	public static final int MULTA_NAO_INDENTIFICACAO_CONDUTOR = 50002;
	public static final int MULTA_NAO_INDENTIFICACAO_CONDUTOR_2 = 50020;
	public static final int MULTA_NAO_SUSPENDE_CNH = 0;
	public static final int MULTA_SUSPENDE_CNH = 1;
	public static final int MULTA_COMPETENCIA_MUNICIPAL = 0;
	public static final int MULTA_COMPETENCIA_ESTADUAL = 1;	

	public static Result save(Infracao infracao){
		return save(infracao, null);	
	}

	public static Result save(Infracao infracao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(infracao==null)
				return new Result(-1, "Erro ao salvar. Infracao é nulo");

			int retorno;
			if(infracao.getCdInfracao()==0){
				retorno = InfracaoDAO.insert(infracao, connect);
				infracao.setCdInfracao(retorno);
			} else {
				retorno = InfracaoDAO.update(infracao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INFRACAO", infracao);
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
	public static Result remove(int cdInfracao){
		return remove(cdInfracao, false, null);
	}
	public static Result remove(int cdInfracao, boolean cascade){
		return remove(cdInfracao, cascade, null);
	}
	public static Result remove(int cdInfracao, boolean cascade, Connection connect){
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
			retorno = InfracaoDAO.delete(cdInfracao, connect);
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
	
	/**
	 * Retorna a infracao vigente
	 * @param cdInfracao
	 * @param connect
	 * @return
	 */
	public static Infracao getVigente(int cdInfracao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
						
			if(lgBaseAntiga)
				pstmt = connect.prepareStatement("SELECT * FROM infracao WHERE nr_cod_detran = (select nr_cod_detran from infracao where cod_infracao = ?) AND dt_fim_vigencia IS NULL");
			else
				pstmt = connect.prepareStatement("SELECT * FROM mob_infracao WHERE nr_cod_detran = (select nr_cod_detran from mob_infracao where cd_infracao = ?) AND dt_fim_vigencia IS NULL");
			
			pstmt.setInt(1, cdInfracao);
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(lgBaseAntiga)
					return new Infracao(rs.getInt("cod_infracao"),
							rs.getString("ds_infracao2"),
							rs.getInt("nr_pontuacao"),
							rs.getInt("nr_cod_detran"),
							rs.getDouble("nr_valor_ufir"),
							rs.getString("nm_natureza"),
							rs.getString("nr_artigo"),
							rs.getString("nr_paragrafo"),
							rs.getString("nr_inciso"),
							rs.getString("nr_alinea"),
							rs.getInt("tp_competencia"),
							rs.getInt("lg_prioritaria"),
							(rs.getTimestamp("dt_fim_vigencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_fim_vigencia").getTime()),
							rs.getDouble("vl_infracao"),
							0,
							0);
				else
					return new Infracao(rs.getInt("cd_infracao"),
							rs.getString("ds_infracao"),
							rs.getInt("nr_pontuacao"),
							rs.getInt("nr_cod_detran"),
							rs.getDouble("vl_ufir"),
							rs.getString("nm_natureza"),
							rs.getString("nr_artigo"),
							rs.getString("nr_paragrafo"),
							rs.getString("nr_inciso"),
							rs.getString("nr_alinea"),
							rs.getInt("tp_competencia"),
							rs.getInt("lg_prioritaria"),
							(rs.getTimestamp("dt_fim_vigencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_fim_vigencia").getTime()),
							rs.getDouble("vl_infracao"),
							0,
							0);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Retorna os dados para sincronia. Usado pelos aplicativos móveis.
	 * @return
	 */
	public static ResultSetMap getSyncData() {
		return getSyncData(null);
	}

	public static ResultSetMap getSyncData(Connection connect) {
		
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			ResultSetMap rsm = getAll(connect);
			
			return toStrInfracoes(rsm);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	


	/**
	 * Retorna todos os registros da tabela de infração. Leva em conta o modelo antigo.
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAll() {
		return getAll(null);
	}
	
	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			String sql = "SELECT * FROM mob_infracao WHERE dt_fim_vigencia is NULL OR dt_fim_vigencia > now() ";
			
			if(Util.isStrBaseAntiga())
				sql = "SELECT A.COD_INFRACAO AS CD_INFRACAO, "
						   + "A.DS_INFRACAO2 AS DS_INFRACAO, "
			               + "A.NR_PONTUACAO, "
			               + "A.NR_COD_DETRAN, "
			               + "A.NR_VALOR_UFIR, "
			               + "A.NM_NATUREZA, "
			               + "A.NR_ARTIGO, "
			               + "A.NR_PARAGRAFO, "
			               + "A.NR_INCISO, "
			               + "A.NR_ALINEA, "
			               + "A.TP_COMPETENCIA, "
			               + "A.LG_PRIORITARIO, "
			               + "A.DT_FIM_VIGENCIA, "
			               + "A.VL_INFRACAO, " 
			               + "A.TP_RESPONSABILIDADE " +
						" FROM INFRACAO A WHERE A.DT_FIM_VIGENCIA IS NULL OR A.DT_FIM_VIGENCIA > CURRENT_DATE";
			
			pstmt = connect.prepareStatement(sql);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		
		boolean isConnectionNull = connect == null;

		boolean lgBaseAntiga    = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA"); //Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
		
		if(lgBaseAntiga) {
			String likeDsInfracao   = "";
			int qtLimite            = 0;
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("DS_INFRACAO_UNIFICADO")) {
//					crt.add(new ItemComparator("A.DS_INFRACAO", criterios.get(i).getValue(), criterios.get(i).getTypeComparation(), criterios.get(i).getTypeColumn()));
//					crt.add(new ItemComparator("A.DS_INFRACAO2", criterios.get(i).getValue(), criterios.get(i).getTypeComparation(), criterios.get(i).getTypeColumn()));
					likeDsInfracao = criterios.get(i).getValue().toUpperCase();
				} else {
					crt.add(criterios.get(i));
				}
			}
			
			return Search.find("SELECT A.*, A.COD_INFRACAO AS CD_INFRACAO, CASE " + 
					"         WHEN DS_INFRACAO is not null THEN DS_INFRACAO " + 
					"         ELSE DS_INFRACAO2 " + 
					"      END as DS_INFRACAO_UNIFICADO FROM infracao A WHERE (DT_FIM_VIGENCIA IS NULL OR DT_FIM_VIGENCIA > CURRENT_DATE) " + 
					(!likeDsInfracao.equals("") ? " AND ((UPPER(A.DS_INFRACAO) LIKE '%"+likeDsInfracao+"%' OR UPPER(A.DS_INFRACAO2) LIKE '%"+likeDsInfracao+"%')) " : ""), 
					"", crt, (isConnectionNull ? Conexao.conectar() : connect), isConnectionNull);
		} else {			
			String limit = "";
			Criterios crt = new Criterios();
			
			String keyword  = "";
			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("limit")) {
					limit += " LIMIT " + criterios.get(i).getValue();				
				} 
				else if (criterios.get(i).getColumn().equalsIgnoreCase("keyword")) {
					if(Util.isNumber(criterios.get(i).getValue()))
						keyword = criterios.get(i).getValue();
				}
				else {
					crt.add(criterios.get(i));
				}
			}
			
			String sql = " SELECT * FROM mob_infracao ";
			
			if(!keyword.equals("")) {
				if(Util.isNumber(keyword))
					sql += " WHERE ((UPPER(ds_infracao) LIKE '%"+keyword+"%' OR CAST(nr_cod_detran as VARCHAR) LIKE '%"+keyword+"%')) ";
				else
					crt.add(new ItemComparator("ds_infracao", keyword, ItemComparator.LIKE_ANY, Types.VARCHAR));
			}
		
			return Search.find(sql, " ORDER BY nr_cod_detran " + limit, 
					crt, (isConnectionNull ? Conexao.conectar() : connect), isConnectionNull);
		}
					
	}

	private static ResultSetMap toStrInfracoes(ResultSetMap rsm) {
		
		if(Util.isStrBaseAntiga()) {
			
			ResultSetMap rsmOut = new ResultSetMap();
			
			while(rsm.next()) {
				Infracao infracaoFta = new Infracao();
				
				infracaoFta.setCdInfracao(rsm.getInt("cd_infracao"));
				infracaoFta.setDsInfracao(rsm.getString("ds_infracao"));
				infracaoFta.setNrPontuacao(rsm.getInt("nr_pontuacao"));
				infracaoFta.setNrCodDetran(rsm.getInt("nr_cod_detran"));
				infracaoFta.setNmNatureza(rsm.getString("nm_natureza"));
				infracaoFta.setNrArtigo(rsm.getString("nr_artigo"));
				infracaoFta.setNrParagrafo(rsm.getString("nr_paragrafo"));
				infracaoFta.setNrInciso(rsm.getString("nr_inciso"));
				infracaoFta.setNrAlinea(rsm.getString("nr_alinea"));
				infracaoFta.setTpCompetencia(rsm.getInt("tp_competencia"));
				infracaoFta.setVlInfracao(rsm.getDouble("vl_infracao"));
				infracaoFta.setLgPrioritaria(rsm.getInt("lg_prioritaria"));
				
				com.tivic.manager.str.Infracao conv = toStrInfracao(infracaoFta);
				
				HashMap<String, Object> hash = new HashMap<String, Object>();
				
				hash.put("cd_infracao", conv.getCdInfracao());
				hash.put("ds_infracao", conv.getDsInfracao());
				hash.put("nr_pontuacao", conv.getNrPontuacao());
				hash.put("nr_cod_detran", conv.getNrCodDetran());
				hash.put("nm_natureza", conv.getNmNatureza());
				hash.put("nr_artigo", conv.getNrArtigo());
				hash.put("nr_paragrafo", conv.getNrParagrafo());
				hash.put("nr_inciso", conv.getNrInciso());
				hash.put("nr_alinea", conv.getNrAlinea());
				hash.put("tp_competencia", conv.getTpCompetencia());
				hash.put("vl_infracao", conv.getVlInfracao());
				hash.put("lg_prioritaria", conv.getLgPrioritaria());
				
				rsmOut.addRegister(hash);
			}
			
			return rsmOut;
			
		} else {
			
			return rsm;
			
		}
		
	}
	
	private static com.tivic.manager.str.Infracao toStrInfracao (Infracao infracao) {
		
		com.tivic.manager.str.Infracao re = new com.tivic.manager.str.Infracao();
				
		return new com.tivic.manager.str.Infracao (
				infracao.getCdInfracao(),
				infracao.getDsInfracao(),
				infracao.getNrPontuacao(),
				infracao.getNrCodDetran(),
				(float) 0.0,
				infracao.getNmNatureza(),
				infracao.getNrArtigo(),
				infracao.getNrParagrafo(),
				infracao.getNrInciso(),
				infracao.getNrAlinea(),
				infracao.getTpCompetencia(),
				infracao.getVlInfracao().floatValue(),
				infracao.getLgPrioritaria()
				);
		
	}
	
	public static ResultSetMap validVigente(int nrCodDetran, Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM MOB_INFRACAO WHERE NR_COD_DETRAN = ? AND DT_FIM_VIGENCIA IS NULL");
			pstmt.setInt(1, nrCodDetran);
			
			return new ResultSetMap(pstmt.executeQuery());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public static int getCodInfracao(int nrCodDetran) 
	{
		return getCodInfracao(nrCodDetran, null);
	}

	public static int getCodInfracao(int nrCodDetran, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
		
		PreparedStatement pstmt;
		String sql = " SELECT cd_infracao FROM mob_infracao"
				   + "  WHERE nr_cod_detran = ? AND dt_fim_vigencia IS NULL";
		
		try 
		{
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, nrCodDetran);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				return rs.getInt("CD_INFRACAO");
			}
			else
			{
				return 0;
			}
		}
		catch(SQLException sqlExpt) 
		{
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getCodInfracao: " + sqlExpt);
			return 0;
		}
		catch(Exception e) 
		{
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getCodInfracao: " + e);
			return 0;
		}
		finally 
		{
			if (isConnectionNull)
			{
				Conexao.desconectar(connect);
			}
		}
	}
	
	public static Infracao getByCodDetran(int nrCodDetran) throws ValidacaoException {
		return getByCodDetran(nrCodDetran, null);
	}

	public static Infracao getByCodDetran(int nrCodDetran, Connection connect) throws ValidacaoException {

		boolean isConnectionNull = connect==null;
		Infracao infracao = new Infracao();

		try {

			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt;
			ResultSet rs;

			String sql = "SELECT cd_infracao FROM mob_infracao WHERE nr_cod_detran = ? AND dt_fim_vigencia IS NULL";

			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, nrCodDetran);
			rs = pstmt.executeQuery();

			if (rs.next())
				infracao = InfracaoDAO.get(rs.getInt("CD_INFRACAO"));

			return infracao;

		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoDAO.getByCodDetran: " + e);
			throw new ValidacaoException("Erro ao buscar infração usando codigo do detran");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}

	}
	
	@SuppressWarnings("static-access")
	protected String getCompetenciaInfracao(int cdAit) throws ValidacaoException
	{
		try
		{
			Ait ait = new Ait();
			AitDAO aitDao = new AitDAO();
			Infracao infracao = new Infracao();
			InfracaoDAO infracaoDao = new InfracaoDAO();
			String competenciaInfracao = "";
			
			ait = aitDao.get(cdAit);
			infracao = infracaoDao.get(ait.getCdInfracao());
			
			competenciaInfracao = infracao.getTpCompetencia() == MULTA_COMPETENCIA_MUNICIPAL ? "MUNICIPAL" : "ESTADUAL";
			
			return competenciaInfracao;
		}
		catch (Exception e)
		{
			System.out.println("Erro in InfracaoServices > getCompetenciaInfracao()");
			e.printStackTrace();
			throw new ValidacaoException("Erro ao verificar a competencia da infração");
		}
	}

}
