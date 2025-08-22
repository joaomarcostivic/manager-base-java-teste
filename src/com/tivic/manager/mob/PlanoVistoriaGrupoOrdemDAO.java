package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class PlanoVistoriaGrupoOrdemDAO{

	public static int insert(PlanoVistoriaGrupoOrdem objeto) {
		return insert(objeto, null);
	}

	public static int insert(PlanoVistoriaGrupoOrdem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_plano_vistoria_grupo_ordem", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPlanoVistoriaGrupoOrdem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_plano_vistoria_grupo_ordem (cd_plano_vistoria_grupo_ordem,"+
			                                  "cd_vistoria_item_grupo,"+
			                                  "cd_plano_vistoria,"+
			                                  "nr_ordem_grupo) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdVistoriaItemGrupo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdVistoriaItemGrupo());
			if(objeto.getCdPlanoVistoria()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPlanoVistoria());
			pstmt.setInt(4,objeto.getNrOrdemGrupo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaGrupoOrdemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaGrupoOrdemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PlanoVistoriaGrupoOrdem objeto) {
		return update(objeto, 0, null);
	}

	public static int update(PlanoVistoriaGrupoOrdem objeto, int cdPlanoVistoriaGrupoOrdemOld) {
		return update(objeto, cdPlanoVistoriaGrupoOrdemOld, null);
	}

	public static int update(PlanoVistoriaGrupoOrdem objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(PlanoVistoriaGrupoOrdem objeto, int cdPlanoVistoriaGrupoOrdemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_plano_vistoria_grupo_ordem SET cd_plano_vistoria_grupo_ordem=?,"+
												      		   "cd_vistoria_item_grupo=?,"+
												      		   "cd_plano_vistoria=?,"+
												      		   "nr_ordem_grupo=? WHERE cd_plano_vistoria_grupo_ordem=?");
			pstmt.setInt(1,objeto.getCdPlanoVistoriaGrupoOrdem());
			if(objeto.getCdVistoriaItemGrupo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdVistoriaItemGrupo());
			if(objeto.getCdPlanoVistoria()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPlanoVistoria());
			pstmt.setInt(4,objeto.getNrOrdemGrupo());
			pstmt.setInt(5, cdPlanoVistoriaGrupoOrdemOld!=0 ? cdPlanoVistoriaGrupoOrdemOld : objeto.getCdPlanoVistoriaGrupoOrdem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaGrupoOrdemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaGrupoOrdemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPlanoVistoriaGrupoOrdem) {
		return delete(cdPlanoVistoriaGrupoOrdem, null);
	}

	public static int delete(int cdPlanoVistoriaGrupoOrdem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_plano_vistoria_grupo_ordem WHERE cd_plano_vistoria_grupo_ordem=?");
			pstmt.setInt(1, cdPlanoVistoriaGrupoOrdem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaGrupoOrdemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaGrupoOrdemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PlanoVistoriaGrupoOrdem get(int cdPlanoVistoriaGrupoOrdem) {
		return get(cdPlanoVistoriaGrupoOrdem, null);
	}

	public static PlanoVistoriaGrupoOrdem get(int cdPlanoVistoriaGrupoOrdem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_plano_vistoria_grupo_ordem WHERE cd_plano_vistoria_grupo_ordem=?");
			pstmt.setInt(1, cdPlanoVistoriaGrupoOrdem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PlanoVistoriaGrupoOrdem(rs.getInt("cd_plano_vistoria_grupo_ordem"),
						rs.getInt("cd_vistoria_item_grupo"),
						rs.getInt("cd_plano_vistoria"),
						rs.getInt("nr_ordem_grupo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaGrupoOrdemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaGrupoOrdemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_plano_vistoria_grupo_ordem");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaGrupoOrdemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaGrupoOrdemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PlanoVistoriaGrupoOrdem> getList() {
		return getList(null);
	}

	public static ArrayList<PlanoVistoriaGrupoOrdem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PlanoVistoriaGrupoOrdem> list = new ArrayList<PlanoVistoriaGrupoOrdem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PlanoVistoriaGrupoOrdem obj = PlanoVistoriaGrupoOrdemDAO.get(rsm.getInt("cd_plano_vistoria_grupo_ordem"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaGrupoOrdemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_plano_vistoria_grupo_ordem", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
