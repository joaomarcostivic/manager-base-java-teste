package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class MatriculaProgramaDAO{

	public static int insert(MatriculaPrograma objeto) {
		return insert(objeto, null);
	}

	public static int insert(MatriculaPrograma objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_matricula_programa (cd_matricula,"+
			                                  "cd_programa,"+
			                                  "nr_matricula_programa,"+
			                                  "dt_inicio,"+
			                                  "dt_termino,"+
			                                  "st_matricula_programa,"+
			                                  "cd_turma) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdMatricula()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdMatricula());
			if(objeto.getCdPrograma()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPrograma());
			pstmt.setString(3,objeto.getNrMatriculaPrograma());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			if(objeto.getDtTermino()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtTermino().getTimeInMillis()));
			pstmt.setInt(6,objeto.getStMatriculaPrograma());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTurma());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaProgramaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaProgramaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MatriculaPrograma objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(MatriculaPrograma objeto, int cdMatriculaOld, int cdProgramaOld) {
		return update(objeto, cdMatriculaOld, cdProgramaOld, null);
	}

	public static int update(MatriculaPrograma objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(MatriculaPrograma objeto, int cdMatriculaOld, int cdProgramaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_matricula_programa SET cd_matricula=?,"+
												      		   "cd_programa=?,"+
												      		   "nr_matricula_programa=?,"+
												      		   "dt_inicio=?,"+
												      		   "dt_termino=?,"+
												      		   "st_matricula_programa=?,"+
												      		   "cd_turma=? WHERE cd_matricula=? AND cd_programa=?");
			pstmt.setInt(1,objeto.getCdMatricula());
			pstmt.setInt(2,objeto.getCdPrograma());
			pstmt.setString(3,objeto.getNrMatriculaPrograma());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			if(objeto.getDtTermino()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtTermino().getTimeInMillis()));
			pstmt.setInt(6,objeto.getStMatriculaPrograma());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTurma());
			pstmt.setInt(8, cdMatriculaOld!=0 ? cdMatriculaOld : objeto.getCdMatricula());
			pstmt.setInt(9, cdProgramaOld!=0 ? cdProgramaOld : objeto.getCdPrograma());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaProgramaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaProgramaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMatricula, int cdPrograma) {
		return delete(cdMatricula, cdPrograma, null);
	}

	public static int delete(int cdMatricula, int cdPrograma, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_matricula_programa WHERE cd_matricula=? AND cd_programa=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdPrograma);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaProgramaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaProgramaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MatriculaPrograma get(int cdMatricula, int cdPrograma) {
		return get(cdMatricula, cdPrograma, null);
	}

	public static MatriculaPrograma get(int cdMatricula, int cdPrograma, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_programa WHERE cd_matricula=? AND cd_programa=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdPrograma);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MatriculaPrograma(rs.getInt("cd_matricula"),
						rs.getInt("cd_programa"),
						rs.getString("nr_matricula_programa"),
						(rs.getTimestamp("dt_inicio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio").getTime()),
						(rs.getTimestamp("dt_termino")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_termino").getTime()),
						rs.getInt("st_matricula_programa"),
						rs.getInt("cd_turma"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaProgramaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaProgramaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_programa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaProgramaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaProgramaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<MatriculaPrograma> getList() {
		return getList(null);
	}

	public static ArrayList<MatriculaPrograma> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<MatriculaPrograma> list = new ArrayList<MatriculaPrograma>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				MatriculaPrograma obj = MatriculaProgramaDAO.get(rsm.getInt("cd_matricula"), rsm.getInt("cd_programa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaProgramaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_matricula_programa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
