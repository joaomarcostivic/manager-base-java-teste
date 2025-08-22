package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class AlunoForaDaRedeDAO{

	public static int insert(AlunoForaDaRede objeto) {
		return insert(objeto, null);
	}

	public static int insert(AlunoForaDaRede objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_aluno_fora_da_rede (cd_aluno,"+
			                                  "cd_instituicao,"+
			                                  "cd_turma,"+
			                                  "cd_matricula,"+
			                                  "tp_situacao,"+
			                                  "dt_registro) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdAluno()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAluno());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInstituicao());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTurma());
			if(objeto.getCdMatricula()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdMatricula());
			pstmt.setInt(5,objeto.getTpSituacao());
			if(objeto.getDtRegistro()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtRegistro().getTimeInMillis()));
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoForaDaRedeDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoForaDaRedeDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AlunoForaDaRede objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(AlunoForaDaRede objeto, int cdAlunoOld, int cdInstituicaoOld, int cdTurmaOld, int cdMatriculaOld) {
		return update(objeto, cdAlunoOld, cdInstituicaoOld, cdTurmaOld, cdMatriculaOld, null);
	}

	public static int update(AlunoForaDaRede objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(AlunoForaDaRede objeto, int cdAlunoOld, int cdInstituicaoOld, int cdTurmaOld, int cdMatriculaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_aluno_fora_da_rede SET cd_aluno=?,"+
												      		   "cd_instituicao=?,"+
												      		   "cd_turma=?,"+
												      		   "cd_matricula=?,"+
												      		   "tp_situacao=?,"+
												      		   "dt_registro=? WHERE cd_aluno=? AND cd_instituicao=? AND cd_turma=? AND cd_matricula=?");
			pstmt.setInt(1,objeto.getCdAluno());
			pstmt.setInt(2,objeto.getCdInstituicao());
			pstmt.setInt(3,objeto.getCdTurma());
			pstmt.setInt(4,objeto.getCdMatricula());
			pstmt.setInt(5,objeto.getTpSituacao());
			if(objeto.getDtRegistro()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtRegistro().getTimeInMillis()));
			pstmt.setInt(7, cdAlunoOld!=0 ? cdAlunoOld : objeto.getCdAluno());
			pstmt.setInt(8, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.setInt(9, cdTurmaOld!=0 ? cdTurmaOld : objeto.getCdTurma());
			pstmt.setInt(10, cdMatriculaOld!=0 ? cdMatriculaOld : objeto.getCdMatricula());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoForaDaRedeDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoForaDaRedeDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAluno, int cdInstituicao, int cdTurma, int cdMatricula) {
		return delete(cdAluno, cdInstituicao, cdTurma, cdMatricula, null);
	}

	public static int delete(int cdAluno, int cdInstituicao, int cdTurma, int cdMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_aluno_fora_da_rede WHERE cd_aluno=? AND cd_instituicao=? AND cd_turma=? AND cd_matricula=?");
			pstmt.setInt(1, cdAluno);
			pstmt.setInt(2, cdInstituicao);
			pstmt.setInt(3, cdTurma);
			pstmt.setInt(4, cdMatricula);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoForaDaRedeDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoForaDaRedeDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AlunoForaDaRede get(int cdAluno, int cdInstituicao, int cdTurma, int cdMatricula) {
		return get(cdAluno, cdInstituicao, cdTurma, cdMatricula, null);
	}

	public static AlunoForaDaRede get(int cdAluno, int cdInstituicao, int cdTurma, int cdMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_aluno_fora_da_rede WHERE cd_aluno=? AND cd_instituicao=? AND cd_turma=? AND cd_matricula=?");
			pstmt.setInt(1, cdAluno);
			pstmt.setInt(2, cdInstituicao);
			pstmt.setInt(3, cdTurma);
			pstmt.setInt(4, cdMatricula);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AlunoForaDaRede(rs.getInt("cd_aluno"),
						rs.getInt("cd_instituicao"),
						rs.getInt("cd_turma"),
						rs.getInt("cd_matricula"),
						rs.getInt("tp_situacao"),
						(rs.getTimestamp("dt_registro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_registro").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoForaDaRedeDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoForaDaRedeDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_aluno_fora_da_rede");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoForaDaRedeDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoForaDaRedeDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AlunoForaDaRede> getList() {
		return getList(null);
	}

	public static ArrayList<AlunoForaDaRede> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AlunoForaDaRede> list = new ArrayList<AlunoForaDaRede>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AlunoForaDaRede obj = AlunoForaDaRedeDAO.get(rsm.getInt("cd_aluno"), rsm.getInt("cd_instituicao"), rsm.getInt("cd_turma"), rsm.getInt("cd_matricula"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoForaDaRedeDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_aluno_fora_da_rede", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}