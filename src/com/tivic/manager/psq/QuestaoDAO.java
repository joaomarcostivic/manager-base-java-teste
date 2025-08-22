package com.tivic.manager.psq;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class QuestaoDAO{

	public static int insert(Questao objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(Questao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_questao");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_questionario");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdQuestionario()));
			int code = Conexao.getSequenceCode("psq_questao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdQuestao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO psq_questao (cd_questao,"+
			                                  "cd_questionario,"+
			                                  "cd_tema,"+
			                                  "txt_questao,"+
			                                  "ds_questao,"+
			                                  "tp_questao,"+
			                                  "lg_exclusivo,"+
			                                  "nr_ordem,"+
			                                  "vl_total_soma,"+
			                                  "lg_ajuda,"+
			                                  "txt_ajuda,"+
			                                  "ds_dica,"+
			                                  "lg_quebra_pagina,"+
			                                  "lg_separador,"+
			                                  "id_questao,"+
			                                  "lg_obrigatoria,"+
			                                  "lg_observacao,"+
			                                  "tp_apresentacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdQuestionario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdQuestionario());
			if(objeto.getCdTema()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTema());
			pstmt.setString(4,objeto.getTxtQuestao());
			pstmt.setString(5,objeto.getDsQuestao());
			pstmt.setInt(6,objeto.getTpQuestao());
			pstmt.setInt(7,objeto.getLgExclusivo());
			pstmt.setInt(8,objeto.getNrOrdem());
			pstmt.setFloat(9,objeto.getVlTotalSoma());
			pstmt.setInt(10,objeto.getLgAjuda());
			pstmt.setString(11,objeto.getTxtAjuda());
			pstmt.setString(12,objeto.getDsDica());
			pstmt.setInt(13,objeto.getLgQuebraPagina());
			pstmt.setInt(14,objeto.getLgSeparador());
			pstmt.setString(15,objeto.getIdQuestao());
			pstmt.setInt(16,objeto.getLgObrigatoria());
			pstmt.setInt(17,objeto.getLgObservacao());
			pstmt.setInt(18,objeto.getTpApresentacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuestaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! QuestaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Questao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Questao objeto, int cdQuestaoOld, int cdQuestionarioOld) {
		return update(objeto, cdQuestaoOld, cdQuestionarioOld, null);
	}

	public static int update(Questao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Questao objeto, int cdQuestaoOld, int cdQuestionarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE psq_questao SET cd_questao=?,"+
												      		   "cd_questionario=?,"+
												      		   "cd_tema=?,"+
												      		   "txt_questao=?,"+
												      		   "ds_questao=?,"+
												      		   "tp_questao=?,"+
												      		   "lg_exclusivo=?,"+
												      		   "nr_ordem=?,"+
												      		   "vl_total_soma=?,"+
												      		   "lg_ajuda=?,"+
												      		   "txt_ajuda=?,"+
												      		   "ds_dica=?,"+
												      		   "lg_quebra_pagina=?,"+
												      		   "lg_separador=?,"+
												      		   "id_questao=?,"+
												      		   "lg_obrigatoria=?,"+
												      		   "lg_observacao=?,"+
												      		   "tp_apresentacao=? WHERE cd_questao=? AND cd_questionario=?");
			pstmt.setInt(1,objeto.getCdQuestao());
			pstmt.setInt(2,objeto.getCdQuestionario());
			if(objeto.getCdTema()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTema());
			pstmt.setString(4,objeto.getTxtQuestao());
			pstmt.setString(5,objeto.getDsQuestao());
			pstmt.setInt(6,objeto.getTpQuestao());
			pstmt.setInt(7,objeto.getLgExclusivo());
			pstmt.setInt(8,objeto.getNrOrdem());
			pstmt.setFloat(9,objeto.getVlTotalSoma());
			pstmt.setInt(10,objeto.getLgAjuda());
			pstmt.setString(11,objeto.getTxtAjuda());
			pstmt.setString(12,objeto.getDsDica());
			pstmt.setInt(13,objeto.getLgQuebraPagina());
			pstmt.setInt(14,objeto.getLgSeparador());
			pstmt.setString(15,objeto.getIdQuestao());
			pstmt.setInt(16,objeto.getLgObrigatoria());
			pstmt.setInt(17,objeto.getLgObservacao());
			pstmt.setInt(18,objeto.getTpApresentacao());
			pstmt.setInt(19, cdQuestaoOld!=0 ? cdQuestaoOld : objeto.getCdQuestao());
			pstmt.setInt(20, cdQuestionarioOld!=0 ? cdQuestionarioOld : objeto.getCdQuestionario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuestaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! QuestaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdQuestao, int cdQuestionario) {
		return delete(cdQuestao, cdQuestionario, null);
	}

	public static int delete(int cdQuestao, int cdQuestionario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM psq_questao WHERE cd_questao=? AND cd_questionario=?");
			pstmt.setInt(1, cdQuestao);
			pstmt.setInt(2, cdQuestionario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuestaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! QuestaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Questao get(int cdQuestao, int cdQuestionario) {
		return get(cdQuestao, cdQuestionario, null);
	}

	public static Questao get(int cdQuestao, int cdQuestionario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM psq_questao WHERE cd_questao=? AND cd_questionario=?");
			pstmt.setInt(1, cdQuestao);
			pstmt.setInt(2, cdQuestionario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Questao(rs.getInt("cd_questao"),
						rs.getInt("cd_questionario"),
						rs.getInt("cd_tema"),
						rs.getString("txt_questao"),
						rs.getString("ds_questao"),
						rs.getInt("tp_questao"),
						rs.getInt("lg_exclusivo"),
						rs.getInt("nr_ordem"),
						rs.getFloat("vl_total_soma"),
						rs.getInt("lg_ajuda"),
						rs.getString("txt_ajuda"),
						rs.getString("ds_dica"),
						rs.getInt("lg_quebra_pagina"),
						rs.getInt("lg_separador"),
						rs.getString("id_questao"),
						rs.getInt("lg_obrigatoria"),
						rs.getInt("lg_observacao"),
						rs.getInt("tp_apresentacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuestaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuestaoDAO.get: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM psq_questao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuestaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuestaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM psq_questao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
