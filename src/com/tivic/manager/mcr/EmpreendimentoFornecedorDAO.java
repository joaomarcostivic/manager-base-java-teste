package com.tivic.manager.mcr;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class EmpreendimentoFornecedorDAO{

	public static int insert(EmpreendimentoFornecedor objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(EmpreendimentoFornecedor objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_empreendimento");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdEmpreendimento()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_fornecedor");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("mcr_empreendimento_fornecedor", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO mcr_empreendimento_fornecedor (cd_empreendimento,"+
			                                  "cd_fornecedor,"+
			                                  "nm_fornecedor,"+
			                                  "nr_frequencia_compra,"+
			                                  "txt_fidelidade,"+
			                                  "nm_tipo_produto) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEmpreendimento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpreendimento());
			pstmt.setInt(2, code);
			pstmt.setString(3,objeto.getNmFornecedor());
			pstmt.setInt(4,objeto.getNrFrequenciaCompra());
			pstmt.setString(5,objeto.getTxtFidelidade());
			pstmt.setString(6,objeto.getNmTipoProduto());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoFornecedorDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoFornecedorDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EmpreendimentoFornecedor objeto) {
		return update(objeto, null);
	}

	public static int update(EmpreendimentoFornecedor objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE mcr_empreendimento_fornecedor SET nm_fornecedor=?,"+
			                                  "nr_frequencia_compra=?,"+
			                                  "txt_fidelidade=?,"+
			                                  "nm_tipo_produto=? WHERE cd_empreendimento=? AND cd_fornecedor=?");
			pstmt.setString(1,objeto.getNmFornecedor());
			pstmt.setInt(2,objeto.getNrFrequenciaCompra());
			pstmt.setString(3,objeto.getTxtFidelidade());
			pstmt.setString(4,objeto.getNmTipoProduto());
			pstmt.setInt(5,objeto.getCdEmpreendimento());
			pstmt.setInt(6,objeto.getCdFornecedor());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoFornecedorDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoFornecedorDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpreendimento, int cdFornecedor) {
		return delete(cdEmpreendimento, cdFornecedor, null);
	}

	public static int delete(int cdEmpreendimento, int cdFornecedor, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM mcr_empreendimento_fornecedor WHERE cd_empreendimento=? AND cd_fornecedor=?");
			pstmt.setInt(1, cdEmpreendimento);
			pstmt.setInt(2, cdFornecedor);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoFornecedorDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoFornecedorDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EmpreendimentoFornecedor get(int cdEmpreendimento, int cdFornecedor) {
		return get(cdEmpreendimento, cdFornecedor, null);
	}

	public static EmpreendimentoFornecedor get(int cdEmpreendimento, int cdFornecedor, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendimento_fornecedor WHERE cd_empreendimento=? AND cd_fornecedor=?");
			pstmt.setInt(1, cdEmpreendimento);
			pstmt.setInt(2, cdFornecedor);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EmpreendimentoFornecedor(rs.getInt("cd_empreendimento"),
						rs.getInt("cd_fornecedor"),
						rs.getString("nm_fornecedor"),
						rs.getInt("nr_frequencia_compra"),
						rs.getString("txt_fidelidade"),
						rs.getString("nm_tipo_produto"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoFornecedorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoFornecedorDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendimento_fornecedor");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoFornecedorDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoFornecedorDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM mcr_empreendimento_fornecedor", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
