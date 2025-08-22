package com.tivic.manager.srh;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TabelaHorarioDAO{

	public static int insert(TabelaHorario objeto) {
		return insert(objeto, null);
	}

	public static int insert(TabelaHorario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("srh_tabela_horario", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTabelaHorario(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO srh_tabela_horario (cd_tabela_horario,"+
			                                  "nm_tabela_horario,"+
			                                  "id_tabela_horario,"+
			                                  "qt_horas_mes,"+
			                                  "st_tabela_horario,"+
			                                  "cd_empresa) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTabelaHorario());
			pstmt.setString(3,objeto.getIdTabelaHorario());
			pstmt.setInt(4,objeto.getQtHorasMes());
			pstmt.setInt(5,objeto.getStTabelaHorario());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEmpresa());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TabelaHorario objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TabelaHorario objeto, int cdTabelaHorarioOld) {
		return update(objeto, cdTabelaHorarioOld, null);
	}

	public static int update(TabelaHorario objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TabelaHorario objeto, int cdTabelaHorarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE srh_tabela_horario SET cd_tabela_horario=?,"+
												      		   "nm_tabela_horario=?,"+
												      		   "id_tabela_horario=?,"+
												      		   "qt_horas_mes=?,"+
												      		   "st_tabela_horario=?,"+
												      		   "cd_empresa=? WHERE cd_tabela_horario=?");
			pstmt.setInt(1,objeto.getCdTabelaHorario());
			pstmt.setString(2,objeto.getNmTabelaHorario());
			pstmt.setString(3,objeto.getIdTabelaHorario());
			pstmt.setInt(4,objeto.getQtHorasMes());
			pstmt.setInt(5,objeto.getStTabelaHorario());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEmpresa());
			pstmt.setInt(7, cdTabelaHorarioOld!=0 ? cdTabelaHorarioOld : objeto.getCdTabelaHorario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTabelaHorario) {
		return delete(cdTabelaHorario, null);
	}

	public static int delete(int cdTabelaHorario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM srh_tabela_horario WHERE cd_tabela_horario=?");
			pstmt.setInt(1, cdTabelaHorario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TabelaHorario get(int cdTabelaHorario) {
		return get(cdTabelaHorario, null);
	}

	public static TabelaHorario get(int cdTabelaHorario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM srh_tabela_horario WHERE cd_tabela_horario=?");
			pstmt.setInt(1, cdTabelaHorario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TabelaHorario(rs.getInt("cd_tabela_horario"),
						rs.getString("nm_tabela_horario"),
						rs.getString("id_tabela_horario"),
						rs.getInt("qt_horas_mes"),
						rs.getInt("st_tabela_horario"),
						rs.getInt("cd_empresa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_tabela_horario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM srh_tabela_horario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
