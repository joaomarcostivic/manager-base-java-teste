package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class BoatDeclaranteDAO{

	public static int insert(BoatDeclarante objeto) {
		return insert(objeto, null);
	}

	public static int insert(BoatDeclarante objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_boat_declarante (cd_boat,"+
			                                  "cd_declarante,"+
			                                  "ds_declaracao,"+
			                                  "tp_relacao) VALUES (?, ?, ?, ?)");
			if(objeto.getCdBoat()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdBoat());
			if(objeto.getCdDeclarante()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDeclarante());
			pstmt.setString(3,objeto.getDsDeclaracao());
			pstmt.setInt(4,objeto.getTpRelacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(BoatDeclarante objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(BoatDeclarante objeto, int cdBoatOld, int cdDeclaranteOld) {
		return update(objeto, cdBoatOld, cdDeclaranteOld, null);
	}

	public static int update(BoatDeclarante objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(BoatDeclarante objeto, int cdBoatOld, int cdDeclaranteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_boat_declarante SET cd_boat=?,"+
												      		   "cd_declarante=?,"+
												      		   "ds_declaracao=?,"+
												      		   "tp_relacao=? WHERE cd_boat=? AND cd_declarante=?");
			pstmt.setInt(1,objeto.getCdBoat());
			pstmt.setInt(2,objeto.getCdDeclarante());
			pstmt.setString(3,objeto.getDsDeclaracao());
			pstmt.setInt(4,objeto.getTpRelacao());
			pstmt.setInt(5, cdBoatOld!=0 ? cdBoatOld : objeto.getCdBoat());
			pstmt.setInt(6, cdDeclaranteOld!=0 ? cdDeclaranteOld : objeto.getCdDeclarante());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdBoat, int cdDeclarante) {
		return delete(cdBoat, cdDeclarante, null);
	}

	public static int delete(int cdBoat, int cdDeclarante, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_boat_declarante WHERE cd_boat=? AND cd_declarante=?");
			pstmt.setInt(1, cdBoat);
			pstmt.setInt(2, cdDeclarante);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static BoatDeclarante get(int cdBoat, int cdDeclarante) {
		return get(cdBoat, cdDeclarante, null);
	}

	public static BoatDeclarante get(int cdBoat, int cdDeclarante, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_declarante WHERE cd_boat=? AND cd_declarante=?");
			pstmt.setInt(1, cdBoat);
			pstmt.setInt(2, cdDeclarante);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new BoatDeclarante(rs.getInt("cd_boat"),
						rs.getInt("cd_declarante"),
						rs.getString("ds_declaracao"),
						rs.getInt("tp_relacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_declarante");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<BoatDeclarante> getList() {
		return getList(null);
	}

	public static ArrayList<BoatDeclarante> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<BoatDeclarante> list = new ArrayList<BoatDeclarante>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				BoatDeclarante obj = BoatDeclaranteDAO.get(rsm.getInt("cd_boat"), rsm.getInt("cd_declarante"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_boat_declarante", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
