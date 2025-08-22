package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class OfertaAvaliacaoQuestaoDAO{

	public static int insert(OfertaAvaliacaoQuestao objeto) {
		return insert(objeto, null);
	}

	public static int insert(OfertaAvaliacaoQuestao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_oferta_avaliacao_questao");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_oferta_avaliacao");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdOfertaAvaliacao()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_oferta");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdOferta()));
			int code = Conexao.getSequenceCode("acd_oferta_avaliacao_questao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOfertaAvaliacaoQuestao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_oferta_avaliacao_questao (cd_oferta_avaliacao_questao,"+
			                                  "cd_oferta_avaliacao,"+
			                                  "cd_oferta,"+
			                                  "nr_ordem,"+
			                                  "txt_questao,"+
			                                  "tp_questao,"+
			                                  "vl_peso,"+
			                                  "cd_oferta_avaliacao_questao_superior) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdOfertaAvaliacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdOfertaAvaliacao());
			if(objeto.getCdOferta()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdOferta());
			pstmt.setInt(4,objeto.getNrOrdem());
			pstmt.setString(5,objeto.getTxtQuestao());
			pstmt.setInt(6,objeto.getTpQuestao());
			pstmt.setDouble(7,objeto.getVlPeso());
			if(objeto.getCdOfertaAvaliacaoQuestaoSuperior()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdOfertaAvaliacaoQuestaoSuperior());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OfertaAvaliacaoQuestao objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(OfertaAvaliacaoQuestao objeto, int cdOfertaAvaliacaoQuestaoOld, int cdOfertaAvaliacaoOld, int cdOfertaOld) {
		return update(objeto, cdOfertaAvaliacaoQuestaoOld, cdOfertaAvaliacaoOld, cdOfertaOld, null);
	}

	public static int update(OfertaAvaliacaoQuestao objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(OfertaAvaliacaoQuestao objeto, int cdOfertaAvaliacaoQuestaoOld, int cdOfertaAvaliacaoOld, int cdOfertaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_oferta_avaliacao_questao SET cd_oferta_avaliacao_questao=?,"+
												      		   "cd_oferta_avaliacao=?,"+
												      		   "cd_oferta=?,"+
												      		   "nr_ordem=?,"+
												      		   "txt_questao=?,"+
												      		   "tp_questao=?,"+
												      		   "vl_peso=?,"+
												      		   "cd_oferta_avaliacao_questao_superior=? WHERE cd_oferta_avaliacao_questao=? AND cd_oferta_avaliacao=? AND cd_oferta=?");
			pstmt.setInt(1,objeto.getCdOfertaAvaliacaoQuestao());
			if(objeto.getCdOfertaAvaliacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdOfertaAvaliacao());
			if(objeto.getCdOferta()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdOferta());
			pstmt.setInt(4,objeto.getNrOrdem());
			pstmt.setString(5,objeto.getTxtQuestao());
			pstmt.setInt(6,objeto.getTpQuestao());
			pstmt.setDouble(7,objeto.getVlPeso());
			if(objeto.getCdOfertaAvaliacaoQuestaoSuperior()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdOfertaAvaliacaoQuestaoSuperior());
			pstmt.setInt(9, cdOfertaAvaliacaoQuestaoOld!=0 ? cdOfertaAvaliacaoQuestaoOld : objeto.getCdOfertaAvaliacaoQuestao());
			pstmt.setInt(10, cdOfertaAvaliacaoOld!=0 ? cdOfertaAvaliacaoOld : objeto.getCdOfertaAvaliacao());
			pstmt.setInt(11, cdOfertaOld!=0 ? cdOfertaOld : objeto.getCdOferta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta) {
		return delete(cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, null);
	}

	public static int delete(int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_oferta_avaliacao_questao WHERE cd_oferta_avaliacao_questao=? AND cd_oferta_avaliacao=? AND cd_oferta=?");
			pstmt.setInt(1, cdOfertaAvaliacaoQuestao);
			pstmt.setInt(2, cdOfertaAvaliacao);
			pstmt.setInt(3, cdOferta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OfertaAvaliacaoQuestao get(int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta) {
		return get(cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, null);
	}

	public static OfertaAvaliacaoQuestao get(int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta_avaliacao_questao WHERE cd_oferta_avaliacao_questao=? AND cd_oferta_avaliacao=? AND cd_oferta=?");
			pstmt.setInt(1, cdOfertaAvaliacaoQuestao);
			pstmt.setInt(2, cdOfertaAvaliacao);
			pstmt.setInt(3, cdOferta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OfertaAvaliacaoQuestao(rs.getInt("cd_oferta_avaliacao_questao"),
						rs.getInt("cd_oferta_avaliacao"),
						rs.getInt("cd_oferta"),
						rs.getInt("nr_ordem"),
						rs.getString("txt_questao"),
						rs.getInt("tp_questao"),
						rs.getDouble("vl_peso"),
						rs.getInt("cd_oferta_avaliacao_questao_superior"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta_avaliacao_questao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OfertaAvaliacaoQuestao> getList() {
		return getList(null);
	}

	public static ArrayList<OfertaAvaliacaoQuestao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OfertaAvaliacaoQuestao> list = new ArrayList<OfertaAvaliacaoQuestao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OfertaAvaliacaoQuestao obj = OfertaAvaliacaoQuestaoDAO.get(rsm.getInt("cd_oferta_avaliacao_questao"), rsm.getInt("cd_oferta_avaliacao"), rsm.getInt("cd_oferta"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_oferta_avaliacao_questao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}