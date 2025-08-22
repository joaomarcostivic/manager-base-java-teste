package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class AlunoRecursoProvaDAO{

	public static int insert(AlunoRecursoProva objeto) {
		return insert(objeto, null);
	}

	public static int insert(AlunoRecursoProva objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_aluno_recurso_prova (cd_aluno,"+
			                                  "cd_tipo_recurso_prova) VALUES (?, ?)");
			if(objeto.getCdAluno()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAluno());
			if(objeto.getCdTipoRecursoProva()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoRecursoProva());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoRecursoProvaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoRecursoProvaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AlunoRecursoProva objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AlunoRecursoProva objeto, int cdAlunoOld, int cdTipoRecursoProvaOld) {
		return update(objeto, cdAlunoOld, cdTipoRecursoProvaOld, null);
	}

	public static int update(AlunoRecursoProva objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AlunoRecursoProva objeto, int cdAlunoOld, int cdTipoRecursoProvaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_aluno_recurso_prova SET cd_aluno=?,"+
												      		   "cd_tipo_recurso_prova=? WHERE cd_aluno=? AND cd_tipo_recurso_prova=?");
			pstmt.setInt(1,objeto.getCdAluno());
			pstmt.setInt(2,objeto.getCdTipoRecursoProva());
			pstmt.setInt(3, cdAlunoOld!=0 ? cdAlunoOld : objeto.getCdAluno());
			pstmt.setInt(4, cdTipoRecursoProvaOld!=0 ? cdTipoRecursoProvaOld : objeto.getCdTipoRecursoProva());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoRecursoProvaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoRecursoProvaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAluno, int cdTipoRecursoProva) {
		return delete(cdAluno, cdTipoRecursoProva, null);
	}

	public static int delete(int cdAluno, int cdTipoRecursoProva, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_aluno_recurso_prova WHERE cd_aluno=? AND cd_tipo_recurso_prova=?");
			pstmt.setInt(1, cdAluno);
			pstmt.setInt(2, cdTipoRecursoProva);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoRecursoProvaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoRecursoProvaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AlunoRecursoProva get(int cdAluno, int cdTipoRecursoProva) {
		return get(cdAluno, cdTipoRecursoProva, null);
	}

	public static AlunoRecursoProva get(int cdAluno, int cdTipoRecursoProva, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_aluno_recurso_prova WHERE cd_aluno=? AND cd_tipo_recurso_prova=?");
			pstmt.setInt(1, cdAluno);
			pstmt.setInt(2, cdTipoRecursoProva);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AlunoRecursoProva(rs.getInt("cd_aluno"),
						rs.getInt("cd_tipo_recurso_prova"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoRecursoProvaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoRecursoProvaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_aluno_recurso_prova");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoRecursoProvaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoRecursoProvaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AlunoRecursoProva> getList() {
		return getList(null);
	}

	public static ArrayList<AlunoRecursoProva> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AlunoRecursoProva> list = new ArrayList<AlunoRecursoProva>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AlunoRecursoProva obj = AlunoRecursoProvaDAO.get(rsm.getInt("cd_aluno"), rsm.getInt("cd_tipo_recurso_prova"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoRecursoProvaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_aluno_recurso_prova", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
