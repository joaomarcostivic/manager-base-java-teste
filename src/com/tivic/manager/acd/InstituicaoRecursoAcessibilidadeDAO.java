package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class InstituicaoRecursoAcessibilidadeDAO{

	public static int insert(InstituicaoRecursoAcessibilidade objeto) {
		return insert(objeto, null);
	}

	public static int insert(InstituicaoRecursoAcessibilidade objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_instituicao");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_tipo_recurso_acessibilidade");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdTipoRecursoAcessibilidade()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_periodo_letivo");
			keys[2].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("acd_instituicao_recurso_acessibilidade", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdInstituicao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_recurso_acessibilidade (cd_instituicao,"+
			                                  "cd_tipo_recurso_acessibilidade,"+
			                                  "cd_periodo_letivo) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTipoRecursoAcessibilidade()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoRecursoAcessibilidade());
			pstmt.setInt(3, code);
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoAcessibilidadeDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoAcessibilidadeDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InstituicaoRecursoAcessibilidade objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(InstituicaoRecursoAcessibilidade objeto, int cdInstituicaoOld, int cdTipoRecursoAcessibilidadeOld, int cdPeriodoLetivoOld) {
		return update(objeto, cdInstituicaoOld, cdTipoRecursoAcessibilidadeOld, cdPeriodoLetivoOld, null);
	}

	public static int update(InstituicaoRecursoAcessibilidade objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(InstituicaoRecursoAcessibilidade objeto, int cdInstituicaoOld, int cdTipoRecursoAcessibilidadeOld, int cdPeriodoLetivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_instituicao_recurso_acessibilidade SET cd_instituicao=?,"+
												      		   "cd_tipo_recurso_acessibilidade=?,"+
												      		   "cd_periodo_letivo=? WHERE cd_instituicao=? AND cd_tipo_recurso_acessibilidade=? AND cd_periodo_letivo=?");
			pstmt.setInt(1,objeto.getCdInstituicao());
			pstmt.setInt(2,objeto.getCdTipoRecursoAcessibilidade());
			pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			pstmt.setInt(4, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.setInt(5, cdTipoRecursoAcessibilidadeOld!=0 ? cdTipoRecursoAcessibilidadeOld : objeto.getCdTipoRecursoAcessibilidade());
			pstmt.setInt(6, cdPeriodoLetivoOld!=0 ? cdPeriodoLetivoOld : objeto.getCdPeriodoLetivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoAcessibilidadeDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoAcessibilidadeDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdInstituicao, int cdTipoRecursoAcessibilidade, int cdPeriodoLetivo) {
		return delete(cdInstituicao, cdTipoRecursoAcessibilidade, cdPeriodoLetivo, null);
	}

	public static int delete(int cdInstituicao, int cdTipoRecursoAcessibilidade, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao_recurso_acessibilidade WHERE cd_instituicao=? AND cd_tipo_recurso_acessibilidade=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdTipoRecursoAcessibilidade);
			pstmt.setInt(3, cdPeriodoLetivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoAcessibilidadeDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoAcessibilidadeDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoRecursoAcessibilidade get(int cdInstituicao, int cdTipoRecursoAcessibilidade, int cdPeriodoLetivo) {
		return get(cdInstituicao, cdTipoRecursoAcessibilidade, cdPeriodoLetivo, null);
	}

	public static InstituicaoRecursoAcessibilidade get(int cdInstituicao, int cdTipoRecursoAcessibilidade, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_recurso_acessibilidade WHERE cd_instituicao=? AND cd_tipo_recurso_acessibilidade=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdTipoRecursoAcessibilidade);
			pstmt.setInt(3, cdPeriodoLetivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InstituicaoRecursoAcessibilidade(rs.getInt("cd_instituicao"),
						rs.getInt("cd_tipo_recurso_acessibilidade"),
						rs.getInt("cd_periodo_letivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoAcessibilidadeDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoAcessibilidadeDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_recurso_acessibilidade");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoAcessibilidadeDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoAcessibilidadeDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InstituicaoRecursoAcessibilidade> getList() {
		return getList(null);
	}

	public static ArrayList<InstituicaoRecursoAcessibilidade> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InstituicaoRecursoAcessibilidade> list = new ArrayList<InstituicaoRecursoAcessibilidade>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InstituicaoRecursoAcessibilidade obj = InstituicaoRecursoAcessibilidadeDAO.get(rsm.getInt("cd_instituicao"), rsm.getInt("cd_tipo_recurso_acessibilidade"), rsm.getInt("cd_periodo_letivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoAcessibilidadeDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_recurso_acessibilidade", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}