package com.tivic.manager.bpm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class BemDAO{

	public static int insert(Bem objeto) {
		return insert(objeto, null);
	}

	public static int insert(Bem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = com.tivic.manager.grl.ProdutoServicoDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdProdutoServico(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO bpm_bem (cd_bem,"+
			                                  "cd_classificacao,"+
			                                  "pr_depreciacao) VALUES (?, ?, ?)");
			
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdProdutoServico());
			if(objeto.getCdClassificacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdClassificacao());
			pstmt.setFloat(3,objeto.getPrDepreciacao());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BemDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BemDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Bem objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Bem objeto, int cdProdutoServicoOld) {
		return update(objeto, cdProdutoServicoOld, null);
	}

	public static int update(Bem objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Bem objeto, int cdProdutoServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			Bem objetoTemp = get(objeto.getCdProdutoServico(), connect);
			if (objetoTemp == null)
				pstmt = connect.prepareStatement("INSERT INTO bpm_bem (cd_bem,"+
			                                  "cd_classificacao,"+
			                                  "pr_depreciacao) VALUES (?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE bpm_bem SET cd_bem=?,"+
												      		   "cd_classificacao=?,"+
												      		   "pr_depreciacao=? WHERE cd_bem=?");
			pstmt.setInt(1,objeto.getCdProdutoServico());
			if(objeto.getCdClassificacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdClassificacao());
			pstmt.setFloat(3,objeto.getPrDepreciacao());
			if (objetoTemp != null) {
				pstmt.setInt(4, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			}
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.ProdutoServicoDAO.update(objeto, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BemDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BemDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProdutoServico) {
		return delete(cdProdutoServico, null);
	}

	public static int delete(int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM bpm_bem WHERE cd_bem=?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.ProdutoServicoDAO.delete(cdProdutoServico, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BemDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BemDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Bem get(int cdProdutoServico) {
		return get(cdProdutoServico, null);
	}

	public static Bem get(int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM bpm_bem A, grl_produto_servico B WHERE A.cd_bem=B.cd_produto_servico AND A.cd_bem=?");
			pstmt.setInt(1, cdProdutoServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Bem(rs.getInt("cd_bem"),
						rs.getInt("cd_categoria_economica"),
						rs.getString("nm_produto_servico"),
						rs.getString("txt_produto_servico"),
						rs.getString("txt_especificacao"),
						rs.getString("txt_dado_tecnico"),
						rs.getString("txt_prazo_entrega"),
						rs.getInt("tp_produto_servico"),
						rs.getString("id_produto_servico"),
						rs.getString("sg_produto_servico"),
						rs.getInt("cd_classificacao_fiscal"),
						rs.getInt("cd_fabricante"),
						rs.getInt("cd_marca"),
						rs.getString("nm_modelo"),
						rs.getInt("cd_ncm"),
						rs.getString("nr_referencia"),
						rs.getInt("cd_classificacao"),
						rs.getFloat("pr_depreciacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM bpm_bem");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BemDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM bpm_bem", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
