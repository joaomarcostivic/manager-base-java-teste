package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CidDAO{

	public static int insert(Cid objeto) {
		return insert(objeto, null);
	}

	public static int insert(Cid objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_cid", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCid(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_cid (cd_cid,"+
			                                  "nm_cid,"+
			                                  "id_cid,"+
			                                  "cd_cid_superior,"+
			                                  "tp_classificacao,"+
			                                  "tp_sexo,"+
			                                  "lg_nao_causa_obito) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmCid());
			pstmt.setString(3,objeto.getIdCid());
			if(objeto.getCdCidSuperior()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCidSuperior());
			pstmt.setInt(5,objeto.getTpClassificacao());
			pstmt.setInt(6,objeto.getTpSexo());
			pstmt.setInt(7,objeto.getLgNaoCausaObito());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CidDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Cid objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Cid objeto, int cdCidOld) {
		return update(objeto, cdCidOld, null);
	}

	public static int update(Cid objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Cid objeto, int cdCidOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_cid SET cd_cid=?,"+
												      		   "nm_cid=?,"+
												      		   "id_cid=?,"+
												      		   "cd_cid_superior=?,"+
												      		   "tp_classificacao=?,"+
												      		   "tp_sexo=?,"+
												      		   "lg_nao_causa_obito=? WHERE cd_cid=?");
			pstmt.setInt(1,objeto.getCdCid());
			pstmt.setString(2,objeto.getNmCid());
			pstmt.setString(3,objeto.getIdCid());
			if(objeto.getCdCidSuperior()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCidSuperior());
			pstmt.setInt(5,objeto.getTpClassificacao());
			pstmt.setInt(6,objeto.getTpSexo());
			pstmt.setInt(7,objeto.getLgNaoCausaObito());
			pstmt.setInt(8, cdCidOld!=0 ? cdCidOld : objeto.getCdCid());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CidDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCid) {
		return delete(cdCid, null);
	}

	public static int delete(int cdCid, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_cid WHERE cd_cid=?");
			pstmt.setInt(1, cdCid);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CidDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Cid get(int cdCid) {
		return get(cdCid, null);
	}

	public static Cid get(int cdCid, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_cid WHERE cd_cid=?");
			pstmt.setInt(1, cdCid);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Cid(rs.getInt("cd_cid"),
						rs.getString("nm_cid"),
						rs.getString("id_cid"),
						rs.getInt("cd_cid_superior"),
						rs.getInt("tp_classificacao"),
						rs.getInt("tp_sexo"),
						rs.getInt("lg_nao_causa_obito"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_cid");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Cid> getList() {
		return getList(null);
	}

	public static ArrayList<Cid> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Cid> list = new ArrayList<Cid>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Cid obj = CidDAO.get(rsm.getInt("cd_cid"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_cid", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}