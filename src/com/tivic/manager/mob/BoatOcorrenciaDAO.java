package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class BoatOcorrenciaDAO{

	public static int insert(BoatOcorrencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(BoatOcorrencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_boat_ocorrencia", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdBoatOcorrencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_boat_ocorrencia (cd_boat_ocorrencia,"+
			                                  "cd_boat,"+
			                                  "cd_agente,"+
			                                  "dt_ocorrencia,"+
			                                  "ds_ocorrencia,"+
			                                  "cd_ocorrencia) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdBoat()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdBoat());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAgente());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			pstmt.setString(5,objeto.getDsOcorrencia());
			if(objeto.getCdOcorrencia()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdOcorrencia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatOcorrenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatOcorrenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(BoatOcorrencia objeto) {
		return update(objeto, 0, null);
	}

	public static int update(BoatOcorrencia objeto, int cdBoatOcorrenciaOld) {
		return update(objeto, cdBoatOcorrenciaOld, null);
	}

	public static int update(BoatOcorrencia objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(BoatOcorrencia objeto, int cdBoatOcorrenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_boat_ocorrencia SET cd_boat_ocorrencia=?,"+
												      		   "cd_boat=?,"+
												      		   "cd_agente=?,"+
												      		   "dt_ocorrencia=?,"+
												      		   "ds_ocorrencia=?,"+
												      		   "cd_ocorrencia=? WHERE cd_boat_ocorrencia=?");
			pstmt.setInt(1,objeto.getCdBoatOcorrencia());
			if(objeto.getCdBoat()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdBoat());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAgente());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			pstmt.setString(5,objeto.getDsOcorrencia());
			if(objeto.getCdOcorrencia()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdOcorrencia());
			pstmt.setInt(7, cdBoatOcorrenciaOld!=0 ? cdBoatOcorrenciaOld : objeto.getCdBoatOcorrencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatOcorrenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatOcorrenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdBoatOcorrencia) {
		return delete(cdBoatOcorrencia, null);
	}

	public static int delete(int cdBoatOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_boat_ocorrencia WHERE cd_boat_ocorrencia=?");
			pstmt.setInt(1, cdBoatOcorrencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatOcorrenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatOcorrenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static BoatOcorrencia get(int cdBoatOcorrencia) {
		return get(cdBoatOcorrencia, null);
	}

	public static BoatOcorrencia get(int cdBoatOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_ocorrencia WHERE cd_boat_ocorrencia=?");
			pstmt.setInt(1, cdBoatOcorrencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new BoatOcorrencia(rs.getInt("cd_boat_ocorrencia"),
						rs.getInt("cd_boat"),
						rs.getInt("cd_agente"),
						(rs.getTimestamp("dt_ocorrencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ocorrencia").getTime()),
						rs.getString("ds_ocorrencia"),
						rs.getInt("cd_ocorrencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatOcorrenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatOcorrenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_ocorrencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatOcorrenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatOcorrenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<BoatOcorrencia> getList() {
		return getList(null);
	}

	public static ArrayList<BoatOcorrencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<BoatOcorrencia> list = new ArrayList<BoatOcorrencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				BoatOcorrencia obj = BoatOcorrenciaDAO.get(rsm.getInt("cd_boat_ocorrencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatOcorrenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_boat_ocorrencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
