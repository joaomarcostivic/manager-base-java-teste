package com.tivic.manager.agd;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AgendaDAO{

	public static int insert(Agenda objeto) {
		return insert(objeto, null);
	}

	public static int insert(Agenda objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("agd_agenda", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAgenda(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_agenda (cd_agenda,"+
			                                  "nm_agenda,"+
			                                  "cd_empresa,"+
			                                  "txt_abertura_ata,"+
			                                  "txt_agenda) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmAgenda());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setString(4,objeto.getTxtAberturaAta());
			pstmt.setString(5,objeto.getTxtAgenda());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Agenda objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Agenda objeto, int cdAgendaOld) {
		return update(objeto, cdAgendaOld, null);
	}

	public static int update(Agenda objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Agenda objeto, int cdAgendaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agd_agenda SET cd_agenda=?,"+
												      		   "nm_agenda=?,"+
												      		   "cd_empresa=?,"+
												      		   "txt_abertura_ata=?,"+
												      		   "txt_agenda=? WHERE cd_agenda=?");
			pstmt.setInt(1,objeto.getCdAgenda());
			pstmt.setString(2,objeto.getNmAgenda());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setString(4,objeto.getTxtAberturaAta());
			pstmt.setString(5,objeto.getTxtAgenda());
			pstmt.setInt(6, cdAgendaOld!=0 ? cdAgendaOld : objeto.getCdAgenda());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAgenda) {
		return delete(cdAgenda, null);
	}

	public static int delete(int cdAgenda, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_agenda WHERE cd_agenda=?");
			pstmt.setInt(1, cdAgenda);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Agenda get(int cdAgenda) {
		return get(cdAgenda, null);
	}

	public static Agenda get(int cdAgenda, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_agenda WHERE cd_agenda=?");
			pstmt.setInt(1, cdAgenda);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Agenda(rs.getInt("cd_agenda"),
						rs.getString("nm_agenda"),
						rs.getInt("cd_empresa"),
						rs.getString("txt_abertura_ata"),
						rs.getString("txt_agenda"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_agenda");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM agd_agenda", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
