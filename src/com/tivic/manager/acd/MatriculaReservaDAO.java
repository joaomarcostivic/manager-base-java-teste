package com.tivic.manager.acd;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class MatriculaReservaDAO{

	public static int insert(MatriculaReserva objeto) {
		return insert(objeto, null);
	}

	public static int insert(MatriculaReserva objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_matricula_reserva", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdReserva(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_matricula_reserva (cd_reserva,"+
			                                  "id_reserva,"+
			                                  "dt_reserva,"+
			                                  "dt_validade,"+
			                                  "st_reserva,"+
			                                  "cd_aluno,"+
			                                  "cd_contrato,"+
			                                  "txt_observacao,"+
			                                  "cd_instituicao,"+
			                                  "cd_curso,"+
			                                  "cd_turma,"+
			                                  "cd_usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getIdReserva());
			if(objeto.getDtReserva()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtReserva().getTimeInMillis()));
			if(objeto.getDtValidade()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtValidade().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStReserva());
			if(objeto.getCdAluno()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdAluno());
			if(objeto.getCdContrato()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdContrato());
			pstmt.setString(8,objeto.getTxtObservacao());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdInstituicao());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdCurso());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdTurma());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdUsuario());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaReservaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaReservaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MatriculaReserva objeto) {
		return update(objeto, 0, null);
	}

	public static int update(MatriculaReserva objeto, int cdReservaOld) {
		return update(objeto, cdReservaOld, null);
	}

	public static int update(MatriculaReserva objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(MatriculaReserva objeto, int cdReservaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_matricula_reserva SET cd_reserva=?,"+
												      		   "id_reserva=?,"+
												      		   "dt_reserva=?,"+
												      		   "dt_validade=?,"+
												      		   "st_reserva=?,"+
												      		   "cd_aluno=?,"+
												      		   "cd_contrato=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_instituicao=?,"+
												      		   "cd_curso=?,"+
												      		   "cd_turma=?,"+
												      		   "cd_usuario=? WHERE cd_reserva=?");
			pstmt.setInt(1,objeto.getCdReserva());
			pstmt.setString(2,objeto.getIdReserva());
			if(objeto.getDtReserva()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtReserva().getTimeInMillis()));
			if(objeto.getDtValidade()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtValidade().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStReserva());
			if(objeto.getCdAluno()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdAluno());
			if(objeto.getCdContrato()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdContrato());
			pstmt.setString(8,objeto.getTxtObservacao());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdInstituicao());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdCurso());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdTurma());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdUsuario());
			pstmt.setInt(13, cdReservaOld!=0 ? cdReservaOld : objeto.getCdReserva());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaReservaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaReservaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdReserva) {
		return delete(cdReserva, null);
	}

	public static int delete(int cdReserva, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_matricula_reserva WHERE cd_reserva=?");
			pstmt.setInt(1, cdReserva);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaReservaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaReservaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MatriculaReserva get(int cdReserva) {
		return get(cdReserva, null);
	}

	public static MatriculaReserva get(int cdReserva, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_reserva WHERE cd_reserva=?");
			pstmt.setInt(1, cdReserva);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MatriculaReserva(rs.getInt("cd_reserva"),
						rs.getString("id_reserva"),
						(rs.getTimestamp("dt_reserva")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_reserva").getTime()),
						(rs.getTimestamp("dt_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_validade").getTime()),
						rs.getInt("st_reserva"),
						rs.getInt("cd_aluno"),
						rs.getInt("cd_contrato"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_instituicao"),
						rs.getInt("cd_curso"),
						rs.getInt("cd_turma"),
						rs.getInt("cd_usuario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaReservaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaReservaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_reserva");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaReservaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaReservaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_matricula_reserva", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
