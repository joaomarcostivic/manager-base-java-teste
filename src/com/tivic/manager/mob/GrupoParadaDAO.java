package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class GrupoParadaDAO{

	public static int insert(GrupoParada objeto) {
		return insert(objeto, null);
	}

	public static int insert(GrupoParada objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_grupo_parada", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdGrupoParada(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_grupo_parada (cd_grupo_parada,"+
			                                  "tp_grupo_parada,"+
			                                  "nm_grupo_parada,"+
			                                  "cd_georreferencia,"+
			                                  "cd_grupo_parada_superior,"+
                    						  "qt_vagas) VALUES (?, ?, ?, ?, ?, ?)");

			if(objeto.getCdGrupoParada()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdGrupoParada());
			pstmt.setInt(2,objeto.getTpGrupoParada());
			pstmt.setString(3,objeto.getNmGrupoParada());
			if(objeto.getCdGeorreferencia()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdGeorreferencia());
			if(objeto.getCdGrupoParadaSuperior()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdGrupoParadaSuperior());
				pstmt.setInt(6,objeto.getQtVagas());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(GrupoParada objeto) {
		return update(objeto, 0, null);
	}

	public static int update(GrupoParada objeto, int cdGrupoParadaOld) {
		return update(objeto, cdGrupoParadaOld, null);
	}

	public static int update(GrupoParada objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(GrupoParada objeto, int cdGrupoParadaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_grupo_parada SET cd_grupo_parada=?,"+
												      		   "tp_grupo_parada=?,"+
												      		   "nm_grupo_parada=?,"+
												      		   "cd_georreferencia=?,"+
												      		   "cd_grupo_parada_superior=?,"+
												      		   "qt_vagas=? WHERE cd_grupo_parada=?");
			pstmt.setInt(1,objeto.getCdGrupoParada());
			pstmt.setInt(2,objeto.getTpGrupoParada());
			pstmt.setString(3,objeto.getNmGrupoParada());
			if(objeto.getCdGeorreferencia()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdGeorreferencia());
			if(objeto.getCdGrupoParadaSuperior()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdGrupoParadaSuperior());
			pstmt.setInt(6, objeto.getQtVagas());
			pstmt.setInt(7, cdGrupoParadaOld!=0 ? cdGrupoParadaOld : objeto.getCdGrupoParada());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdGrupoParada) {
		return delete(cdGrupoParada, null);
	}

	public static int delete(int cdGrupoParada, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_grupo_parada WHERE cd_grupo_parada=?");
			pstmt.setInt(1, cdGrupoParada);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static GrupoParada get(int cdGrupoParada) {
		return get(cdGrupoParada, null);
	}

	public static GrupoParada get(int cdGrupoParada, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_grupo_parada WHERE cd_grupo_parada=?");
			pstmt.setInt(1, cdGrupoParada);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new GrupoParada(rs.getInt("cd_grupo_parada"),
						rs.getInt("tp_grupo_parada"),
						rs.getString("nm_grupo_parada"),
						rs.getInt("cd_georreferencia"),
						rs.getInt("cd_grupo_parada_superior"),
						rs.getInt("qt_vagas"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_grupo_parada");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<GrupoParada> getList() {
		return getList(null);
	}

	public static ArrayList<GrupoParada> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<GrupoParada> list = new ArrayList<GrupoParada>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				GrupoParada obj = GrupoParadaDAO.get(rsm.getInt("cd_grupo_parada"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_grupo_parada", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
