package com.tivic.manager.pcb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoTanqueDAO{

	public static int insert(TipoTanque objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoTanque objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("pcb_tipo_tanque", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoTanque(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO pcb_tipo_tanque (cd_tipo_tanque,"+
			                                  "id_tipo_tanque,"+
			                                  "nm_tipo_tanque,"+
			                                  "qt_medida_cm,"+
			                                  "qt_litros,"+
			                                  "qt_litros_milimetros," +
			                                  "st_tipo_tanque," +
			                                  "qt_capacidade) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getIdTipoTanque());
			pstmt.setString(3,objeto.getNmTipoTanque());
			pstmt.setFloat(4,objeto.getQtMedidaCm());
			pstmt.setFloat(5,objeto.getQtLitros());
			pstmt.setFloat(6,objeto.getQtLitrosMilimetros());
			pstmt.setInt(7, objeto.getStTipoTanque());
			pstmt.setFloat(8, objeto.getQtCapacidade());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoTanqueDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoTanqueDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static TipoTanque insertObjeto(TipoTanque objeto){
		return insertObjeto(objeto,null);
	}
	
	public static TipoTanque insertObjeto(TipoTanque objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("pcb_tipo_tanque", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return null;
			}
			objeto.setCdTipoTanque(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO pcb_tipo_tanque (cd_tipo_tanque,"+
			                                  "id_tipo_tanque,"+
			                                  "nm_tipo_tanque,"+
			                                  "qt_medida_cm,"+
			                                  "qt_litros,"+
			                                  "qt_litros_milimetros," +
			                                  "st_tipo_tanque," +
			                                  "qt_capacidade) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getIdTipoTanque());
			pstmt.setString(3,objeto.getNmTipoTanque());
			pstmt.setFloat(4,objeto.getQtMedidaCm());
			pstmt.setFloat(5,objeto.getQtLitros());
			pstmt.setFloat(6,objeto.getQtLitrosMilimetros());
			pstmt.setInt(7, objeto.getStTipoTanque());
			pstmt.setFloat(8, objeto.getQtCapacidade());
			pstmt.executeUpdate();
			return objeto;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoTanqueDAO.insert: " + sqlExpt);
			return null;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoTanqueDAO.insert: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoTanque objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoTanque objeto, int cdTipoTanqueOld) {
		return update(objeto, cdTipoTanqueOld, null);
	}

	public static int update(TipoTanque objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoTanque objeto, int cdTipoTanqueOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE pcb_tipo_tanque SET cd_tipo_tanque=?,"+
												      		   "id_tipo_tanque=?,"+
												      		   "nm_tipo_tanque=?,"+
												      		   "qt_medida_cm=?,"+
												      		   "qt_litros=?,"+
												      		   "qt_litros_milimetros=?," +
												      		   "st_tipo_tanque=?," +
												      		   "qt_capacidade=? WHERE cd_tipo_tanque=?");
			pstmt.setInt(1,objeto.getCdTipoTanque());
			pstmt.setString(2,objeto.getIdTipoTanque());
			pstmt.setString(3,objeto.getNmTipoTanque());
			pstmt.setFloat(4,objeto.getQtMedidaCm());
			pstmt.setFloat(5,objeto.getQtLitros());
			pstmt.setFloat(6,objeto.getQtLitrosMilimetros());
			pstmt.setInt(7, objeto.getStTipoTanque());
			pstmt.setFloat(8, objeto.getQtCapacidade());
			pstmt.setInt(9, cdTipoTanqueOld!=0 ? cdTipoTanqueOld : objeto.getCdTipoTanque());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoTanqueDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoTanqueDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoTanque) {
		return delete(cdTipoTanque, null);
	}

	public static int delete(int cdTipoTanque, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM pcb_tipo_tanque WHERE cd_tipo_tanque=?");
			pstmt.setInt(1, cdTipoTanque);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoTanqueDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoTanqueDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoTanque get(int cdTipoTanque) {
		return get(cdTipoTanque, null);
	}

	public static TipoTanque get(int cdTipoTanque, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM pcb_tipo_tanque WHERE cd_tipo_tanque=?");
			pstmt.setInt(1, cdTipoTanque);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoTanque(rs.getInt("cd_tipo_tanque"),
						rs.getString("id_tipo_tanque"),
						rs.getString("nm_tipo_tanque"),
						rs.getFloat("qt_medida_cm"),
						rs.getFloat("qt_litros"),
						rs.getFloat("qt_litros_milimetros"),
						rs.getInt("st_tipo_tanque"),
						rs.getFloat("qt_capacidade"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoTanqueDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoTanqueDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM pcb_tipo_tanque");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoTanqueDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoTanqueDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM pcb_tipo_tanque", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	
	public static TipoTanque updateObjeto(TipoTanque objeto) {
		return updateObjeto(objeto, 0, null);
	}

	public static TipoTanque updateObjeto(TipoTanque objeto, int cdTipoTanqueOld) {
		return updateObjeto(objeto, cdTipoTanqueOld, null);
	}

	public static TipoTanque updateObjeto(TipoTanque objeto, Connection connect) {
		return updateObjeto(objeto, 0, connect);
	}

	public static TipoTanque updateObjeto(TipoTanque objeto, int cdTipoTanqueOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE pcb_tipo_tanque SET cd_tipo_tanque=?,"+
												      		   "id_tipo_tanque=?,"+
												      		   "nm_tipo_tanque=?,"+
												      		   "qt_medida_cm=?,"+
												      		   "qt_litros=?,"+
												      		   "qt_litros_milimetros=?," +
												      		   "st_tipo_tanque=?," +
												      		   "qt_capacidade=? WHERE cd_tipo_tanque=?");
			pstmt.setInt(1,objeto.getCdTipoTanque());
			pstmt.setString(2,objeto.getIdTipoTanque());
			pstmt.setString(3,objeto.getNmTipoTanque());
			pstmt.setFloat(4,objeto.getQtMedidaCm());
			pstmt.setFloat(5,objeto.getQtLitros());
			pstmt.setFloat(6,objeto.getQtLitrosMilimetros());
			pstmt.setInt(7, objeto.getStTipoTanque());			
			pstmt.setFloat(8, objeto.getQtCapacidade());
			pstmt.setInt(9, cdTipoTanqueOld!=0 ? cdTipoTanqueOld : objeto.getCdTipoTanque());
			pstmt.executeUpdate();
			return objeto;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoTanqueDAO.update: " + sqlExpt);
			return null;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoTanqueDAO.update: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

}
