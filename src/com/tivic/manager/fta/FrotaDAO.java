package com.tivic.manager.fta;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class FrotaDAO{

	public static int insert(Frota objeto) {
		return insert(objeto, null);
	}

	public static int insert(Frota objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fta_frota", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFrota(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_frota (cd_frota,"+
			                                  "nm_frota,"+
			                                  "id_frota,"+
			                                  "txt_observacao,"+
			                                  "st_frota,"+
			                                  "cd_responsavel,"+
			                                  "cd_proprietario,"+
			                                  "tp_frota) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmFrota());
			pstmt.setString(3,objeto.getIdFrota());
			pstmt.setString(4,objeto.getTxtObservacao());
			pstmt.setInt(5,objeto.getStFrota());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdResponsavel());
			if(objeto.getCdProprietario()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdProprietario());
			pstmt.setInt(8,objeto.getTpFrota());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FrotaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FrotaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Frota objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Frota objeto, int cdFrotaOld) {
		return update(objeto, cdFrotaOld, null);
	}

	public static int update(Frota objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Frota objeto, int cdFrotaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_frota SET cd_frota=?,"+
												      		   "nm_frota=?,"+
												      		   "id_frota=?,"+
												      		   "txt_observacao=?,"+
												      		   "st_frota=?,"+
												      		   "cd_responsavel=?,"+
												      		   "cd_proprietario=?,"+
												      		   "tp_frota=? WHERE cd_frota=?");
			pstmt.setInt(1,objeto.getCdFrota());
			pstmt.setString(2,objeto.getNmFrota());
			pstmt.setString(3,objeto.getIdFrota());
			pstmt.setString(4,objeto.getTxtObservacao());
			pstmt.setInt(5,objeto.getStFrota());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdResponsavel());
			if(objeto.getCdProprietario()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdProprietario());
			pstmt.setInt(8,objeto.getTpFrota());
			pstmt.setInt(9, cdFrotaOld!=0 ? cdFrotaOld : objeto.getCdFrota());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FrotaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FrotaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFrota) {
		return delete(cdFrota, null);
	}

	public static int delete(int cdFrota, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_frota WHERE cd_frota=?");
			pstmt.setInt(1, cdFrota);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FrotaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FrotaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Frota get(int cdFrota) {
		return get(cdFrota, null);
	}

	public static Frota get(int cdFrota, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_frota WHERE cd_frota=?");
			pstmt.setInt(1, cdFrota);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Frota(rs.getInt("cd_frota"),
						rs.getString("nm_frota"),
						rs.getString("id_frota"),
						rs.getString("txt_observacao"),
						rs.getInt("st_frota"),
						rs.getInt("cd_responsavel"),
						rs.getInt("cd_proprietario"),
						rs.getInt("tp_frota"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FrotaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FrotaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_frota");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FrotaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FrotaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Frota> getList() {
		return getList(null);
	}

	public static ArrayList<Frota> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Frota> list = new ArrayList<Frota>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Frota obj = FrotaDAO.get(rsm.getInt("cd_frota"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FrotaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM fta_frota", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
