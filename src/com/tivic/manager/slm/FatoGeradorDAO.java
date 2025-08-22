package com.tivic.manager.slm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class FatoGeradorDAO{

	public static int insert(FatoGerador objeto) {
		return insert(objeto, null);
	}

	public static int insert(FatoGerador objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("slm_fato_gerador", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFatoGerador(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO slm_fato_gerador (cd_fato_gerador,"+
			                                  "nm_fato_gerador,"+
			                                  "txt_fato_gerador,"+
			                                  "id_fato_gerador,"+
			                                  "cd_fato_gerador_superior,"+
			                                  "tp_origem,"+
			                                  "tp_fato_gerador,"+
			                                  "tp_causa) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmFatoGerador());
			pstmt.setString(3,objeto.getTxtFatoGerador());
			pstmt.setString(4,objeto.getIdFatoGerador());
			if(objeto.getCdFatoGeradorSuperior()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdFatoGeradorSuperior());
			pstmt.setInt(6,objeto.getTpOrigem());
			pstmt.setInt(7,objeto.getTpFatoGerador());
			pstmt.setInt(8,objeto.getTpCausa());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FatoGeradorDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FatoGeradorDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FatoGerador objeto) {
		return update(objeto, 0, null);
	}

	public static int update(FatoGerador objeto, int cdFatoGeradorOld) {
		return update(objeto, cdFatoGeradorOld, null);
	}

	public static int update(FatoGerador objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(FatoGerador objeto, int cdFatoGeradorOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE slm_fato_gerador SET cd_fato_gerador=?,"+
												      		   "nm_fato_gerador=?,"+
												      		   "txt_fato_gerador=?,"+
												      		   "id_fato_gerador=?,"+
												      		   "cd_fato_gerador_superior=?,"+
												      		   "tp_origem=?,"+
												      		   "tp_fato_gerador=?,"+
												      		   "tp_causa=? WHERE cd_fato_gerador=?");
			pstmt.setInt(1,objeto.getCdFatoGerador());
			pstmt.setString(2,objeto.getNmFatoGerador());
			pstmt.setString(3,objeto.getTxtFatoGerador());
			pstmt.setString(4,objeto.getIdFatoGerador());
			if(objeto.getCdFatoGeradorSuperior()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdFatoGeradorSuperior());
			pstmt.setInt(6,objeto.getTpOrigem());
			pstmt.setInt(7,objeto.getTpFatoGerador());
			pstmt.setInt(8,objeto.getTpCausa());
			pstmt.setInt(9, cdFatoGeradorOld!=0 ? cdFatoGeradorOld : objeto.getCdFatoGerador());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FatoGeradorDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FatoGeradorDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFatoGerador) {
		return delete(cdFatoGerador, null);
	}

	public static int delete(int cdFatoGerador, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM slm_fato_gerador WHERE cd_fato_gerador=?");
			pstmt.setInt(1, cdFatoGerador);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FatoGeradorDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FatoGeradorDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FatoGerador get(int cdFatoGerador) {
		return get(cdFatoGerador, null);
	}

	public static FatoGerador get(int cdFatoGerador, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM slm_fato_gerador WHERE cd_fato_gerador=?");
			pstmt.setInt(1, cdFatoGerador);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FatoGerador(rs.getInt("cd_fato_gerador"),
						rs.getString("nm_fato_gerador"),
						rs.getString("txt_fato_gerador"),
						rs.getString("id_fato_gerador"),
						rs.getInt("cd_fato_gerador_superior"),
						rs.getInt("tp_origem"),
						rs.getInt("tp_fato_gerador"),
						rs.getInt("tp_causa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FatoGeradorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FatoGeradorDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM slm_fato_gerador");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FatoGeradorDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FatoGeradorDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<FatoGerador> getList() {
		return getList(null);
	}

	public static ArrayList<FatoGerador> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<FatoGerador> list = new ArrayList<FatoGerador>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				FatoGerador obj = FatoGeradorDAO.get(rsm.getInt("cd_fato_gerador"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FatoGeradorDAO.getList: " + e);
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
		return Search.find("SELECT * FROM slm_fato_gerador", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
