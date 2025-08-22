package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ModeloPneuDAO{

	public static int insert(ModeloPneu objeto) {
		return insert(objeto, null);
	}

	public static int insert(ModeloPneu objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fta_modelo_pneu", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdModelo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_modelo_pneu (cd_modelo,"+
			                                  "nm_modelo,"+
			                                  "nr_aro,"+
			                                  "nr_largura,"+
			                                  "nr_altura,"+
			                                  "qt_vida_util) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmModelo());
			pstmt.setInt(3,objeto.getNrAro());
			pstmt.setString(4,objeto.getNrLargura());
			pstmt.setString(5,objeto.getNrAltura());
			pstmt.setInt(6,objeto.getQtVidaUtil());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloPneuDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloPneuDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ModeloPneu objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ModeloPneu objeto, int cdModeloOld) {
		return update(objeto, cdModeloOld, null);
	}

	public static int update(ModeloPneu objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ModeloPneu objeto, int cdModeloOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_modelo_pneu SET cd_modelo=?,"+
												      		   "nm_modelo=?,"+
												      		   "nr_aro=?,"+
												      		   "nr_largura=?,"+
												      		   "nr_altura=?,"+
												      		   "qt_vida_util=? WHERE cd_modelo=?");
			pstmt.setInt(1,objeto.getCdModelo());
			pstmt.setString(2,objeto.getNmModelo());
			pstmt.setInt(3,objeto.getNrAro());
			pstmt.setString(4,objeto.getNrLargura());
			pstmt.setString(5,objeto.getNrAltura());
			pstmt.setInt(6,objeto.getQtVidaUtil());
			pstmt.setInt(7, cdModeloOld!=0 ? cdModeloOld : objeto.getCdModelo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloPneuDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloPneuDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdModelo) {
		return delete(cdModelo, null);
	}

	public static int delete(int cdModelo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_modelo_pneu WHERE cd_modelo=?");
			pstmt.setInt(1, cdModelo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloPneuDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloPneuDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ModeloPneu get(int cdModelo) {
		return get(cdModelo, null);
	}

	public static ModeloPneu get(int cdModelo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_modelo_pneu WHERE cd_modelo=?");
			pstmt.setInt(1, cdModelo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ModeloPneu(rs.getInt("cd_modelo"),
						rs.getString("nm_modelo"),
						rs.getInt("nr_aro"),
						rs.getString("nr_largura"),
						rs.getString("nr_altura"),
						rs.getInt("qt_vida_util"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloPneuDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloPneuDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_modelo_pneu");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloPneuDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloPneuDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_modelo_pneu", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
