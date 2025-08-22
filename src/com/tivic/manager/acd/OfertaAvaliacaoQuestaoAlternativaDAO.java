package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class OfertaAvaliacaoQuestaoAlternativaDAO{

	public static int insert(OfertaAvaliacaoQuestaoAlternativa objeto) {
		return insert(objeto, null);
	}

	public static int insert(OfertaAvaliacaoQuestaoAlternativa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[4];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_alternativa");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_oferta_avaliacao_questao");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdOfertaAvaliacaoQuestao()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_oferta_avaliacao");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdOfertaAvaliacao()));
			keys[3] = new HashMap<String,Object>();
			keys[3].put("FIELD_NAME", "cd_oferta");
			keys[3].put("IS_KEY_NATIVE", "NO");
			keys[3].put("FIELD_VALUE", new Integer(objeto.getCdOferta()));
			int code = Conexao.getSequenceCode("acd_oferta_avaliacao_questao_alternativa", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAlternativa(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_oferta_avaliacao_questao_alternativa (cd_alternativa,"+
			                                  "cd_oferta_avaliacao_questao,"+
			                                  "cd_oferta_avaliacao,"+
			                                  "cd_oferta,"+
			                                  "nr_ordem,"+
			                                  "txt_alternativa,"+
			                                  "lg_correta) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdOfertaAvaliacaoQuestao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdOfertaAvaliacaoQuestao());
			if(objeto.getCdOfertaAvaliacao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdOfertaAvaliacao());
			if(objeto.getCdOferta()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdOferta());
			pstmt.setInt(5,objeto.getNrOrdem());
			pstmt.setString(6,objeto.getTxtAlternativa());
			pstmt.setInt(7,objeto.getLgCorreta());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoAlternativaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoAlternativaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OfertaAvaliacaoQuestaoAlternativa objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(OfertaAvaliacaoQuestaoAlternativa objeto, int cdAlternativaOld, int cdOfertaAvaliacaoQuestaoOld, int cdOfertaAvaliacaoOld, int cdOfertaOld) {
		return update(objeto, cdAlternativaOld, cdOfertaAvaliacaoQuestaoOld, cdOfertaAvaliacaoOld, cdOfertaOld, null);
	}

	public static int update(OfertaAvaliacaoQuestaoAlternativa objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(OfertaAvaliacaoQuestaoAlternativa objeto, int cdAlternativaOld, int cdOfertaAvaliacaoQuestaoOld, int cdOfertaAvaliacaoOld, int cdOfertaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_oferta_avaliacao_questao_alternativa SET cd_alternativa=?,"+
												      		   "cd_oferta_avaliacao_questao=?,"+
												      		   "cd_oferta_avaliacao=?,"+
												      		   "cd_oferta=?,"+
												      		   "nr_ordem=?,"+
												      		   "txt_alternativa=? WHERE cd_alternativa=? AND cd_oferta_avaliacao_questao=? AND cd_oferta_avaliacao=? AND cd_oferta=?");
			pstmt.setInt(1,objeto.getCdAlternativa());
			pstmt.setInt(2,objeto.getCdOfertaAvaliacaoQuestao());
			pstmt.setInt(3,objeto.getCdOfertaAvaliacao());
			pstmt.setInt(4,objeto.getCdOferta());
			pstmt.setInt(5,objeto.getNrOrdem());
			pstmt.setString(6,objeto.getTxtAlternativa());
			pstmt.setInt(7,objeto.getLgCorreta());
			pstmt.setInt(8, cdAlternativaOld!=0 ? cdAlternativaOld : objeto.getCdAlternativa());
			pstmt.setInt(9, cdOfertaAvaliacaoQuestaoOld!=0 ? cdOfertaAvaliacaoQuestaoOld : objeto.getCdOfertaAvaliacaoQuestao());
			pstmt.setInt(10, cdOfertaAvaliacaoOld!=0 ? cdOfertaAvaliacaoOld : objeto.getCdOfertaAvaliacao());
			pstmt.setInt(11, cdOfertaOld!=0 ? cdOfertaOld : objeto.getCdOferta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoAlternativaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoAlternativaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAlternativa, int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta) {
		return delete(cdAlternativa, cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, null);
	}

	public static int delete(int cdAlternativa, int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_oferta_avaliacao_questao_alternativa WHERE cd_alternativa=? AND cd_oferta_avaliacao_questao=? AND cd_oferta_avaliacao=? AND cd_oferta=?");
			pstmt.setInt(1, cdAlternativa);
			pstmt.setInt(2, cdOfertaAvaliacaoQuestao);
			pstmt.setInt(3, cdOfertaAvaliacao);
			pstmt.setInt(4, cdOferta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoAlternativaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoAlternativaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OfertaAvaliacaoQuestaoAlternativa get(int cdAlternativa, int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta) {
		return get(cdAlternativa, cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, null);
	}

	public static OfertaAvaliacaoQuestaoAlternativa get(int cdAlternativa, int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta_avaliacao_questao_alternativa WHERE cd_alternativa=? AND cd_oferta_avaliacao_questao=? AND cd_oferta_avaliacao=? AND cd_oferta=?");
			pstmt.setInt(1, cdAlternativa);
			pstmt.setInt(2, cdOfertaAvaliacaoQuestao);
			pstmt.setInt(3, cdOfertaAvaliacao);
			pstmt.setInt(4, cdOferta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OfertaAvaliacaoQuestaoAlternativa(rs.getInt("cd_alternativa"),
						rs.getInt("cd_oferta_avaliacao_questao"),
						rs.getInt("cd_oferta_avaliacao"),
						rs.getInt("cd_oferta"),
						rs.getInt("nr_ordem"),
						rs.getString("txt_alternativa"),
						rs.getInt("lg_correta"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoAlternativaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoAlternativaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta_avaliacao_questao_alternativa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoAlternativaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoAlternativaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OfertaAvaliacaoQuestaoAlternativa> getList() {
		return getList(null);
	}

	public static ArrayList<OfertaAvaliacaoQuestaoAlternativa> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OfertaAvaliacaoQuestaoAlternativa> list = new ArrayList<OfertaAvaliacaoQuestaoAlternativa>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OfertaAvaliacaoQuestaoAlternativa obj = OfertaAvaliacaoQuestaoAlternativaDAO.get(rsm.getInt("cd_alternativa"), rsm.getInt("cd_oferta_avaliacao_questao"), rsm.getInt("cd_oferta_avaliacao"), rsm.getInt("cd_oferta"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoAlternativaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_oferta_avaliacao_questao_alternativa", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}