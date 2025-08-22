package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AvalistaContratoDAO{

	public static int insert(AvalistaContrato objeto) {
		return insert(objeto, null);
	}

	public static int insert(AvalistaContrato objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_avalista_contrato (cd_contrato,"+
			                                  "cd_pessoa) VALUES (?, ?)");
			if(objeto.getCdContrato()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContrato());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AvalistaContratoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AvalistaContratoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AvalistaContrato objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AvalistaContrato objeto, int cdContratoOld, int cdPessoaOld) {
		return update(objeto, cdContratoOld, cdPessoaOld, null);
	}

	public static int update(AvalistaContrato objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AvalistaContrato objeto, int cdContratoOld, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_avalista_contrato SET cd_contrato=?,"+
												      		   "cd_pessoa=? WHERE cd_contrato=? AND cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdContrato());
			pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setInt(3, cdContratoOld!=0 ? cdContratoOld : objeto.getCdContrato());
			pstmt.setInt(4, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AvalistaContratoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AvalistaContratoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContrato, int cdPessoa) {
		return delete(cdContrato, cdPessoa, null);
	}

	public static int delete(int cdContrato, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_avalista_contrato WHERE cd_contrato=? AND cd_pessoa=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdPessoa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AvalistaContratoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AvalistaContratoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AvalistaContrato get(int cdContrato, int cdPessoa) {
		return get(cdContrato, cdPessoa, null);
	}

	public static AvalistaContrato get(int cdContrato, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_avalista_contrato WHERE cd_contrato=? AND cd_pessoa=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AvalistaContrato(rs.getInt("cd_contrato"),
						rs.getInt("cd_pessoa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AvalistaContratoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AvalistaContratoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_avalista_contrato");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AvalistaContratoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AvalistaContratoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_avalista_contrato", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
