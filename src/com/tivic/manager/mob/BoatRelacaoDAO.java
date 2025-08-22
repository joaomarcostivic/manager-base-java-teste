package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class BoatRelacaoDAO{

	public static int insert(BoatRelacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(BoatRelacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_boat_relacao (cd_boat,"+
			                                  "cd_boat_relacao) VALUES (?, ?)");
			if(objeto.getCdBoat()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdBoat());
			if(objeto.getCdBoatRelacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdBoatRelacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatRelacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatRelacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(BoatRelacao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(BoatRelacao objeto, int cdBoatOld, int cdBoatRelacaoOld) {
		return update(objeto, cdBoatOld, cdBoatRelacaoOld, null);
	}

	public static int update(BoatRelacao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(BoatRelacao objeto, int cdBoatOld, int cdBoatRelacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_boat_relacao SET cd_boat=?,"+
												      		   "cd_boat_relacao=? WHERE cd_boat=? AND cd_boat_relacao=?");
			pstmt.setInt(1,objeto.getCdBoat());
			pstmt.setInt(2,objeto.getCdBoatRelacao());
			pstmt.setInt(3, cdBoatOld!=0 ? cdBoatOld : objeto.getCdBoat());
			pstmt.setInt(4, cdBoatRelacaoOld!=0 ? cdBoatRelacaoOld : objeto.getCdBoatRelacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatRelacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatRelacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdBoat, int cdBoatRelacao) {
		return delete(cdBoat, cdBoatRelacao, null);
	}

	public static int delete(int cdBoat, int cdBoatRelacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_boat_relacao WHERE cd_boat=? AND cd_boat_relacao=?");
			pstmt.setInt(1, cdBoat);
			pstmt.setInt(2, cdBoatRelacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatRelacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatRelacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static BoatRelacao get(int cdBoat, int cdBoatRelacao) {
		return get(cdBoat, cdBoatRelacao, null);
	}

	public static BoatRelacao get(int cdBoat, int cdBoatRelacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_relacao WHERE cd_boat=? AND cd_boat_relacao=?");
			pstmt.setInt(1, cdBoat);
			pstmt.setInt(2, cdBoatRelacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new BoatRelacao(rs.getInt("cd_boat"),
						rs.getInt("cd_boat_relacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatRelacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatRelacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_relacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatRelacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatRelacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<BoatRelacao> getList() {
		return getList(null);
	}

	public static ArrayList<BoatRelacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<BoatRelacao> list = new ArrayList<BoatRelacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				BoatRelacao obj = BoatRelacaoDAO.get(rsm.getInt("cd_boat"), rsm.getInt("cd_boat_relacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatRelacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_boat_relacao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
