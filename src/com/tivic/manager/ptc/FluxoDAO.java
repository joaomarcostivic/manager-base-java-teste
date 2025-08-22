package com.tivic.manager.ptc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class FluxoDAO{

	public static int insert(Fluxo objeto) {
		return insert(objeto, null);
	}

	public static int insert(Fluxo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ptc_fluxo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFluxo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PTC_FLUXO (CD_FLUXO,"+
                    "NM_FLUXO,"+
                    "CD_FLUXO_ANTERIOR) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmFluxo());
			if(objeto.getCdFluxoAnterior()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdFluxoAnterior());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FluxoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FluxoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Fluxo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Fluxo objeto, int cdFluxoOld) {
		return update(objeto, cdFluxoOld, null);
	}

	public static int update(Fluxo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Fluxo objeto, int cdFluxoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_fluxo SET cd_fluxo=?,"+
												      		   "nm_fluxo=?,"+
												      		   "cd_fluxo_anterior=?  WHERE cd_fluxo=?");
			pstmt.setInt(1,objeto.getCdFluxo());
			pstmt.setString(2,objeto.getNmFluxo());
			if(objeto.getCdFluxoAnterior()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdFluxoAnterior());
			pstmt.setInt(4, cdFluxoOld!=0 ? cdFluxoOld : objeto.getCdFluxo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FluxoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FluxoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFluxo) {
		return delete(cdFluxo, null);
	}

	public static int delete(int cdFluxo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_fluxo WHERE cd_fluxo=?");
			pstmt.setInt(1, cdFluxo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FluxoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FluxoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Fluxo get(int cdFluxo) {
		return get(cdFluxo, null);
	}

	public static Fluxo get(int cdFluxo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_fluxo WHERE cd_fluxo=?");
			pstmt.setInt(1, cdFluxo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Fluxo(rs.getInt("cd_fluxo"),
						rs.getString("nm_fluxo"),
						rs.getInt("cd_fluxo_anterior"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FluxoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FluxoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_fluxo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FluxoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FluxoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Fluxo> getList() {
		return getList(null);
	}

	public static ArrayList<Fluxo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Fluxo> list = new ArrayList<Fluxo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Fluxo obj = FluxoDAO.get(rsm.getInt("cd_fluxo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FluxoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ptc_fluxo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
