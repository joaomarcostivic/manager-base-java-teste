package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ModalidadeEducarteDAO{

	public static int insert(ModalidadeEducarte objeto) {
		return insert(objeto, null);
	}

	public static int insert(ModalidadeEducarte objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_modalidade_educarte", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdModalidade(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_modalidade_educarte (cd_modalidade,"+
			                                  "nm_modalidade,"+
			                                  "id_modalidade,"+
			                                  "st_modalidade,"+
			                                  "txt_descricao) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmModalidade());
			pstmt.setString(3,objeto.getIdModalidade());
			pstmt.setInt(4,objeto.getStModalidade());
			pstmt.setString(5,objeto.getTxtDescricao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeEducarteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeEducarteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ModalidadeEducarte objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ModalidadeEducarte objeto, int cdModalidadeOld) {
		return update(objeto, cdModalidadeOld, null);
	}

	public static int update(ModalidadeEducarte objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ModalidadeEducarte objeto, int cdModalidadeOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_modalidade_educarte SET cd_modalidade=?,"+
												      		   "nm_modalidade=?,"+
												      		   "id_modalidade=?,"+
												      		   "st_modalidade=?,"+
												      		   "txt_descricao=? WHERE cd_modalidade=?");
			pstmt.setInt(1,objeto.getCdModalidade());
			pstmt.setString(2,objeto.getNmModalidade());
			pstmt.setString(3,objeto.getIdModalidade());
			pstmt.setInt(4,objeto.getStModalidade());
			pstmt.setString(5,objeto.getTxtDescricao());
			pstmt.setInt(6, cdModalidadeOld!=0 ? cdModalidadeOld : objeto.getCdModalidade());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeEducarteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeEducarteDAO.update: " +  e);
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
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_modalidade_educarte WHERE cd_modalidade=?");
			pstmt.setInt(1, cdModalidade);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeEducarteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeEducarteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ModalidadeEducarte get(int cdModalidade) {
		return get(cdModalidade, null);
	}

	public static ModalidadeEducarte get(int cdModalidade, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_modalidade_educarte WHERE cd_modalidade=?");
			pstmt.setInt(1, cdModalidade);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ModalidadeEducarte(rs.getInt("cd_modalidade"),
						rs.getString("nm_modalidade"),
						rs.getString("id_modalidade"),
						rs.getInt("st_modalidade"),
						rs.getString("txt_descricao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeEducarteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeEducarteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_modalidade_educarte");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeEducarteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeEducarteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ModalidadeEducarte> getList() {
		return getList(null);
	}

	public static ArrayList<ModalidadeEducarte> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ModalidadeEducarte> list = new ArrayList<ModalidadeEducarte>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ModalidadeEducarte obj = ModalidadeEducarteDAO.get(rsm.getInt("cd_modalidade"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeEducarteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_modalidade_educarte", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}