package com.tivic.manager.ctb;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class HistoricoDAO{

	public static int insert(Historico objeto) {
		return insert(objeto, null);
	}

	public static int insert(Historico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ctb_historico", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdHistorico(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ctb_historico (cd_historico,"+
			                                  "cd_empresa,"+
			                                  "nm_historico,"+
			                                  "id_historico,"+
			                                  "lg_complemento) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setString(3,objeto.getNmHistorico());
			pstmt.setString(4,objeto.getIdHistorico());
			pstmt.setInt(5,objeto.getLgComplemento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HistoricoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! HistoricoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Historico objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Historico objeto, int cdHistoricoOld) {
		return update(objeto, cdHistoricoOld, null);
	}

	public static int update(Historico objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Historico objeto, int cdHistoricoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ctb_historico SET cd_historico=?,"+
												      		   "cd_empresa=?,"+
												      		   "nm_historico=?,"+
												      		   "id_historico=?,"+
												      		   "lg_complemento=? WHERE cd_historico=?");
			pstmt.setInt(1,objeto.getCdHistorico());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setString(3,objeto.getNmHistorico());
			pstmt.setString(4,objeto.getIdHistorico());
			pstmt.setInt(5,objeto.getLgComplemento());
			pstmt.setInt(6, cdHistoricoOld!=0 ? cdHistoricoOld : objeto.getCdHistorico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HistoricoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! HistoricoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdHistorico) {
		return delete(cdHistorico, null);
	}

	public static int delete(int cdHistorico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ctb_historico WHERE cd_historico=?");
			pstmt.setInt(1, cdHistorico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HistoricoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! HistoricoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Historico get(int cdHistorico) {
		return get(cdHistorico, null);
	}

	public static Historico get(int cdHistorico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ctb_historico WHERE cd_historico=?");
			pstmt.setInt(1, cdHistorico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Historico(rs.getInt("cd_historico"),
						rs.getInt("cd_empresa"),
						rs.getString("nm_historico"),
						rs.getString("id_historico"),
						rs.getInt("lg_complemento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HistoricoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! HistoricoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_historico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HistoricoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! HistoricoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM ctb_historico", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
