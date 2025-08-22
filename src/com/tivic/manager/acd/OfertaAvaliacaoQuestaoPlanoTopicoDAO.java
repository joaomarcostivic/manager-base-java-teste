package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class OfertaAvaliacaoQuestaoPlanoTopicoDAO{

	public static int insert(OfertaAvaliacaoQuestaoPlanoTopico objeto) {
		return insert(objeto, null);
	}

	public static int insert(OfertaAvaliacaoQuestaoPlanoTopico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_oferta_avaliacao_questao_plano_topico (cd_oferta_avaliacao_questao,"+
			                                  "cd_oferta_avaliacao,"+
			                                  "cd_oferta,"+
			                                  "cd_plano,"+
			                                  "cd_secao,"+
			                                  "cd_topico) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdOfertaAvaliacaoQuestao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdOfertaAvaliacaoQuestao());
			if(objeto.getCdOfertaAvaliacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdOfertaAvaliacao());
			if(objeto.getCdOferta()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdOferta());
			if(objeto.getCdPlano()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdPlano());
			if(objeto.getCdSecao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdSecao());
			if(objeto.getCdTopico()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTopico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoPlanoTopicoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoPlanoTopicoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OfertaAvaliacaoQuestaoPlanoTopico objeto) {
		return update(objeto, 0, 0, 0, 0, 0, 0, null);
	}

	public static int update(OfertaAvaliacaoQuestaoPlanoTopico objeto, int cdOfertaAvaliacaoQuestaoOld, int cdOfertaAvaliacaoOld, int cdOfertaOld, int cdPlanoOld, int cdSecaoOld, int cdTopicoOld) {
		return update(objeto, cdOfertaAvaliacaoQuestaoOld, cdOfertaAvaliacaoOld, cdOfertaOld, cdPlanoOld, cdSecaoOld, cdTopicoOld, null);
	}

	public static int update(OfertaAvaliacaoQuestaoPlanoTopico objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, 0, 0, connect);
	}

	public static int update(OfertaAvaliacaoQuestaoPlanoTopico objeto, int cdOfertaAvaliacaoQuestaoOld, int cdOfertaAvaliacaoOld, int cdOfertaOld, int cdPlanoOld, int cdSecaoOld, int cdTopicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_oferta_avaliacao_questao_plano_topico SET cd_oferta_avaliacao_questao=?,"+
												      		   "cd_oferta_avaliacao=?,"+
												      		   "cd_oferta=?,"+
												      		   "cd_plano=?,"+
												      		   "cd_secao=?,"+
												      		   "cd_topico=? WHERE cd_oferta_avaliacao_questao=? AND cd_oferta_avaliacao=? AND cd_oferta=? AND cd_plano=? AND cd_secao=? AND cd_topico=?");
			pstmt.setInt(1,objeto.getCdOfertaAvaliacaoQuestao());
			pstmt.setInt(2,objeto.getCdOfertaAvaliacao());
			pstmt.setInt(3,objeto.getCdOferta());
			pstmt.setInt(4,objeto.getCdPlano());
			pstmt.setInt(5,objeto.getCdSecao());
			pstmt.setInt(6,objeto.getCdTopico());
			pstmt.setInt(7, cdOfertaAvaliacaoQuestaoOld!=0 ? cdOfertaAvaliacaoQuestaoOld : objeto.getCdOfertaAvaliacaoQuestao());
			pstmt.setInt(8, cdOfertaAvaliacaoOld!=0 ? cdOfertaAvaliacaoOld : objeto.getCdOfertaAvaliacao());
			pstmt.setInt(9, cdOfertaOld!=0 ? cdOfertaOld : objeto.getCdOferta());
			pstmt.setInt(10, cdPlanoOld!=0 ? cdPlanoOld : objeto.getCdPlano());
			pstmt.setInt(11, cdSecaoOld!=0 ? cdSecaoOld : objeto.getCdSecao());
			pstmt.setInt(12, cdTopicoOld!=0 ? cdTopicoOld : objeto.getCdTopico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoPlanoTopicoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoPlanoTopicoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, int cdPlano, int cdSecao, int cdTopico) {
		return delete(cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, cdPlano, cdSecao, cdTopico, null);
	}

	public static int delete(int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, int cdPlano, int cdSecao, int cdTopico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_oferta_avaliacao_questao_plano_topico WHERE cd_oferta_avaliacao_questao=? AND cd_oferta_avaliacao=? AND cd_oferta=? AND cd_plano=? AND cd_secao=? AND cd_topico=?");
			pstmt.setInt(1, cdOfertaAvaliacaoQuestao);
			pstmt.setInt(2, cdOfertaAvaliacao);
			pstmt.setInt(3, cdOferta);
			pstmt.setInt(4, cdPlano);
			pstmt.setInt(5, cdSecao);
			pstmt.setInt(6, cdTopico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoPlanoTopicoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoPlanoTopicoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OfertaAvaliacaoQuestaoPlanoTopico get(int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, int cdPlano, int cdSecao, int cdTopico) {
		return get(cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, cdPlano, cdSecao, cdTopico, null);
	}

	public static OfertaAvaliacaoQuestaoPlanoTopico get(int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, int cdPlano, int cdSecao, int cdTopico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta_avaliacao_questao_plano_topico WHERE cd_oferta_avaliacao_questao=? AND cd_oferta_avaliacao=? AND cd_oferta=? AND cd_plano=? AND cd_secao=? AND cd_topico=?");
			pstmt.setInt(1, cdOfertaAvaliacaoQuestao);
			pstmt.setInt(2, cdOfertaAvaliacao);
			pstmt.setInt(3, cdOferta);
			pstmt.setInt(4, cdPlano);
			pstmt.setInt(5, cdSecao);
			pstmt.setInt(6, cdTopico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OfertaAvaliacaoQuestaoPlanoTopico(rs.getInt("cd_oferta_avaliacao_questao"),
						rs.getInt("cd_oferta_avaliacao"),
						rs.getInt("cd_oferta"),
						rs.getInt("cd_plano"),
						rs.getInt("cd_secao"),
						rs.getInt("cd_topico"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoPlanoTopicoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoPlanoTopicoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta_avaliacao_questao_plano_topico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoPlanoTopicoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoPlanoTopicoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OfertaAvaliacaoQuestaoPlanoTopico> getList() {
		return getList(null);
	}

	public static ArrayList<OfertaAvaliacaoQuestaoPlanoTopico> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OfertaAvaliacaoQuestaoPlanoTopico> list = new ArrayList<OfertaAvaliacaoQuestaoPlanoTopico>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OfertaAvaliacaoQuestaoPlanoTopico obj = OfertaAvaliacaoQuestaoPlanoTopicoDAO.get(rsm.getInt("cd_oferta_avaliacao_questao"), rsm.getInt("cd_oferta_avaliacao"), rsm.getInt("cd_oferta"), rsm.getInt("cd_plano"), rsm.getInt("cd_secao"), rsm.getInt("cd_topico"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoPlanoTopicoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_oferta_avaliacao_questao_plano_topico", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}