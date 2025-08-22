package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;

public class AtributoGrupoProdutoDAO{

	public static int insert(AtributoGrupoProduto objeto) {
		return insert(objeto, null);
	}

	public static int insert(AtributoGrupoProduto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int code = Conexao.getSequenceCode("grl_atributo_grupo_produto", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO grl_atributo_grupo_produto (cd_produto_atributo,"+
			                                  "cd_produto_servico,"+
			                                  "cd_empresa,"+
			                                  "txt_atributo) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2, code);
			pstmt.setInt(3, code);
			pstmt.setString(4,objeto.getTxtAtributo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtributoGrupoProdutoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtributoGrupoProdutoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AtributoGrupoProduto objeto) {
		return update(objeto, null);
	}

	public static int update(AtributoGrupoProduto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE grl_atributo_grupo_produto SET txt_atributo=? WHERE cd_produto_atributo=? AND cd_produto_servico=? AND cd_empresa=?");
			pstmt.setString(1,objeto.getTxtAtributo());
			pstmt.setInt(2,objeto.getCdProdutoAtributo());
			pstmt.setInt(3,objeto.getCdProdutoServico());
			pstmt.setInt(4,objeto.getCdEmpresa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtributoGrupoProdutoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtributoGrupoProdutoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProdutoAtributo, int cdProdutoServico, int cdEmpresa) {
		return delete(cdProdutoAtributo, cdProdutoServico, cdEmpresa, null);
	}

	public static int delete(int cdProdutoAtributo, int cdProdutoServico, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM grl_atributo_grupo_produto WHERE cd_produto_atributo=? AND cd_produto_servico=? AND cd_empresa=?");
			pstmt.setInt(1, cdProdutoAtributo);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdEmpresa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtributoGrupoProdutoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtributoGrupoProdutoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AtributoGrupoProduto get(int cdProdutoAtributo, int cdProdutoServico, int cdEmpresa) {
		return get(cdProdutoAtributo, cdProdutoServico, cdEmpresa, null);
	}

	public static AtributoGrupoProduto get(int cdProdutoAtributo, int cdProdutoServico, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_atributo_grupo_produto WHERE cd_produto_atributo=? AND cd_produto_servico=? AND cd_empresa=?");
			pstmt.setInt(1, cdProdutoAtributo);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdEmpresa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AtributoGrupoProduto(rs.getInt("cd_produto_atributo"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_empresa"),
						rs.getString("txt_atributo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtributoGrupoProdutoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtributoGrupoProdutoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_atributo_grupo_produto");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtributoGrupoProdutoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtributoGrupoProdutoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_atributo_grupo_produto", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
