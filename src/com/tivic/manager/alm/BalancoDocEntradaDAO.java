package com.tivic.manager.alm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class BalancoDocEntradaDAO{

	public static int insert(BalancoDocEntrada objeto) {
		return insert(objeto, null);
	}

	public static int insert(BalancoDocEntrada objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_balanco_doc_entrada (cd_balanco,"+
			                                  "cd_empresa,"+
			                                  "cd_documento_entrada) VALUES (?, ?, ?)");
			if(objeto.getCdBalanco()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdBalanco());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdDocumentoEntrada());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BalancoDocEntradaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BalancoDocEntradaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(BalancoDocEntrada objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(BalancoDocEntrada objeto, int cdBalancoOld, int cdEmpresaOld, int cdDocumentoEntradaOld) {
		return update(objeto, cdBalancoOld, cdEmpresaOld, cdDocumentoEntradaOld, null);
	}

	public static int update(BalancoDocEntrada objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(BalancoDocEntrada objeto, int cdBalancoOld, int cdEmpresaOld, int cdDocumentoEntradaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_balanco_doc_entrada SET cd_balanco=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_documento_entrada=? WHERE cd_balanco=? AND cd_empresa=? AND cd_documento_entrada=?");
			pstmt.setInt(1,objeto.getCdBalanco());
			pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setInt(3,objeto.getCdDocumentoEntrada());
			pstmt.setInt(4, cdBalancoOld!=0 ? cdBalancoOld : objeto.getCdBalanco());
			pstmt.setInt(5, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(6, cdDocumentoEntradaOld!=0 ? cdDocumentoEntradaOld : objeto.getCdDocumentoEntrada());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BalancoDocEntradaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BalancoDocEntradaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdBalanco, int cdEmpresa, int cdDocumentoEntrada) {
		return delete(cdBalanco, cdEmpresa, cdDocumentoEntrada, null);
	}

	public static int delete(int cdBalanco, int cdEmpresa, int cdDocumentoEntrada, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_balanco_doc_entrada WHERE cd_balanco=? AND cd_empresa=? AND cd_documento_entrada=?");
			pstmt.setInt(1, cdBalanco);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdDocumentoEntrada);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BalancoDocEntradaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BalancoDocEntradaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static BalancoDocEntrada get(int cdBalanco, int cdEmpresa, int cdDocumentoEntrada) {
		return get(cdBalanco, cdEmpresa, cdDocumentoEntrada, null);
	}

	public static BalancoDocEntrada get(int cdBalanco, int cdEmpresa, int cdDocumentoEntrada, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_balanco_doc_entrada WHERE cd_balanco=? AND cd_empresa=? AND cd_documento_entrada=?");
			pstmt.setInt(1, cdBalanco);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdDocumentoEntrada);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new BalancoDocEntrada(rs.getInt("cd_balanco"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_documento_entrada"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BalancoDocEntradaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BalancoDocEntradaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_balanco_doc_entrada");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BalancoDocEntradaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BalancoDocEntradaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM alm_balanco_doc_entrada", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
