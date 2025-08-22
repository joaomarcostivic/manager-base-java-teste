package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CidReferenciaDAO{

	public static int insert(CidReferencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(CidReferencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_cid_referencia (cd_cid,"+
			                                  "cd_cid_referencia,"+
			                                  "id_cid_referencia) VALUES (?, ?, ?)");
			if(objeto.getCdCid()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdCid());
			if(objeto.getCdCidReferencia()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCidReferencia());
			pstmt.setString(3,objeto.getIdCidReferencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidReferenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CidReferenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CidReferencia objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(CidReferencia objeto, int cdCidOld, int cdCidReferenciaOld) {
		return update(objeto, cdCidOld, cdCidReferenciaOld, null);
	}

	public static int update(CidReferencia objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(CidReferencia objeto, int cdCidOld, int cdCidReferenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_cid_referencia SET cd_cid=?,"+
												      		   "cd_cid_referencia=?,"+
												      		   "id_cid_referencia=? WHERE cd_cid=? AND cd_cid_referencia=?");
			pstmt.setInt(1,objeto.getCdCid());
			pstmt.setInt(2,objeto.getCdCidReferencia());
			pstmt.setString(3,objeto.getIdCidReferencia());
			pstmt.setInt(4, cdCidOld!=0 ? cdCidOld : objeto.getCdCid());
			pstmt.setInt(5, cdCidReferenciaOld!=0 ? cdCidReferenciaOld : objeto.getCdCidReferencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidReferenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CidReferenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCid, int cdCidReferencia) {
		return delete(cdCid, cdCidReferencia, null);
	}

	public static int delete(int cdCid, int cdCidReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_cid_referencia WHERE cd_cid=? AND cd_cid_referencia=?");
			pstmt.setInt(1, cdCid);
			pstmt.setInt(2, cdCidReferencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidReferenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CidReferenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CidReferencia get(int cdCid, int cdCidReferencia) {
		return get(cdCid, cdCidReferencia, null);
	}

	public static CidReferencia get(int cdCid, int cdCidReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_cid_referencia WHERE cd_cid=? AND cd_cid_referencia=?");
			pstmt.setInt(1, cdCid);
			pstmt.setInt(2, cdCidReferencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CidReferencia(rs.getInt("cd_cid"),
						rs.getInt("cd_cid_referencia"),
						rs.getString("id_cid_referencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidReferenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidReferenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_cid_referencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidReferenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidReferenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CidReferencia> getList() {
		return getList(null);
	}

	public static ArrayList<CidReferencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CidReferencia> list = new ArrayList<CidReferencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CidReferencia obj = CidReferenciaDAO.get(rsm.getInt("cd_cid"), rsm.getInt("cd_cid_referencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidReferenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_cid_referencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}