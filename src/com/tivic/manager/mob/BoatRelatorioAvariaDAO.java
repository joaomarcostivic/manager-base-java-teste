package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class BoatRelatorioAvariaDAO{

	public static int insert(BoatRelatorioAvaria objeto) {
		return insert(objeto, null);
	}

	public static int insert(BoatRelatorioAvaria objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_boat_relatorio_avaria (cd_vistoria,"+
			                                  "cd_veiculo,"+
			                                  "cd_boat) VALUES (?, ?, ?)");
			if(objeto.getCdVistoria()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdVistoria());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdVeiculo());
			if(objeto.getCdBoat()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdBoat());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatRelatorioAvariaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatRelatorioAvariaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(BoatRelatorioAvaria objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(BoatRelatorioAvaria objeto, int cdVistoriaOld, int cdVeiculoOld, int cdBoatOld) {
		return update(objeto, cdVistoriaOld, cdVeiculoOld, cdBoatOld, null);
	}

	public static int update(BoatRelatorioAvaria objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(BoatRelatorioAvaria objeto, int cdVistoriaOld, int cdVeiculoOld, int cdBoatOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_boat_relatorio_avaria SET cd_vistoria=?,"+
												      		   "cd_veiculo=?,"+
												      		   "cd_boat=? WHERE cd_vistoria=? AND cd_veiculo=? AND cd_boat=?");
			pstmt.setInt(1,objeto.getCdVistoria());
			pstmt.setInt(2,objeto.getCdVeiculo());
			pstmt.setInt(3,objeto.getCdBoat());
			pstmt.setInt(4, cdVistoriaOld!=0 ? cdVistoriaOld : objeto.getCdVistoria());
			pstmt.setInt(5, cdVeiculoOld!=0 ? cdVeiculoOld : objeto.getCdVeiculo());
			pstmt.setInt(6, cdBoatOld!=0 ? cdBoatOld : objeto.getCdBoat());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatRelatorioAvariaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatRelatorioAvariaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdVistoria, int cdVeiculo, int cdBoat) {
		return delete(cdVistoria, cdVeiculo, cdBoat, null);
	}

	public static int delete(int cdVistoria, int cdVeiculo, int cdBoat, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_boat_relatorio_avaria WHERE cd_vistoria=? AND cd_veiculo=? AND cd_boat=?");
			pstmt.setInt(1, cdVistoria);
			pstmt.setInt(2, cdVeiculo);
			pstmt.setInt(3, cdBoat);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatRelatorioAvariaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatRelatorioAvariaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static BoatRelatorioAvaria get(int cdVistoria, int cdVeiculo, int cdBoat) {
		return get(cdVistoria, cdVeiculo, cdBoat, null);
	}

	public static BoatRelatorioAvaria get(int cdVistoria, int cdVeiculo, int cdBoat, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_relatorio_avaria WHERE cd_vistoria=? AND cd_veiculo=? AND cd_boat=?");
			pstmt.setInt(1, cdVistoria);
			pstmt.setInt(2, cdVeiculo);
			pstmt.setInt(3, cdBoat);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new BoatRelatorioAvaria(rs.getInt("cd_vistoria"),
						rs.getInt("cd_veiculo"),
						rs.getInt("cd_boat"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatRelatorioAvariaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatRelatorioAvariaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_relatorio_avaria");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatRelatorioAvariaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatRelatorioAvariaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<BoatRelatorioAvaria> getList() {
		return getList(null);
	}

	public static ArrayList<BoatRelatorioAvaria> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<BoatRelatorioAvaria> list = new ArrayList<BoatRelatorioAvaria>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				BoatRelatorioAvaria obj = BoatRelatorioAvariaDAO.get(rsm.getInt("cd_vistoria"), rsm.getInt("cd_veiculo"), rsm.getInt("cd_boat"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatRelatorioAvariaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_boat_relatorio_avaria", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}