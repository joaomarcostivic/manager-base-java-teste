package com.tivic.manager.ord;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ModalidadeDAO{

	public static int insert(Modalidade objeto) {
		return insert(objeto, null);
	}

	public static int insert(Modalidade objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ord_modalidade", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdModalidade(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ord_modalidade (cd_modalidade,"+
			                                  "nm_modalidade,"+
			                                  "id_modalidade,"+
			                                  "lg_contrato) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmModalidade());
			pstmt.setString(3,objeto.getIdModalidade());
			pstmt.setInt(4,objeto.getLgContrato());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Modalidade objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Modalidade objeto, int cdModalidadeOld) {
		return update(objeto, cdModalidadeOld, null);
	}

	public static int update(Modalidade objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Modalidade objeto, int cdModalidadeOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ord_modalidade SET cd_modalidade=?,"+
												      		   "nm_modalidade=?,"+
												      		   "id_modalidade=?,"+
												      		   "lg_contrato=? WHERE cd_modalidade=?");
			pstmt.setInt(1,objeto.getCdModalidade());
			pstmt.setString(2,objeto.getNmModalidade());
			pstmt.setString(3,objeto.getIdModalidade());
			pstmt.setInt(4,objeto.getLgContrato());
			pstmt.setInt(5, cdModalidadeOld!=0 ? cdModalidadeOld : objeto.getCdModalidade());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdModalidade) {
		return delete(cdModalidade, null);
	}

	public static int delete(int cdModalidade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ord_modalidade WHERE cd_modalidade=?");
			pstmt.setInt(1, cdModalidade);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Modalidade get(int cdModalidade) {
		return get(cdModalidade, null);
	}

	public static Modalidade get(int cdModalidade, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ord_modalidade WHERE cd_modalidade=?");
			pstmt.setInt(1, cdModalidade);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Modalidade(rs.getInt("cd_modalidade"),
						rs.getString("nm_modalidade"),
						rs.getString("id_modalidade"),
						rs.getInt("lg_contrato"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ord_modalidade");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Modalidade> getList() {
		return getList(null);
	}

	public static ArrayList<Modalidade> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Modalidade> list = new ArrayList<Modalidade>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Modalidade obj = ModalidadeDAO.get(rsm.getInt("cd_modalidade"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ord_modalidade", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
