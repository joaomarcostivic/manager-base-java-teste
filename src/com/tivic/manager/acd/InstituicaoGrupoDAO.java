package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class InstituicaoGrupoDAO{

	public static int insert(InstituicaoGrupo objeto) {
		return insert(objeto, null);
	}

	public static int insert(InstituicaoGrupo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_grupo");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_instituicao");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdInstituicao()));
			int code = Conexao.getSequenceCode("acd_instituicao_grupo", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdGrupo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_grupo (cd_grupo,"+
			                                  "cd_instituicao,"+
			                                  "cd_tipo_grupo,"+
			                                  "nm_grupo,"+
			                                  "sg_grupo,"+
			                                  "txt_observacao,"+
			                                  "st_grupo,"+
			                                  "dt_criacao,"+
			                                  "dt_extincao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInstituicao());
			if(objeto.getCdTipoGrupo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoGrupo());
			pstmt.setString(4,objeto.getNmGrupo());
			pstmt.setString(5,objeto.getSgGrupo());
			pstmt.setString(6,objeto.getTxtObservacao());
			pstmt.setInt(7,objeto.getStGrupo());
			if(objeto.getDtCriacao()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtCriacao().getTimeInMillis()));
			if(objeto.getDtExtincao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtExtincao().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InstituicaoGrupo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(InstituicaoGrupo objeto, int cdGrupoOld, int cdInstituicaoOld) {
		return update(objeto, cdGrupoOld, cdInstituicaoOld, null);
	}

	public static int update(InstituicaoGrupo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(InstituicaoGrupo objeto, int cdGrupoOld, int cdInstituicaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_instituicao_grupo SET cd_grupo=?,"+
												      		   "cd_instituicao=?,"+
												      		   "cd_tipo_grupo=?,"+
												      		   "nm_grupo=?,"+
												      		   "sg_grupo=?,"+
												      		   "txt_observacao=?,"+
												      		   "st_grupo=?,"+
												      		   "dt_criacao=?,"+
												      		   "dt_extincao=? WHERE cd_grupo=? AND cd_instituicao=?");
			pstmt.setInt(1,objeto.getCdGrupo());
			pstmt.setInt(2,objeto.getCdInstituicao());
			if(objeto.getCdTipoGrupo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoGrupo());
			pstmt.setString(4,objeto.getNmGrupo());
			pstmt.setString(5,objeto.getSgGrupo());
			pstmt.setString(6,objeto.getTxtObservacao());
			pstmt.setInt(7,objeto.getStGrupo());
			if(objeto.getDtCriacao()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtCriacao().getTimeInMillis()));
			if(objeto.getDtExtincao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtExtincao().getTimeInMillis()));
			pstmt.setInt(10, cdGrupoOld!=0 ? cdGrupoOld : objeto.getCdGrupo());
			pstmt.setInt(11, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdGrupo, int cdInstituicao) {
		return delete(cdGrupo, cdInstituicao, null);
	}

	public static int delete(int cdGrupo, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao_grupo WHERE cd_grupo=? AND cd_instituicao=?");
			pstmt.setInt(1, cdGrupo);
			pstmt.setInt(2, cdInstituicao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoGrupo get(int cdGrupo, int cdInstituicao) {
		return get(cdGrupo, cdInstituicao, null);
	}

	public static InstituicaoGrupo get(int cdGrupo, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_grupo WHERE cd_grupo=? AND cd_instituicao=?");
			pstmt.setInt(1, cdGrupo);
			pstmt.setInt(2, cdInstituicao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InstituicaoGrupo(rs.getInt("cd_grupo"),
						rs.getInt("cd_instituicao"),
						rs.getInt("cd_tipo_grupo"),
						rs.getString("nm_grupo"),
						rs.getString("sg_grupo"),
						rs.getString("txt_observacao"),
						rs.getInt("st_grupo"),
						(rs.getTimestamp("dt_criacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_criacao").getTime()),
						(rs.getTimestamp("dt_extincao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_extincao").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_grupo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InstituicaoGrupo> getList() {
		return getList(null);
	}

	public static ArrayList<InstituicaoGrupo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InstituicaoGrupo> list = new ArrayList<InstituicaoGrupo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InstituicaoGrupo obj = InstituicaoGrupoDAO.get(rsm.getInt("cd_grupo"), rsm.getInt("cd_instituicao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_grupo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
