package com.tivic.manager.seg;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class AssinaturaDigitalDAO{

	public static int insert(AssinaturaDigital objeto) {
		return insert(objeto, null);
	}

	public static int insert(AssinaturaDigital objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("seg_assinatura_digital", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAssinatura(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO seg_assinatura_digital (cd_assinatura,"+
			                                  "cd_chave,"+
			                                  "cd_usuario,"+
			                                  "dt_assinatura,"+
			                                  "blb_assinatura) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdChave()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdChave());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdUsuario());
			if(objeto.getDtAssinatura()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtAssinatura().getTimeInMillis()));
			if(objeto.getBlbAssinatura()==null)
				pstmt.setNull(5, Types.BINARY);
			else
				pstmt.setBytes(5,objeto.getBlbAssinatura());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AssinaturaDigitalDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AssinaturaDigitalDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AssinaturaDigital objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AssinaturaDigital objeto, int cdAssinaturaOld) {
		return update(objeto, cdAssinaturaOld, null);
	}

	public static int update(AssinaturaDigital objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AssinaturaDigital objeto, int cdAssinaturaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE seg_assinatura_digital SET cd_assinatura=?,"+
												      		   "cd_chave=?,"+
												      		   "cd_usuario=?,"+
												      		   "dt_assinatura=?,"+
												      		   "blb_assinatura=? WHERE cd_assinatura=?");
			pstmt.setInt(1,objeto.getCdAssinatura());
			if(objeto.getCdChave()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdChave());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdUsuario());
			if(objeto.getDtAssinatura()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtAssinatura().getTimeInMillis()));
			if(objeto.getBlbAssinatura()==null)
				pstmt.setNull(5, Types.BINARY);
			else
				pstmt.setBytes(5,objeto.getBlbAssinatura());
			pstmt.setInt(6, cdAssinaturaOld!=0 ? cdAssinaturaOld : objeto.getCdAssinatura());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AssinaturaDigitalDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AssinaturaDigitalDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAssinatura) {
		return delete(cdAssinatura, null);
	}

	public static int delete(int cdAssinatura, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM seg_assinatura_digital WHERE cd_assinatura=?");
			pstmt.setInt(1, cdAssinatura);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AssinaturaDigitalDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AssinaturaDigitalDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AssinaturaDigital get(int cdAssinatura) {
		return get(cdAssinatura, null);
	}

	public static AssinaturaDigital get(int cdAssinatura, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_assinatura_digital WHERE cd_assinatura=?");
			pstmt.setInt(1, cdAssinatura);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AssinaturaDigital(rs.getInt("cd_assinatura"),
						rs.getInt("cd_chave"),
						rs.getInt("cd_usuario"),
						(rs.getTimestamp("dt_assinatura")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_assinatura").getTime()),
						rs.getBytes("blb_assinatura")==null?null:rs.getBytes("blb_assinatura"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AssinaturaDigitalDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AssinaturaDigitalDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_assinatura_digital");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AssinaturaDigitalDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AssinaturaDigitalDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AssinaturaDigital> getList() {
		return getList(null);
	}

	public static ArrayList<AssinaturaDigital> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AssinaturaDigital> list = new ArrayList<AssinaturaDigital>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AssinaturaDigital obj = AssinaturaDigitalDAO.get(rsm.getInt("cd_assinatura"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AssinaturaDigitalDAO.getList: " + e);
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
		return Search.find("SELECT * FROM seg_assinatura_digital", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}