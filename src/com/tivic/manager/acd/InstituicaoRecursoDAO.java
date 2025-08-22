package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class InstituicaoRecursoDAO{

	public static int insert(InstituicaoRecurso objeto) {
		return insert(objeto, null);
	}

	public static int insert(InstituicaoRecurso objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_instituicao_recurso");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_instituicao");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdInstituicao()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_recurso");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdRecurso()));
			int code = Conexao.getSequenceCode("acd_instituicao_recurso", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdInstituicaoRecurso(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_recurso (cd_instituicao_recurso,"+
			                                  "cd_instituicao,"+
			                                  "cd_recurso,"+
			                                  "cd_tipo_recurso,"+
			                                  "cd_usuario,"+
			                                  "dt_entrega,"+
			                                  "dt_previsao_devolucao,"+
			                                  "dt_devolucao,"+
			                                  "st_instituicao_recurso,"+
			                                  "txt_observacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInstituicao());
			if(objeto.getCdRecurso()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdRecurso());
			if(objeto.getCdTipoRecurso()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTipoRecurso());
			pstmt.setInt(5,objeto.getCdUsuario());
			if(objeto.getDtEntrega()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtEntrega().getTimeInMillis()));
			if(objeto.getDtPrevisaoDevolucao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtPrevisaoDevolucao().getTimeInMillis()));
			if(objeto.getDtDevolucao()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtDevolucao().getTimeInMillis()));
			pstmt.setInt(9,objeto.getStInstituicaoRecurso());
			pstmt.setString(10,objeto.getTxtObservacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InstituicaoRecurso objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(InstituicaoRecurso objeto, int cdInstituicaoRecursoOld, int cdInstituicaoOld, int cdRecursoOld) {
		return update(objeto, cdInstituicaoRecursoOld, cdInstituicaoOld, cdRecursoOld, null);
	}

	public static int update(InstituicaoRecurso objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(InstituicaoRecurso objeto, int cdInstituicaoRecursoOld, int cdInstituicaoOld, int cdRecursoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_instituicao_recurso SET cd_instituicao_recurso=?,"+
												      		   "cd_instituicao=?,"+
												      		   "cd_recurso=?,"+
												      		   "cd_tipo_recurso=?,"+
												      		   "cd_usuario=?,"+
												      		   "dt_entrega=?,"+
												      		   "dt_previsao_devolucao=?,"+
												      		   "dt_devolucao=?,"+
												      		   "st_instituicao_recurso=?,"+
												      		   "txt_observacao=? WHERE cd_instituicao_recurso=? AND cd_instituicao=? AND cd_recurso=?");
			pstmt.setInt(1,objeto.getCdInstituicaoRecurso());
			pstmt.setInt(2,objeto.getCdInstituicao());
			pstmt.setInt(3,objeto.getCdRecurso());
			if(objeto.getCdTipoRecurso()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTipoRecurso());
			pstmt.setInt(5,objeto.getCdUsuario());
			if(objeto.getDtEntrega()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtEntrega().getTimeInMillis()));
			if(objeto.getDtPrevisaoDevolucao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtPrevisaoDevolucao().getTimeInMillis()));
			if(objeto.getDtDevolucao()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtDevolucao().getTimeInMillis()));
			pstmt.setInt(9,objeto.getStInstituicaoRecurso());
			pstmt.setString(10,objeto.getTxtObservacao());
			pstmt.setInt(11, cdInstituicaoRecursoOld!=0 ? cdInstituicaoRecursoOld : objeto.getCdInstituicaoRecurso());
			pstmt.setInt(12, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.setInt(13, cdRecursoOld!=0 ? cdRecursoOld : objeto.getCdRecurso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdInstituicaoRecurso, int cdInstituicao, int cdRecurso) {
		return delete(cdInstituicaoRecurso, cdInstituicao, cdRecurso, null);
	}

	public static int delete(int cdInstituicaoRecurso, int cdInstituicao, int cdRecurso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao_recurso WHERE cd_instituicao_recurso=? AND cd_instituicao=? AND cd_recurso=?");
			pstmt.setInt(1, cdInstituicaoRecurso);
			pstmt.setInt(2, cdInstituicao);
			pstmt.setInt(3, cdRecurso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoRecurso get(int cdInstituicaoRecurso, int cdInstituicao, int cdRecurso) {
		return get(cdInstituicaoRecurso, cdInstituicao, cdRecurso, null);
	}

	public static InstituicaoRecurso get(int cdInstituicaoRecurso, int cdInstituicao, int cdRecurso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_recurso WHERE cd_instituicao_recurso=? AND cd_instituicao=? AND cd_recurso=?");
			pstmt.setInt(1, cdInstituicaoRecurso);
			pstmt.setInt(2, cdInstituicao);
			pstmt.setInt(3, cdRecurso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InstituicaoRecurso(rs.getInt("cd_instituicao_recurso"),
						rs.getInt("cd_instituicao"),
						rs.getInt("cd_recurso"),
						rs.getInt("cd_tipo_recurso"),
						rs.getInt("cd_usuario"),
						(rs.getTimestamp("dt_entrega")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_entrega").getTime()),
						(rs.getTimestamp("dt_previsao_devolucao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_previsao_devolucao").getTime()),
						(rs.getTimestamp("dt_devolucao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_devolucao").getTime()),
						rs.getInt("st_instituicao_recurso"),
						rs.getString("txt_observacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_recurso");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InstituicaoRecurso> getList() {
		return getList(null);
	}

	public static ArrayList<InstituicaoRecurso> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InstituicaoRecurso> list = new ArrayList<InstituicaoRecurso>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InstituicaoRecurso obj = InstituicaoRecursoDAO.get(rsm.getInt("cd_instituicao_recurso"), rsm.getInt("cd_instituicao"), rsm.getInt("cd_recurso"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoRecursoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_recurso", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}