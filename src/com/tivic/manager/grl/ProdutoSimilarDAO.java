package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ProdutoSimilarDAO{

	public static int insert(ProdutoSimilar objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProdutoSimilar objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_produto_similar (cd_produto_servico,"+
			                                  "cd_similar,"+
			                                  "lg_referenciado,"+
			                                  "nm_produto_similar,"+
			                                  "txt_descricao,"+
			                                  "txt_especificacao) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdProdutoServico());
			if(objeto.getCdSimilar()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdSimilar());
			pstmt.setInt(3,objeto.getLgReferenciado());
			pstmt.setString(4,objeto.getNmProdutoSimilar());
			pstmt.setString(5,objeto.getTxtDescricao());
			pstmt.setString(6,objeto.getTxtEspecificacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoSimilarDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoSimilarDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProdutoSimilar objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ProdutoSimilar objeto, int cdProdutoServicoOld, int cdSimilarOld) {
		return update(objeto, cdProdutoServicoOld, cdSimilarOld, null);
	}

	public static int update(ProdutoSimilar objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ProdutoSimilar objeto, int cdProdutoServicoOld, int cdSimilarOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_produto_similar SET cd_produto_servico=?,"+
												      		   "cd_similar=?,"+
												      		   "lg_referenciado=?,"+
												      		   "nm_produto_similar=?,"+
												      		   "txt_descricao=?,"+
												      		   "txt_especificacao=? WHERE cd_produto_servico=? AND cd_similar=?");
			pstmt.setInt(1,objeto.getCdProdutoServico());
			pstmt.setInt(2,objeto.getCdSimilar());
			pstmt.setInt(3,objeto.getLgReferenciado());
			pstmt.setString(4,objeto.getNmProdutoSimilar());
			pstmt.setString(5,objeto.getTxtDescricao());
			pstmt.setString(6,objeto.getTxtEspecificacao());
			pstmt.setInt(7, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.setInt(8, cdSimilarOld!=0 ? cdSimilarOld : objeto.getCdSimilar());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoSimilarDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoSimilarDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProdutoServico, int cdSimilar) {
		return delete(cdProdutoServico, cdSimilar, null);
	}

	public static int delete(int cdProdutoServico, int cdSimilar, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_produto_similar WHERE cd_produto_servico=? AND cd_similar=?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdSimilar);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoSimilarDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoSimilarDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProdutoSimilar get(int cdProdutoServico, int cdSimilar) {
		return get(cdProdutoServico, cdSimilar, null);
	}

	public static ProdutoSimilar get(int cdProdutoServico, int cdSimilar, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_produto_similar WHERE cd_produto_servico=? AND cd_similar=?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdSimilar);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProdutoSimilar(rs.getInt("cd_produto_servico"),
						rs.getInt("cd_similar"),
						rs.getInt("lg_referenciado"),
						rs.getString("nm_produto_similar"),
						rs.getString("txt_descricao"),
						rs.getString("txt_especificacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoSimilarDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoSimilarDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_produto_similar");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoSimilarDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoSimilarDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ProdutoSimilar> getList() {
		return getList(null);
	}

	public static ArrayList<ProdutoSimilar> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ProdutoSimilar> list = new ArrayList<ProdutoSimilar>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ProdutoSimilar obj = ProdutoSimilarDAO.get(rsm.getInt("cd_produto_servico"), rsm.getInt("cd_similar"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoSimilarDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_produto_similar", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
