package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoDescontoEmpresaDAO{

	public static int insert(TipoDescontoEmpresa objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoDescontoEmpresa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_tipo_desconto_empresa (cd_tipo_desconto,"+
			                                  "cd_empresa,"+
			                                  "st_tipo_desconto) VALUES (?, ?, ?)");
			if(objeto.getCdTipoDesconto()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTipoDesconto());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setInt(3,objeto.getStTipoDesconto());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDescontoEmpresaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDescontoEmpresaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoDescontoEmpresa objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TipoDescontoEmpresa objeto, int cdTipoDescontoOld, int cdEmpresaOld) {
		return update(objeto, cdTipoDescontoOld, cdEmpresaOld, null);
	}

	public static int update(TipoDescontoEmpresa objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TipoDescontoEmpresa objeto, int cdTipoDescontoOld, int cdEmpresaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_tipo_desconto_empresa SET cd_tipo_desconto=?,"+
												      		   "cd_empresa=?,"+
												      		   "st_tipo_desconto=? WHERE cd_tipo_desconto=? AND cd_empresa=?");
			pstmt.setInt(1,objeto.getCdTipoDesconto());
			pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setInt(3,objeto.getStTipoDesconto());
			pstmt.setInt(4, cdTipoDescontoOld!=0 ? cdTipoDescontoOld : objeto.getCdTipoDesconto());
			pstmt.setInt(5, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDescontoEmpresaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDescontoEmpresaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoDesconto, int cdEmpresa) {
		return delete(cdTipoDesconto, cdEmpresa, null);
	}

	public static int delete(int cdTipoDesconto, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_tipo_desconto_empresa WHERE cd_tipo_desconto=? AND cd_empresa=?");
			pstmt.setInt(1, cdTipoDesconto);
			pstmt.setInt(2, cdEmpresa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDescontoEmpresaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDescontoEmpresaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoDescontoEmpresa get(int cdTipoDesconto, int cdEmpresa) {
		return get(cdTipoDesconto, cdEmpresa, null);
	}

	public static TipoDescontoEmpresa get(int cdTipoDesconto, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_tipo_desconto_empresa WHERE cd_tipo_desconto=? AND cd_empresa=?");
			pstmt.setInt(1, cdTipoDesconto);
			pstmt.setInt(2, cdEmpresa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoDescontoEmpresa(rs.getInt("cd_tipo_desconto"),
						rs.getInt("cd_empresa"),
						rs.getInt("st_tipo_desconto"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDescontoEmpresaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDescontoEmpresaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_tipo_desconto_empresa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDescontoEmpresaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDescontoEmpresaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_tipo_desconto_empresa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
