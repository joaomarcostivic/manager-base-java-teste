package com.tivic.manager.srh;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class PlanoCargoNivelDAO{

	public static int insert(PlanoCargoNivel objeto) {
		return insert(objeto, null);
	}

	public static int insert(PlanoCargoNivel objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_plano");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdPlano()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_nivel");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("srh_plano_cargo_nivel", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdNivel(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO srh_plano_cargo_nivel (cd_plano,"+
			                                  "cd_nivel,"+
			                                  "vl_salario_base,"+
			                                  "qt_vagas,"+
			                                  "nm_nivel) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdPlano()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPlano());
			pstmt.setInt(2, code);
			pstmt.setDouble(3,objeto.getVlSalarioBase());
			pstmt.setInt(4,objeto.getQtVagas());
			pstmt.setString(5,objeto.getNmNivel());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoNivelDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoNivelDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PlanoCargoNivel objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(PlanoCargoNivel objeto, int cdPlanoOld, int cdNivelOld) {
		return update(objeto, cdPlanoOld, cdNivelOld, null);
	}

	public static int update(PlanoCargoNivel objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(PlanoCargoNivel objeto, int cdPlanoOld, int cdNivelOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE srh_plano_cargo_nivel SET cd_plano=?,"+
												      		   "cd_nivel=?,"+
												      		   "vl_salario_base=?,"+
												      		   "qt_vagas=?,"+
												      		   "nm_nivel=? WHERE cd_plano=? AND cd_nivel=?");
			pstmt.setInt(1,objeto.getCdPlano());
			pstmt.setInt(2,objeto.getCdNivel());
			pstmt.setDouble(3,objeto.getVlSalarioBase());
			pstmt.setInt(4,objeto.getQtVagas());
			pstmt.setString(5,objeto.getNmNivel());
			pstmt.setInt(6, cdPlanoOld!=0 ? cdPlanoOld : objeto.getCdPlano());
			pstmt.setInt(7, cdNivelOld!=0 ? cdNivelOld : objeto.getCdNivel());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoNivelDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoNivelDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPlano, int cdNivel) {
		return delete(cdPlano, cdNivel, null);
	}

	public static int delete(int cdPlano, int cdNivel, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM srh_plano_cargo_nivel WHERE cd_plano=? AND cd_nivel=?");
			pstmt.setInt(1, cdPlano);
			pstmt.setInt(2, cdNivel);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoNivelDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoNivelDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PlanoCargoNivel get(int cdPlano, int cdNivel) {
		return get(cdPlano, cdNivel, null);
	}

	public static PlanoCargoNivel get(int cdPlano, int cdNivel, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM srh_plano_cargo_nivel WHERE cd_plano=? AND cd_nivel=?");
			pstmt.setInt(1, cdPlano);
			pstmt.setInt(2, cdNivel);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PlanoCargoNivel(rs.getInt("cd_plano"),
						rs.getInt("cd_nivel"),
						rs.getDouble("vl_salario_base"),
						rs.getInt("qt_vagas"),
						rs.getString("nm_nivel"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoNivelDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoNivelDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_plano_cargo_nivel");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoNivelDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoNivelDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PlanoCargoNivel> getList() {
		return getList(null);
	}

	public static ArrayList<PlanoCargoNivel> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PlanoCargoNivel> list = new ArrayList<PlanoCargoNivel>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PlanoCargoNivel obj = PlanoCargoNivelDAO.get(rsm.getInt("cd_plano"), rsm.getInt("cd_nivel"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoNivelDAO.getList: " + e);
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
		return Search.find("SELECT * FROM srh_plano_cargo_nivel", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}