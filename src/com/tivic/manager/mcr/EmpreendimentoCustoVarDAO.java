package com.tivic.manager.mcr;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class EmpreendimentoCustoVarDAO{

	public static int insert(EmpreendimentoCustoVar objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(EmpreendimentoCustoVar objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			HashMap<String,Object>[] keys = new HashMap[1];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_empreendimento");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdEmpreendimento()));
			int code = Conexao.getSequenceCode("mcr_empreendimento_custo_var", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO mcr_empreendimento_custo_var (cd_empreendimento,"+
			                                  "vl_transporte_venda,"+
			                                  "vl_comissao_venda,"+
			                                  "lg_industria,"+
			                                  "vl_outro_custo,"+
			                                  "lg_comercio,"+
			                                  "lg_servico,"+
			                                  "vl_imposto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEmpreendimento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpreendimento());
			pstmt.setFloat(2,objeto.getVlTransporteVenda());
			pstmt.setFloat(3,objeto.getVlComissaoVenda());
			pstmt.setInt(4,objeto.getLgIndustria());
			pstmt.setFloat(5,objeto.getVlOutroCusto());
			pstmt.setInt(6,objeto.getLgComercio());
			pstmt.setInt(7,objeto.getLgServico());
			pstmt.setFloat(8,objeto.getVlImposto());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoCustoVarDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoCustoVarDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EmpreendimentoCustoVar objeto) {
		return update(objeto, null);
	}

	public static int update(EmpreendimentoCustoVar objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE mcr_empreendimento_custo_var SET vl_transporte_venda=?,"+
			                                  "vl_comissao_venda=?,"+
			                                  "lg_industria=?,"+
			                                  "vl_outro_custo=?,"+
			                                  "lg_comercio=?,"+
			                                  "lg_servico=?,"+
			                                  "vl_imposto=? WHERE cd_empreendimento=?");
			pstmt.setFloat(1,objeto.getVlTransporteVenda());
			pstmt.setFloat(2,objeto.getVlComissaoVenda());
			pstmt.setInt(3,objeto.getLgIndustria());
			pstmt.setFloat(4,objeto.getVlOutroCusto());
			pstmt.setInt(5,objeto.getLgComercio());
			pstmt.setInt(6,objeto.getLgServico());
			pstmt.setFloat(7,objeto.getVlImposto());
			pstmt.setInt(8,objeto.getCdEmpreendimento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoCustoVarDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoCustoVarDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpreendimento) {
		return delete(cdEmpreendimento, null);
	}

	public static int delete(int cdEmpreendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM mcr_empreendimento_custo_var WHERE cd_empreendimento=?");
			pstmt.setInt(1, cdEmpreendimento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoCustoVarDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoCustoVarDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EmpreendimentoCustoVar get(int cdEmpreendimento) {
		return get(cdEmpreendimento, null);
	}

	public static EmpreendimentoCustoVar get(int cdEmpreendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendimento_custo_var WHERE cd_empreendimento=?");
			pstmt.setInt(1, cdEmpreendimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EmpreendimentoCustoVar(rs.getInt("cd_empreendimento"),
						rs.getFloat("vl_transporte_venda"),
						rs.getFloat("vl_comissao_venda"),
						rs.getInt("lg_industria"),
						rs.getFloat("vl_outro_custo"),
						rs.getInt("lg_comercio"),
						rs.getInt("lg_servico"),
						rs.getFloat("vl_imposto"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoCustoVarDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoCustoVarDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendimento_custo_var");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoCustoVarDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoCustoVarDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM mcr_empreendimento_custo_var", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
