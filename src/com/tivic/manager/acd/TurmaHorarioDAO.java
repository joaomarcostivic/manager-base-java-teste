package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TurmaHorarioDAO{

	public static int insert(TurmaHorario objeto) {
		return insert(objeto, null);
	}

	public static int insert(TurmaHorario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_turma_horario (cd_horario,"+
			                                  "cd_turma) VALUES (?, ?)");
			if(objeto.getCdHorario()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdHorario());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTurma());
			int ret = pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaHorarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaHorarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TurmaHorario objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TurmaHorario objeto, int cdHorarioOld, int cdTurmaOld) {
		return update(objeto, cdHorarioOld, cdTurmaOld, null);
	}

	public static int update(TurmaHorario objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TurmaHorario objeto, int cdHorarioOld, int cdTurmaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_turma_horario SET cd_horario=?,"+
												      		   "cd_turma=? WHERE cd_horario=? AND cd_turma=?");
			pstmt.setInt(1,objeto.getCdHorario());
			pstmt.setInt(2,objeto.getCdTurma());
			pstmt.setInt(3, cdHorarioOld!=0 ? cdHorarioOld : objeto.getCdHorario());
			pstmt.setInt(4, cdTurmaOld!=0 ? cdTurmaOld : objeto.getCdTurma());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaHorarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaHorarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdHorario, int cdTurma) {
		return delete(cdHorario, cdTurma, null);
	}

	public static int delete(int cdHorario, int cdTurma, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_turma_horario WHERE cd_horario=? AND cd_turma=?");
			pstmt.setInt(1, cdHorario);
			pstmt.setInt(2, cdTurma);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaHorarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaHorarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TurmaHorario get(int cdHorario, int cdTurma) {
		return get(cdHorario, cdTurma, null);
	}

	public static TurmaHorario get(int cdHorario, int cdTurma, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_turma_horario WHERE cd_horario=? AND cd_turma=?");
			pstmt.setInt(1, cdHorario);
			pstmt.setInt(2, cdTurma);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TurmaHorario(rs.getInt("cd_horario"),
						rs.getInt("cd_turma"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaHorarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaHorarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_turma_horario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaHorarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaHorarioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TurmaHorario> getList() {
		return getList(null);
	}

	public static ArrayList<TurmaHorario> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TurmaHorario> list = new ArrayList<TurmaHorario>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TurmaHorario obj = TurmaHorarioDAO.get(rsm.getInt("cd_horario"), rsm.getInt("cd_turma"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaHorarioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_turma_horario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
