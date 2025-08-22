package com.tivic.manager.crt;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.manager.conexao.*;
import com.tivic.sol.connection.Conexao;

public class ProdutoDAO{

	public static int insert(Produto objeto) {
		return insert(objeto, null);
	}

	public static int insert(Produto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int code = Conexao.getSequenceCode("SCE_PRODUTO");
			pstmt = connect.prepareStatement("INSERT INTO SCE_PRODUTO (CD_PRODUTO,"+
			                                  "NM_PRODUTO,"+
			                                  "CD_ORGAO,"+
			                                  "CD_INSTITUICAO_FINANCEIRA,"+
			                                  "PR_MARGEM,"+
			                                  "LG_TABELA_UNICA,"+
			                                  "ST_PRODUTO) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmProduto());
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdOrgao());
			if(objeto.getCdInstituicaoFinanceira()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdInstituicaoFinanceira());
			pstmt.setFloat(5, objeto.getPrMargem());
			pstmt.setInt(6,objeto.getLgTabelaUnica());
			pstmt.setInt(7,objeto.getStProduto());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Produto objeto) {
		return update(objeto, null);
	}

	public static int update(Produto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE SCE_PRODUTO SET NM_PRODUTO=?,"+
			                                  "CD_ORGAO=?,"+
			                                  "CD_INSTITUICAO_FINANCEIRA=?,"+
			                                  "PR_MARGEM=?, "+
			                                  "LG_TABELA_UNICA=?,"+
			                                  "ST_PRODUTO=? WHERE CD_PRODUTO=?");
			pstmt.setString(1,objeto.getNmProduto());
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdOrgao());
			if(objeto.getCdInstituicaoFinanceira()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdInstituicaoFinanceira());
			pstmt.setFloat(4,objeto.getPrMargem());
			pstmt.setInt(5,objeto.getLgTabelaUnica());
			pstmt.setInt(6,objeto.getStProduto());
			pstmt.setInt(7,objeto.getCdProduto());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProduto) {
		return delete(cdProduto, null);
	}

	public static int delete(int cdProduto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM SCE_PRODUTO WHERE CD_PRODUTO=?");
			pstmt.setInt(1, cdProduto);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Produto get(int cdProduto) {
		return get(cdProduto, null);
	}

	public static Produto get(int cdProduto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM SCE_PRODUTO WHERE CD_PRODUTO=?");
			pstmt.setInt(1, cdProduto);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Produto(rs.getInt("CD_PRODUTO"),
						rs.getString("NM_PRODUTO"),
						rs.getInt("CD_ORGAO"),
						rs.getInt("CD_INSTITUICAO_FINANCEIRA"),
						rs.getFloat("PR_MARGEM"),
						rs.getInt("LG_TABELA_UNICA"),
						rs.getInt("ST_PRODUTO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM SCE_PRODUTO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT * FROM SCE_PRODUTO", criterios, Conexao.conectar());
	}

}