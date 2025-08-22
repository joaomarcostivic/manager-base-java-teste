package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class DisciplinaAvaliacaoDAO{

	public static int insert(DisciplinaAvaliacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(DisciplinaAvaliacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[5];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_disciplina_avaliacao");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_curso");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdCurso()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_curso_modulo");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdCursoModulo()));
			keys[3] = new HashMap<String,Object>();
			keys[3].put("FIELD_NAME", "cd_disciplina");
			keys[3].put("IS_KEY_NATIVE", "NO");
			keys[3].put("FIELD_VALUE", new Integer(objeto.getCdDisciplina()));
			keys[4] = new HashMap<String,Object>();
			keys[4].put("FIELD_NAME", "cd_matriz");
			keys[4].put("IS_KEY_NATIVE", "NO");
			keys[4].put("FIELD_VALUE", new Integer(objeto.getCdMatriz()));
			int code = Conexao.getSequenceCode("acd_disciplina_avaliacao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDisciplinaAvaliacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_disciplina_avaliacao (cd_disciplina_avaliacao,"+
			                                  "cd_curso,"+
			                                  "cd_curso_modulo,"+
			                                  "cd_disciplina,"+
			                                  "cd_matriz,"+
			                                  "cd_unidade,"+
			                                  "cd_tipo_avaliacao,"+
			                                  "nm_avaliacao,"+
			                                  "txt_observacao,"+
			                                  "vl_peso,"+
			                                  "id_avaliacao,"+
			                                  "cd_formulario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdCurso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCurso());
			if(objeto.getCdCursoModulo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCursoModulo());
			if(objeto.getCdDisciplina()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdDisciplina());
			if(objeto.getCdMatriz()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdMatriz());
			if(objeto.getCdUnidade()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdUnidade());
			if(objeto.getCdTipoAvaliacao()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTipoAvaliacao());
			pstmt.setString(8,objeto.getNmAvaliacao());
			pstmt.setString(9,objeto.getTxtObservacao());
			pstmt.setFloat(10,objeto.getVlPeso());
			pstmt.setString(11,objeto.getIdAvaliacao());
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdFormulario());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DisciplinaAvaliacao objeto) {
		return update(objeto, 0, 0, 0, 0, 0, null);
	}

	public static int update(DisciplinaAvaliacao objeto, int cdDisciplinaAvaliacaoOld, int cdCursoOld, int cdCursoModuloOld, int cdDisciplinaOld, int cdMatrizOld) {
		return update(objeto, cdDisciplinaAvaliacaoOld, cdCursoOld, cdCursoModuloOld, cdDisciplinaOld, cdMatrizOld, null);
	}

	public static int update(DisciplinaAvaliacao objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, 0, connect);
	}

	public static int update(DisciplinaAvaliacao objeto, int cdDisciplinaAvaliacaoOld, int cdCursoOld, int cdCursoModuloOld, int cdDisciplinaOld, int cdMatrizOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_disciplina_avaliacao SET cd_disciplina_avaliacao=?,"+
												      		   "cd_curso=?,"+
												      		   "cd_curso_modulo=?,"+
												      		   "cd_disciplina=?,"+
												      		   "cd_matriz=?,"+
												      		   "cd_unidade=?,"+
												      		   "cd_tipo_avaliacao=?,"+
												      		   "nm_avaliacao=?,"+
												      		   "txt_observacao=?,"+
												      		   "vl_peso=?,"+
												      		   "id_avaliacao=?,"+
												      		   "cd_formulario=? WHERE cd_disciplina_avaliacao=? AND cd_curso=? AND cd_curso_modulo=? AND cd_disciplina=? AND cd_matriz=?");
			pstmt.setInt(1,objeto.getCdDisciplinaAvaliacao());
			pstmt.setInt(2,objeto.getCdCurso());
			pstmt.setInt(3,objeto.getCdCursoModulo());
			pstmt.setInt(4,objeto.getCdDisciplina());
			pstmt.setInt(5,objeto.getCdMatriz());
			if(objeto.getCdUnidade()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdUnidade());
			if(objeto.getCdTipoAvaliacao()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTipoAvaliacao());
			pstmt.setString(8,objeto.getNmAvaliacao());
			pstmt.setString(9,objeto.getTxtObservacao());
			pstmt.setFloat(10,objeto.getVlPeso());
			pstmt.setString(11,objeto.getIdAvaliacao());
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdFormulario());
			pstmt.setInt(13, cdDisciplinaAvaliacaoOld!=0 ? cdDisciplinaAvaliacaoOld : objeto.getCdDisciplinaAvaliacao());
			pstmt.setInt(14, cdCursoOld!=0 ? cdCursoOld : objeto.getCdCurso());
			pstmt.setInt(15, cdCursoModuloOld!=0 ? cdCursoModuloOld : objeto.getCdCursoModulo());
			pstmt.setInt(16, cdDisciplinaOld!=0 ? cdDisciplinaOld : objeto.getCdDisciplina());
			pstmt.setInt(17, cdMatrizOld!=0 ? cdMatrizOld : objeto.getCdMatriz());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDisciplinaAvaliacao) {
		return delete(cdDisciplinaAvaliacao, null);
	}

	public static int delete(int cdDisciplinaAvaliacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_disciplina_avaliacao WHERE cd_disciplina_avaliacao=?");
			pstmt.setInt(1, cdDisciplinaAvaliacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DisciplinaAvaliacao get(int cdDisciplinaAvaliacao, int cdCurso, int cdCursoModulo, int cdDisciplina, int cdMatriz) {
		return get(cdDisciplinaAvaliacao, cdCurso, cdCursoModulo, cdDisciplina, cdMatriz, null);
	}

	public static DisciplinaAvaliacao get(int cdDisciplinaAvaliacao, int cdCurso, int cdCursoModulo, int cdDisciplina, int cdMatriz, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_disciplina_avaliacao WHERE cd_disciplina_avaliacao=? AND cd_curso=? AND cd_curso_modulo=? AND cd_disciplina=? AND cd_matriz=?");
			pstmt.setInt(1, cdDisciplinaAvaliacao);
			pstmt.setInt(2, cdCurso);
			pstmt.setInt(3, cdCursoModulo);
			pstmt.setInt(4, cdDisciplina);
			pstmt.setInt(5, cdMatriz);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DisciplinaAvaliacao(rs.getInt("cd_disciplina_avaliacao"),
						rs.getInt("cd_curso"),
						rs.getInt("cd_curso_modulo"),
						rs.getInt("cd_disciplina"),
						rs.getInt("cd_matriz"),
						rs.getInt("cd_unidade"),
						rs.getInt("cd_tipo_avaliacao"),
						rs.getString("nm_avaliacao"),
						rs.getString("txt_observacao"),
						rs.getFloat("vl_peso"),
						rs.getString("id_avaliacao"),
						rs.getInt("cd_formulario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_disciplina_avaliacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<DisciplinaAvaliacao> getList() {
		return getList(null);
	}

	public static ArrayList<DisciplinaAvaliacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<DisciplinaAvaliacao> list = new ArrayList<DisciplinaAvaliacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				DisciplinaAvaliacao obj = DisciplinaAvaliacaoDAO.get(rsm.getInt("cd_disciplina_avaliacao"), rsm.getInt("cd_curso"), rsm.getInt("cd_curso_modulo"), rsm.getInt("cd_disciplina"), rsm.getInt("cd_matriz"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_disciplina_avaliacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}