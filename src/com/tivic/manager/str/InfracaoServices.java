package com.tivic.manager.str;

import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import sol.dao.ResultSetMap;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.util.Util;

public class InfracaoServices {
	
	/**
	 * Retorna os dados para sincronia. Usado pelos aplicativos móveis (eTrânsito e eTrasporte).
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
			
			boolean str2mob = ManagerConf.getInstance().getAsBoolean("STR_TO_MOB");
			if(str2mob) {
				return com.tivic.manager.mob.InfracaoServices.getSyncData(connect);
			}
			
			return getAll(connect);
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
			
			String sql = "SELECT * FROM str_infracao";
			
			if(Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1"))
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
			               + "A.LG_PRIORITARIO,"
			               + "A.DT_FIM_VIGENCIA,"
			               + "A.VL_INFRACAO,"
			               + "A.TP_RESPONSABILIDADE " +
						" FROM INFRACAO A WHERE A.DT_FIM_VIGENCIA IS NULL";
			
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
			
			if(cdInfracao==0) return null;
			
			boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
			
			//TEMPORARIAMENTE
			Infracao infracao = InfracaoDAO.get(cdInfracao, connect);
			
			if(lgBaseAntiga)
				pstmt = connect.prepareStatement("SELECT * FROM infracao WHERE nr_cod_detran=? AND dt_fim_vigencia IS NULL");
			else
				pstmt = connect.prepareStatement("SELECT * FROM str_infracao WHERE nr_cod_detran=? AND dt_fim_vigencia IS NULL");
			
			pstmt.setInt(1, infracao.getNrCodDetran());
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(lgBaseAntiga)
					return new Infracao(rs.getInt("cod_infracao"),
						rs.getString("ds_infracao"),
						rs.getInt("nr_pontuacao"),
						rs.getInt("nr_cod_detran"),
						rs.getFloat("nr_valor_ufir"),
						rs.getString("nm_natureza"),
						rs.getString("nr_artigo"),
						rs.getString("nr_paragrafo"),
						rs.getString("nr_inciso"),
						rs.getString("nr_alinea"),
						rs.getInt("tp_competencia"),
						rs.getFloat("vl_infracao"),
						rs.getInt("lg_prioritario"));
				else
					return new Infracao(rs.getInt("cd_infracao"),
							rs.getString("ds_infracao"),
							rs.getInt("nr_pontuacao"),
							rs.getInt("nr_cod_detran"),
							rs.getFloat("nr_valor_ufir"),
							rs.getString("nm_natureza"),
							rs.getString("nr_artigo"),
							rs.getString("nr_paragrafo"),
							rs.getString("nr_inciso"),
							rs.getString("nr_alinea"),
							rs.getInt("tp_competencia"),
							rs.getFloat("vl_infracao"),
							rs.getInt("lg_prioritaria"));
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
	 * Retorna a infracao vigente
	 * @param cdInfracao
	 * @param connect
	 * @return
	 */
	public static Infracao getVigenteByNrCodDetran(int nrCodDetran, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
						
			if(lgBaseAntiga)
				pstmt = connect.prepareStatement("SELECT * FROM infracao WHERE nr_cod_detran=? AND dt_fim_vigencia IS NULL");
			else
				pstmt = connect.prepareStatement("SELECT * FROM str_infracao WHERE nr_cod_detran=? AND dt_fim_vigencia IS NULL");
			
			pstmt.setInt(1, nrCodDetran);
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(lgBaseAntiga)
					return new Infracao(rs.getInt("cod_infracao"),
						rs.getString("ds_infracao"),
						rs.getInt("nr_pontuacao"),
						rs.getInt("nr_cod_detran"),
						rs.getFloat("nr_valor_ufir"),
						rs.getString("nm_natureza"),
						rs.getString("nr_artigo"),
						rs.getString("nr_paragrafo"),
						rs.getString("nr_inciso"),
						rs.getString("nr_alinea"),
						rs.getInt("tp_competencia"),
						rs.getFloat("vl_infracao"),
						rs.getInt("lg_prioritario"));
				else
					return new Infracao(rs.getInt("cd_infracao"),
							rs.getString("ds_infracao"),
							rs.getInt("nr_pontuacao"),
							rs.getInt("nr_cod_detran"),
							rs.getFloat("nr_valor_ufir"),
							rs.getString("nm_natureza"),
							rs.getString("nr_artigo"),
							rs.getString("nr_paragrafo"),
							rs.getString("nr_inciso"),
							rs.getString("nr_alinea"),
							rs.getInt("tp_competencia"),
							rs.getFloat("vl_infracao"),
							rs.getInt("lg_prioritaria"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoServices.getVigenteByNrCodDetran: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoServices.getVigenteByNrCodDetran: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	/**
	 * Exporta o conteúdo de STR_INFRACAO para um arquivo com comandos insert SQL.
	 * @param fileName Nome e caminho do arquivo a ser gerado
	 * @return Result Resultado da operação
	 */
	public static Result exportToSQLFile(String fileName) {
		try {
			
			ResultSetMap rsm = getAll();
			
			RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
			
			while(rsm.next()) {
				raf.writeBytes("INSERT INTO STR_INFRACAO (cd_infracao, ds_infracao, nr_pontuacao, nr_cod_detran, nr_valor_ufir, nm_natureza, nr_artigo, nr_paragrafo, nr_inciso, nr_alinea, tp_competencia, lg_prioritario) "
						+ " VALUES ("+rsm.getInt("CD_INFRACAO")+", " +
								      "'"+rsm.getString("DS_INFRACAO")+"', " +
								      rsm.getInt("NR_PONTUACAO")+", " +
								      rsm.getInt("NR_COD_DETRAN")+", " +
								      rsm.getFloat("NR_VALOR_UFIR")+", " +
								      "'"+rsm.getString("NM_NATUREZA")+"', " +
								      "'"+rsm.getString("NR_ARTIGO")+"', " +
								      "'"+rsm.getString("NR_PARAGRAFO")+"', " +
								      "'"+rsm.getString("NR_INCISO")+"', " +
								      "'"+rsm.getString("NR_ALINEA")+"', " +
								      rsm.getInt("TP_COMPETENCIA")+", " +
								      rsm.getInt("LG_PRIORITARIO")+
						");\n");
			}
			
			raf.close();
			
			return new Result(1, "Dados de infração exportados com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao exportar dados de infração.");
		}
	}
	
}