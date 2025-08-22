package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class NaturezaJuridicaDAO{

	public static int insert(NaturezaJuridica objeto) {
		return insert(objeto, null);
	}

	public static int insert(NaturezaJuridica objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_natureza_juridica", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdNaturezaJuridica(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_natureza_juridica (cd_natureza_juridica,"+
			                                  "cd_natureza_superior,"+
			                                  "nm_natureza_juridica,"+
			                                  "id_natureza_juridica,"+
			                                  "nr_natureza_juridica) VALUES (?, ?, ?, ?,?)");
			pstmt.setInt(1, code);
			if(objeto.getCdNaturezaSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdNaturezaSuperior());
			pstmt.setString(3,objeto.getNmNaturezaJuridica());
			pstmt.setString(4,objeto.getIdNaturezaJuridica());
			pstmt.setString(5,objeto.getNrNaturezaJuridica());
			
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NaturezaJuridicaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NaturezaJuridicaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(NaturezaJuridica objeto) {
		return update(objeto, 0, null);
	}

	public static int update(NaturezaJuridica objeto, int cdNaturezaJuridicaOld) {
		return update(objeto, cdNaturezaJuridicaOld, null);
	}

	public static int update(NaturezaJuridica objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(NaturezaJuridica objeto, int cdNaturezaJuridicaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_natureza_juridica SET cd_natureza_juridica=?,"+
												      		   "cd_natureza_superior=?,"+
												      		   "nm_natureza_juridica=?,"+
												      		   "id_natureza_juridica=?," +
												      		   "nr_natureza_juridica=? WHERE cd_natureza_juridica=?");
			pstmt.setInt(1,objeto.getCdNaturezaJuridica());
			if(objeto.getCdNaturezaSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdNaturezaSuperior());
			pstmt.setString(3,objeto.getNmNaturezaJuridica());
			pstmt.setString(4,objeto.getIdNaturezaJuridica());
			pstmt.setString(5,objeto.getIdNaturezaJuridica());
			pstmt.setInt(6, cdNaturezaJuridicaOld!=0 ? cdNaturezaJuridicaOld : objeto.getCdNaturezaJuridica());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NaturezaJuridicaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NaturezaJuridicaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdNaturezaJuridica) {
		return delete(cdNaturezaJuridica, null);
	}

	public static int delete(int cdNaturezaJuridica, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_natureza_juridica WHERE cd_natureza_juridica=?");
			pstmt.setInt(1, cdNaturezaJuridica);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NaturezaJuridicaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NaturezaJuridicaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static NaturezaJuridica get(int cdNaturezaJuridica) {
		return get(cdNaturezaJuridica, null);
	}

	public static NaturezaJuridica get(int cdNaturezaJuridica, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_natureza_juridica WHERE cd_natureza_juridica=?");
			pstmt.setInt(1, cdNaturezaJuridica);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new NaturezaJuridica(rs.getInt("cd_natureza_juridica"),
						rs.getInt("cd_natureza_superior"),
						rs.getString("nm_natureza_juridica"),
						rs.getString("id_natureza_juridica"),rs.getString("nr_natureza_juridica"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NaturezaJuridicaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NaturezaJuridicaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_natureza_juridica");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NaturezaJuridicaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NaturezaJuridicaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator>  criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator>  criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_natureza_juridica", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
