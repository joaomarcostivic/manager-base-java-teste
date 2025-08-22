package com.tivic.manager.fsc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class MotivoCancelamentoDAO{

	public static int insert(MotivoCancelamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(MotivoCancelamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fsc_motivo_cancelamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMotivoCancelamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fsc_motivo_cancelamento (cd_motivo_cancelamento,"+
			                                  "ds_motivo_cancelamento,"+
			                                  "id_motivo_cancelamento) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getDsMotivoCancelamento());
			pstmt.setString(3,objeto.getIdMotivoCancelamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoCancelamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoCancelamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MotivoCancelamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(MotivoCancelamento objeto, int cdMotivoCancelamentoOld) {
		return update(objeto, cdMotivoCancelamentoOld, null);
	}

	public static int update(MotivoCancelamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(MotivoCancelamento objeto, int cdMotivoCancelamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fsc_motivo_cancelamento SET cd_motivo_cancelamento=?,"+
												      		   "ds_motivo_cancelamento=?,"+
												      		   "id_motivo_cancelamento=? WHERE cd_motivo_cancelamento=?");
			pstmt.setInt(1,objeto.getCdMotivoCancelamento());
			pstmt.setString(2,objeto.getDsMotivoCancelamento());
			pstmt.setString(3,objeto.getIdMotivoCancelamento());
			pstmt.setInt(4, cdMotivoCancelamentoOld!=0 ? cdMotivoCancelamentoOld : objeto.getCdMotivoCancelamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoCancelamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoCancelamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMotivoCancelamento) {
		return delete(cdMotivoCancelamento, null);
	}

	public static int delete(int cdMotivoCancelamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fsc_motivo_cancelamento WHERE cd_motivo_cancelamento=?");
			pstmt.setInt(1, cdMotivoCancelamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoCancelamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoCancelamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MotivoCancelamento get(int cdMotivoCancelamento) {
		return get(cdMotivoCancelamento, null);
	}

	public static MotivoCancelamento get(int cdMotivoCancelamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_motivo_cancelamento WHERE cd_motivo_cancelamento=?");
			pstmt.setInt(1, cdMotivoCancelamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MotivoCancelamento(rs.getInt("cd_motivo_cancelamento"),
						rs.getString("ds_motivo_cancelamento"),
						rs.getString("id_motivo_cancelamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoCancelamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoCancelamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fsc_motivo_cancelamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoCancelamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoCancelamentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fsc_motivo_cancelamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
