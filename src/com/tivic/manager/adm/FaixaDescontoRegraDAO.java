package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class FaixaDescontoRegraDAO{

	public static int insert(FaixaDescontoRegra objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(FaixaDescontoRegra objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[4];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_tipo_desconto");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdTipoDesconto()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_empresa");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdEmpresa()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_faixa_desconto");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdFaixaDesconto()));
			keys[3] = new HashMap<String,Object>();
			keys[3].put("FIELD_NAME", "cd_regra");
			keys[3].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("adm_faixa_desconto_regra", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRegra(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_faixa_desconto_regra (cd_tipo_desconto,"+
			                                  "cd_empresa,"+
			                                  "cd_faixa_desconto,"+
			                                  "cd_regra,"+
			                                  "cd_produto_servico,"+
			                                  "cd_plano_pagamento,"+
			                                  "dt_inicial,"+
			                                  "dt_final) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdTipoDesconto()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTipoDesconto());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdFaixaDesconto()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdFaixaDesconto());
			pstmt.setInt(4, code);
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdProdutoServico());
			if(objeto.getCdPlanoPagamento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdPlanoPagamento());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FaixaDescontoRegraDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaDescontoRegraDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FaixaDescontoRegra objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(FaixaDescontoRegra objeto, int cdTipoDescontoOld, int cdEmpresaOld, int cdFaixaDescontoOld, int cdRegraOld) {
		return update(objeto, cdTipoDescontoOld, cdEmpresaOld, cdFaixaDescontoOld, cdRegraOld, null);
	}

	public static int update(FaixaDescontoRegra objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(FaixaDescontoRegra objeto, int cdTipoDescontoOld, int cdEmpresaOld, int cdFaixaDescontoOld, int cdRegraOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_faixa_desconto_regra SET cd_tipo_desconto=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_faixa_desconto=?,"+
												      		   "cd_regra=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_plano_pagamento=?,"+
												      		   "dt_inicial=?,"+
												      		   "dt_final=? WHERE cd_tipo_desconto=? AND cd_empresa=? AND cd_faixa_desconto=? AND cd_regra=?");
			pstmt.setInt(1,objeto.getCdTipoDesconto());
			pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setInt(3,objeto.getCdFaixaDesconto());
			pstmt.setInt(4,objeto.getCdRegra());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdProdutoServico());
			if(objeto.getCdPlanoPagamento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdPlanoPagamento());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setInt(9, cdTipoDescontoOld!=0 ? cdTipoDescontoOld : objeto.getCdTipoDesconto());
			pstmt.setInt(10, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(11, cdFaixaDescontoOld!=0 ? cdFaixaDescontoOld : objeto.getCdFaixaDesconto());
			pstmt.setInt(12, cdRegraOld!=0 ? cdRegraOld : objeto.getCdRegra());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FaixaDescontoRegraDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaDescontoRegraDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoDesconto, int cdEmpresa, int cdFaixaDesconto, int cdRegra) {
		return delete(cdTipoDesconto, cdEmpresa, cdFaixaDesconto, cdRegra, null);
	}

	public static int delete(int cdTipoDesconto, int cdEmpresa, int cdFaixaDesconto, int cdRegra, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_faixa_desconto_regra WHERE cd_tipo_desconto=? AND cd_empresa=? AND cd_faixa_desconto=? AND cd_regra=?");
			pstmt.setInt(1, cdTipoDesconto);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdFaixaDesconto);
			pstmt.setInt(4, cdRegra);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FaixaDescontoRegraDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaDescontoRegraDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FaixaDescontoRegra get(int cdTipoDesconto, int cdEmpresa, int cdFaixaDesconto, int cdRegra) {
		return get(cdTipoDesconto, cdEmpresa, cdFaixaDesconto, cdRegra, null);
	}

	public static FaixaDescontoRegra get(int cdTipoDesconto, int cdEmpresa, int cdFaixaDesconto, int cdRegra, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_faixa_desconto_regra WHERE cd_tipo_desconto=? AND cd_empresa=? AND cd_faixa_desconto=? AND cd_regra=?");
			pstmt.setInt(1, cdTipoDesconto);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdFaixaDesconto);
			pstmt.setInt(4, cdRegra);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FaixaDescontoRegra(rs.getInt("cd_tipo_desconto"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_faixa_desconto"),
						rs.getInt("cd_regra"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_plano_pagamento"),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FaixaDescontoRegraDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaDescontoRegraDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_faixa_desconto_regra");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FaixaDescontoRegraDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaDescontoRegraDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_faixa_desconto_regra", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
