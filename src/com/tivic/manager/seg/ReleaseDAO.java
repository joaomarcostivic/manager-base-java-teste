package com.tivic.manager.seg;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class ReleaseDAO{

	public static int insert(Release objeto) {
		return insert(objeto, null);
	}

	public static int insert(Release objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("seg_release", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRelease(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO seg_release (cd_release,"+
			                                  "nr_maior,"+
			                                  "nr_menor,"+
			                                  "txt_descricao,"+
			                                  "dt_release,"+
			                                  "blb_release,"+
			                                  "lg_executado,"+
			                                  "nr_build,"+
			                                  "cd_sistema) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getNrMaior());
			pstmt.setInt(3,objeto.getNrMenor());
			pstmt.setString(4,objeto.getTxtDescricao());
			if(objeto.getDtRelease()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtRelease().getTimeInMillis()));
			if(objeto.getBlbRelease()==null)
				pstmt.setNull(6, Types.BINARY);
			else
				pstmt.setBytes(6,objeto.getBlbRelease());
			pstmt.setInt(7,objeto.getLgExecutado());
			pstmt.setInt(8,objeto.getNrBuild());
			if(objeto.getCdSistema()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdSistema());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReleaseDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ReleaseDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Release objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Release objeto, int cdReleaseOld) {
		return update(objeto, cdReleaseOld, null);
	}

	public static int update(Release objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Release objeto, int cdReleaseOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE seg_release SET cd_release=?,"+
												      		   "nr_maior=?,"+
												      		   "nr_menor=?,"+
												      		   "txt_descricao=?,"+
												      		   "dt_release=?,"+
												      		   "blb_release=?,"+
												      		   "lg_executado=?,"+
												      		   "nr_build=?,"+
												      		   "cd_sistema=? WHERE cd_release=?");
			pstmt.setInt(1,objeto.getCdRelease());
			pstmt.setInt(2,objeto.getNrMaior());
			pstmt.setInt(3,objeto.getNrMenor());
			pstmt.setString(4,objeto.getTxtDescricao());
			if(objeto.getDtRelease()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtRelease().getTimeInMillis()));
			if(objeto.getBlbRelease()==null)
				pstmt.setNull(6, Types.BINARY);
			else
				pstmt.setBytes(6,objeto.getBlbRelease());
			pstmt.setInt(7,objeto.getLgExecutado());
			pstmt.setInt(8,objeto.getNrBuild());
			if(objeto.getCdSistema()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdSistema());
			pstmt.setInt(10, cdReleaseOld!=0 ? cdReleaseOld : objeto.getCdRelease());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReleaseDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ReleaseDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRelease) {
		return delete(cdRelease, null);
	}

	public static int delete(int cdRelease, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM seg_release WHERE cd_release=?");
			pstmt.setInt(1, cdRelease);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReleaseDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ReleaseDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Release get(int cdRelease) {
		return get(cdRelease, null);
	}

	public static Release get(int cdRelease, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_release WHERE cd_release=?");
			pstmt.setInt(1, cdRelease);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Release(rs.getInt("cd_release"),
						rs.getInt("nr_maior"),
						rs.getInt("nr_menor"),
						rs.getString("txt_descricao"),
						(rs.getTimestamp("dt_release")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_release").getTime()),
						rs.getBytes("blb_release")==null?null:rs.getBytes("blb_release"),
						rs.getInt("lg_executado"),
						rs.getInt("nr_build"),
						rs.getInt("cd_sistema"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReleaseDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ReleaseDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_release");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReleaseDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ReleaseDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Release> getList() {
		return getList(null);
	}

	public static ArrayList<Release> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Release> list = new ArrayList<Release>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Release obj = ReleaseDAO.get(rsm.getInt("cd_release"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ReleaseDAO.getList: " + e);
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
		return Search.find("SELECT * FROM seg_release", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
