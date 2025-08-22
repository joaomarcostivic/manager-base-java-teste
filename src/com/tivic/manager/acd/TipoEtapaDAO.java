package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoEtapaDAO{

	public static int insert(TipoEtapa objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoEtapa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_tipo_etapa", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdEtapa(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_tipo_etapa (cd_etapa,"+
			                                  "nm_etapa,"+
			                                  "id_etapa,"+
			                                  "lg_regular,"+
			                                  "lg_especial,"+
			                                  "lg_eja,"+
			                                  "cd_etapa_posterior,"+
			                                  "sg_tipo_etapa) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmEtapa());
			pstmt.setString(3,objeto.getIdEtapa());
			pstmt.setInt(4,objeto.getLgRegular());
			pstmt.setInt(5,objeto.getLgEspecial());
			pstmt.setInt(6,objeto.getLgEja());
			if(objeto.getCdEtapaPosterior()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdEtapaPosterior());
			pstmt.setString(8,objeto.getSgTipoEtapa());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEtapaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEtapaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoEtapa objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoEtapa objeto, int cdEtapaOld) {
		return update(objeto, cdEtapaOld, null);
	}

	public static int update(TipoEtapa objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoEtapa objeto, int cdEtapaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_tipo_etapa SET cd_etapa=?,"+
												      		   "nm_etapa=?,"+
												      		   "id_etapa=?,"+
												      		   "lg_regular=?,"+
												      		   "lg_especial=?,"+
												      		   "lg_eja=?,"+
												      		   "cd_etapa_posterior=?,"+
												      		   "sg_tipo_etapa=? WHERE cd_etapa=?");
			pstmt.setInt(1,objeto.getCdEtapa());
			pstmt.setString(2,objeto.getNmEtapa());
			pstmt.setString(3,objeto.getIdEtapa());
			pstmt.setInt(4,objeto.getLgRegular());
			pstmt.setInt(5,objeto.getLgEspecial());
			pstmt.setInt(6,objeto.getLgEja());
			if(objeto.getCdEtapaPosterior()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdEtapaPosterior());
			pstmt.setString(8,objeto.getSgTipoEtapa());
			pstmt.setInt(9, cdEtapaOld!=0 ? cdEtapaOld : objeto.getCdEtapa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEtapaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEtapaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEtapa) {
		return delete(cdEtapa, null);
	}

	public static int delete(int cdEtapa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_tipo_etapa WHERE cd_etapa=?");
			pstmt.setInt(1, cdEtapa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEtapaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEtapaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoEtapa get(int cdEtapa) {
		return get(cdEtapa, null);
	}

	public static TipoEtapa get(int cdEtapa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_etapa WHERE cd_etapa=?");
			pstmt.setInt(1, cdEtapa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoEtapa(rs.getInt("cd_etapa"),
						rs.getString("nm_etapa"),
						rs.getString("id_etapa"),
						rs.getInt("lg_regular"),
						rs.getInt("lg_especial"),
						rs.getInt("lg_eja"),
						rs.getInt("cd_etapa_posterior"),
						rs.getString("sg_tipo_etapa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEtapaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEtapaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_etapa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEtapaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEtapaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoEtapa> getList() {
		return getList(null);
	}

	public static ArrayList<TipoEtapa> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoEtapa> list = new ArrayList<TipoEtapa>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoEtapa obj = TipoEtapaDAO.get(rsm.getInt("cd_etapa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEtapaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_etapa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
