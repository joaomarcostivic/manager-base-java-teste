package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class PreMatriculaDAO{

	public static int insert(PreMatricula objeto) {
		return insert(objeto, null);
	}

	public static int insert(PreMatricula objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_pre_matricula", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPreMatricula(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_pre_matricula (cd_pre_matricula,"+
			                                  "cd_instituicao_inscricao,"+
			                                  "cd_instituicao,"+
			                                  "cd_curso,"+
			                                  "cd_aluno,"+
			                                  "st_pre_matricula,"+
			                                  "tp_pre_matricula,"+
			                                  "dt_inscricao,"+
			                                  "tp_procedencia,"+
			                                  "lg_irmao_instituicao,"+
			                                  "lg_responsavel_trabalhador,"+
			                                  "id_pre_matricula) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdInstituicaoInscricao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInstituicaoInscricao());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdInstituicao());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCurso());
			if(objeto.getCdAluno()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdAluno());
			pstmt.setInt(6,objeto.getStPreMatricula());
			pstmt.setInt(7,objeto.getTpPreMatricula());
			if(objeto.getDtInscricao()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtInscricao().getTimeInMillis()));
			pstmt.setInt(9,objeto.getTpProcedencia());
			pstmt.setInt(10,objeto.getLgIrmaoInstituicao());
			pstmt.setInt(11,objeto.getLgResponsavelTrabalhador());
			pstmt.setString(12,objeto.getIdPreMatricula());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PreMatriculaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PreMatriculaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PreMatricula objeto) {
		return update(objeto, 0, null);
	}

	public static int update(PreMatricula objeto, int cdPreMatriculaOld) {
		return update(objeto, cdPreMatriculaOld, null);
	}

	public static int update(PreMatricula objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(PreMatricula objeto, int cdPreMatriculaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_pre_matricula SET cd_pre_matricula=?,"+
												      		   "cd_instituicao_inscricao=?,"+
												      		   "cd_instituicao=?,"+
												      		   "cd_curso=?,"+
												      		   "cd_aluno=?,"+
												      		   "st_pre_matricula=?,"+
												      		   "tp_pre_matricula=?,"+
												      		   "dt_inscricao=?,"+
												      		   "tp_procedencia=?,"+
												      		   "lg_irmao_instituicao=?,"+
												      		   "lg_responsavel_trabalhador=?,"+
												      		   "id_pre_matricula=? WHERE cd_pre_matricula=?");
			pstmt.setInt(1,objeto.getCdPreMatricula());
			if(objeto.getCdInstituicaoInscricao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInstituicaoInscricao());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdInstituicao());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCurso());
			if(objeto.getCdAluno()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdAluno());
			pstmt.setInt(6,objeto.getStPreMatricula());
			pstmt.setInt(7,objeto.getTpPreMatricula());
			if(objeto.getDtInscricao()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtInscricao().getTimeInMillis()));
			pstmt.setInt(9,objeto.getTpProcedencia());
			pstmt.setInt(10,objeto.getLgIrmaoInstituicao());
			pstmt.setInt(11,objeto.getLgResponsavelTrabalhador());
			pstmt.setString(12,objeto.getIdPreMatricula());
			pstmt.setInt(13, cdPreMatriculaOld!=0 ? cdPreMatriculaOld : objeto.getCdPreMatricula());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PreMatriculaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PreMatriculaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPreMatricula) {
		return delete(cdPreMatricula, null);
	}

	public static int delete(int cdPreMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_pre_matricula WHERE cd_pre_matricula=?");
			pstmt.setInt(1, cdPreMatricula);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PreMatriculaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PreMatriculaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PreMatricula get(int cdPreMatricula) {
		return get(cdPreMatricula, null);
	}

	public static PreMatricula get(int cdPreMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_pre_matricula WHERE cd_pre_matricula=?");
			pstmt.setInt(1, cdPreMatricula);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PreMatricula(rs.getInt("cd_pre_matricula"),
						rs.getInt("cd_instituicao_inscricao"),
						rs.getInt("cd_instituicao"),
						rs.getInt("cd_curso"),
						rs.getInt("cd_aluno"),
						rs.getInt("st_pre_matricula"),
						rs.getInt("tp_pre_matricula"),
						(rs.getTimestamp("dt_inscricao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inscricao").getTime()),
						rs.getInt("tp_procedencia"),
						rs.getInt("lg_irmao_instituicao"),
						rs.getInt("lg_responsavel_trabalhador"),
						rs.getString("id_pre_matricula"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PreMatriculaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PreMatriculaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_pre_matricula");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PreMatriculaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PreMatriculaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PreMatricula> getList() {
		return getList(null);
	}

	public static ArrayList<PreMatricula> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PreMatricula> list = new ArrayList<PreMatricula>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PreMatricula obj = PreMatriculaDAO.get(rsm.getInt("cd_pre_matricula"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PreMatriculaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_pre_matricula", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
