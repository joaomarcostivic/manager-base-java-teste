package com.tivic.manager.psq;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class GrupoQuestionarioDAO{

	public static int insert(GrupoQuestionario objeto) {
		return insert(objeto, null);
	}

	public static int insert(GrupoQuestionario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("psq_grupo_questionario", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdGrupoQuestionario(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO psq_grupo_questionario (cd_grupo_questionario,"+
			                                  "cd_pessoa,"+
			                                  "nm_grupo,"+
			                                  "id_grupo) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setString(3,objeto.getNmGrupo());
			pstmt.setString(4,objeto.getIdGrupo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoQuestionarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoQuestionarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(GrupoQuestionario objeto) {
		return update(objeto, 0, null);
	}

	public static int update(GrupoQuestionario objeto, int cdGrupoQuestionarioOld) {
		return update(objeto, cdGrupoQuestionarioOld, null);
	}

	public static int update(GrupoQuestionario objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(GrupoQuestionario objeto, int cdGrupoQuestionarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE psq_grupo_questionario SET cd_grupo_questionario=?,"+
												      		   "cd_pessoa=?,"+
												      		   "nm_grupo=?,"+
												      		   "id_grupo=? WHERE cd_grupo_questionario=?");
			pstmt.setInt(1,objeto.getCdGrupoQuestionario());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setString(3,objeto.getNmGrupo());
			pstmt.setString(4,objeto.getIdGrupo());
			pstmt.setInt(5, cdGrupoQuestionarioOld!=0 ? cdGrupoQuestionarioOld : objeto.getCdGrupoQuestionario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoQuestionarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoQuestionarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdGrupoQuestionario) {
		return delete(cdGrupoQuestionario, null);
	}

	public static int delete(int cdGrupoQuestionario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM psq_grupo_questionario WHERE cd_grupo_questionario=?");
			pstmt.setInt(1, cdGrupoQuestionario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoQuestionarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoQuestionarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static GrupoQuestionario get(int cdGrupoQuestionario) {
		return get(cdGrupoQuestionario, null);
	}

	public static GrupoQuestionario get(int cdGrupoQuestionario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM psq_grupo_questionario WHERE cd_grupo_questionario=?");
			pstmt.setInt(1, cdGrupoQuestionario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new GrupoQuestionario(rs.getInt("cd_grupo_questionario"),
						rs.getInt("cd_pessoa"),
						rs.getString("nm_grupo"),
						rs.getString("id_grupo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoQuestionarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoQuestionarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM psq_grupo_questionario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoQuestionarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoQuestionarioDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM psq_grupo_questionario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
