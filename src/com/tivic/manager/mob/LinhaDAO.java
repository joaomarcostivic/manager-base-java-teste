package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class LinhaDAO{

	public static int insert(Linha objeto) {
		return insert(objeto, null);
	}

	public static int insert(Linha objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_linha", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLinha(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_linha (cd_linha,"+
			                                  "cd_concessao,"+
			                                  "nr_linha,"+
			                                  "id_linha,"+
			                                  "tp_linha,"+
			                                  "qt_ciclos,"+
			                                  "st_linha) VALUES (?, ?, ?, ?, ?, ?,?)");
			pstmt.setInt(1, code);
			if(objeto.getCdConcessao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConcessao());
			pstmt.setString(3,objeto.getNrLinha());
			pstmt.setString(4,objeto.getIdLinha());
			pstmt.setInt(5,objeto.getTpLinha());
			pstmt.setInt(6,objeto.getQtCiclos());
			pstmt.setInt(7,objeto.getStLinha());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Linha objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Linha objeto, int cdLinhaOld) {
		return update(objeto, cdLinhaOld, null);
	}

	public static int update(Linha objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Linha objeto, int cdLinhaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_linha SET cd_linha=?,"+
												      		   "cd_concessao=?,"+
												      		   "nr_linha=?,"+
												      		   "id_linha=?,"+
												      		   "tp_linha=?,"+
												      		   "qt_ciclos=?,"+
												      		   "st_linha=? WHERE cd_linha=?");
			pstmt.setInt(1,objeto.getCdLinha());
			if(objeto.getCdConcessao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConcessao());
			pstmt.setString(3,objeto.getNrLinha());
			pstmt.setString(4,objeto.getIdLinha());
			pstmt.setInt(5,objeto.getTpLinha());
			pstmt.setInt(6,objeto.getQtCiclos());
			pstmt.setInt(7,objeto.getStLinha());
			pstmt.setInt(8, cdLinhaOld!=0 ? cdLinhaOld : objeto.getCdLinha());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLinha) {
		return delete(cdLinha, null);
	}

	public static int delete(int cdLinha, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_linha WHERE cd_linha=?");
			pstmt.setInt(1, cdLinha);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Linha get(int cdLinha) {
		return get(cdLinha, null);
	}

	public static Linha get(int cdLinha, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_linha WHERE cd_linha=?");
			pstmt.setInt(1, cdLinha);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Linha(rs.getInt("cd_linha"),
						rs.getInt("cd_concessao"),
						rs.getString("nr_linha"),
						rs.getString("id_linha"),
						rs.getInt("tp_linha"),
						rs.getInt("qt_ciclos"),
						rs.getInt("st_linha"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_linha");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Linha> getList() {
		return getList(null);
	}

	public static ArrayList<Linha> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Linha> list = new ArrayList<Linha>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Linha obj = LinhaDAO.get(rsm.getInt("cd_linha"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_linha", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
