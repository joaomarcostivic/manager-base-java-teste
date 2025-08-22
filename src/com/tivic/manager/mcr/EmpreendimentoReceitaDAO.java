package com.tivic.manager.mcr;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class EmpreendimentoReceitaDAO{

	public static int insert(EmpreendimentoReceita objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(EmpreendimentoReceita objeto, Connection connect){
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
			keys[1].put("FIELD_NAME", "cd_receita");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("mcr_empreendimento_receita", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO mcr_empreendimento_receita (cd_empreendimento,"+
			                                  "cd_receita,"+
			                                  "nm_produto,"+
			                                  "lg_receita,"+
			                                  "vl_preco_unitario,"+
			                                  "qt_vendida,"+
			                                  "sg_unidade) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEmpreendimento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpreendimento());
			pstmt.setInt(2, code);
			pstmt.setString(3,objeto.getNmProduto());
			pstmt.setInt(4,objeto.getLgReceita());
			pstmt.setFloat(5,objeto.getVlPrecoUnitario());
			pstmt.setFloat(6,objeto.getQtVendida());
			pstmt.setString(7,objeto.getSgUnidade());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoReceitaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoReceitaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EmpreendimentoReceita objeto) {
		return update(objeto, null);
	}

	public static int update(EmpreendimentoReceita objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE mcr_empreendimento_receita SET nm_produto=?,"+
			                                  "lg_receita=?,"+
			                                  "vl_preco_unitario=?,"+
			                                  "qt_vendida=?,"+
			                                  "sg_unidade=? WHERE cd_empreendimento=? AND cd_receita=?");
			pstmt.setString(1,objeto.getNmProduto());
			pstmt.setInt(2,objeto.getLgReceita());
			pstmt.setFloat(3,objeto.getVlPrecoUnitario());
			pstmt.setFloat(4,objeto.getQtVendida());
			pstmt.setString(5,objeto.getSgUnidade());
			pstmt.setInt(6,objeto.getCdEmpreendimento());
			pstmt.setInt(7,objeto.getCdReceita());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoReceitaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoReceitaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpreendimento, int cdReceita) {
		return delete(cdEmpreendimento, cdReceita, null);
	}

	public static int delete(int cdEmpreendimento, int cdReceita, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM mcr_empreendimento_receita WHERE cd_empreendimento=? AND cd_receita=?");
			pstmt.setInt(1, cdEmpreendimento);
			pstmt.setInt(2, cdReceita);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoReceitaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoReceitaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EmpreendimentoReceita get(int cdEmpreendimento, int cdReceita) {
		return get(cdEmpreendimento, cdReceita, null);
	}

	public static EmpreendimentoReceita get(int cdEmpreendimento, int cdReceita, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendimento_receita WHERE cd_empreendimento=? AND cd_receita=?");
			pstmt.setInt(1, cdEmpreendimento);
			pstmt.setInt(2, cdReceita);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EmpreendimentoReceita(rs.getInt("cd_empreendimento"),
						rs.getInt("cd_receita"),
						rs.getString("nm_produto"),
						rs.getInt("lg_receita"),
						rs.getFloat("vl_preco_unitario"),
						rs.getFloat("qt_vendida"),
						rs.getString("sg_unidade"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoReceitaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoReceitaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendimento_receita");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoReceitaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoReceitaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM mcr_empreendimento_receita", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
