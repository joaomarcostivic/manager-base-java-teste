package com.tivic.manager.psq;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class QuestaoValorDAO{

	public static int insert(QuestaoValor objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(QuestaoValor objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_questao_valor");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_questao");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdQuestao()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_questionario");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdQuestionario()));
			int code = Conexao.getSequenceCode("psq_questao_valor", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdQuestaoValor(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO psq_questao_valor (cd_questao_valor,"+
			                                  "cd_questao,"+
			                                  "cd_questionario,"+
			                                  "cd_biblioteca_recurso,"+
			                                  "tp_dado,"+
			                                  "blb_valor,"+
			                                  "txt_apresentacao,"+
			                                  "nr_ordem,"+
			                                  "lg_ajuda,"+
			                                  "txt_ajuda,"+
			                                  "ds_dica,"+
			                                  "vl_peso) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdQuestao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdQuestao());
			if(objeto.getCdQuestionario()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdQuestionario());
			if(objeto.getCdBibliotecaRecurso()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdBibliotecaRecurso());
			pstmt.setInt(5,objeto.getTpDado());
			if(objeto.getBlbValor()==null)
				pstmt.setNull(6, Types.BINARY);
			else
				pstmt.setBytes(6,objeto.getBlbValor());
			pstmt.setString(7,objeto.getTxtApresentacao());
			pstmt.setInt(8,objeto.getNrOrdem());
			pstmt.setInt(9,objeto.getLgAjuda());
			pstmt.setString(10,objeto.getTxtAjuda());
			pstmt.setString(11,objeto.getDsDica());
			pstmt.setFloat(12,objeto.getVlPeso());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuestaoValorDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! QuestaoValorDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(QuestaoValor objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(QuestaoValor objeto, int cdQuestaoValorOld, int cdQuestaoOld, int cdQuestionarioOld) {
		return update(objeto, cdQuestaoValorOld, cdQuestaoOld, cdQuestionarioOld, null);
	}

	public static int update(QuestaoValor objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(QuestaoValor objeto, int cdQuestaoValorOld, int cdQuestaoOld, int cdQuestionarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE psq_questao_valor SET cd_questao_valor=?,"+
												      		   "cd_questao=?,"+
												      		   "cd_questionario=?,"+
												      		   "cd_biblioteca_recurso=?,"+
												      		   "tp_dado=?,"+
												      		   "blb_valor=?,"+
												      		   "txt_apresentacao=?,"+
												      		   "nr_ordem=?,"+
												      		   "lg_ajuda=?,"+
												      		   "txt_ajuda=?,"+
												      		   "ds_dica=?,"+
												      		   "vl_peso=? WHERE cd_questao_valor=? AND cd_questao=? AND cd_questionario=?");
			pstmt.setInt(1,objeto.getCdQuestaoValor());
			pstmt.setInt(2,objeto.getCdQuestao());
			pstmt.setInt(3,objeto.getCdQuestionario());
			if(objeto.getCdBibliotecaRecurso()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdBibliotecaRecurso());
			pstmt.setInt(5,objeto.getTpDado());
			if(objeto.getBlbValor()==null)
				pstmt.setNull(6, Types.BINARY);
			else
				pstmt.setBytes(6,objeto.getBlbValor());
			pstmt.setString(7,objeto.getTxtApresentacao());
			pstmt.setInt(8,objeto.getNrOrdem());
			pstmt.setInt(9,objeto.getLgAjuda());
			pstmt.setString(10,objeto.getTxtAjuda());
			pstmt.setString(11,objeto.getDsDica());
			pstmt.setFloat(12,objeto.getVlPeso());
			pstmt.setInt(13, cdQuestaoValorOld!=0 ? cdQuestaoValorOld : objeto.getCdQuestaoValor());
			pstmt.setInt(14, cdQuestaoOld!=0 ? cdQuestaoOld : objeto.getCdQuestao());
			pstmt.setInt(15, cdQuestionarioOld!=0 ? cdQuestionarioOld : objeto.getCdQuestionario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuestaoValorDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! QuestaoValorDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdQuestaoValor, int cdQuestao, int cdQuestionario) {
		return delete(cdQuestaoValor, cdQuestao, cdQuestionario, null);
	}

	public static int delete(int cdQuestaoValor, int cdQuestao, int cdQuestionario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM psq_questao_valor WHERE cd_questao_valor=? AND cd_questao=? AND cd_questionario=?");
			pstmt.setInt(1, cdQuestaoValor);
			pstmt.setInt(2, cdQuestao);
			pstmt.setInt(3, cdQuestionario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuestaoValorDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! QuestaoValorDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static QuestaoValor get(int cdQuestaoValor, int cdQuestao, int cdQuestionario) {
		return get(cdQuestaoValor, cdQuestao, cdQuestionario, null);
	}

	public static QuestaoValor get(int cdQuestaoValor, int cdQuestao, int cdQuestionario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM psq_questao_valor WHERE cd_questao_valor=? AND cd_questao=? AND cd_questionario=?");
			pstmt.setInt(1, cdQuestaoValor);
			pstmt.setInt(2, cdQuestao);
			pstmt.setInt(3, cdQuestionario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new QuestaoValor(rs.getInt("cd_questao_valor"),
						rs.getInt("cd_questao"),
						rs.getInt("cd_questionario"),
						rs.getInt("cd_biblioteca_recurso"),
						rs.getInt("tp_dado"),
						rs.getBytes("blb_valor")==null?null:rs.getBytes("blb_valor"),
						rs.getString("txt_apresentacao"),
						rs.getInt("nr_ordem"),
						rs.getInt("lg_ajuda"),
						rs.getString("txt_ajuda"),
						rs.getString("ds_dica"),
						rs.getFloat("vl_peso"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuestaoValorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuestaoValorDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM psq_questao_valor");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuestaoValorDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuestaoValorDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM psq_questao_valor", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
