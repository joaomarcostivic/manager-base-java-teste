package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class FaixaDescontoDAO{

	public static int insert(FaixaDesconto objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(FaixaDesconto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
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
			keys[2].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("adm_faixa_desconto", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFaixaDesconto(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_faixa_desconto (cd_tipo_desconto,"+
			                                  "cd_empresa,"+
			                                  "cd_faixa_desconto,"+
			                                  "pr_desconto,"+
			                                  "dt_inicial_validade,"+
			                                  "dt_final_validade) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdTipoDesconto()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTipoDesconto());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setInt(3, code);
			pstmt.setFloat(4,objeto.getPrDesconto());
			if(objeto.getDtInicialValidade()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtInicialValidade().getTimeInMillis()));
			if(objeto.getDtFinalValidade()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtFinalValidade().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FaixaDescontoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaDescontoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FaixaDesconto objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(FaixaDesconto objeto, int cdTipoDescontoOld, int cdEmpresaOld, int cdFaixaDescontoOld) {
		return update(objeto, cdTipoDescontoOld, cdEmpresaOld, cdFaixaDescontoOld, null);
	}

	public static int update(FaixaDesconto objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(FaixaDesconto objeto, int cdTipoDescontoOld, int cdEmpresaOld, int cdFaixaDescontoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_faixa_desconto SET cd_tipo_desconto=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_faixa_desconto=?,"+
												      		   "pr_desconto=?,"+
												      		   "dt_inicial_validade=?,"+
												      		   "dt_final_validade=? WHERE cd_tipo_desconto=? AND cd_empresa=? AND cd_faixa_desconto=?");
			pstmt.setInt(1,objeto.getCdTipoDesconto());
			pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setInt(3,objeto.getCdFaixaDesconto());
			pstmt.setFloat(4,objeto.getPrDesconto());
			if(objeto.getDtInicialValidade()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtInicialValidade().getTimeInMillis()));
			if(objeto.getDtFinalValidade()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtFinalValidade().getTimeInMillis()));
			pstmt.setInt(7, cdTipoDescontoOld!=0 ? cdTipoDescontoOld : objeto.getCdTipoDesconto());
			pstmt.setInt(8, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(9, cdFaixaDescontoOld!=0 ? cdFaixaDescontoOld : objeto.getCdFaixaDesconto());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FaixaDescontoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaDescontoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoDesconto, int cdEmpresa, int cdFaixaDesconto) {
		return delete(cdTipoDesconto, cdEmpresa, cdFaixaDesconto, null);
	}

	public static int delete(int cdTipoDesconto, int cdEmpresa, int cdFaixaDesconto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_faixa_desconto WHERE cd_tipo_desconto=? AND cd_empresa=? AND cd_faixa_desconto=?");
			pstmt.setInt(1, cdTipoDesconto);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdFaixaDesconto);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FaixaDescontoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaDescontoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FaixaDesconto get(int cdTipoDesconto, int cdEmpresa, int cdFaixaDesconto) {
		return get(cdTipoDesconto, cdEmpresa, cdFaixaDesconto, null);
	}

	public static FaixaDesconto get(int cdTipoDesconto, int cdEmpresa, int cdFaixaDesconto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_faixa_desconto WHERE cd_tipo_desconto=? AND cd_empresa=? AND cd_faixa_desconto=?");
			pstmt.setInt(1, cdTipoDesconto);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdFaixaDesconto);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FaixaDesconto(rs.getInt("cd_tipo_desconto"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_faixa_desconto"),
						rs.getFloat("pr_desconto"),
						(rs.getTimestamp("dt_inicial_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial_validade").getTime()),
						(rs.getTimestamp("dt_final_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final_validade").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FaixaDescontoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaDescontoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_faixa_desconto");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FaixaDescontoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaDescontoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_faixa_desconto", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
