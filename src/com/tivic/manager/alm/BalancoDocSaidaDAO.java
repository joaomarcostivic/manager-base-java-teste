package com.tivic.manager.alm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class BalancoDocSaidaDAO{

	public static int insert(BalancoDocSaida objeto) {
		return insert(objeto, null);
	}

	public static int insert(BalancoDocSaida objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_balanco_doc_saida (cd_balanco,"+
			                                  "cd_empresa,"+
			                                  "cd_documento_saida) VALUES (?, ?, ?)");
			if(objeto.getCdBalanco()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdBalanco());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdDocumentoSaida());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BalancoDocSaidaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BalancoDocSaidaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(BalancoDocSaida objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(BalancoDocSaida objeto, int cdBalancoOld, int cdEmpresaOld, int cdDocumentoSaidaOld) {
		return update(objeto, cdBalancoOld, cdEmpresaOld, cdDocumentoSaidaOld, null);
	}

	public static int update(BalancoDocSaida objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(BalancoDocSaida objeto, int cdBalancoOld, int cdEmpresaOld, int cdDocumentoSaidaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_balanco_doc_saida SET cd_balanco=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_documento_saida=? WHERE cd_balanco=? AND cd_empresa=? AND cd_documento_saida=?");
			pstmt.setInt(1,objeto.getCdBalanco());
			pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setInt(3,objeto.getCdDocumentoSaida());
			pstmt.setInt(4, cdBalancoOld!=0 ? cdBalancoOld : objeto.getCdBalanco());
			pstmt.setInt(5, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(6, cdDocumentoSaidaOld!=0 ? cdDocumentoSaidaOld : objeto.getCdDocumentoSaida());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BalancoDocSaidaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BalancoDocSaidaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdBalanco, int cdEmpresa, int cdDocumentoSaida) {
		return delete(cdBalanco, cdEmpresa, cdDocumentoSaida, null);
	}

	public static int delete(int cdBalanco, int cdEmpresa, int cdDocumentoSaida, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_balanco_doc_saida WHERE cd_balanco=? AND cd_empresa=? AND cd_documento_saida=?");
			pstmt.setInt(1, cdBalanco);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdDocumentoSaida);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BalancoDocSaidaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BalancoDocSaidaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static BalancoDocSaida get(int cdBalanco, int cdEmpresa, int cdDocumentoSaida) {
		return get(cdBalanco, cdEmpresa, cdDocumentoSaida, null);
	}

	public static BalancoDocSaida get(int cdBalanco, int cdEmpresa, int cdDocumentoSaida, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_balanco_doc_saida WHERE cd_balanco=? AND cd_empresa=? AND cd_documento_saida=?");
			pstmt.setInt(1, cdBalanco);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdDocumentoSaida);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new BalancoDocSaida(rs.getInt("cd_balanco"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_documento_saida"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BalancoDocSaidaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BalancoDocSaidaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_balanco_doc_saida");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BalancoDocSaidaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BalancoDocSaidaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM alm_balanco_doc_saida", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
