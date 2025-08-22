package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class MoedaDAO{

	public static int insert(Moeda objeto) {
		return insert(objeto, null);
	}

	public static int insert(Moeda objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_moeda", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMoeda(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_moeda (cd_moeda,"+
			                                  "cd_indicador,"+
			                                  "nm_moeda,"+
			                                  "sg_moeda,"+
			                                  "id_moeda,"+
			                                  "lg_ativo) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdIndicador()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdIndicador());
			pstmt.setString(3,objeto.getNmMoeda());
			pstmt.setString(4,objeto.getSgMoeda());
			pstmt.setString(5,objeto.getIdMoeda());
			pstmt.setInt(6,objeto.getLgAtivo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MoedaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MoedaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Moeda objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Moeda objeto, int cdMoedaOld) {
		return update(objeto, cdMoedaOld, null);
	}

	public static int update(Moeda objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Moeda objeto, int cdMoedaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_moeda SET cd_moeda=?,"+
												      		   "cd_indicador=?,"+
												      		   "nm_moeda=?,"+
												      		   "sg_moeda=?,"+
												      		   "id_moeda=?,"+
												      		   "lg_ativo=? WHERE cd_moeda=?");
			pstmt.setInt(1,objeto.getCdMoeda());
			if(objeto.getCdIndicador()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdIndicador());
			pstmt.setString(3,objeto.getNmMoeda());
			pstmt.setString(4,objeto.getSgMoeda());
			pstmt.setString(5,objeto.getIdMoeda());
			pstmt.setInt(6,objeto.getLgAtivo());
			pstmt.setInt(7, cdMoedaOld!=0 ? cdMoedaOld : objeto.getCdMoeda());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MoedaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MoedaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMoeda) {
		return delete(cdMoeda, null);
	}

	public static int delete(int cdMoeda, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_moeda WHERE cd_moeda=?");
			pstmt.setInt(1, cdMoeda);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MoedaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MoedaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Moeda get(int cdMoeda) {
		return get(cdMoeda, null);
	}

	public static Moeda get(int cdMoeda, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_moeda WHERE cd_moeda=?");
			pstmt.setInt(1, cdMoeda);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Moeda(rs.getInt("cd_moeda"),
						rs.getInt("cd_indicador"),
						rs.getString("nm_moeda"),
						rs.getString("sg_moeda"),
						rs.getString("id_moeda"),
						rs.getInt("lg_ativo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MoedaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MoedaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_moeda");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MoedaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MoedaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Moeda> getList() {
		return getList(null);
	}

	public static ArrayList<Moeda> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Moeda> list = new ArrayList<Moeda>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Moeda obj = MoedaDAO.get(rsm.getInt("cd_moeda"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MoedaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_moeda", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}