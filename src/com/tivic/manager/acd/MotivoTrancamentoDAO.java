package com.tivic.manager.acd;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class MotivoTrancamentoDAO{

	public static int insert(MotivoTrancamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(MotivoTrancamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_motivo_trancamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMotivoTrancamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_motivo_trancamento (cd_motivo_trancamento,"+
			                                  "nm_motivo_trancamento) VALUES (?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmMotivoTrancamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoTrancamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoTrancamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MotivoTrancamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(MotivoTrancamento objeto, int cdMotivoTrancamentoOld) {
		return update(objeto, cdMotivoTrancamentoOld, null);
	}

	public static int update(MotivoTrancamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(MotivoTrancamento objeto, int cdMotivoTrancamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_motivo_trancamento SET cd_motivo_trancamento=?,"+
												      		   "nm_motivo_trancamento=? WHERE cd_motivo_trancamento=?");
			pstmt.setInt(1,objeto.getCdMotivoTrancamento());
			pstmt.setString(2,objeto.getNmMotivoTrancamento());
			pstmt.setInt(3, cdMotivoTrancamentoOld!=0 ? cdMotivoTrancamentoOld : objeto.getCdMotivoTrancamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoTrancamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoTrancamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMotivoTrancamento) {
		return delete(cdMotivoTrancamento, null);
	}

	public static int delete(int cdMotivoTrancamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_motivo_trancamento WHERE cd_motivo_trancamento=?");
			pstmt.setInt(1, cdMotivoTrancamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoTrancamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoTrancamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MotivoTrancamento get(int cdMotivoTrancamento) {
		return get(cdMotivoTrancamento, null);
	}

	public static MotivoTrancamento get(int cdMotivoTrancamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_motivo_trancamento WHERE cd_motivo_trancamento=?");
			pstmt.setInt(1, cdMotivoTrancamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MotivoTrancamento(rs.getInt("cd_motivo_trancamento"),
						rs.getString("nm_motivo_trancamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoTrancamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoTrancamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_motivo_trancamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoTrancamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoTrancamentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_motivo_trancamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
