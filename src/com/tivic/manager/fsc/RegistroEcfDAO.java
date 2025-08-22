package com.tivic.manager.fsc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class RegistroEcfDAO{

	public static int insert(RegistroEcf objeto) {
		return insert(objeto, null);
	}

	public static int insert(RegistroEcf objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fsc_registro_ecf", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRegistroEcf(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fsc_registro_ecf (cd_registro_ecf,"+
			                                  "tp_registro_ecf,"+
			                                  "dt_registro_ecf,"+
			                                  "vl_registro_ecf,"+
			                                  "txt_registro_ecf,"+
			                                  "cd_referencia_ecf,"+
			                                  "lg_sped," +
			                                  "status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getTpRegistroEcf());
			if(objeto.getDtRegistroEcf()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtRegistroEcf().getTimeInMillis()));
			pstmt.setFloat(4,objeto.getVlRegistroEcf());
			pstmt.setString(5,objeto.getTxtRegistroEcf());
			if(objeto.getCdReferenciaEcf()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdReferenciaEcf());
			pstmt.setInt(7,objeto.getLgSped());
			pstmt.setString(8, objeto.getStatus());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(RegistroEcf objeto) {
		return update(objeto, 0, null);
	}

	public static int update(RegistroEcf objeto, int cdRegistroEcfOld) {
		return update(objeto, cdRegistroEcfOld, null);
	}

	public static int update(RegistroEcf objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(RegistroEcf objeto, int cdRegistroEcfOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fsc_registro_ecf SET cd_registro_ecf=?,"+
												      		   "tp_registro_ecf=?,"+
												      		   "dt_registro_ecf=?,"+
												      		   "vl_registro_ecf=?,"+
												      		   "txt_registro_ecf=?,"+
												      		   "cd_referencia_ecf=?," +
												      		   "lg_sped=?," +
												      		   "status=? WHERE cd_registro_ecf=?");
			pstmt.setInt(1,objeto.getCdRegistroEcf());
			pstmt.setString(2,objeto.getTpRegistroEcf());
			if(objeto.getDtRegistroEcf()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtRegistroEcf().getTimeInMillis()));
			pstmt.setFloat(4,objeto.getVlRegistroEcf());
			pstmt.setString(5,objeto.getTxtRegistroEcf());
			if(objeto.getCdReferenciaEcf()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdReferenciaEcf());			
			pstmt.setInt(7,objeto.getLgSped());
			pstmt.setString(8, objeto.getStatus());
			pstmt.setInt(9, cdRegistroEcfOld!=0 ? cdRegistroEcfOld : objeto.getCdRegistroEcf());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRegistroEcf) {
		return delete(cdRegistroEcf, null);
	}

	public static int delete(int cdRegistroEcf, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			return connect.prepareStatement("DELETE FROM fsc_registro_ecf WHERE cd_registro_ecf="+cdRegistroEcf).executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static RegistroEcf get(int cdRegistroEcf) {
		return get(cdRegistroEcf, null);
	}

	public static RegistroEcf get(int cdRegistroEcf, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_registro_ecf WHERE cd_registro_ecf=?");
			pstmt.setInt(1, cdRegistroEcf);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new RegistroEcf(rs.getInt("cd_registro_ecf"),
						rs.getString("tp_registro_ecf"),
						(rs.getTimestamp("dt_registro_ecf")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_registro_ecf").getTime()),
						rs.getFloat("vl_registro_ecf"),
						rs.getString("txt_registro_ecf"),
						rs.getInt("cd_referencia_ecf"),
						rs.getInt("lg_sped"),
						rs.getString("status"));
			}
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
			pstmt = connect.prepareStatement("SELECT * FROM fsc_registro_ecf");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		return Search.find("SELECT * FROM fsc_registro_ecf", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
