package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.manager.grl.banco.TipoBancoConvenioEnum;
import com.tivic.sol.connection.Conexao;

public class BancoDAO{

	public static int insert(Banco objeto) {
		return insert(objeto, null);
	}

	public static int insert(Banco objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_banco", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdBanco(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_banco (cd_banco,"+
											                    "nr_banco,"+
											                    "nm_banco,"+
											                    "id_banco,"+
											                    "nm_url,"+
											                    "banco_conveniado) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNrBanco());
			pstmt.setString(3,objeto.getNmBanco());
			pstmt.setString(4,objeto.getIdBanco());
			pstmt.setString(5,objeto.getNmUrl());
			pstmt.setInt(6,objeto.getBancoConveniado() ? TipoBancoConvenioEnum.COM_CONVENIO.getKey() : TipoBancoConvenioEnum.SEM_CONVENIO.getKey());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BancoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BancoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Banco objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Banco objeto, int cdBancoOld) {
		return update(objeto, cdBancoOld, null);
	}

	public static int update(Banco objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Banco objeto, int cdBancoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_banco SET cd_banco=?,"+
												      		   "nr_banco=?,"+
												      		   "nm_banco=?,"+
												      		   "id_banco=?,"+
												      		   "nm_url=?,"+
												      		   "banco_conveniado=? WHERE cd_banco=?");
			pstmt.setInt(1,objeto.getCdBanco());
			pstmt.setString(2,objeto.getNrBanco());
			pstmt.setString(3,objeto.getNmBanco());
			pstmt.setString(4,objeto.getIdBanco());
			pstmt.setString(5,objeto.getNmUrl());
			pstmt.setInt(6,objeto.getBancoConveniado() ? TipoBancoConvenioEnum.COM_CONVENIO.getKey() : TipoBancoConvenioEnum.SEM_CONVENIO.getKey());
			pstmt.setInt(7,objeto.getCdBanco());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BancoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BancoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdBanco) {
		return delete(cdBanco, null);
	}

	public static int delete(int cdBanco, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_banco WHERE cd_banco=?");
			pstmt.setInt(1, cdBanco);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BancoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BancoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Banco get(int cdBanco) {
		return get(cdBanco, null);
	}

	public static Banco get(int cdBanco, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_banco WHERE cd_banco=?");
			pstmt.setInt(1, cdBanco);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Banco(rs.getInt("cd_banco"),
						rs.getString("nr_banco"),
						rs.getString("nm_banco"),
						rs.getString("id_banco"),
						rs.getString("nm_url"),
						rs.getBoolean("banco_conveniado"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BancoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BancoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_banco");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BancoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BancoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_banco", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
