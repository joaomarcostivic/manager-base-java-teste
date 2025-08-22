package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class RrdDAO{

	public static int insert(Rrd objeto) {
		return insert(objeto, null);
	}

	public static int insert(Rrd objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_rrd", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRrd(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_rrd (cd_rrd,"+
			                                  "nr_rrd,"+
			                                  "dt_ocorrencia,"+
			                                  "cd_usuario,"+
			                                  "cd_agente,"+
			                                  "ds_observacao,"+
			                                  "ds_local_ocorrencia,"+
			                                  "ds_ponto_referencia,"+
			                                  "vl_latitude,"+
			                                  "vl_longitude,"+
			                                  "cd_cidade,"+
			                                  "dt_regularizar," +
			                                  "cd_rrd_orgao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getNrRrd());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdAgente());
			pstmt.setString(6,objeto.getDsObservacao());
			pstmt.setString(7,objeto.getDsLocalOcorrencia());
			pstmt.setString(8,objeto.getDsPontoReferencia());
			pstmt.setDouble(9,objeto.getVlLatitude());
			pstmt.setDouble(10,objeto.getVlLongitude());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdCidade());
			if(objeto.getDtRegularizar()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtRegularizar().getTimeInMillis()));
			
			pstmt.setInt(13,objeto.getCdRrdOrgao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Rrd objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Rrd objeto, int cdRrdOld) {
		return update(objeto, cdRrdOld, null);
	}

	public static int update(Rrd objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Rrd objeto, int cdRrdOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_rrd SET cd_rrd=?,"+
												      		   "nr_rrd=?,"+
												      		   "dt_ocorrencia=?,"+
												      		   "cd_usuario=?,"+
												      		   "cd_agente=?,"+
												      		   "ds_observacao=?,"+
												      		   "ds_local_ocorrencia=?,"+
												      		   "ds_ponto_referencia=?,"+
												      		   "vl_latitude=?,"+
												      		   "vl_longitude=?,"+	
												      		   "cd_cidade=?,"+
												      		   "dt_regularizar=?," + 
												      		   "cd_rrd_orgao=? WHERE cd_rrd=?");
			pstmt.setInt(1,objeto.getCdRrd());
			pstmt.setInt(2,objeto.getNrRrd());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdAgente());
			pstmt.setString(6,objeto.getDsObservacao());
			pstmt.setString(7,objeto.getDsLocalOcorrencia());
			pstmt.setString(8,objeto.getDsPontoReferencia());
			pstmt.setDouble(9,objeto.getVlLatitude());
			pstmt.setDouble(10,objeto.getVlLongitude());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdCidade());
			if(objeto.getDtRegularizar()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtRegularizar().getTimeInMillis()));
		
			pstmt.setInt(13,objeto.getCdRrdOrgao());
			
			pstmt.setInt(14, cdRrdOld!=0 ? cdRrdOld : objeto.getCdRrd());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRrd) {
		return delete(cdRrd, null);
	}

	public static int delete(int cdRrd, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_rrd WHERE cd_rrd=?");
			pstmt.setInt(1, cdRrd);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Rrd get(int cdRrd) {
		return get(cdRrd, null);
	}

	public static Rrd get(int cdRrd, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_rrd WHERE cd_rrd=?");
			pstmt.setInt(1, cdRrd);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Rrd(rs.getInt("cd_rrd"),
						rs.getInt("nr_rrd"),
						(rs.getTimestamp("dt_ocorrencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ocorrencia").getTime()),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_agente"),
						rs.getString("ds_observacao"),
						rs.getString("ds_local_ocorrencia"),
						rs.getString("ds_ponto_referencia"),
						rs.getDouble("vl_latitude"),
						rs.getDouble("vl_longitude"),
						rs.getInt("cd_cidade"),
						(rs.getTimestamp("dt_regularizar")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_regularizar").getTime()),
						rs.getInt("cd_rrd_orgao"),null,null,null);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_rrd");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RrdDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Rrd> getList() {
		return getList(null);
	}

	public static ArrayList<Rrd> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Rrd> list = new ArrayList<Rrd>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Rrd obj = RrdDAO.get(rsm.getInt("cd_rrd"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RrdDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_rrd", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}