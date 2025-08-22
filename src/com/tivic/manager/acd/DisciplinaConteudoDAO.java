package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class DisciplinaConteudoDAO{

	public static int insert(DisciplinaConteudo objeto) {
		return insert(objeto, null);
	}

	public static int insert(DisciplinaConteudo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_disciplina_conteudo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdConteudo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_disciplina_conteudo (cd_conteudo,"+
			                                  "cd_conteudo_superior,"+
			                                  "txt_conteudo,"+
			                                  "tp_conteudo,"+
			                                  "cd_matriz,"+
			                                  "cd_curso,"+
			                                  "cd_curso_periodo,"+
			                                  "cd_disciplina) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdConteudoSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConteudoSuperior());
			pstmt.setString(3,objeto.getTxtConteudo());
			pstmt.setInt(4,objeto.getTpConteudo());
			if(objeto.getCdMatriz()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdMatriz());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCurso());
			if(objeto.getCdCursoPeriodo()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdCursoPeriodo());
			if(objeto.getCdDisciplina()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdDisciplina());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaConteudoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaConteudoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DisciplinaConteudo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(DisciplinaConteudo objeto, int cdConteudoOld) {
		return update(objeto, cdConteudoOld, null);
	}

	public static int update(DisciplinaConteudo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(DisciplinaConteudo objeto, int cdConteudoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_disciplina_conteudo SET cd_conteudo=?,"+
												      		   "cd_conteudo_superior=?,"+
												      		   "txt_conteudo=?,"+
												      		   "tp_conteudo=?,"+
												      		   "cd_matriz=?,"+
												      		   "cd_curso=?,"+
												      		   "cd_curso_periodo=?,"+
												      		   "cd_disciplina=? WHERE cd_conteudo=?");
			pstmt.setInt(1,objeto.getCdConteudo());
			if(objeto.getCdConteudoSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConteudoSuperior());
			pstmt.setString(3,objeto.getTxtConteudo());
			pstmt.setInt(4,objeto.getTpConteudo());
			if(objeto.getCdMatriz()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdMatriz());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCurso());
			if(objeto.getCdCursoPeriodo()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdCursoPeriodo());
			if(objeto.getCdDisciplina()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdDisciplina());
			pstmt.setInt(9, cdConteudoOld!=0 ? cdConteudoOld : objeto.getCdConteudo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaConteudoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaConteudoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConteudo) {
		return delete(cdConteudo, null);
	}

	public static int delete(int cdConteudo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_disciplina_conteudo WHERE cd_conteudo=?");
			pstmt.setInt(1, cdConteudo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaConteudoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaConteudoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DisciplinaConteudo get(int cdConteudo) {
		return get(cdConteudo, null);
	}

	public static DisciplinaConteudo get(int cdConteudo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_disciplina_conteudo WHERE cd_conteudo=?");
			pstmt.setInt(1, cdConteudo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DisciplinaConteudo(rs.getInt("cd_conteudo"),
						rs.getInt("cd_conteudo_superior"),
						rs.getString("txt_conteudo"),
						rs.getInt("tp_conteudo"),
						rs.getInt("cd_matriz"),
						rs.getInt("cd_curso"),
						rs.getInt("cd_curso_periodo"),
						rs.getInt("cd_disciplina"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaConteudoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaConteudoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_disciplina_conteudo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaConteudoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaConteudoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<DisciplinaConteudo> getList() {
		return getList(null);
	}

	public static ArrayList<DisciplinaConteudo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<DisciplinaConteudo> list = new ArrayList<DisciplinaConteudo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				DisciplinaConteudo obj = DisciplinaConteudoDAO.get(rsm.getInt("cd_conteudo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaConteudoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_disciplina_conteudo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
