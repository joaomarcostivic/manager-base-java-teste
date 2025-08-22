package com.tivic.manager.flp;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class NaturezaEventoDAO{

	public static int insert(NaturezaEvento objeto) {
		return insert(objeto, null);
	}

	public static int insert(NaturezaEvento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("flp_natureza_evento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdNaturezaEvento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO flp_natureza_evento (cd_natureza_evento,"+
			                                  "cd_natureza_superior,"+
			                                  "nm_natureza_evento,"+
			                                  "id_natureza_evento) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdNaturezaSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdNaturezaSuperior());
			pstmt.setString(3,objeto.getNmNaturezaEvento());
			pstmt.setString(4,objeto.getIdNaturezaEvento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NaturezaEventoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NaturezaEventoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(NaturezaEvento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(NaturezaEvento objeto, int cdNaturezaEventoOld) {
		return update(objeto, cdNaturezaEventoOld, null);
	}

	public static int update(NaturezaEvento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(NaturezaEvento objeto, int cdNaturezaEventoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE flp_natureza_evento SET cd_natureza_evento=?,"+
												      		   "cd_natureza_superior=?,"+
												      		   "nm_natureza_evento=?,"+
												      		   "id_natureza_evento=? WHERE cd_natureza_evento=?");
			pstmt.setInt(1,objeto.getCdNaturezaEvento());
			if(objeto.getCdNaturezaSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdNaturezaSuperior());
			pstmt.setString(3,objeto.getNmNaturezaEvento());
			pstmt.setString(4,objeto.getIdNaturezaEvento());
			pstmt.setInt(5, cdNaturezaEventoOld!=0 ? cdNaturezaEventoOld : objeto.getCdNaturezaEvento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NaturezaEventoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NaturezaEventoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdNaturezaEvento) {
		return delete(cdNaturezaEvento, null);
	}

	public static int delete(int cdNaturezaEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM flp_natureza_evento WHERE cd_natureza_evento=?");
			pstmt.setInt(1, cdNaturezaEvento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NaturezaEventoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NaturezaEventoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static NaturezaEvento get(int cdNaturezaEvento) {
		return get(cdNaturezaEvento, null);
	}

	public static NaturezaEvento get(int cdNaturezaEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM flp_natureza_evento WHERE cd_natureza_evento=?");
			pstmt.setInt(1, cdNaturezaEvento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new NaturezaEvento(rs.getInt("cd_natureza_evento"),
						rs.getInt("cd_natureza_superior"),
						rs.getString("nm_natureza_evento"),
						rs.getString("id_natureza_evento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NaturezaEventoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NaturezaEventoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM flp_natureza_evento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NaturezaEventoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NaturezaEventoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM flp_natureza_evento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
