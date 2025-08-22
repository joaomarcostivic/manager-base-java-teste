package com.tivic.manager.agd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class GrupoDAO{

	public static int insert(Grupo objeto) {
		return insert(objeto, null);
	}

	public static int insert(Grupo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("agd_grupo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdGrupo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_grupo (cd_grupo,"+
			                                  "nm_grupo,"+
			                                  "cd_proprietario,"+
			                                  "tp_visibilidade,"+
			                                  "cd_vinculo,"+
			                                  "nm_email,"+
			                                  "st_grupo) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmGrupo());
			if(objeto.getCdProprietario()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdProprietario());
			pstmt.setInt(4,objeto.getTpVisibilidade());
			if(objeto.getCdVinculo()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdVinculo());
			pstmt.setString(6,objeto.getNmEmail());
			pstmt.setInt(7,objeto.getStGrupo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Grupo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Grupo objeto, int cdGrupoOld) {
		return update(objeto, cdGrupoOld, null);
	}

	public static int update(Grupo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Grupo objeto, int cdGrupoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agd_grupo SET cd_grupo=?,"+
												      		   "nm_grupo=?,"+
												      		   "cd_proprietario=?,"+
												      		   "tp_visibilidade=?,"+
												      		   "cd_vinculo=?,"+
												      		   "nm_email=?,"+
												      		   "st_grupo=? WHERE cd_grupo=?");
			pstmt.setInt(1,objeto.getCdGrupo());
			pstmt.setString(2,objeto.getNmGrupo());
			if(objeto.getCdProprietario()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdProprietario());
			pstmt.setInt(4,objeto.getTpVisibilidade());
			if(objeto.getCdVinculo()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdVinculo());
			pstmt.setString(6,objeto.getNmEmail());
			pstmt.setInt(7,objeto.getStGrupo());
			pstmt.setInt(8, cdGrupoOld!=0 ? cdGrupoOld : objeto.getCdGrupo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdGrupo) {
		return delete(cdGrupo, null);
	}

	public static int delete(int cdGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_grupo WHERE cd_grupo=?");
			pstmt.setInt(1, cdGrupo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Grupo get(int cdGrupo) {
		return get(cdGrupo, null);
	}

	public static Grupo get(int cdGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_grupo WHERE cd_grupo=?");
			pstmt.setInt(1, cdGrupo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Grupo(rs.getInt("cd_grupo"),
						rs.getString("nm_grupo"),
						rs.getInt("cd_proprietario"),
						rs.getInt("tp_visibilidade"),
						rs.getInt("cd_vinculo"),
						rs.getString("nm_email"),
						rs.getInt("st_grupo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_grupo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Grupo> getList() {
		return getList(null);
	}

	public static ArrayList<Grupo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Grupo> list = new ArrayList<Grupo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Grupo obj = GrupoDAO.get(rsm.getInt("cd_grupo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM agd_grupo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}